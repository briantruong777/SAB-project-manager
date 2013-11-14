package resourceModel;

public class Resource
{
	private String name;
	private int max;
	private int available;
	
	public Resource(String name)
	{
		this.name = name;
	}
	
	public Resource(String name, int max)
	{
		this(name);
		this.max = available = max;
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
	
	public boolean equals(Object o)
	{
		return name.equals(o.toString());
	}
	
	public String toString()
	{
		return name;
	}
}
