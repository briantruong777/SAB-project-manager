package resourceModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import taskModel.Task;

@SuppressWarnings("serial")
public class Resource implements Serializable, Comparable<Resource>
{
	private String name;
	private int max;
	private int available;
	private ArrayList<Task> dependers;
	private boolean broken;
	private ArrayList<BrokenReport> reports;
	
	public Resource(String name)
	{
		this(name, 0);
		reports = new ArrayList<BrokenReport>();
	}
	
	public Resource(String name, int max)
	{
		this.name = name;
		this.max = available = max;
		dependers = new ArrayList<Task>();
		broken = false;
		reports = new ArrayList<BrokenReport>();
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

	public boolean isBroken()
	{
		return broken;
	}
	public void setBroken(boolean val)
	{
		broken = val;
	}

	public int compareTo(Resource o)
	{
		return name.compareTo(o.name);
	}
	
	public boolean equals(Object o)
	{
		return name.equals(o.toString());
	}
	
	public void addReport(String s, Calendar startDate, String b, String f, String t)
	{
		reports.add(new BrokenReport(s, startDate, b, f, t));
	}
	
	public BrokenReport getLatestReport()
	{
		return reports.get(reports.size()-1);
	}
	
	public ArrayList<BrokenReport> getBrokenReports()
	{
		return reports;
	}
	
	public String toString()
	{
		return name;
	}
}
