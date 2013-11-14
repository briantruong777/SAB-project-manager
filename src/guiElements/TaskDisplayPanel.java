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

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class TaskDisplayPanel extends JPanel
{

	Task task;
	JRadioButton mradioPlayButton;
	JRadioButton mradioPausebutton;
	JRadioButton mradioStopbutton;
	private JRadioButton mradioNotesButton;
	private JSeparator mseparator;
	private JRadioButton mradioLinkutton;
	
	/**
	 * Create the panel.
	 */
	public TaskDisplayPanel(Task task)
	{
		this.task = task;
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblTasknamelabel = new JLabel(task.getName());
//		JLabel lblTasknamelabel = new JLabel(t.getName());
		add(lblTasknamelabel);
		
//		TaskStatusManager statusManager = new TaskStatusManager();
		
		mradioPlayButton = new JRadioButton(new ImageIcon("res/play.png"));
//		mradioPlayButton.addActionListener(statusManager);
		mradioPlayButton.setActionCommand("Play");
		add(mradioPlayButton);
		
		mradioPausebutton = new JRadioButton(new ImageIcon("res/pause.png"));
//		mradioPausebutton.addActionListener(statusManager);
		mradioPausebutton.setActionCommand("Pause");
		add(mradioPausebutton);
		
		mradioStopbutton = new JRadioButton(new ImageIcon("res/stop.png"));
//		mradioStopbutton.addActionListener(statusManager);
		mradioStopbutton.setActionCommand("Stop");
		add(mradioStopbutton);
		
		mseparator = new JSeparator();
		mseparator.setOrientation(SwingConstants.VERTICAL);
		add(mseparator);

		mradioNotesButton = new JRadioButton(new ImageIcon("res/notes.png"));
		mradioStopbutton.setActionCommand("Notes");
		add(mradioNotesButton);
		
		mradioLinkutton = new JRadioButton(new ImageIcon("res/folder.png"));
		add(mradioLinkutton);
		
//		taskStatusChange();
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
