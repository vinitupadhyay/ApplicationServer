package troubleshoot.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;

public class LabelPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private Device deviceType;
	
	private JButton btnRefresh;
	private JLabel lblDevSerialNo;
	private JLabel lblCommunication;
	private JLabel lblDismount;
	private JLabel lblError;
	private JLabel lblStatus;
	private JLabel lblTestStatus;
	
	private GridBagConstraints gridBagConstraints;
	
	public LabelPanel(Device deviceType) 
	{
		this.deviceType = deviceType;
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addLabels();
	}
	
	private void windowSettings()
	{
		this.setPreferredSize(new Dimension(600, 60));
		/*TitledBorder border = new TitledBorder("Label");
		border.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setBorder(border);*/
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
	
	private void addLabels()
	{
		btnRefresh = new JButton("Refresh");
		gridBagConstraints = getConstraint(0, 0, 1, 1, GridBagConstraints.BOTH, 0.07, 0.07);
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		btnRefresh.setHorizontalAlignment(SwingConstants.CENTER);
		btnRefresh.setVerticalAlignment(SwingConstants.CENTER);
		btnRefresh.setFont(new Font(font, Font.PLAIN, fontSize));
		btnRefresh.setVisible(true);
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(deviceType == Device.PRINTER)
				{
					PrinterDialog.getInstance().clearAllSystemStatus();
					TroubleshootController.getInstance().collectOPTInformation(true);
				}
				else
				{
					TroubleshootController.getTroubleshootView().clearAllSystemStatus();
					TroubleshootController.getInstance().collectInformation();
				}
			}
		});
		
		this.add(btnRefresh, gridBagConstraints);
		
		lblDevSerialNo = new JLabel("");
		if(deviceType == Device.PRINTER)
		{
			lblDevSerialNo.setText("Type");
		}
		else
		{
			lblDevSerialNo.setText("Device Serial No.");
		}
		//lblDevSerialNo.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 1, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblDevSerialNo.setFont(new Font(font, Font.BOLD, fontSize));
		lblDevSerialNo.setHorizontalAlignment(SwingConstants.LEFT);
		lblDevSerialNo.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblDevSerialNo, gridBagConstraints);
		
		lblCommunication = new JLabel("");
		if(deviceType == Device.PRINTER)
		{
			lblCommunication.setText("State");
		}
		else
		{
			lblCommunication.setText("Communication?");
		}
		//lblCommunication.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 3, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblCommunication.setFont(new Font(font, Font.BOLD, fontSize));
		lblCommunication.setHorizontalAlignment(SwingConstants.LEFT);
		lblCommunication.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblCommunication, gridBagConstraints);
		
		lblDismount = new JLabel("");
		if(deviceType == Device.PRINTER)
		{
			lblDismount.setText("Additional Info");
		}
		else
		{
			lblDismount.setText("Dismount?");
		}
		//lblDismount.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 4, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblDismount.setFont(new Font(font, Font.BOLD, fontSize));
		lblDismount.setHorizontalAlignment(SwingConstants.LEFT);
		lblDismount.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblDismount, gridBagConstraints);
		
		lblError = new JLabel("Errors?");
		//lblError.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 5, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblError.setFont(new Font(font, Font.BOLD, fontSize));
		lblError.setHorizontalAlignment(SwingConstants.LEFT);
		lblError.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblError, gridBagConstraints);
		
		lblStatus = new JLabel("Status:");
		//lblStatus.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 6, 1, 1, GridBagConstraints.BOTH, 0.10, 0.4);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblStatus.setFont(new Font(font, Font.BOLD, fontSize));
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatus.setVerticalAlignment(SwingConstants.TOP);
		this.add(lblStatus, gridBagConstraints);
		
		JLabel lblempty2 = new JLabel(" ");
		//lblempty2.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 7, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblempty2.setFont(new Font(font, Font.PLAIN, fontSize));
		lblempty2.setHorizontalAlignment(SwingConstants.LEFT);
		lblempty2.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblempty2, gridBagConstraints);
		
		JLabel lblempty3 = new JLabel(" ");
		//lblempty3.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 8, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblempty3.setFont(new Font(font, Font.PLAIN, fontSize));
		lblempty3.setHorizontalAlignment(SwingConstants.LEFT);
		lblempty3.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblempty3, gridBagConstraints);
		
		if(deviceType == Device.PRINTER)
		{
			JLabel lblempty4 = new JLabel(" ");
			//lblempty4.setBorder(BorderFactory.createLineBorder(Color.black));
			gridBagConstraints = getConstraint(0, 9, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			lblempty4.setFont(new Font(font, Font.PLAIN, fontSize));
			lblempty4.setHorizontalAlignment(SwingConstants.LEFT);
			lblempty4.setVerticalAlignment(SwingConstants.CENTER);
			this.add(lblempty4, gridBagConstraints);
			
			lblTestStatus = new JLabel("Test Status:");
			//lblTestStatus.setBorder(BorderFactory.createLineBorder(Color.black));
			gridBagConstraints = getConstraint(0, 10, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			lblTestStatus.setFont(new Font(font, Font.BOLD, fontSize));
			lblTestStatus.setHorizontalAlignment(SwingConstants.LEFT);
			lblTestStatus.setVerticalAlignment(SwingConstants.CENTER);
			this.add(lblTestStatus, gridBagConstraints);
			
		}
		else
		{
			lblTestStatus = new JLabel("Test Status:");
			//lblTestStatus.setBorder(BorderFactory.createLineBorder(Color.black));
			gridBagConstraints = getConstraint(0, 9, 1, 1, GridBagConstraints.BOTH, 0.09, 0.09);
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			lblTestStatus.setFont(new Font(font, Font.BOLD, fontSize));
			lblTestStatus.setHorizontalAlignment(SwingConstants.LEFT);
			lblTestStatus.setVerticalAlignment(SwingConstants.CENTER);
			this.add(lblTestStatus, gridBagConstraints);
		}
	}
	
	public void enableAll(boolean isEnable)
	{
		btnRefresh.setEnabled(isEnable);
	}

}
