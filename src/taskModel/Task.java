package taskModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import resourceModel.Inventory;
import resourceModel.ResourceConstraint;
import resourceModel.Resource;

@SuppressWarnings("serial")
public class Task implements Serializable, Comparable<Task>
{
	private String name;
	private Status taskStatus;
	private HashMap<String, ResourceConstraint> tools;
	private HashMap<String, ResourceConstraint> parts;
	private ArrayList<Task> dependencies;
	private ArrayList<Task> dependers;
	private Calendar startDate;
	private Calendar endDate;
	private ArrayList<Session> sessions;
	private long timeSpent;
	private String path;
	private String notes;
	private String steps;
	private boolean undoCompleted;

	private long lastResumeTime;
	
	public enum Status
	{
		UNAVAILABLE, UNSTARTED, WORKING, PAUSED, COMPLETE;
	}
	
	public Task(String s)
	{
		this.name = s;
		taskStatus = Status.UNSTARTED;
		tools = new HashMap<String, ResourceConstraint>();
		parts = new HashMap<String, ResourceConstraint>();
		dependencies = new ArrayList<Task>();
		dependers = new ArrayList<Task>();
		startDate = Calendar.getInstance();
		startDate.clear(); // Invalidates value
		endDate = Calendar.getInstance();
		endDate.clear();
		sessions = new ArrayList<Session>();
		timeSpent = 0; // In milliseconds
		path= "";
		notes = "";
		steps = "";
		undoCompleted = false;

		lastResumeTime = -1; // -1 when paused
	}

	public String getName()
	{
		return name;
	}
	
