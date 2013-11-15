package taskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class TaskManager
{
	private static HashMap<String, Task> tasks;

	static
	{
		tasks = new HashMap<String, Task>();
	}

	public static HashMap<String, Task> getTasksMap()
	{
		return tasks;
	}
	public static void setTasksMap(HashMap<String, Task> newTasks)
	{
		tasks = newTasks;
	}

	public static Task getTask(String taskName)
	{
		return tasks.get(taskName);
	}

	public static ArrayList<Task> getAllTasks()
	{ 
		//TODO: Implement advanced sorting here or in gui
		ArrayList<Task> values = new ArrayList<Task>(tasks.values());
		Collections.sort(values);
		return values;
	}
	
	public static void clear()
	{
		tasks.clear();
	}

	public static int getTotalNumTasks()
	{
		return tasks.size();
	}

	/**
	 * Creates a new task while also creating a new task for every named
	 * dependency and adding it as a dependecy to the original task.
	 *
	 * If current task (taskName) exists already, it will add the deps as
	 * dependencies to the task. In other words, call this with every Task you
	 * need to create and even if it was created already, you will still be able
	 * to add dependencies correctly. Returns the task it creates.
	 */
	public static Task createNewTask(String taskName, String[] deps)
	{
		Task t = createNewTask(taskName);

		Task d;
		for (String str : deps)
		{
			if (!tasks.containsKey(str))
			{
				d = new Task(str);
				tasks.put(str, d);
			}
			else
			{
				d = tasks.get(str);
			}
			t.addDependency(d);
		}

		return t;
	}
	/**
	 * Creates new task that doesn't have any dependencies
	 */
	public static Task createNewTask(String taskName)
	{
		Task t;
		if (!tasks.containsKey(taskName))
		{
			t = new Task(taskName);
			tasks.put(taskName, t);
		}
		else
		{
			t = tasks.get(taskName);
		}

		return t;
	}
}
