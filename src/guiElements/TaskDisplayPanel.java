package guiElements;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import taskModel.Task;
import taskModel.Task.Status;

public class TaskDisplayPanel extends JPanel implements ActionListener
{

	Task task;
	//private JButton incompleteButton;
	private JLabel statusLabel;
	private JButton pauseButton;
	private JButton completeButton;
	private JButton workingButton;
	private JRadioButton mradioNotesButton;
	private JSeparator mseparator;
	private JRadioButton mradioLinkButton;
	
	/**
	 * Create the panel.
	 */
	public TaskDisplayPanel(Task task)
	{
		this.task = task;
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		statusLabel = new JLabel("STATUS", new ImageIcon("res/incomplete_bw.png"), JLabel.LEFT);
		add(statusLabel);
		
		JLabel lblTasknamelabel = new JLabel(task.getName());
//		JLabel lblTasknamelabel = new JLabel(t.getName());
		add(lblTasknamelabel);
		
//		TaskStatusManager statusManager = new TaskStatusManager();
		
		/*incompleteButton = new JButton(new ImageIcon("res/incomplete.png"));
//		mradioPlayButton.addActionListener(statusManager);
		incompleteButton.setActionCommand("Incomplete");
		add(incompleteButton);*/
		
		
		
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
		
		mseparator = new JSeparator();
		mseparator.setOrientation(SwingConstants.VERTICAL);
		add(mseparator);

		mradioNotesButton = new JRadioButton(new ImageIcon("res/notes.png"));
		mradioNotesButton.setActionCommand("Notes");
		add(mradioNotesButton);
		
		mradioLinkButton = new JRadioButton(new ImageIcon("res/folder.png"));
		add(mradioLinkButton);

		setMaximumSize(getMinimumSize());
		
//		taskStatusChange();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		System.out.println(command);
		if (command.equals("Working"))
		{
			//System.out.println("I have just pressed working");
			workingButton.setEnabled(false);
			pauseButton.setEnabled(true);
			completeButton.setEnabled(true);
			if (task.getStatus() == Task.Status.INCOMPLETE)
				task.start();
			else if (task.getStatus() == Task.Status.PAUSED || task.getStatus() == Task.Status.COMPLETE)
				task.resume();
			task.setStatus(Task.Status.WORKING);
			statusLabel.setIcon(new ImageIcon("res/work.png"));
		}
		else if (command.equals("Paused"))
		{
			pauseButton.setEnabled(false);
			workingButton.setEnabled(true);
			completeButton.setEnabled(true);
			task.pause();
			task.setStatus(Task.Status.PAUSED);
			statusLabel.setIcon(new ImageIcon("res/pause.png"));
		}
		else if (command.equals("Complete"))
		{
			completeButton.setEnabled(false);
			workingButton.setEnabled(true);
			pauseButton.setEnabled(true);
			if (task.getStatus() == Task.Status.WORKING || task.getStatus() == Task.Status.PAUSED)
				task.stop();
			task.setStatus(Task.Status.COMPLETE);
			statusLabel.setIcon(new ImageIcon("res/complete.png"));
		}
	}

	public Task.Status getStatus()
	{
		return task.getStatus();
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
			case INCOMPLETE:
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
