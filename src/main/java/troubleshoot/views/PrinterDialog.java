package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;

public class PrinterDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static PrinterDialog printerDialog = null;
	
	private GridBagConstraints gridBagConstraints;
	private DevicePanel printerPanel;
	private LabelPanel labelPanel;
	
	private PrinterDialog() 
	{
		this.initialise();
	}
	
	public static PrinterDialog getInstance()
	{
		if(printerDialog == null)
		{
			printerDialog = new PrinterDialog();
		}
		return printerDialog;
	}
	
	private void initialise()
	{
		windowSettings();
		addLabelPanel();
		addPrinterPanel();
		this.pack();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 175;
		int height = parentLocation.y - 250;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(350, 500));
		this.setLayout(new GridBagLayout());
		this.setResizable(true);
		this.getContentPane().setBackground(Color.white);
	}
	
	private GridBagConstraints getConstraint(int gridx, int gridy, int gridWidth, int gridHeight, int fill, double weightx, double weighty)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridWidth;
		constraints.gridheight = gridHeight;
		constraints.fill = fill;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		
		return constraints;
	}
	
	private void addLabelPanel()
	{
		labelPanel = new LabelPanel(Device.PRINTER);
		labelPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(0, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(labelPanel, gridBagConstraints);
	}
	
	private void addPrinterPanel()
	{
		printerPanel = new DevicePanel(TroubleshootController.getInstance(), Device.PRINTER);
		printerPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(1, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(printerPanel, gridBagConstraints);
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	private void setIcon()
	{
		URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
		if (url != null)
		{
			this.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
		}
	}
	
	public void showDialog()
	{
		this.setTitle("Troubleshooting");
		this.setModal(true);
		this.setIcon();
		this.setVisible(true);
	}
	
	public void clearAll()
	{
		printerPanel.clearAll();
	}
	
	public void clearAllSystemStatus()
	{
		printerPanel.clearAllSystemStatus();
	}
	
	public void enableAll(final boolean isEnable)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				printerPanel.enableAll(isEnable);
				labelPanel.enableAll(isEnable);
			}
		});
	}

	public DevicePanel getDevicePanel()
	{
		return printerPanel;
	}
	
	// status setters
	
	public void showSerialNumber(Device deviceType, String serNumber)
	{
		getDevicePanel().showSerialNumber(serNumber);
	}
	
	public void showCommunicationStaus(Device deviceType, String label, Color color, Color fgColor, String status)
	{
		getDevicePanel().showCommunicationStaus(label, color, fgColor);
		getDevicePanel().showStatusMessage(status);
	}
	
	public void showDismountStatus(Device deviceType, String label, Color color, Color fgColor, String status)
	{
		getDevicePanel().showDismountStaus(label, color, fgColor);
		getDevicePanel().showStatusMessage(status);
	}
	
	public void showErrorStatus(Device deviceType, String label, Color color, Color fgColor, String status)
	{
		getDevicePanel().showErrorStaus(label, color, fgColor);
		getDevicePanel().showStatusMessage(status);
	}
	
	public void showStatusMsg(Device deviceType, String status)
	{
		getDevicePanel().showStatusMessage(status);
	}
	
	public void showTestStatus(Device deviceType, TestStatus status)
	{
		getDevicePanel().showTestStatus(status);
	}
}
