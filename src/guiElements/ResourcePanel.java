package guiElements;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resourceModel.BrokenReport;
import resourceModel.Inventory;
import resourceModel.Resource;
import taskModel.Task;
import taskModel.TaskManager;

@SuppressWarnings("serial")
public class ResourcePanel extends JPanel
{
	private JList<Resource> toolList;
	private JList<Resource> partList;
	private ArrayListModel<Resource> toolModel;
	private ArrayListModel<Resource> partModel;
	
	private JTextField toolName;
	private JTextField partName;
	private JSpinner toolSpinner;
	private JSpinner partSpinner;
	
	private JButton toolChange;
	private JButton partChange;
	private JButton toolRemove;
	private JButton partRemove;
	private JButton toolMarkBroken;
	private JButton partMarkBroken;

  private JPanel fbDialogPanel;
  private JTextField builderTextField;
  private JTextField foremanTextField;
  
  private ResourcePanel thisPanel;
	
	/**
	 * Create the panel.
	 */
	public ResourcePanel()
	{
	  thisPanel = this;
	  
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				toolList.clearSelection();
				partList.clearSelection();
			}
		});
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_panel);
		
		JLabel toolLabel = new JLabel("Tools (Available/Max)");
		GridBagConstraints gbc_toolLabel = new GridBagConstraints();
		gbc_toolLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolLabel.gridwidth = 3;
		gbc_toolLabel.insets = new Insets(5, 5, 5, 5);
		gbc_toolLabel.gridx = 0;
		gbc_toolLabel.gridy = 0;
		add(toolLabel, gbc_toolLabel);
		
		JLabel partLabel = new JLabel("Parts");
		GridBagConstraints gbc_partLabel = new GridBagConstraints();
		gbc_partLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_partLabel.gridwidth = 3;
		gbc_partLabel.insets = new Insets(5, 5, 5, 5);
		gbc_partLabel.gridx = 3;
		gbc_partLabel.gridy = 0;
		add(partLabel, gbc_partLabel);
		
		JScrollPane toolScroll = new JScrollPane();
		GridBagConstraints gbc_toolScroll = new GridBagConstraints();
		gbc_toolScroll.fill = GridBagConstraints.BOTH;
		gbc_toolScroll.weightx = 1.0;
		gbc_toolScroll.gridheight = 7;
		gbc_toolScroll.insets = new Insets(5, 5, 5, 5);
		gbc_toolScroll.gridx = 0;
		gbc_toolScroll.gridy = 1;
		add(toolScroll, gbc_toolScroll);
		
		toolModel = new ArrayListModel<Resource>();
		toolList = new JList<Resource>(toolModel);
		toolList.setCellRenderer(new DefaultListCellRenderer()
		{
			public Component getListCellRendererComponent(JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
			{
				Resource r = (Resource)value; 
				String s = "" + r.getAvailable() + '/' + r.getMax() + ' ' + r +
					(r.isBroken() ? " (Broken)" : "");
				return super.getListCellRendererComponent(list, s, index, isSelected, cellHasFocus);
			}
		});
		toolList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				Resource r = toolList.getSelectedValue();
				// nothing is selected
				if (r == null)
					unpickTool();
				else
					pickTool(r);
			}
		});
		toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		toolScroll.setViewportView(toolList);
		
		JScrollPane partScroll = new JScrollPane();
		GridBagConstraints gbc_partScroll = new GridBagConstraints();
		gbc_partScroll.weightx = 1.0;
		gbc_partScroll.fill = GridBagConstraints.BOTH;
		gbc_partScroll.gridheight = 7;
		gbc_partScroll.insets = new Insets(5, 5, 5, 5);
		gbc_partScroll.gridx = 3;
		gbc_partScroll.gridy = 1;
		add(partScroll, gbc_partScroll);
		
		partModel = new ArrayListModel<Resource>();
		partList = new JList<Resource>(partModel);
		partList.setCellRenderer(new DefaultListCellRenderer()
		{
			public Component getListCellRendererComponent(JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
			{
				Resource r = (Resource)value; 
				String s = "" + r.getAvailable() + '/' + r.getMax() + ' ' + r +
					(r.isBroken() ? " (Broken)" : "");
				return super.getListCellRendererComponent(list, s, index, isSelected, cellHasFocus);
			}
		});
		partList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				Resource r = partList.getSelectedValue();
				// nothing is selected
				if (r == null)
					unpickPart();
				else
					pickPart(r);
			}
		});
		partList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		partScroll.setViewportView(partList);
		
		JLabel toolNameLabel = new JLabel("Name");
		GridBagConstraints gbc_toolNameLabel = new GridBagConstraints();
		gbc_toolNameLabel.anchor = GridBagConstraints.EAST;
		gbc_toolNameLabel.insets = new Insets(5, 5, 5, 5);
		gbc_toolNameLabel.gridx = 1;
		gbc_toolNameLabel.gridy = 2;
		add(toolNameLabel, gbc_toolNameLabel);
		
		toolName = new JTextField();
		toolName.setColumns(10);
		GridBagConstraints gbc_toolName = new GridBagConstraints();
		gbc_toolName.weightx = 1.0;
		gbc_toolName.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolName.insets = new Insets(5, 5, 5, 5);
		gbc_toolName.gridx = 2;
		gbc_toolName.gridy = 2;
		add(toolName, gbc_toolName);
		
		JLabel partNameLabel = new JLabel("Name");
		GridBagConstraints gbc_partNameLabel = new GridBagConstraints();
		gbc_partNameLabel.anchor = GridBagConstraints.EAST;
		gbc_partNameLabel.insets = new Insets(5, 5, 5, 5);
		gbc_partNameLabel.gridx = 4;
		gbc_partNameLabel.gridy = 2;
		add(partNameLabel, gbc_partNameLabel);
		
		partName = new JTextField();
		partName.setColumns(10);
		GridBagConstraints gbc_partName = new GridBagConstraints();
		gbc_partName.weightx = 1.0;
		gbc_partName.fill = GridBagConstraints.HORIZONTAL;
		gbc_partName.insets = new Insets(5, 5, 5, 5);
		gbc_partName.gridx = 5;
		gbc_partName.gridy = 2;
		add(partName, gbc_partName);
		
		JLabel toolMaxLabel = new JLabel("Max#");
		GridBagConstraints gbc_toolMaxLabel = new GridBagConstraints();
		gbc_toolMaxLabel.insets = new Insets(5, 5, 5, 5);
		gbc_toolMaxLabel.gridx = 1;
		gbc_toolMaxLabel.gridy = 3;
		add(toolMaxLabel, gbc_toolMaxLabel);
		
		toolSpinner = new JSpinner();
		toolSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_toolSpinner = new GridBagConstraints();
		gbc_toolSpinner.weightx = 1.0;
		gbc_toolSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolSpinner.insets = new Insets(5, 5, 5, 5);
		gbc_toolSpinner.gridx = 2;
		gbc_toolSpinner.gridy = 3;
		add(toolSpinner, gbc_toolSpinner);
		partSpinner = new JSpinner();
		partSpinner.setValue(1);
		/*
		JLabel partMaxLabel = new JLabel("Max#");
		GridBagConstraints gbc_partMaxLabel = new GridBagConstraints();
		gbc_partMaxLabel.insets = new Insets(5, 5, 5, 5);
		gbc_partMaxLabel.gridx = 4;
		gbc_partMaxLabel.gridy = 3;
		add(partMaxLabel, gbc_partMaxLabel);
		
		partSpinner = new JSpinner();
		partSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_partSpinner = new GridBagConstraints();
		gbc_partSpinner.weightx = 1.0;
		gbc_partSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_partSpinner.insets = new Insets(5, 5, 5, 5);
		gbc_partSpinner.gridx = 5;
		gbc_partSpinner.gridy = 3;
		add(partSpinner, gbc_partSpinner);*/
		
		JButton toolAdd = new JButton("Add");
		toolAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ("".equals(toolName.getText()))
				{
					JOptionPane.showMessageDialog(null, "Tool is not named.", "", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				Resource newTool = new Resource(toolName.getText(), (Integer)toolSpinner.getValue());
				int i = toolModel.indexOf(newTool);
				
				// does not contain given resource
				if (i == -1)
				{
					toolModel.add(newTool);
					Inventory.addTool(newTool);
				}
				else
				{
					Resource existTool = toolModel.get(i);
					existTool.setMax(existTool.getMax() + newTool.getMax());
					existTool.setAvailable(existTool.getAvailable() + newTool.getMax());
					for (Task t: existTool.getDependers())
						t.refreshStatus();
					toolModel.notifyChanged(i);
				}
				toolList.clearSelection();
				unpickTool();
				Runner.notifyChange();
			}
		});
		GridBagConstraints gbc_toolAdd = new GridBagConstraints();
		gbc_toolAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolAdd.gridwidth = 2;
		gbc_toolAdd.insets = new Insets(5, 5, 5, 5);
		gbc_toolAdd.gridx = 1;
		gbc_toolAdd.gridy = 4;
		add(toolAdd, gbc_toolAdd);
		
		JButton partAdd = new JButton("Add");
		partAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ("".equals(partName.getText()))
				{
					JOptionPane.showMessageDialog(null, "Part is not named.", "", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				Resource newPart = new Resource(partName.getText(), 1);//(Integer)partSpinner.getValue());
				int i = partModel.indexOf(newPart);
				
				// does not contain given resource
				if (i == -1)
				{
					partModel.add(newPart);
					Inventory.addPart(newPart);
				}
				else
				{
					/*
					Resource existPart = partModel.get(i);
					existPart.setMax(existPart.getMax() + newPart.getMax());
					existPart.setAvailable(existPart.getAvailable() + newPart.getMax());
					for (Task t: existPart.getDependers())
						t.refreshStatus();
					partModel.notifyChanged(i);*/
					JOptionPane.showMessageDialog(null, "Part with that name already exists", "", JOptionPane.ERROR_MESSAGE);					
				}
				partList.clearSelection();
				unpickPart();
				Runner.notifyChange();
			}
		});
		GridBagConstraints gbc_partAdd = new GridBagConstraints();
		gbc_partAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_partAdd.gridwidth = 2;
		gbc_partAdd.insets = new Insets(5, 5, 5, 5);
		gbc_partAdd.gridx = 4;
		gbc_partAdd.gridy = 4;
		add(partAdd, gbc_partAdd);
		
		toolChange = new JButton("Change");
		toolChange.setEnabled(false);
		toolChange.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = toolList.getSelectedValue();
				String text = toolName.getText();
				if ("".equals(text))
				{
					JOptionPane.showMessageDialog(null, "Name of tool cannot be blank.", "", JOptionPane.ERROR_MESSAGE);
					toolName.setText(r.getName());
					repaint();
					return;
				}
				if (!r.getName().equals(text) && toolModel.contains(new Resource(text)))
				{
					JOptionPane.showMessageDialog(null, "There is already a tool with that name.", "", JOptionPane.ERROR_MESSAGE);
					toolName.setText(r.getName());
					repaint();
					return;
				}
				
				int amount = r.getAvailable() + (Integer)toolSpinner.getValue() - r.getMax();
				if (amount < 0)
				{
					JOptionPane.showMessageDialog(null, "Please stop the task whose tool has issues.", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (r.hasDepender())
				{
					JOptionPane.showMessageDialog(null, "There are tasks that need this tool so it cannot be changed", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					ArrayList<Task> refreshTasks = new ArrayList<Task>();
					Inventory.removeTool(r.getName());
					r.setName(text);
					for (Task t: r.getDependers())
					{
						t.renameTool(r.getName(), text);
						refreshTasks.add(t);
					}
					r.setMax((Integer)toolSpinner.getValue());
					r.setAvailable(amount);
					toolModel.notifyChanged(toolList.getMinSelectionIndex());
					Inventory.addTool(r);
					Runner.refreshTaskPanelTasks(refreshTasks);
					repaint();
					Runner.notifyChange();
				}
			}
		});
		GridBagConstraints gbc_toolChange = new GridBagConstraints();
		gbc_toolChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolChange.gridwidth = 2;
		gbc_toolChange.insets = new Insets(5, 5, 5, 5);
		gbc_toolChange.gridx = 1;
		gbc_toolChange.gridy = 5;
		add(toolChange, gbc_toolChange);
		
		partChange = new JButton("Change");
		partChange.setEnabled(false);
		partChange.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = partList.getSelectedValue();
				String text = partName.getText();
				if ("".equals(text))
				{
					JOptionPane.showMessageDialog(null, "Name of tool cannot be blank.", "", JOptionPane.ERROR_MESSAGE);
					toolName.setText(r.getName());
					repaint();
					return;
				}
				if (!r.getName().equals(text) && partModel.contains(new Resource(text)))
				{
					JOptionPane.showMessageDialog(null, "There is already a part with that name.", "", JOptionPane.ERROR_MESSAGE);
					partName.setText(r.getName());
					repaint();
					return;
				}
				int amount = r.getAvailable() + (Integer)partSpinner.getValue() - r.getMax();
				if (amount < 0)
				{
					JOptionPane.showMessageDialog(null, "Please stop the task whose part has issues.", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (r.hasDepender())
				{
					JOptionPane.showMessageDialog(null, "There are tasks that need this part so it cannot be changed", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					ArrayList<Task> refreshTasks = new ArrayList<Task>();
					Inventory.removePart(r.getName());
					for (Task t: r.getDependers())
					{
						t.renamePart(r.getName(), text);
						refreshTasks.add(t);
					}
					r.setName(text);
					r.setMax((Integer)partSpinner.getValue());
					r.setAvailable(amount);
					partModel.notifyChanged(partList.getMinSelectionIndex());
					Inventory.addPart(r);
					Runner.refreshTaskPanelTasks(refreshTasks);
					repaint();
					Runner.notifyChange();
				}
			}
		});
		GridBagConstraints gbc_partChange = new GridBagConstraints();
		gbc_partChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_partChange.gridwidth = 2;
		gbc_partChange.insets = new Insets(5, 5, 5, 5);
		gbc_partChange.gridx = 4;
		gbc_partChange.gridy = 5;
		add(partChange, gbc_partChange);
		
		toolRemove = new JButton("Remove");
		toolRemove.setEnabled(false);
		toolRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = toolList.getSelectedValue();
				if (r.hasDepender())
				{
					JOptionPane.showMessageDialog(null, "There are tasks that need this tool. Change max# to 0 if all tools of this type have issues.", "", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				toolList.clearSelection();
				toolModel.remove(r);
				Inventory.removeTool(r.getName());
				Runner.notifyChange();
			}
		});
		GridBagConstraints gbc_toolRemove = new GridBagConstraints();
		gbc_toolRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolRemove.gridwidth = 2;
		gbc_toolRemove.insets = new Insets(5, 5, 5, 5);
		gbc_toolRemove.gridx = 1;
		gbc_toolRemove.gridy = 6;
		add(toolRemove, gbc_toolRemove);
		
		partRemove = new JButton("Remove");
		partRemove.setEnabled(false);
		partRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = partList.getSelectedValue();
				if (r.hasDepender())
				{
					JOptionPane.showMessageDialog(null, "There are tasks that need this part. Change max# to 0 if all parts of this type have issues.", "", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				partList.clearSelection();
				partModel.remove(r);
				Inventory.removePart(r.getName());
				Runner.notifyChange();
			}
		});
		GridBagConstraints gbc_partRemove = new GridBagConstraints();
		gbc_partRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_partRemove.gridwidth = 2;
		gbc_partRemove.insets = new Insets(5, 5, 5, 5);
		gbc_partRemove.gridx = 4;
		gbc_partRemove.gridy = 6;
		add(partRemove, gbc_partRemove);

		toolMarkBroken = new JButton("Mark Broken");
		toolMarkBroken.setEnabled(false);
		toolMarkBroken.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = toolList.getSelectedValue();

				if (r.isBroken())
				{
					BrokenReport report = r.getLatestReport();
					report.markAsFixed(Calendar.getInstance());
					toolMarkBroken.setText("Mark Broken");
					r.setBroken(false);
					Runner.refreshTaskPanelTasks(r.getDependers());
					Runner.notifyChange();
				}
				else
				{
					// If any working tasks, ask to pause them
					ArrayList<Task> workingDependers = new ArrayList<Task>();
					for (Task t : r.getDependers())
					{
						if (t.getStatus() == Task.Status.WORKING)
						{
							workingDependers.add(t);
						}
					}
					if (workingDependers.size() > 0)
					{
						String confirmStr = "<html>Marking this tool as broken will set the following Working tasks to the Paused state:";
						confirmStr += "<br> - " + workingDependers.get(0);
						for (int i = 1; i < workingDependers.size(); i++)
							confirmStr += "<br> - " + workingDependers.get(i);
						confirmStr += "<br><br>Do you want to mark this tool as broken and set the above tasks to the Paused state?";
						if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, confirmStr, "Mark this tool as broken?", JOptionPane.YES_NO_OPTION))
						{
							// Set all dependers to Paused so they can later be set to
							// Unavailable
							for (Task t : r.getDependers())
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
					
					String dispString = "Have the correct people been contacted?\n" + Runner.getContact();
					int option = JOptionPane.showConfirmDialog(thisPanel, dispString, "Confirm the following",
					    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.NO_OPTION)
					{
					  JOptionPane.showMessageDialog(thisPanel, "Please contact the correct people");
					  return;
					}
					else if (option == JOptionPane.CANCEL_OPTION)
					{
					  return;
					}
					
					// Getting foreman and builder name
          foremanTextField.setText("");
          builderTextField.setText("");
          while (foremanTextField.getText().length() == 0 ||
                 builderTextField.getText().length() == 0)
          {
            option = JOptionPane.showConfirmDialog(thisPanel, fbDialogPanel,
              "Foreman and Builder Name", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.OK_OPTION)
            {
              if (foremanTextField.getText().length() == 0 ||
                 builderTextField.getText().length() == 0)
              {
                JOptionPane.showMessageDialog(thisPanel,
                  "Foreman and Builder name must both be provided",
                  "Foreman and Builder Name Issue", JOptionPane.ERROR_MESSAGE);
              }
            }
            else
            {
              return;
            }
          }
          JPanel taskComboPanel = new JPanel();
          taskComboPanel.setLayout(new BoxLayout(taskComboPanel, BoxLayout.Y_AXIS));
          ArrayList taskArrayList = TaskManager.getSortedTasks();
          Task[] taskArray = new Task[taskArrayList.size()];
          taskArrayList.toArray(taskArray);
          //generate array of strings
          JComboBox<Task> taskComboBox = new JComboBox<Task>(taskArray);
          taskComboPanel.add(new JLabel("Select the task the part was broken on"));
          taskComboPanel.add(taskComboBox);
          int result = JOptionPane.showConfirmDialog(thisPanel, taskComboPanel, "Select a Task",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (result != JOptionPane.OK_OPTION)
            return;
					
					JScrollPane textScroll;
          JTextArea textArea;
          textArea = new JTextArea();
          textArea.setColumns(30);
          textArea.setRows(10);
          textArea.setLineWrap( true );
          textArea.setWrapStyleWord( true );
          textScroll = new JScrollPane(textArea);
          String text = "";
          textArea.setText(text);
          textArea.setEditable(true);
          option = JOptionPane.showConfirmDialog(toolMarkBroken, textScroll, "Broken Resource Report", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
          if (option == JOptionPane.OK_OPTION)
            r.addReport(textArea.getText(), Calendar.getInstance(), builderTextField.getText(),
                foremanTextField.getText(), taskComboBox.getSelectedItem() != null ? taskComboBox.getSelectedItem().toString() : "No Task");
          else
            return;
          
					r.setBroken(true);
					toolMarkBroken.setText("Unmark Broken");
					Runner.refreshTaskPanelTasks(r.getDependers());
					Runner.notifyChange();
				}

				repaint();
			}
		});
		GridBagConstraints gbc_toolMarkBroken = new GridBagConstraints();
		gbc_toolMarkBroken.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolMarkBroken.gridwidth = 2;
		gbc_toolMarkBroken.insets = new Insets(5, 5, 5, 5);
		gbc_toolMarkBroken.gridx = 1;
		gbc_toolMarkBroken.gridy = 7;
		add(toolMarkBroken, gbc_toolMarkBroken);

		partMarkBroken = new JButton("Mark Broken");
		partMarkBroken.setEnabled(false);
		partMarkBroken.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = partList.getSelectedValue();

				if (r.isBroken())
				{
				  BrokenReport report = r.getLatestReport();
				  report.markAsFixed(Calendar.getInstance());
					r.setBroken(false);
					partMarkBroken.setText("Mark Broken");
					Runner.refreshTaskPanelTasks(r.getDependers());
					Runner.notifyChange();
				}
				else
				{
					// If any working tasks, ask to pause them
					boolean haveWorkingDependers = false;
					for (Task t : r.getDependers())
					{
						if (t.getStatus() == Task.Status.WORKING)
						{
							haveWorkingDependers = true;
							break;
						}
					}
					if (haveWorkingDependers)
					{
						if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Marking this part as broken\nwill set any Working\ntasks dependent on it\nto the Paused state.\n\nDo you want to mark this part as broken and set relevant\ndependent tasks to the Paused state?", "Mark this part as broken?", JOptionPane.YES_NO_OPTION))
						{
							  
							// Set all dependers to Paused so they can later be set to
							// Unavailable
							for (Task t : r.getDependers())
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
					
					String dispString = "Have the correct people been contacted?\n" + Runner.getContact();
          int option = JOptionPane.showConfirmDialog(thisPanel, dispString, "Confirm the following",
              JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (option == JOptionPane.NO_OPTION)
          {
            JOptionPane.showMessageDialog(thisPanel, "Please contact the correct people");
            return;
          }
          else if (option == JOptionPane.CANCEL_OPTION)
          {
            return;
          }
					
					// Getting foreman and builder name
		      foremanTextField.setText("");
		      builderTextField.setText("");
		      while (foremanTextField.getText().length() == 0 ||
		             builderTextField.getText().length() == 0)
		      {
		        option = JOptionPane.showConfirmDialog(thisPanel, fbDialogPanel,
		          "Foreman and Builder Name", JOptionPane.OK_CANCEL_OPTION,
		          JOptionPane.QUESTION_MESSAGE);
		        if (option == JOptionPane.OK_OPTION)
		        {
		          if (foremanTextField.getText().length() == 0 ||
		             builderTextField.getText().length() == 0)
		          {
		            JOptionPane.showMessageDialog(thisPanel,
		              "Foreman and Builder name must both be provided",
		              "Foreman and Builder Name Issue", JOptionPane.ERROR_MESSAGE);
		          }
		        }
		        else
		        {
		          return;
		        }
		      }
		      JPanel taskComboPanel = new JPanel();
          taskComboPanel.setLayout(new BoxLayout(taskComboPanel, BoxLayout.Y_AXIS));
		      ArrayList taskArrayList = TaskManager.getSortedTasks();
		      Task[] taskArray = new Task[taskArrayList.size()];
		      taskArrayList.toArray(taskArray);
		      //generate array of strings
		      JComboBox<Task> taskComboBox = new JComboBox<Task>(taskArray);
		      taskComboPanel.add(new JLabel("Select the task the part was broken on"));
		      taskComboPanel.add(taskComboBox);
		      int result = JOptionPane.showConfirmDialog(thisPanel, taskComboPanel, "Select a Task",
		          JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		      if (result != JOptionPane.OK_OPTION)
		        return;
		      
					JScrollPane textScroll;
          JTextArea textArea;
          textArea = new JTextArea();
          textArea.setColumns(30);
          textArea.setRows(10);
          textArea.setLineWrap( true );
          textArea.setWrapStyleWord( true );
          textScroll = new JScrollPane(textArea);
          String text = "";
          textArea.setText(text);
          textArea.setEditable(true);
          option = JOptionPane.showConfirmDialog(partMarkBroken, textScroll, "Broken Resource Report", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
          if (option == JOptionPane.OK_OPTION)
            r.addReport(textArea.getText(), Calendar.getInstance(), builderTextField.getText(),
                foremanTextField.getText(), taskComboBox.getSelectedItem() != null ? taskComboBox.getSelectedItem().toString() : "No Task");
          else
            return;
          
					r.setBroken(true);
					partMarkBroken.setText("Unmark Broken");
					Runner.refreshTaskPanelTasks(r.getDependers());
					Runner.notifyChange();
				}

				repaint();
			}
		});
		GridBagConstraints gbc_partMarkBroken = new GridBagConstraints();
		gbc_partMarkBroken.fill = GridBagConstraints.HORIZONTAL;
		gbc_partMarkBroken.gridwidth = 2;
		gbc_partMarkBroken.insets = new Insets(5, 5, 5, 5);
		gbc_partMarkBroken.gridx = 4;
		gbc_partMarkBroken.gridy = 7;
		add(partMarkBroken, gbc_partMarkBroken);
		
		// Setting up fbDialogPanel
    fbDialogPanel = new JPanel();
    builderTextField = new JTextField();
    foremanTextField = new JTextField();
    fbDialogPanel.setLayout(new BoxLayout(fbDialogPanel, BoxLayout.Y_AXIS));
    fbDialogPanel.add(new JLabel("Builder Name:"));
    fbDialogPanel.add(builderTextField);
    fbDialogPanel.add(new JLabel("Foreman Name:"));
    fbDialogPanel.add(foremanTextField);
	}
	
	private void unpickTool()
	{
		toolName.setText("");
		toolSpinner.setValue(1);
		toolChange.setEnabled(false);
		toolRemove.setEnabled(false);
		toolMarkBroken.setEnabled(false);
		repaint();
	}
	
	private void pickTool(Resource r)
	{
		toolName.setText(r.getName());
		toolSpinner.setValue(r.getMax());
		toolChange.setEnabled(true);
		toolRemove.setEnabled(true);
		toolMarkBroken.setEnabled(true);
		toolMarkBroken.setText(r.isBroken() ? "Unmark Broken" : "Mark Broken");
		partList.clearSelection();
		repaint();
	}
	
	private void unpickPart()
	{
		partName.setText("");
		//partSpinner.setValue(1);
		partChange.setEnabled(false);
		partRemove.setEnabled(false);
		partMarkBroken.setEnabled(false);
		repaint();
	}
	
	private void pickPart(Resource r)
	{
		partName.setText(r.getName());
		//partSpinner.setValue(r.getMax());
		partChange.setEnabled(true);
		partRemove.setEnabled(true);
		partMarkBroken.setEnabled(true);
		partMarkBroken.setText(r.isBroken() ? "Unmark Broken" : "Mark Broken");
		toolList.clearSelection();
		repaint();
	}

	public void clearInventory()
	{
		toolList.clearSelection();
		partList.clearSelection();
		toolModel.clear();
		partModel.clear();
	}
	
	public void reloadInventory()
	{
		clearInventory();
		toolModel.addAll(Inventory.getTools());
		Collections.sort(toolModel);
		partModel.addAll(Inventory.getParts());
		Collections.sort(partModel);
		repaint();
	}
}
