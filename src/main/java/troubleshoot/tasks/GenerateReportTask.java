package troubleshoot.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.FixActionReport;
import troubleshoot.model.pojo.LogError;
import troubleshoot.model.pojo.ReportInformation;
import troubleshoot.model.pojo.Step;
import troubleshoot.models.OPTDeviceInformation;
import troubleshoot.models.PackageInformation;
import troubleshoot.models.StatusValidator;
import troubleshoot.models.PackageInformation.ApplicationVersion;
import troubleshoot.states.Device;
import troubleshoot.views.TroubleshootMainDialog;

public class GenerateReportTask extends SwingWorker<Boolean, Void>
{
	private final static Logger logger = Logger.getLogger(GenerateReportTask.class);
	
	private Vector<ApplicationVersion> firmwareInfo = null;
	private ReportInformation reportInformation = null;
	private TroubleshootMainDialog mainView = null;
	private String date;
	private long totalTime = 0;
	private String dispenserSerialNo;
	private String serviceRequestNo;
	
	public GenerateReportTask(String dispenserSerialNo, String serviceRequestNo)
	{
		mainView = TroubleshootController.getTroubleshootView();
		reportInformation = ReportInformation.getInstance();
		date = getDate();
		this.dispenserSerialNo = dispenserSerialNo;
		this.serviceRequestNo = serviceRequestNo;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception
	{
		TroubleshootController.endTime = System.currentTimeMillis();
		totalTime = (TroubleshootController.endTime - TroubleshootController.startTime);
		if(OPTDeviceInformation.getInstance().getInformation())
		{
			StatusValidator.getInstance().validate(Device.PRINTER);
			reportInformation.setInformation(false);
			
			if(PackageInformation.getInstance().collectPkgInfoFromUPM())
			{
				firmwareInfo = PackageInformation.getInstance().getAppVersionsFromSpot();
			}
			
			return generateReport();
		}
		
		return false;
	}

	@Override
	protected void done()
	{
		try
		{
			if (get( )) //is successful??
			{
				logger.info("Report generated successfully.");
				TroubleshootController.showMessageDlg("Report generated successfully.", "Generate Report", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				logger.error("Error while generating report.");
				TroubleshootController.showMessageDlg("Error while generating report.", "Generate Report", JOptionPane.ERROR_MESSAGE);
			}
			TroubleshootController.getTroubleshootView().enableAll(true);
		}
		catch (InterruptedException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String getDate()
	{
		String strDate = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		strDate = dateFormat.format(cal.getTime()) + " " + cal.getTimeZone().getDisplayName();
		return strDate;
	}
	
	private String isHardwareReplaced(Device device)
	{
		String isReplaced = "NO";
		
		switch (device)
		{
			case UPM:
				if(!reportInformation.upmInfo_initial.getSerialNumber().equalsIgnoreCase(TroubleshootController.upmInfo.getSerialNumber()))
				{
					isReplaced = "YES";
				}
				break;
			case CARD_READER:
				if(!reportInformation.cardReaderInfo_initial.getSerialNumber().equalsIgnoreCase(TroubleshootController.cardReaderInfo.getSerialNumber()))
				{
					isReplaced = "YES";
				}
				break;
			default:
				break;
		}
		
		return isReplaced;
	}
	
	private String getReportFileName()
	{
		String[] dateAndTime = date.split(" ");
		String reportName = TroubleshootConfigurations.getReportsDirectoryPath() + dispenserSerialNo + "_" + dateAndTime[0] +".xml";//+ "T" + dateAndTime[1] +".xml";
		return reportName;
	}
	
	private boolean generateReport()
	{
		boolean isSuccess = true;
		FileWriter fileWriter = null;
		
		try
		{
			Element root = new Element("FlexPayIV");
			Document doc = new Document();
			
			// Info
			Element info = new Element("Info");
			Element date = new Element("Date");
			date.addContent(this.date);
			Element timeRequired = new Element("TotalTimeRequiredForFix");
			timeRequired.addContent
			(
				String.format
				(
					"%02d min : %02d sec", 
				    TimeUnit.MILLISECONDS.toMinutes(totalTime),
				    (TimeUnit.MILLISECONDS.toSeconds(totalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime)))
				)
			);
			Element techID = new Element("TechID");
			techID.addContent(TroubleshootController.techID);
			Element dispenserNo = new Element("DispenserSerialNO");
			dispenserNo.addContent(dispenserSerialNo);
			Element sRequest = new Element("ServiceRequestNO");
			sRequest.addContent(serviceRequestNo);
			Element upmPPN = new Element("UPM_PPN");
			upmPPN.addContent(TroubleshootController.upmInfo.getSerialNumber());
			Element readerSN = new Element("Card_Reader_SN");
			readerSN.addContent(TroubleshootController.cardReaderInfo.getSerialNumber());
			info.addContent(date);
			info.addContent(timeRequired);
			info.addContent(techID);
			info.addContent(dispenserNo);
			info.addContent(sRequest);
			info.addContent(upmPPN);
			info.addContent(readerSN);
			
			// Software
			Element software = new Element("Software");
			Element upmVer = new Element("UPM_Version");
			for (ApplicationVersion app : firmwareInfo)
			{
				Element appVer = new Element(app.getAppId());
				appVer.addContent(app.getVersion() +" - "+ app.getBuildNumber());
				upmVer.addContent(appVer);
			}
			Element readerVer = new Element("Card_Reader_version");
			readerVer.addContent(TroubleshootController.cardReaderInfo.getFmwVersion());
			Element printerVer = new Element("Printer_version");
			printerVer.addContent(TroubleshootController.printerInfo.getFmwVersion());
			software.addContent(upmVer);
			software.addContent(readerVer);
			software.addContent(printerVer);
			
			// Troubleshooting Status
			Element troubleshootingStatus = new Element("Troubleshooting_Status");
			
			// Test Status
			Element testStatus = new Element("Test_Status");
			Element upmTest = new Element("UPMTest");
			upmTest.addContent(mainView.getDevicePanel(Device.UPM).getTestStatus());
			Element readerTest = new Element("CardReaderTest");
			readerTest.addContent(mainView.getDevicePanel(Device.CARD_READER).getTestStatus());
			Element printerTest = new Element("PrinterTest");
			printerTest.addContent(mainView.getDevicePanel(Device.PRINTER).getTestStatus());
			testStatus.addContent(upmTest);
			testStatus.addContent(readerTest);
			testStatus.addContent(printerTest);
			
			// Device Initial Status
			Element initialStatus = new Element("DeviceInitialStatus");
			
			Element upm_initial = new Element("UPM");
			Element uComm_initial = new Element("Communication");
			uComm_initial.addContent(reportInformation.upmStatus_initial.getCommunication());
			Element uDis_initial = new Element("Dismount");
			uDis_initial.addContent(reportInformation.upmStatus_initial.getDismount());
			Element uErr_initial = new Element("Error");
			uErr_initial.addContent(reportInformation.upmStatus_initial.getError());
			Element uSystemStatus_initial = new Element("SystemStatus");
			uSystemStatus_initial.addContent(reportInformation.upmInfo_initial.getStatus().toString());
			upm_initial.addContent(uComm_initial);
			upm_initial.addContent(uDis_initial);
			upm_initial.addContent(uErr_initial);
			upm_initial.addContent(uSystemStatus_initial);
			
			Element reader_initial = new Element("CardReader");
			Element rComm_initial = new Element("Communication");
			rComm_initial.addContent(reportInformation.readerStatus_initial.getCommunication());
			Element rDis_initial = new Element("Dismount");
			rDis_initial.addContent(reportInformation.readerStatus_initial.getDismount());
			Element rErr_initial = new Element("Error");
			rErr_initial.addContent(reportInformation.readerStatus_initial.getError());
			Element rSystemStatus_initial = new Element("SystemStatus");
			rSystemStatus_initial.addContent(reportInformation.cardReaderInfo_initial.getStatus().toString());
			reader_initial.addContent(rComm_initial);
			reader_initial.addContent(rDis_initial);
			reader_initial.addContent(rErr_initial);
			reader_initial.addContent(rSystemStatus_initial);
			
			Element printer_initial = new Element("Printer");
			Element pState_initial = new Element("State");
			pState_initial.addContent(reportInformation.printerStatus_initial.getCommunication());
			Element pAddInfo_initial = new Element("AdditionalInfo");
			pAddInfo_initial.addContent(reportInformation.printerStatus_initial.getDismount());
			Element pErr_initial = new Element("Error");
			pErr_initial.addContent(reportInformation.printerStatus_initial.getError());
			Element pSystemStatus_initial = new Element("SystemStatus");
			pSystemStatus_initial.addContent(reportInformation.printerInfo_initial.getStatus().toString());
			printer_initial.addContent(pState_initial);
			printer_initial.addContent(pAddInfo_initial);
			printer_initial.addContent(pErr_initial);
			printer_initial.addContent(pSystemStatus_initial);
			
			initialStatus.addContent(upm_initial);
			initialStatus.addContent(reader_initial);
			initialStatus.addContent(printer_initial);
			
			// Device Final Status
			Element finalStatus = new Element("DeviceFinalStatus");
			
			Element upm_final = new Element("UPM");
			Element uComm_final = new Element("Communication");
			uComm_final.addContent(mainView.getDevicePanel(Device.UPM).getCommunication());
			Element uDis_final = new Element("Dismount");
			uDis_final.addContent(mainView.getDevicePanel(Device.UPM).getDismount());
			Element uErr_final = new Element("Error");
			uErr_final.addContent(mainView.getDevicePanel(Device.UPM).getError());
			Element uSystemStatus_final = new Element("SystemStatus");
			uSystemStatus_final.addContent(TroubleshootController.upmInfo.getStatus().toString());
			upm_final.addContent(uComm_final);
			upm_final.addContent(uDis_final);
			upm_final.addContent(uErr_final);
			upm_final.addContent(uSystemStatus_final);
			
			Element reader_final = new Element("CardReader");
			Element rComm_final = new Element("Communication");
			rComm_final.addContent(mainView.getDevicePanel(Device.CARD_READER).getCommunication());
			Element rDis_final = new Element("Dismount");
			rDis_final.addContent(mainView.getDevicePanel(Device.CARD_READER).getDismount());
			Element rErr_final = new Element("Error");
			rErr_final.addContent(mainView.getDevicePanel(Device.CARD_READER).getError());
			Element rSystemStatus_final = new Element("SystemStatus");
			rSystemStatus_final.addContent(TroubleshootController.cardReaderInfo.getStatus().toString());
			reader_final.addContent(rComm_final);
			reader_final.addContent(rDis_final);
			reader_final.addContent(rErr_final);
			reader_final.addContent(rSystemStatus_final);
			
			Element printer_final = new Element("Printer");
			Element pState_final = new Element("State");
			pState_final.addContent(mainView.getDevicePanel(Device.PRINTER).getCommunication());
			Element pAddInfo_final = new Element("AdditionalInfo");
			pAddInfo_final.addContent(mainView.getDevicePanel(Device.PRINTER).getDismount());
			Element pErr_final = new Element("Error");
			pErr_final.addContent(mainView.getDevicePanel(Device.PRINTER).getError());
			Element pSystemStatus_final = new Element("SystemStatus");
			pSystemStatus_final.addContent(TroubleshootController.printerInfo.getStatus().toString());
			printer_final.addContent(pState_final);
			printer_final.addContent(pAddInfo_final);
			printer_final.addContent(pErr_final);
			printer_final.addContent(pSystemStatus_final);
			
			finalStatus.addContent(upm_final);
			finalStatus.addContent(reader_final);
			finalStatus.addContent(printer_final);
			
			troubleshootingStatus.addContent(testStatus);
			troubleshootingStatus.addContent(initialStatus);
			troubleshootingStatus.addContent(finalStatus);
			
			// Mix Package Issue
			Element mixPkgIssue = new Element("Error");
			if(TroubleshootController.isMixPkgIssueFound)
			{
				mixPkgIssue.addContent("MIX PACKAGE ISSUE FOUND");
			}
			
			// Log Errors Report
			Element logReport = new Element("LogErrorReport");
			
			if(TroubleshootController.isLogCheckSkip && reportInformation.foundErrors.isEmpty())
			{
				logReport.addContent("Log check is Skipped");
			}
			else
			{
				if(!reportInformation.foundErrors.isEmpty())
				{
					for (LogError logError : reportInformation.foundErrors)
					{
						if(logError.getIsFound())
						{
							Element error = new Element("Error");
							Element severity = new Element("Severity");
							severity.addContent(logError.getSeverity());
							Element category = new Element("Category");
							category.addContent(logError.getCategory());
							Element errorMsg = new Element("ErrorMessage");
							errorMsg.addContent(logError.getErrorString());
							error.addContent(severity);
							error.addContent(category);
							error.addContent(errorMsg);
							logReport.addContent(error);
						}
					}
				}
			}
			
			// Hardware Replaced
			Element hwReplaced = new Element("Hardware_Replaceed");
			Element upmHW = new Element("UPM_HW");
			upmHW.addContent(isHardwareReplaced(Device.UPM));
			Element readerHW = new Element("CardReader_HW");
			readerHW.addContent(isHardwareReplaced(Device.CARD_READER));
			hwReplaced.addContent(upmHW);
			hwReplaced.addContent(readerHW);
			
			//Fix Report
			Element fixReport = new Element("Fix_Report");
			addErrorReport(fixReport, "UPM_Fix_Report", Device.UPM, false);
			addErrorReport(fixReport, "CR_Fix_Report", Device.CARD_READER, false);
			addErrorReport(fixReport, "Printer_Fix_Report", Device.PRINTER, false);
			addErrorReport(fixReport, "LogError_Fix_Report", Device.UPM, true);
			
			root.addContent(info);
			root.addContent(software);
			root.addContent(troubleshootingStatus);
			if(TroubleshootController.isMixPkgIssueFound)
			{
				root.addContent(mixPkgIssue);
			}
			root.addContent(logReport);
			root.addContent(hwReplaced);
			root.addContent(fixReport);
			
			doc.setRootElement(root);

			fileWriter = new FileWriter(new File(getReportFileName()));
			XMLOutputter outter = new XMLOutputter();
			outter.setFormat(Format.getPrettyFormat());
			outter.output(doc, fileWriter);
		}
		catch (IOException e)
		{
			isSuccess = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		catch (Exception e)
		{
			isSuccess = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			if(fileWriter != null)
			{
				try
				{
					fileWriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
		
		return isSuccess;
	}
	
	private void addErrorReport(Element fixReport, String reportName, Device deviceType, boolean isLogErrorFix)
	{
		Element deviceReport = new Element(reportName);
		Element errors = new Element("Errors");
		
		if(isLogErrorFix)
		{
			if(!reportInformation.fixActionReports.isEmpty())
			{
				for (FixActionReport fixActionReport : reportInformation.fixActionReports)
				{
					if(fixActionReport.isLogErrorFix())
					{
						Element error = new Element("Error");
						Element errorMessage = new Element("ErrorMessage");
						errorMessage.addContent(fixActionReport.getErrorMessage());
						Element actionFile = new Element("ActionFileName");
						actionFile.addContent(fixActionReport.getActionFileName());
						Element fixSteps = new Element("FixSteps");
						if(!fixActionReport.getSteps().isEmpty())
						{
							for (Step step : fixActionReport.getSteps())
							{
								Element elementStep = new Element("Step");
								for (String msg : step.getActionMessages())
								{
									Element message = new Element("Message");
									message.addContent(msg);
									elementStep.addContent(message);
								}
								Element action = new Element("Action");
								action.addContent(step.getAction());
								elementStep.addContent(action);
								fixSteps.addContent(elementStep);
							}
						}
						error.addContent(errorMessage);
						error.addContent(actionFile);
						error.addContent(fixSteps);
						errors.addContent(error);
					}
				}
			}
		}
		else
		{
			if(!reportInformation.fixActionReports.isEmpty())
			{
				for (FixActionReport fixActionReport : reportInformation.fixActionReports)
				{
					if(!fixActionReport.isLogErrorFix())
					{
						if(fixActionReport.getDeviceType() == deviceType)
						{
							Element error = new Element("Error");
							Element errorMessage = new Element("ErrorMessage");
							errorMessage.addContent(fixActionReport.getErrorMessage());
							Element actionFile = new Element("ActionFileName");
							actionFile.addContent(fixActionReport.getActionFileName());
							Element fixSteps = new Element("FixSteps");
							if(!fixActionReport.getSteps().isEmpty())
							{
								for (Step step : fixActionReport.getSteps())
								{
									Element elementStep = new Element("Step");
									for (String msg : step.getActionMessages())
									{
										Element message = new Element("Message");
										message.addContent(msg);
										elementStep.addContent(message);
									}
									Element action = new Element("Action");
									action.addContent(step.getAction());
									elementStep.addContent(action);
									fixSteps.addContent(elementStep);
								}
							}
							error.addContent(errorMessage);
							error.addContent(actionFile);
							error.addContent(fixSteps);
							errors.addContent(error);
						}
					}
				}
			}
		}
		
		deviceReport.addContent(errors);
		fixReport.addContent(deviceReport);
	}
}
