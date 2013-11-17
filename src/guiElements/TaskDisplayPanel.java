package guiElements;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
public class TaskDisplayPanel extends JPanel implements ActionListener
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
	
	/**
	 * Create the panel.
	 */
	public TaskDisplayPanel(Task task, TaskPanel panel)
	{
		setMaximumSize(new Dimension(32767, 40));
		this.panel = panel;
		this.task = task;
		setAlignmentX(0);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		mhorizontalStrut_1 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_1);
		
		statusLabel = new JLabel(new ImageIcon("res/incomplete_bw.png"), JLabel.LEFT);
		add(statusLabel);
		
		mhorizontalStrut = Box.createHorizontalStrut(10);
		add(mhorizontalStrut);
		
		JLabel lblTasknamelabel = new JLabel(task.getName());
//		JLabel lblTasknamelabel = new JLabel(t.getName());
		add(lblTasknamelabel);
		
//		TaskStatusManager statusManager = new TaskStatusManager();
		
		/*incompleteButton = new JButton(new ImageIcon("res/incomplete.png"));
//		mradioPlayButton.addActionListener(statusManager);
		incompleteButton.setActionCommand("Incomplete");
		add(incompleteButton);*/
		
//		stopButton = new JButton(new ImageIcon("res/stop.png"));
		stopButton = new JButton(new ImageIcon("res/stop.png"));
		stopButton.addActionListener(this);
		
		mhorizontalGlue = Box.createHorizontalGlue();
		add(mhorizontalGlue);
		stopButton.setActionCommand("Stopped");
		add(stopButton);
		
		pauseButton = new JButton(new ImageIcon("res/pause.png"));
		pauseButton.addActionListener(this);
		pauseButton.setActionCommand("Paused");
		add(pauseButton);
		
		workingButton = new JButton(new ImageIcon("res/work.png"));
		workingButton.addActionListener(this);
		workingButton.setActionCommand("Working");
		add(workingButton);
		
		completeButton = new JButton(new ImageIcon("res/complete.png"));
		completeButton.addActionListener(this);
		completeButton.setActionCommand("Complete");
		add(completeButton);

		mradioNotesButton = new JRadioButton(new ImageIcon("res/notes.png"));
		mradioNotesButton.addActionListener(this);
		mradioNotesButton.setActionCommand("Notes");
		add(mradioNotesButton);
		
		mradioLinkButton = new JRadioButton(new ImageIcon("res/folder.png"));
		mradioLinkButton .addActionListener(this);
		mradioLinkButton .setActionCommand("File");
		add(mradioLinkButton);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(this);
		editButton.setActionCommand("Edit");
		add(editButton);
		
		mhorizontalStrut_2 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_2);

		refreshTaskStatus();
		
//		taskStatusChange();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		System.out.println(command);
		if (command.equals("Stopped"))
		{
			if (task.getStatus() == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			if (task.getStatus() == Task.Status.PAUSED ||
					task.getStatus() == Task.Status.WORKING)
				task.stop();

			task.pause();
			task.setStatus(Task.Status.STOPPED);
		}
		else if (command.equals("Paused"))
		{
			if (task.getStatus() == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			if (task.getStatus() != Task.Status.WORKING)
				task.start();

			task.pause();
			task.setStatus(Task.Status.PAUSED);
		}
		else if (command.equals("Working"))
		{
			if (task.getStatus() == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			if (task.getStatus() != Task.Status.WORKING)
				task.start();

			task.resume();
			task.setStatus(Task.Status.WORKING);
		}
		else if (command.equals("Complete"))
		{
			if (task.getStatus() == Task.Status.UNSTARTED)
			{
				task.begin();
			}
			if (task.getStatus() == Task.Status.PAUSED ||
					task.getStatus() == Task.Status.WORKING)
				task.stop();

			task.finish();
			task.setStatus(Task.Status.COMPLETE);
		}
		else if (command.equals("Notes"))
		{
			String text = task.getNotes();
			 
				JTextArea textArea = new JTextArea(text);
				textArea.setColumns(30);
				textArea.setLineWrap( true );
				textArea.setWrapStyleWord( true );
				JScrollPane scroll = new JScrollPane(textArea);
				JOptionPane.showMessageDialog(null, scroll, "Task Notes", JOptionPane.PLAIN_MESSAGE);		
				task.setNotes(textArea.getText());
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
				statusLabel.setIcon(new ImageIcon("res/incomplete_bw.png"));
				break;
			case UNSTARTED:
				stopButton.setEnabled(true);
				pauseButton.setEnabled(true);
				workingButton.setEnabled(true);
				completeButton.setEnabled(true);
				statusLabel.setIcon(new ImageIcon("res/incomplete_bw.png"));
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
				workingButton.setEnabled(false);
				completeButton.setEnabled(true);
				statusLabel.setIcon(new ImageIcon("res/work.png"));
				break;
			case COMPLETE:
				stopButton.setEnabled(true);
				pauseButton.setEnabled(true);
				workingButton.setEnabled(true);
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
