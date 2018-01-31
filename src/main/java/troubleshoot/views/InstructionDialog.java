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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.file.managment.api.FileResourceReader;

public class InstructionDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static final Color btnColor = Color.decode("#497ED4");
	private static final int fontSize = 15;
	private static final String font = "segoe ui";
	private static final String	filePath = TroubleshootConfigurations.getResourcesDirectory() + "instructions\\selfcheck.ini";
	
	private JLabel lblHeading;
	private JTextArea txtInstructionArea;
	private JButton btnOk;
	private JScrollPane areaScrollPane;
	private JPanel areaPanel;
	
	public InstructionDialog() 
	{
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addLabelAndTextAread();
		addButtons();
		this.pack();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 250;
		int height = parentLocation.y - 250;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	private void addLabelAndTextAread()
	{
		lblHeading = new JLabel("Instructions to run Selfcheck Tool");
		lblHeading.setBounds(10, 10, 480, 50);
		//lblHeading.setBorder(BorderFactory.createLineBorder(Color.black));
		lblHeading.setFont(new Font(font, Font.BOLD, fontSize));
		lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeading.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblHeading);
		
		txtInstructionArea = new JTextArea();
		txtInstructionArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtInstructionArea.setFont(new Font(font, Font.BOLD, fontSize));
		txtInstructionArea.setEditable(false);
		txtInstructionArea.setLineWrap(true);
		txtInstructionArea.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret) txtInstructionArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		getContentPane().add(txtInstructionArea);
		
		areaScrollPane = new JScrollPane(txtInstructionArea);
		areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(470,335));
		areaScrollPane.setVisible(true);
		
		areaPanel = new JPanel();
		areaPanel.setBounds(10, 70, 480, 350);
		//areaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		areaPanel.setVisible(true);
		areaPanel.add(areaScrollPane);
		getContentPane().add(areaPanel);
		
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
		    	closeDialog( );
		    }
		});
	}
	
	private void addButtons()
	{
		btnOk = new JButton("OK");
		btnOk.setBounds(200, 430, 100, 30);
		btnOk.setFont(new Font(font, Font.BOLD, fontSize));
		btnOk.setVisible(true);
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				closeDialog();
				
			}
		});
		
		getContentPane().add(btnOk);
	}
	
	private void setTextAreaContent()
	{
		SetData(filePath);
	}
	
	private void SetData(String fileName)
	{
		FileResourceReader fileResourceReader = new FileResourceReader(fileName);
		byte[] data = fileResourceReader.getFullData();
		String fileData = new String(data);
		txtInstructionArea.setText(fileData);
	}
	
	private void setIcon()
	{
		URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
		if (url != null)
		{
			this.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
		}
	}
	
	public void showDialog(String dialogTitle)
	{
		setTextAreaContent();
		this.setTitle(dialogTitle);
		this.setIcon();
		this.setAlwaysOnTop(false);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
}
