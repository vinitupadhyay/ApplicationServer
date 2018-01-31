package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.states.Device;
import troubleshoot.states.TestStatus;

public class ReaderOutput extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private JLabel lblTrack1;
	private JLabel lblTrack1Status;
	private JLabel lblTrack2;
	private JLabel lblTrack2Status;
	private JLabel lblTrack3;
	private JLabel lblTrack3Status;
	private JLabel lblChip;
	private JLabel lblChipStatus;
	
	private JButton btnOk;
	private JButton btnRepeat;
	
	public ReaderOutput() 
	{
		this.initialise();
		
	}
	
	private void initialise()
	{
		windowSettings();
		addLabels();
		addButtons();
		pack();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 150;
		int height = parentLocation.y - 150;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(300, 300));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	private void addLabels()
	{
		lblTrack1 = new JLabel("Track1: ");
		lblTrack1.setBounds(80, 35, 50, 30);
		lblTrack1.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblTrack1.setVerticalAlignment(SwingConstants.CENTER);
		lblTrack1.setHorizontalAlignment(SwingConstants.CENTER);
		//lblTrack1.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblTrack1);
		
		lblTrack1Status = new JLabel("");
		lblTrack1Status.setBounds(140, 35, 80, 30);
		lblTrack1Status.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblTrack1Status.setVerticalAlignment(SwingConstants.CENTER);
		lblTrack1Status.setHorizontalAlignment(SwingConstants.CENTER);
		//lblTrack1Status.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblTrack1Status);
		
		lblTrack2 = new JLabel("Track2: ");
		lblTrack2.setBounds(80, 75, 50, 30);
		lblTrack2.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblTrack2.setVerticalAlignment(SwingConstants.CENTER);
		lblTrack2.setHorizontalAlignment(SwingConstants.CENTER);
		//lblTrack2.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblTrack2);
		
		lblTrack2Status = new JLabel("");
		lblTrack2Status.setBounds(140, 75, 80, 30);
		lblTrack2Status.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblTrack2Status.setVerticalAlignment(SwingConstants.CENTER);
		lblTrack2Status.setHorizontalAlignment(SwingConstants.CENTER);
		//lblTrack2Status.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblTrack2Status);
		
		lblTrack3 = new JLabel("Track3: ");
		lblTrack3.setBounds(80, 115, 50, 30);
		lblTrack3.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblTrack3.setVerticalAlignment(SwingConstants.CENTER);
		lblTrack3.setHorizontalAlignment(SwingConstants.CENTER);
		//lblTrack3.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblTrack3);
		
		lblTrack3Status = new JLabel("");
		lblTrack3Status.setBounds(140, 115, 80, 30);
		lblTrack3Status.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblTrack3Status.setVerticalAlignment(SwingConstants.CENTER);
		lblTrack3Status.setHorizontalAlignment(SwingConstants.CENTER);
		//lblTrack3Status.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblTrack3Status);
		
		lblChip = new JLabel("Chip: ");
		lblChip.setBounds(80, 155, 50, 30);
		lblChip.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblChip.setVerticalAlignment(SwingConstants.CENTER);
		lblChip.setHorizontalAlignment(SwingConstants.CENTER);
		//lblChip.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblChip);
		
		lblChipStatus = new JLabel("");
		lblChipStatus.setBounds(140, 155, 80, 30);
		lblChipStatus.setFont(new FontUIResource(font, Font.PLAIN, fontSize));
		lblChipStatus.setVerticalAlignment(SwingConstants.CENTER);
		lblChipStatus.setHorizontalAlignment(SwingConstants.CENTER);
		//lblChipStatus.setBorder(BorderFactory.createLineBorder(Color.black));
		this.getContentPane().add(lblChipStatus);
		
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
	
	private void addButtons()
	{
		btnOk = new JButton("OK");
		btnOk.setBounds(80, 220, 50, 30);
		btnOk.setHorizontalAlignment(SwingConstants.CENTER);
		btnOk.setVerticalAlignment(SwingConstants.CENTER);
		btnOk.setFont(new Font(font, Font.PLAIN, fontSize));
		btnOk.setVisible(true);
		this.add(btnOk);
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				closeDialog();
			}
		});
		
		btnRepeat = new JButton("Repeat Test");
		btnRepeat.setBounds(140, 220, 100, 30);
		btnRepeat.setHorizontalAlignment(SwingConstants.CENTER);
		btnRepeat.setVerticalAlignment(SwingConstants.CENTER);
		btnRepeat.setFont(new Font(font, Font.PLAIN, fontSize));
		btnRepeat.setVisible(true);
		this.add(btnRepeat);
		
		btnRepeat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				closeDialog();
				TroubleshootController.getTroubleshootView().getDevicePanel(Device.CARD_READER).setTestStatus(TestStatus.NOT_EXECUTED);
				TroubleshootController.getInstance().testCardReader();
			}
		});
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
	
	public void showDialog(String oneStatus, String twoStatus, String threeStatus, String chipStatus)
	{
		lblTrack1Status.setText(oneStatus);
		lblTrack2Status.setText(twoStatus);
		lblTrack3Status.setText(threeStatus);
		lblChipStatus.setText(chipStatus);
		this.setTitle("Test Card Reader");
		this.setModal(true);
		this.setIcon();
		this.setVisible(true);
	}
}
