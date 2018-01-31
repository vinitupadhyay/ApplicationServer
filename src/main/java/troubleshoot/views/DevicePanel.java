package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;
import troubleshoot.xml.ErrorFileParser;

public class DevicePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorRed = Color.decode("#ff3333");
	private static final Color colorYellow = Color.decode("#ffff80");
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private JLabel lblDeviceName;
	private JTextField txtSerialNo;
	private JTextField txtCommunication;
	private JTextField txtDismount;
	private JTextField txtError;
	private JTextPane txtStatus;
	private JButton btnFix;
	private JButton btnTestDevice;
	private JTextField txtTestResult;
	
	private Device deviceType;
	private GridBagConstraints gridBagConstraints;
	private TroubleshootController troubleshootController;
	
	public DevicePanel(TroubleshootController controller, Device deviceType) 
	{
		this.deviceType = deviceType;
		this.troubleshootController = controller;
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addComponents();
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
	
	private void addComponents()
	{
		lblDeviceName = new JLabel(getDeviceName());
		//lblDeviceName.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		if(deviceType == Device.UPM)
		{
			gridBagConstraints.ipadx = 90;
		}
		lblDeviceName.setFont(new Font(font, Font.BOLD, 15));
		lblDeviceName.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeviceName.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblDeviceName, gridBagConstraints);
		
		txtSerialNo = new  JTextField("");
		gridBagConstraints = getConstraint(0, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtSerialNo.setFont(new Font(font, Font.PLAIN, fontSize));
		txtSerialNo.setEditable(false);
		txtSerialNo.setHorizontalAlignment(SwingConstants.CENTER);
		txtSerialNo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(txtSerialNo, gridBagConstraints);
		
		txtCommunication = new  JTextField("");
		gridBagConstraints = getConstraint(0, 3, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtCommunication.setFont(new Font(font, Font.PLAIN, fontSize));
		txtCommunication.setEditable(false);
		txtCommunication.setHorizontalAlignment(SwingConstants.CENTER);
		txtCommunication.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(txtCommunication, gridBagConstraints);
		
		txtDismount = new  JTextField("");
		gridBagConstraints = getConstraint(0, 4, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtDismount.setFont(new Font(font, Font.PLAIN, fontSize));
		txtDismount.setEditable(false);
		txtDismount.setHorizontalAlignment(SwingConstants.CENTER);
		txtDismount.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(txtDismount, gridBagConstraints);
		
		txtError = new  JTextField("");
		gridBagConstraints = getConstraint(0, 5, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtError.setFont(new Font(font, Font.PLAIN, fontSize));
		txtError.setEditable(false);
		txtError.setHorizontalAlignment(SwingConstants.CENTER);
		txtError.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(txtError, gridBagConstraints);
		
		
		txtStatus = new JTextPane();
		gridBagConstraints = getConstraint(0, 6, 1, 1, GridBagConstraints.BOTH, 0.5, 0.5);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtStatus.setEditable(false);
		txtStatus.setContentType("text/html");
		DefaultCaret caret = (DefaultCaret)txtStatus.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		JScrollPane scrollPane = new JScrollPane(txtStatus);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getHorizontalScrollBar().setAutoscrolls(false);
		this.add(scrollPane, gridBagConstraints);
		
		btnFix = new JButton("Fix");
		gridBagConstraints = getConstraint(0, 7, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(2, 5, 5, 5);
		btnFix.setHorizontalAlignment(SwingConstants.CENTER);
		btnFix.setVerticalAlignment(SwingConstants.CENTER);
		btnFix.setFont(new Font(font, Font.PLAIN, fontSize));
		btnFix.setVisible(true);
		
		this.add(btnFix, gridBagConstraints);
		btnFix.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new Thread(new Runnable() {
					
					@Override
					public void run()
					{
						TroubleshootController.getTroubleshootView().enableAll(false);
						ErrorFileParser fileParser = new ErrorFileParser();
						troubleshootController.startErrorFixing(fileParser, deviceType);
					}
				}).start();
			}
		});
		
		
		btnTestDevice = new JButton("Test "+getDeviceName());
		gridBagConstraints = getConstraint(0, 8, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(2, 5, 5, 5);
		btnTestDevice.setHorizontalAlignment(SwingConstants.CENTER);
		btnTestDevice.setVerticalAlignment(SwingConstants.CENTER);
		btnTestDevice.setFont(new Font(font, Font.PLAIN, fontSize));
		btnTestDevice.setVisible(true);

		this.add(btnTestDevice, gridBagConstraints);
		btnTestDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				setTestStatus(TestStatus.NOT_EXECUTED);
				if(deviceType == Device.UPM)
				{
					troubleshootController.testUPM();
				}
				else if(deviceType == Device.CARD_READER)
				{
					troubleshootController.testCardReader();
				}
				else if(deviceType == Device.PRINTER)
				{
					troubleshootController.testPrinter();
				}
			}
		});
		
		txtTestResult = new  JTextField("");
		gridBagConstraints = getConstraint(0, 9, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		txtTestResult.setFont(new Font(font, Font.PLAIN, fontSize));
		txtTestResult.setEditable(false);
		txtTestResult.setHorizontalAlignment(SwingConstants.CENTER);
		txtTestResult.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setTestStatus(TestStatus.NOT_EXECUTED);
		this.add(txtTestResult, gridBagConstraints);
		
	}

	public void setTestStatus(final TestStatus status)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				switch (status)
				{
					case PASS:
						txtTestResult.setForeground(Color.black);
						txtTestResult.setBackground(colorGreen);
						txtTestResult.setText("PASS");
						break;
					case FAIL:
						txtTestResult.setForeground(Color.white);
						txtTestResult.setBackground(colorRed);
						txtTestResult.setText("FAIL");
						break;
					case NOT_EXECUTED:
						txtTestResult.setForeground(Color.black);
						txtTestResult.setBackground(colorYellow);
						txtTestResult.setText("Test Not Executed");
						break;
			
					default:
						break;
				}
			}
		});
	}
	
	public String getTestStatus()
	{
		return txtTestResult.getText();
	}
	
	public String getCommunication()
	{
		return txtCommunication.getText();
	}
	
	public String getDismount()
	{
		return txtDismount.getText();
	}
	
	public String getError()
	{
		return txtError.getText();
	}
	
	public void enableAll(boolean isEnable)
	{
		btnFix.setEnabled(isEnable);
		btnTestDevice.setEnabled(isEnable);
	}
	
	public void showSerialNumber(String serNumber)
	{
		txtSerialNo.setText(serNumber);
	}
	
	public void showCommunicationStaus(String label, Color color, Color fgColor)
	{
		txtCommunication.setBackground(color);
		txtCommunication.setForeground(fgColor);
		txtCommunication.setText(label);
	}
	
	public void showDismountStaus(String label, Color color, Color fgColor)
	{
		txtDismount.setBackground(color);
		txtDismount.setForeground(fgColor);
		txtDismount.setText(label);
	}
	
	public void showErrorStaus(String label, Color color, Color fgColor)
	{
		txtError.setBackground(color);
		txtError.setForeground(fgColor);
		txtError.setText(label);
	}
	
	public void showStatusMessage(String message)
	{
		try
		{
			if(!message.isEmpty())
			{
				message = "<font face='segoe ui' size='3'><br>" + message + "</br></font>";
				HTMLDocument htmlDoc = (HTMLDocument)txtStatus.getStyledDocument();
				htmlDoc.insertAfterEnd(htmlDoc.getCharacterElement(htmlDoc.getLength()),message);
			}
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showTestStatus(TestStatus status)
	{
		setTestStatus(status);
	}
	
	public void clearAll()
	{
		txtSerialNo.setText("");
		txtSerialNo.setBackground(Color.white);
		txtCommunication.setText("");
		txtCommunication.setBackground(Color.white);
		txtDismount.setText("");
		txtDismount.setBackground(Color.white);
		txtError.setText("");
		txtError.setBackground(Color.white);
		txtStatus.setText("");
		txtStatus.setBackground(Color.white);
		txtTestResult.setText("");
		txtTestResult.setBackground(Color.white);
		setTestStatus(TestStatus.NOT_EXECUTED);
	}
	
	public void clearAllSystemStatus()
	{
		txtSerialNo.setText("");
		txtSerialNo.setBackground(Color.white);
		txtCommunication.setText("");
		txtCommunication.setBackground(Color.white);
		txtDismount.setText("");
		txtDismount.setBackground(Color.white);
		txtError.setText("");
		txtError.setBackground(Color.white);
		txtStatus.setText("");
		txtStatus.setBackground(Color.white);
	}
	
	private String getDeviceName()
	{
		String name = "";
		
		if(deviceType == Device.CARD_READER)
		{
			name = "Card Reader";
		}
		else if(deviceType == Device.UPM)
		{
			name = "UPM";
		}
		else if(deviceType == Device.PRINTER)
		{
			name = "Printer";
		}
		return name;
	}
}
