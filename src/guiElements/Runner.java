package guiElements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import resourceModel.Inventory;
import resourceModel.Resource;
import taskModel.Task;
import taskModel.TaskManager;

public class Runner 
{
  private static final String CONTACT_INFO = "Andy Myrna (630) 536-7620"; // NOTE:CONTACT_INFO
  private static final int CONTACT_MAX_LENGTH = 200; // NOTE:CONTACT_MAX_LENGTH
	private static ActiveInstructionsFrame frame;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		frame = new ActiveInstructionsFrame();
		frame.setVisible(true);
	}

	/**
	 * Makes and saves a new project folder, but does not save project file.
	 * This will make a new folder with the path given (meaning path should not
	 * exist yet). Inside this folder, this method will make a backup folder.
	 * Returns true if successfully make project directory regardless of success
	 * of making backup folder.
	 */
	public static boolean saveNewProjectFolder(String path, String password)
	{
		// Assuming path does not end with \
		String projectNameStr = path.substring(path.lastIndexOf('\\')+1);

		File projectDirectory = new File(path);
		if (projectDirectory.exists())
		{
			if (!projectDirectory.isDirectory())
			{
				System.err.println("Project folder already exists as file!");
				JOptionPane.showMessageDialog(frame, "There already is a file here of that name! Project was not created.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else
		{
			if (!projectDirectory.mkdir())
			{
				System.err.println("Failed to make project directory!");
				JOptionPane.showMessageDialog(frame, "Failed to make project directory! Project was not created.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// Check if Backup folder exists already
		File backupDirectory = new File(path + "\\" + projectNameStr + "-Backup");
		if (backupDirectory.exists())
		{
			if (!backupDirectory.isDirectory())
			{
				System.err.println("Backup folder already exists as file.");
				// Not returning false since backup is only secondary
			}
		}
		else
		{
			backupDirectory.mkdir();
			// Not returning false if failure since backup folder is only secondary
		}

		// Saving password file
		File passFile = new File(backupDirectory.getAbsolutePath() + "\\misc.dat");
		if (passFile.exists())
		{
			if (!passFile.isFile())
			{
				System.err.println("Misc file already exists as directory!");
				JOptionPane.showMessageDialog(frame, "There already is a directory here of that name! Misc file was not created.", null, JOptionPane.ERROR_MESSAGE);
				// Not returning false since password is secondary
			}
		}
		else
		{
			try
			{
				passFile.createNewFile();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.err.println("Failed to create misc file!");
				JOptionPane.showMessageDialog(frame, "IO Error: Failed to create misc file!", null, JOptionPane.ERROR_MESSAGE);
				// Not returning false since password is secondary
			}
		}

		try
		{
			FileWriter fw = new FileWriter(passFile);
			fw.write(password);
			fw.flush();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.err.println("Failed to save misc file!");
			JOptionPane.showMessageDialog(frame, "IO Error: Failed to save misc file!", null, JOptionPane.ERROR_MESSAGE);
			// Not returning false since password is secondary
		}

    // Saving contact file
    File contactFile = new File(backupDirectory.getAbsolutePath() + "\\contact.dat");
    if (contactFile.exists())
    {
      if (!contactFile.isFile())
      {
        System.err.println("Contact file already exists as directory!");
        JOptionPane.showMessageDialog(frame, "There already is a directory here of that name! Contact file was not created.", null, JOptionPane.ERROR_MESSAGE);
        // Not returning false since contact is secondary
      }
    }
    else
    {
      try
      {
        contactFile.createNewFile();
      }
      catch(IOException ex)
      {
        ex.printStackTrace();
        System.err.println("Failed to create contact file!");
        JOptionPane.showMessageDialog(frame, "IO Error: Failed to create contact file!", null, JOptionPane.ERROR_MESSAGE);
        // Not returning false since contact is secondary
      }
    }
		
    try
    {
      FileWriter fw = new FileWriter(contactFile);
      fw.write(CONTACT_INFO);
      fw.flush();
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
      System.err.println("Failed to save contact file!");
      JOptionPane.showMessageDialog(frame, "IO Error: Failed to save contact file!", null, JOptionPane.ERROR_MESSAGE);
      // Not returning false since contact is secondary
    }

		return true;
	}

	/**
	 * This saves the project to the file given by path. Returns true if
	 * main save is successful, false if failed. Will still return true if
	 * backup save fails. This will also look for a folder called
	 * projectname-Backup in the same directory as the file given by path. If
	 * it doesn't find the folder, it does not save a backup.
	 */
	public static boolean saveProject(String path)
	{
		// Saving project file
		ArrayList<Serializable> saveList = new ArrayList<Serializable>();
		saveList.add(TaskManager.getTasksMap());
		saveList.add(Inventory.getToolsHash());
		saveList.add(Inventory.getPartsHash());

		FileOutputStream fos;
		ObjectOutputStream out;
		try
		{
			fos = new FileOutputStream(path);
			out = new ObjectOutputStream(fos);
			out.writeObject(saveList);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.err.println("Failed to save project file!");
			JOptionPane.showMessageDialog(frame, "IO Error: Failed to save project file!", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Now looking for Backup folder

		// Assuming path does not end with \
		String projectNameStr = path.substring(path.lastIndexOf('\\')+1);
		// projectDirectoryStr will not include \
		String projectDirectoryStr = path.substring(0, path.lastIndexOf('\\'));

		// backupDirectory.getPath() will not include \
		File backupDirectory = new File(
			projectDirectoryStr + "\\" + projectNameStr + "-Backup");

		if (backupDirectory.isDirectory())
		{
			// Format of backup: projectname-v###
			// Now attempting to find correct number to append
			int maxNum = 0;
			if (backupDirectory.list() != null)
			{
				int num;
				int dashIdx;
				for (String s : backupDirectory.list())
				{
					dashIdx = s.lastIndexOf('-');

					// If can't find '-' or file name doesn't match
					if (dashIdx == -1 || !s.substring(0, dashIdx).equals(projectNameStr))
						continue;

					try
					{
						num = Integer.parseInt(s.substring(s.lastIndexOf('-')+1));
					}
					catch (NumberFormatException ex)
					{
						continue;
					}

					if (num > maxNum)
					{
						maxNum = num;
					}
				}
				maxNum++;
			}
			else
			{
				System.err.println("Failed to list files in backup directory");
			}

			// Saving backup file
			try
			{
				fos = new FileOutputStream(
					backupDirectory.getPath() + "\\" + projectNameStr + "-" + maxNum);
				out = new ObjectOutputStream(fos);
				out.writeObject(saveList);
				out.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.err.println("Failed to save to backup file.");
				// Backup is only secondary so don't return false
			}
		}
		else
		{
			System.err.println("Backup directory does not exist or is a file.");
			// Backup is only secondary so don't return false
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean loadProject(String path)
	{
		ArrayList<Serializable> saveList;
		HashMap<String, Task> tasks;
		HashMap<String, Resource> tools;
		HashMap<String, Resource> parts;

		try
		{
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fis);
			saveList = (ArrayList<Serializable>) in.readObject();
			in.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "IO Error: File could not be loaded.", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "File Format Error: File is formatted incorrectly.", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try
		{
			tasks = (HashMap<String, Task>) saveList.get(0);
			tools = (HashMap<String, Resource>) saveList.get(1);
			parts = (HashMap<String, Resource>) saveList.get(2);
		}
		catch (ClassCastException ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "File Format Error: File is formatted incorrectly.", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		TaskManager.setTasksMap(tasks);
		Inventory.clear();
		Inventory.addTools(tools.values());
		Inventory.addParts(parts.values());
		 return true;
	}

	/**
	 * Returns true if the given String matches the given password file.
	 * Get password file by accessing file variable from frame.
	 */
	public static boolean checkPassword(String password)
	{
		File passFile = new File(frame.getFile().getAbsolutePath() + "-Backup\\misc.dat");
		if (passFile.isFile())
		{
			char[] charArray = new char[password.length()];
			FileReader fr;
			try
			{
				fr = new FileReader(passFile);
				fr.read(charArray);

				if (password.equals(new String(charArray)) && !fr.ready())
					return true;
				else
					return false;
			}
			catch (FileNotFoundException ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame, "FileNotFound Error: Could not find password file!", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame, "IO Error: Could not read password file!", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else
		{
			System.err.println("Failed to find password file!");
			JOptionPane.showMessageDialog(frame, "Failed to find password file!", null, JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public static String getContact()
	{
	  File contactFile = new File(frame.getFile().getAbsolutePath() + "-Backup\\contact.dat");
    if (contactFile.isFile())
    {
      char[] charArray = new char[CONTACT_MAX_LENGTH];
      FileReader fr;
      try
      {
        fr = new FileReader(contactFile);
        fr.read(charArray);
        return ((new String(charArray)).trim());
      }
      catch (FileNotFoundException ex)
      {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "FileNotFound Error: Could not find contact file!", null, JOptionPane.ERROR_MESSAGE);
        return CONTACT_INFO;
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "IO Error: Could not read contact file!", null, JOptionPane.ERROR_MESSAGE);
        return CONTACT_INFO;
      }
    }
    else
    {
      System.err.println("Failed to find contact file!");
      JOptionPane.showMessageDialog(frame, "Failed to find contact file!", null, JOptionPane.ERROR_MESSAGE);
      return CONTACT_INFO;
    }
	}
	
	public static void notifyChange()
	{
		frame.notifyChange();
	}

	public static void refreshTaskPanelTasks(Collection<Task> tasks)
	{
		frame.refreshTaskPanelTasks(tasks);
	}
}
