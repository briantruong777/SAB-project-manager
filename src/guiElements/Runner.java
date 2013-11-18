package guiElements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import javax.swing.JOptionPane;

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

		frame = new ActiveInstructionsFrame();
		frame.setVisible(true);
//		frame.reloadTaskPanel();
	}

	public static void saveProject(String path)
	{
		ArrayList<Serializable> saveList = new ArrayList<Serializable>();
		saveList.add(TaskManager.getTasksMap());
		saveList.add(Inventory.getToolsHash());
		saveList.add(Inventory.getPartsHash());

		try
		{
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(saveList);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
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
		System.out.println(Inventory.getParts());

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
