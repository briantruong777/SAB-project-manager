package resourceModel;

import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import resourceModel.Widget;

public class Inventory
{
	private static HashMap<String, Widget> tools = new HashMap<String, Widget>();
	private static HashMap<String, Widget> parts = new HashMap<String, Widget>();
	private HashMap<String, Resource> toolMap;
	private HashMap<String, Resource> partMap;

	public static void fillMap(HashMap map, String filename)
	{
		//    Scanner filereader = new Scanner("tools.txt");
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

		parts.put("blah", new Widget(2,2));
	}  

	public static void printMap()
	{
		for (String key : tools.keySet())
		{
			System.out.println(key + " has " + tools.get(key).num + " available out of " + tools.get(key).max + " total"); 
		}
	}

	public static boolean checkTools(HashMap<String, Integer> taskTools)
	{
		boolean available = true;
		for (String key : taskTools.keySet())
		{
			if (!tools.containsKey(key) || taskTools.get(key) > tools.get(key).num)
				available = false;
		}
		return available;  
	}

	public static boolean checkParts(HashMap<String, Integer> taskParts)
	{
		boolean available = true;
		for (String key : taskParts.keySet())
		{
			if (!parts.containsKey(key) || taskParts.get(key) > parts.get(key).num)
				available = false;
		}
		return available;  
	}

	public static boolean checkResources(HashMap<String, Integer> taskTools,
			HashMap<String, Integer> taskParts)
	{
		return checkTools(taskTools) && checkParts(taskParts);  
	}

	@SuppressWarnings("unchecked")
	public static void readToolsFromFile(String fileName) throws IOException,
	ClassNotFoundException
	{
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(fis);
		tools = (HashMap<String, Widget>) in.readObject();
		in.close();
	}
	public static void writeToolsToFile(String fileName) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(tools);
		out.close();
	}

	@SuppressWarnings("unchecked")
	public static void readPartsFromFile(String fileName) throws IOException,
	ClassNotFoundException
	{
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(fis);
		parts = (HashMap<String, Widget>) in.readObject();
		in.close();
	}
	public static void writePartsToFile(String fileName) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(parts);
		out.close();
	}
	
	public Inventory()
	{
		toolMap = new HashMap<String, Resource>();
		partMap = new HashMap<String, Resource>();
	}
	
	public void addTool(Resource tool)
	{
		toolMap.put(tool.toString(), tool);
	}

	public void addPart(Resource part)
	{
		partMap.put(part.toString(), part);
	}
	
	public void removeTool(Resource tool)
	{
		toolMap.remove(tool.toString());
	}
	
	public void removePart(Resource part)
	{
		partMap.remove(part.toString());
	}
}