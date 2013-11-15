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
			}
		});
		checkUnavailable.setToolTipText("View tasks whose constraints have not been met.");
		taskViewPanel.add(checkUnavailable);

		JCheckBox checkIncomplete = new JCheckBox("Incomplete");
		checkIncomplete.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					for (TaskDisplayPanel p: tasks)
					{
						
					}
				}
				else
				{
					for (TaskDisplayPanel p: tasks)
					{
						
					}
				}
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
			}
		});
		checkComplete.setToolTipText("View completed tasks.");
		taskViewPanel.add(checkComplete);

		Box taskControlPanel = Box.createVerticalBox();
		add(taskControlPanel, BorderLayout.EAST);

		JButton btnNew = new JButton("New");
		btnNew.setPreferredSize(new Dimension(81, 25));
		btnNew.setMaximumSize(new Dimension(81, 25));
		btnNew.setMinimumSize(new Dimension(81, 25));
		taskControlPanel.add(btnNew);

		JButton btnDelete = new JButton("Delete");
		taskControlPanel.add(btnDelete);
	}

}
