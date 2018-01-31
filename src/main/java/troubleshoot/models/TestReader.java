package troubleshoot.models;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.event.handler.EventManager;
import troubleshoot.model.pojo.DeviceInfo;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.util.EdtConvert;
import troubleshoot.views.MessageDialog;
import troubleshoot.views.OptionDialog;

public class TestReader
{
	private final static Logger logger = Logger.getLogger(TestReader.class);
	private static final int DEFAULT_TIME_OUT = 20000;
	private static final int READER_TIME_OUT = 60000;
	
	private static final String NON_CHIP_CARD_INSIDE_READER = "81";
	private static final String CHIP_CARD_INSIDE_READER = "83";
	
	private static TestReader testReader = null;
	
	private ErrorState errorState = ErrorState.NONE;
	private DeviceInfo readerInfo = null;
	private boolean isErrorInResponse;
	private Object monitorForEnable;
	private Object monitorForInsert;
	private Object monitorForTrack;
	private boolean isChipCard;
	
	private MessageDialog insertDialog = null;
	private MessageDialog removeDialog = null;
	
	public String trackOneStatus;
	public String trackTwoStatus;
	public String trackThreeStatus;
	public String chipStatus;
	public boolean isInserted;
	
	public boolean isTestingReader;
	
	private WakeUpState resultState = WakeUpState.NONE;
	private WakeUpState expectedState = WakeUpState.NONE;
	
	public enum ErrorState
	{
		NONE,
		ENABLE_ERROR,
		ENABLE_TIMEOUT,
		READ_TIMEOUT,
		READ_ERROR,
		GENRIC_ERROR;
	}
	
	public enum WakeUpState
	{
		NONE,
		ENABLE,
		INSERT,
		TRACK_DATA;
	}
	
	private TestReader() 
	{
		monitorForEnable = new Object();
		monitorForInsert = new Object();
		monitorForTrack = new Object();
		readerInfo = new DeviceInfo();
		isChipCard = false;
		isTestingReader = false;
		subscribeToEvent();
	}
	
	public static TestReader getInstance()
	{
		if(testReader == null)
		{
			testReader = new TestReader();
		}
		return testReader;
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}
	
	public DeviceInfo getReaderInfo()
	{
		return readerInfo;
	}
	
	public ErrorState getErrorState()
	{
		return errorState;
	}
	
	public boolean startTest()
	{
		isTestingReader = true;
		isChipCard = false;
		isInserted = false;
		errorState = ErrorState.NONE;
		expectedState = WakeUpState.NONE;
		resultState = WakeUpState.NONE;
		trackOneStatus = "";
		trackTwoStatus = "";
		trackThreeStatus = "";
		chipStatus = "";
		
		if(DeviceInformation.getInstance().getInformation())
		{
			if( TroubleshootController.cardReaderInfo.getStatus().getDismount().equals(NON_CHIP_CARD_INSIDE_READER) 
				|| TroubleshootController.cardReaderInfo.getStatus().getDismount().equals(CHIP_CARD_INSIDE_READER) ) 
			{
				if(sendCardReaderEnableRequest(true))
				{
					logger.info("Waiting for Track Data response");
					expectedState = WakeUpState.TRACK_DATA;
					if(!waitForResponse(DEFAULT_TIME_OUT, monitorForTrack) && !isErrorInResponse)
					{
						if(!isChipCard)
						{
							chipStatus = "NA";
						}
						
						return true;
					}
					else
					{
						logger.error("CR Track Data : Timeout or Error in response");
					}
				}
			}
			else
			{
			
				if(sendCardReaderEnableRequest(true))
				{
					if(insertCard())
					{
						expectedState = WakeUpState.TRACK_DATA;
						logger.info("Waiting for Track Data response or ATR if Chip Card");
						if(!waitForResponse(DEFAULT_TIME_OUT, monitorForTrack) && !isErrorInResponse)
						{
							if(isChipCard)
							{
								logger.info("Waiting for Track Data response");
								expectedState = WakeUpState.TRACK_DATA;
								if(!waitForResponse(DEFAULT_TIME_OUT, monitorForTrack) && !isErrorInResponse)
								{
									return true;
								}
								else
								{
									logger.error("CR Track Data : Timeout or Error in response");
								}
							}
							else
							{
								chipStatus = "NA";
								return true;
							}
						}
						else
						{
							logger.error("CR Track Data : Timeout or Error in response");
						}
					}
				}
				else // NO
				{
					TroubleshootController.showMessageDlg("Test Failed\nFailed to enable Card Reader", "Test Card Reader", JOptionPane.ERROR_MESSAGE);
					errorState = ErrorState.GENRIC_ERROR;
				}
			}
		}
		return false;
	}
	
