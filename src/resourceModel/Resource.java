package resourceModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Resource implements Serializable
{
	private String name;
	private int max;
	private int available;
	private ArrayList<ResourceConstraint> constraints;
	
	public Resource(String name)
	{
		this(name, 0);
	}
	
	public Resource(String name, int max)
	{
		this.name = name;
		this.max = available = max;
		constraints = new ArrayList<ResourceConstraint>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		for (ResourceConstraint rc: constraints)
			rc.setName(name);
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
	
	public void addConstraint(ResourceConstraint constraint)
	{
		this.constraints.add(constraint);
	}
	
	public void removeConstraint(ResourceConstraint constraint)
	{
		this.constraints.remove(constraint);
	}
	
	public boolean hasConstraint()
	{
		return !constraints.isEmpty();
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
