
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
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ActiveInstructionsFrame extends JFrame
{

	private JPanel mcontentPane;
	private FileHandler mfileHandler;

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
		setBounds(100, 100, 700, 400);

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
				
				JPanel resourcePanel = new ResourcePanel();
				tabbedPane.addTab("Resources", null, resourcePanel, null);
	}
}
