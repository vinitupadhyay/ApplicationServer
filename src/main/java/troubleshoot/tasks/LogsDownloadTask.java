package troubleshoot.tasks;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.views.SelectDirectoryDialog;

public class LogsDownloadTask extends SwingWorker<Boolean, Void>
{
	private File selectedFile;
	private SelectDirectoryDialog dialogInstance;
	public LogsDownloadTask(File selectedFile ,SelectDirectoryDialog instance)
	{
		this.selectedFile=selectedFile;
		dialogInstance = instance;
	}
	
	public Boolean doInBackground()
	{
		return TroubleshootController.getInstance().collectLogFile(selectedFile);
	}
	
	public void done()
	{
		try 
		{
			if(get())
			{
				dialogInstance.resetDownloadScreen("Current logs downloaded successfully", true);
				dialogInstance.enableAll();
			}
			else
			{
				dialogInstance.resetDownloadScreen("Current logs download failed", false);
				dialogInstance.enableAll();
			}
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		} 
		catch (ExecutionException e) 
		{
			e.printStackTrace();
		}
				
	}
}
