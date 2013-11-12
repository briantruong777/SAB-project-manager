
package guiElements;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.Box;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSpinner;

public class ActiveInstructionsFrame extends JFrame
{

	private JPanel mcontentPane;
	private FileHandler mfileHandler;
	private JTextField mtextToolName;
	private JTextField mtextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ActiveInstructionsFrame frame = new ActiveInstructionsFrame();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ActiveInstructionsFrame()
	{
		setTitle("Untitled - Active Instructions");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 303);

		mfileHandler = new FileHandler();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(mfileHandler);
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(mfileHandler);
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(mfileHandler);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(mfileHandler);
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(mfileHandler);
		
		JMenuItem mntmExport = new JMenuItem("Export");
		mnFile.add(mntmExport);
		
		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);
		mnFile.add(mntmClose);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(mfileHandler);
		mnFile.add(mntmQuit);
		mcontentPane = new JPanel();
		mcontentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mcontentPane);
		mcontentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mcontentPane.add(tabbedPane);
		
		JPanel taskPanel = new JPanel();
		tabbedPane.addTab("Tasks", null, taskPanel, null);
		tabbedPane.setEnabledAt(0, true);
		taskPanel.setLayout(new BorderLayout(0, 0));
				
				JScrollPane taskScroll = new JScrollPane();
				taskPanel.add(taskScroll, BorderLayout.CENTER);
				
				JList taskList = new JList();
				taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				taskScroll.setViewportView(taskList);
		
				JPanel taskViewControlPanel = new JPanel();
				taskPanel.add(taskViewControlPanel, BorderLayout.SOUTH);
				FlowLayout fl_taskViewControlPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
				taskViewControlPanel.setLayout(fl_taskViewControlPanel);
				
				
				JCheckBox rdbtnUnavailable = new JCheckBox("Unavailable");
				rdbtnUnavailable.setToolTipText("View tasks whose constraints have not been met.");
				taskViewControlPanel.add(rdbtnUnavailable);
				
				JCheckBox rdbtnIncomplete = new JCheckBox("Incomplete");
				rdbtnIncomplete.setSelected(true);
				rdbtnIncomplete.setToolTipText("View tasks that can be started or are in progress.");
				taskViewControlPanel.add(rdbtnIncomplete);
				
				JCheckBox rdbtnComplete = new JCheckBox("Complete");
				rdbtnComplete.setToolTipText("View completed tasks.");
				taskViewControlPanel.add(rdbtnComplete);
				
				Box taskControlPanel = Box.createVerticalBox();
				taskPanel.add(taskControlPanel, BorderLayout.EAST);
				
				JButton btnNew = new JButton("New");
				taskControlPanel.add(btnNew);
				
				JButton btnDelete = new JButton("Delete");
				taskControlPanel.add(btnDelete);
				
				Box verticalBox = Box.createVerticalBox();
				taskPanel.add(verticalBox, BorderLayout.WEST);
				
				JPanel resourcePanel = new JPanel();
				tabbedPane.addTab("Resources", null, resourcePanel, null);
				GridBagLayout gbl_resourcePanel = new GridBagLayout();
				gbl_resourcePanel.columnWidths = new int[]{0, 0, 0, 0, 0};
				gbl_resourcePanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
				gbl_resourcePanel.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
				gbl_resourcePanel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
				resourcePanel.setLayout(gbl_resourcePanel);
				
				JLabel lblTools = new JLabel("Tools");
				GridBagConstraints gbc_lblTools = new GridBagConstraints();
				gbc_lblTools.gridwidth = 2;
				gbc_lblTools.insets = new Insets(0, 0, 5, 5);
				gbc_lblTools.gridx = 0;
				gbc_lblTools.gridy = 0;
				resourcePanel.add(lblTools, gbc_lblTools);
				
				JLabel lblParts = new JLabel("Parts");
				GridBagConstraints gbc_lblParts = new GridBagConstraints();
				gbc_lblParts.gridwidth = 2;
				gbc_lblParts.insets = new Insets(0, 0, 5, 0);
				gbc_lblParts.gridx = 2;
				gbc_lblParts.gridy = 0;
				resourcePanel.add(lblParts, gbc_lblParts);
				
				JList toolList = new JList();
				GridBagConstraints gbc_toolList = new GridBagConstraints();
				gbc_toolList.gridheight = 7;
				gbc_toolList.insets = new Insets(0, 0, 0, 5);
				gbc_toolList.fill = GridBagConstraints.BOTH;
				gbc_toolList.gridx = 0;
				gbc_toolList.gridy = 1;
				resourcePanel.add(toolList, gbc_toolList);
				
				mtextToolName = new JTextField();
				GridBagConstraints gbc_textToolName = new GridBagConstraints();
				gbc_textToolName.insets = new Insets(0, 0, 5, 5);
				gbc_textToolName.fill = GridBagConstraints.HORIZONTAL;
				gbc_textToolName.gridx = 1;
				gbc_textToolName.gridy = 2;
				resourcePanel.add(mtextToolName, gbc_textToolName);
				mtextToolName.setColumns(10);
				
				JList partList = new JList();
				GridBagConstraints gbc_partList = new GridBagConstraints();
				gbc_partList.gridheight = 7;
				gbc_partList.insets = new Insets(0, 0, 0, 5);
				gbc_partList.fill = GridBagConstraints.BOTH;
				gbc_partList.gridx = 2;
				gbc_partList.gridy = 1;
				resourcePanel.add(partList, gbc_partList);
				
				mtextField = new JTextField();
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 0);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 3;
				gbc_textField.gridy = 2;
				resourcePanel.add(mtextField, gbc_textField);
				mtextField.setColumns(10);
				
				JSpinner toolNum = new JSpinner();
				GridBagConstraints gbc_toolNum = new GridBagConstraints();
				gbc_toolNum.insets = new Insets(0, 0, 5, 5);
				gbc_toolNum.gridx = 1;
				gbc_toolNum.gridy = 3;
				resourcePanel.add(toolNum, gbc_toolNum);
				
				JSpinner partNum = new JSpinner();
				GridBagConstraints gbc_partNum = new GridBagConstraints();
				gbc_partNum.insets = new Insets(0, 0, 5, 0);
				gbc_partNum.gridx = 3;
				gbc_partNum.gridy = 3;
				resourcePanel.add(partNum, gbc_partNum);
				
				JButton btnAddTool = new JButton("Add");
				GridBagConstraints gbc_btnAddTool = new GridBagConstraints();
				gbc_btnAddTool.insets = new Insets(0, 0, 5, 5);
				gbc_btnAddTool.gridx = 1;
				gbc_btnAddTool.gridy = 4;
				resourcePanel.add(btnAddTool, gbc_btnAddTool);
				
				JButton btnAddPart = new JButton("Add");
				GridBagConstraints gbc_btnAddPart = new GridBagConstraints();
				gbc_btnAddPart.insets = new Insets(0, 0, 5, 0);
				gbc_btnAddPart.gridx = 3;
				gbc_btnAddPart.gridy = 4;
				resourcePanel.add(btnAddPart, gbc_btnAddPart);
				
				JButton btnChangeTool = new JButton("Change");
				GridBagConstraints gbc_btnChangeTool = new GridBagConstraints();
				gbc_btnChangeTool.insets = new Insets(0, 0, 5, 5);
				gbc_btnChangeTool.gridx = 1;
				gbc_btnChangeTool.gridy = 5;
				resourcePanel.add(btnChangeTool, gbc_btnChangeTool);
				
				JButton btnChangePart = new JButton("Change");
				GridBagConstraints gbc_btnChangePart = new GridBagConstraints();
				gbc_btnChangePart.insets = new Insets(0, 0, 5, 0);
				gbc_btnChangePart.gridx = 3;
				gbc_btnChangePart.gridy = 5;
				resourcePanel.add(btnChangePart, gbc_btnChangePart);
				
				JButton btnRemoveTool = new JButton("Remove");
				GridBagConstraints gbc_btnRemoveTool = new GridBagConstraints();
				gbc_btnRemoveTool.insets = new Insets(0, 0, 5, 5);
				gbc_btnRemoveTool.gridx = 1;
				gbc_btnRemoveTool.gridy = 6;
				resourcePanel.add(btnRemoveTool, gbc_btnRemoveTool);
				
				JButton btnRemovePart = new JButton("Remove");
				GridBagConstraints gbc_btnRemovePart = new GridBagConstraints();
				gbc_btnRemovePart.insets = new Insets(0, 0, 5, 0);
				gbc_btnRemovePart.gridx = 3;
				gbc_btnRemovePart.gridy = 6;
				resourcePanel.add(btnRemovePart, gbc_btnRemovePart);
	}
}
