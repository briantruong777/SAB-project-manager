package guiElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;

import resourceModel.Inventory;
import runner.Runner;

public class FileHandler implements ActionListener
{
	public static String lastSavedLocation;
	private File f;
	
	public FileHandler()
	{
		f = new File("");
		lastSavedLocation = getLastSavedLocation();
	}

	
	public String getLastSavedLocation()
	{
		try
		{
			Scanner reader = new Scanner(f);
			return reader.nextLine();
		}
		catch (FileNotFoundException e)
		{
			f = new File("C:\\lastsavedlocation.txt");
			return "";
		}		
	}
	
	public String setLastSavedLocation(String path)
	{
		try
		{
			PrintWriter writer = new PrintWriter(f);
			writer.write("");
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			
		}
		return "";
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getActionCommand().equals("New"))
			actionNew();
		if (arg0.getActionCommand().equals("Open"))
			open();
		if (arg0.getActionCommand().equals("Save"))
			save();
		if (arg0.getActionCommand().equals("Save As..."))
			saveAs();
		if (arg0.getActionCommand().equals("Export"))
			export();
		if (arg0.getActionCommand().equals("Quit"))
			quit();
	}
	
	public void actionNew()
	{
		save();
		Inventory.clear();
		//TaskManager.clear();
	}
	
	public void open()
	{
		if (hasBeenSaved())
		{
			Runner.loadTasks(lastSavedLocation);
		}
		else
		{
			JFileChooser fileChooser = new JFileChooser();
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) 
	        {
	        	File selectedFile = fileChooser.getSelectedFile();
	        	setLastSavedLocation(selectedFile.getAbsolutePath());
	        }
		}
	}
	
	public void save()
	{
		if (!hasBeenSaved())
			saveAs();
		else
		{
			Runner.saveTasks(lastSavedLocation);
		}
	}
	
	public void saveAs()
	{
		JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) 
        {
        	File selectedFile = fileChooser.getSelectedFile();
        	setLastSavedLocation(selectedFile.getAbsolutePath());
        }
        Runner.saveTasks(lastSavedLocation);
	}
	
	public void export()
	{
		
	}
	
	public void quit()
	{
		save();
		System.exit(1);
	}
	public boolean hasBeenSaved()
	{
		return getLastSavedLocation().length() != 0;
	}

}
