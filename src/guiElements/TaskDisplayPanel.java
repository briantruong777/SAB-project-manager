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
import javax.swing.JTextField;

import taskModel.Task;
import resourceModel.Resource;

@SuppressWarnings("serial")
public class TaskDisplayPanel extends JPanel implements ActionListener, MouseListener
{

	Task task;
	//private JButton incompleteButton;
	private JLabel statusLabel;
	private JLabel brokenResourceLabel;
	private JButton undoCompleteButton;
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
	private Component mhorizontalStrut_4;
	

	private JScrollPane textScroll;
	private JTextArea textArea;

	private JPanel fbDialogPanel;
	private JTextField builderTextField;
	private JTextField foremanTextField;

	private boolean adminViewEnabled;

	/**
	 * Create the panel.
	 */
	public TaskDisplayPanel(Task task, TaskPanel panel)
	{
		// Setting up textArea and textScroll
		textArea = new JTextArea();
		textArea.setColumns(30);
		textArea.setRows(10);
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		textScroll = new JScrollPane(textArea);

		// Setting up fbDialogPanel
		fbDialogPanel = new JPanel();
		builderTextField = new JTextField();
		foremanTextField = new JTextField();
		fbDialogPanel.setLayout(new BoxLayout(fbDialogPanel, BoxLayout.Y_AXIS));
		fbDialogPanel.add(new JLabel("Builder Name:"));
		fbDialogPanel.add(builderTextField);
		fbDialogPanel.add(new JLabel("Foreman Name:"));
		fbDialogPanel.add(foremanTextField);

		// Setting up this TaskDisplayPanel
		setMaximumSize(new Dimension(32767, 40));
		this.panel = panel;
		this.task = task;
		setAlignmentX(0);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		mhorizontalStrut_1 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_1);

		statusLabel = new JLabel(new ImageIcon("res/unstarted.png"), JLabel.LEFT);
		add(statusLabel);

		mhorizontalStrut_4 = Box.createHorizontalStrut(5);
		add(mhorizontalStrut_4);

		brokenResourceLabel = new JLabel(new ImageIcon("res/broken_part.png"), JLabel.LEFT);
		brokenResourceLabel.setVisible(false);
		add(brokenResourceLabel);

		mhorizontalStrut = Box.createHorizontalStrut(5);
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

		undoCompleteButton = new JButton(new ImageIcon("res/stop.png"));
		undoCompleteButton.addActionListener(this);
		undoCompleteButton.setActionCommand("UndoComplete");
		undoCompleteButton.setToolTipText("Undo-complete this task");
		add(undoCompleteButton);

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

		setAdminView(false);

		refreshTaskStatus();
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		Task.Status curTaskStatus = task.getStatus();
		System.out.println(command);
		if (command.equals("UndoComplete")) // Only possible when state is COMPLETE
		{
			// First change state of all dependers
			boolean haveWorkingDependers = false;
			for (Task t : task.getDependers())
			{
				if (t.getStatus() == Task.Status.WORKING)
				{
					haveWorkingDependers = true;
					break;
				}
			}
			if (haveWorkingDependers)
			{
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(panel, "Undo-completing this task\nwill set any Working\ntasks dependent on this one\nto the Paused state.\n\nDo you want to undo-complete this task and set relevant\ndependent tasks to the Paused state?", "Undo-complete this task?", JOptionPane.YES_NO_OPTION))
				{
					// Set all Working dependers to Paused so they can later be set to
					// Unavailable
					for (Task t : task.getDependers())
					{
						if (t.getStatus() == Task.Status.WORKING)
						{
							t.stop();
							t.pause();
							t.setStatus(Task.Status.PAUSED);
						}
					}
				}
				else
				{
					return;
				}
			}
			// Now undo-complete this task
			task.setStatus(Task.Status.PAUSED);
			task.setUndoCompleted(true);
			// Check if this Task can still be run after undo-completed
			task.refreshStatus();

