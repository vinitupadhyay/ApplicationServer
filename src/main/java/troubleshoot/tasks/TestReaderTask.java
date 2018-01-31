package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.SystemStatusInfo;
import troubleshoot.models.TestReader;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;
import troubleshoot.views.ReaderOutput;
import troubleshoot.views.ShowErrorsDialog;
import troubleshoot.views.TroubleshootMainDialog;

public class TestReaderTask extends SwingWorker<Boolean, Void>
{
	private final static Logger logger = Logger.getLogger(TestReaderTask.class);
	
	private TestReader testReader = null;
	private TroubleshootMainDialog mainView = null;
	
	public TestReaderTask()
	{
		testReader = TestReader.getInstance();
		mainView = TroubleshootController.getTroubleshootView();
	}

	@Override
	protected Boolean doInBackground() throws Exception
	{
		return testReader.startTest();
	}

	@Override
	protected void done()
	{
		try
		{
			TroubleshootController.getTroubleshootView().enableAll(true);
			testReader.isTestingReader = false;
			testReader.disposeReaderDialogs();
			
			if (get( )) //is successful??
			{
				ReaderOutput output = new ReaderOutput();
				mainView.showTestStatus(Device.CARD_READER, TestStatus.PASS);
				output.showDialog(testReader.trackOneStatus, testReader.trackTwoStatus, testReader.trackThreeStatus, testReader.chipStatus);
				ShowErrorsDialog.updateBtnFixInAcion(TestStatus.PASS);
			}
			else
			{
				mainView.showTestStatus(Device.CARD_READER, TestStatus.FAIL);
				ShowErrorsDialog.updateBtnFixInAcion(TestStatus.FAIL);
				switch (testReader.getErrorState())
				{
					case ENABLE_ERROR:
						SystemStatusInfo.getInstance().getCommunicationInfo(Device.CARD_READER, testReader.getReaderInfo().getStatus().getCommunication());
						break;
						
					case ENABLE_TIMEOUT:	
						break;
						
					case READ_ERROR:	
						break;
						
					case READ_TIMEOUT:	
						break;
	
					case NONE :
						break;
				default:
					break;
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
