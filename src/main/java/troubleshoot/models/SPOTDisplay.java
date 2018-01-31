package troubleshoot.models;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.event.handler.EventManager;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.states.TestStatus;
import troubleshoot.util.EdtConvert;
import troubleshoot.views.PinpadDialog;

public class SPOTDisplay
{
	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	private final static Logger logger = Logger.getLogger(SPOTDisplay.class);

	/** The timeout for a download command answer is set to 60000 milliseconds for UPM */
	private static final long SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS = 60000L;
	
	private static final long SPOT_SHORT_TIMEOUT = 20000L;
	
	private static final String NO_ERROR = "00";
	
	public static final byte SMC_SECURE = 0x49;
	
	public static final byte SMC_NON_SECURE = 0x46;
	
	public static final byte SMC_EXTERNAL = 0x45;
	
	private static SPOTDisplay spotDisplay = null;
	
	public static boolean isClosed = false;
	
	public static boolean isKeyPressed = false;
	
	public static boolean isResetTimer = false;
	
	public TestStatus testStatus;

	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************

	private boolean isProcessing;
	private boolean isErrorInResponse;
	private Object monitor;
	private byte windowId;
	
	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************
	
	public ArrayList<Byte> usedWinIds;

	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************
	
	private SPOTDisplay()
	{
		testStatus = TestStatus.NOT_EXECUTED;
		monitor = new Object();
		subscribeToEvent();
		isProcessing = false;
		usedWinIds = new ArrayList<Byte>();
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}

	// ******************************************************************
	// PUBLIC METHODS (general, getter, setter, interface imp)
	// ******************************************************************
	
	public static SPOTDisplay getInstance()
	{
		if(spotDisplay == null)
		{
			spotDisplay = new SPOTDisplay();
		}
		return spotDisplay;
	}
	
