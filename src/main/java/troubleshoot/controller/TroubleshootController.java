package troubleshoot.controller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import troubleshoot.config.FileResourceLogStandardConfig;
import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.event.handler.EventManager;
import troubleshoot.file.managment.api.FileCompression;
import troubleshoot.model.pojo.DeviceInfo;
import troubleshoot.model.pojo.DeviceStatus;
import troubleshoot.model.pojo.FixActionReport;
import troubleshoot.model.pojo.LogError;
import troubleshoot.model.pojo.ReportInformation;
import troubleshoot.model.pojo.Resource;
import troubleshoot.model.pojo.SPOTInfo;
import troubleshoot.models.Connection;
import troubleshoot.models.DeviceInformation;
import troubleshoot.models.DiagnosticInformation;
import troubleshoot.models.FileDownload;
import troubleshoot.models.OPTDeviceInformation;
import troubleshoot.models.PackageInformation;
import troubleshoot.models.PackageInformation.ApplicationVersion;
import troubleshoot.models.StatusValidator;
import troubleshoot.states.Device;
import troubleshoot.tasks.CheckUPMLogsFixTask;
import troubleshoot.tasks.CheckUPMLogsTask;
import troubleshoot.tasks.GenerateReportTask;
import troubleshoot.tasks.InfoTask;
import troubleshoot.tasks.OPTInfoTask;
import troubleshoot.tasks.PackageFirmwareInfoTask;
import troubleshoot.tasks.RunSelfCheckTask;
import troubleshoot.tasks.TestPrinterTask;
import troubleshoot.tasks.TestReaderTask;
import troubleshoot.tasks.TestUpmTask;
import troubleshoot.util.EdtFileUtil;
import troubleshoot.util.EdtUtil;
import troubleshoot.views.MessageDialog;
import troubleshoot.views.OptionDialog;
import troubleshoot.views.PinpadDialog;
import troubleshoot.views.PrinterDialog;
import troubleshoot.views.SelectDirectoryDialog;
import troubleshoot.views.TroubleshootInstructionDialog;
import troubleshoot.views.TroubleshootMainDialog;
import troubleshoot.xml.ErrorFileParser;
import troubleshoot.xml.ErrorFixStepsDialog;
import troubleshoot.xml.ParsePinPadLogsFile;
import troubleshoot.xml.Tag;

@Component
public class TroubleshootController 
{
	private final static Logger logger = Logger.getLogger(TroubleshootController.class);
	private final static String DEFAULT_JOURANAL_LOG_FILE = "JOURNAL";
	private final static String DEFAULT_JOURANAL_HIST_LOG_FILE = "JOURNAL_HIST";
	private final static String DEFAULT_JOURANAL_LOG_FILE_EXT = ".log";
	private final static String DEFAULT_PINPAD_LOG_FILE = "PINPAD_HIST";
	private final static String DEFAULT_PINPAD_LOG_FILE_EXT = "";
	private final static String DEFAULT_KEYWORD_TAMPER = "Reader error has been tampered";
	private final static String DEFAULT_XML_FILE_UPM_ERRORS = "Fix\\UPMErrors.xml";
	private final static String DEFAULT_XML_FILE_CR_ERRORS = "Fix\\CardReaderErrors.xml";
	private final static String DEFAULT_XML_FILE_PNTR_ERRORS = "Fix\\PrinterErrors.xml";
	private final static byte JOURNAL_LOG_FILE_TYPE = 0x13;
	private final static byte JOURNAL_LOG_FILE_ID =	0x03;
	private final static byte JOURNAL_HIST_LOG_FILE_ID = (byte) 0X83;
	private final static byte PINPAD_LOG_FILE_TYPE = 0x13;
	private final static byte PINPAD_LOG_FILE_ID = (byte) 0x80;
	private final static String REATDER_IN_STARTUP_STATE = "05";
	private final static String KEYBOARD_IN_STARTUP_STATE = "05";
	private final static String PRINTER_IN_STARTUP_STATE = "04";
	private final static String CONNECTION_ERROR_MSG = "Unable to connect\n\n"
			+ "Following may be the reasons for Connection Error:\n"
			+ "1. Bad Ethernet cable or UPM not plugged into AFP or DCM2.\n"
			+ "(Look for Ethernet LEDs."
			+ "See if AFP or DCM2 has any lights as indication that it is working.)\n"
			+ "2. UPM not powered.\n"
			+ "(Look for any lit LEDs on the back of UPM. If unlit, check power cable and trace voltage back to source. If no problem found, replace UPM.)\n"
			+ "3. Incorrect IP address for the UPM (check in secure menu.)\n"
			+ "4. Technician's PC is plugged into the wrong port on the DCM2.\n"
			+ "5. UPM is tampered. (Look for blinking tamper LED.)";
	
	
	private static TroubleshootController troubleshootController;
	private static TroubleshootMainDialog troubleshootView;
	public static SPOTInfo spotInfo = new SPOTInfo();
	public static boolean isConnected;
	public static DeviceInfo systemInfo;
	public static DeviceInfo upmInfo;
	public static DeviceInfo cardReaderInfo;
	public static DeviceInfo displayInfo;
	public static DeviceInfo securityInfo;
	public static DeviceInfo printerInfo;
	public static boolean isConnectBtnPressed = false;
	public static String previousIP = "";
	public static long startTime = 0;
	public static long endTime = 0;
	public static boolean isMixPkgIssueFound = false;
	public static boolean isLogCheckSkip = true;
	public static String techID = "";
	
