package troubleshoot.xml;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import troubleshoot.model.pojo.FixActionReport;
import troubleshoot.model.pojo.Step;
import troubleshoot.models.Connection;
import troubleshoot.models.DiagnosticInformation;
import troubleshoot.states.Device;
import troubleshoot.tasks.CheckTamperTask;
import troubleshoot.views.PrinterDialog;
import troubleshoot.views.ShowErrorsDialog;

public class ErrorFixStepsDialog extends JDialog implements Runnable
{
	private static final long 	serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(ErrorFixStepsDialog.class);
	private static final long SPOT_CONNECTION_DISCONNECT_WAIT_TIME = 30000;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	public static ErrorFixStepsDialog instance = null;
	
	private JButton btn = null;
	private JLabel lbl = null;
	private JLabel img = null;
	private JCheckBox[] chckbx = new JCheckBox[10];
	private int chkBoxIndex = 0;
	
	private Thread threadcheckUPMWakeup;
	private int DEFAULT_DIALOG_WIDTH = 490;
	private int DEFAULT_DIALOG_HEIGHT = 100;
	
	private Vector<Tag> vecTags = null;
	private ErrorFileParser fileParser;
	private FixActionReport fixActionReport;
	private Step step;
	
	public ErrorFixStepsDialog(ErrorFileParser fileParser, Vector<Tag> vecTags, FixActionReport fixActionReport)
	{
		instance = this;
		step = new Step();
		this.fixActionReport = fixActionReport;
		this.fileParser = fileParser;
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
	public void createButton(final Tag tag)
	{
		btn = new JButton(tag.getValue());
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
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
	
	public void buttonActionPerformed(ButtonAction bAction, String btnName)
	{
		step.setAction(btnName);
		fixActionReport.getSteps().add(step);
		switch(bAction)
		{
		case YES:
			closeDialog();
			fileParser.setNodeName("yes");
			break;
		case NO:
			closeDialog();
			fileParser.setNodeName("no");
			break;
		case OK:
			closeDialog();
			break;
		case LOGIN_NONSECURE: 
			if(!isAllSelected())
			{
				String message="Please tick all the checkboxes";
				showConfirmDialog(message, "Error" , new Object[]{"Ok"} );
			}
			else
			{
				closeDialog();;
				TroubleshootController.getInstance().nonSecureMenu();
			}
			break;
		case WARM_REBOOT:
			closeDialog();
			TroubleshootController.getInstance().warmRebootSPOT(Connection.SOFT_RESET);
			break;
		case WAIT_REBOOT:
			closeDialog();
			if(threadcheckUPMWakeup == null)
			{
				threadcheckUPMWakeup = new Thread(this);
				threadcheckUPMWakeup.start();
			}
			break;
		case SYSTEM_MENU:
			closeDialog();
			TroubleshootController.getInstance().nonSecureMenu();
			break;
		case SYSTEM_STATUS:
			closeDialog();
			TroubleshootController.getTroubleshootView().clearAllSystemStatus();
			TroubleshootController.getInstance().collectInformation();
			break;
		case CHECK_TAMPER:
			closeDialog();
			CheckTamperTask worker = new CheckTamperTask();
			worker.execute();
			break;
		case CHECK_READER_STATUS:
			closeDialog();
			TroubleshootController.getTroubleshootView().clearAllSystemStatus();
			TroubleshootController.sleep(10000);
			if(TroubleshootController.getInstance().isCardReaderSystemStatusOK())
			{
				fileParser.setNodeName("yes");
			}
			else
			{
				fileParser.setNodeName("no");
			}
			break;
		case CHECK_UPM_STATUS:
			closeDialog();
			TroubleshootController.getTroubleshootView().clearAllSystemStatus();
			String error = TroubleshootController.getInstance().isUPMStatusOK();
			switch (error)
			{
				case "Info Error":
					TroubleshootController.showMessageDlg("Error while retrieving Status Information.", "Info", JOptionPane.ERROR_MESSAGE);
					break;
					
				case "Unknown Error":
					TroubleshootController.showMessageDlg("No Fix available for this Error.", "Info", JOptionPane.INFORMATION_MESSAGE);
					break;
					
				case "bothDism":
				case "keyDism":
				case "dispDism":
					ErrorFileParser newFileParser = new ErrorFileParser();
					TroubleshootController.getInstance().startErrorFixing(newFileParser, Device.UPM, "00", "00", error, fixActionReport);
					break;
	
				default:
					break;
			}
			break;
		case CHECK_PRINTER_STATUS:
			closeDialog();
			PrinterDialog.getInstance().clearAllSystemStatus();
			if(TroubleshootController.getInstance().isPrinterSystemStatusOK())
			{
				fileParser.setNodeName("yes");
			}
			else
			{
				fileParser.setNodeName("no");
			}
			break;
		case CHECK_SIDE:
			closeDialog();
			if(DiagnosticInformation.getInstance().collectDiagnosticInfo())
			{
				if(DiagnosticInformation.rollCallInfo.getSide().equalsIgnoreCase("undefined"))
				{
					fileParser.setNodeName("no");
				}
				else
				{
					fileParser.setNodeName("yes");
				}
			}
			break;
		case CHECK_UPM_LOG:
			closeDialog();
			TroubleshootController.getInstance().checkUPMlogs();
			break;
		case DISCONNECT:
			closeDialog();
			if(ShowErrorsDialog.getInstance() != null)
			{
				ShowErrorsDialog.getInstance().closeDialog();
				PrinterDialog.getInstance().closeDialog();
			}
			TroubleshootController.getInstance().disconnect();
			break;
		case TESTREADER:
			closeDialog();
			TroubleshootController.getInstance().testCardReader();
			break;
		case TESTUPM:
			closeDialog();
			TroubleshootController.getInstance().testUPM();
			break;
			
			default :
				closeDialog();
		}
	}
	private boolean isAllSelected()
	{
		for(int i=0;i<chkBoxIndex;i++)
		{
			if(!chckbx[i].isSelected())
			{
				return false;
			}
		}
		return true;
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
}


