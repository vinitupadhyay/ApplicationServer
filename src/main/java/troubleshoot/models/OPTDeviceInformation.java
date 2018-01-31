package troubleshoot.models;

import org.apache.log4j.Logger;

import spot.message.models.SpotApdu;
import spot.messages.SpotMessageType;
import spot.messages.SpotMessages;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.event.handler.EventManager;
import troubleshoot.socket.event.EventListener;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;
import troubleshoot.util.EdtConvert;

public class OPTDeviceInformation
{
	private final static Logger logger = Logger.getLogger(OPTDeviceInformation.class);
	private static final int DEFAULT_TIME_OUT = 20000;
	
	// Device
	private static final byte PRINTER = 0x01;
	
	// Software
	private static final byte PR_FWR_VERSION = 0x01;
	
	// Hardware
	private static final byte PR_SERIAL_NUMBER = 0x01;
	private static final byte PR_DEV_TYPE = 0x02;
	private static final byte PR_DEV_MODEL = 0x04;
	
	private static OPTDeviceInformation optDeviceInformation = null;
	
	private boolean isErrorInResponse;
	private Object monitor;
	
	private OPTDeviceInformation()
	{
		monitor = new Object();
		subscribeToEvent();
	}
	
	public static OPTDeviceInformation getInstance()
	{
		if(optDeviceInformation == null)
		{
			optDeviceInformation = new OPTDeviceInformation();
		}
		return optDeviceInformation;
	}
	
	private void subscribeToEvent()
	{
		SocketEventService.getInstance().subscribe(new SocketEventListener());
	}
	
	public boolean getInformation()
	{
		if(getHardwareConfiguration())
		{
			if(getSofwareConfiguration())
			{
				return getSystemStatus();
			}
		}
		return false;
	}
	
	private boolean getSystemStatus()
	{
		isErrorInResponse = false;
		logger.info("Sending OPT System Status Request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getSystemStatusRequestMessage(SpotMessageType.SYSTEM_STATUS, (byte)0x04, (byte)0x01));
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("OPT System Status Info Retrived.");
			return true;
		}
		else
		{
			logger.error("Error while retrieving OPT System Status.");
			return false;
		}
	}
	
	public void processSystemStatusResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			int index = 0;
			byte[] data = new byte[apdu.getData().length()/2];
			EdtConvert.hexStringToBytes(data,apdu.getData());
			int numberOfEntries = data[index];
			index++;
			
			for(int i=0;i<numberOfEntries;i++)
			{
				byte type = data[index];
				index++;
				
				switch(type)
				{
					case PRINTER:
						byte[] printer = getStatus(index, data);
						index += 3;
						TroubleshootController.printerInfo.getStatus().parse(printer);
						isErrorInResponse = false;
						logger.info("Printer Status Retrieved : "+EdtConvert.bytesToHexString(printer));
						break;
				}
			}
			
