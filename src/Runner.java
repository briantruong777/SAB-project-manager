import taskModel.TaskManager;
import resourceModel.Inventory;
import java.io.IOException;

public class Runner 
{
	public static void main(String[] args)
	{
		TaskManager tm = new TaskManager();

		// Saving all tasks to file
//		try
//		{
//			tm.writeToFile("test_tasks.dat");
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}

		// Loading tasks from file
//		try
//		{
//			tm.readFromFile("test_tasks.dat");
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		catch(ClassNotFoundException ex)
//		{
//			ex.printStackTrace();
//		}

    // Saving tools and parts to files
//    try
//    {
//      Inventory.writeToolsToFile("tools_test.dat");
//      Inventory.writePartsToFile("parts_test.dat");
//    }
//    catch(IOException ex)
//    {
//      ex.printStackTrace();
//    }

    // Loading tools and parts from files
//    try
//    {
//      Inventory.readToolsFromFile("tools_test.dat");
//      Inventory.readPartsFromFile("parts_test.dat");
//    }
//    catch(IOException ex)
//    {
//      ex.printStackTrace();
//    }
//    catch(ClassNotFoundException ex)
//    {
//      ex.printStackTrace();
//    }
	}
}
