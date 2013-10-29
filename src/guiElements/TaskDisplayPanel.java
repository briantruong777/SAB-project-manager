package guiElements;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import taskModel.Task;

public class TaskDisplayPanel extends JPanel
{

	Task task;
	JRadioButton rdbtnPlayButton;
	JRadioButton rdbtnPausebutton;
	JRadioButton rdbtnStopbutton;
	
	/**
	 * Create the panel.
	 */
	public TaskDisplayPanel(Task t)
	{
		task = t;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
//		JLabel lblTasknamelabel = new JLabel("Tester");
		JLabel lblTasknamelabel = new JLabel(t.getName());
		add(lblTasknamelabel);
		
		TaskStatusManager statusManager = new TaskStatusManager();
		
		rdbtnPlayButton = new JRadioButton(new ImageIcon("res/play.png"));
		rdbtnPlayButton.addActionListener(statusManager);
		rdbtnPlayButton.setActionCommand("Play");
		add(rdbtnPlayButton);
		
		rdbtnPausebutton = new JRadioButton(new ImageIcon("res/pause.png"));
		rdbtnPausebutton.addActionListener(statusManager);
		rdbtnPausebutton.setActionCommand("Pause");
		add(rdbtnPausebutton);
		
		rdbtnStopbutton = new JRadioButton(new ImageIcon("res/stop.png"));
		rdbtnStopbutton.addActionListener(statusManager);
		rdbtnStopbutton.setActionCommand("Stop");
		add(rdbtnStopbutton);
		
		taskStatusChange();
	}
	
	private void taskStatusChange()
	{
		switch (task.getStatus())
		{
			case ILLEGAL:
				rdbtnPlayButton.setEnabled(false);
				rdbtnPausebutton.setEnabled(false);
				rdbtnStopbutton.setEnabled(false);
				setBackground(Color.RED);
				break;
			case UNAVAILABLE:
				rdbtnPlayButton.setEnabled(false);
				rdbtnPausebutton.setEnabled(false);
				rdbtnStopbutton.setEnabled(false);
				setBackground(Color.DARK_GRAY);
				break;
			case INCOMPLETE:
				rdbtnPlayButton.setEnabled(true);
				rdbtnPausebutton.setEnabled(false);
				rdbtnStopbutton.setEnabled(false);
				setBackground(Color.BLUE);
				break;
			case WORKING:
				rdbtnPlayButton.setEnabled(false);
				rdbtnPausebutton.setEnabled(true);
				rdbtnStopbutton.setEnabled(true);
				setBackground(Color.GREEN);
				break;
			case PAUSED:
				rdbtnPlayButton.setEnabled(true);
				rdbtnPausebutton.setEnabled(false);
				rdbtnStopbutton.setEnabled(true);
				setBackground(Color.YELLOW);
				break;
			case COMPLETE:
				rdbtnPlayButton.setEnabled(false);
				rdbtnPausebutton.setEnabled(false);
				rdbtnStopbutton.setEnabled(false);
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
		
	}
}
