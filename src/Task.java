import java.util.*;

public class Task 
{
	private String name;
	private Status taskStatus;
	private HashMap<String, Widget> tools = new HashMap<String, Widget>();
	private HashMap<String, Widget> parts = new HashMap<String, Widget>();
	
	public enum Status
	{
		INCOMPLETE, WORKING, COMPLETE;
	}
	
	public Task(String s)
	{
		this.name = s;
		taskStatus = Status.INCOMPLETE;		
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
		tools.put(name, new Widget(available, max));
	}
	
	public void addPart(String name, int available, int max)
	{
		parts.put(name, new Widget(available, max));
	}
	
	
}
