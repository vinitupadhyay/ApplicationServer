package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.OPTDeviceInformation;
import troubleshoot.models.StatusValidator;
import troubleshoot.states.Device;
import troubleshoot.views.PrinterDialog;

public class OPTInfoTask extends SwingWorker<Boolean, Void>
{
	private final static Logger logger = Logger.getLogger(OPTInfoTask.class);
	
	private OPTDeviceInformation deviceInformation = null;
	private boolean isRefresh = false;
	
	public OPTInfoTask(boolean isRefresh)
	{
		this.isRefresh = isRefresh;
		deviceInformation = OPTDeviceInformation.getInstance();
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
				StatusValidator.getInstance().validate(Device.PRINTER);
				if(!isRefresh)
				{
					PrinterDialog.getInstance().showDialog();
				}
			}
			else
			{
				TroubleshootController.showMessageDlg("Error while retriving OPT Device Information", "Error", JOptionPane.ERROR_MESSAGE);
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
