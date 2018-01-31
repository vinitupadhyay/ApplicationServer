package troubleshoot.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import troubleshoot.config.TroubleshootConfigurations;
import troubleshoot.controller.TroubleshootController;
import troubleshoot.tasks.LogsDownloadTask;

/**
 * The Class SelectDirectoryDialog.
 */
public class SelectDirectoryDialog extends JDialog
{

	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************

	private static final String font = "segoe ui";
	
	/** The file chooser default dimension*/
	private static final Dimension	FILECHOOSER_DIMENSION	= new Dimension( 600, 400 );

	/** The Constant serialVersionUID. */
	private static final long				serialVersionUID		= 8755710539831593576L;

	// ******************************************************************
	// STATIC METHODS
	// ******************************************************************
	/** The button Okay */
	private JButton							btnOk;

	/** The button cancel. */
	private JButton							btnCancel;
	
	private JButton							btnDownload;

	/** The directory selected boolean value. */
	protected boolean						bDirectorySelected;

	/** The file chooser. */
	private JFileChooser					fileChooser;

	/** The selected file. */
	private File							selectedFile;

	/** The label directory. */
	private JLabel							lblDirectory;

	/** The directory text field. */
	private JTextField						txtDirectory;

	/** The button browse. */
	private JButton							btnBrowse;
	
	private JLabel							gif;
	
	private JLabel							imgTick;
	
	private JLabel							imgCross;
	
	private JPanel							statusPanel;
	
	private JLabel							lblMessage;

	/* The current instance of the dialog */
	private static SelectDirectoryDialog	dialogInstance;

	// ******************************************************************
	// CONSTRUCTORS.
	// ******************************************************************

	/**
	 * Instantiates a new select directory dialog.
	 */
	public SelectDirectoryDialog()
	{
		super( );
		dialogInstance = this;
		this.getContentPane( ).setBackground( new Color( 255, 255, 255 ) );

		this.setLocation( -223, -44 );
		commonConstructor();
	}

	/**
	 * Common constructor.
	 */
	private void commonConstructor()
	{
		setLayoutSettings();
		addDirectoryText();
		addBrowseButton();
		addStatusPanel();
		addGif();
		addImageTick();
		addImageCross();
		addDownloadButton(100, 200, 125, 30);
		addCancelButton(280,200, 125, 30);
		addOkButton(186, 200, 125, 30);
		pack( );
		getContentPane( ).setLayout( null );
	}

