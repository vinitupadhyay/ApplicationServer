package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;

public class TroubleshootMainDialog extends JFrame 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TroubleshootController troubleshootController;
	
	private GridBagConstraints gridBagConstraints;
	private MenuPanel menuPanel;
	private LabelPanel labelPanel;
	private DevicePanel upmPanel;
	private DevicePanel cardReaderPanel;
	private PeripheralPanel otherDevicePanel;
	private TroubleShootingPanel troubleShootingPanel;
	
	public static TroubleshootMainDialog mainDialogInstance;
	
	public TroubleshootMainDialog(TroubleshootController troubleshootController) 
	{
		this.troubleshootController = troubleshootController;
		this.initialise();
	}
	
	public TroubleshootMainDialog getMainDialogInstance()
	{
		return mainDialogInstance;
	}
	
	private void initialise()
	{
		windowSettings();
		addMenuPanel();
		addLabelPanel();
		addUPMPanel();
		addCardReaderPanel();
		addOtherPeripheralPanel();
		addTroubleShootingPanel();
		this.pack();
	}
	
	private void windowSettings()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenDimension =  toolkit.getScreenSize();
		int width = (int)screenDimension.getWidth()/2 - 350;
		int height = (int)screenDimension.getHeight()/2 - 300;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(725,600));
		this.setResizable(true);
		this.setLayout(new GridBagLayout());
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
	
	
	private void addMenuPanel()
	{
		menuPanel = new MenuPanel(troubleshootController);
		menuPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(0, 0, 5, 1, GridBagConstraints.BOTH, 0, 0);
		this.add(menuPanel, gridBagConstraints);
	}
	
	private void addLabelPanel()
	{
		labelPanel = new LabelPanel(Device.UPM);
		labelPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(0, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(labelPanel, gridBagConstraints);
	}
	
	private void addUPMPanel()
	{
		upmPanel = new DevicePanel(troubleshootController, Device.UPM);
		upmPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(1, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(upmPanel, gridBagConstraints);
	}
	
	private void addCardReaderPanel()
	{
		cardReaderPanel = new DevicePanel(troubleshootController, Device.CARD_READER);
		cardReaderPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(2, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(cardReaderPanel, gridBagConstraints);
	}
	
	private void addOtherPeripheralPanel()
	{
		otherDevicePanel = new PeripheralPanel();
		otherDevicePanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(3, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(otherDevicePanel, gridBagConstraints);
	}
	
	private void addTroubleShootingPanel()
	{
		
		troubleShootingPanel = new TroubleShootingPanel();
		troubleShootingPanel.setBackground(Color.white);
		gridBagConstraints = getConstraint(4, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		this.add(troubleShootingPanel, gridBagConstraints);
		
		
		this.getRootPane( ).getActionMap( ).put( "ON_EXIT", new AbstractAction( )
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				closeDialog( );
			}
		} );
		
		this.getRootPane( ).getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( KeyStroke.getKeyStroke( "ESCAPE" ), "ON_EXIT" );
		
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent)
		    {
		    	closeDialog();
		    }
		});
	}
	
	public void showDialog()
	{
		mainDialogInstance = this;
		this.setTitle("M7 SPOT Troubleshooting Tool v00.00.07");
		this.setIcon();
		this.setLocationRelativeTo( null );
		this.setAlwaysOnTop(false);
		this.setVisible(true);
	}
	
	private void setIcon()
	{
		URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
		if (url != null)
		{
			this.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
		}
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
		TroubleshootController.getInstance().close();
	}
	
	public void clearAll()
	{
		cardReaderPanel.clearAll();
		upmPanel.clearAll();
		if(PrinterDialog.getInstance() != null)
		{
			PrinterDialog.getInstance().clearAll();
		}
	}
	
	public void clearAllSystemStatus()
	{
		cardReaderPanel.clearAllSystemStatus();
		upmPanel.clearAllSystemStatus();
		if(PrinterDialog.getInstance() != null)
		{
			PrinterDialog.getInstance().clearAllSystemStatus();
		}
	}
	
	public void enableAll(final boolean isEnable)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				menuPanel.enableAll(isEnable);
				cardReaderPanel.enableAll(isEnable);
				upmPanel.enableAll(isEnable);
				otherDevicePanel.enableAll(isEnable);
				troubleShootingPanel.enableAll(isEnable);
				labelPanel.enableAll(isEnable);
			}
		});
	}
	
	public void enableMenuPanel(boolean isEnable)
	{
		menuPanel.enableMenuPanel(isEnable);
	}

	public MenuPanel getMenuPanel()
	{
		return menuPanel;
	}

	public DevicePanel getDevicePanel(Device deviceType)
	{
		if(Device.UPM == deviceType)
		{
			return upmPanel;
		}
		else if(Device.CARD_READER == deviceType)
		{
			return cardReaderPanel;
		}
		else if(Device.PRINTER == deviceType)
		{
			return PrinterDialog.getInstance().getDevicePanel();
		}
		return null;
	}

	public PeripheralPanel getOtherDevicePanel()
	{
		return otherDevicePanel;
	}

	public TroubleShootingPanel getTroubleShootingPanel()
	{
		return troubleShootingPanel;
	}
	
	public void setConnected(boolean isConnected)
	{
		menuPanel.setConnected(isConnected);
	}
	
	// status setters
	
	public void showSerialNumber(Device deviceType, String serNumber)
	{
		getDevicePanel(deviceType).showSerialNumber(serNumber);
	}
	
	public void showCommunicationStaus(Device deviceType, String label, Color color, Color fgColor, String status)
	{
		getDevicePanel(deviceType).showCommunicationStaus(label, color, fgColor);
		getDevicePanel(deviceType).showStatusMessage(status);
	}
	
	public void showDismountStatus(Device deviceType, String label, Color color, Color fgColor, String status)
	{
		getDevicePanel(deviceType).showDismountStaus(label, color, fgColor);
		getDevicePanel(deviceType).showStatusMessage(status);
	}
	
	public void showErrorStatus(Device deviceType, String label, Color color, Color fgColor, String status)
	{
		getDevicePanel(deviceType).showErrorStaus(label, color, fgColor);
		getDevicePanel(deviceType).showStatusMessage(status);
	}
	
	public void showStatusMsg(Device deviceType, String status)
	{
		getDevicePanel(deviceType).showStatusMessage(status);
	}
	
	public void showTestStatus(Device deviceType, TestStatus status)
	{
		getDevicePanel(deviceType).showTestStatus(status);
	}
}