	private boolean insertCard()
	{
		showInsertMsg();
		expectedState = WakeUpState.INSERT;
		if(!waitForResponse(READER_TIME_OUT, monitorForInsert) && !isErrorInResponse)
		{
			logger.info("Card Inserted");
			return true;
		}
		else
		{
			insertDialog.dispose();
			int resultCode = TroubleshootController.showOptionDlg("Did you insert a card?", "Test Card Reader", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if( resultCode == 0)
			{
				errorState = ErrorState.READ_ERROR;
				TroubleshootController.showMessageDlg("Card Reader failed to read the card.", "Test Card Reader", JOptionPane.ERROR_MESSAGE);
				TroubleshootController.showMessageDlg("Please replace the Card Reader", "Card Reader Fix", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(resultCode == 1)
			{
				return insertCard();
			}
			else
			{
				logger.info("Card Inserted");
				return true;
			}
		}
		return false;
	}
	
	public boolean sendCardReaderEnableRequest(boolean doWait)
	{
		isErrorInResponse = false;
		logger.info("sending card enable request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getReaderEnableMessage(SpotMessageType.ENABLE_READER));
		if(doWait)
		{
			expectedState = WakeUpState.ENABLE;
			if(!waitForResponse(DEFAULT_TIME_OUT, monitorForEnable) && !isErrorInResponse)
			{
				if(readerInfo.getStatus().getCommunication().equals("01"))
				{
					logger.info("card reader enabled");
					return true;
				}
				else
				{
					errorState = ErrorState.ENABLE_ERROR;
					logger.error("Error while enabling card reader");
				}
			}
			else
			{
				errorState = ErrorState.ENABLE_TIMEOUT;
			}
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void sendChipResetRequest()
	{
		EventManager.getInstance().send(SpotMessages.getInstance().getReaderEnableMessage(SpotMessageType.CHIP_RESET));
	}
	
	public void processSystemStatusResponse(SpotApdu apdu)
	{
		if(isTestingReader)
		{
			if(apdu.getAck().equals("00"))
			{
				int index = 0;
				byte[] data = new byte[apdu.getData().length()/2];
				EdtConvert.hexStringToBytes(data,apdu.getData());
				int numberOfEntries = data[index];
				if(numberOfEntries > 1)
				{
					return;
				}
				index++;
				
				for(int i=0;i<numberOfEntries;i++)
				{
					byte type = data[index];
					index++;
					
					switch(type)
					{
						case 0x10:
							
							byte[] cardReader = getStatus(index, data);
							index += 3;
							readerInfo.getStatus().parse(cardReader);
							String communication = readerInfo.getStatus().getCommunication();
							String dismount = readerInfo.getStatus().getDismount();
							String error = readerInfo.getStatus().getError();
							
							if( communication.equals("01") && dismount.equals("80") && error.equals("00"))
							{
								resultState = WakeUpState.ENABLE;
							}
							else if( communication.equals("01") && dismount.equals("81") && error.equals("00") )
							{
								resultState = WakeUpState.INSERT;
								isInserted = true;
								logger.info("Card Inserted. Show Remove Card message");
								showRemoveMsg();
							}
							else if( communication.equals("01") && dismount.equals("83") && error.equals("00"))
							{
								resultState = WakeUpState.INSERT;
								logger.info("Sending Chip Reset command");
								sendChipResetRequest();
							}
							
							isErrorInResponse = false;
							logger.info("Card Reader status retrieved : "+EdtConvert.bytesToHexString(cardReader));
							wakeUp(resultState);
							break;
					}
				}
			}
			else
			{
				isErrorInResponse = true;
				logger.error("System Status : error in response. error code = "+apdu.getAck());
				if(expectedState == WakeUpState.ENABLE || expectedState == WakeUpState.INSERT)
				{
					wakeUp(expectedState);
				}
			}
		}
	}
	
	
	public void processCRTrackDataResponse(SpotApdu apdu)
	{
		if(isTestingReader)
		{
			if(apdu.getAck().equals("00"))
			{
				int index = 0;
				byte status = 0;
				short len = 0;
				
				byte[] data = new byte[apdu.getData().length()/2];
				EdtConvert.hexStringToBytes(data,apdu.getData());
				int numberOfEntries = data[index];
				index++;
	
				for(int i=0;i<numberOfEntries;i++)
				{
					byte source = data[index];
					index++;
					
					switch(source)
					{
						case 0x01:
							status = data[index];
							index++;
							len = EdtConvert.bytesToShort(true, data, index);
							index = index + len + 2;
							trackOneStatus = getTrackStatus(status);
							logger.info("CR Track Data : TRACK1 retrieved");
							break;
							
						case 0x02:
							status = data[index];
							index++;
							len = EdtConvert.bytesToShort(true, data, index);
							index = index + len + 2;
							trackTwoStatus = getTrackStatus(status);
							logger.info("CR Track Data : TRACK2 retrieved");
							break;
							
						case 0x04:
							status = data[index];
							index++;
							len = EdtConvert.bytesToShort(true, data, index);
							index = index + len + 2;
							trackThreeStatus = getTrackStatus(status);
							logger.info("CR Track Data : TRACK3 retrieved");
							logger.info("Closing Remove Card dialog");
							removeDialog.dispose();
							break;
							
						case 0x08:
							closeOptionDialog();
							status = data[index];
							index++;
							len = EdtConvert.bytesToShort(true, data, index);
							index = index + len + 2;
							chipStatus = getTrackStatus(status);
							isChipCard = true;
							logger.info("CR Track Data : ATR retrieved");
							sendCardReaderEnableRequest(false);
							break;
					}
				}
				wakeUp(WakeUpState.TRACK_DATA);
			}
			else
			{
				isErrorInResponse = true;
				logger.error("CR Track Data : error in response. error code = "+apdu.getAck());
				wakeUp(WakeUpState.TRACK_DATA);
			}
		}
	}
	
	private void showInsertMsg()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				insertDialog = TroubleshootController.getMessageDialog("Please insert a test card in Card Reader", "Test Card Reader", JOptionPane.INFORMATION_MESSAGE);
				insertDialog.showDialog(true);
			}
		}).start();
	}
	
	private void showRemoveMsg()
	{
		if(insertDialog != null)
		{
			insertDialog.dispose();
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				closeOptionDialog();
				removeDialog = TroubleshootController.getMessageDialog("Please remove a test card from Card Reader", "Test Card Reader", JOptionPane.INFORMATION_MESSAGE);
				removeDialog.setVisible(true);
			}
		}).start();
	}
	
