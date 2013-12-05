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
/*		TaskManager.createNewTask("Blah1");
		TaskManager.createNewTask("Blah2");
		TaskManager.createNewTask("Blah3");
		TaskManager.createNewTask("Blah4");
		Inventory.addTool(new Resource("Hammer", 1));
		Inventory.addTool(new Resource("Axe", 1));
		Inventory.addTool(new Resource("Sword", 1));
		Inventory.addPart(new Resource("Wing", 1));
		Inventory.addPart(new Resource("Shield", 1));*/
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			
		}
		frame = new ActiveInstructionsFrame();
		frame.setVisible(true);
//		frame.reloadTaskPanel();
	}

	/**
	 * Makes and saves a new project folder. This will make a new folder with
	 * the path given. Inside this folder, this method will make a Backup folder.
	 * Afterwards, it should just call saveProject().
	 */
	public static void saveNewProject(String path)
	{
		path += "\\";
		// path is now the folder containing the Backup folder and project file
		//TODO: Finish implementing this
	}

	/**
	 * This saves the project to the file given by path. This will also
	 * look for a folder called Backup in the same directory as the file given
	 * by path. If it doesn't find the folder, it does not save a backup.
	 */
	public static void saveProject(String path)
	{
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
		}

		// Now looking for Backup folder

		// Assuming path does not end with \
		String projectNameStr = path.substring(path.lastIndexOf('\\')+1);
		// projectDirectory will not include \
		String projectDirectoryStr = path.substring(0, path.lastIndexOf('\\'));

		// backupDirectory.toString() will not include \
		File backupDirectory = new File(
			projectDirectoryStr + "\\" + projectNameStr + "-Backup");

		if (backupDirectory.exists())
		{
			//TODO: Label backups by date or something
			System.out.println("BackupDirectory does exist\n");
			try
			{
				fos = new FileOutputStream(
					backupDirectory.toString() + "\\" + projectNameStr);
				out = new ObjectOutputStream(fos);
				out.writeObject(saveList);
				out.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("BackupDirectory does not exist");
		}
	}
	@SuppressWarnings("unchecked")
	public static void loadProject(String path)
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
			JOptionPane.showMessageDialog(null, "File could not be loaded.", null, JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return;
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			return;
		}

		tasks = (HashMap<String, Task>) saveList.get(0);
		tools = (HashMap<String, Resource>) saveList.get(1);
		parts = (HashMap<String, Resource>) saveList.get(2);
		TaskManager.setTasksMap(tasks);
		Inventory.clear();
		Inventory.addTools(tools.values());
		Inventory.addParts(parts.values());

		System.out.println(TaskManager.getTasksMap());
		System.out.println(Inventory.getTools());
		System.out.println(Inventory.getParts()+"\n");

		frame.reloadTaskPanel();
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