	private Connection connection;
	private TroubleshootConfigurations configurations;
	private String statusMsg = "";
	private ErrorFixStepsDialog errorDialog;
	private SelectDirectoryDialog selectDialogInstance;
	private URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
	
	public boolean isTampered = false;
	public String selectedPath = null;
	
	public enum Version
	{
		DELTA_C(4),
		DELTA_E(5),
		DELTA_F(6);
		
		private int versionNumber;
		
		Version(int versionNumber)
		{
			this.versionNumber = versionNumber;
		}
		
		public int getVersion()
		{
			return versionNumber;
		}
	}
	
	private TroubleshootController()
	{
		isConnected = false; 
		configurations = TroubleshootConfigurations.getInstance();
		connection = Connection.getInstance();
		systemInfo = new DeviceInfo();
		upmInfo = new DeviceInfo();
		cardReaderInfo = new DeviceInfo();
		displayInfo = new DeviceInfo();
		securityInfo = new DeviceInfo();
		printerInfo = new DeviceInfo();
	}
	
	public static synchronized TroubleshootController getInstance()
	{
		if(troubleshootController == null)
		{
			troubleshootController = new TroubleshootController();
		}
		return troubleshootController;
	}
	
	public static TroubleshootMainDialog getTroubleshootView() throws NullPointerException
	{
		if(troubleshootView != null)
		{
			return troubleshootView;
		}
		else
		{
			throw new NullPointerException("View is null"); 
		}
	}
	
	public void showEolGui()
	{
		troubleshootView = new TroubleshootMainDialog(troubleshootController);
		troubleshootView.showDialog();
	}
	
	public DeviceInfo getInfo(Device deviceType)
	{
		if(Device.UPM == deviceType)
		{
			return upmInfo;
		}
		else if(Device.CARD_READER == deviceType)
		{
			return cardReaderInfo;
		}
		else if(Device.DISPLAY == deviceType)
		{
			return displayInfo;
		}
		else if(Device.SECURITY == deviceType)
		{
			return securityInfo;
		}
		else if(Device.PRINTER == deviceType)
		{
			return printerInfo;
		}
		return null;
	}
	