	public void disposeReaderDialogs()
	{
		if(insertDialog != null)
		{
			insertDialog.dispose();
		}
		
		if(removeDialog != null)
		{
			removeDialog.dispose();
		}
	}
	
	private void closeOptionDialog()
	{
		if(OptionDialog.getInstance() != null)
		{
			OptionDialog.getInstance().closeDialog();
		}
	}
	
	private byte[] getStatus(int index, byte[] rawData)
	{
		byte[] info = new byte[3];
		System.arraycopy(rawData, index, info, 0, 3);
		return info;
	}
	
	private String getTrackStatus(byte status)
	{
		if(status == 0)
		{
			return "PASS";
		}
		else if(status == 1)
		{
			return "NOT PRESENT";
		}
		else
		{
			return "FAIL";
		}
	}
	
	private void wakeUp(WakeUpState resultState)
	{
		switch (resultState)
		{
			case ENABLE:
				if(expectedState == WakeUpState.ENABLE)
				{
					synchronized (monitorForEnable) 
					{
						monitorForEnable.notify();
					}
				}
				break;
				
			case INSERT:
				if(expectedState == WakeUpState.INSERT)
				{
					synchronized (monitorForInsert) 
					{
						monitorForInsert.notify();
					}
				}
				break;
				
			case TRACK_DATA:
				if(expectedState == WakeUpState.TRACK_DATA)
				{
					synchronized (monitorForTrack) 
					{
						monitorForTrack.notify();
					}
				}
				break;
			default:
				break;
		}
	}
	
	private boolean waitForResponse(final int timeout, Object monitor)
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
				if((t1-t0) >= (timeout-10)) // sometimes there will be early wake up call by 1-10 milliseconds that why comparing (timeout-10)
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
	
	private final class SocketEventListener implements EventListener
	{

		@Override
		public void onMessage(SocketEvent eventType)
		{
			switch (eventType)
			{
				case Disconnect:
					disposeReaderDialogs();
					break;
				default:
					break;
			}
		}
	}
}
