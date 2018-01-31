package spot.message.models;

import troubleshoot.models.OPTDeviceInformation;
import troubleshoot.models.TestPrinter;

public class OPTApplication
{
	public static final String CMD_SYSTEM_STATUS = "1001";
	public static final String CMD_HARDWARE_CONFIG = "1002";
	public static final String CMD_SOFTWARE_CONFIG = "1003";
	public static final String CMD_CREATE_JOB = "2004";
	public static final String CMD_ENQUEUE_DATA = "2002";
	public static final String CMD_EXECUTE_JOB = "2005";
	public static final String CMD_JOB_STATUS = "2003";
	
	private static OPTApplication optApplication = null;
	
	public static OPTApplication getInstance()
	{
		if(optApplication == null)
		{
			optApplication = new OPTApplication();
		}
		return optApplication;
	}
	
	public void processIncommingMessage(SpotApdu apdu)
	{
		if(apdu.getCmd().equals(CMD_SYSTEM_STATUS))
		{
			OPTDeviceInformation.getInstance().processSystemStatusResponse(apdu);
		}
		else if(apdu.getCmd().equals(CMD_HARDWARE_CONFIG))
		{
			OPTDeviceInformation.getInstance().processHardwareConfigurationResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_SOFTWARE_CONFIG))
		{
			OPTDeviceInformation.getInstance().processSoftwareConfigurationResponse(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_CREATE_JOB))
		{
			TestPrinter.getInstance().processCreateJobAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_ENQUEUE_DATA))
		{
			TestPrinter.getInstance().processEnqueueDataAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_EXECUTE_JOB))
		{
			TestPrinter.getInstance().processExecuteJobAnswer(apdu);
		}
		else if(apdu.getCmd().equalsIgnoreCase(CMD_JOB_STATUS))
		{
			TestPrinter.getInstance().processJobStatusAnswer(apdu);
		}
	}
}
