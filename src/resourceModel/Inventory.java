package resourceModel;

import guiElements.Runner;

import java.util.ArrayList;
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
	
	public static void addTool(Resource tool)
	{
		tools.put(tool.toString(), tool);
	}

	public static void addPart(Resource part)
	{
		parts.put(part.toString(), part);
	}
	
	public static int getNumTools()
	{
		return tools.size();
	}
	
	public static int getNumParts()
	{
		return parts.size();
	}
	
	public static Resource getTool(String name)
	{
		return tools.get(name);
	}
	
	public static Resource getTool(int i)
	{
		ArrayList<Resource> temp = new ArrayList<Resource>(tools.values());
		return temp.get(i);
	}
	
	public static Resource getPart(String name)
	{
		return parts.get(name);
	}
	
	public static Resource getPart(int i)
	{
		ArrayList<Resource> temp = new ArrayList<Resource>(parts.values());
		return temp.get(i);
	}
	
	public static void removeTool(String name)
	{
		tools.remove(name);
	}
	
	public static void removePart(String name)
	{
		parts.remove(name);
	}

	public static Collection<Resource> getTools()
	{
		return tools.values();
	}

	public static Collection<Resource> getParts()
	{
		return parts.values();
	}

	public static HashMap<String, Resource> getToolsHash()
	{
		return tools;
	}
	public static HashMap<String, Resource> getPartsHash()
	{
		return parts;
	}
	
	public static void addTools(Collection<Resource> addedTools)
	{
		for (Resource t: addedTools)
			tools.put(t.getName(), t);
	}
	public static void addParts(Collection<Resource> addedParts)
	{
		for (Resource p: addedParts)
			parts.put(p.getName(), p);
	}
	
	public static void clear()
	{
		tools.clear();
		parts.clear();
	}
	
/*	public static void printMap()
	{
		for (String key : tools.keySet())
		{
			System.out.println(key + " has " + tools.get(key).getAvailable() + " available out of " + tools.get(key).getMax() + " total"); 
		}
	}*/

	public static boolean checkResources(Collection<ResourceConstraint> taskTools,
			Collection<ResourceConstraint> taskParts)
	{
		for (ResourceConstraint tool : taskTools)
		{
			if (!tools.containsKey(tool.getName()) || tools.get(tool.getName()).isBroken() || tool.getAmount() > tools.get(tool.getName()).getAvailable())
				return false;
		}
		for (ResourceConstraint part : taskParts)
		{
			if (!parts.containsKey(part.getName()) || parts.get(part.getName()).isBroken() || part.getAmount() > parts.get(part.getName()).getAvailable())
				return false;
		}
		return true;
	}

	public static void takeResources(Collection<ResourceConstraint> taskTools,
			Collection<ResourceConstraint> taskParts)
	{
		Resource r;
		for (ResourceConstraint t : taskTools)
		{
			r = tools.get(t.getName());
			r.setAvailable(r.getAvailable() - t.getAmount());
			Runner.refreshTaskPanelTasks(r.getDependers());
		}
		for (ResourceConstraint t : taskParts)
		{
			r = parts.get(t.getName());
			r.setAvailable(r.getAvailable() - t.getAmount());
			Runner.refreshTaskPanelTasks(r.getDependers());
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
			Runner.refreshTaskPanelTasks(r.getDependers());
		}
		for (ResourceConstraint t : taskParts)
		{
			r = parts.get(t.getName());
			r.setAvailable(r.getAvailable() + t.getAmount());
			Runner.refreshTaskPanelTasks(r.getDependers());
		}
	}
}
