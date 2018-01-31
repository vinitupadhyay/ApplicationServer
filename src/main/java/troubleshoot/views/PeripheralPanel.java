package troubleshoot.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import troubleshoot.controller.TroubleshootController;

public class PeripheralPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private JLabel lblPeripheral;
	private JButton btnPrinter;
	
	private JLabel[] emptyLabels = null;
	
	private GridBagConstraints gridBagConstraints;
	
	public PeripheralPanel() 
	{
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addLabels();
		addButtons();
		addEmptyLabelsVertically(6, 0, 1);
	}
	
	private void windowSettings()
	{
		this.setPreferredSize(new Dimension(600, 60));
		/*TitledBorder border = new TitledBorder("Label");
		border.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setBorder(border);*/
		this.setLayout(new GridBagLayout());
	}
	
	private GridBagConstraints getConstraint(int gridx, int gridy, int gridWidth, int gridHeight, int fill, double weightx, double weighty)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridWidth;
		constraints.gridheight = gridHeight;
		constraints.fill = fill;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		
		return constraints;
	}
	
	private void addLabels()
	{
		lblPeripheral = new JLabel("Other Peripherals");
		//lblSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblPeripheral.setFont(new Font(font, Font.BOLD, 15));
		lblPeripheral.setHorizontalAlignment(SwingConstants.CENTER);
		lblPeripheral.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblPeripheral, gridBagConstraints);
		
		JLabel lblempty = new JLabel(" ");
		//lblempty.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 9, 1, 1, GridBagConstraints.BOTH, 0.5, 0.5);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblempty.setFont(new Font(font, Font.PLAIN, fontSize));
		lblempty.setHorizontalAlignment(SwingConstants.LEFT);
		lblempty.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblempty, gridBagConstraints);
	}
	
	private void addButtons()
	{
		
		btnPrinter = new JButton("Printer");
		gridBagConstraints = getConstraint(0, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnPrinter.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrinter.setVerticalAlignment(SwingConstants.CENTER);
		btnPrinter.setFont(new Font(font, Font.PLAIN, fontSize));
		btnPrinter.setVisible(true);
		btnPrinter.setEnabled(true);
		this.add(btnPrinter, gridBagConstraints);
		btnPrinter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				PrinterDialog.getInstance().clearAll();
				TroubleshootController.getInstance().collectOPTInformation(false);
			}
		});
	}
	
	private void addEmptyLabelsVertically(int emptyLabelCount, int gridx, int gridy)
	{
		emptyLabels = new JLabel[emptyLabelCount];
		for(int i=0; i<emptyLabelCount; i++)
		{
			emptyLabels[i] = new JLabel(" ");
			//emptyLabels[i].setBorder(BorderFactory.createLineBorder(Color.black));
			gridBagConstraints = getConstraint(gridx, (1+gridy+i), 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			emptyLabels[i].setFont(new Font(font, Font.PLAIN, fontSize));
			emptyLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
			emptyLabels[i].setVerticalAlignment(SwingConstants.CENTER);
			this.add(emptyLabels[i], gridBagConstraints);
		}
	}
	
	public void enableAll(boolean isEnable)
	{
		btnPrinter.setEnabled(isEnable);
	}
}
