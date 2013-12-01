package guiElements;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import taskModel.Task;

@SuppressWarnings("serial")
public class TaskDisplayPanel extends JPanel implements ActionListener, MouseListener
{

	Task task;
	//private JButton incompleteButton;
	private JLabel statusLabel;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton workingButton;
	private JButton completeButton;
	private JRadioButton mradioNotesButton;
	private JRadioButton mradioLinkButton;
	private JButton editButton;
	private TaskPanel panel;
	private Component mhorizontalGlue;
	private Component mhorizontalStrut;
	private Component mhorizontalStrut_1;
	private Component mhorizontalStrut_2;
	private Component mhorizontalStrut_3;
	private JScrollPane textScroll;
	private JTextArea textArea;

	/**
	 * Create the panel.
	 */
	public TaskDisplayPanel(Task task, TaskPanel panel)
	{
		textArea = new JTextArea();
		textArea.setColumns(30);
		textArea.setRows(10);
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		textScroll = new JScrollPane(textArea);

		setMaximumSize(new Dimension(32767, 40));
		this.panel = panel;
		this.task = task;
		setAlignmentX(0);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		mhorizontalStrut_1 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_1);

		statusLabel = new JLabel(new ImageIcon("res/unstarted.png"), JLabel.LEFT);
		add(statusLabel);

		mhorizontalStrut = Box.createHorizontalStrut(10);
		add(mhorizontalStrut);

		JLabel lblTasknamelabel = new JLabel(task.getName());
		lblTasknamelabel.addMouseListener(this);
		add(lblTasknamelabel);

		mhorizontalGlue = Box.createHorizontalGlue();
		add(mhorizontalGlue);

		workingButton = new JButton(new ImageIcon("res/work.png"));
		workingButton.addActionListener(this);
		workingButton.setActionCommand("Working");
		workingButton.setToolTipText("Set this task to the Working state");
		add(workingButton);

		pauseButton = new JButton(new ImageIcon("res/pause.png"));
		pauseButton.addActionListener(this);
		pauseButton.setActionCommand("Paused");
		pauseButton.setToolTipText("Set this task to the Paused state");
		add(pauseButton);

		completeButton = new JButton(new ImageIcon("res/complete.png"));
		completeButton.addActionListener(this);
		completeButton.setActionCommand("Complete");
		completeButton.setToolTipText("Set this task to the Complete state");
		add(completeButton);

