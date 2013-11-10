import taskModel.TaskManager;

public class Runner 
{
	public static void main(String[] args)
	{
		GUIFrame g = new GUIFrame();
		//Inventory box = new Inventory();
		//box.printMap();

		TaskManager tm = new TaskManager();

		// Saving all tasks to file
//		try
//		{
//			tm.writeToFile("test_tasks.txt");
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}

		// Loading tasks from file
//		try
//		{
//			tm.readFromFile("test_tasks.txt");
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		catch(ClassNotFoundException ex)
//		{
//			ex.printStackTrace();
//		}

	}
}
