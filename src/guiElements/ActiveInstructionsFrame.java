
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
		setBounds(100, 100, 450, 300);

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
		mcontentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mcontentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mcontentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel taskPanel = new JPanel();
		tabbedPane.addTab("Tasks", null, taskPanel, null);
		tabbedPane.setEnabledAt(0, true);
		taskPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		taskPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel taskListPanel = new JPanel();
		scrollPane.setViewportView(taskListPanel);
		taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
		taskListPanel.add(new TaskDisplayPanel());
		
		JPanel taskViewControlPanel = new JPanel();
		taskPanel.add(taskViewControlPanel, BorderLayout.SOUTH);
		FlowLayout fl_taskViewControlPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		taskViewControlPanel.setLayout(fl_taskViewControlPanel);
		
		ButtonGroup taskViewControlGroup = new ButtonGroup();
		
		JRadioButton rdbtnAll = new JRadioButton("All");
		taskViewControlPanel.add(rdbtnAll);
		taskViewControlGroup.add(rdbtnAll);
		
		JRadioButton rdbtnUnavailable = new JRadioButton("Unavailable");
		taskViewControlPanel.add(rdbtnUnavailable);
		taskViewControlGroup.add(rdbtnUnavailable);
		
		JRadioButton rdbtnNotStarted = new JRadioButton("Not Started");
		taskViewControlPanel.add(rdbtnNotStarted);
		taskViewControlGroup.add(rdbtnNotStarted);
		
		JRadioButton rdbtnInProgress = new JRadioButton("In Progress");
		taskViewControlPanel.add(rdbtnInProgress);
		taskViewControlGroup.add(rdbtnInProgress);
		
		JRadioButton rdbtnComplete = new JRadioButton("Complete");
		taskViewControlPanel.add(rdbtnComplete);
		taskViewControlGroup.add(rdbtnComplete);
		
		Box taskControlPanel = Box.createVerticalBox();
		taskPanel.add(taskControlPanel, BorderLayout.EAST);
		
		JButton btnNew = new JButton("New");
		taskControlPanel.add(btnNew);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		taskControlPanel.add(btnEdit);
		
		JButton btnDelete = new JButton("Delete");
		taskControlPanel.add(btnDelete);
		
		JSeparator separator_2 = new JSeparator();
		taskControlPanel.add(separator_2);
		
		JButton btnStart = new JButton("Start");
		taskControlPanel.add(btnStart);
		
		JButton btnPause = new JButton("Pause");
		taskControlPanel.add(btnPause);
		
		JButton btnComplete = new JButton("Complete");
		taskControlPanel.add(btnComplete);
		
		JSeparator separator_3 = new JSeparator();
		taskControlPanel.add(separator_3);
		
		JButton btnNotes = new JButton("Notes");
		taskControlPanel.add(btnNotes);
		
		Box resourcePanel = Box.createHorizontalBox();
		tabbedPane.addTab("Resources", null, resourcePanel, null);
		
		JList toolsList = new JList();
		toolsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resourcePanel.add(toolsList);
		
		Box toolsControls = Box.createVerticalBox();
		resourcePanel.add(toolsControls);
		
		JList partsList = new JList();
		partsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resourcePanel.add(partsList);
		
		Box partsControl = Box.createVerticalBox();
		resourcePanel.add(partsControl);
	}
}
