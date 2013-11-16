package guiElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import taskModel.Task;
import taskModel.TaskManager;

@SuppressWarnings("serial")
public class TaskPanel extends JPanel
{
	private ArrayList<TaskDisplayPanel> tasks;
	private Box taskList;
	/**
	 * Create the panel.
	 */
	public TaskPanel()
	{
		tasks = new ArrayList<TaskDisplayPanel>();
		setLayout(new BorderLayout(0, 0));

		JScrollPane taskScroll = new JScrollPane();
		add(taskScroll, BorderLayout.CENTER);

		taskList = Box.createVerticalBox();
		taskScroll.setViewportView(taskList);

		JPanel taskViewPanel = new JPanel();
		add(taskViewPanel, BorderLayout.SOUTH);
		FlowLayout fl_taskViewPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		taskViewPanel.setLayout(fl_taskViewPanel);


		JCheckBox checkUnavailable = new JCheckBox("Unavailable");
		checkUnavailable.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				for (TaskDisplayPanel p: tasks)
				{
					switch(p.getStatus())
					{
						case UNAVAILABLE:
						case STOPPED:
							if (e.getStateChange() == ItemEvent.SELECTED)
								p.setVisible(true);
							else
								p.setVisible(false);
							break;
						default:
							break;
					}
				}
				repaint();
			}
		});
		checkUnavailable.setToolTipText("View tasks whose constraints have not been met.");
		taskViewPanel.add(checkUnavailable);

		JCheckBox checkIncomplete = new JCheckBox("Incomplete");
		checkIncomplete.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				for (TaskDisplayPanel p: tasks)
				{
					switch(p.getStatus())
					{
						case INCOMPLETE:
						case WORKING:
						case PAUSED:
							if (e.getStateChange() == ItemEvent.SELECTED)
								p.setVisible(true);
							else
								p.setVisible(false);
							break;
						default:
							break;
					}
				}
				repaint();
			}
		});
		checkIncomplete.setSelected(true);
		checkIncomplete.setToolTipText("View tasks that can be started or are in progress.");
		taskViewPanel.add(checkIncomplete);

		JCheckBox checkComplete = new JCheckBox("Complete");
		checkComplete.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				for (TaskDisplayPanel p: tasks)
				{
					if (p.getStatus() == Task.Status.COMPLETE)
					{
						if (e.getStateChange() == ItemEvent.SELECTED)
							p.setVisible(true);
						else
							p.setVisible(false);
					}
				}
				repaint();
			}
		});
		checkComplete.setToolTipText("View completed tasks.");
		taskViewPanel.add(checkComplete);

		Box taskControlPanel = Box.createVerticalBox();
		add(taskControlPanel, BorderLayout.EAST);

		JButton btnCreateTask = new JButton("Create Task");
		btnCreateTask.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (TaskInfoDialog.showCreateDialog());
				{
					TaskManager.addTask(TaskInfoDialog.getTask());
					TaskDisplayPanel p = new TaskDisplayPanel(TaskInfoDialog.getTask());
					taskList.add(p);
					tasks.add(p);
				}
			}
		});

		Component glue = Box.createGlue();
		taskControlPanel.add(glue);
		taskControlPanel.add(btnCreateTask);

		Component glue_1 = Box.createGlue();
		taskControlPanel.add(glue_1);
	}

	/**
	 * Completely clears and reloads taskList and tasks
	 */
	public void reloadTaskList()
	{
		tasks.clear();
		taskList.removeAll();

		TaskDisplayPanel tdp;
		for (Task t : TaskManager.getSortedTasks())
		{
			tdp = new TaskDisplayPanel(t);
			tasks.add(tdp);
			taskList.add(tdp);
		}
		taskList.repaint();
	}
}
