package troubleshoot.tasks;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.FixActionReport;
import troubleshoot.model.pojo.LogError;
import troubleshoot.model.pojo.ReportInformation;
import troubleshoot.states.Device;
import troubleshoot.xml.ErrorFileParser;

public class CheckUPMLogsFixTask extends SwingWorker<Boolean, Void>
{
	private final static Logger logger = Logger.getLogger(CheckUPMLogsFixTask.class);
	private static final String ACTIONS_FOLDER_PATH = TroubleshootConfigurations.getResourcesDirectory() + "CheckUPMLogs\\Actions";
	
	private LogError logError;
	private ErrorFileParser xmlParser;

	public CheckUPMLogsFixTask(LogError logError)
	{
		this.logError = logError;
		this.xmlParser = new ErrorFileParser();
	}
	
	@Override
	protected Boolean doInBackground() throws Exception
	{
		try
		{
			String filePath = ACTIONS_FOLDER_PATH + "\\" + logError.getActionFilePath();
			File actionFile = new File(filePath);
			if(actionFile.exists())
			{
				Device deviceType = Device.valueOf(logError.getCategory().toUpperCase());
				FixActionReport fixActionReport = new FixActionReport(true, deviceType, logError.getErrorString(), logError.getActionFilePath());
				ReportInformation.getInstance().fixActionReports.add(fixActionReport);
				xmlParser.setErrorStepsFileName(ACTIONS_FOLDER_PATH + "\\" + logError.getActionFilePath());
				xmlParser.setNodeName("step");
				xmlParser.searchSteps(fixActionReport);
			}
			else
			{
				String errorMsg = actionFile + "file not found";
				TroubleshootController.showMessageDlg(errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return false;
	}
}
