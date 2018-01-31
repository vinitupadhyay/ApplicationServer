package troubleshoot.models;

import java.util.Arrays;

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

public class DeviceInformation
{
	private final static Logger logger = Logger.getLogger(DeviceInformation.class);
	private static final int DEFAULT_TIME_OUT = 20000;
	
	// Devices
	private static final byte SYSTEM = 0x01;
	private static final byte SECURITY = 0x02;
	private static final byte DISPLAY = 0x04;
	private static final byte KEYBOARD = 0x08;
	private static final byte CARD_READER = 0x10;
	private static final byte CONTACT_LESS_CARD_READER = 0x40;
	
	// Software
	private static final byte SPOT_SYS_BUILD_VERSION = 0x0F;
	private static final byte CR_FWR_VERSION = 0x10;
	private static final byte TIME_ZONE = 0x11;
	private static final byte SPOT_M7_FW_VERSION = 0x13;
	private static final byte SPOT_SYS_PCI_APPLICATION_VERSION = 0x14;
	
	// Hardware
	private static final byte SERIAL_NUMBER = 0x01;
	private static final byte CR_SERIAL_NUMBER = 0x30;
	private static final byte CR_DEV_TYPE = 0x32;
	private static final byte CR_DEV_MODEL = 0x34;
	private static final byte DISPLAY_HWR_CONFIG = 0x43;
	
	private static DeviceInformation deviceInformation = null;
	
	private boolean isErrorInResponse;
	private Object monitor;
	
	private DeviceInformation()
	{
		monitor = new Object();
		subscribeToEvent();
	}
	
