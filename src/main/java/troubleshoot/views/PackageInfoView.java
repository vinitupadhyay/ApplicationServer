package troubleshoot.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.DeviceInfo;
import troubleshoot.models.PackageInformation.ApplicationVersion;
import troubleshoot.states.Device;

public class PackageInfoView extends JDialog 
{
	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	
		private static final long serialVersionUID = 1L;
		private static final int fontSize = 12;
		private static final String font = "segoe ui";
		
		
		// ******************************************************************
		// PRIVATE FIELDS.
		// ******************************************************************
		private JLabel lblPackageInfo;
		private JLabel lblCardReaderInfo;
		private JLabel lblPrinterInfo;
		private JScrollPane jScrollPane;
		public JTable tablePackageInfo;
		private JButton btnOk; 
		
		// ******************************************************************
		// CONSTRUCTORS.
		// ******************************************************************

		
		/**
		 * Instantiates a new select Package version dialog.
		 */
		
		public PackageInfoView(Vector<ApplicationVersion> rowData, DeviceInfo cardReader, DeviceInfo printer)
		{
			super();
			this.getContentPane( ).setBackground( new Color( 255, 255, 255 ) );
			this.setBackground( Color.gray );
			commonConstructor(rowData, cardReader, printer);
		}
		
		/**
		 * Common constructor.
		 */
		private void commonConstructor(Vector<ApplicationVersion> rowData, DeviceInfo cardReader, DeviceInfo printer)
		{
			setLayoutSettings( );
			addLabels();
			addPackageInfoArea(rowData);
			addDeviceInfoArea(Device.CARD_READER, cardReader);
			addDeviceInfoArea(Device.PRINTER, printer);
			addOkButton( );
			pack( );
		}

		
		/**
		 * Setting Layout
		 */
		private void setLayoutSettings()
		{
			setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
			getContentPane( ).setLayout( null );
			setPreferredSize( new Dimension( 500, 210 ) );
			setResizable( false );

			this.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) 
			    {
			    	closeDialog();
			    }
			});

		}
		
		/**
		 * Adding all labels on the dialog.
		 */
		private void addLabels()
		{
			lblPackageInfo = new JLabel("Package Version Info");
			lblPackageInfo.setBounds(10, 20, 200, 30);
			lblPackageInfo.setFont( new Font( font, Font.BOLD, fontSize ) );
			getContentPane( ).add( lblPackageInfo );
			
			lblCardReaderInfo = new JLabel("Card Reader Info");
			lblCardReaderInfo.setBounds(10, 270, 200, 30);
			lblCardReaderInfo.setFont( new Font( font, Font.BOLD, fontSize ) );
			getContentPane( ).add( lblCardReaderInfo );
			
			lblPrinterInfo = new JLabel("Printer Info");
			lblPrinterInfo.setBounds(10, 370, 200, 30);
			lblPrinterInfo.setFont( new Font( font, Font.BOLD, fontSize ) );
			getContentPane( ).add( lblPrinterInfo );
			
		}
		
		/**
		 * Adding TextArea to show package versioning information.
		 */
		private void addPackageInfoArea(Vector<ApplicationVersion> rowData)
		{
			
			String columnNames[] = {"Name", "Firmware Info","Build Number Info"};
			DefaultTableModel tblModel = new DefaultTableModel(columnNames,0)
			{

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isCellEditable(int row, int column) 
				{
					return false;
				}
				
			};
			
			for (ApplicationVersion info : rowData)
			{
				Object[] data = {info.getAppId(), info.getVersion(), info.getBuildNumber()};
				tblModel.addRow(data);
			}
			
			tablePackageInfo = new JTable(tblModel)
			{
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				//  Returning the Class of each column will allow different
	            //  renderers to be used based on Class
	            public Class<?> getColumnClass(int column)
	            {
	                return getValueAt(0, column).getClass();
	            }
	        };
			
	        tablePackageInfo.setBackground(Color.white);  
	        tablePackageInfo.setForeground(Color.black);  
			Border border = BorderFactory.createLineBorder(Color.BLACK);
			tablePackageInfo.setShowHorizontalLines(false);
			tablePackageInfo.setShowVerticalLines(false);
			tablePackageInfo.setBorder(border);
			
			tablePackageInfo.setFont(new Font( font, Font.PLAIN, fontSize ));
			tablePackageInfo.setPreferredScrollableViewportSize(new Dimension(400, 165));
			tablePackageInfo.setFillsViewportHeight(true);
			tablePackageInfo.getTableHeader().setReorderingAllowed(false);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			tablePackageInfo.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			tablePackageInfo.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			
		
			tablePackageInfo.getColumnModel().getColumn(0).setPreferredWidth(105);
			tablePackageInfo.getColumnModel().getColumn(0).setResizable(true);
			tablePackageInfo.getColumnModel().getColumn(1).setPreferredWidth(180);
			tablePackageInfo.getColumnModel().getColumn(1).setResizable(true);
			tablePackageInfo.getColumnModel().getColumn(2).setPreferredWidth(180);
			tablePackageInfo.getColumnModel().getColumn(2).setResizable(true);
			
			
			jScrollPane = new JScrollPane(tablePackageInfo,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setBounds(10, 60, 520, 200);
			jScrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			getContentPane().add(jScrollPane);
			
		}
		
		private void addDeviceInfoArea(Device device, DeviceInfo deviceInfo)
		{
			
			String columnNames[] = {"Type", "Model", "Serial No.", "Firmware Version"};
			DefaultTableModel tblModel = new DefaultTableModel(columnNames,0)
			{

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isCellEditable(int row, int column) 
				{
					return false;
				}
				
			};
			
			Object[] data = {deviceInfo.getType(), deviceInfo.getModel(), deviceInfo.getSerialNumber(), deviceInfo.getFmwVersion()};
			tblModel.addRow(data);
			
			JTable tableDeviceInfo = new JTable(tblModel)
			{
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				//  Returning the Class of each column will allow different
	            //  renderers to be used based on Class
	            public Class<?> getColumnClass(int column)
	            {
	                return getValueAt(0, column).getClass();
	            }
	        };
			
	        tableDeviceInfo.setBackground(Color.white);  
	        tableDeviceInfo.setForeground(Color.black);  
			Border border = BorderFactory.createLineBorder(Color.BLACK);
			tableDeviceInfo.setShowHorizontalLines(false);
			tableDeviceInfo.setShowVerticalLines(false);
			tableDeviceInfo.setBorder(border);
			
			tableDeviceInfo.setFont(new Font( font, Font.PLAIN, fontSize ));
			tableDeviceInfo.setPreferredScrollableViewportSize(new Dimension(400, 30));
			tableDeviceInfo.setFillsViewportHeight(true);
			tableDeviceInfo.getTableHeader().setReorderingAllowed(false);
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			tableDeviceInfo.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			tableDeviceInfo.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			tableDeviceInfo.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			tableDeviceInfo.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			
			jScrollPane = new JScrollPane(tableDeviceInfo,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			
			if(device == Device.CARD_READER)
			{
				jScrollPane.setBounds(10, 310, 520, 50);
			}
			else
			{
				jScrollPane.setBounds(10, 410, 520, 50);
			}
			jScrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));

			getContentPane().add(jScrollPane);
			
		}
		
		/**
		 * Adding ok button.
		 */
		private void addOkButton()
		{
			btnOk = new JButton( "OK" );
			btnOk.setBounds( 230, 480, 70, 30 );
			btnOk.setFont( new Font( font, Font.PLAIN, fontSize ) );
			btnOk.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
			btnOk.setVisible(true);
			btnOk.addActionListener( new ActionListener( )
			{
				public void actionPerformed(ActionEvent e)
				{
					closeDialog( );
				}
			} );
			
			getContentPane( ).add( btnOk );
			
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
			this.getRootPane().setDefaultButton(btnOk);
		}
		
		/**
		 * Closing Dialog.
		 */
		protected void closeDialog()
		{
			this.setVisible( false );
			this.dispose();
		}
		
		public void showDialog()
		{
			this.setBounds( 0, 0, 550,550 );
			this.setTitle("Firmware Version");
			this.setLocationRelativeTo(TroubleshootController.getTroubleshootView());
			this.setAlwaysOnTop( true );
			this.setIcon();
			this.setModal( true );
			this.setVisible(true);
		}
		
		private void setIcon()
		{
			URL url = TroubleshootMainDialog.class.getClassLoader().getResource( "resources/GVR_Color_Iso.gif" );
			if (url != null)
			{
				this.setIconImage( Toolkit.getDefaultToolkit( ).getImage( url ) );
			}
		}
		
		
		/**
		 * @return returns Ok button reference
		 */
		public JButton getOkBtnRef()
		{
			return btnOk;
		}
}
