package troubleshoot.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.Step;
import troubleshoot.models.Connection;
import troubleshoot.xml.ButtonAction;
import troubleshoot.xml.DialogElements;
import troubleshoot.xml.Tag;

public class TroubleshootInstructionDialog extends JDialog implements Runnable
{
	private static final long 	serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private final static Logger logger = Logger.getLogger(TroubleshootInstructionDialog.class);
	private static final long SPOT_CONNECTION_DISCONNECT_WAIT_TIME = 30000;

	private URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
	
	private JButton btn = null;
	private JLabel lbl = null;
	private JLabel img = null;
	private JCheckBox[] chckbx = new JCheckBox[10];
	private int chkBoxIndex = 0;
	
	private int DEFAULT_DIALOG_WIDTH = 490;
	private int DEFAULT_DIALOG_HEIGHT = 100;
	
	private Vector<Tag> vecTags = null;
	private Step step;
	
	private Thread threadcheckUPMWakeup;
	
	public boolean isExit;
	
	public TroubleshootInstructionDialog(Vector<Tag> vecTags)
	{
		isExit = false;
		step = new Step();
		this.vecTags = vecTags;
		commonConstructor();
		
		for (Tag tag : vecTags)
		{
			DialogElements element = DialogElements.valueOf(tag.getName().toUpperCase());
			setComponents(element, tag);
		}
	}
	
	public void setComponents(DialogElements element, Tag tag)
	{
		switch(element)
		{
		case BUTTON : 
			createButton(tag);
			break;
		case MESSAGE :
			step.getActionMessages().add(tag.getValue());
			if(tag.getAction().equals("header"))
			{
				createHeader(tag);
			}
			else if(tag.getAction().equals("label"))
			{
				createLabel(tag);
			}
			break;
		case CHECKBOX : 
			createCheckBox(tag);
			break;
		case IMAGE :
			if(tag.getAction().equalsIgnoreCase("image"))
			{
				createImg(tag);
			}
			else if(tag.getAction().equalsIgnoreCase("wait_reboot"))
			{
				if(threadcheckUPMWakeup == null)
				{
					threadcheckUPMWakeup = new Thread(this);
					threadcheckUPMWakeup.start();
				}
				createImg(tag);
			}
			break;
		}
	}
	
