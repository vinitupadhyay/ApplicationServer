package troubleshoot.model.pojo;

import java.util.Vector;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;

public class ReportInformation
{
	private static ReportInformation reportInformation = null;
	
	public static boolean isUPMFirstTime = false;
	public static boolean isOPTFirstTime = false;
	
	public DeviceInfo upmInfo_initial;
	public DeviceInfo cardReaderInfo_initial;
	public DeviceInfo displayInfo_initial;
	public DeviceInfo securityInfo_initial;
	public DeviceInfo printerInfo_initial;
	
	// This is the initial status shown in Communication, Dismount and Error Text Fields
	public DeviceStatus upmStatus_initial;
	public DeviceStatus readerStatus_initial;
	public DeviceStatus printerStatus_initial;
	
	public Vector<LogError> foundErrors = null;
	
	public Vector<FixActionReport> fixActionReports = null;
	
	
	private ReportInformation()
	{
		this.foundErrors = new Vector<LogError>();
		this.fixActionReports = new Vector<FixActionReport>();
	}
	
	public static ReportInformation getInstance()
	{
		if(reportInformation == null)
		{
			reportInformation = new ReportInformation();
		}
		return reportInformation;
	}
	
	public void setInformation(boolean isUPM)
	{
		if(isUPM)
		{
			if(isUPMFirstTime)
			{
				isUPMFirstTime = false;
				
				upmInfo_initial = new DeviceInfo(TroubleshootController.upmInfo);
				cardReaderInfo_initial = new DeviceInfo(TroubleshootController.cardReaderInfo);
				displayInfo_initial = new DeviceInfo(TroubleshootController.displayInfo);
				securityInfo_initial = new DeviceInfo(TroubleshootController.securityInfo);
				
				upmStatus_initial = new DeviceStatus();
				upmStatus_initial.setCommunication(TroubleshootController.getTroubleshootView().getDevicePanel(Device.UPM).getCommunication());
				upmStatus_initial.setDismount(TroubleshootController.getTroubleshootView().getDevicePanel(Device.UPM).getDismount());
				upmStatus_initial.setError(TroubleshootController.getTroubleshootView().getDevicePanel(Device.UPM).getError());
				
				readerStatus_initial = new DeviceStatus();
				readerStatus_initial.setCommunication(TroubleshootController.getTroubleshootView().getDevicePanel(Device.CARD_READER).getCommunication());
				readerStatus_initial.setDismount(TroubleshootController.getTroubleshootView().getDevicePanel(Device.CARD_READER).getDismount());
				readerStatus_initial.setError(TroubleshootController.getTroubleshootView().getDevicePanel(Device.CARD_READER).getError());
			}
		}
		else
		{
			if(isOPTFirstTime)
			{
				isOPTFirstTime = false;
				
				printerInfo_initial = new DeviceInfo(TroubleshootController.printerInfo);
				
				printerStatus_initial = new DeviceStatus();
				printerStatus_initial.setCommunication(TroubleshootController.getTroubleshootView().getDevicePanel(Device.PRINTER).getCommunication());
				printerStatus_initial.setDismount(TroubleshootController.getTroubleshootView().getDevicePanel(Device.PRINTER).getDismount());
				printerStatus_initial.setError(TroubleshootController.getTroubleshootView().getDevicePanel(Device.PRINTER).getError());
			}
		}
	}

	public void setFoundedErrors(Vector<LogError> paramFoundErrors)
	{
		if(foundErrors.isEmpty())
		{
			for (LogError logError : paramFoundErrors)
			{
				foundErrors.add(logError);
			}
		}
		else
		{
			for (LogError logError : paramFoundErrors)
			{
				if(!isAlreadyPresent(logError))
				{
					foundErrors.add(logError);
				}
			}
		}
	}
	
	private boolean isAlreadyPresent(LogError logError)
	{
		for (LogError logErrorReport : foundErrors)
		{
			if(logError.getErrorString().equals(logErrorReport.getErrorString()))
			{
				return true;
				
			}
		}
		return false;
	}
	
	public void clearReport()
	{
		foundErrors.clear();
		fixActionReports.clear();
	}
}
