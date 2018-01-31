package troubleshoot.models;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;
import troubleshoot.states.DisplayDismStatus;
import troubleshoot.states.PrinterComStatus;
import troubleshoot.states.PrinterDismStatus;
import troubleshoot.states.PrinterErrorStatus;
import troubleshoot.states.ReaderComStatus;
import troubleshoot.states.ReaderDismStatus;
import troubleshoot.states.ReaderErrorStatus;
import troubleshoot.states.UpmComStatus;
import troubleshoot.states.UpmDismStatus;
import troubleshoot.states.UpmErrorStatus;
import troubleshoot.views.PrinterDialog;
import troubleshoot.views.TroubleshootMainDialog;

public class StatusValidator
{
	private static final Logger logger = Logger.getLogger(StatusValidator.class);
	
	private static StatusValidator upmStatusValidator = null;
	
	private TroubleshootController troubleshootController;
	TroubleshootMainDialog mainView;
	PrinterDialog printerDialog;
	
	private StatusValidator()
	{
		troubleshootController = TroubleshootController.getInstance();
		mainView = TroubleshootController.getTroubleshootView();
		printerDialog = PrinterDialog.getInstance();
	}
	
	public static StatusValidator getInstance()
	{
		if(upmStatusValidator == null)
		{
			upmStatusValidator = new StatusValidator();
		}
		return upmStatusValidator;
	}
	
	public void validate(Device deviceType)
	{
		setSerialNumber(deviceType);
		setCommunicationStatus(deviceType);
		setDismountStatus(deviceType);
		setErrorStatus(deviceType);
	}
	
	private void setSerialNumber(Device deviceType)
	{
		if(deviceType == Device.PRINTER)
		{
			// set Printer Type here
			PrinterDialog.getInstance().showSerialNumber(deviceType, troubleshootController.getInfo(deviceType).getType());
		}
		else
		{
			mainView.showSerialNumber(deviceType, troubleshootController.getInfo(deviceType).getSerialNumber());
		}
	}
	
	private void setCommunicationStatus(Device deviceType)
	{
		String state = troubleshootController.getInfo(deviceType).getStatus().getCommunication();
		
		if(Device.UPM == deviceType)
		{
			switch (state)
			{
				case "00":
					mainView.showCommunicationStaus(deviceType, UpmComStatus.CODE_00.getResultText(), UpmComStatus.CODE_00.getColor(), UpmComStatus.CODE_00.getForColor(), UpmComStatus.CODE_00.getStatusText());
					break;
					
				case "01":
					mainView.showCommunicationStaus(deviceType, UpmComStatus.CODE_01.getResultText(), UpmComStatus.CODE_01.getColor(), UpmComStatus.CODE_01.getForColor(), UpmComStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					mainView.showCommunicationStaus(deviceType, UpmComStatus.CODE_02.getResultText(), UpmComStatus.CODE_02.getColor(), UpmComStatus.CODE_02.getForColor(), UpmComStatus.CODE_02.getStatusText());
					break;
					
				case "03":
					mainView.showCommunicationStaus(deviceType, UpmComStatus.CODE_03.getResultText(), UpmComStatus.CODE_03.getColor(), UpmComStatus.CODE_03.getForColor(), UpmComStatus.CODE_03.getStatusText());
					break;
					
				case "04":
					mainView.showCommunicationStaus(deviceType, UpmComStatus.CODE_04.getResultText(), UpmComStatus.CODE_04.getColor(), UpmComStatus.CODE_04.getForColor(), UpmComStatus.CODE_04.getStatusText());
					break;
					
				case "05":
					mainView.showCommunicationStaus(deviceType, UpmComStatus.CODE_05.getResultText(), UpmComStatus.CODE_05.getColor(), UpmComStatus.CODE_05.getForColor(), UpmComStatus.CODE_05.getStatusText());
					break;
					
				default:
					logger.info("Unkown communication state : "+state);
					break;
			}
		
		}
		else if(Device.CARD_READER == deviceType) 
		{
			switch (state)
			{
				case "00":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_00.getResultText(), ReaderComStatus.CODE_00.getColor(), ReaderComStatus.CODE_00.getForColor(), ReaderComStatus.CODE_00.getStatusText());
					break;
					
				case "01":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_01.getResultText(), ReaderComStatus.CODE_01.getColor(), ReaderComStatus.CODE_01.getForColor(), ReaderComStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_02.getResultText(), ReaderComStatus.CODE_02.getColor(), ReaderComStatus.CODE_02.getForColor(), ReaderComStatus.CODE_02.getStatusText());
					break;
					
				case "03":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_03.getResultText(), ReaderComStatus.CODE_03.getColor(), ReaderComStatus.CODE_03.getForColor(), ReaderComStatus.CODE_03.getStatusText());
					break;
					
				case "04":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_04.getResultText(), ReaderComStatus.CODE_04.getColor(), ReaderComStatus.CODE_04.getForColor(), ReaderComStatus.CODE_04.getStatusText());
					break;
					