	// return whether Task needs to be rehashed
	public boolean setName(String str)
	{
		// check whether name is changed
		if (!"".equals(name) && !name.equals(str))
		{
			TaskManager.removeTask(name);
			name = str;
			TaskManager.addTask(this);
			return true;
		}
		else
		{
			name = str;
			return false;
		}
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
	
	public void renameTool(String oldName, String newName)
	{
		ResourceConstraint rc = tools.remove(oldName);
		rc.setName(newName);
		tools.put(newName, rc);
	}
	
	public ResourceConstraint getTool(String name)
	{
		return tools.get(name);
	}
	
	public void addTool(ResourceConstraint tool)
	{
		Inventory.getTool(tool.getName()).addDepender(this);
		tools.put(tool.getName(), tool);
		
	}
	public void removeTool(String name)
	{
		Inventory.getTool(name).removeDepender(this);
		tools.remove(name);
	}
	
	public Collection<ResourceConstraint> getParts()
	{
		return parts.values();
	}
	
	public void renamePart(String oldName, String newName)
	{
		ResourceConstraint rc = parts.remove(oldName);
		rc.setName(newName);
		parts.put(newName, rc);
	}

	public ResourceConstraint getPart(String name)
	{
		return parts.get(name);
	}
	
	public void addPart(ResourceConstraint part)
	{
		Inventory.getPart(part.getName()).addDepender(this);
		parts.put(part.getName(), part);
	}
	
	public void removePart(String name)
	{
		parts.remove(name);
		Inventory.getPart(name).removeDepender(this);
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
	
	public void clearDependers()
	{
		dependers.clear();
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

	public Calendar getStartDate()
	{
		return startDate;
	}
	public Calendar getEndDate()
	{
		return endDate;
	}

	public ArrayList<Session> getSessions()
	{
		return sessions;
	}

	public long getTimeSpent()
	{
		return timeSpent;
	}
	public void setTimeSpent(long timeSpent)
	{
		this.timeSpent = timeSpent;
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
		// Only if status is UNAVAILABLE, UNSTARTED, or PAUSED, then refresh status
		if (taskStatus == Status.UNAVAILABLE ||
				taskStatus == Status.UNSTARTED ||
				taskStatus == Status.PAUSED)
		{
			if (meetDependencies() && meetResources())
			{
				if (startDate.isSet(Calendar.MINUTE))
				{
					taskStatus = Status.PAUSED;
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
			Inventory.getTool(toolRC.getName()).removeDepender(this);
		tools.clear();
	}
	
	public void clearParts()
	{
		for (ResourceConstraint partRC: parts.values())
			Inventory.getPart(partRC.getName()).removeDepender(this);
		parts.clear();
	}
	
	public void addTools(Collection<ResourceConstraint> toolRCs)
	{
		for (ResourceConstraint toolRC: toolRCs)
		{
			Inventory.getTool(toolRC.getName()).addDepender(this);
			tools.put(toolRC.getName(), toolRC);
		}
	}

	public void addParts(Collection<ResourceConstraint> partRCs)
	{
		for (ResourceConstraint partRC: partRCs)
		{
			Inventory.getPart(partRC.getName()).addDepender(this);
			parts.put(partRC.getName(), partRC);
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
	
	public String getSteps()
	{
		return steps;
	}
	
	public void setSteps(String s)
	{
		steps = s;
	}

	public boolean getUndoCompleted()
	{
		return undoCompleted;
	}
	public void setUndoCompleted(boolean val)
	{
		undoCompleted = val;
	}

	/**
	 * Returns true when Task has some dependencies that are undo-completed
	 */
	public boolean checkDependenciesUndoCompleted()
	{
		for (Task t : dependencies)
		{
			if (t.getUndoCompleted())
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns ArrayList of dependencies (Tasks) that are undo-completed
	 */
	public ArrayList<Task> getDependenciesUndoCompleted()
	{
		ArrayList<Task> undoCompletedTasks = new ArrayList<Task>();
		for (Task t : dependencies)
		{
			if (t.getUndoCompleted())
			{
				undoCompletedTasks.add(t);
			}
		}
		return undoCompletedTasks;
	}

	/**
	 * Returns ArrayList of any broken tools.
	 */
	public ArrayList<Resource> getBrokenTools()
	{
		ArrayList<Resource> brokenTools = new ArrayList<Resource>();
		Resource r;
		for (ResourceConstraint rc : tools.values())
		{
			r = Inventory.getTool(rc.getName());
			if (r.isBroken())
				brokenTools.add(r);
		}
		return brokenTools;
	}
	/**
	 * Returns ArrayList of any broken parts.
	 */
	public ArrayList<Resource> getBrokenParts()
	{
		ArrayList<Resource> brokenParts = new ArrayList<Resource>();
		Resource r;
		for (ResourceConstraint rc : parts.values())
		{
			r = Inventory.getPart(rc.getName());
			if (r.isBroken())
				brokenParts.add(r);
		}
		return brokenParts;
	}
	
	public int hashCode()
	{
		return name.hashCode();
	}

	/**
	 * Run only once when Task is begun. Sets the startDate.
	 */
	public void begin()
	{
		startDate = Calendar.getInstance();
	}
	/**
	 * Run when task is completely finished. Sets the endDate.
	 */
	public void finish()
	{
		endDate = Calendar.getInstance();
	}

	public void start()
	{
		Inventory.takeResources(tools.values(), parts.values());
	}
	public void stop()
	{
		endDate.clear();
		Inventory.releaseResources(tools.values(), parts.values());
	}

	/**
	 * Resumes counting working time. Can be called multiple times safely.
	 */
	public void resume(String builderName, String foremanName)
	{
		if (lastResumeTime == -1) // Check if paused
		{
			Calendar temp = Calendar.getInstance();
			lastResumeTime = temp.getTimeInMillis();
			sessions.add(new Session(builderName, foremanName, temp));
		}
	}
	/**
	 * Pauses counting working time. Can be called multiple times safely.
	 */
	public void pause()
	{
		if (lastResumeTime != -1) // Check for valid last resume time
		{
			Calendar temp = Calendar.getInstance();
			long curTime = temp.getTimeInMillis();
			timeSpent += curTime - lastResumeTime;
			lastResumeTime = -1;
			sessions.get(sessions.size()-1).finish(temp);
		}
	}
}
