package guiElements;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ResourcePanel extends JPanel
{
	private JTextField toolsNameTextField;
	private JTextField partsNameTextField;
	/**
	 * Create the panel.
	 */
	public ResourcePanel()
	{
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
//		JPanel panel = new JPanel();
//		add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_panel);
		
		JLabel toolsLabel = new JLabel("Tools (Available/Max)");
		GridBagConstraints gbc_toolsLabel = new GridBagConstraints();
		gbc_toolsLabel.gridwidth = 3;
		gbc_toolsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toolsLabel.gridx = 0;
		gbc_toolsLabel.gridy = 0;
		add(toolsLabel, gbc_toolsLabel);
		
		JLabel partsLabel = new JLabel("Parts (Available/Max)");
		GridBagConstraints gbc_partsLabel = new GridBagConstraints();
		gbc_partsLabel.gridwidth = 3;
		gbc_partsLabel.insets = new Insets(0, 0, 5, 0);
		gbc_partsLabel.gridx = 3;
		gbc_partsLabel.gridy = 0;
		add(partsLabel, gbc_partsLabel);
		
		JScrollPane toolsScroll = new JScrollPane();
		GridBagConstraints gbc_toolsScroll = new GridBagConstraints();
		gbc_toolsScroll.fill = GridBagConstraints.BOTH;
		gbc_toolsScroll.gridheight = 7;
		gbc_toolsScroll.insets = new Insets(0, 0, 0, 5);
		gbc_toolsScroll.gridx = 0;
		gbc_toolsScroll.gridy = 1;
		add(toolsScroll, gbc_toolsScroll);
		
		JList toolsList = new JList();
		toolsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
			}
		});
		toolsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		toolsScroll.setViewportView(toolsList);
		
		JScrollPane partsScroll = new JScrollPane();
		GridBagConstraints gbc_partsScroll = new GridBagConstraints();
		gbc_partsScroll.fill = GridBagConstraints.BOTH;
		gbc_partsScroll.gridheight = 7;
		gbc_partsScroll.insets = new Insets(0, 0, 0, 5);
		gbc_partsScroll.gridx = 3;
		gbc_partsScroll.gridy = 1;
		add(partsScroll, gbc_partsScroll);
		
		JList partsList = new JList();
		partsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			}
		});
		partsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		partsScroll.setViewportView(partsList);
		
		JLabel toolsNameLabel = new JLabel("Name");
		GridBagConstraints gbc_toolsNameLabel = new GridBagConstraints();
		gbc_toolsNameLabel.anchor = GridBagConstraints.EAST;
		gbc_toolsNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toolsNameLabel.gridx = 1;
		gbc_toolsNameLabel.gridy = 2;
		add(toolsNameLabel, gbc_toolsNameLabel);
		
		toolsNameTextField = new JTextField();
		toolsNameTextField.setColumns(10);
		GridBagConstraints gbc_toolsNameTextField = new GridBagConstraints();
		gbc_toolsNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_toolsNameTextField.gridx = 2;
		gbc_toolsNameTextField.gridy = 2;
		add(toolsNameTextField, gbc_toolsNameTextField);
		
		JLabel partsNameLabel = new JLabel("Name");
		GridBagConstraints gbc_partsNameLabel = new GridBagConstraints();
		gbc_partsNameLabel.anchor = GridBagConstraints.EAST;
		gbc_partsNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_partsNameLabel.gridx = 4;
		gbc_partsNameLabel.gridy = 2;
		add(partsNameLabel, gbc_partsNameLabel);
		
		partsNameTextField = new JTextField();
		partsNameTextField.setColumns(10);
		GridBagConstraints gbc_partsNameTextField = new GridBagConstraints();
		gbc_partsNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_partsNameTextField.gridx = 5;
		gbc_partsNameTextField.gridy = 2;
		add(partsNameTextField, gbc_partsNameTextField);
		
		JLabel toolsMaxLabel = new JLabel("Max#");
		GridBagConstraints gbc_toolsMaxLabel = new GridBagConstraints();
		gbc_toolsMaxLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toolsMaxLabel.gridx = 1;
		gbc_toolsMaxLabel.gridy = 3;
		add(toolsMaxLabel, gbc_toolsMaxLabel);
		
		JSpinner toolsMaxSpinner = new JSpinner();
		GridBagConstraints gbc_toolsMaxSpinner = new GridBagConstraints();
		gbc_toolsMaxSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsMaxSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_toolsMaxSpinner.gridx = 2;
		gbc_toolsMaxSpinner.gridy = 3;
		add(toolsMaxSpinner, gbc_toolsMaxSpinner);
		
		JLabel partsMaxLabel = new JLabel("Max#");
		GridBagConstraints gbc_partsMaxLabel = new GridBagConstraints();
		gbc_partsMaxLabel.insets = new Insets(0, 0, 5, 5);
		gbc_partsMaxLabel.gridx = 4;
		gbc_partsMaxLabel.gridy = 3;
		add(partsMaxLabel, gbc_partsMaxLabel);
		
		JSpinner partsMaxSpinner = new JSpinner();
		GridBagConstraints gbc_partsMaxSpinner = new GridBagConstraints();
		gbc_partsMaxSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsMaxSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_partsMaxSpinner.gridx = 5;
		gbc_partsMaxSpinner.gridy = 3;
		add(partsMaxSpinner, gbc_partsMaxSpinner);
		
		JButton toolsAdd = new JButton("Add");
		toolsAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_toolsAdd = new GridBagConstraints();
		gbc_toolsAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsAdd.gridwidth = 2;
		gbc_toolsAdd.insets = new Insets(0, 0, 5, 5);
		gbc_toolsAdd.gridx = 1;
		gbc_toolsAdd.gridy = 4;
		add(toolsAdd, gbc_toolsAdd);
		
		JButton partsAdd = new JButton("Add");
		partsAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_partsAdd = new GridBagConstraints();
		gbc_partsAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsAdd.gridwidth = 2;
		gbc_partsAdd.insets = new Insets(0, 0, 5, 0);
		gbc_partsAdd.gridx = 4;
		gbc_partsAdd.gridy = 4;
		add(partsAdd, gbc_partsAdd);
		
		JButton toolsChange = new JButton("Change");
		toolsChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_toolsChange = new GridBagConstraints();
		gbc_toolsChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsChange.gridwidth = 2;
		gbc_toolsChange.insets = new Insets(0, 0, 5, 5);
		gbc_toolsChange.gridx = 1;
		gbc_toolsChange.gridy = 5;
		add(toolsChange, gbc_toolsChange);
		
		JButton partsChange = new JButton("Change");
		partsChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_partsChange = new GridBagConstraints();
		gbc_partsChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsChange.gridwidth = 2;
		gbc_partsChange.insets = new Insets(0, 0, 5, 0);
		gbc_partsChange.gridx = 4;
		gbc_partsChange.gridy = 5;
		add(partsChange, gbc_partsChange);
		
		JButton toolsRemove = new JButton("Remove");
		toolsRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_toolsRemove = new GridBagConstraints();
		gbc_toolsRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsRemove.gridwidth = 2;
		gbc_toolsRemove.insets = new Insets(0, 0, 5, 5);
		gbc_toolsRemove.gridx = 1;
		gbc_toolsRemove.gridy = 6;
		add(toolsRemove, gbc_toolsRemove);
		
		JButton partsRemove = new JButton("Remove");
		partsRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_partsRemove = new GridBagConstraints();
		gbc_partsRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsRemove.gridwidth = 2;
		gbc_partsRemove.insets = new Insets(0, 0, 5, 0);
		gbc_partsRemove.gridx = 4;
		gbc_partsRemove.gridy = 6;
		add(partsRemove, gbc_partsRemove);

	}
}
