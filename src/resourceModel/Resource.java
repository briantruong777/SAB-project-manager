package resourceModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import taskModel.Task;

@SuppressWarnings("serial")
public class Resource implements Serializable
{
	private String name;
	private int max;
	private int available;
	private ArrayList<Task> dependers;
	
	public Resource(String name)
	{
		this(name, 0);
	}
	
	public Resource(String name, int max)
	{
		this.name = name;
		this.max = available = max;
		dependers = new ArrayList<Task>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public int getAvailable()
	{
		return available;
	}

	public void setAvailable(int available)
	{
		this.available = available;
	}
	
	public void addDepender(Task t)
	{
		dependers.add(t);
	}
	
	public void removeDepender(Task t)
	{
		dependers.remove(t);
	}
	
	public boolean hasDepender()
	{
		return !dependers.isEmpty();
	}
	
	public Collection<Task> getDependers()
	{
		return dependers;
	}
	
	public boolean equals(Object o)
	{
		return name.equals(o.toString());
	}
	
	public String toString()
	{
		return name;
	}
}
