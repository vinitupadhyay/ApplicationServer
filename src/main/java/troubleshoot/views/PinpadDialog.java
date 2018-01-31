package troubleshoot.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.models.SPOTDisplay;

public class PinpadDialog extends JDialog 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	private static final Color colorGreen = Color.decode("#53c653");
	private static final int TIME_ONE_SEC = 1000;
	
	public static boolean isBtnOKClicked = false;
	
	private JLabel lblTimeLeft;
	private JLabel lblTime;
	private JLabel lblHeading;
	private JTextField[][] tags;
	
	private JButton btnOk;
	private JButton btnRepeat;
	
	private static PinpadDialog pinadDialog = null;
	
	private PinpadDialog() 
	{
		this.initialise();
	}
	
	public static synchronized PinpadDialog getInstance()
	{
		if(pinadDialog == null)
		{
			pinadDialog = new PinpadDialog();
		}
		return pinadDialog;
	}
	
	private void initialise()
	{
		windowSettings();
		addTimerLabels();
		addLabel();
		addTags();
		addButtons();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 200;
		int height = parentLocation.y - 200;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(400, 400));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	private void addTimerLabels()
	{
		lblTimeLeft = new JLabel("Time Left: ");
		lblTimeLeft.setBounds(280, 2, 100, 30);
		//lblTimeLeft.setBorder(BorderFactory.createLineBorder(Color.black));
		lblTimeLeft.setFont(new Font(font, Font.PLAIN, 12));
		lblTimeLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeLeft.setVerticalAlignment(SwingConstants.CENTER);
		lblTimeLeft.setVisible(false);
		this.add(lblTimeLeft);
		
		lblTime = new JLabel("");
		lblTime.setBounds(360, 2, 30, 30);
		//lblTime.setBorder(BorderFactory.createLineBorder(Color.black));
		lblTime.setFont(new Font(font, Font.PLAIN, 12));
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setVerticalAlignment(SwingConstants.CENTER);
		lblTime.setVisible(false);
		this.add(lblTime);
	}
	
	private void addLabel()
	{
		lblHeading = new JLabel("Press all the keypad keys, one at a time");
		//lblHeading.setBorder(BorderFactory.createLineBorder(Color.black));
		lblHeading.setFont(new Font(font, Font.PLAIN, fontSize));
		lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeading.setVerticalAlignment(SwingConstants.CENTER);
		lblHeading.setBounds(20, 20, 350, 30);
		this.add(lblHeading);
		
		
		this.getRootPane( ).getActionMap( ).put( "ON_EXIT", new AbstractAction( )
		{
			private static final long	serialVersionUID	= 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				isBtnOKClicked = true;
				closeDialog( );
			}
		} );
		this.getRootPane( ).getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( KeyStroke.getKeyStroke( "ESCAPE" ), "ON_EXIT" );
		
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent)
		    {
		    	isBtnOKClicked = true;
		    	closeDialog();
		    }
		});
	}
	
	private void addTags()
	{
		int x = 75;
		int y = 60; 
		tags = new JTextField[4][4];
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				
				tags[i][j] = new JTextField();
				tags[i][j].setBounds(x, y, 60, 60);
				tags[i][j].setBackground(Color.white);
				tags[i][j].setFont(new Font(font, Font.PLAIN, fontSize));
				tags[i][j].setEditable(false);
				tags[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				tags[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.5f)));
				this.add(tags[i][j]);
				
				x+=60;
			}
			x=75;
			y+=60;
		}
	}
	
	private void addButtons()
	{
		btnOk = new JButton("OK");
		btnOk.setBounds(110, 320, 50, 30);
		btnOk.setHorizontalAlignment(SwingConstants.CENTER);
		btnOk.setVerticalAlignment(SwingConstants.CENTER);
		btnOk.setFont(new Font(font, Font.PLAIN, fontSize));
		btnOk.setVisible(true);
		this.add(btnOk);
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isBtnOKClicked = true;
				closeDialog();
			}
		});
		
		btnRepeat = new JButton("Repeat Test");
		btnRepeat.setBounds(210, 320, 100, 30);
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
				TroubleshootController.sleep(3000);
				TroubleshootController.getInstance().testUPM();
			}
		});
	}
	
	public void setBtnStatus(byte key)
	{
		switch (key)
		{
			case 48: // 0 key
				tags[3][1].setBackground(colorGreen);
				break;
				
			case 49: // 1 key
				tags[0][0].setBackground(colorGreen);
				break;
				
			case 50: // 2 key
				tags[0][1].setBackground(colorGreen);
				break;
				
			case 51: // 3 key
				tags[0][2].setBackground(colorGreen);
				break;
				
			case 52: // 4 key
				tags[1][0].setBackground(colorGreen);
				break;
				
			case 53: // 5 key
				tags[1][1].setBackground(colorGreen);
				break;
				
			case 54: // 6 key
				tags[1][2].setBackground(colorGreen);
				break;
				
			case 55: // 7 key
				tags[2][0].setBackground(colorGreen);
				break;
				
			case 56: // 8 key
				tags[2][1].setBackground(colorGreen);
				break;
				
			case 57: // 9 key
				tags[2][2].setBackground(colorGreen);
				break;
				
			case 12: // cancel key
				tags[2][3].setBackground(colorGreen);
				break;
				
			case 13: // ok or enter key
				tags[3][2].setBackground(colorGreen);
				break;
				
			case 14: // clear or delete key
				tags[3][0].setBackground(colorGreen);
				break;
				
			case 20: // help key
				tags[3][3].setBackground(colorGreen);
				break;
				
			case 21: // no key
				tags[1][3].setBackground(colorGreen);
				break;
				
			case 22: // yes key
				tags[0][3].setBackground(colorGreen);
				break;
	
			default:
				break;
		}
	}
	
	public synchronized void showTimer()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				while(SPOTDisplay.isResetTimer)
				{
					lblTimeLeft.setVisible(true);
					lblTime.setVisible(true);
					
					for(int i=60; i>0; i--)
					{
						if(SPOTDisplay.isKeyPressed)
						{
							SPOTDisplay.isKeyPressed = false;
							break;
						}
						TroubleshootController.sleep(TIME_ONE_SEC);
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
					
					lblTimeLeft.setVisible(false);
					lblTime.setVisible(false);
					lblTime.setFont(new Font(font, Font.PLAIN, fontSize));
					lblTime.setForeground(Color.black);
					lblTime.setText("60");
				}
			}
		}).start();
	}
	
	private void clearGrid()
	{
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				tags[i][j].setBackground(Color.white);
			}
		}
	}
	
	public void closeDialog()
	{
		SPOTDisplay.isKeyPressed = true;
		SPOTDisplay.isResetTimer = false;
		SPOTDisplay.isClosed = true;
		SPOTDisplay.getInstance().closeWindow();
		this.setVisible(false);
		clearGrid();
		this.dispose();
	}
	
	public void showDialog()
	{
		this.setTitle("Test UPM");
		this.setModal(true);
		this.pack();
		this.setVisible(true);
	}

}
