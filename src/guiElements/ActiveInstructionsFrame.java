package guiElements;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import resourceModel.Inventory;
import resourceModel.ResourceConstraint;
import taskModel.Task;
import taskModel.TaskManager;

@SuppressWarnings("serial")
public class ActiveInstructionsFrame extends JFrame
{
	private TaskPanel taskPanel;
	private ResourcePanel resourcePanel;
	private JMenuItem menuSave;
	private FileStatus status;
	private File file;
	/**
	 * Create the frame.
	 */
	public ActiveInstructionsFrame()
	{
		file = new File("");
		status = FileStatus.UNCHANGED;
		FileHandler mfileHandler = new FileHandler(this);

		addWindowListener(mfileHandler);
		setTitle("Untitled - Active Instructions");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 700, 400);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem menuNew = new JMenuItem("New...");
		menuNew.addActionListener(mfileHandler);
		menuNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(menuNew);

		JMenuItem menuOpen = new JMenuItem("Open...");
		menuOpen.addActionListener(mfileHandler);
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(menuOpen);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		menuSave = new JMenuItem("Save");
		menuSave.setEnabled(false);
		menuSave.addActionListener(mfileHandler);
		menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(menuSave);

		JMenuItem menuSaveAs = new JMenuItem("Save As...");
		menuSaveAs.addActionListener(mfileHandler);
		mnFile.add(menuSaveAs);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		JMenuItem menuExport = new JMenuItem("Export...");
		menuExport.addActionListener(mfileHandler);
		mnFile.add(menuExport);

		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);

		JMenuItem menuQuit = new JMenuItem("Quit");
		menuQuit.addActionListener(mfileHandler);
		mnFile.add(menuQuit);
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
		if (file.isFile())
			menuSave.setEnabled(true);
	}

	public void clearPanels()
	{
		taskPanel.clearTaskList();
		resourcePanel.clearInventory();
	}

	public void reloadPanels()
	{
		taskPanel.reloadTaskList();
		resourcePanel.reloadInventory();
	}

	public void refreshTaskPanelTasks(Collection<Task> tasks)
	{
		taskPanel.refreshTasks(tasks);
	}
	
	private class FileHandler extends WindowAdapter implements ActionListener
	{
		//public static String lastSavedLocation;
		private JFrame parent;
		private JFileChooser fileChooser;
		
		public FileHandler(JFrame parent)
		{
			this.parent = parent;
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			switch(e.getActionCommand())
			{
				case "New...":
					if (pauseTasks() && savedChanges())
						actionNew();
					break;
				case "Open...":
					if (pauseTasks() && savedChanges())
						open();
					break;
				case "Save":
					if (file.isFile())
						save();
					else
						saveAs();
					break;
				case "Save As...":
					if (pauseTasks())
					{
						saveAs();
					}
					break;
				case "Export...":
					try {
						export();					
					} catch (WriteException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				case "Quit":						
					if (pauseTasks() && savedChanges())
						System.exit(0);
					break;
			}
		}
		
		/**
		 * Pause all working tasks if necessary. Returns true when all tasks have
		 * been paused. No action should be taken if tasks are not paused. Does not
		 * save anything.
		 */
		public boolean pauseTasks()
		{
			boolean found = false;
			for (Task t : TaskManager.getTasks())
			{
				if (t.getStatus() == Task.Status.WORKING)
				{
					found = true;
					break;
				}
			}

			if (found)
			{
				switch(JOptionPane.showConfirmDialog(null, "Pause all tasks?", "", JOptionPane.YES_NO_OPTION))
				{
					case JOptionPane.YES_OPTION:
						ArrayList<Task> refreshArray = new ArrayList<Task>();
						for (Task t : TaskManager.getTasks())
						{
							if (t.getStatus() == Task.Status.WORKING)
							{
								t.stop();
								t.pause();
								t.setStatus(Task.Status.PAUSED);
								refreshArray.add(t);
								notifyChange();
							}
						}
						refreshTaskPanelTasks(refreshArray);
						return true;
					case JOptionPane.NO_OPTION:
						return false;
					default:
						return false;
				}
			}
			else
				return true;
		}
		
		/**
		 * Saves all changes to current file if necessary.
		 * Returns true when it is successful in saving file or confirming no
		 * save necessary. Returns false if it fails and no action should be taken
		 * outside of this. Does not pause tasks or change them anymore.
		 */
		public boolean savedChanges()
		{
			if (status == FileStatus.UNCHANGED)
				return true;
			else
			{
				if (file.isFile())
					return save();
				else
				{
					switch (JOptionPane.showConfirmDialog(parent, "You have not saved your current project, would you like to save it before continuing?", "Save current project?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE))
					{
						case JOptionPane.YES_OPTION:
							return saveAs();
						case JOptionPane.NO_OPTION:
							return true; // Continue with other actions since user confirmed
						case JOptionPane.CANCEL_OPTION:
							return false;
						default:
							return false;
					}
				}
			}
		}

		public void actionNew()
		{
			//TODO: Ask for password for project
			String passwd = JOptionPane.showInputDialog(parent, "Choose a password for this project", "Password Input", JOptionPane.QUESTION_MESSAGE);

			JOptionPane.showMessageDialog(parent, "Choose a location to save the new project");

			// Set current directory to file if exists, otherwise null
			fileChooser.setCurrentDirectory(file.exists() ? file : null);
			fileChooser.setDialogTitle("Where to save new project?");
			int returnValue = fileChooser.showSaveDialog(parent);
			if (returnValue != JFileChooser.APPROVE_OPTION)
				return;
			if (!Runner.saveNewProjectFolder(fileChooser.getSelectedFile().getAbsolutePath()))
				return;

			file = fileChooser.getSelectedFile();
			// Assuming path does not end with \
			String projectFileStr =
				file.getPath() + "\\" +
				file.getPath().substring(file.getPath().lastIndexOf('\\')+1);
			file = new File(projectFileStr);

			Inventory.clear();
			TaskManager.clear();

			clearPanels();

			setTitle(file.getName() + " - Active Instructions");

			save();

			// Saving password file
			File passFile = new File(file.getParent() + "\\misc.dat");
			if (passFile.exists())
			{
				if (!passFile.isFile())
				{
					System.err.println("Misc file already exists as directory!");
					JOptionPane.showMessageDialog(parent, "There already is a directory here of that name! Misc file was not created.", null, JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else
			{
				try
				{
					passFile.createNewFile();
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
					System.err.println("Failed to create misc file!");
					JOptionPane.showMessageDialog(parent, "IO Error: Failed to create misc file!", null, JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			try
			{
				FileWriter fw = new FileWriter(passFile);
				fw.write(passwd);
				fw.flush();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				System.err.println("Failed to save misc file!");
				JOptionPane.showMessageDialog(parent, "IO Error: Failed to save misc file!", null, JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		public void open()
		{
			// Set current directory to file if exists, otherwise null
			fileChooser.setCurrentDirectory(file.exists() ? file : null);
			fileChooser.setDialogTitle("Which project do you want to open?");
			int returnValue = fileChooser.showOpenDialog(parent);
			if (returnValue != JFileChooser.APPROVE_OPTION)
				return;
			if (!Runner.loadProject(fileChooser.getSelectedFile().getAbsolutePath()))
				return;

			file = fileChooser.getSelectedFile();

			reloadPanels();

			setTitle(file.getName() + " - Active Instructions");
			status = FileStatus.UNCHANGED;
			menuSave.setEnabled(false);
		}

		/**
		 * Saves project to previously saved location. Expects file to be valid.
		 * Return true when successful. Sets status correctly and disables
		 * menuSave as well.
		 */
		public boolean save()
		{
			boolean success = Runner.saveProject(file.getAbsolutePath());
			if (success)
			{
				status = FileStatus.UNCHANGED;
				menuSave.setEnabled(false);
				return true;
			}
			else
			{
				JOptionPane.showMessageDialog(parent, "Automatic saving has failed. You should use Save As to save to a valid file to avoid data loss.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		/**
		 * Saves project to new file. Returns true when successful.
		 */
		public boolean saveAs()
		{
			// Set current directory to file if exists, otherwise null
			fileChooser.setCurrentDirectory(file.exists() ? file : null);
			fileChooser.setDialogTitle("Where would you like to save the project?");
			int returnValue = fileChooser.showSaveDialog(parent);
			if (returnValue != JFileChooser.APPROVE_OPTION)
				return false;

			File oldFile = file;
			file = fileChooser.getSelectedFile();
			if (save())
			{
				setTitle(file.getName() + " - Active Instructions");
				return true;
			}
			else
			{
				file = oldFile;
				return false;
			}
		}

		public boolean export() throws IOException, WriteException
		{
			
			fileChooser = new JFileChooser();
			int returnValue = fileChooser.showSaveDialog(null);
			if (returnValue != JFileChooser.APPROVE_OPTION)
				return false;
			file = fileChooser.getSelectedFile();
			String filename = file.getAbsolutePath();
			if (!filename.substring(filename.length()-4, filename.length()).equals(".xls"))
			{
				filename +=".xls";
			}
			WorkbookSettings ws = new WorkbookSettings();
		    ws.setLocale(new Locale("en", "EN"));
		    WritableWorkbook workbook = Workbook.createWorkbook(new File(filename), ws);		
		    WritableSheet s1 = workbook.createSheet("Inventory", 0);
		    WritableSheet s2 = workbook.createSheet("Task Properties", 1);
		    WritableSheet s3 = workbook.createSheet("Task Dependencies", 2);
		    exportInventory(s1);
		    exportTaskProperties(s2);
		    exportTaskDependencies(s3);
		    workbook.write();
		    workbook.close();
		    return true;
		}
		
		public void exportInventory(WritableSheet s1) throws WriteException, IOException
		{
			Label tools = new Label(0, 0, "Tool");
		    Label numAvailableTools = new Label(1, 0, "Number Available");
		    Label numMaxTools = new Label(2, 0, "Total Amount");
		    Label parts = new Label(4, 0, "Part");
		    Label numAvailableParts = new Label(5, 0, "Number Available");
		    Label numMaxParts = new Label(6, 0, "Total Amount");
		    s1.setColumnView(1, 15);
		    s1.setColumnView(2, 15);
		    s1.setColumnView(5, 15);
		    s1.setColumnView(6, 15);
		    s1.addCell(tools);
		    s1.addCell(parts);
		    s1.addCell(numAvailableTools);
		    s1.addCell(numAvailableParts);
		    s1.addCell(numMaxTools);
		    s1.addCell(numMaxParts);
	     	for (int j = 1; j <= Inventory.getNumTools(); j++)
	    	{
	    		Label l1 = new Label(0,j, Inventory.getTool(j-1).getName());
	    		Number n1 = new Number(1,j, Inventory.getTool(j-1).getAvailable());
	    		Number n2 = new Number(2,j, Inventory.getTool(j-1).getMax());
	    	    s1.addCell(l1);
	    	    s1.addCell(n1);
	    	    s1.addCell(n2);
	    	}
	     	for (int i = 1; i <= Inventory.getNumParts(); i++)
	     	{
	     		Label l1 = new Label(4,i, Inventory.getPart(i-1).getName());
	    		Number n1 = new Number(5,i, Inventory.getPart(i-1).getAvailable());
	    		Number n2 = new Number(6,i, Inventory.getPart(i-1).getMax());
	    	    s1.addCell(l1);
	    	    s1.addCell(n1);
	    	    s1.addCell(n2);
	     	}
		}
		
		public void exportTaskProperties(WritableSheet s2) throws WriteException, IOException
		{
			s2.setColumnView(2, 20);
	     	s2.setColumnView(3, 20);
	     	s2.setColumnView(4, 20);
	     	s2.setColumnView(5, 20);
	     	s2.setColumnView(6, 40);
	     	ArrayList<Label> propertyLabels = new ArrayList<Label>();
	     	propertyLabels.add(new Label(0, 0, "Task"));
	     	propertyLabels.add(new Label(1, 0, "Builder"));
	     	propertyLabels.add(new Label(2, 0, "Foreman"));
	     	propertyLabels.add(new Label(3, 0, "Start Time"));
	     	propertyLabels.add(new Label(4, 0, "End Time"));
	     	propertyLabels.add(new Label(5, 0, "Time Spent"));
	     	propertyLabels.add(new Label(6,0, "Notes"));
	     	for (int k = 1; k <= TaskManager.getTotalNumTasks(); k++)
	     	{
	     		Task t = TaskManager.getTask(k-1);
	     		ArrayList<Label> valueLabels = new ArrayList<Label>();
	     		valueLabels.add(new Label(0, k, t.getName()));
	     		//valueLabels.add(new Label(1, k, t.getBuilder()));
	     		//valueLabels.add(new Label(2, k, t.getForeman()));
	     		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
	     		if (t.getStartDate().isSet(Calendar.MINUTE))
	     			valueLabels.add(new Label(3, k, df.format(t.getStartDate().getTime())));
	     		else
	     			valueLabels.add(new Label(3, k, ""));
	     		if (t.getEndDate().isSet(Calendar.MINUTE))
	     			valueLabels.add(new Label(4, k, df.format(t.getEndDate().getTime())));
	     		else
	     			valueLabels.add(new Label(4, k, ""));
	     		valueLabels.add(new Label(6, k, t.getNotes()));
	     		
	     		String timeStr = "";
	     		if (t.getTimeSpent() != 0)
	     		{	
	     			long hrs = t.getTimeSpent() / 1000 / 60 / 60;
	     			long days = hrs / 24;
	     			hrs %= 24;
	     			timeStr += days + " days, " + hrs + " hours";
	     		}
	     		valueLabels.add(new Label(5, k, timeStr));
	     		
	     		for (int l = 0; l < valueLabels.size(); l++)
	     			s2.addCell(valueLabels.get(l));  		
//	     		s2.addCell(timeSpent);  
	     	}
	     	for (int m = 0; m < propertyLabels.size(); m++)
     			s2.addCell(propertyLabels.get(m)); 
		}
		
		public void exportTaskDependencies(WritableSheet s3) throws WriteException, IOException
		{
			s3.setColumnView(1, 30);
			s3.setColumnView(2, 30);
			s3.setColumnView(3, 30);
			ArrayList<Label> labels1 = new ArrayList<Label>();
			labels1.add(new Label(0, 0, "Task"));
			labels1.add(new Label(1, 0, "Tool Dependencies"));
			labels1.add(new Label(2, 0, "Part Dependencies"));
			labels1.add(new Label(3, 0, "Task Dependencies"));
			for (int i = 0; i < labels1.size(); i++)
				s3.addCell(labels1.get(i));
			for (int j = 1; j <= TaskManager.getTotalNumTasks(); j++)
			{
				Task t = TaskManager.getTask(j-1);
				s3.addCell(new Label(0, j, t.getName()));
				
				StringBuilder toolConstraints = new StringBuilder();
				for (ResourceConstraint rc : t.getTools())
				{
					toolConstraints.append(rc.getAmount()).append(' ').append(rc.getName()).append(", ");
				}
				// remove trailing ", "
				if (toolConstraints.length() != 0)
					toolConstraints.delete(toolConstraints.length() - 2, toolConstraints.length());
				s3.addCell(new Label(1, j, toolConstraints.toString()));
				
				StringBuilder partConstraints = new StringBuilder();
				for (ResourceConstraint rc : t.getParts())
				{
					partConstraints.append(rc.getAmount()).append(' ').append(rc.getName()).append(", ");
				}
				// remove trailing ", "
				if (partConstraints.length() != 0)
					partConstraints.delete(partConstraints.length() - 2, partConstraints.length());
				s3.addCell(new Label(2, j, partConstraints.toString()));	
				
				StringBuilder taskConstraints = new StringBuilder();
				for (Task task : t.getDependencies())
				{
					taskConstraints.append(task.getName()).append(", ");
				}
				// remove trailing ", "
				if (taskConstraints.length() != 0)
					taskConstraints.delete(taskConstraints.length() - 2, taskConstraints.length());
				s3.addCell(new Label(3, j, taskConstraints.toString()));	
			}
			
		}
		
		@Override
		public void windowClosing(WindowEvent e)
		{
			if (pauseTasks() && savedChanges())
				System.exit(0);
		}
	}
}

