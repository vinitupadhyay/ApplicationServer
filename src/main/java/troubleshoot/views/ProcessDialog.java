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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;

public class ProcessDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int fontSize = 15;
	private static final String font = "segoe ui";
	private static ProcessDialog processDialog = null;
	
	private JLabel lblImage;
	private JLabel lblText;
	
	public ProcessDialog()
	{
		processDialog = this;
		initialise();
	}
	
	public static ProcessDialog getInstance()
	{
		if(processDialog == null)
		{
			new ProcessDialog();
		}
		return processDialog;
	}

	private void initialise()
	{
		windowSettings();
		addLabels();
		pack();
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
	
	private void addLabels()
	{
		
		lblText = new JLabel("");
		lblText.setBounds(50, 50, 400, 50);
		//lblText.setBorder(BorderFactory.createLineBorder(Color.black));
		lblText.setFont(new Font(font, Font.BOLD, fontSize));
		lblText.setHorizontalAlignment(SwingConstants.CENTER);
		lblText.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblText);
		
		ImageIcon image = new ImageIcon(TroubleshootConfigurations.getResourcesDirectory()+"images//Loader.gif");
		lblImage = new JLabel(image);
		lblImage.setBounds(200, 100, 100, 100);
		//lblImage.setBorder(BorderFactory.createLineBorder(Color.black));
		lblImage.setFont(new Font(font, Font.BOLD, fontSize));
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblImage);
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	public void setProcessStatus(final String status)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				lblText.setText(status);
				
			}
		});
	}
	
	private void setIcon()
	{
		URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
		if (url != null)
		{
			this.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
		}
	}
	
	public void showDialog(String message)
	{
		lblText.setText(message);
		this.setTitle("Troubleshooting - Parsing Logs");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(false);
		this.setIcon();
		this.setVisible(true);
	}
}
