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

public class TroubleShootingPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int fontSize = 12;
	private static final String font = "segoe ui";
	
	private JLabel lblTroubleShooting;
	private JButton btnCheckUpmLogs;
	private JButton btnRunSelfCheck;
	private JButton btnGenerateReport;
	private JButton btnViewReport;
	private JButton btnExtractCurrentLogs;
	private JButton btnFirmVersion;
	
	private JLabel[] emptyLabels = null;
	
	private GridBagConstraints gridBagConstraints;
	
	public TroubleShootingPanel() 
	{
		this.initialise();
	}
	
	private void initialise()
	{
		windowSettings();
		addLabels();
		addButtons();
		addEmptyLabelsVertically(2, 0, 4);
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
		lblTroubleShooting = new JLabel("Troubleshooting");
		//lblSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		gridBagConstraints = getConstraint(0, 0, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		lblTroubleShooting.setFont(new Font(font, Font.BOLD, 15));
		lblTroubleShooting.setHorizontalAlignment(SwingConstants.CENTER);
		lblTroubleShooting.setVerticalAlignment(SwingConstants.CENTER);
		this.add(lblTroubleShooting, gridBagConstraints);
		
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
		btnCheckUpmLogs = new JButton("Check UPM Logs");
		gridBagConstraints = getConstraint(0, 1, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnCheckUpmLogs.setHorizontalAlignment(SwingConstants.CENTER);
		btnCheckUpmLogs.setVerticalAlignment(SwingConstants.CENTER);
		btnCheckUpmLogs.setFont(new Font(font, Font.PLAIN, fontSize));
		btnCheckUpmLogs.setVisible(true);
		btnCheckUpmLogs.setEnabled(true);
		this.add(btnCheckUpmLogs, gridBagConstraints);
		btnCheckUpmLogs.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				TroubleshootController.getInstance().checkUPMlogs();
			}
		});
		
		btnRunSelfCheck = new JButton("Run Self Check");
		gridBagConstraints = getConstraint(0, 2, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnRunSelfCheck.setHorizontalAlignment(SwingConstants.CENTER);
		btnRunSelfCheck.setVerticalAlignment(SwingConstants.CENTER);
		btnRunSelfCheck.setFont(new Font(font, Font.PLAIN, fontSize));
		btnRunSelfCheck.setVisible(true);
		btnRunSelfCheck.setEnabled(true);
		this.add(btnRunSelfCheck, gridBagConstraints);
		btnRunSelfCheck.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				TroubleshootController.getInstance().runSelfcheck();
			}
		});
		
		btnGenerateReport = new JButton("Generate Report");
		gridBagConstraints = getConstraint(0, 3, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnGenerateReport.setHorizontalAlignment(SwingConstants.CENTER);
		btnGenerateReport.setVerticalAlignment(SwingConstants.CENTER);
		btnGenerateReport.setFont(new Font(font, Font.PLAIN, fontSize));
		btnGenerateReport.setVisible(true);
		btnGenerateReport.setEnabled(true);
		this.add(btnGenerateReport, gridBagConstraints);
		btnGenerateReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new ReportInputDialog().showDialog("Report - Input");
			}
		});
		
		btnViewReport = new JButton("View Report");
		gridBagConstraints = getConstraint(0, 4, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnViewReport.setHorizontalAlignment(SwingConstants.CENTER);
		btnViewReport.setVerticalAlignment(SwingConstants.CENTER);
		btnViewReport.setFont(new Font(font, Font.PLAIN, fontSize));
		btnViewReport.setVisible(true);
		btnViewReport.setEnabled(true);
		this.add(btnViewReport, gridBagConstraints);
		btnViewReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				TroubleshootController.getInstance().ViewReport();
			}
		});
		
		btnExtractCurrentLogs = new JButton("Extract Current Logs");
		gridBagConstraints = getConstraint(0, 5, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnExtractCurrentLogs.setHorizontalAlignment(SwingConstants.CENTER);
		btnExtractCurrentLogs.setVerticalAlignment(SwingConstants.CENTER);
		btnExtractCurrentLogs.setFont(new Font(font, Font.PLAIN, fontSize));
		btnExtractCurrentLogs.setVisible(true);
		btnExtractCurrentLogs.setEnabled(true);
		this.add(btnExtractCurrentLogs, gridBagConstraints);
		btnExtractCurrentLogs.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				TroubleshootController.getInstance().startDownloadCurrentLogs();
			}
		});
		
		btnFirmVersion = new JButton("Firmware Version");
		gridBagConstraints = getConstraint(0, 6, 1, 1, GridBagConstraints.BOTH, 0.10, 0.10);
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		btnFirmVersion.setHorizontalAlignment(SwingConstants.CENTER);
		btnFirmVersion.setVerticalAlignment(SwingConstants.CENTER);
		btnFirmVersion.setFont(new Font(font, Font.PLAIN, fontSize));
		btnFirmVersion.setVisible(true);
		btnFirmVersion.setEnabled(true);
		this.add(btnFirmVersion, gridBagConstraints);
		btnFirmVersion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				TroubleshootController.getInstance().getFirmwareInformation();
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
		btnCheckUpmLogs.setEnabled(isEnable);
		btnRunSelfCheck.setEnabled(isEnable);
		btnGenerateReport.setEnabled(isEnable);
		btnViewReport.setEnabled(isEnable);
		btnExtractCurrentLogs.setEnabled(isEnable);
		btnFirmVersion.setEnabled(isEnable);
	}

}