	public void commonConstructor()
	{
		this.getContentPane( ).setBackground( new Color( 255, 255, 255 ) );
		setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE);
		getContentPane( ).setLayout( null );
		adjustDialogSize();
		setPreferredSize( new Dimension(DEFAULT_DIALOG_WIDTH, DEFAULT_DIALOG_HEIGHT) );
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - (DEFAULT_DIALOG_WIDTH/2);
		int height = parentLocation.y - (DEFAULT_DIALOG_HEIGHT/2);
		setLocation(width, height);
		setResizable( false );
		pack( );
	}
	
	public void adjustDialogSize()
	{
		int y;
		y=DEFAULT_DIALOG_HEIGHT;
		
		for (Tag tag : vecTags)
		{
			y+= (tag.getHeight() + 5);
		}
		DEFAULT_DIALOG_HEIGHT=y;
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	public void showDialog()
	{
		setAlwaysOnTop( true );
		setModal( true );
		setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
		setTitle( "Fix");
		
		// Finally here the control is transfered to the dialog
		setVisible( true );
	}
	
	public void createButton(final Tag tag)
	{
		btn = new JButton(tag.getValue());
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ButtonAction bAction = ButtonAction.valueOf(tag.getAction().toUpperCase());
				buttonActionPerformed(bAction, tag.getValue());
			}
		});
		
		if(tag.getxPos() == 0)
		{
			int half = DEFAULT_DIALOG_WIDTH/2;
			int newXPOS = half - (tag.getWidth() / 2);
			btn.setBounds(newXPOS, tag.getyPos(), tag.getWidth(), tag.getHeight());
		}
		else
		{
			btn.setBounds(tag.getxPos(), tag.getyPos(), tag.getWidth(), tag.getHeight());
		}
		btn.setFont( new Font( font, Font.PLAIN, fontSize ) );
		btn.setFocusable(false);
		btn.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		btn.setEnabled(true);
		getContentPane( ).add( btn );
	}
	
	public void createImg(Tag tag)
	{
		String gifPath = TroubleshootConfigurations.getResourcesDirectory() + tag.getValue();
		img = new JLabel(new ImageIcon(gifPath, ""));
		
		if(TroubleshootConfigurations.getInstance().getIsBorderPainted())
		{
			img.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		
		img.setVisible(true);
		img.setBounds(tag.getxPos(), tag.getyPos(), tag.getWidth(), tag.getHeight());
		img.setHorizontalAlignment(SwingConstants.CENTER);
		img.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane( ).add( img );
	}
	
	public void createHeader(Tag tag)
	{
		lbl = new JLabel();
		
		if(TroubleshootConfigurations.getInstance().getIsBorderPainted())
		{
			lbl.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		
		lbl.setVisible(true);
		lbl.setText(tag.getValue());
		lbl.setBounds(tag.getxPos(), tag.getyPos(), tag.getWidth(), tag.getHeight());
		
		if(tag.getAlignment() == -1)
		{
			lbl.setHorizontalAlignment(JLabel.CENTER);
		}
		else
		{
			lbl.setHorizontalAlignment(tag.getAlignment());
		}

		lbl.setFont(new Font(font, Font.BOLD, 14));
		lbl.setForeground(Color.BLACK);
		getContentPane().add(lbl);
	}
	
	public void createLabel(Tag tag)
	{
		lbl = new JLabel();
		
		if(TroubleshootConfigurations.getInstance().getIsBorderPainted())
		{
			lbl.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		
		lbl.setVisible(true);
		lbl.setText(tag.getValue());
		lbl.setBounds(tag.getxPos(), tag.getyPos(), tag.getWidth(), tag.getHeight());
		if(tag.getAlignment() == -1)
		{
			lbl.setHorizontalAlignment(JLabel.LEFT);
		}
		else
		{
			lbl.setHorizontalAlignment(tag.getAlignment());
		}
		lbl.setFont(new Font(font, Font.PLAIN, 14));
		lbl.setForeground(Color.BLACK);
		getContentPane().add(lbl);
	}
	
	public void createCheckBox(Tag tag)
	{
		chckbx[chkBoxIndex] = new JCheckBox(tag.getName());
		if(TroubleshootConfigurations.getInstance().getIsBorderPainted())
		{
			chckbx[chkBoxIndex].setBorder(BorderFactory.createLineBorder(Color.black));
		}
		chckbx[chkBoxIndex].setBounds(tag.getxPos(), tag.getyPos(), tag.getWidth(), tag.getHeight());	
		chckbx[chkBoxIndex].setSelected(false);
		chckbx[chkBoxIndex].setVisible(true);
		chckbx[chkBoxIndex].setBorderPainted( false );
		chckbx[chkBoxIndex].setFont( new Font("Dialog", Font.ITALIC, 11) );
		chckbx[chkBoxIndex].setBackground( Color.WHITE );
		
		getContentPane().add(chckbx[chkBoxIndex]);
		chkBoxIndex++;
	}
	
	public int showConfirmDialog(String message, String title ,Object[] options )
	{
		int indResult = JOptionPane.showOptionDialog( this, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null,options, null );

		if (indResult == 0)		// Ok
		{
			return JOptionPane.OK_OPTION;
		}
		else	// Cancel
		{
			return JOptionPane.NO_OPTION;
		}
	}
	
	public void buttonActionPerformed(ButtonAction bAction, String btnName)
	{
		step.setAction(btnName);
		switch(bAction)
		{
			case EXIT:
				isExit = true;
				closeDialog();
				break;
				
			case OK:
				closeDialog();
				
			case HARD_REBOOT:
				closeDialog();
				TroubleshootController.getInstance().warmRebootSPOT(Connection.HARD_RESET);
				break;
					
			default :
				closeDialog();
		}
	}
	
	public void run()
	{
		//wait to reboot
		logger.info("Waiting to get SPOT up.");
		try 
		{
			Thread.sleep(SPOT_CONNECTION_DISCONNECT_WAIT_TIME);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		if(TroubleshootController.getInstance().checkSPOTUp())
		{
			closeDialog();
		}

	}
}
