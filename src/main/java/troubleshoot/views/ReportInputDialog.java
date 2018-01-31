package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import troubleshoot.controller.TroubleshootController;

public class ReportInputDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private JLabel lblDispenserNo;
	private JLabel lblServiceRequestNo;
	private JTextField txtDispenserField;
	private JTextField txtServiceRequestField;
	private JButton btnSubmit;
	private JLabel lblStatusMsg;
	
	public ReportInputDialog() 
	{
		super(TroubleshootController.getTroubleshootView());
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addComponents();
		this.pack();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 150;
		int height = parentLocation.y - 125;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(300, 250));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	public void addComponents()
	{
		addLabels();;
		addTxtField();
		addSubmitButton();
		addLblStatus();
	}
	
	public void addLabels()
	{
		lblDispenserNo = new JLabel("Dispenser Serial No. : ");
		lblDispenserNo.setBounds(20, 20, 130, 30);
		//lblDispenserNo.setBorder(BorderFactory.createLineBorder(Color.black));
		lblDispenserNo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispenserNo.setVerticalAlignment(SwingConstants.CENTER);
		lblDispenserNo.setFont(new Font(font, Font.PLAIN, fontSize));
		this.getContentPane().add(lblDispenserNo);
		
		lblServiceRequestNo = new JLabel("Service Request No. : ");
		lblServiceRequestNo.setBounds(20, 70, 130, 30);
		//lblServiceRequestNo.setBorder(BorderFactory.createLineBorder(Color.black));
		lblServiceRequestNo.setHorizontalAlignment(SwingConstants.CENTER);
		lblServiceRequestNo.setVerticalAlignment(SwingConstants.CENTER);
		lblServiceRequestNo.setFont(new Font(font, Font.PLAIN, fontSize));
		this.getContentPane().add(lblServiceRequestNo);
	}
	
	public void addTxtField()
	{
		txtDispenserField = new JTextField();
		txtDispenserField.setBounds(160, 20, 110, 30);
		txtDispenserField.setToolTipText("Enter the Dispenser Serial Number. This field is mandatory");
		//txtDispenserField.setBorder(BorderFactory.createLineBorder(Color.black));
		txtDispenserField.setHorizontalAlignment(SwingConstants.CENTER);
		this.getContentPane().add(txtDispenserField);
		
		txtDispenserField.addKeyListener(new KeyAdapter() {
			
			public void keyReleased(KeyEvent e)
			{
				lblStatusMsg.setText("");
			}
		});
		
		txtServiceRequestField = new JTextField();
		txtServiceRequestField.setBounds(160, 70, 110, 30);
		txtServiceRequestField.setToolTipText("Enter Service Request Number.");
		//txtServiceRequestField.setBorder(BorderFactory.createLineBorder(Color.black));
		txtServiceRequestField.setHorizontalAlignment(SwingConstants.CENTER);
		this.getContentPane().add(txtServiceRequestField);
		
		// This may required in future when service request is mandatory.
		/*txtServiceRequestField.addKeyListener(new KeyAdapter() {
			
			public void keyReleased(KeyEvent e)
			{
				lblStatusMsg.setText("");
			}
		});*/
	}
	
	public void addSubmitButton()
	{
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(110, 130, 80, 30);
		//btnSubmit.setBorder(BorderFactory.createLineBorder(Color.black));
		btnSubmit.setHorizontalAlignment(SwingConstants.CENTER);
		btnSubmit.setFont(new Font(font, Font.PLAIN, fontSize));
		this.getContentPane().add(btnSubmit);
		
		btnSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String dispenserNo = txtDispenserField.getText();
				String serviceRequestNo = txtServiceRequestField.getText();
				if(dispenserNo.isEmpty())
				{
					lblStatusMsg.setText("Please enter Dispenser Serial Number");
				}
				else
				{
					closeDialog();
					TroubleshootController.getInstance().generateReport(dispenserNo, serviceRequestNo);
				}
			}
		});
		
		this.getRootPane().setDefaultButton(btnSubmit);
		
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
	
	public void addLblStatus()
	{
		lblStatusMsg = new JLabel("");
		lblStatusMsg.setBounds(10, 180, 270, 30);
		//lblStatusMsg.setBorder(BorderFactory.createLineBorder(Color.black));
		lblStatusMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatusMsg.setVerticalAlignment(SwingConstants.CENTER);
		lblStatusMsg.setFont(new Font(font, Font.PLAIN, fontSize));
		lblStatusMsg.setForeground(Color.RED);
		this.getContentPane().add(lblStatusMsg);
	}
	
	public void showDialog(String title)
	{
		this.setTitle(title);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
}
