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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import troubleshoot.controller.TroubleshootController;

public class OptionDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 15;
	private static final String font = "segoe ui";
	private static OptionDialog instance = null;
	
	private JLabel lblImage;
	private JTextArea txtArea;
	private JButton btnYes;
	private JButton btnNo;
	private JButton btnOk;
	
	private int result;
	private boolean isCloseNeeded;
	
	private OptionDialog(String message, int messageType) 
	{
		isCloseNeeded = false;
		result = -1;
		this.initialise(message, messageType);
	}
	
	public static OptionDialog getInstance()
	{
		return instance;
	}
	
	private void initialise(String message, int messageType)
	{
		windowSettings();
		addLabels(messageType, message);
		addButtons();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 250;
		int height = parentLocation.y - 150;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(500, 300));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	private void addLabels(int messagetype, String message)
	{
		URL url = getURL(messagetype);
		ImageIcon image = new ImageIcon(url);
		lblImage = new JLabel(image);
		lblImage.setBounds(30, 40, 100, 100);
		//lblImage.setBorder(BorderFactory.createLineBorder(Color.black));
		lblImage.setFont(new Font(font, Font.BOLD, fontSize));
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblImage);
		
		txtArea = new JTextArea(message);
		txtArea.setEditable(false);
		txtArea.setBounds(160, 70, 320, 50);
		//txtArea.setBorder(BorderFactory.createLineBorder(Color.black));
		txtArea.setFont(new Font(font, Font.BOLD, fontSize));
		this.add(txtArea);
	}
	
	private void addButtons()
	{
		btnYes = new JButton("Yes");
		btnYes.setBounds(150, 170, 80, 50);
		btnYes.setHorizontalAlignment(SwingConstants.CENTER);
		btnYes.setVerticalAlignment(SwingConstants.CENTER);
		btnYes.setFont(new Font(font, Font.PLAIN, fontSize));
		btnYes.setVisible(false);
		this.add(btnYes);
		btnYes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				result = 0;
				closeDialog();
			}
		});
		
		btnNo = new JButton("No");
		btnNo.setBounds(300, 170, 80, 50);
		btnNo.setHorizontalAlignment(SwingConstants.CENTER);
		btnNo.setVerticalAlignment(SwingConstants.CENTER);
		btnNo.setFont(new Font(font, Font.PLAIN, fontSize));
		btnNo.setVisible(false);
		this.add(btnNo);
		btnNo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				result = 1;
				closeDialog();
			}
		});
		
		btnOk = new JButton("OK");
		btnOk.setBounds(220, 170, 80, 50);
		btnOk.setHorizontalAlignment(SwingConstants.CENTER);
		btnOk.setVerticalAlignment(SwingConstants.CENTER);
		btnOk.setFont(new Font(font, Font.PLAIN, fontSize));
		btnOk.setVisible(false);
		this.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				result = -1;
				closeDialog();
			}
		});
		
		this.getRootPane( ).getActionMap( ).put( "ON_EXIT", new AbstractAction( )
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(isCloseNeeded)
				{
					closeDialog( );
				}
			}
		} );
		this.getRootPane( ).getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( KeyStroke.getKeyStroke( "ESCAPE" ), "ON_EXIT" );
		
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent)
		    {
		    	if(isCloseNeeded)
				{
					closeDialog( );
				}
		    }
		});
	}
	
	public static int showOptionDialog(String title, String message, int optionType, int messageType, boolean isCloseNeeded)
	{
		OptionDialog dialog = new OptionDialog(message, messageType);
		instance = dialog;
		dialog.isCloseNeeded = isCloseNeeded;
		dialog.setOptions(optionType);
		dialog.setTitle(title);
		dialog.setIcon();
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(true);
		dialog.pack();
		dialog.setVisible(true);
		
		return dialog.result;
	}
	
	private void setOptions(int optionType)
	{
		if(JOptionPane.YES_NO_OPTION == optionType)
		{
			btnYes.setVisible(true);
			btnNo.setVisible(true);
			btnOk.setVisible(false);
		}
		else if(JOptionPane.DEFAULT_OPTION == optionType)
		{
			btnYes.setVisible(false);
			btnNo.setVisible(false);
			btnOk.setVisible(true);
		}
	}
	
	private URL getURL(int type)
	{
		URL url = null;
		if(type == JOptionPane.QUESTION_MESSAGE)
		{
			url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/Question-icon.png" );
		}
		else if(type == JOptionPane.INFORMATION_MESSAGE)
		{
			url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/Info-icon.png" );
		}
		else if(type == JOptionPane.ERROR_MESSAGE)
		{
			url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/Error-icon.png" );
		}
		
		return url;
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
		instance = null;
		this.dispose();
	}
}
