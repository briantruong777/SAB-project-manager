package taskModel;
import java.util.ArrayList;
import java.util.HashMap;

public class Task 
{
	private String name;
	private Status taskStatus;
	private HashMap<String, Widget> tools;
	private HashMap<String, Widget> parts;
	private ArrayList<Task> dependencies;
	
	public enum Status
	{
		ILLEGAL, UNAVAILABLE, INCOMPLETE, WORKING, PAUSED, COMPLETE;
	}
	
	public Task()
	{
		
	}
	
	public Task(String s)
	{
		this.name = s;
		taskStatus = Status.INCOMPLETE;		
		tools = new HashMap<String, Widget>();
		parts = new HashMap<String, Widget>();
		dependencies = new ArrayList<Task>();
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Status getStatus()
	{
		return this.taskStatus;
	}
	
	public void setStatus(Status s)
	{
		this.taskStatus = s;
	}
	
	public void addTool(String name, int available, int max)
	{
		this.tools.put(name, new Widget(available, max));
	}
	
	public void addPart(String name, int available, int max)
	{
		this.parts.put(name, new Widget(available, max));
	}
	
	public void removeTool(String name)
	{
		this.tools.remove(name);
	}
	
	public void removePart(String name)
	{
		this.parts.remove(name);
	}
	
	public void addDependency(Task t)
	{
		this.dependencies.add(t);
	}
	
	public boolean checkresources()
	{
		//tools in Task HashMap tools available in tools in Inventory HashMap tools
		//parts in Task HashMap parts available in tools in Inventory HashMap parts
		
		boolean available = false;
		return available;
	}
	
	public boolean checkDependencies()
	{
		//
		boolean available = false;
		return available;
	}
}
