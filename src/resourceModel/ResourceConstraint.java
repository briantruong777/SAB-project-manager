package resourceModel;

import java.io.Serializable;

public class ResourceConstraint implements Serializable
{
	private String name;
	private int amount;
	
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
		this.amount = amount;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
