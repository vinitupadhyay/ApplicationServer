package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.OPTDeviceInformation;
import troubleshoot.models.PackageInformation;
import troubleshoot.views.PackageInfoView;

public class PackageFirmwareInfoTask extends SwingWorker<Boolean, Void>
{
	private final static Logger logger = Logger.getLogger(PackageFirmwareInfoTask.class);
	private PackageInformation pkgInfo = null;
	
	public PackageFirmwareInfoTask()
	{
		pkgInfo = PackageInformation.getInstance();
	}
	
	@Override
	protected Boolean doInBackground() throws Exception
	{
		if(OPTDeviceInformation.getInstance().getInformation())
		{
			return pkgInfo.collectPkgInfoFromUPM();
		}
		return false;
	}
	
	@Override
	protected void done()
	{
		try
		{
			TroubleshootController.getTroubleshootView().enableAll(true);
			if (get( )) //is successful??
			{
				PackageInfoView infoView = new PackageInfoView(pkgInfo.getAppVersionsFromSpot(), TroubleshootController.cardReaderInfo, TroubleshootController.printerInfo);
				infoView.showDialog();
			}
			else
			{
				TroubleshootController.showMessageDlg("Error while retrieving Firmware Version \nInformation", "Error", JOptionPane.ERROR_MESSAGE);
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
