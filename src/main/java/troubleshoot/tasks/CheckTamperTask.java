package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.xml.ButtonAction;
import troubleshoot.xml.ErrorFixStepsDialog;

public class CheckTamperTask extends SwingWorker<Boolean, Void>
{
	private final static Logger logger = Logger.getLogger(CheckTamperTask.class);
	
	
	
	protected Boolean doInBackground()
	{
		logger.info("Parsing pinpad logs process start");
		return TroubleshootController.getInstance().checkTamper();
	}
	
	protected void done()
	{
		try 
		{
			if(get())
			{

				if(ErrorFixStepsDialog.instance != null)
				{
					ErrorFixStepsDialog.instance.closeDialog();
				}
				else
				{
					System.out.println("instance is null");
				}
				if(TroubleshootController.getInstance().isTampered)
				{
					ErrorFixStepsDialog.instance.buttonActionPerformed(ButtonAction.YES, "Yes");
				}
				else
				{
					ErrorFixStepsDialog.instance.buttonActionPerformed(ButtonAction.NO, "No");
				}
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
