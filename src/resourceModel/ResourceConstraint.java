package resourceModel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResourceConstraint implements Serializable, Comparable<ResourceConstraint>
{
	private String name;
	private int amount;
	
	public ResourceConstraint(String name)
	{
		this.name = name;
		this.amount = 1;
	}

	public ResourceConstraint(String name, int amount)
	{
		this.name = name;
		this.amount = amount;
	}
	
	public String getName()
	{
		return name;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = 1;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int compareTo(ResourceConstraint o)
	{
		return name.compareTo(o.name);
	}
	
	public String toString()
	{
		return name;
	}
	
	public void increment()
	{
		this.amount++;
	}
}
