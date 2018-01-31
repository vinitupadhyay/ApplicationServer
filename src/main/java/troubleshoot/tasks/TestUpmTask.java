package troubleshoot.tasks;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.FileBrowseInfo;
import troubleshoot.model.pojo.ResourceCertificate;
import troubleshoot.models.FileBrowse;
import troubleshoot.models.FileUpload;
import troubleshoot.models.SPOTDisplay;
import troubleshoot.models.VanguardResourceCertificate;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;
import troubleshoot.views.PinpadDialog;
import troubleshoot.views.ShowErrorsDialog;

public class TestUpmTask extends SwingWorker<Boolean, Void>
{

	private final static Logger logger = Logger.getLogger(TestUpmTask.class);
	private static final byte resType = 21;
	private static final byte authType = 2;
	
	private FileBrowse fileBrowse;
	private FileUpload fileUpload;
	private SPOTDisplay spotDisplay;
	private TroubleshootController controller;
	private byte fileId;
	private byte winId;
	private int deviceType;
	
	public TestUpmTask()
	{
		deviceType = 1;
		fileBrowse = FileBrowse.getInstance();
		fileUpload = FileUpload.getInstance();
		spotDisplay = SPOTDisplay.getInstance();
		controller = TroubleshootController.getInstance();
	}
	
	@Override
	protected Boolean doInBackground() throws Exception
	{
		SPOTDisplay.isClosed = false;
		if(fileBrowse.browseFileResource(resType))
		{
			if(isResUploadNeeded())
			{
				fileId = getFileId();
				fileUpload.upgradeResource(getResRelativePath(deviceType), resType, fileId, authType);
				if((!fileUpload.hasErrors( )))
				{
					logger.info("Resource file uploaded successfully");
					return doTest();
				}
				else
				{
					logger.error("error occur while uploading resource");
					if(fileUpload.isErrorInResponse)
					{
						deviceType++;
						return tryToUploadOtherResource();
					}
				}
			}
			else
			{
				logger.info("Resource file already present");
				return doTest();
			}
		}
		else
		{
			logger.error("error occur while resource browsing");
		}
		return false;
	}
	
	@Override
	protected void done()
	{
		try 
		{
			if(get())
			{
				if(PinpadDialog.isBtnOKClicked)
				{
					PinpadDialog.isBtnOKClicked = false;
					TroubleshootController.getTroubleshootView().enableAll(true);
				}
				
				TroubleshootController.getTroubleshootView().showTestStatus(Device.UPM, spotDisplay.testStatus);
				ShowErrorsDialog.updateBtnFixInAcion(spotDisplay.testStatus);
			}
			else
			{
				TroubleshootController.showMessageDlg("Error Occured.\nPlease fix the error using 'Fix' button.", "Error", JOptionPane.ERROR_MESSAGE);
				TroubleshootController.getTroubleshootView().showTestStatus(Device.UPM, spotDisplay.testStatus);
				TroubleshootController.getTroubleshootView().enableAll(true);
			}
		}
		catch (Exception e)
		{
			TroubleshootController.getTroubleshootView().enableAll(true);
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	private boolean tryToUploadOtherResource()
	{
		try
		{
			if(deviceType < 4)
			{
				SPOTDisplay.isClosed = false;
				if(fileBrowse.browseFileResource(resType))
				{
					if(isResUploadNeeded())
					{
						fileId = getFileId();
						fileUpload.upgradeResource(getResRelativePath(deviceType), resType, fileId, authType);
						if((!fileUpload.hasErrors( )))
						{
							logger.info("Resource file uploaded successfully");
							return doTest();
						}
						else
						{
							logger.error("error occur while uploading resource");
							if(fileUpload.isErrorInResponse)
							{
								deviceType++;
								return tryToUploadOtherResource();
							}
						}
					}
					else
					{
						logger.info("Resource file already present");
						return doTest();
					}
				}
				else
				{
					logger.error("error occur while resource browsing");
				}
			}
		}
		catch (InterruptedException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean doTest()
	{
		spotDisplay.testStatus = TestStatus.NOT_EXECUTED;
		if(spotDisplay.collectUsedWindId())
		{
			winId = getUnusedWindowId(spotDisplay.usedWinIds);
			if(spotDisplay.switchWindow(SPOTDisplay.SMC_SECURE))
			{
				spotDisplay.createWindowFromRes(winId, fileId);
				controller.showPinpadDialog();
				SPOTDisplay.isKeyPressed = false;
				spotDisplay.enableInput(winId, (byte)0x03, true, true);
				return true;
			}
			else
			{
				logger.error("Unable to switch secure media connector");
			}
		}
		return false;
	}
	
	private byte getFileId()
	{
		byte fileId = 0;
		for (FileBrowseInfo resource : fileBrowse.fileIdsForReqType)
		{
			if(resource.getFileId() == fileId)
			{
				fileId++;
			}
			else
			{
				return fileId;
			}
		}
		return fileId;
	}
	
	private boolean isResUploadNeeded()
	{
		ResourceCertificate resourceCertificate = new VanguardResourceCertificate(getResRelativePath(deviceType));
		if(resourceCertificate.loadCertificate(true))
		{
			if(fileBrowse.fileIdsForReqType != null && !fileBrowse.fileIdsForReqType.isEmpty())
			{
				for (FileBrowseInfo resource : fileBrowse.fileIdsForReqType)
				{
					if( resource.getFileName().trim().equalsIgnoreCase(getNameFromResInfo(resourceCertificate.getResInfo()).trim()) && 
					resource.getCrc().equalsIgnoreCase(getCrcFromResInfo(resourceCertificate.getResInfo())))
					{
						fileId = resource.getFileId();
						return false;
					}
				}
			}
			else
			{
				logger.error("File Browse return empty list");
			}
			return true;
		}
		return false;
	}
	
	private String getResRelativePath(int deviceType)
	{
		switch (deviceType)
		{
			case 1:
				return "resources/Id001_2_Troubleshoot_D.bin";
			
			case 2:
				return "resources/Id001_2_Troubleshoot_T.bin";
				
			case 3:
				return "resources/Id001_2_Troubleshoot_P.bin";

			default:
				return "resources/Id001_2_Troubleshoot_D.bin";
		}
	}
	
	private String getCrcFromResInfo(byte[] resInfo)
	{
		try
		{
			String sResInfo = new String( resInfo, "UTF-8" );
			String[] info = sResInfo.split(":");
			if(info.length == 5)
			{
				return info[1];
			}
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error(e.getMessage());
		}
		return ""; 
	}
	
	private String getNameFromResInfo(byte[] resInfo)
	{
		try
		{
			String sResInfo = new String( resInfo, "UTF-8" );
			String[] info = sResInfo.split(":");
			if(info.length == 5)
			{
				return info[4];
			}
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error(e.getMessage());
		}
		return "";
	}
	
	private byte getUnusedWindowId(ArrayList<Byte> usedWindIds)
	{
		byte id = 1;
		for (Byte windowId : usedWindIds)
		{
			if(id == windowId)
			{
				id++;
			}
			else
			{
				return id;
			}
		}
		return id;
	}
}