			wakeUp();
			
		}
		else
		{
			isErrorInResponse = true;
			logger.error("OPT System Status : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	private byte[] getStatus(int index, byte[] rawData)
	{
		byte[] info = new byte[3];
		System.arraycopy(rawData, index, info, 0, 3);
		return info;
	}
	
	private boolean getSofwareConfiguration()
	{
		isErrorInResponse = false;
		logger.info("Sending OPT Software Configuration Request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getSoftwareConfigRequestMessage(SpotMessageType.SOFTWARE_INFO, (byte)0x04, (byte)0x01, new byte[]{0x01} ));
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("OPT Software Configuration Info Retrived.");
			return true;
		}
		else
		{
			logger.error("Error while retrieving OPT Software Configuration.");
			return false;
		}
	}
	
	
	public void processSoftwareConfigurationResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			int index = 0;
			byte[] data = new byte[apdu.getData().length()/2];
			EdtConvert.hexStringToBytes(data,apdu.getData());
			int numberOfEntries = data[index];
			index++;
			
			for(int i=0;i<numberOfEntries;i++)
			{
				byte type = data[index];
				index++;
				
				switch(type)
				{
					case PR_FWR_VERSION:
						byte[] printerFwm = getData(index, data);
						index += (printerFwm.length + 1);
						String sPrinterFwm = new String(printerFwm).trim();
						if(sPrinterFwm.isEmpty())
						{
							sPrinterFwm = "NA";
						}
						TroubleshootController.printerInfo.setFmwVersion(sPrinterFwm);
						isErrorInResponse = false;
						logger.info("Printer firmware Version Retrieved : "+sPrinterFwm);
						break;
				}
			}
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("OPT Soft Config : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	private boolean getHardwareConfiguration()
	{
		isErrorInResponse = false;
		logger.info("Sending OPT Hardware Configuration Request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getHardwareConfigRequestMessage(SpotMessageType.HARDWARE_INFO, (byte)0x04, (byte)0x03, new byte[]{0x01, 0x02, 0x04}));
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("OPT Hardware Configuration Info Retrived.");
			return true;
		}
		else
		{
			logger.error("Error while retrieving OPT Hardware Configuration.");
			return false;
		}
	}
	
	public void processHardwareConfigurationResponse(SpotApdu apdu)
	{
		if(apdu.getAck().equals("00"))
		{
			int index = 0;
			byte[] data = new byte[apdu.getData().length()/2];
			EdtConvert.hexStringToBytes(data,apdu.getData());
			int numberOfEntries = data[index];
			index++;
			
			for(int i=0;i<numberOfEntries;i++)
			{
				byte type = data[index];
				index++;
				
				switch(type)
				{
					case PR_SERIAL_NUMBER:
						
						byte[] printSerial = getData(index, data);
						index += (printSerial.length + 1);
						String sPrintSerial = new String(printSerial).trim();
						if(sPrintSerial.isEmpty())
						{
							sPrintSerial = "NA";
						}
						TroubleshootController.printerInfo.setSerialNumber(sPrintSerial);
						isErrorInResponse = false;
						logger.info("Printer Serial Retrieved : "+sPrintSerial);			
						break;
						
					case PR_DEV_TYPE:
						
						byte[] printerType = getData(index, data);
						index += (printerType.length + 1);
						String sPrinterType = getPrinterType(printerType[0]);
						if(sPrinterType.isEmpty())
						{
							sPrinterType = "NA";
						}
						TroubleshootController.printerInfo.setType(sPrinterType);
						isErrorInResponse = false;
						logger.info("Printer Type Retrieved : "+printerType[0]);
						break;
						
					case PR_DEV_MODEL:
						byte[] printModel = getData(index, data);
						index += (printModel.length + 1);
						String sPrintModel = new String(printModel).trim();
						if(sPrintModel.isEmpty())
						{
							sPrintModel = "NA";
						}
						TroubleshootController.printerInfo.setModel(sPrintModel);
						isErrorInResponse = false;
						logger.info("Printer Model Retrieved : "+sPrintModel);
						break;
				}
			}
			
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("OPT Hard Config : error in response. error code = "+apdu.getAck());
			
			wakeUp();
		}
	}
	
	private String getPrinterType(byte type)
	{
		switch (type)
		{
			case 0x01:
				return "Thermal Printer";
				
			case 0x02:
				return "Dot-matrix Printer";
	
			default:
				return "Unknown";
		}
	}
	
	private byte[] getData(int index, byte[] rawData)
	{
		int len = rawData[index];
		if(len < 0)
		{
			len += 256;
		}
		index++;
		byte[] info = new byte[len];
		System.arraycopy(rawData, index, info, 0, len);
		
		return info;
	}
	
	private boolean waitForResponse()
	{
		boolean isTimeOutError = false;
		synchronized (monitor) 
		{
			try 
			{
				long t0 = System.currentTimeMillis();
				long t1 = 0;
				monitor.wait(DEFAULT_TIME_OUT);
				t1 = System.currentTimeMillis();
				if((t1-t0) >= DEFAULT_TIME_OUT)
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
					TroubleshootController.getInstance().handleSocketDisconnect();
					break;
				default:
					break;
			}
		}
		
	}
}