			panel.refreshTasks(task.getDependers());
			Runner.notifyChange();
		}
		else if (command.equals("Paused")) // Only possible when state is WORKING
		{
			task.stop();
			task.pause();
			task.setStatus(Task.Status.PAUSED);
			Runner.notifyChange();
		}
		else if (command.equals("Working")) // state is PAUSED or UNSTARTED
		{
			// Getting foreman and builder name
			foremanTextField.setText("");
			builderTextField.setText("");
			while (foremanTextField.getText().length() == 0 ||
			       builderTextField.getText().length() == 0)
			{
				int option = JOptionPane.showConfirmDialog(this, fbDialogPanel,
					"Foreman and Builder Name", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.OK_OPTION)
				{
					if (foremanTextField.getText().length() == 0 ||
			       builderTextField.getText().length() == 0)
					{
						JOptionPane.showMessageDialog(this,
							"Foreman and Builder name must both be provided",
							"Foreman and Builder Name Issue", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					return;
				}
			}

			// Setting Task to Working state
			if (curTaskStatus == Task.Status.UNSTARTED)
			{
				task.begin();
			}

			task.start();
			task.resume(builderTextField.getText(), foremanTextField.getText());
			task.setStatus(Task.Status.WORKING);
			Runner.notifyChange();
		}
		else if (command.equals("Complete")) // Only possible when state is WORKING
		{
			task.stop();
			task.pause();
			task.finish();
			task.setStatus(Task.Status.COMPLETE);

			// Check if this task was undo-completed
			if (task.getUndoCompleted())
			{
				task.setUndoCompleted(false);
			}

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
		String toolTip;
		ArrayList<Resource> brokenTools;
		ArrayList<Resource> brokenParts;
		switch (task.getStatus())
		{
			case UNAVAILABLE:
				workingButton.setVisible(true);
				workingButton.setEnabled(false);
				pauseButton.setVisible(false);
				completeButton.setVisible(false);
				undoCompleteButton.setVisible(false);
				toolTip = "<html>Unavailable Task";

				if (task.checkDependenciesUndoCompleted())
				{
					ArrayList<Task> depsUndoCompleted =
						task.getDependenciesUndoCompleted();
					String undoCompleteTasksStr = " - " +
						depsUndoCompleted.get(0).getName();
					for (int i = 1; i < depsUndoCompleted.size(); i++)
					{
						undoCompleteTasksStr += "<br> - " +
							depsUndoCompleted.get(i).getName();
					}

					if (task.getUndoCompleted())
					{
						statusLabel.setIcon(new ImageIcon("res/unavailable_red_square.png"));
						toolTip += " (Undo-completed)<br><br>" +
							"Dependencies that were undo-completed:<br>" +
							undoCompleteTasksStr;
					}
					else
					{
						statusLabel.setIcon(new ImageIcon("res/unavailable_red.png"));
						toolTip += "<br><br>" +
							"Dependencies that were undo-completed:<br>" +
							undoCompleteTasksStr;
					}
				}
				else
				{
					statusLabel.setIcon(new ImageIcon("res/unavailable.png"));
				}
				statusLabel.setToolTipText(toolTip);

				brokenTools = task.getBrokenTools();
				brokenParts = task.getBrokenParts();
				if (brokenTools.size() > 0 || brokenParts.size() > 0)
				{
					String brokenToolTip = "<html>Some resource(s) are broken<br>";

					// Finding broken tools
					if (brokenTools.size() > 0)
					{
						String brokenToolsStr = " - " +
							brokenTools.get(0).getName();
						for (int i = 1; i < brokenTools.size(); i++)
						{
							brokenToolsStr += "<br> - " + brokenTools.get(i).getName();
						}

						brokenToolTip += "<br>Broken Tools:<br>" + brokenToolsStr;
					}
					// Finding broken parts
					if (brokenParts.size() > 0)
					{
						String brokenPartsStr = " - " +
							brokenParts.get(0).getName();
						for (int i = 1; i < brokenParts.size(); i++)
						{
							brokenPartsStr += "<br> - " + brokenParts.get(i).getName();
						}

						brokenToolTip += "<br>Broken Parts:<br>" + brokenPartsStr;
					}

					brokenResourceLabel.setVisible(true);
					brokenResourceLabel.setToolTipText(brokenToolTip);
				}
				else // if (brokenTools.size() > 0 || brokenParts.size() > 0)
				{
					brokenResourceLabel.setVisible(false);
				}
				break;
			case UNSTARTED:
				workingButton.setVisible(true);
				workingButton.setEnabled(true);
				pauseButton.setVisible(false);
				completeButton.setVisible(false);
				undoCompleteButton.setVisible(false);

				statusLabel.setIcon(new ImageIcon("res/unstarted.png"));
				statusLabel.setToolTipText("Unstarted Task");
				brokenResourceLabel.setVisible(false);
				break;
			case PAUSED:
				workingButton.setVisible(true);
				workingButton.setEnabled(true);
				pauseButton.setVisible(false);
				completeButton.setVisible(false);
				undoCompleteButton.setVisible(false);

				if (task.getUndoCompleted())
				{
					statusLabel.setIcon(new ImageIcon("res/pause_square.png"));
					statusLabel.setToolTipText("Paused Task (Undo-completed)");
				}
				else
				{
					statusLabel.setIcon(new ImageIcon("res/pause.png"));
					statusLabel.setToolTipText("Paused Task");
				}
				brokenResourceLabel.setVisible(false);
				break;
			case WORKING:
				workingButton.setVisible(false);
				pauseButton.setVisible(true);
				pauseButton.setEnabled(true);
				completeButton.setVisible(true);
				undoCompleteButton.setVisible(false);

				if (task.getUndoCompleted())
				{
					statusLabel.setIcon(new ImageIcon("res/work_square.png"));
					statusLabel.setToolTipText("Working Task (Undo-completed)");
				}
				else
				{
					statusLabel.setIcon(new ImageIcon("res/work.png"));
					statusLabel.setToolTipText("Working Task");
				}
				brokenResourceLabel.setVisible(false);
				break;
			case COMPLETE:
				workingButton.setVisible(false);
				pauseButton.setVisible(false);
				completeButton.setVisible(false);
				if (adminViewEnabled)
					undoCompleteButton.setVisible(true);
				else
					undoCompleteButton.setVisible(false);

				toolTip = "<html>Complete Task";

				if (task.checkDependenciesUndoCompleted())
				{
					statusLabel.setIcon(new ImageIcon("res/complete_red.png"));

					ArrayList<Task> depsUndoCompleted =
						task.getDependenciesUndoCompleted();
					String undoCompleteTasksStr = " - " +
						depsUndoCompleted.get(0).getName();
					for (int i = 1; i < depsUndoCompleted.size(); i++)
					{
						undoCompleteTasksStr += "<br> - " +
							depsUndoCompleted.get(i).getName();
					}
					toolTip += " (DEPENDENCY FAILURE)<br><br>" +
						"Dependencies that were undo-completed:<br>" +
						undoCompleteTasksStr;
				}
				else
				{
					statusLabel.setIcon(new ImageIcon("res/complete.png"));
				}
				statusLabel.setToolTipText(toolTip);

				brokenTools = task.getBrokenTools();
				brokenParts = task.getBrokenParts();
				if (brokenTools.size() > 0 || brokenParts.size() > 0)
				{
					String brokenToolTip = "<html>Some resource(s) are broken<br>";

					// Finding broken tools
					if (brokenTools.size() > 0)
					{
						String brokenToolsStr = " - " +
							brokenTools.get(0).getName();
						for (int i = 1; i < brokenTools.size(); i++)
						{
							brokenToolsStr += "<br> - " + brokenTools.get(i).getName();
						}

						brokenToolTip += "<br>Broken Tools:<br>" + brokenToolsStr;
					}
					// Finding broken parts
					if (brokenParts.size() > 0)
					{
						String brokenPartsStr = " - " +
							brokenParts.get(0).getName();
						for (int i = 1; i < brokenParts.size(); i++)
						{
							brokenPartsStr += "<br> - " + brokenParts.get(i).getName();
						}

						brokenToolTip += "<br>Broken Parts:<br>" + brokenPartsStr;
					}

					brokenResourceLabel.setVisible(true);
					brokenResourceLabel.setToolTipText(brokenToolTip);
				}
				else // if (brokenTools.size() > 0 || brokenParts.size() > 0)
				{
					brokenResourceLabel.setVisible(false);
				}
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

	/**
	 * Sets admin view by showing/hiding certain elements
	 */
	public void setAdminView(boolean val)
	{
		editButton.setVisible(val);
		adminViewEnabled = val;
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
}
