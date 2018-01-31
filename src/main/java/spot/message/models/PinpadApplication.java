package spot.message.models;

import troubleshoot.models.Connection;
import troubleshoot.models.DeviceInformation;
import troubleshoot.models.DiagnosticInformation;
import troubleshoot.models.FileBrowse;
import troubleshoot.models.FileDownload;
import troubleshoot.models.FileUpload;
import troubleshoot.models.PackageInformation;
import troubleshoot.models.SPOTDisplay;
import troubleshoot.models.TestReader;

public class PinpadApplication 
{
	public static final String CMD_SYSTEM_STATUS = "1001";
	public static final String CMD_HARDWARE_CONFIG = "1002";
	public static final String CMD_SOFTWARE_CONFIG = "1003";
	public static final String CMD_TEMP_ACTIVATION = "10FF";
	public static final String CMD_FILE_DOWNLOAD = "0005";
	public static final String CMD_FILE_BLOCK_DOWNLOAD = "0006";
	public static final String CMD_PACKAGE_INFO = "6003";
	public static final String CMD_EXT_DIAGNOSTIC_INFO = "100A";
	public static final String CMD_UX300_LOG_FILE_UPLOAD = "200A";
	public static final String CMD_CR_TRACK_DATA = "2003";
	public static final String CMD_FILE_UPLOAD = "0001";
	public static final String CMD_FILE_BLOCK_UPLOAD = "0002";
	public static final String CMD_FILE_BROWSE = "0003";
	public static final String CMD_FILE_DELETE = "0004";
	public static final String CMD_DISPLAY_STATUS = "300F";
	public static final String CMD_INPUT_ENABLE = "4001";
	public static final String CMD_SMC_SWITCH = "3011";
	
	
	private static PinpadApplication pinpadApplication = null;
	
	public static PinpadApplication getInstance()
	{
		if(pinpadApplication == null)
		{
			pinpadApplication = new PinpadApplication();
		}
		return pinpadApplication;
	}
	
	public void processIncommingMessage(SpotApdu apdu)
	{
		if(apdu.getCmd().equals(CMD_SYSTEM_STATUS))
		{
			DeviceInformation.getInstance().processSystemStatusResponse(apdu);
			TestReader.getInstance().processSystemStatusResponse(apdu);
		}
		else if(apdu.getCmd().equals(CMD_HARDWARE_CONFIG))
		{
			DeviceInformation.getInstance().processHardwareConfigurationResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_SOFTWARE_CONFIG))
		{
			DeviceInformation.getInstance().processSoftwareConfigurationResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_TEMP_ACTIVATION))
		{
			Connection.getInstance().processTempActivationAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_FILE_DOWNLOAD))
		{
			FileDownload.getInstance().processFileDownloadAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_FILE_BLOCK_DOWNLOAD))
		{
			FileDownload.getInstance().processFileBlockDownloadAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_PACKAGE_INFO))
		{
			PackageInformation.getInstance().processPackageInfoAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_EXT_DIAGNOSTIC_INFO))
		{
			DiagnosticInformation.getInstance().processExtDiagnosticAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_UX300_LOG_FILE_UPLOAD))
		{
			FileDownload.getInstance().uxLogFileUploadResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_CR_TRACK_DATA))
		{
			TestReader.getInstance().processCRTrackDataResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_FILE_UPLOAD))
		{
			FileUpload.getInstance().handleFileUploadResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_FILE_BLOCK_UPLOAD))
		{
			FileUpload.getInstance().handleFileUploadResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_FILE_BROWSE))
		{
			FileBrowse.getInstance().handleFileBrowseResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_FILE_DELETE))
		{
			FileUpload.getInstance().handleFileDeleteResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_DISPLAY_STATUS))
		{
			SPOTDisplay.getInstance().handleDisplayStatusResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_INPUT_ENABLE))
		{
			SPOTDisplay.getInstance().handleInputEnableResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_SMC_SWITCH))
		{
			SPOTDisplay.getInstance().handleSMCResponse(apdu);
		}
	}
}
