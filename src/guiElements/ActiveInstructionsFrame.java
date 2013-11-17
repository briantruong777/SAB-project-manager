package guiElements;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import resourceModel.*;
import taskModel.*;

@SuppressWarnings("serial")
public class ActiveInstructionsFrame extends JFrame
{
	private TaskPanel taskPanel;
	private ResourcePanel resourcePanel;
	private JMenuItem mntmSave;
	private FileStatus status;
	/**
	 * Create the frame.
	 */
	public ActiveInstructionsFrame()
	{
		status = FileStatus.UNCHANGED;
		FileHandler mfileHandler = new FileHandler();

		addWindowListener(mfileHandler);
		setTitle("Untitled - Active Instructions");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 700, 400);

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

		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(mfileHandler);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(mfileHandler);
		mnFile.add(mntmSaveAs);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		JMenuItem mntmExport = new JMenuItem("Export");
		mnFile.add(mntmExport);

		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(mfileHandler);
		mnFile.add(mntmQuit);
		JPanel mcontentPane = new JPanel();
		mcontentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mcontentPane);
		mcontentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mcontentPane.add(tabbedPane);

		taskPanel = new TaskPanel();
		tabbedPane.addTab("Tasks", null, taskPanel, null);
		tabbedPane.setEnabledAt(0, true);

		resourcePanel = new ResourcePanel();
		tabbedPane.addTab("Resources", null, resourcePanel, null);
	}

	private enum FileStatus
	{
		CHANGED, UNCHANGED;
	}

	public void notifyChange()
	{
		status = FileStatus.CHANGED;
	}

	public void clearTaskPanel()
	{
		mntmSave.setEnabled(false);
		taskPanel.clearTaskList();
		resourcePanel.clearInventory();
	}

	public void reloadTaskPanel()
	{
		mntmSave.setEnabled(false);
		taskPanel.reloadTaskList();
		resourcePanel.reloadInventory();
	}
	
	private class FileHandler extends WindowAdapter implements ActionListener
	{
		//public static String lastSavedLocation;
		private File file;
		private JFileChooser fileChooser;
		
		public FileHandler()
		{
			file = new File("");
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//lastSavedLocation = getLastSavedLocation();
		}


		/*public String getLastSavedLocation()
		{
			try
			{
				Scanner reader = new Scanner(f);
				return reader.nextLine();
			}
			catch (FileNotFoundException e)
			{
				f = new File("C:\\lastsavedlocation.txt");
				return "";
			}		
		}

		public String setLastSavedLocation(String path)
		{
			try
			{
				PrintWriter writer = new PrintWriter(f);
				writer.write("");
				writer.close();
			}
			catch (FileNotFoundException e)
			{

			}
			return "";
		}*/

		public void actionPerformed(ActionEvent e)
		{
			switch(e.getActionCommand())
			{
				case "New":
					if (!savedChanges())
						return;
					actionNew();
					break;
				case "Open":
					if (!savedChanges())
						return;
					open();
					break;
				case "Save":
					save();
					break;
				case "Save As...":
					saveAs();
					break;
				case "Export":
					export();
					break;
				case "Quit":
					if (!savedChanges())
						return;
					System.exit(0);
					break;
			}
		}
		
		// returns whether to continue after asking for unsaved changes
		public boolean savedChanges()
		{
			if (status == FileStatus.UNCHANGED)
				return true;
			else
			{
				switch(JOptionPane.showConfirmDialog(null, "Save changes?", "", JOptionPane.YES_NO_CANCEL_OPTION))
				{
					case JOptionPane.YES_OPTION:
						if (file.isFile())
							save();
						else if (!saveAs())
							return false;
						return true;
					case JOptionPane.NO_OPTION:
						return true;
					default:
						return false;
				}
			}
		}

		public void actionNew()
		{
			setTitle("Untitled - Active Instructions");
			Inventory.clear();
			TaskManager.clear();
			clearTaskPanel();
			status = FileStatus.UNCHANGED;
		}

		public void open()
		{
			/*if (hasBeenSaved())
			{
				Runner.loadTasks(lastSavedLocation);
			}
			else
			{*/
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue != JFileChooser.APPROVE_OPTION)
				return;
			
			// !!!assuming this file is correct
			file = fileChooser.getSelectedFile();
			setTitle(file.getName() + " - Active Instructions");
			Runner.loadProject(file.getAbsolutePath());
			status = FileStatus.UNCHANGED;
			
				//setLastSavedLocation(selectedFile.getAbsolutePath());
			//Runner.loadTools(path+"/tools");
			//}
		}

		public void save()
		{
			//if (!hasBeenSaved())
			Runner.saveProject(file.getAbsolutePath());
			status = FileStatus.UNCHANGED;
			/*else
			{
				//Runner.saveTasks(lastSavedLocation);
			}*/
		}

		public boolean saveAs()
		{
			JFileChooser fileChooser = new JFileChooser();
			int returnValue = fileChooser.showSaveDialog(null);
			if (returnValue != JFileChooser.APPROVE_OPTION)
				return false;

			file = fileChooser.getSelectedFile();
//			System.out.println(selectedFile.getAbsolutePath());
				//setLastSavedLocation(selectedFile.getAbsolutePath());
			Runner.saveProject(file.getAbsolutePath());
			setTitle(file.getName() + " - Active Instructions");
			status = FileStatus.UNCHANGED;
			return true;
		}

		public void export()
		{
			// Excel???
		}

		/*public boolean hasBeenSaved()
		{
			return getLastSavedLocation().length() != 0;
		}*/

		@Override
		public void windowClosing(WindowEvent e)
		{
			if (!savedChanges())
				return;
			System.exit(0);
		}
	}
}

