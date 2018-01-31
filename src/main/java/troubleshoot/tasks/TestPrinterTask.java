package troubleshoot.tasks;

import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.TestPrinter;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;
import troubleshoot.views.PrinterDialog;

public class TestPrinterTask extends SwingWorker<Boolean, Void>
{
private final static Logger logger = Logger.getLogger(TestPrinterTask.class);
	
	private TestPrinter testPrinter = null;
	private PrinterDialog printerDialog = null;
	
	public TestPrinterTask()
	{
		testPrinter = TestPrinter.getInstance();
		printerDialog = PrinterDialog.getInstance();
	}

	@Override
	protected Boolean doInBackground() throws Exception
	{
		return testPrinter.startTest();
	}

	@Override
	protected void done()
	{
		try
		{
			printerDialog.enableAll(true);

			if (get( )) //is successful??
			{
				printerDialog.showTestStatus(Device.PRINTER, TestStatus.PASS);
				TroubleshootController.showMessageDlg("Please collect the Printout.", "Test Printer", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				printerDialog.showTestStatus(Device.PRINTER, TestStatus.FAIL);
				TroubleshootController.showMessageDlg("Test Failed.\nUnable to Print.", "Test Printer", JOptionPane.ERROR_MESSAGE);
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
