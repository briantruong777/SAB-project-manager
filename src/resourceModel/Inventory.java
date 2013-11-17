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

	public static boolean checkTools(Collection<ResourceConstraint> taskTools)
	{
		for (ResourceConstraint tool : taskTools)
		{
			if (!tools.containsKey(tool.getName()) || tool.getAmount() > tools.get(tool.getName()).getAvailable())
				return false;
		}
		return true;
	}

	public static boolean checkParts(Collection<ResourceConstraint> taskParts)
	{
		for (ResourceConstraint part : taskParts)
		{
			if (!parts.containsKey(part.getName()) || part.getAmount() > tools.get(part.getName()).getAvailable())
				return false;
		}
		return true;
	}

	public static boolean checkResources(Collection<ResourceConstraint> taskTools,
			Collection<ResourceConstraint> taskParts)
	{
		return checkTools(taskTools) && checkParts(taskParts);
	}

	public static void takeResources(Collection<ResourceConstraint> taskTools,
			Collection<ResourceConstraint> taskParts)
	{
		Resource r;
		for (ResourceConstraint t : taskTools)
		{
			r = tools.get(t.getName());
			r.setAvailable(r.getAvailable() - t.getAmount());
		}
		for (ResourceConstraint t : taskParts)
		{
			r = parts.get(t.getName());
			r.setAvailable(r.getAvailable() - t.getAmount());
		}
	}

	public static void releaseResources(Collection<ResourceConstraint> taskTools,
			Collection<ResourceConstraint> taskParts)
	{
		Resource r;
		for (ResourceConstraint t : taskTools)
		{
			r = tools.get(t.getName());
			r.setAvailable(r.getAvailable() + t.getAmount());
		}
		for (ResourceConstraint t : taskParts)
		{
			r = parts.get(t.getName());
			r.setAvailable(r.getAvailable() + t.getAmount());
		}
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
