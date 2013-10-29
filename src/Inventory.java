import java.util.HashMap;
import java.util.Scanner;

import taskModel.Widget;

public class Inventory
{
	private static HashMap<String, Widget> tools = new HashMap<String, Widget>();
	private static HashMap<String, Widget> parts = new HashMap<String, Widget>();
	
	public Inventory()
	{
		fillMap(tools, "tools.txt");
		//fillMap(parts, "parts.txt");
	}
	
	public static void fillMap(HashMap map, String filename)
	{
		Scanner filereader = new Scanner("tools.txt");
		/*while (filereader.hasNext())
		{
			String name = filereader.nextLine();
			int available = filereader.nextInt();
			int max = filereader.nextInt();
			map.put(name, new Widget(available, max));
			//map.put((String)filereader.nextLine(), new Widget(filereader.nextInt(), filereader.nextInt()));
		}*/
		
		tools.put("hammer", new Widget(1, 2));
		tools.put("screwdriver", new Widget(5, 10));
		tools.put("saw", new Widget(3, 7));
	}	
	
	public static void printMap()
	{
		/*for (String key : tools.keySet())
		{
			System.out.println(key + " has " + tools.get(key).available + " available out of " + tools.get(key).max + " total"); 
		}*/
	}
	
	public static boolean checkResources(HashMap<String, Widget> taskTools, HashMap<String, Widget> taskParts)
	{
		boolean available = true;
		for (String key : taskTools.keySet())
		{
			if (taskTools.get(key).num > tools.get(key).num)
				available = false;
		}
		for (String key : taskParts.keySet())
		{
			if (taskParts.get(key).num > parts.get(key).num)
				available = false;
		}
		return available;	
	}
	
	public static boolean checkTools(HashMap<String, Widget> taskTools)
	{
		boolean available = true;
		for (String key : taskTools.keySet())
		{
			if (taskTools.get(key).num > tools.get(key).num)
				available = false;
		}
		return available;	
	}
	
	public static boolean checkParts(HashMap<String, Widget> taskParts)
	{
		boolean available = true;
		for (String key : taskParts.keySet())
		{
			if (taskParts.get(key).num > parts.get(key).num)
				available = false;
		}
		return available;	
	}
	
	
}