				case "05":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_05.getResultText(), ReaderComStatus.CODE_05.getColor(), ReaderComStatus.CODE_05.getForColor(), ReaderComStatus.CODE_05.getStatusText());
					break;
					
				case "06":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_06.getResultText(), ReaderComStatus.CODE_06.getColor(), ReaderComStatus.CODE_06.getForColor(), ReaderComStatus.CODE_06.getStatusText());
					break;
					
				case "0C":
					mainView.showCommunicationStaus(deviceType, ReaderComStatus.CODE_0C.getResultText(), ReaderComStatus.CODE_0C.getColor(), ReaderComStatus.CODE_0C.getForColor(), ReaderComStatus.CODE_0C.getStatusText());
					break;
					
				default:
					logger.info("Unkown communication state : "+state);
					break;
			}
		}
		else if(Device.PRINTER == deviceType)
		{
			switch (state)
			{
				case "01":
					printerDialog.showCommunicationStaus(deviceType, PrinterComStatus.CODE_01.getResultText(), PrinterComStatus.CODE_01.getColor(), PrinterComStatus.CODE_01.getForColor(), PrinterComStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					printerDialog.showCommunicationStaus(deviceType, PrinterComStatus.CODE_02.getResultText(), PrinterComStatus.CODE_02.getColor(), PrinterComStatus.CODE_02.getForColor(), PrinterComStatus.CODE_02.getStatusText());
					break;
					
				case "03":
					printerDialog.showCommunicationStaus(deviceType, PrinterComStatus.CODE_03.getResultText(), PrinterComStatus.CODE_03.getColor(), PrinterComStatus.CODE_03.getForColor(), PrinterComStatus.CODE_03.getStatusText());
					break;
					
				case "04":
					printerDialog.showCommunicationStaus(deviceType, PrinterComStatus.CODE_04.getResultText(), PrinterComStatus.CODE_04.getColor(), PrinterComStatus.CODE_04.getForColor(), PrinterComStatus.CODE_04.getStatusText());
					break;
					
				default:
					logger.info("Unkown communication state : "+state);
					break;
			}
		}
	}
	
	private void setDismountStatus(Device deviceType)
	{
		String state = troubleshootController.getInfo(deviceType).getStatus().getDismount();
		
		if(Device.UPM == deviceType)
		{
			switch (state)
			{
				case "00":
					if(troubleshootController.getInfo(Device.DISPLAY).getStatus().getDismount().equals("00"))
					{
						mainView.showDismountStatus(deviceType, "Keyboard and Display Dismounted", UpmDismStatus.CODE_00.getColor(), UpmDismStatus.CODE_00.getForColor(), UpmDismStatus.CODE_00.getStatusText());
					}
					else if(troubleshootController.getInfo(Device.DISPLAY).getStatus().getDismount().equals("80"))
					{
						mainView.showDismountStatus(deviceType, UpmDismStatus.CODE_00.getResultText(), UpmDismStatus.CODE_00.getColor(), UpmDismStatus.CODE_00.getForColor(), UpmDismStatus.CODE_00.getStatusText());
					}
					break;
					
				case "80":
					if(troubleshootController.getInfo(Device.DISPLAY).getStatus().getDismount().equals("00"))
					{
						mainView.showDismountStatus(deviceType, DisplayDismStatus.CODE_00.getResultText(), DisplayDismStatus.CODE_00.getColor(), DisplayDismStatus.CODE_00.getForColor(), DisplayDismStatus.CODE_00.getStatusText());
					}
					else if(troubleshootController.getInfo(Device.DISPLAY).getStatus().getDismount().equals("80"))
					{
						mainView.showDismountStatus(deviceType, UpmDismStatus.CODE_80.getResultText(), UpmDismStatus.CODE_80.getColor(), UpmDismStatus.CODE_80.getForColor(), UpmDismStatus.CODE_80.getStatusText());
					}
					break;
					
				default:
					logger.info("Unkown dismount state : "+state);
					break;
			}
		}
		else if(Device.CARD_READER == deviceType) 
		{
			switch (state)
			{
				case "00":
					mainView.showDismountStatus(deviceType, ReaderDismStatus.CODE_00.getResultText(), ReaderDismStatus.CODE_00.getColor(), ReaderDismStatus.CODE_00.getForColor(), ReaderDismStatus.CODE_00.getStatusText());
					break;
					
				case "80":
					mainView.showDismountStatus(deviceType, ReaderDismStatus.CODE_80.getResultText(), ReaderDismStatus.CODE_80.getColor(), ReaderDismStatus.CODE_80.getForColor(), ReaderDismStatus.CODE_80.getStatusText());
					break;
					
				case "81":
					mainView.showDismountStatus(deviceType, ReaderDismStatus.CODE_81.getResultText(), ReaderDismStatus.CODE_81.getColor(), ReaderDismStatus.CODE_81.getForColor(), ReaderDismStatus.CODE_81.getStatusText());
					break;
					
				case "83":
					mainView.showDismountStatus(deviceType, ReaderDismStatus.CODE_83.getResultText(), ReaderDismStatus.CODE_83.getColor(), ReaderDismStatus.CODE_83.getForColor(), ReaderDismStatus.CODE_83.getStatusText());
					break;
					
				default:
					logger.info("Unkown dismount state : "+state);
					break;
			}
		}
		else if(Device.PRINTER == deviceType)
		{
			switch (state)
			{
				case "00":
					printerDialog.showDismountStatus(deviceType, PrinterDismStatus.CODE_00.getResultText(), PrinterDismStatus.CODE_00.getColor(), PrinterDismStatus.CODE_00.getForColor(), PrinterDismStatus.CODE_00.getStatusText());
					break;
					
				case "01":
					printerDialog.showDismountStatus(deviceType, PrinterDismStatus.CODE_01.getResultText(), PrinterDismStatus.CODE_01.getColor(), PrinterDismStatus.CODE_01.getForColor(), PrinterDismStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					printerDialog.showDismountStatus(deviceType, PrinterDismStatus.CODE_02.getResultText(), PrinterDismStatus.CODE_02.getColor(), PrinterDismStatus.CODE_02.getForColor(), PrinterDismStatus.CODE_02.getStatusText());
					break;
					
				default:
					logger.info("Unkown dismount state : "+state);
					break;
			}
		}
	}
	
	private void setErrorStatus(Device deviceType)
	{
		String state = troubleshootController.getInfo(deviceType).getStatus().getError();
		
		if(Device.UPM == deviceType)
		{
			switch (state)
			{
				case "00":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_00.getResultText(), UpmErrorStatus.CODE_00.getColor(), UpmErrorStatus.CODE_00.getForColor(), UpmErrorStatus.CODE_00.getStatusText());
					break;
					
				case "01":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_01.getResultText(), UpmErrorStatus.CODE_01.getColor(), UpmErrorStatus.CODE_01.getForColor(), UpmErrorStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_02.getResultText(), UpmErrorStatus.CODE_02.getColor(), UpmErrorStatus.CODE_02.getForColor(), UpmErrorStatus.CODE_02.getStatusText());
					break;
					
				case "03":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_03.getResultText(), UpmErrorStatus.CODE_03.getColor(), UpmErrorStatus.CODE_03.getForColor(), UpmErrorStatus.CODE_03.getStatusText());
					break;
					
				case "04":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_04.getResultText(), UpmErrorStatus.CODE_04.getColor(), UpmErrorStatus.CODE_04.getForColor(), UpmErrorStatus.CODE_04.getStatusText());
					break;
					
				case "06":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_06.getResultText(), UpmErrorStatus.CODE_06.getColor(), UpmErrorStatus.CODE_06.getForColor(), UpmErrorStatus.CODE_06.getStatusText());
					break;
					
				case "07":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_07.getResultText(), UpmErrorStatus.CODE_07.getColor(), UpmErrorStatus.CODE_07.getForColor(), UpmErrorStatus.CODE_07.getStatusText());
					break;
					
				case "09":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_09.getResultText(), UpmErrorStatus.CODE_09.getColor(), UpmErrorStatus.CODE_09.getForColor(), UpmErrorStatus.CODE_09.getStatusText());
					break;
					
				case "7F":
					mainView.showErrorStatus(deviceType, UpmErrorStatus.CODE_7F.getResultText(), UpmErrorStatus.CODE_7F.getColor(), UpmErrorStatus.CODE_7F.getForColor(), UpmErrorStatus.CODE_7F.getStatusText());
					break;
					
				default:
					logger.info("Unkown error state : "+state);
					break;
			}
		}
		else if(Device.CARD_READER == deviceType) 
		{
			switch (state)
			{
				case "00":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_00.getResultText(), ReaderErrorStatus.CODE_00.getColor(), ReaderErrorStatus.CODE_00.getForColor(), ReaderErrorStatus.CODE_00.getStatusText());
					break;
					
				case "01":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_01.getResultText(), ReaderErrorStatus.CODE_01.getColor(), ReaderErrorStatus.CODE_01.getForColor(), ReaderErrorStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_02.getResultText(), ReaderErrorStatus.CODE_02.getColor(), ReaderErrorStatus.CODE_02.getForColor(), ReaderErrorStatus.CODE_02.getStatusText());
					break;
					
				case "03":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_03.getResultText(), ReaderErrorStatus.CODE_03.getColor(), ReaderErrorStatus.CODE_03.getForColor(), ReaderErrorStatus.CODE_03.getStatusText());
					break;
					
				case "04":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_04.getResultText(), ReaderErrorStatus.CODE_04.getColor(), ReaderErrorStatus.CODE_04.getForColor(), ReaderErrorStatus.CODE_04.getStatusText());
					break;
					
				case "06":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_06.getResultText(), ReaderErrorStatus.CODE_06.getColor(), ReaderErrorStatus.CODE_06.getForColor(), ReaderErrorStatus.CODE_06.getStatusText());
					break;
					
				case "07":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_07.getResultText(), ReaderErrorStatus.CODE_07.getColor(), ReaderErrorStatus.CODE_07.getForColor(), ReaderErrorStatus.CODE_07.getStatusText());
					break;
					
				case "08":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_08.getResultText(), ReaderErrorStatus.CODE_08.getColor(), ReaderErrorStatus.CODE_08.getForColor(), ReaderErrorStatus.CODE_08.getStatusText());
					break;
					
				case "09":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_09.getResultText(), ReaderErrorStatus.CODE_09.getColor(), ReaderErrorStatus.CODE_09.getForColor(), ReaderErrorStatus.CODE_09.getStatusText());
					break;
					
				case "0A":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_0A.getResultText(), ReaderErrorStatus.CODE_0A.getColor(), ReaderErrorStatus.CODE_0A.getForColor(), ReaderErrorStatus.CODE_0A.getStatusText());
					break;
					
				case "0B":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_0B.getResultText(), ReaderErrorStatus.CODE_0B.getColor(), ReaderErrorStatus.CODE_0B.getForColor(), ReaderErrorStatus.CODE_0B.getStatusText());
					break;
					
				case "0C":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_0C.getResultText(), ReaderErrorStatus.CODE_0C.getColor(), ReaderErrorStatus.CODE_0C.getForColor(), ReaderErrorStatus.CODE_0C.getStatusText());
					break;
					
				case "7F":
					mainView.showErrorStatus(deviceType, ReaderErrorStatus.CODE_7F.getResultText(), ReaderErrorStatus.CODE_7F.getColor(), ReaderErrorStatus.CODE_7F.getForColor(), ReaderErrorStatus.CODE_7F.getStatusText());
					break;
					
				default:
					logger.info("Unkown error state : "+state);
					break;
			}
		}
		else if(Device.PRINTER == deviceType)
		{
			switch (state)
			{
				case "00":
					printerDialog.showErrorStatus(deviceType, PrinterErrorStatus.CODE_00.getResultText(), PrinterErrorStatus.CODE_00.getColor(), PrinterErrorStatus.CODE_00.getForColor(), PrinterErrorStatus.CODE_00.getStatusText());
				break;
				
				case "01":
					printerDialog.showErrorStatus(deviceType, PrinterErrorStatus.CODE_01.getResultText(), PrinterErrorStatus.CODE_01.getColor(), PrinterErrorStatus.CODE_01.getForColor(), PrinterErrorStatus.CODE_01.getStatusText());
					break;
					
				case "02":
					printerDialog.showErrorStatus(deviceType, PrinterErrorStatus.CODE_02.getResultText(), PrinterErrorStatus.CODE_02.getColor(), PrinterErrorStatus.CODE_02.getForColor(), PrinterErrorStatus.CODE_02.getStatusText());
					break;
					
				case "04":
					printerDialog.showErrorStatus(deviceType, PrinterErrorStatus.CODE_04.getResultText(), PrinterErrorStatus.CODE_04.getColor(), PrinterErrorStatus.CODE_04.getForColor(), PrinterErrorStatus.CODE_04.getStatusText());
					break;
					
				case "08":
					printerDialog.showErrorStatus(deviceType, PrinterErrorStatus.CODE_08.getResultText(), PrinterErrorStatus.CODE_08.getColor(), PrinterErrorStatus.CODE_08.getForColor(), PrinterErrorStatus.CODE_08.getStatusText());
					break;
					
				case "10":
					printerDialog.showErrorStatus(deviceType, PrinterErrorStatus.CODE_10.getResultText(), PrinterErrorStatus.CODE_10.getColor(), PrinterErrorStatus.CODE_10.getForColor(), PrinterErrorStatus.CODE_10.getStatusText());
					break;
					
				default:
					logger.info("Unkown error state : "+state);
					break;
			}
		}
	}
}
