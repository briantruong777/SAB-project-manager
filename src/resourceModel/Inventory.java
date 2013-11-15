package resourceModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;

public class Inventory
{
	private static HashMap<String, Resource> tools;
	private static HashMap<String, Resource> parts;

	static
	{
		tools = new HashMap<String, Resource>();
		parts = new HashMap<String, Resource>();
	}
	
	public static void fillMap(HashMap map, String filename)
	{
		//		Scanner filereader = new Scanner("tools.txt");
		/*while (filereader.hasNext())
		{
			String name = filereader.nextLine();
			int available = filereader.nextInt();
			int max = filereader.nextInt();
			map.put(name, new Widget(available, max));
			//map.put((String)filereader.nextLine(), new Widget(filereader.nextInt(), filereader.nextInt()));
		}*/
	}	

	public static HashMap<String, Resource> getToolsHash()
	{
		return tools;
	}
	public static HashMap<String, Resource> getPartsHash()
	{
		return parts;
	}
	public static void setTools(HashMap<String, Resource> newTools)
	{
		tools = newTools;
	}
	public static void setParts(HashMap<String, Resource> newParts)
	{
		parts = newParts;
	}
	
	public static void clear()
	{
		tools.clear();
		parts.clear();
	}
	
	public static void printMap()
	{
		for (String key : tools.keySet())
		{
			System.out.println(key + " has " + tools.get(key).getAvailable() + " available out of " + tools.get(key).getMax() + " total"); 
		}
	}

	public static boolean checkTools(HashMap<String, Integer> taskTools)
	{
		boolean available = true;
		for (String key : taskTools.keySet())
		{
			if (!tools.containsKey(key) || taskTools.get(key) > tools.get(key).getAvailable())
				available = false;
		}
		return available;
	}

	public static boolean checkParts(HashMap<String, Integer> taskParts)
	{
		boolean available = true;
		for (String key : taskParts.keySet())
		{
			if (!parts.containsKey(key) || taskParts.get(key) > parts.get(key).getAvailable())
				available = false;
		}
		return available;
	}

	public static boolean checkResources(HashMap<String, Integer> taskTools,
			HashMap<String, Integer> taskParts)
	{
		return checkTools(taskTools) && checkParts(taskParts);
	}

	public static void addTool(Resource tool)
	{
		tools.put(tool.toString(), tool);
	}

	public static void addPart(Resource part)
	{
		parts.put(part.toString(), part);
	}
	
	public static Resource getTool(String name)
	{
		return tools.get(name);
	}
	
	public static Resource getPart(String name)
	{
		return parts.get(name);
	}
	
	public static Collection<Resource> getTools()
	{
		return tools.values();
	}

	public static Collection<Resource> getParts()
	{
		return parts.values();
	}

	public static void removeTool(Resource tool)
	{
		tools.remove(tool.toString());
	}
	
	public static void removePart(Resource part)
	{
		parts.remove(part.toString());
	}
}