	/**
	 * Sets the layout settings.
	 */
	private void setLayoutSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 250;
		int height = parentLocation.y - 150;
		this.setLocation(width, height);
		this.setPreferredSize( new Dimension( 500,300 ) );
		this.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
		this.getContentPane( ).setLayout( null );
		this.setAlwaysOnTop(false);
		this.setResizable( false );
	}

	/**
	 * Adds the directory text field.
	 */
	private void addDirectoryText()
	{
		lblDirectory = new JLabel( "Directory:" );
		lblDirectory.setFont( new Font( font, Font.PLAIN, 13 ) );
		lblDirectory.setHorizontalAlignment( SwingConstants.LEFT );
		lblDirectory.setBounds( 20, 32, 62, 36 );
		getContentPane( ).add( lblDirectory );

		txtDirectory = new JTextField( "" );
		txtDirectory.setFont( new Font( font, Font.PLAIN, 13 ) );
		txtDirectory.setHorizontalAlignment( SwingConstants.LEFT );
		txtDirectory.setEditable( true );
		txtDirectory.setBounds( 90, 36, 300, 28 );
		txtDirectory.setBackground( Color.WHITE );
		txtDirectory.addKeyListener( new KeyAdapter( )
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				String text = ((JTextField) e.getSource( )).getText( );
				if ((text.trim( ).isEmpty( ) || text.trim().length() <= 2) && (btnDownload != null))
				{
					btnDownload.setEnabled( false );                      
				}
				else if (btnDownload != null)                             
				{
					btnDownload.setEnabled( true );                       
				}
			}
		} );
		getContentPane( ).add( txtDirectory );
	}

	/**
	 * Adds the browse button.
	 */
	private void addBrowseButton()
	{
		btnBrowse = new JButton( "Browse" );
		btnBrowse.setFont( new Font( font, Font.PLAIN, 12 ) );
		btnBrowse.addActionListener( new ActionListener( )
		{
			public void actionPerformed(ActionEvent arg0)
			{
				chooseDirectory( );
				enableRequest();
			}
		} );
		btnBrowse.setBounds( 400, 35, 76, 30 );
		btnBrowse.setFont( new Font( font, Font.PLAIN, 13 ) );
		btnBrowse.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		getContentPane( ).add( btnBrowse );

	}
	
	public void addStatusPanel()
	{
		statusPanel = new JPanel();
		statusPanel.setOpaque(true);
		statusPanel.setBackground(Color.WHITE);
		statusPanel.setLayout(null);
		statusPanel.setPreferredSize(new Dimension(10,100));
		statusPanel.setBounds(10,90,470,30);
		statusPanel.add(getJLabelMessage(),null);
		getContentPane( ).add( statusPanel );
	}
	
	public JLabel getJLabelMessage()
	{
		lblMessage = new JLabel();
		lblMessage.setVisible(false);
		lblMessage.setHorizontalAlignment(JLabel.CENTER);
		lblMessage.setFont(new Font(font, Font.PLAIN, 14));
		lblMessage.setForeground(Color.BLACK);
		lblMessage.setSize(470, 30);
	
		return lblMessage;
	}
	
	public void addGif()
	{
		gif = new JLabel(new ImageIcon(TroubleshootConfigurations.getResourcesDirectory()+"images//Loader.gif", ""));
		gif.setVisible(false);
		gif.setBounds(215,120,60,60);
		gif.setHorizontalAlignment(SwingConstants  .CENTER);
		gif.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane( ).add( gif );
	}
	
	public void addImageTick()
	{
		imgTick = new JLabel(new ImageIcon(TroubleshootConfigurations.getResourcesDirectory()+"images//Green_Tick2.png", ""));
		imgTick.setVisible(false);
		imgTick.setBounds(215,120,60,60);
		imgTick.setHorizontalAlignment(SwingConstants  .CENTER);
		imgTick.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane( ).add( imgTick );
	}
	
	public void addImageCross()
	{
		imgCross = new JLabel(new ImageIcon(TroubleshootConfigurations.getResourcesDirectory()+"images//Red_Cross.png", ""));
		imgCross.setVisible(false);
		imgCross.setBounds(215,120,60,60);
		imgCross.setHorizontalAlignment(SwingConstants  .CENTER);
		imgCross.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane( ).add( imgCross );
	}

	/**
	 * Adds the okay button.
	 */
	private void addDownloadButton(int x, int y, int width, int height)
	{
		btnDownload = new JButton( "Download" );
		btnDownload.addActionListener( new ActionListener( )
		{
			public void actionPerformed(ActionEvent e)
			{
				setDownloadScreen();
			}
		} );
		btnDownload.setBounds( x, y, width, height );
		btnDownload.setFont( new Font( font, Font.PLAIN, 13 ) );
		btnDownload.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );

		enableRequest( );
		getContentPane( ).add( btnDownload );
	}
	
	public void setDownloadScreen()
	{
		if(checkDirectory())
		{
			selectedFile = new File( txtDirectory.getText( ) );
			imgCross.setVisible(false);
			gif.setVisible(true);
			lblMessage.setVisible(true);
			lblMessage.setText("Current logs downloading. Please wait...");
			disableAll();
			
			/* Start background task*/
			LogsDownloadTask task = new LogsDownloadTask(selectedFile,dialogInstance);	
			task.execute();
		}
		else
		{
			TroubleshootController.showMessageDlg("Invalid directory selected to download logs", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void resetDownloadScreen(String message, boolean isSuccess)
	{
		gif.setVisible(false);
		lblMessage.setText(message);
		btnDownload.setVisible(false);
		btnCancel.setVisible(false);
		btnOk.setVisible(true);
		if(isSuccess)
		{
			imgTick.setVisible(true);
		}
		else
		{
			imgCross.setVisible(true);
		}
	}
	
	public void disableAll()
	{
		btnDownload.setEnabled(false);
		btnCancel.setEnabled(false);
		btnBrowse.setEnabled(false);
		txtDirectory.setEnabled(false);
	}
	public void enableAll()
	{
		btnDownload.setEnabled(true);
		btnCancel.setEnabled(true);
		btnBrowse.setEnabled(true);
		txtDirectory.setEnabled(true);
	}
	
	/**
	 * Adds the cancel button.
	 */
	private void addCancelButton(int x, int y, int width, int height)
	{	
		btnCancel = new JButton( "Cancel" );
		btnCancel.addActionListener( new ActionListener( )
		{
			public void actionPerformed(ActionEvent e)
			{
				closeDialog( );
			}
		} );
		btnCancel.setBounds(x, y, width, height);
		btnCancel.setFont( new Font( font, Font.PLAIN, 13 ) );
		btnCancel.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		getContentPane( ).add( btnCancel );
		this.getRootPane().setDefaultButton(btnCancel);
	}
	
	public void addOkButton(int x, int y, int width, int height)
	{
		btnOk = new JButton( "OK" );
		btnOk.setVisible(false);
		btnOk.addActionListener( new ActionListener( )
		{
			public void actionPerformed(ActionEvent e)
			{
				closeDialog();
			}
		} );
		btnOk.setBounds( x, y, width, height );
		btnOk.setFont( new Font( font, Font.PLAIN, 13 ) );
		btnOk.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );

		enableRequest( );
		getContentPane( ).add( btnOk );
	}

	/**
	 * Choose directory.
	 * 
	 * @return true, if a directory was choose
	 */
	protected boolean chooseDirectory()
	{
		if (fileChooser == null)
		{
			fileChooser = new JFileChooser( );
			fileChooser.setPreferredSize( FILECHOOSER_DIMENSION );
			fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
			fileChooser.setDialogTitle( "Choose Directory:" );
		}

		File selectedFileTmp = new File( txtDirectory.getText( ) );
		if (selectedFileTmp != null && selectedFileTmp.exists( ))
		{
			while (!selectedFileTmp.exists( ) || !selectedFileTmp.isDirectory( ))
			{
				selectedFileTmp = selectedFileTmp.getParentFile( );
			}
		}
		
		fileChooser.setCurrentDirectory( selectedFileTmp );
		if (fileChooser.showDialog( this, "Choose" ) == JFileChooser.APPROVE_OPTION)
		{
			selectedFile = fileChooser.getSelectedFile( );
			String absolutePath = selectedFile.getAbsolutePath( );
			txtDirectory.setText( absolutePath );
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Close dialog.
	 */
	protected void closeDialog()
	{
		this.setVisible( false );
	}

	/**
	 * Enable request.
	 */
	public void enableRequest()
	{
		btnDownload.setEnabled( this.txtDirectory.getText( ).length( ) > 2 );  //  C:(2 chars) won't be accepted
	}

	/**
	 * Directory selected.
	 * 
	 * @return true, if successful
	 */
	public boolean directorySelected()
	{
		return bDirectorySelected;
	}

	/**
	 * Clear directory selected.
	 */
	public void clearDirectorySelected()
	{
		bDirectorySelected = false;
		selectedFile = null;
	}

	/**
	 * Sets the default directory path.
	 * 
	 * @param path
	 *            the new default directory
	 */
	public void setDefaultDirectory(String path)
	{
		selectedFile = new File( path );
		txtDirectory.setText( selectedFile.getAbsolutePath( ) );
	}

	/**
	 * @return the selected directory
	 */
	public Path getSelectedDirectory()
	{
		Path selectedPath = null;
		if (selectedFile != null)
		{
			
			if(selectedFile.exists())
			{
				selectedPath = Paths.get( selectedFile.getPath( ) );
			}
		}
		else
		{
			selectedPath = Paths.get( "" );
		}
		return selectedPath;
	}

	/**
	 * Choose directory.
	 * 
	 * @brief display the current dialog with the specified title and default folder.
	 * 
	 * @param tiltle
	 *            the title action that generates a selection.
	 * @param defaultFolder
	 *            the default folder to choose.
	 * @return true if a valid directory was selected, false in other cases.
	 */
	public void chooseDirectory(String title, String defaultFolder)
	{
		dialogInstance.setAlwaysOnTop( true );
		dialogInstance.setModal( true );
		dialogInstance.setTitle( "Choose Directory - " + title );
		dialogInstance.clearDirectorySelected( );
		dialogInstance.setDefaultDirectory( defaultFolder );
		dialogInstance.enableRequest( );

		// Finally here the control is transfered to the dialog
		dialogInstance.setVisible( true );
	}
	
	public boolean checkDirectory()
	{
		// return true if a valid directory was selected.
		File outputDirectory = new File(txtDirectory.getText());
		if(!txtDirectory.getText().isEmpty() && outputDirectory.isDirectory())
		{
			bDirectorySelected = true;
		}
		else
		{
			bDirectorySelected = false;
		}
		try
		{
			return (dialogInstance.directorySelected( ) && dialogInstance.getSelectedDirectory( ).toString( )!= null && !dialogInstance.getSelectedDirectory( ).toString( ).isEmpty( ));
		}
		catch(NullPointerException e)
		{
			return false;
		}
	}
	
	
}