	public static DeviceInformation getInstance()
	{
		if(deviceInformation == null)
		{
			deviceInformation = new DeviceInformation();
		}
		return deviceInformation;
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
	
	public boolean getSystemStatus()
	{
		isErrorInResponse = false;
		logger.info("sending system status request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getSystemStatusRequestMessage(SpotMessageType.SYSTEM_STATUS, (byte)0x01, (byte)0xff));
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("system status info retrived.");
			return true;
		}
		else
		{
			logger.error("error while retrieving system status.");
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
			// Sometimes unsolicited response of system status is received before the actual response so to avoid unsolicited message this check is applied
			if(numberOfEntries < 6)
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
					case SYSTEM:
						
						byte[] system = getStatus(index, data);
						index += 3;
						TroubleshootController.systemInfo.getStatus().parse(system);
						isErrorInResponse = false;
						logger.info("Main status retrieved : "+EdtConvert.bytesToHexString(system));
						break;
						
					case SECURITY:
						
						byte[] securityModule = getStatus(index, data);
						index += 3;
						TroubleshootController.securityInfo.getStatus().parse(securityModule);
						isErrorInResponse = false;
						logger.info("Security Module status retrieved : "+EdtConvert.bytesToHexString(securityModule));
						break;
						
					case DISPLAY:
						
						byte[] display = getStatus(index, data);
						index += 3;
						TroubleshootController.displayInfo.getStatus().parse(display);
						isErrorInResponse = false;
						logger.info("Display status retrieved : "+EdtConvert.bytesToHexString(display));
						break;

					case KEYBOARD:
						
						byte[] keyboard = getStatus(index, data);
						index += 3;
						TroubleshootController.upmInfo.getStatus().parse(keyboard);
						isErrorInResponse = false;
						logger.info("Keyboard status retrieved : "+EdtConvert.bytesToHexString(keyboard));
						break;

					case CARD_READER:
						
						byte[] cardReader = getStatus(index, data);
						index += 3;
						TroubleshootController.cardReaderInfo.getStatus().parse(cardReader);
						isErrorInResponse = false;
						logger.info("Card Reader status retrieved : "+EdtConvert.bytesToHexString(cardReader));
						break;

					case CONTACT_LESS_CARD_READER:
						
						byte[] cless = getStatus(index, data);
						index += 3;
						isErrorInResponse = false;
						logger.info("Cless status retrieved : "+EdtConvert.bytesToHexString(cless));
						break;
				}
			}
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("System Status : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	private byte[] getStatus(int index, byte[] rawData)
	{
		byte[] info = new byte[3];
		System.arraycopy(rawData, index, info, 0, 3);
		return info;
	}
	
	public boolean getSofwareConfiguration()
	{
		isErrorInResponse = false;
		logger.info("sending software configuration request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getSoftwareConfigRequestMessage(SpotMessageType.SOFTWARE_INFO, (byte)0x01, (byte)0x00, new byte[]{0x00} ));
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("software configuration info retrived.");
			return true;
		}
		else
		{
			logger.error("error while retrieving software configuration.");
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
					case SPOT_SYS_BUILD_VERSION:
					
						byte[] buildVer = getData(index, data);
						index += (buildVer.length + 1);
						String sBuildVer = new String(buildVer).trim();
						if(sBuildVer.isEmpty())
						{
							sBuildVer = "NA";
						}
						isErrorInResponse = false;
						logger.info("Build Version retrieved : "+sBuildVer);
						break;
					
					case CR_FWR_VERSION:
						
						byte[] cardFwm = getData(index, data);
						index += (cardFwm.length + 1);
						String sCardFwm = new String(cardFwm).trim();
						if(sCardFwm.isEmpty())
						{
							sCardFwm = "NA";
						}
						TroubleshootController.cardReaderInfo.setFmwVersion(sCardFwm);
						TroubleshootController.upmInfo.setFmwVersion("");
						isErrorInResponse = false;
						logger.info("Card Reader firmware version retrieved : "+sCardFwm);
						break;
						
					case TIME_ZONE:
						
						byte[] timeZone = getData(index, data);
						index += (timeZone.length + 1);
						String sTimeZone = new String(timeZone).trim();
						if(sTimeZone.isEmpty())
						{
							sTimeZone = "NA";
						}
						
						isErrorInResponse = false;
						logger.info("Timezone retrieved : "+sTimeZone);
						break;
						
					case SPOT_M7_FW_VERSION:
						
						byte[] firmVer = getData(index, data);
						index += (firmVer.length + 1);
						String sFirmVer = new String(firmVer).trim();
						if(sFirmVer.isEmpty())
						{
							sFirmVer = "NA";
						}

						isErrorInResponse = false;
						logger.info("Firmware Version retrieved : "+sFirmVer);
						break;
						
					case SPOT_SYS_PCI_APPLICATION_VERSION:
						
						byte[] pciVer = getData(index, data);
						index += (pciVer.length + 1);
						String sPciVer = new String(pciVer).trim();
						if(sPciVer.isEmpty())
						{
							sPciVer = "NA";
						}

						isErrorInResponse = false;
						logger.info("PCI Application Version retrieved : "+sPciVer);
						break;
						
					default :
						
						String typeName = EdtConvert.byteToHexString(type);
						byte[] version = getData(index, data);
						index += (version.length + 1);
						String sVersion = new String(version).trim();
						if(sVersion.isEmpty())
						{
							sVersion = "NA";
						}

						isErrorInResponse = false;
						logger.info("Type "+typeName+" version retrieved : "+sVersion);
				}
			}
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Soft Config : error in response. error code = "+apdu.getAck());
			wakeUp();
		}
	}
	
