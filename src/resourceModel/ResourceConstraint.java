package resourceModel;

public class ResourceConstraint
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
	
	public String toString()
	{
		return getName();
	}
	
	public boolean equals(Object o)
	{
		return toString().equals(o.toString());
	}
}
