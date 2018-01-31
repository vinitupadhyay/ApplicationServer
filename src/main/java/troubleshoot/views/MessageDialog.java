package troubleshoot.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.TestReader;

public class MessageDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 15;
	private static final String font = "segoe ui";
	
	private JLabel lblImage;
	private JTextArea txtArea;
	private JLabel lblTimeLeft;
	private JLabel lblTime;
	
	private MessageDialog(int type, String message) 
	{
		this.initialise(type, message);
	}
	
	private void initialise(int type, String message)
	{
		windowSettings();
		addTimerLabels();
		addLabels(type, message);
		this.pack();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 250;
		int height = parentLocation.y - 100;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(500, 200));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	private void addTimerLabels()
	{
		lblTimeLeft = new JLabel("Time Left: ");
		lblTimeLeft.setBounds(350, 10, 100, 30);
		//lblTimeLeft.setBorder(BorderFactory.createLineBorder(Color.black));
		lblTimeLeft.setFont(new Font(font, Font.PLAIN, 12));
		lblTimeLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeLeft.setVerticalAlignment(SwingConstants.CENTER);
		lblTimeLeft.setVisible(false);
		this.add(lblTimeLeft);
		
		lblTime = new JLabel("");
		lblTime.setBounds(440, 10, 30, 30);
		//lblTime.setBorder(BorderFactory.createLineBorder(Color.black));
		lblTime.setFont(new Font(font, Font.PLAIN, 12));
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setVerticalAlignment(SwingConstants.CENTER);
		lblTime.setVisible(false);
		this.add(lblTime);
	}
	
	private void addLabels(int type, String message)
	{
		URL url = getURL(type);
		ImageIcon image = new ImageIcon(url);
		lblImage = new JLabel(image);
		lblImage.setBounds(30, 40, 100, 100);
		//lblImage.setBorder(BorderFactory.createLineBorder(Color.black));
		lblImage.setFont(new Font(font, Font.BOLD, fontSize));
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblImage);
		
		
		txtArea = new JTextArea(message);
		txtArea.setBounds(160, 70, 320, 50);
		txtArea.setEditable(false);
		//txtArea.setBorder(BorderFactory.createLineBorder(Color.black));
		txtArea.setFont(new Font(font, Font.BOLD, fontSize));
		this.add(txtArea);
	}
	
	public static MessageDialog getMessageDialog(String title, int type, String message)
	{
		MessageDialog dialog = new MessageDialog(type, message);
		dialog.setTitle(title);
		dialog.setIcon();
		dialog.setModal(true); 
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
		return dialog;
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
	
	public void showDialog(boolean isTimerNeeded)
	{
		if(isTimerNeeded)
		{
			lblTimeLeft.setVisible(true);
			lblTime.setVisible(true);
			
			new Thread(new Runnable() {
				
				@Override
				public void run()
				{
					for(int i=60; i>0; i--)
					{
						if(TestReader.getInstance().isInserted)
						{
							break;
						}
						TroubleshootController.sleep(1000);
						lblTime.setText(String.valueOf(i));
						if(i<10)
						{
							if(i==9)
							{
								lblTime.setFont(new Font(font, Font.BOLD, fontSize));
								lblTime.setForeground(Color.red);
							}
							
							Toolkit.getDefaultToolkit().beep();
						}
					}
					
				}
			}).start();
			
			this.setVisible(true);
		}
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
}
