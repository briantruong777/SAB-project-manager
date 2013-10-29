/*
 * TODO:
 * -Start, resume things
 * -Store link to folder
 */

package taskModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

public class Task 
{
	private String name;
	private Status taskStatus;
	private HashMap<String, Integer> tools;
	private HashMap<String, Integer> parts;
	private ArrayList<Task> dependencies;
  private Calendar startDate;
  private Calendar endDate;
	
	public enum Status
	{
		ILLEGAL, UNAVAILABLE, INCOMPLETE, WORKING, PAUSED, COMPLETE;
	}
	
	public Task(String s)
	{
		this.name = s;
		taskStatus = Status.INCOMPLETE;		
		tools = new HashMap<String, Integer>();
		parts = new HashMap<String, Integer>();
		dependencies = new ArrayList<Task>();
    startDate = Calendar.getInstance();
    startDate.clear(); // Instantiate these variables, but invalidate value
    endDate = Calendar.getInstance();
    endDate.clear();
	}
	
	public String getName()
	{
		return name;
	}
  public void setName(String str)
  {
    name = str;
  }
	
	public Status getStatus()
	{
		return this.taskStatus;
	}
	public void setStatus(Status s)
	{
		this.taskStatus = s;
	}
	
  public HashMap<String, Integer> getTools()
  {
    return tools;
  }
	public void addTool(String name, int numNeeded)
	{
		this.tools.put(name, new Integer(numNeeded));
	}
	public void removeTool(String name)
	{
		this.tools.remove(name);
	}
	
  public HashMap<String, Integer> getParts()
  {
    return parts;
  }
	public void addPart(String name, int numNeeded)
	{
		this.parts.put(name, new Integer(numNeeded));
	}
	public void removePart(String name)
	{
		this.parts.remove(name);
	}
	
  public ArrayList<Task> getDependencies()
  {
    return dependencies;
  }
	public void addDependency(Task t)
	{
		this.dependencies.add(t);
	}
  public void removeDependency(Task t)
  {
    this.dependencies.remove(t);
  }

  public void start()
  {
    startDate = Calendar.getInstance();
  }
  public void stop()
  {
    endDate = Calendar.getInstance()
  }
  public Calendar getStartDate()
  {
    return startDate;
  }
  public Calendar getEndDate()
  {
    return endDate;
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