		mhorizontalStrut_3 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_3);

		stopButton = new JButton(new ImageIcon("res/stop.png"));
		stopButton.addActionListener(this);
		stopButton.setActionCommand("Stopped");
		stopButton.setToolTipText("Set this task to the Stopped state");
		add(stopButton);

		mradioNotesButton = new JRadioButton(new ImageIcon("res/notes.png"));
		mradioNotesButton.addActionListener(this);
		mradioNotesButton.setActionCommand("Notes");
		mradioNotesButton.setToolTipText("Edit/View this Task's notes");
		add(mradioNotesButton);

		mradioLinkButton = new JRadioButton(new ImageIcon("res/folder.png"));
		mradioLinkButton.addActionListener(this);
		mradioLinkButton.setActionCommand("File");
		mradioLinkButton.setToolTipText("Open a window to this Task's associated file");
		add(mradioLinkButton);

		editButton = new JButton("Edit");
		editButton.addActionListener(this);
		editButton.setActionCommand("Edit");
		editButton.setToolTipText("Edit this Task");
		add(editButton);

		mhorizontalStrut_2 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_2);

		refreshTaskStatus();

		//		taskStatusChange();
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		Task.Status curTaskStatus = task.getStatus();
		System.out.println(command);
		if (command.equals("Stopped"))
		{
			if (curTaskStatus == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			else if (curTaskStatus == Task.Status.PAUSED ||
							 curTaskStatus == Task.Status.WORKING)
			{
				task.stop();
			}
			else if (curTaskStatus == Task.Status.COMPLETE)
			{
				boolean haveWorkingDependers = false;
				for (Task t : task.getDependers())
				{
					if (t.getStatus() == Task.Status.WORKING ||
							t.getStatus() == Task.Status.PAUSED)
					{
						haveWorkingDependers = true;
						break;
					}
				}
				if (haveWorkingDependers)
				{
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(panel, "Setting this task to the Stopped state\nwill set any Working/Paused\ntasks dependent on this one\nto the Stopped state.\n\nDo you want to set this task and relevant\ndependent tasks to the Stopped state?", "Set this task to Stopped state?", JOptionPane.YES_NO_OPTION))
					{
						ArrayList<Task> refreshTasks = new ArrayList<Task>();
						for (Task t : task.getDependers())
						{
							if (t.getStatus() == Task.Status.WORKING ||
									t.getStatus() == Task.Status.PAUSED)
							{
								t.stop();
								t.pause();
								t.setStatus(Task.Status.STOPPED);
								refreshTasks.add(t);
							}
						}
						panel.refreshTasks(refreshTasks);
					}
					else
					{
						return;
					}
				}
			}

			task.pause();
			task.setStatus(Task.Status.STOPPED);
			if (curTaskStatus == Task.Status.COMPLETE)
			{
				panel.refreshTasks(task.getDependers());
				refreshTaskStatus();
			}
			Runner.notifyChange();
		}
		else if (command.equals("Paused"))
		{
			if (curTaskStatus == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			if (curTaskStatus != Task.Status.WORKING)
			{
				task.start();
			}

			task.pause();
			task.setStatus(Task.Status.PAUSED);
			if (curTaskStatus == Task.Status.COMPLETE)
			{
				panel.refreshTasks(task.getDependers());
			}
			Runner.notifyChange();
		}
		else if (command.equals("Working"))
		{
			if (curTaskStatus == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			if (curTaskStatus != Task.Status.PAUSED)
			{
				task.start();
			}

			task.resume();
			task.setStatus(Task.Status.WORKING);
			if (curTaskStatus == Task.Status.COMPLETE)
			{
				panel.refreshTasks(task.getDependers());
			}
			Runner.notifyChange();
		}
		else if (command.equals("Complete"))
		{
			if (curTaskStatus == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			else if (curTaskStatus == Task.Status.PAUSED ||
					curTaskStatus == Task.Status.WORKING)
			{
				task.stop();
			}

			task.pause();
			task.finish();
			task.setStatus(Task.Status.COMPLETE);
			panel.refreshTasks(task.getDependers());
			Runner.notifyChange();
		}
		else if (command.equals("Notes"))
		{
			String text = task.getNotes();
			textArea.setText(text);
			int option = JOptionPane.showConfirmDialog(this, textScroll, "Task Notes", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (option == JOptionPane.OK_OPTION)
			{
				task.setNotes(textArea.getText());
				Runner.notifyChange();
			}
		}
		else if (command.equals("File"))
		{
			try
			{
				Runtime.getRuntime().exec("explorer /select," + task.getPath());
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else if (command.equals("Edit"))
		{
			if (TaskInfoDialog.showEditDialog(task))
			{
				if (TaskInfoDialog.getTask() == null)
					panel.removeTask(task);
				if (TaskInfoDialog.nameChanged())
					panel.renameTask(TaskInfoDialog.oldName(), TaskInfoDialog.getTask().getName());
				refreshTaskStatus();
			}
		}

		enableButtons();
	}

	/**
	 * Enables buttons and sets icon based on current status
	 */
	private void enableButtons()
	{
		switch (task.getStatus())
		{
			case UNAVAILABLE:
				stopButton.setEnabled(false);
				pauseButton.setEnabled(false);
				workingButton.setEnabled(false);
				completeButton.setEnabled(false);
				statusLabel.setIcon(new ImageIcon("res/unavailable.png"));
				break;
			case UNSTARTED:
				stopButton.setEnabled(true);
				pauseButton.setEnabled(true);
				workingButton.setEnabled(true);
				completeButton.setEnabled(true);
				statusLabel.setIcon(new ImageIcon("res/unstarted.png"));
				break;
			case STOPPED:
				stopButton.setEnabled(false);
				pauseButton.setEnabled(true);
				workingButton.setEnabled(true);
				completeButton.setEnabled(true);
				statusLabel.setIcon(new ImageIcon("res/stop.png"));
				break;
			case PAUSED:
				stopButton.setEnabled(true);
				pauseButton.setEnabled(false);
				workingButton.setEnabled(true);
				completeButton.setEnabled(true);
				statusLabel.setIcon(new ImageIcon("res/pause.png"));
				break;
			case WORKING:
				stopButton.setEnabled(true);
				pauseButton.setEnabled(true);
				completeButton.setEnabled(true);
				workingButton.setEnabled(false);
				statusLabel.setIcon(new ImageIcon("res/work.png"));
				break;
			case COMPLETE:
				stopButton.setEnabled(true);
				pauseButton.setEnabled(false);
				workingButton.setEnabled(false);
				completeButton.setEnabled(false);
				statusLabel.setIcon(new ImageIcon("res/complete.png"));
				break;
			default:
				break;
		}
	}

	public Task.Status getStatus()
	{
		return task.getStatus();
	}

	public void refreshTaskStatus()
	{
		task.refreshStatus();
		enableButtons();
	}

	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			System.out.println("Double click");
			String text = task.getSteps();
			textArea.setText(text);
			int option = JOptionPane.showConfirmDialog(this, textScroll, "Task Steps", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (option == JOptionPane.OK_OPTION)
			{
				task.setSteps(textArea.getText());
				Runner.notifyChange();
			}
		}
	}

	// Needed to fulfill interface MouseListener
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}

	/*	
	private void taskStatusChange()
	{
		switch (task.getStatus())
		{
			case ILLEGAL:
				mradioPlayButton.setEnabled(false);
				mradioPausebutton.setEnabled(false);
				mradioStopbutton.setEnabled(false);
				setBackground(Color.RED);
				break;
			case UNAVAILABLE:
				mradioPlayButton.setEnabled(false);
				mradioPausebutton.setEnabled(false);
				mradioStopbutton.setEnabled(false);
				setBackground(Color.DARK_GRAY);
				break;
			case UNSTARTED:
				mradioPlayButton.setEnabled(true);
				mradioPausebutton.setEnabled(false);
				mradioStopbutton.setEnabled(false);
				setBackground(Color.BLUE);
				break;
			case WORKING:
				mradioPlayButton.setEnabled(false);
				mradioPausebutton.setEnabled(true);
				mradioStopbutton.setEnabled(true);
				setBackground(Color.GREEN);
				break;
			case PAUSED:
				mradioPlayButton.setEnabled(true);
				mradioPausebutton.setEnabled(false);
				mradioStopbutton.setEnabled(true);
				setBackground(Color.YELLOW);
				break;
			case COMPLETE:
				mradioPlayButton.setEnabled(false);
				mradioPausebutton.setEnabled(false);
				mradioStopbutton.setEnabled(false);
				setBackground(Color.LIGHT_GRAY);
				break;
		}
	}

	private class TaskStatusManager implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			switch(e.getActionCommand())
			{
				case "Play":
					task.setStatus(Task.Status.WORKING);
					break;
				case "Pause":
					task.setStatus(Task.Status.PAUSED);
					break;
				case "Stop":
					task.setStatus(Task.Status.COMPLETE);
			}
			taskStatusChange();
		}

	}*/

}
