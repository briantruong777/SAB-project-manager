package resourceModel;

import java.io.Serializable;
import java.util.Calendar;

public class BrokenReport implements Serializable
{
	private String report;
	private Calendar startDate;
	private Calendar endDate;
	private String builder;
	private String foreman;
	private String task;
	
	public BrokenReport(String r, Calendar curTime, String b, String f, String t)
	{
		report = r;
		startDate = curTime;
		endDate = Calendar.getInstance();
		endDate.clear(); // Invalidates value
		builder = b;
		foreman = f;
		task = t;
	}
	
	public String getReport()
	{
		return report;
	}
	
	public Calendar getStartDate()
	{
		return startDate;
	}
	
	public Calendar getEndDate()
	{
		return endDate;
	}
	
	public void markAsFixed(Calendar curTime)
	{
		endDate = curTime;
	}
	
	public String getBuilder()
	{
	  return builder;
	}
  
  public String getForeman()
  {
    return foreman;
  }
  
  public String getTask()
  {
    return task;
  }
	
	public String toString()
	{
		return report+""+startDate.get(Calendar.SECOND)+" "+endDate.get(Calendar.SECOND);
	}
}
