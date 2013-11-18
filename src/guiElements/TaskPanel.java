package guiElements;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import taskModel.*;

@SuppressWarnings("serial")
public class TaskPanel extends JPanel implements ItemListener
{
	private HashMap<String, TaskDisplayPanel> tasks;
	private Box taskList;
	private JCheckBox checkUnavailable;
	private JCheckBox checkIncomplete;
	private JCheckBox checkComplete;
	private TaskPanel self;
	/**
	 * Create the panel.
	 */
	public TaskPanel()
	{
		self = this;
		tasks = new HashMap<String, TaskDisplayPanel>();
		setLayout(new BorderLayout(0, 0));

		JScrollPane taskScroll = new JScrollPane();
		add(taskScroll, BorderLayout.CENTER);

		taskList = Box.createVerticalBox();
		taskScroll.setViewportView(taskList);

		JPanel taskViewPanel = new JPanel();
		add(taskViewPanel, BorderLayout.SOUTH);
		FlowLayout fl_taskViewPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		taskViewPanel.setLayout(fl_taskViewPanel);


		checkUnavailable = new JCheckBox("Unavailable");
		checkUnavailable.addItemListener(this);
		checkUnavailable.setToolTipText("View tasks whose constraints have not been met.");
		taskViewPanel.add(checkUnavailable);

		checkIncomplete = new JCheckBox("Incomplete");
		checkIncomplete.addItemListener(this);
		checkIncomplete.setSelected(true);
		checkIncomplete.setToolTipText("View tasks that can be started or are in progress.");
		taskViewPanel.add(checkIncomplete);

		checkComplete = new JCheckBox("Complete");
		checkComplete.addItemListener(this);
		checkComplete.setToolTipText("View completed tasks.");
		taskViewPanel.add(checkComplete);

		Box taskControlPanel = Box.createVerticalBox();
		add(taskControlPanel, BorderLayout.EAST);

		JButton btnCreateTask = new JButton("Create Task");
		btnCreateTask.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (TaskInfoDialog.showCreateDialog())
				{
					Task t = TaskInfoDialog.getTask();
					TaskManager.addTask(t);
					TaskDisplayPanel tdp = new TaskDisplayPanel(t, self);
					tasks.put(t.getName(), tdp);
					taskList.add(tdp);
					updateUI();
					Runner.notifyChange();
				}
			}
		});

		Component glue = Box.createGlue();
		taskControlPanel.add(glue);
		taskControlPanel.add(btnCreateTask);

		Component glue_1 = Box.createGlue();
		taskControlPanel.add(glue_1);
	}

	public void clearTaskList()
	{
		tasks.clear();
		taskList.removeAll();
		checkUnavailable.setSelected(false);
		checkIncomplete.setSelected(true);
		checkComplete.setSelected(false);
		repaint();
	}
	
	/**
	 * Completely clears and reloads taskList and tasks
	 */
	
	public void reloadTaskList()
	{
		clearTaskList();

		TaskDisplayPanel tdp;
		for (Task t : TaskManager.getSortedTasks())
		{
			tdp = new TaskDisplayPanel(t, this);
			tasks.put(t.getName(), tdp);
			taskList.add(tdp);
		}
		taskList.repaint();
	}

	public void refreshTasks(Collection<Task> refreshTasks)
	{
		for (Task t : refreshTasks)
		{
			tasks.get(t.getName()).refreshTaskStatus();
		}
		updateUI();
	}
	
	public void removeTask(Task t)
	{
		taskList.remove(tasks.get(t.getName()));
		tasks.remove(t.getName());
		updateUI();
	}
	
	public void renameTask(String oldName, String newName)
	{
		tasks.put(newName, tasks.remove(oldName));
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		for (TaskDisplayPanel p: tasks.values())
		{
			switch(p.getStatus())
			{
				case UNAVAILABLE:
					if (checkUnavailable.isSelected())
						p.setVisible(true);
					else
						p.setVisible(false);
					break;
				case UNSTARTED:
				case STOPPED:
				case WORKING:
				case PAUSED:
					if (checkIncomplete.isSelected())
						p.setVisible(true);
					else
						p.setVisible(false);
					break;
				case COMPLETE:
					if (checkComplete.isSelected())
						p.setVisible(true);
					else
						p.setVisible(false);
				default:
					break;
			}
		}
		taskList.repaint();
	}
}
