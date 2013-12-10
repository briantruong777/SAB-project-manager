package resourceModel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResourceConstraint implements Serializable
{
	private String name;
	private int amount;
	
	public ResourceConstraint(String name)
	{
		this.name = name;
		this.amount = 1;
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
	
	public String toString()
	{
		return name;
	}
}
