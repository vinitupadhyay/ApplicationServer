package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.Connection;
import troubleshoot.views.InstructionDialog;

public class RunSelfCheckTask extends SwingWorker<Boolean, Void> 
{
	private final static Logger logger = Logger.getLogger(RunSelfCheckTask.class);
	
	public RunSelfCheckTask()
	{
	}
	
	@Override
	protected Boolean doInBackground() throws Exception
	{
		return Connection.getInstance().goToServiceMenu();
	}

	@Override
	protected void done()
	{
		try
		{
			
			if (get( )) //is successful??
			{
				new InstructionDialog().showDialog("Instructions");
			}
			else
			{
				TroubleshootController.showMessageDlg("Error while logging into maintenance mode", "Error", JOptionPane.ERROR_MESSAGE);
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
}
