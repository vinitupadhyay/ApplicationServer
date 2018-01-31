package troubleshoot.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import troubleshoot.controller.TroubleshootController;
import troubleshoot.model.pojo.LogError;
import troubleshoot.states.TestStatus;

public class ShowErrorsDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static final Color colorYellow = Color.decode("#ffff80");
	private static final Color colorGreen = Color.decode("#53c653");
	private static final Color colorRed = Color.decode("#ff3333");
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	

	private JLabel lblHeading;	
	private JScrollPane jScrollPane;
	public JPanel tblPanel;
	public JTable tableErrors;
	private JButton	btnOk;
	
	private JButton btnFixInAction;
	
	private static ShowErrorsDialog	errorDialog;
	Vector<LogError> foundedErrors;
	
	public ShowErrorsDialog(Vector<LogError> rowData)
	{
		errorDialog = this;
		foundedErrors = rowData;
		initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addLabels();
		addInformationArea();
		addOkButton( );
		pack();
	}
	
	private void windowSettings()
	{
		Point parentLocation = TroubleshootController.getCenterLocation(TroubleshootController.getTroubleshootView());
		int width = parentLocation.x - 275;
		int height = parentLocation.y - 205;
		this.setLocation(width, height);
		this.setPreferredSize(new Dimension(550, 410));
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.white);
	}
	
	public static ShowErrorsDialog getInstance()
	{
		return errorDialog;
	}
	
	private void addLabels()
	{
		lblHeading = new JLabel( "Issues identified via logs" );
		lblHeading.setBounds( 175, 20, 200, 30 );
		lblHeading.setFont(new Font(font, Font.BOLD, 15));
		lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeading.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane( ).add( lblHeading );	
	}
	
	private void addInformationArea()
	{
		
		String columnNames[] = {"Device", "Error", "Severity", "Action"};
		DefaultTableModel tblModel = new DefaultTableModel(columnNames, 0)
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) 
			{
				if(column == 3)
				{
					return true;	
				}
				else
				{
					return false;
				}
			}
			
		};
		
		addErrorsDataInRow(tblModel);
		
		tableErrors = new JTable(tblModel)
		{
			private static final long serialVersionUID = 1L;

			//  Returning the Class of each column will allow different
            //  Renderer to be used based on Class
			public Class<?> getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
		
        tableErrors.setBackground(Color.white);  
        tableErrors.setForeground(Color.black);  
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		tableErrors.setShowHorizontalLines(false);
		tableErrors.setShowVerticalLines(false);
		tableErrors.setBorder(border);
		
		tableErrors.setFont(new Font( font, Font.PLAIN, fontSize ));
		tableErrors.setPreferredScrollableViewportSize(new Dimension(400, 165));
		tableErrors.setFillsViewportHeight(true);
		tableErrors.getTableHeader().setReorderingAllowed(false);
		tableErrors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tableErrors.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableErrors.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tableErrors.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tableErrors.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
		tableErrors.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());
		
		tableErrors.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableErrors.getColumnModel().getColumn(0).setResizable(true);
		tableErrors.getColumnModel().getColumn(1).setPreferredWidth(280);
		tableErrors.getColumnModel().getColumn(1).setResizable(true);
		tableErrors.getColumnModel().getColumn(2).setPreferredWidth(10);
		tableErrors.getColumnModel().getColumn(2).setResizable(true);
		tableErrors.getColumnModel().getColumn(3).setPreferredWidth(30);
		tableErrors.getColumnModel().getColumn(3).setResizable(true);
	
		jScrollPane = new JScrollPane(tableErrors,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setBounds(10, 90, 520, 240);
		jScrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		getContentPane().add(jScrollPane);
		
	}
	
	public void addErrorsDataInRow(DefaultTableModel tblModel)
	{
		for (LogError error : foundedErrors)
		{
			Object[] data = {error.getCategory(), error.getErrorString(), error.getSeverity(), getFixBtn()};
			tblModel.addRow(data);
		}
	}
	
	
	private JButton getFixBtn()
	{
		JButton btnFix = new JButton("FIX");
		btnFix.setOpaque(true);
		btnFix.setBorderPainted(true);
		btnFix.setFont( new Font( font, Font.PLAIN, fontSize) );
		btnFix.setForeground(Color.black);
		btnFix.setBackground(colorYellow);
		btnFix.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		btnFix.setVisible(true);
		return btnFix;
	}
	
	private void addOkButton()
	{
		btnOk = new JButton("OK");
		btnOk.setBounds( 240, 340, 70, 30 );
		btnOk.setBorderPainted( false );
		btnOk.setFont( new Font( font, Font.PLAIN, fontSize) );
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
		errorDialog = null;
		this.setVisible( false );
		this.dispose();
	}
	
	public void showDialog()
	{
		this.setTitle("Troubleshooting - Issues Identified");
		this.setModal(true);
		this.setAlwaysOnTop(false);
		this.setIcon();
		this.setVisible(true);
	}
	
	public JButton getBtnFixInAction()
	{
		return btnFixInAction;
	}
	
	public static void updateBtnFixInAcion(TestStatus testStatus)
	{
		if(errorDialog != null)
		{
			if(errorDialog.btnFixInAction != null)
			{
				if(testStatus == TestStatus.PASS)
				{
					errorDialog.btnFixInAction.setBackground(colorGreen);
					errorDialog.btnFixInAction.setForeground(Color.black);
					
				}
				else if(testStatus == TestStatus.FAIL)
				{
					errorDialog.btnFixInAction.setBackground(colorRed);
					errorDialog.btnFixInAction.setForeground(Color.white);
				}
				errorDialog.repaint();
			}
		}
	}
	
	class ButtonRenderer implements TableCellRenderer
	{
		private JButton btnFix;
		
		public ButtonRenderer()
		{
			
		}
	   
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			btnFix = (JButton)value;
			return btnFix;
		}
	}
	
	class ButtonEditor extends AbstractCellEditor implements TableCellEditor
	{
		private static final long serialVersionUID = 1L;
		
		protected JButton btnFix;
		private int rowNumber;
		private ActionListener actionListener;

		public ButtonEditor()
		{
			
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		{
			btnFix = (JButton)value;
			btnFixInAction = btnFix;
			rowNumber = row;
			actionListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					TroubleshootController.getInstance().checkUPMlogsFix(ShowErrorsDialog.getInstance().foundedErrors.get(rowNumber));
					fireEditingStopped();
				}
			};
			
			btnFix.addActionListener(actionListener);
			
			return btnFix;
		}

		public Object getCellEditorValue()
		{
			btnFix.removeActionListener(actionListener);
			return btnFix;
		}
	}
}
