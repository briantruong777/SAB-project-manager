package runner;
import guiElements.ActiveInstructionsFrame;

import java.io.IOException;

import resourceModel.Inventory;
import taskModel.TaskManager;

public class Runner 
{
	//static TaskManager tm;
	
	public static void main(String[] args)
	{
		//tm = new TaskManager();	
		TaskManager.createNewTask("Blah1");
		TaskManager.createNewTask("Blah2");
		TaskManager.createNewTask("Blah3");
		TaskManager.createNewTask("Blah4");
		ActiveInstructionsFrame aif = new ActiveInstructionsFrame();
		aif.setVisible(true);
	}
	
	public static void saveTasks(String path)
	{
		try
		{
			//if (tm == null)
				//System.out.println("tm is null");
			TaskManager.writeToFile(path);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void loadTasks(String path)
	{
		// Loading tasks from file	
		try
		{
			TaskManager.readFromFile(path);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void savetools(String toolPath, String partPath)
	{
		// Saving tools and parts to files
		try
		{
			Inventory.writeToolsToFile(toolPath);
			Inventory.writePartsToFile(partPath);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void loadTools(String toolPath, String partPath)
	{
		// Loading tools and parts from files
		try
		{
			Inventory.readToolsFromFile(toolPath);
			Inventory.readPartsFromFile(partPath);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
	}
}
