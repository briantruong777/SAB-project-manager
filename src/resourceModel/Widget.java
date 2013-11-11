package resourceModel;

import java.io.Serializable;

public class Widget implements Serializable
{
	public int num, max;
	
	public Widget(int n, int m)
	{
		num = n;
		max = m;
	}
}
