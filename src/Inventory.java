import java.util.HashMap;
import java.util.Scanner;

public class Inventory
{
	public static HashMap<String, Widget> tools = new HashMap<String, Widget>();
	public static HashMap<String, Widget> parts = new HashMap<String, Widget>();
	
	public Inventory()
	{
		fillMap(tools, "tools.txt");
		//fillMap(parts, "parts.txt");
	}
	
	public static void fillMap(HashMap map, String filename)
	{
		Scanner filereader = new Scanner("tools.txt");
		/*while (filereader.hasNext())
		{
			String name = filereader.nextLine();
			int available = filereader.nextInt();
			int max = filereader.nextInt();
			map.put(name, new Widget(available, max));
			//map.put((String)filereader.nextLine(), new Widget(filereader.nextInt(), filereader.nextInt()));
		}*/
		
		tools.put("hammer", new Widget(1, 2));
		tools.put("screwdriver", new Widget(5, 10));
		tools.put("saw", new Widget(3, 7));
	}	
	
	public static void printMap()
	{
		for (String key : tools.keySet())
		{
			System.out.println(key + " has " + tools.get(key).available + " available out of " + tools.get(key).max + " total"); 
		}
	}
}
