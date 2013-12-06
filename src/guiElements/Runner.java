package guiElements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import resourceModel.Inventory;
import resourceModel.Resource;
import taskModel.Task;
import taskModel.TaskManager;

public class Runner 
{
	private static ActiveInstructionsFrame frame;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		frame = new ActiveInstructionsFrame();
		frame.setVisible(true);
	}

	/**
	 * Makes and saves a new project folder, but does not save project file.
	 * This will make a new folder with the path given (meaning path should not
	 * exist yet). Inside this folder, this method will make a backup folder.
	 * Returns true if successfully make project directory regardless of success
	 * of making backup folder.
	 */
	public static boolean saveNewProjectFolder(String path)
	{
		// Assuming path does not end with \
		String projectNameStr = path.substring(path.lastIndexOf('\\')+1);

		File projectDirectory = new File(path);
		if (projectDirectory.exists())
		{
			if (!projectDirectory.isDirectory())
			{
				System.out.println("Project folder already exists as file!");
				JOptionPane.showMessageDialog(frame, "There already is a file here of that name! Project was not created.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else
		{
			if (!projectDirectory.mkdir())
			{
				System.out.println("Failed to make project directory!");
				JOptionPane.showMessageDialog(frame, "Failed to make project directory! Project was not created.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// Check if Backup folder exists already
		File backupDirectory = new File(path + "\\" + projectNameStr + "-Backup");
		if (backupDirectory.exists())
		{
			if (!backupDirectory.isDirectory())
			{
				System.out.println("Backup folder already exists as file.");
				// Not returning false since backup is only secondary
			}
		}
		else
		{
			backupDirectory.mkdir();
			// Not returning false if failure since backup folder is only secondary
		}

		return true;
	}

	/**
	 * This saves the project to the file given by path. Returns true if
	 * main save is successful, false if failed. Will still return true if
	 * backup save fails. This will also look for a folder called
	 * projectname-Backup in the same directory as the file given by path. If
	 * it doesn't find the folder, it does not save a backup.
	 */
	public static boolean saveProject(String path)
	{
		// Saving project file
		ArrayList<Serializable> saveList = new ArrayList<Serializable>();
		saveList.add(TaskManager.getTasksMap());
		saveList.add(Inventory.getToolsHash());
		saveList.add(Inventory.getPartsHash());

		FileOutputStream fos;
		ObjectOutputStream out;
		try
		{
			fos = new FileOutputStream(path);
			out = new ObjectOutputStream(fos);
			out.writeObject(saveList);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println("Failed to save project file!");
			JOptionPane.showMessageDialog(frame, "IO Error: Failed to save project file!", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Now looking for Backup folder

		// Assuming path does not end with \
		String projectNameStr = path.substring(path.lastIndexOf('\\')+1);
		// projectDirectoryStr will not include \
		String projectDirectoryStr = path.substring(0, path.lastIndexOf('\\'));

		// backupDirectory.getPath() will not include \
		File backupDirectory = new File(
			projectDirectoryStr + "\\" + projectNameStr + "-Backup");

		if (backupDirectory.isDirectory())
		{
			//TODO: Label backups by date or something
			try
			{
				fos = new FileOutputStream(
					backupDirectory.getPath() + "\\" + projectNameStr);
				out = new ObjectOutputStream(fos);
				out.writeObject(saveList);
				out.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.out.println("Failed to save to backup file.");
				// Backup is only secondary so don't return false
			}
		}
		else
		{
			System.out.println("Backup directory does not exist or is a file.");
			// Backup is only secondary so don't return false
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean loadProject(String path)
	{
		ArrayList<Serializable> saveList;
		HashMap<String, Task> tasks;
		HashMap<String, Resource> tools;
		HashMap<String, Resource> parts;

		try
		{
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fis);
			saveList = (ArrayList<Serializable>) in.readObject();
			in.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "IO Error: File could not be loaded.", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "File Format Error: File is formatted incorrectly.", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try
		{
			tasks = (HashMap<String, Task>) saveList.get(0);
			tools = (HashMap<String, Resource>) saveList.get(1);
			parts = (HashMap<String, Resource>) saveList.get(2);
		}
		catch (ClassCastException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "File Format Error: File is formatted incorrectly.", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		TaskManager.setTasksMap(tasks);
		Inventory.clear();
		Inventory.addTools(tools.values());
		Inventory.addParts(parts.values());
		 return true;
	}
	
	public static void notifyChange()
	{
		frame.notifyChange();
	}

	public static void refreshTaskPanelTasks(Collection<Task> tasks)
	{
		frame.refreshTaskPanelTasks(tasks);
	}
}
