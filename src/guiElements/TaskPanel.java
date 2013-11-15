package guiElements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import taskModel.Task;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class TaskPanel extends JPanel
{
	private ArrayList<TaskDisplayPanel> tasks;
	/**
	 * Create the panel.
	 */
	public TaskPanel()
	{
		tasks = new ArrayList<TaskDisplayPanel>();
		setLayout(new BorderLayout(0, 0));

		JScrollPane taskScroll = new JScrollPane();
		add(taskScroll, BorderLayout.CENTER);

		Box taskList = Box.createVerticalBox();
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
					if (p.getStatus() == Task.Status.UNAVAILABLE)
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

		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				
			}
		});
		btnNew.setPreferredSize(new Dimension(81, 25));
		btnNew.setMaximumSize(new Dimension(81, 25));
		btnNew.setMinimumSize(new Dimension(81, 25));
		taskControlPanel.add(btnNew);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		taskControlPanel.add(btnDelete);
	}

}
