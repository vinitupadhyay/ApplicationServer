package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.ReportInformation;
import troubleshoot.models.DeviceInformation;
import troubleshoot.models.StatusValidator;
import troubleshoot.states.Device;

public class InfoTask extends SwingWorker<Boolean, Void> 
{
	private final static Logger logger = Logger.getLogger(InfoTask.class);
	private static int refreshCount = 0;
	
	private DeviceInformation deviceInformation = null;
	
	public InfoTask()
	{
		deviceInformation = DeviceInformation.getInstance();
	}
	
	@Override
	protected Boolean doInBackground() throws Exception
	{
		return deviceInformation.getInformation();
	}

	@Override
	protected void done()
	{
		try
		{
			if (get( )) //is successful??
			{
				refreshCount = 0;
				StatusValidator.getInstance().validate(Device.UPM);
				StatusValidator.getInstance().validate(Device.CARD_READER);
				ReportInformation.getInstance().setInformation(true);
			}
			else
			{
				logger.error("Error while retrieving Device Information");
				if(refreshCount < 2)
				{
					refreshCount++;
					new InfoTask().execute();
				}
				else
				{
					TroubleshootController.showMessageDlg("Error while retrieving Device Information", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
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
