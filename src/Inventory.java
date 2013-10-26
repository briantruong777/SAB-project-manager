import java.util.HashMap;
import java.util.Scanner;

public class Inventory
{
	HashMap<String, Widget> tools = new HashMap<String, Widget>();
	HashMap<String, Widget> parts = new HashMap<String, Widget>();
	
	public Inventory()
	{
		
	}
	
	public void fillMap(HashMap map, String filename)
	{
		Scanner filereader = new Scanner(filename);
		while (filereader.hasNext())
		{
			map.put(filereader.next(), new Widget(filereader.nextInt(), filereader.nextInt()));
		}
	}
	
}
