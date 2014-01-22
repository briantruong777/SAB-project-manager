package resourceModel;

import java.io.Serializable;
import java.util.Calendar;

public class BrokenReport implements Serializable
{
	private String report;
	private Calendar startDate;
	private Calendar endDate;
	
	public BrokenReport(String r, Calendar curTime)
	{
		report = r;
		startDate = curTime;
		endDate = Calendar.getInstance();
		endDate.clear(); // Invalidates value
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
	
	public String toString()
	{
		return report+""+startDate.get(Calendar.SECOND)+" "+endDate.get(Calendar.SECOND);
	}
}
