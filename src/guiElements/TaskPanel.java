package guiElements;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import taskModel.Task;
import taskModel.TaskManager;

@SuppressWarnings("serial")
public class TaskPanel extends JPanel implements ItemListener, Comparator<TaskDisplayPanel>
{
	private HashMap<String, TaskDisplayPanel> tasks;
	private Box taskList;
	private JCheckBox checkUnavailable;
	private JCheckBox checkIncomplete;
	private JCheckBox checkComplete;
	private JButton buttonFilter;
	private TaskPanel self;
	private JPanel taskControlPanel;

	private boolean adminViewEnabled;
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
		
		buttonFilter = new JButton("Filter");
		buttonFilter.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				itemStateChanged(null);
			}
		});
		buttonFilter.setToolTipText("Filter the list of tasks");
		taskViewPanel.add(buttonFilter);
		
		JButton btnSort = new JButton("Sort");
		btnSort.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				taskList.removeAll();
				ArrayList<TaskDisplayPanel> tdps = new ArrayList<TaskDisplayPanel>(tasks.values());
				Collections.sort(tdps, self);
				for (TaskDisplayPanel tdp: tdps)
					taskList.add(tdp);
				updateUI();
			}
		});
		taskViewPanel.add(btnSort);
		
		taskControlPanel = new JPanel();
		add(taskControlPanel, BorderLayout.EAST);
		GridBagLayout gbl_taskControlPanel = new GridBagLayout();
		gbl_taskControlPanel.columnWidths = new int[]{0, 0};
		gbl_taskControlPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_taskControlPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_taskControlPanel.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		taskControlPanel.setLayout(gbl_taskControlPanel);
		
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
					tdp.setAdminView(adminViewEnabled);
					tasks.put(t.getName(), tdp);
					taskList.add(tdp);
					updateUI();
					Runner.notifyChange();
				}
			}
		});
		GridBagConstraints gbc_btnCreateTask = new GridBagConstraints();
		gbc_btnCreateTask.insets = new Insets(0, 0, 5, 0);
		gbc_btnCreateTask.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCreateTask.gridx = 0;
		gbc_btnCreateTask.gridy = 1;
		taskControlPanel.add(btnCreateTask, gbc_btnCreateTask);

		setAdminView(false);
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

	/**
	 * Sets the admin view features visible or not based on given value
	 */
	public void setAdminView(boolean val)
	{
		taskControlPanel.setVisible(val);
		for (TaskDisplayPanel tdp : tasks.values())
		{
			tdp.setAdminView(val);
		}
		adminViewEnabled = val;
	}

	@Override
	public int compare(TaskDisplayPanel tdp1, TaskDisplayPanel tdp2)
	{
		int diff = tdp1.getStatus().compareTo(tdp2.getStatus());
		if (diff != 0)
			return diff;

		diff = tdp2.task.getDependers().size() - tdp1.task.getDependers().size();
		if (diff != 0)
			return diff;

		diff = tdp2.task.getParts().size() - tdp1.task.getParts().size();
		if (diff != 0)
			return diff;

		diff = tdp2.task.getTools().size() - tdp1.task.getTools().size();
		if (diff != 0)
			return diff;

		return tdp1.task.compareTo(tdp2.task);
	}
}
