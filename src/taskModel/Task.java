/*
 * TODO:
 * -Start, resume things
 * -Store link to folder
 */

package taskModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import resourceModel.Inventory;
import resourceModel.ResourceConstraint;

public class Task implements Serializable, Comparable<Task>
{
	private String name;
	private String builder;
	private String foreman;
	private Status taskStatus;
	private HashMap<String, ResourceConstraint> tools;
	private HashMap<String, ResourceConstraint> parts;
	private ArrayList<Task> dependencies;
	private ArrayList<Task> dependers;
	private Calendar startDate;
	private Calendar endDate;
	private long timeSpent;
	private String path;
	private String notes;

	private long lastResumeTime;
	
	public enum Status
	{
		UNAVAILABLE, STOPPED, UNSTARTED, WORKING, PAUSED, COMPLETE;
	}
	
	public Task(String s)
	{
		this.name = s;
		builder = "No Builder";
		foreman = "No Foreman";
		taskStatus = Status.UNSTARTED;
		tools = new HashMap<String, ResourceConstraint>();
		parts = new HashMap<String, ResourceConstraint>();
		dependencies = new ArrayList<Task>();
		dependers new ArrayList<Task>();
		startDate = Calendar.getInstance();
		startDate.clear(); // Invalidates value
		endDate = Calendar.getInstance();
		endDate.clear();
		timeSpent = 0; // In milliseconds
		path= "";
		notes = "\n\n\n\n";

		lastResumeTime = -1; // -1 when paused
	}
	
	
	public String getName()
	{
		return name;
	}
	public void setName(String str)
	{
		name = str;
	}
	public String getBuilder()
	{
		return builder;
	}
	public void setBuilder(String str)
	{
		builder = str;
	}
	public String getForeman()
	{
		return foreman;
	}
	public void setForeman(String str)
	{
		foreman = str;
	}
	public String getPath()
	{
		return path;
	}
	public void setPath(String str)
	{
		path= str;
	}
	
	public Status getStatus()
	{
		return this.taskStatus;
	}
	public void setStatus(Status s)
	{
		this.taskStatus = s;
	}
	
	public Collection<ResourceConstraint> getTools()
	{
		return tools.values();
	}
	public void addTool(ResourceConstraint tool)
	{
		Inventory.getTool(tool.getName()).addConstraint(tool);
		tools.put(tool.getName(), tool);
		
	}
	public void removeTool(String name)
	{
		Inventory.getTool(name).removeConstraint(tools.remove(name));
	}
	
	public Collection<ResourceConstraint> getParts()
	{
		return parts.values();
	}
	public void addPart(ResourceConstraint part)
	{
		Inventory.getPart(part.getName()).addConstraint(part);
		parts.put(part.getName(), part);
	}
	public void removePart(String name)
	{
		Inventory.getPart(name).removeConstraint(parts.remove(name));
	}
	
	public ArrayList<Task> getDependencies()
	{
		return dependencies;
	}
	public void addDependency(Task t)
	{
		dependencies.add(t);
		t.addDepender(this);
	}
	public void removeDependency(Task t)
	{
		dependencies.remove(t);
		t.removeDepender(t);
	}
	
	public ArrayList<Task> getDependers()
	{
		return dependers;
	}

	public void clearDependencies()
	{
		for (Task t: dependencies)
			t.removeDepender(this);
		dependencies.clear();
	}

	public void addDependencies(Collection<Task> tasks)
	{
		for (Task t: tasks)
			t.addDepender(this);
		dependencies.addAll(tasks);
	}

	private void addDepender(Task t)
	{
		dependers.add(t);
	}
	
	public void removeDepender(Task t)
	{
		dependers.remove(t);
	}


	/**
	 * Run only once when Task is begun. Sets the startDate.
	 */
	public void begin()
	{
		startDate = Calendar.getInstance();
	}
	/**
	 * Run when task is finished. Sets the endDate.
	 */
	public void finish()
	{
		endDate = Calendar.getInstance();
	}

	//NOTE:We could also check resources from an outside class since that
	//is more likely to avoid bugs
	public void start()
	{
		//TODO:Remove required resources from Inventory
	}
	public void stop()
	{
		//TODO:Add back required resources to Inventory
	}

	public Calendar getStartDate()
	{
		return startDate;
	}
	public Calendar getEndDate()
	{
		return endDate;
	}

	public long getTimeSpent()
	{
		return timeSpent;
	}
	public void setTimeSpent(long timeSpent)
	{
		this.timeSpent = timeSpent;
	}

	/**
	 * Resumes counting working time. Can be called multiple times safely.
	 */
	public void resume()
	{
		if (lastResumeTime == -1) // Check if paused
		{
			lastResumeTime = Calendar.getInstance().getTimeInMillis();
		}
	}
	/**
	 * Pauses counting working time. Can be called multiple times safely.
	 */
	public void pause()
	{
		if (lastResumeTime != -1) // Check for valid last resume time
		{
			long curTime = Calendar.getInstance().getTimeInMillis();
			timeSpent += curTime - lastResumeTime;
			lastResumeTime = -1;
		}
	}
	
	public boolean meetResources()
	{
		//tools in Task HashMap tools available in tools in Inventory HashMap tools
		//parts in Task HashMap parts available in tools in Inventory HashMap parts
		return Inventory.checkResources(tools.values(), parts.values());
	}
	
	public boolean meetDependencies()
	{
		for (Task t : dependencies)
		{
			if (t.getStatus() != Status.COMPLETE)
				return false;
		}
		return true;
	}

	public void refreshStatus()
	{
		if (taskStatus == Status.WORKING ||
				taskStatus == Status.PAUSED ||
				taskStatus == Status.COMPLETE)
		{
			return;
		}

		if (meetDependencies() && meetResources())
		{
			if (startDate.isSet())
			{
				taskStatus = Status.STOPPED;
			}
			else
			{
				taskStatus = Status.UNSTARTED;
			}
		}
		else
		{
			taskStatus = Status.UNAVAILABLE;
		}
	}

	public String toString()
	{
		return name;
	}

	public int compareTo(Task o)
	{
		return name.compareTo(o.name);
	}
	
	public void clearTools()
	{
		for (ResourceConstraint toolRC: tools.values())
			Inventory.getTool(toolRC.getName()).removeConstraint(toolRC);
		tools.clear();
	}
	
	public void clearParts()
	{
		for (ResourceConstraint partRC: parts.values())
			Inventory.getPart(partRC.getName()).removeConstraint(partRC);
		parts.clear();
	}
	
	public void addTools(Collection<ResourceConstraint> toolRCs)
	{
		for (ResourceConstraint toolRC: toolRCs)
		{
			Inventory.getTool(toolRC.getName()).addConstraint(toolRC);
			tools.put(toolRC.getName(), toolRC);
		}
	}

	public void addParts(Collection<ResourceConstraint> partRCs)
	{
		for (ResourceConstraint partRC: partRCs)
		{
			Inventory.getPart(partRC.getName()).addConstraint(partRC);
			tools.put(partRC.getName(), partRC);
		}
	}
	
	public String getNotes()
	{
		return notes;
	}
	
	public void setNotes(String s)
	{
		notes = s;
	}
}
