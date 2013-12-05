package taskModel;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents the time between a resume() and pause() for a Task.
 */
@SuppressWarnings("serial")
public class Session implements Serializable
{
	private String builder;
	private String foreman;
	private Calendar startDate;
	private Calendar endDate;

	/**
	 * Creates a new Session whose startDate is set to the provided time
	 */
	public Session(String builderName, String foremanName, Calendar curTime)
	{
		builder = builderName;
		foreman = foremanName;
		startDate = curTime;
		endDate = Calendar.getInstance();
		endDate.clear(); // Invalidates value
	}

	public String getBuilderName()
	{
		return builder;
	}
	public String getForemanName()
	{
		return foreman;
	}
	public Calendar getStartDate()
	{
		return startDate;
	}
	public Calendar getEndDate()
	{
		return endDate;
	}

	public boolean isFinished()
	{
		return endDate.isSet(Calendar.MINUTE);
	}
	/**
	 * Sets the endDate to the provided time
	 */
	public void finish(Calendar curTime)
	{
		endDate = curTime;
	}

	public String toString()
	{
		return "B: "+builder+" F: "+foreman+" "+startDate.get(Calendar.SECOND)+" "+endDate.get(Calendar.SECOND);
	}
}
