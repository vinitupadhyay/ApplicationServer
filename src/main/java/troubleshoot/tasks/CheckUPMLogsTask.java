package troubleshoot.tasks;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.ReportInformation;
import troubleshoot.models.CheckUPMLogs;
import troubleshoot.models.CheckUPMLogs.LogResult;
import troubleshoot.views.ProcessDialog;
import troubleshoot.views.ShowErrorsDialog;

public class CheckUPMLogsTask extends SwingWorker<Void, Void>
{
	private final static Logger logger = Logger.getLogger(CheckUPMLogsTask.class);
	
	private CheckUPMLogs checkLogs = null;
	private ProcessDialog processDialog;
	
	public CheckUPMLogsTask()
	{
		checkLogs = CheckUPMLogs.getInstance();
	}
	
	@Override
	protected Void doInBackground() throws Exception
	{
		if(TroubleshootController.showOptionDlg("Do you want to check the logs for identifying\nthe issues?", "Troubleshooting - Parsing Logs", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0)
		{
			showProcessDialog();
			checkLogs.checkLogs(); 
		}
		else
		{
			if(TroubleshootController.showOptionDlg("Are you sure you do not want to check logs?", "Troubleshooting - Parsing Logs", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
			{
				showProcessDialog();
				checkLogs.checkLogs();
			}
			else
			{
				checkLogs.Status = LogResult.NOT_REQUIRED;
			}
		}
		
		return null;
	}

	@Override
	protected void done()
	{
		try
		{
			if(processDialog != null)
			{
				processDialog.closeDialog();
			}
			
			switch (checkLogs.Status)
			{
				case SUCCESS:
					if(checkLogs.foundErrors.isEmpty())
					{
						TroubleshootController.showMessageDlg("There are no errors in logs", "Check UPM Logs", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						ReportInformation.getInstance().setFoundedErrors(checkLogs.foundErrors);
						ShowErrorsDialog errorsDialog = new ShowErrorsDialog(checkLogs.foundErrors);
						errorsDialog.showDialog();
					}
					break;
					
				case ERROR:
					TroubleshootController.showMessageDlg("Error while parsing the logs", "Error", JOptionPane.ERROR_MESSAGE);
					logger.error("Error while parsing the logs");
					break;
		
				default:
					break;
			}
		}
		catch(Exception e)
		{
			if(processDialog != null)
			{
				processDialog.closeDialog();
			}
			logger.error(e.getMessage());
		}
	} 
	
	private void showProcessDialog()
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				processDialog = new ProcessDialog();
				processDialog.showDialog("Extracting logs...");
			}
		});
	}
}
