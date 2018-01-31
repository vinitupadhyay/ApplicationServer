package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.ReportInformation;
import troubleshoot.util.EdtStringUtil;
import troubleshoot.util.IpValidator;

public class MenuPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private JLabel lblIpAddress;
	private JTextField txtFieldIp1;
	private JTextField txtFieldIp2;
	private JTextField txtFieldIp3;
	private JTextField txtFieldIp4;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JRadioButton rbtnSideA;
	private JRadioButton rbtnSideB;
	
	private GridBagConstraints gridBagConstraints;
	
	private TroubleshootController troubleshootController;
	
	public MenuPanel(TroubleshootController controller) 
	{
		this.troubleshootController = controller;
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addIP();
		addRadioBtn();
		addBtn();
	}
	
	private void windowSettings()
	{
		this.setPreferredSize(new Dimension(600, 60));
		TitledBorder border = new TitledBorder("Info");
		border.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setBorder(border);
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
	
	private void addIP()
	{
		lblIpAddress = new JLabel("Unit IP Address: ");
		//lblSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 0, 1, 1, GridBagConstraints.BOTH, 0, 0);
		lblIpAddress.setFont(new Font(font, Font.PLAIN, fontSize));
		lblIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblIpAddress.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblIpAddress, gridBagConstraints);
		
		txtFieldIp1 = new  JTextField("");
		gridBagConstraints = getConstraint(1, 0, 1, 1, GridBagConstraints.BOTH, 0.07, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtFieldIp1.setFont(new Font(font, Font.PLAIN, fontSize));
		txtFieldIp1.setEditable(true);
		txtFieldIp1.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldIp1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		txtFieldIp1.addKeyListener(getIpFieldKeyListener(1, 255));
		this.add(txtFieldIp1, gridBagConstraints);
		
		txtFieldIp2 = new  JTextField("");
		gridBagConstraints = getConstraint(2, 0, 1, 1, GridBagConstraints.BOTH, 0.07, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtFieldIp2.setFont(new Font(font, Font.PLAIN, fontSize));
		txtFieldIp2.setEditable(true);
		txtFieldIp2.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldIp2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		txtFieldIp2.addKeyListener(getIpFieldKeyListener(0, 255));
		this.add(txtFieldIp2, gridBagConstraints);
		
		txtFieldIp3 = new  JTextField("");
		gridBagConstraints = getConstraint(3, 0, 1, 1, GridBagConstraints.BOTH, 0.07, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtFieldIp3.setFont(new Font(font, Font.PLAIN, fontSize));
		txtFieldIp3.setEditable(true);
		txtFieldIp3.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldIp3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		txtFieldIp3.addKeyListener(getIpFieldKeyListener(0, 255));
		this.add(txtFieldIp3, gridBagConstraints);
		
		txtFieldIp4 = new  JTextField("");
		gridBagConstraints = getConstraint(4, 0, 1, 1, GridBagConstraints.BOTH, 0.07, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtFieldIp4.setFont(new Font(font, Font.PLAIN, fontSize));
		txtFieldIp4.setEditable(true);
		txtFieldIp4.setHorizontalAlignment(SwingConstants.CENTER);
		txtFieldIp4.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		txtFieldIp4.addKeyListener(getIpFieldKeyListener(1, 254));
		this.add(txtFieldIp4, gridBagConstraints);
		
		String ipAddr = TroubleshootController.spotInfo.getUPMip();
		if(!ipAddr.isEmpty())
		{
			String[] octets = ipAddr.split("\\.");
			if(octets.length == 4)
			{
				txtFieldIp1.setText(octets[0]);
				txtFieldIp2.setText(octets[1]);
				txtFieldIp3.setText(octets[2]);
				txtFieldIp4.setText(octets[3]);
			}
		}
	}
	
	private void addBtn()
	{
		btnConnect = new JButton("Connect");
		gridBagConstraints = getConstraint(5, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnConnect.setHorizontalAlignment(SwingConstants.CENTER);
		btnConnect.setVerticalAlignment(SwingConstants.CENTER);
		btnConnect.setFont(new Font(font, Font.PLAIN, fontSize));
		btnConnect.setVisible(true);
		this.add(btnConnect, gridBagConstraints);
		btnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(!TroubleshootController.spotInfo.getUPMip().isEmpty())
				{
					TroubleshootController.getTroubleshootView().enableAll(false);
					TroubleshootController.getTroubleshootView().enableMenuPanel(false);
					new Thread(new Runnable() {
						
						@Override
						public void run()
						{
							if(!TroubleshootController.isConnectBtnPressed || (!TroubleshootController.previousIP.equals(TroubleshootController.spotInfo.getUPMip())))
							{
								TroubleshootController.isConnectBtnPressed = true;
								TroubleshootController.isMixPkgIssueFound = false;
								TroubleshootController.isLogCheckSkip = true;
								TroubleshootController.previousIP = TroubleshootController.spotInfo.getUPMip();
								TroubleshootController.startTime = System.currentTimeMillis();
								ReportInformation.getInstance().clearReport();
								ReportInformation.isUPMFirstTime = true;
								ReportInformation.isOPTFirstTime = true;
							}
							troubleshootController.connectToSpot();
						}
					}).start();
				}
				else
				{
					TroubleshootController.showMessageDlg("Please insert valid IP address", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		btnDisconnect = new JButton("Disconnect");
		gridBagConstraints = getConstraint(5, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnDisconnect.setHorizontalAlignment(SwingConstants.CENTER);
		btnDisconnect.setVerticalAlignment(SwingConstants.CENTER);
		btnDisconnect.setFont(new Font(font, Font.PLAIN, fontSize));
		btnDisconnect.setVisible(false);
		this.add(btnDisconnect, gridBagConstraints);
		btnDisconnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new Thread(new Runnable() {
					
					@Override
					public void run()
					{
						if(TroubleshootController.isConnected)
						{
							TroubleshootController.getTroubleshootView().enableAll(false);
							troubleshootController.disconnect();
						}
					}
				}).start();
				
			}
		});
	}
	
	private void addRadioBtn()
	{
		rbtnSideA = new JRadioButton("Side A");
		rbtnSideA.setFocusPainted(false);
		rbtnSideA.setHorizontalAlignment(SwingConstants.CENTER);
		rbtnSideA.setBackground(Color.white);
		rbtnSideA.setFont(new Font(font, Font.PLAIN, fontSize));
		rbtnSideA.setSelected(true);
		//rbtnSideA.setBorder(BorderFactory.createLineBorder(Color.black));
		
		rbtnSideA.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
			}
		});
		
		rbtnSideB = new JRadioButton("Side B");
		rbtnSideB.setHorizontalAlignment(SwingConstants.CENTER);
		//rbtnSideB.setBorder(BorderFactory.createLineBorder(Color.black));
		rbtnSideB.setBackground(Color.white);
		rbtnSideB.setFont(new Font(font, Font.PLAIN, fontSize));
		rbtnSideB.setFocusPainted(false);
		rbtnSideB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
	
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbtnSideA);
		bg.add(rbtnSideB);
		
		this.add(rbtnSideA, getConstraint(6, 0, 1, 1, GridBagConstraints.BOTH, 0.05, 0.05));
		this.add(rbtnSideB, getConstraint(7, 0, 1, 1, GridBagConstraints.BOTH, 0.05, 0.05));
	}
	
	private KeyAdapter getIpFieldKeyListener(final int min, final int max)
	{
		return new KeyAdapter( ) // First field value has to be between 1 and 255.
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				char code = e.getKeyChar( );
				String text = ((JTextField) e.getSource( )).getText( );
				String textselected = ((JTextField) e.getSource( )).getSelectedText( );
				if ((code != KeyEvent.VK_BACK_SPACE && code != KeyEvent.VK_CLEAR && code != KeyEvent.VK_DELETE)
								&& (!Character.isDigit( code ) || e.isAltDown( ) || ((text.length( ) == 3) && ((textselected == null) || (textselected.length( ) == 0)))))
				{
					e.consume( );
					return;
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if(isKeyCodeAllowed( e ) )
					return;

				JTextField ipField = (JTextField) e.getSource( );
				String ipFieldText = ipField.getText( );

				if (EdtStringUtil.isStringNumeric( ipFieldText ))
				{
					if (ipFieldText.length( ) > 3)
					{
						ipFieldText = ipFieldText.substring( 0, 3 );
					}
					try
					{
						int ipFieldValue = Integer.parseInt( ipFieldText );
						if ((ipFieldValue >= min) && (ipFieldValue <= max)) //>0 <=255 
										{
							ipFieldText = Integer.toString( ipFieldValue );
										}
						else if (ipFieldValue > max)
						{
							ipFieldText = Integer.toString( ipFieldValue ).substring( 0, 2 );
						}
						else
						{
							ipFieldText = "";
						}
					}
					catch (NumberFormatException ex)
					{
						ipFieldText = "";
					}
				}
				else
				{
					assert false : "Shouldn't be here !";
					ipFieldText = "";
				}
				
				setIpFieldText( ipField, ipFieldText );
				saveCurrentIp( );
			}
		};
	}
	
	private void setIpFieldText(JTextField ipField, String ipFieldText)
	{
		if(!ipField.getText( ).equals( ipFieldText))
		{
			ipField.setText( ipFieldText );
		}
	}
	
	/**
	 * Save current ip.
	 */
	private void saveCurrentIp()
	{
		String ip1 = EdtStringUtil.trimLeadingZeros( txtFieldIp1.getText( ) );
		String ip2 = EdtStringUtil.trimLeadingZeros( txtFieldIp2.getText( ) );
		String ip3 = EdtStringUtil.trimLeadingZeros( txtFieldIp3.getText( ) );
		String ip4 = EdtStringUtil.trimLeadingZeros( txtFieldIp4.getText( ) );
		
		TroubleshootController.spotInfo.setUPMip(IpValidator.joinIP( ip1, ip2, ip3, ip4 ));
	}
	
	/**
	 * Checks if the event represents an arrow key code allowed.
	 *
	 * @param e the KeyEvent
	 * @return true, if is the key code allowed
	 */
	private boolean isKeyCodeAllowed(KeyEvent e)
	{
		return !Character.isDigit(e.getKeyChar( )) && e.getKeyCode( )>36 && e.getKeyCode( )<40;
	}
	
	public void enableAll(boolean isEnable)
	{
		btnConnect.setEnabled(isEnable);
		btnDisconnect.setEnabled(isEnable);
	}
	
	public void enableMenuPanel(boolean isEnable)
	{
		txtFieldIp1.setEnabled(isEnable);
		txtFieldIp2.setEnabled(isEnable);
		txtFieldIp3.setEnabled(isEnable);
		txtFieldIp4.setEnabled(isEnable);
		
		rbtnSideA.setEnabled(isEnable);
		rbtnSideB.setEnabled(isEnable);
	}
	
	public void setConnected(final boolean isConnected)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				TroubleshootController.isConnected = isConnected;
				btnConnect.setEnabled(!isConnected);
				btnConnect.setVisible(!isConnected);
				btnDisconnect.setEnabled(isConnected);
				btnDisconnect.setVisible(isConnected);
			}
		});
	}
}