	private void closePinpadDialog()
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				PinpadDialog.getInstance().closeDialog();
			}
		});
	}
	
	public void closeWindow()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				if(isProcessing)
				{
					wakeUp();
				}
				sendInputAbortRequest();
				sendDestroyWindowRequest();
				sendSMCRequest(getPreviousVideoMode());
			}
		}).start();
	}
	
	/**
	 * This function provides the previous video mode
	 * @return previous mode of display frame buffer
	 */
	private byte getPreviousVideoMode()
	{
		String videoStatus = TroubleshootController.systemInfo.getStatus().getDismount();
		if(videoStatus.equals("00"))
		{
			return 0x49;
		}
		else if(videoStatus.equals("01"))
		{
			return 0x45;
		}
		else
		{
			return 0x46;
		}
	}
	
	private boolean isBtnPressed()
	{
		if(TroubleshootController.showOptionDlg("Have you pressed a button on keypad?", "Test UPM", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0)
		{
			return true;
		}
		else // NO
		{
			return false;
		}
	}
	
	public boolean collectUsedWindId()
	{
		isProcessing = true;
		isErrorInResponse = false;
		usedWinIds.clear();
		sendDisplayStatusRequest();
		if(!waitForResponse(SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS) && !isErrorInResponse)
		{
			isProcessing = false;
			return true;
		}
		else
		{
			isProcessing = false;
			logger.error("Display Status : timeout or error in response");
			return false;
		}	
	}
	
	public boolean switchWindow(byte mode)
	{
		isProcessing = true;
		isErrorInResponse = false;
		usedWinIds.clear();
		sendSMCRequest(mode);
		if(!waitForResponse(SPOT_SHORT_TIMEOUT) && !isErrorInResponse)
		{
			isProcessing = false;
			return true;
		}
		else
		{
			isProcessing = false;
			logger.error("SMC : timeout or error in response");
			return false;
		}
	}
	
	public void createWindowFromRes(byte winId, byte fileId)
	{
		windowId = winId;
		sendCreateWindowFromResRequest(winId, fileId);
		sendHideWindowRequest((byte)39);
	}
	
	public void enableInput(final byte winId, final byte tagId, boolean isSend, boolean isStartTimer)
	{
		isProcessing = true;
		isErrorInResponse = false;
		
		if(isSend)
		{
			sendInputEnableRequest(winId, tagId);
		}
		
		if(isStartTimer)
		{
			isResetTimer = true;
			isStartTimer = false;
			PinpadDialog.getInstance().showTimer();
		}
		
		if(!waitForResponse(SPOT_COMMAND_REPLY_TIMEOUT_MILLISECONDS))
		{
			if(!isClosed)
			{
				if(isErrorInResponse)
				{
					PinpadDialog.isBtnOKClicked = true;
					closePinpadDialog();
					TroubleshootController.showMessageDlg("Test Failed.\nFailed to Enable Input Keypad.", "Test UPM", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					enableInput(winId, tagId, true, false);
				}
			}
		}
		else
		{
			logger.error("Input Enable : timeout");
			
			if(!isClosed)
			{
				isResetTimer = false;
				if(isBtnPressed())
				{
					PinpadDialog.isBtnOKClicked = true;
					testStatus = TestStatus.FAIL;
					closePinpadDialog();
					TroubleshootController.showMessageDlg("Test Failed.\nResponse not received when button pressed.", "Test UPM", JOptionPane.ERROR_MESSAGE);
					TroubleshootController.showMessageDlg("Please Replace the UPM.", "UPM Fix", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					enableInput(winId, tagId, true, true);
				}
			}
		}
		isProcessing = false;
	}
	
	private void sendInputAbortRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getInputAbortMessage(SpotMessageType.INPUT_ABORT));
	}
	
	private void sendDestroyWindowRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getDestroyWindowMessage(SpotMessageType.DESTROY_WINDOW, windowId, (byte)0));
	}
	
	private void sendHideWindowRequest(byte tagId)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getHideWindowMessage(SpotMessageType.HIDE_WINDOW, windowId, tagId));
	}
	
	private void sendShowWindowRequest(byte tagId)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getShowWindowMessage(SpotMessageType.SHOW_WINDOW, windowId, tagId, (byte)2));
	}
	
	private void sendInputEnableRequest(byte winId, byte tagId)
	{
		logger.info("sending input enable command");
		EventManager.getInstance().send(SpotMessages.getInstance().getInputEnableMessage(SpotMessageType.INPUT_ENABLE, winId, tagId));
	}
	
	private void sendSMCRequest(byte mode)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getSMCMessage(SpotMessageType.SMC, mode));
	}
	
	private void sendCreateWindowFromResRequest(byte winId, byte fileId)
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getCreateWinFromResMessage(SpotMessageType.CREATE_WINDOW_FROM_RES, winId, fileId));
	}
	
	private void sendDisplayStatusRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getDisplayStatusMessage(SpotMessageType.DISPLAY_STATUS));
	}
	
	public void handleSMCResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals(NO_ERROR))
		{
			isErrorInResponse = false;
		}
		else
		{
			isErrorInResponse = true;
			logger.error("SMC : error in response : code - "+apdu.getAck());
		}
		wakeUp(); // wakes up the wait()
	}
	
	public void handleInputEnableResponse(SpotApdu apdu)
	{
		logger.info("input enable response received");
		if(apdu.getAck().equals(NO_ERROR))
		{
			System.out.println("key pressed true");
			isKeyPressed = true;
			int index = 0;
			byte[] data = EdtConvert.hexStringToByteArray(apdu.getData());
			short inputSize = EdtConvert.bytesToShort(true, data, index);
			index += 2;
			byte[] inputData = Arrays.copyOfRange(data, index, (inputSize+index));
			PinpadDialog.getInstance().setBtnStatus(inputData[0]);
			testStatus = TestStatus.PASS;
		}
		else
		{
			testStatus = TestStatus.FAIL;
			isErrorInResponse = true;
			logger.error("Input Enable : error in response : code - "+apdu.getAck());
		}
		wakeUp(); // wakes up the wait()
	}
	
	public void handleDisplayStatusResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals(NO_ERROR))
		{
			int index = 0;
			byte[] data = EdtConvert.hexStringToByteArray(apdu.getData());
			short numOfWindows = EdtConvert.bytesToShort(true, data, index);
			index += 2;
			for(int i=0; i<numOfWindows; i++)
			{
				byte windId = data[index];
				usedWinIds.add(windId);
				index += 5;
			}
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Display Status : error in response : code - "+apdu.getAck());
		}
		wakeUp(); // wakes up the wait()
	}
	
	private boolean waitForResponse(long timeout)
	{
		boolean isTimeOutError = false;
		synchronized (monitor) 
		{
			try 
			{
				long t0 = System.currentTimeMillis();
				long t1 = 0;
				monitor.wait(timeout);
				t1 = System.currentTimeMillis();
				if((t1-t0) >= timeout)
				{
					isTimeOutError = true;
					logger.error("Response not received : timeout error");
				}
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return isTimeOutError;
	}
	
	private void wakeUp()
	{
		synchronized (monitor) 
		{
			monitor.notify();
		}
	}
	
	private final class  SocketEventListener implements EventListener
	{

		@Override
		public void onMessage(SocketEvent eventType)
		{
			switch (eventType)
			{
				case Disconnect:
					logger.debug("SOCKET_DISCONNECT");
					if(isProcessing)
					{
						isClosed = true;
						wakeUp( );
						closePinpadDialog();
					}
					break;
				default:
					break;
			}
		}
		
	}
}