	public void connectToSpot()
	{
		TroubleshootConfigurations.getInstance().updateLastUsedIp(spotInfo.getUPMip());
		if(connection.doConnectAndLogin(spotInfo.getUPMip(), Integer.parseInt(configurations.getUPMport()), TroubleshootConfigurations.getCertificatePath(), true))
		{
			sleep(2000);
			isConnected = true;
			troubleshootView.setConnected(true);
			collectInformation();
			if(isMixPackagesPresent())
			{
				isMixPkgIssueFound = true;
				showMessageDlg("Software versions are Incompatible.\nPlease replace the UPM", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				checkUPMlogs();
			}
		}
		else
		{
			showConnectionError();
			troubleshootView.setConnected(false);
			troubleshootView.enableMenuPanel(true);
		}
		troubleshootView.enableAll(true);
	}
	
	public boolean doConnect()
	{
		int count = 3;
		while( count>0 )
		{
			if(connection.doConnectAndLogin(spotInfo.getUPMip(), Integer.parseInt(configurations.getUPMport()), TroubleshootConfigurations.getCertificatePath(), true))
			{
				return true;
			}
			count--;
		}
		return false;
	}
	
	public boolean doConnectOnceOnly()
	{
		if(connection.doConnectAndLogin(spotInfo.getUPMip(), Integer.parseInt(configurations.getUPMport()), TroubleshootConfigurations.getCertificatePath(), true))
		{
			return true;
		}
		return false;
	}
	
	public void close()
	{
		EventManager.getInstance().setStopped(true);
		if(connection.pingTimer != null)
		{
			connection.pingTimer.stop();
		}
		System.exit(0);
	}
	
	public void disconnect()
	{
		if(connection.doDisconnect())
		{
			troubleshootView.setConnected(false);
		}
		troubleshootView.clearAll();
		troubleshootView.enableAll(true);
		troubleshootView.enableMenuPanel(true);
	}
	
	public void handleSocketDisconnect()
	{
		troubleshootView.setConnected(false);
		troubleshootView.clearAll();
		troubleshootView.enableAll(true);
		troubleshootView.enableMenuPanel(true);
	}
	
	public static void sleep(long timeInMillis)
	{
		try 
		{
			Thread.sleep(timeInMillis);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void collectInformation()
	{
		if(isConnected)
		{
			new InfoTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void collectOPTInformation(boolean isRefresh)
	{
		if(isConnected)
		{
			new OPTInfoTask(isRefresh).execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void getFirmwareInformation()
	{
		if(isConnected)
		{
			TroubleshootController.getTroubleshootView().enableAll(false);
			new PackageFirmwareInfoTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private boolean isSideDefined()
	{
		if(DiagnosticInformation.getInstance().collectDiagnosticInfo())
		{
			if(DiagnosticInformation.rollCallInfo.getSide().equalsIgnoreCase("undefined"))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void startErrorFixing(ErrorFileParser fileParser, Device deviceType)
	{
		if(isConnected)
		{
			if(new File(TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_UPM_ERRORS).exists() && new File(TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_CR_ERRORS).exists() && new File(TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_PNTR_ERRORS).exists())
			{
				//get error code here 
				/*Error Code iError: 0  = no error , 1 = no error find device is ok , 2 = xml file not present*/
				String status;
				String statusExt; 
				String error; 
				String errorFileToParse;
				int  iError = -1;
				String errorMessage = "";
				
				FixActionReport fixActionReport = null;
				
				if(deviceType == Device.UPM)
				{
					if(isSecurityStatusOk())
					{
						iError = -1;
						showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						boolean isStop = false;
						TroubleshootInstructionDialog dialog = null;
						Vector<Tag> tags = new Vector<Tag>();
						
						while(!isStop)
						{
							if(isSideDefined())
							{
								isStop = true;
								errorMessage = "Security : " + securityInfo.getStatus().toString() + " Keyboard : " + upmInfo.getStatus().toString() + " Display : "+displayInfo.getStatus().toString();
								fixActionReport = new FixActionReport(false, deviceType, errorMessage);
								deviceType = Device.SECURITY;
								errorFileToParse = TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_UPM_ERRORS;
								status = getInfo(deviceType).getStatus().getCommunication();
								statusExt = getInfo(deviceType).getStatus().getDismount();
								error = getInfo(deviceType).getStatus().getError();
								iError = fileParser.searchError(errorFileToParse, status, statusExt, error, fixActionReport);
							}
							else
							{
								tags.clear();
								tags.add(new Tag("message", "Please check and connect USB cable between UPM and PIP3.", 10, 30, 470, 40, "header", "center"));
								tags.add(new Tag("image", "images//UPM_PIP3_usb.jpg", 10, 70, 470, 150, "image", "center"));
								tags.add(new Tag("button", "Reboot", 100, 240, 90, 30, "hard_reboot", "center"));
								tags.add(new Tag("button", "Exit", 290, 240, 90, 30, "exit", "center"));
					
								dialog = new TroubleshootInstructionDialog(tags);
								dialog.showDialog();
								if(dialog.isExit)
								{
									isStop = dialog.isExit;
								}
								else
								{
									tags.clear();
									tags.add(new Tag("message", "Dispenser is Rebooting. Please wait", 10, 30, 470, 20, "header", "center"));
									tags.add(new Tag("image", "images//Loader.gif", 10, 60, 470, 150, "wait_reboot", "center"));
									
									dialog = new TroubleshootInstructionDialog(tags);
									dialog.showDialog();
								}
							}
						}
					}
				}
				else if(deviceType == Device.CARD_READER)
				{
					errorMessage = "Card Reader : " + cardReaderInfo.getStatus().toString();
					fixActionReport = new FixActionReport(false, deviceType, errorMessage);
					errorFileToParse = TroubleshootConfigurations.getResourcesDirectory() +  DEFAULT_XML_FILE_CR_ERRORS;
					status = getInfo(deviceType).getStatus().getCommunication();
					statusExt = getInfo(deviceType).getStatus().getDismount();
					error = getInfo(deviceType).getStatus().getError();
					logger.info("Communication : " + status + " Dismount : " + statusExt + " Error : " + error);
					iError = fileParser.searchError(errorFileToParse, status, statusExt, error, fixActionReport);
				}
				else if(deviceType == Device.PRINTER)
				{
					errorMessage = "Printer : " + printerInfo.getStatus().toString();
					fixActionReport = new FixActionReport(false, deviceType, errorMessage);
					errorFileToParse = TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_PNTR_ERRORS;
					status = getInfo(deviceType).getStatus().getCommunication();
					statusExt = getInfo(deviceType).getStatus().getDismount();
					error = getInfo(deviceType).getStatus().getError();
					logger.info("Communication : " + status + " Dismount : " + statusExt + " Error : " + error);
					iError = fileParser.searchError(errorFileToParse, status, "XX", error, fixActionReport);
				}
				
				if(iError==0)
				{
					if(fixActionReport != null)
					{
						ReportInformation.getInstance().fixActionReports.add(fixActionReport);
					}
					logger.info("Error fix step successfully completed");
				}
				else if(iError==1)
				{
					if(deviceType == Device.CARD_READER)
					{
						if(isCardReaderSystemStatusOK())
						{
							showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else if(deviceType == Device.PRINTER)
					{
						if(isPrinterSystemStatusOK())
						{
							showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						if(isSecurityStatusOk())
						{
							showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				else if(iError==2)
				{
					showMessageDlg("XML file not present", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				showMessageDlg("XML file not present", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
		troubleshootView.enableAll(true);
	}
	
	public void startErrorFixing(ErrorFileParser fileParser, Device deviceType, String status, String statusExt , String error, FixActionReport fixActionReport)
	{
		if(isConnected)
		{
			if(new File(TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_UPM_ERRORS).exists() && new File(TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_CR_ERRORS).exists() && new File(TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_PNTR_ERRORS).exists())
			{
				//get error code here 
				/*Error Code iError: 0  = no error , 1 = no error find device is ok , 2 = xml file not present*/
				String errorFileToParse;
				int  iError = -1;
				if(deviceType == Device.UPM)
				{
					errorFileToParse = TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_UPM_ERRORS;
					iError = fileParser.searchError(errorFileToParse ,status , statusExt , error, fixActionReport);
				}
				else if(deviceType == Device.CARD_READER)
				{
					errorFileToParse = TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_CR_ERRORS;
					logger.info("Communication : " + status + " Dismount : " + statusExt + " Error : " + error);
					iError = fileParser.searchError(errorFileToParse ,status , statusExt , error, fixActionReport);
				}
				else if(deviceType == Device.PRINTER)
				{
					errorFileToParse = TroubleshootConfigurations.getResourcesDirectory() + DEFAULT_XML_FILE_PNTR_ERRORS;
					logger.info("Communication : " + status + " Dismount : " + statusExt + " Error : " + error);
					iError = fileParser.searchError(errorFileToParse ,status , "XX" , error, fixActionReport);
				}
				
				if(iError==0)
				{
					logger.info("Error fix step successfully completed");
				}
				else if(iError==1)
				{
					if(deviceType == Device.CARD_READER)
					{
						if(isCardReaderSystemStatusOK())
						{
							showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else if(deviceType == Device.PRINTER)
					{
						if(isPrinterSystemStatusOK())
						{
							showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						if(isSecurityStatusOk())
						{
							showMessageDlg("No Error.\nDevice is OK.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				else if(iError==2)
				{
					showMessageDlg("XML file not present", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				showMessageDlg("XML file not present", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void showErrorParsingStepsDialog(ErrorFileParser fileParser, Vector<Tag> vecTags, FixActionReport fixActionReport)
	{
		errorDialog = new ErrorFixStepsDialog(fileParser, vecTags, fixActionReport);
		errorDialog.setAlwaysOnTop( true );
		errorDialog.setModal( true );
		errorDialog.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
		errorDialog.setTitle( "Fix");
		
		// Finally here the control is transfered to the dialog
		errorDialog.setVisible( true );
 		//errorDialog.showErrorFixSteps();
	}
	
	public void warmRebootSPOT(byte resetType)
	{
		if(isConnected)
		{
			isConnected = false;
			troubleshootView.setConnected(false);
			connection.warmReboot(resetType);
		}
	}
	
	public boolean checkSPOTUp()
	{
		while(isConnected == false)
		{
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			if(connection.doConnectAndLogin(spotInfo.getUPMip(), Integer.parseInt(configurations.getUPMport()), TroubleshootConfigurations.getCertificatePath(), true))
			{
				isConnected = true;
				troubleshootView.setConnected(true);
				collectInformation();
				collectOPTInformation(true);
			}
			else
			{
				troubleshootView.setConnected(false);
			}
		}
		
		return isConnected;
	}
	
	public void testCardReader()
	{
		if(isConnected)
		{
			TroubleshootController.getTroubleshootView().enableAll(false);
			new TestReaderTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void testUPM()
	{
		if(isConnected)
		{
			TroubleshootController.getTroubleshootView().enableAll(false);
			new TestUpmTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void testPrinter()
	{
		if(isConnected)
		{
			PrinterDialog.getInstance().enableAll(false);
			new TestPrinterTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void checkUPMlogs()
	{
		if(isConnected)
		{
			new CheckUPMLogsTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void checkUPMlogsFix(LogError logError)
	{
		if(isConnected)
		{
			CheckUPMLogsFixTask checkUPMLogsFixTask = new CheckUPMLogsFixTask(logError);
			checkUPMLogsFixTask.execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void runSelfcheck()
	{
		if(isConnected)
		{
			TroubleshootController.getTroubleshootView().enableAll(false);
			new RunSelfCheckTask().execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void generateReport(String dispenserSerialNo, String serviceRequestNo)
	{
		if(isConnected)
		{
			TroubleshootController.getTroubleshootView().enableAll(false);
			new GenerateReportTask(dispenserSerialNo, serviceRequestNo).execute();
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void ViewReport()
	{
		try
		{
			File reportDirectory = new File(TroubleshootConfigurations.getReportsDirectoryPath());
			if(reportDirectory.exists())
			{
				Desktop.getDesktop().open(reportDirectory);
			}
			else
			{
				showMessageDlg("'Reports' directory does not exist.", "View Report", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (IOException e)
		{
			showMessageDlg("Error while opening File Explorer", "View Report", JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void showPinpadDialog()
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				PinpadDialog.getInstance().showDialog();
			}
		});
	}
	
	public void nonSecureMenu()
	{
		if(connection.goToServiceMenu())
		{
			logger.info("Login to non secure mode");
		}
	}
	
	public boolean checkTamper()
	{
		/* Download Journal and pinpad log files*/
		boolean isDone = false;
		Vector<Resource> allResources = new Vector<Resource>();
		Resource resourceJournal = new Resource(DEFAULT_JOURANAL_LOG_FILE, JOURNAL_LOG_FILE_TYPE, JOURNAL_LOG_FILE_ID, FileDownload.DEFAULT_LOG_FOLDER, DEFAULT_JOURANAL_LOG_FILE_EXT);
		Resource resourcePinpad = new Resource(DEFAULT_PINPAD_LOG_FILE, PINPAD_LOG_FILE_TYPE, PINPAD_LOG_FILE_ID, FileDownload.DEFAULT_LOG_FOLDER, DEFAULT_PINPAD_LOG_FILE_EXT);
		allResources.add(resourceJournal);
		allResources.add(resourcePinpad);
		if(FileDownload.getInstance().startDownload(allResources, false))
		{
			File journal = new File(FileDownload.DEFAULT_LOG_FOLDER + "//JOURNAL.log");
			File pinpad = new File (FileDownload.DEFAULT_LOG_FOLDER + "//PINPAD_HIST");
			Date firstDate=null;
			if(journal.exists())
			{
				/*First read the JOURNAl file and find the >>>>>> START DATE*/			
				firstDate = ParsePinPadLogsFile.getInstance().deviceLastStartDate(resourceJournal.getLogFilePath());
			}
			else
			{
				firstDate = downloadJournalHist();
			}
			if(pinpad.exists())
			{
				/*Get the current date which is as last date here*/
				Date lastDate =  currentDateofSystem();
				File currFile = new File(resourcePinpad.getLogFilePath() );
				File DestDirFolder = new File(FileDownload.DEFAULT_LOG_FOLDER);
				FileCompression.getInstance().uncompress(currFile, DestDirFolder);
				File uncompressFilesDirectory = new File(FileDownload.DEFAULT_LOG_FOLDER +"//gilbarco//logs");
				ParsePinPadLogsFile.getInstance().logByDateAction(uncompressFilesDirectory.toString(),firstDate,lastDate);
				if(uncompressFilesDirectory.length()!=0)
				{
					ParsePinPadLogsFile.getInstance().unZipExtractedTarFiles(uncompressFilesDirectory, ".txt");
					isTampered = ParsePinPadLogsFile.getInstance().searchKeyWord(uncompressFilesDirectory,DEFAULT_KEYWORD_TAMPER);
				}
				if(isTampered)
				{
					statusMsg = "tampered";
				}
				else
				{
					statusMsg = "not tampered";
				}
				isDone = true;
				EdtFileUtil.delete(DestDirFolder, true);
			}
			else
			{
				logger.info("Pinpad and journal logs download failed");
			}
		}
		else
		{
			logger.info("Pinpad and journal logs download failed");
		}
		logger.info("Parsing pinpad logs process completed");
		return isDone;
		
	}
	
	public Date downloadJournalHist()
	{
		Vector<Resource> allResources = new Vector<Resource>();
		Resource resourceJournalHist = new Resource(DEFAULT_JOURANAL_HIST_LOG_FILE, JOURNAL_LOG_FILE_TYPE, JOURNAL_HIST_LOG_FILE_ID, FileDownload.DEFAULT_TEMP_FOLDER, "");
		allResources.add(resourceJournalHist);
		Date firstDate =null;
		logger.info("Start download journal history logs");
		if(FileDownload.getInstance().startDownload(allResources, false))
		{
			File journal = new File(FileDownload.DEFAULT_LOG_FOLDER + "//JOURNAL_HIST");
			File pinpad = new File (FileDownload.DEFAULT_LOG_FOLDER + "//PINPAD_HIST");
			if(journal.exists() && pinpad.exists())
			{
				File currFile = new File(resourceJournalHist.getLogFilePath() );
				File DestDirFolder = new File(FileDownload.DEFAULT_TEMP_FOLDER);
				FileCompression.getInstance().uncompress(currFile, DestDirFolder);
				File uncompressFilesDirectory = new File(FileDownload.DEFAULT_TEMP_FOLDER +"//gilbarco//logs");
				File journalfileNearestDate=ParsePinPadLogsFile.getInstance().journalLogOfNearestDate(uncompressFilesDirectory.toString());
				ParsePinPadLogsFile.getInstance().unZipExtractedTarFiles(journalfileNearestDate, ".txt");
				firstDate = ParsePinPadLogsFile.getInstance().deviceLastStartDate(FilenameUtils.getFullPath(journalfileNearestDate.toString()) +FilenameUtils.getBaseName(journalfileNearestDate.toString()) + ".txt");
			}
			else
			{
				logger.info("Pinpad and journal logs download failed");
			}
		}
		return firstDate;
	}
	
	public String getStatusMsg()
	{
		return statusMsg;
	}
	
	public Date currentDateofSystem()
	{
		Date lastDateOfFile = null;
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		try 
		{
			lastDateOfFile = new SimpleDateFormat("yyyyMMdd").parse(sdf.format(date));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return lastDateOfFile;
	}
	
	public void startDownloadCurrentLogs()
	{
		if(isConnected)
		{
			/* choose directory */
			String destinationLogsFolder = TroubleshootConfigurations.getInstance().getUpmLogsFolder();
			String title ="Download Current UPM Logs" ;
			selectDialogInstance = new SelectDirectoryDialog();
			selectDialogInstance.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
			selectDialogInstance.chooseDirectory( title, destinationLogsFolder );
		}
		else
		{
			showMessageDlg("Please connect to SPOT first", "info", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	public boolean collectLogFile(File selectedFile)
	{
		boolean bResult = false;
		Resource [] values = null;
		Vector<Resource> allResources = new Vector<Resource>();
		String sLogsDownloadPath = selectedFile.getAbsolutePath(); 
		Path sessionDirectory = getDefaultLogsSessionDirectory( sLogsDownloadPath );
		values=FileResourceLogStandardConfig.getInstance().upmCurrentValues(sessionDirectory.toString());
		if(values.length > 0)
		{
			for(int i = 0 ; i<values.length ; i++)
			{
				allResources.add(values[i]);
			}
		}
		
		logger.info("Start download current logs");
		if(FileDownload.getInstance().startDownload(allResources, false))
		{
			logger.info("Current logs downloaded successfully");
			bResult = true;
		}
		return bResult;
	}
	
	
	/**
	 * Gets the result expected directory for download logs
	 * @param the default download path
	 * @return the path generated
	 * */
	private Path getDefaultLogsSessionDirectory(String sLogsDownloadPath)
	{
		Path sessionDirectory;
		String sCurrentIp = spotInfo.getUPMip();		
		try
		{
			sessionDirectory = Paths.get( sLogsDownloadPath, sCurrentIp+"_"+ EdtUtil.getDateTimeStamp( "yyyy-MM-dd'T'HHmmss" ) ); // The path is built with the download_path/target_unit_ip/timestamp/
		}
		catch (RuntimeException e)
		{
			sessionDirectory = Paths.get( sLogsDownloadPath, sCurrentIp+"_tmp" );
		}
		return sessionDirectory;
	}

	public static void showMessageDlg(String message, String title, int messageType)
	{
		OptionDialog.showOptionDialog(title, message, JOptionPane.DEFAULT_OPTION, messageType, true);
	}
	
	public static int showOptionDlg(String message, String title, int optionType, int messageType)
	{
		return OptionDialog.showOptionDialog(title, message, optionType, messageType, false);
	}
	
	public static MessageDialog getMessageDialog(String message, String title, int messageType)
	{
		return MessageDialog.getMessageDialog(title, messageType, message);
	}
	
	public static void showConnectionError()
	{
		UIManager.put("OptionPane.background", Color.white);
		UIManager.put("Panel.background", Color.white);
		JOptionPane.showMessageDialog(troubleshootView, CONNECTION_ERROR_MSG, "Connection Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static Point getCenterLocation(JFrame dlg)
	{
		Point centerPoint = null;
		Dimension dlgDimension = dlg.getSize();
		Point dlgPoint = dlg.getLocationOnScreen();
		centerPoint = new Point( (dlgPoint.x+(dlgDimension.width/2)), (dlgPoint.y+(dlgDimension.height/2)) );
		return centerPoint;
	}
	
	public boolean isCardReaderSystemStatusOK()
	{
		if(DeviceInformation.getInstance().getInformation())
		{
			StatusValidator.getInstance().validate(Device.UPM);
			StatusValidator.getInstance().validate(Device.CARD_READER);
			
			DeviceStatus status = cardReaderInfo.getStatus();
			if(status.getCommunication().equals(REATDER_IN_STARTUP_STATE))
			{
				return isCardReaderSystemStatusOK();
			}
			else if( (status.getCommunication().equals("03") ||status.getCommunication().equals("04")) ||
					 (status.getDismount().equals("00")) ||
					 (!status.getError().equals("00")))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	public String isUPMStatusOK()
	{
		String error = "";
		
		if(DeviceInformation.getInstance().getInformation())
		{
			StatusValidator.getInstance().validate(Device.UPM);
			StatusValidator.getInstance().validate(Device.CARD_READER);
			
			DeviceStatus upmStatus = upmInfo.getStatus();
			DeviceStatus securityStatus = securityInfo.getStatus();
			DeviceStatus displayStatus = displayInfo.getStatus();
			
			if(securityStatus.toString().equals("000000"))
			{
				if( (upmStatus.toString().equals("000000")) && (displayStatus.toString().equals("000000")) )
				{
					error = "bothDism";
				}
				else if(upmStatus.toString().equals("000000"))
				{
					error = "keyDism";
				}
				else if(displayStatus.toString().equals("000000"))
				{
					error = "dispDism";
				}
				else
				{
					error = "Unknown Error";
				}
			}
			else
			{
				error = "Unknown Error";
			}
		}
		else
		{
			error = "Info Error";
		}
		
		return error;
	}
	
	public boolean isSecurityStatusOk()
	{
		if(DeviceInformation.getInstance().getInformation())
		{
			StatusValidator.getInstance().validate(Device.UPM);
			StatusValidator.getInstance().validate(Device.CARD_READER);
			
			DeviceStatus upmStatus = upmInfo.getStatus();
			DeviceStatus securityStatus = securityInfo.getStatus();
			DeviceStatus displayStatus = displayInfo.getStatus();
			
			if(securityStatus.toString().equals("000000"))
			{
				if( (upmStatus.toString().equals("000000")) || (displayStatus.toString().equals("000000")) )
				{
					return false;
				}
			}
			else
			{
				if(upmStatus.getCommunication().equals(KEYBOARD_IN_STARTUP_STATE))
				{
					return isSecurityStatusOk();
				}
				else if( (upmStatus.getCommunication().equals("03") ||upmStatus.getCommunication().equals("04")) ||
						 (upmStatus.getDismount().equals("00")) ||
						 (!upmStatus.getError().equals("00")))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isPrinterSystemStatusOK()
	{
		if(OPTDeviceInformation.getInstance().getInformation())
		{
			StatusValidator.getInstance().validate(Device.PRINTER);
			
			DeviceStatus status = printerInfo.getStatus();
			if(status.getCommunication().equals(PRINTER_IN_STARTUP_STATE))
			{
				return isPrinterSystemStatusOK();
			}
			else if( (status.getCommunication().equals("01")) || (!status.getError().equals("00")) )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	//check mix packages of delta c and delta e only
	public boolean isMixPackagesPresent()
	{
		try
		{
			ArrayList<Integer> versions = new ArrayList<Integer>();
			
			if(PackageInformation.getInstance().collectPkgInfoFromUPM())
			{
				Vector<ApplicationVersion> firmwareInfo = PackageInformation.getInstance().getAppVersionsFromSpot();
				if(firmwareInfo != null && !firmwareInfo.isEmpty())
				{
					versions.clear();
					int buildVersion = getBuildVersion(firmwareInfo);
					
					if(buildVersion != 0)
					{
						if(buildVersion == Version.DELTA_F.getVersion())
						{
							versions.add(Version.DELTA_C.getVersion());
							return isOtherPackages(versions, firmwareInfo);
						}
						else if(buildVersion == Version.DELTA_E.getVersion())
						{
							versions.add(Version.DELTA_C.getVersion());
							return isOtherPackages(versions, firmwareInfo);
						}
						else if(buildVersion == Version.DELTA_C.getVersion())
						{
							versions.add(Version.DELTA_F.getVersion());
							versions.add(Version.DELTA_E.getVersion());
							return isOtherPackages(versions, firmwareInfo);
						}
					}
				}
			}
			else
			{
				logger.error("Failed to collect package information");
			}
		}
		catch(InterruptedException e)
		{
			logger.error(e.getMessage());
		}
		return false;
	}
	
	private int getBuildVersion(Vector<ApplicationVersion> firmwareInfo)
	{
		for (ApplicationVersion applicationVersion : firmwareInfo)
		{
			if(applicationVersion.getAppId().equalsIgnoreCase("OS"))
			{
				String buildNumber = applicationVersion.getBuildNumber();
				String numbers[] = buildNumber.split("\\.");
				if(numbers.length >= 2)
				{
					int middleNumber = Integer.parseInt(numbers[1]);
					return middleNumber;
				}
				break;
			}
		}
		return 0;
	}
	
	private boolean isOtherPackages(ArrayList<Integer> versions, Vector<ApplicationVersion> firmwareInfo)
	{
		boolean isMixed = false;
		
		logger.info("Below are the packages having other Build Number : ");
		for (ApplicationVersion applicationVersion : firmwareInfo)
		{
			String appName = applicationVersion.getAppId();
			if(appName.equalsIgnoreCase("RESOURCE") || appName.equalsIgnoreCase("CRINDBIOS") || appName.equalsIgnoreCase("CLOUD"))
			{
				continue;
			}
			else
			{
				String buildNumber = applicationVersion.getBuildNumber();
				String numbers[] = buildNumber.split("\\.");
				if(numbers.length >= 2)
				{
					int middleNumber = Integer.parseInt(numbers[1]);
					if(isMixed(middleNumber, versions))
					{
						logger.info(appName + " : "+ buildNumber);
						isMixed = true;
					}
				}
			}
		}
		return isMixed;
	}
	
	private boolean isMixed(int middleNumber, ArrayList<Integer> versions)
	{
		for (Integer version : versions)
		{
			if(middleNumber == version)
			{
				return true;
			}
		}
		
		return false;
	}
}