	public boolean getHardwareConfiguration()
	{
		isErrorInResponse = false;
		logger.info("sending hardware configuration request.");
		EventManager.getInstance().send(SpotMessages.getInstance().getHardwareConfigRequestMessage(SpotMessageType.HARDWARE_INFO, (byte)0x01, (byte)0x05, new byte[]{0x01, 0x30, 0x43, 0x32, 0x34}));
		if(!waitForResponse() && !isErrorInResponse)
		{
			logger.info("hardware configuration info retrived.");
			return true;
		}
		else
		{
			logger.error("error while retrieving hardware configuration.");
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
					case SERIAL_NUMBER:
						
						byte[] ppn = getData(index, data);
						index += (ppn.length + 1);
						String sPPN = new String(ppn);
						TroubleshootController.spotInfo.setUPMppn(sPPN.substring(sPPN.length()-8));
						TroubleshootController.spotInfo.setUPMmac(getMac(sPPN.substring(sPPN.length()-8)));
						TroubleshootController.upmInfo.setSerialNumber(sPPN.substring(sPPN.length()-8));
						isErrorInResponse = false;
						logger.info("ppn retrieved : "+sPPN);
						break;
						
					case CR_SERIAL_NUMBER:
						
						byte[] cardSerial = getData(index, data);
						index += (cardSerial.length + 1);
						String sCardSerial = new String(cardSerial).trim();
						if(sCardSerial.isEmpty())
						{
							sCardSerial = "NA";
						}
						TroubleshootController.cardReaderInfo.setSerialNumber(sCardSerial);
						isErrorInResponse = false;
						logger.info("card reader serial retrieved : "+sCardSerial);
						break;
						
					case CR_DEV_TYPE:
						
						byte[] cardType = getData(index, data);
						index += (cardType.length + 1);
						String sCardType = getReaderType(cardType[0]);
						if(sCardType.isEmpty())
						{
							sCardType = "NA";
						}
						TroubleshootController.cardReaderInfo.setType(sCardType);
						isErrorInResponse = false;
						logger.info("card reader type retrieved : "+cardType[0]);
						break;
						
					case CR_DEV_MODEL:
						
						byte[] cardModel = getData(index, data);
						index += (cardModel.length + 1);
						String sCardModel = new String(cardModel).trim();
						if(sCardModel.isEmpty())
						{
							sCardModel = "NA";
						}
						TroubleshootController.cardReaderInfo.setModel(sCardModel);
						isErrorInResponse = false;
						logger.info("card reader model retrieved : "+sCardModel);
						break;
						
					case DISPLAY_HWR_CONFIG:
						
						byte[] dispResolution = getData(index, data);
						Short hght = EdtConvert.bytesToShort(true, Arrays.copyOfRange(dispResolution, 0, 2), 0);
						Short wth = EdtConvert.bytesToShort(true, Arrays.copyOfRange(dispResolution, 2, 4), 0);
						String width = wth.toString();
						String height = hght.toString();
						index += (dispResolution.length + 1);
						String sDispRes = width + "x" + height;
						TroubleshootController.displayInfo.setDispResolution(sDispRes);
						isErrorInResponse = false;
						logger.info("display resolution retireved : "+sDispRes);
						break;
				}
			}
			wakeUp();
		}
		else
		{
			isErrorInResponse = true;
			logger.error("Hard Config : error in response. error code = "+apdu.getAck());
			logger.info("before wakeup");
			wakeUp();
			logger.info("after wakeup");
		}
	}
	
	private String getReaderType(byte type)
	{
		switch (type)
		{
			case (byte)0x80:
				return "Hybrid Reader";
			
			case (byte)0x81:
				return "Swipe Through";
				
			case (byte)0x82:
				return "Mannual";
				
			case (byte)0x84:
				return "Motorised";
				
			case (byte)0x90:
				return "Magnetic";
				
			case (byte)0xA0:
				return "Chip Reader";
				
			case (byte)0xB0:
				return "Both Separated";
				
			case (byte)0xC0:
				return "Dual Reader";
				
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
	
	private String getMac(String ppn)
	{
		String mac = "";
		String deviceMac = "";
		
		if(!ppn.isEmpty())
		{
			long deviceSerial = EdtConvert.stringToLong( ppn ) & 0xFFFFFF;
			String hexSerialNum = EdtConvert.decToHex(deviceSerial);
			mac = Connection.VGD_MAC_PREFIX.concat(hexSerialNum);
			for(int i=0; i<mac.length();i=i+2)
			{
				deviceMac += mac.substring(i, i+2);
				if(i != mac.length()-2)
				{
					deviceMac += "-";
				}
			}
		}
		
		return deviceMac;
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
