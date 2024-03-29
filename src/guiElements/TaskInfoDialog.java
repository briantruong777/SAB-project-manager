package guiElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;

import resourceModel.Inventory;
import resourceModel.Resource;
import resourceModel.ResourceConstraint;
import taskModel.Task;
import taskModel.TaskManager;

@SuppressWarnings("serial")
public class TaskInfoDialog extends JDialog
{
	private JTextField taskName;
	private JTextField foremanName;
	private JTextField builderName;
	private JTextField folderPath;
	private JSpinner toolSpinner;
	private JSpinner partSpinner;
	private JTextArea stepsTextArea;
	private JScrollPane stepsScroll;
	
	private JButton addTask;
	private JButton removeTask;
	private JButton addTool;
	private JButton removeTool;
	private JButton addPart;
	private JButton removePart;
	private JButton find;
	private JButton deleteButton;
	private JButton taskSteps;

	private JList<Task> taskList;
	private JList<Task> taskCstrList;
	private JList<Resource> toolList;
	private JList<ResourceConstraint> toolCstrList;
	private JList<Resource> partList;
	private JList<ResourceConstraint> partCstrList;
	
	private ArrayListModel<Task> taskModel;
	private ArrayListModel<Task> taskCstrModel;
	private ArrayListModel<Resource> toolModel;
	private ArrayListModel<ResourceConstraint> toolCstrModel;
	private ArrayListModel<Resource> partModel;
	private ArrayListModel<ResourceConstraint> partCstrModel;
	
	private JLabel startDate;
	private JLabel timeSpent;
	private JLabel endDate;
	
	private JFileChooser fileChooser;

	private static TaskInfoDialog dialog;
	private static Task task;
	private static Status status;
	private static boolean change;
	private static DateFormat format;
	private static String oldName;
	private static boolean nameChange;

	private enum Status
	{
		CREATE, EDIT, VIEW;
	}
	
	static
	{
		dialog = new TaskInfoDialog();
		format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		oldName = "";
		nameChange = false;
	}
	
	/**
	 * Create the dialog.
	 */
	public TaskInfoDialog()
	{
		setTitle("Task Contents");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setBounds(100, 100, 704, 550);
		
		getContentPane().setLayout(new BorderLayout());
		JPanel mcontentPanel = new JPanel();
		mcontentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(mcontentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 1.0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 45, 50, 30, 0, 0, 30, 0, 0, 0, 0};
		gbl_contentPanel.columnWidths = new int[]{0, 150, 0, 0, 150};
		mcontentPanel.setLayout(gbl_contentPanel);
		
		{
			JLabel taskNameLabel = new JLabel("Task Name");
			GridBagConstraints gbc_taskNameLabel = new GridBagConstraints();
			gbc_taskNameLabel.insets = new Insets(0, 0, 5, 5);
			gbc_taskNameLabel.anchor = GridBagConstraints.EAST;
			gbc_taskNameLabel.gridx = 0;
			gbc_taskNameLabel.gridy = 0;
			mcontentPanel.add(taskNameLabel, gbc_taskNameLabel);
		}
		{
			taskName = new JTextField();
			GridBagConstraints gbc_taskName = new GridBagConstraints();
			gbc_taskName.fill = GridBagConstraints.HORIZONTAL;
			gbc_taskName.insets = new Insets(0, 0, 5, 5);
			gbc_taskName.gridx = 1;
			gbc_taskName.gridy = 0;
			mcontentPanel.add(taskName, gbc_taskName);
		}
		{
			stepsTextArea = new JTextArea();
			stepsTextArea.setColumns(30);
			stepsTextArea.setRows(10);
			stepsTextArea.setLineWrap( true );
			stepsTextArea.setWrapStyleWord( true );
			stepsScroll = new JScrollPane(stepsTextArea);

			taskSteps = new JButton("Edit Task Steps");
			taskSteps.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String curStr = stepsTextArea.getText();
					if (JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog(dialog, stepsScroll, "Task Steps", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE))
					{
						stepsTextArea.setText(curStr);
					}
				}
			});
			GridBagConstraints gbc_taskSteps = new GridBagConstraints();
			gbc_taskSteps.fill = GridBagConstraints.HORIZONTAL;
			gbc_taskSteps.insets = new Insets(0, 0, 5, 5);
			gbc_taskSteps.gridx = 4;
			gbc_taskSteps.gridy = 0;
			mcontentPanel.add(taskSteps, gbc_taskSteps);
		}
		/*{
			JLabel builderLabel = new JLabel("Builder");
			GridBagConstraints gbc_builderLabel = new GridBagConstraints();
			gbc_builderLabel.anchor = GridBagConstraints.EAST;
			gbc_builderLabel.insets = new Insets(0, 0, 5, 5);
			gbc_builderLabel.gridx = 0;
			gbc_builderLabel.gridy = 1;
			mcontentPanel.add(builderLabel, gbc_builderLabel);
		}
		{
			builderName = new JTextField();
			GridBagConstraints gbc_builderName = new GridBagConstraints();
			gbc_builderName.fill = GridBagConstraints.HORIZONTAL;
			gbc_builderName.insets = new Insets(0, 0, 5, 5);
			gbc_builderName.gridx = 1;
			gbc_builderName.gridy = 1;
			mcontentPanel.add(builderName, gbc_builderName);
		}
		{
			JLabel foremanLabel = new JLabel("Foreman");
			GridBagConstraints gbc_foremanLabel = new GridBagConstraints();
			gbc_foremanLabel.anchor = GridBagConstraints.EAST;
			gbc_foremanLabel.insets = new Insets(0, 0, 5, 5);
			gbc_foremanLabel.gridx = 3;
			gbc_foremanLabel.gridy = 1;
			mcontentPanel.add(foremanLabel, gbc_foremanLabel);
		}
		{
			foremanName = new JTextField();
			GridBagConstraints gbc_foremanName = new GridBagConstraints();
			gbc_foremanName.insets = new Insets(0, 0, 5, 0);
			gbc_foremanName.fill = GridBagConstraints.HORIZONTAL;
			gbc_foremanName.gridx = 4;
			gbc_foremanName.gridy = 1;
			mcontentPanel.add(foremanName, gbc_foremanName);
		}*/
		{
			JLabel taskConstraintLabel = new JLabel("Task Constraint");
			GridBagConstraints gbc_taskConstraintLabel = new GridBagConstraints();
			gbc_taskConstraintLabel.gridheight = 2;
			gbc_taskConstraintLabel.insets = new Insets(0, 0, 5, 5);
			gbc_taskConstraintLabel.gridx = 0;
			gbc_taskConstraintLabel.gridy = 2;
			mcontentPanel.add(taskConstraintLabel, gbc_taskConstraintLabel);
		}
		{
			JScrollPane taskScroll = new JScrollPane();
			taskScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			GridBagConstraints gbc_taskScroll = new GridBagConstraints();
			gbc_taskScroll.fill = GridBagConstraints.BOTH;
			gbc_taskScroll.gridheight = 2;
			gbc_taskScroll.insets = new Insets(0, 0, 5, 5);
			gbc_taskScroll.gridx = 1;
			gbc_taskScroll.gridy = 2;
			mcontentPanel.add(taskScroll, gbc_taskScroll);
			{
				taskModel = new ArrayListModel<Task>();
				taskList = new JList<Task>(taskModel);
				taskList.addListSelectionListener(new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (taskList.getMinSelectionIndex() == -1)
							addTask.setEnabled(false);
						else
							addTask.setEnabled(true);
						repaint();
					}
				});
        taskList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        taskList.setSelectionModel(new SpecialListSelectionModel());
        MouseMotionListener[] ml = taskList.getMouseMotionListeners();
        taskList.removeMouseMotionListener(ml[0] instanceof BasicListUI.MouseInputHandler ? ml[0] : ml[1]);
        taskList.addMouseMotionListener(new SpecialMouseMotionListener());
				taskScroll.setViewportView(taskList);
			}
		}
		{
			addTask = new JButton("Add >>");
			addTask.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (Task t : taskList.getSelectedValuesList())
					{
						if (!taskCstrModel.contains(t))
						{
							taskCstrModel.add(t);
						}
					}
					taskCstrList.repaint();
				}
			});
			GridBagConstraints gbc_addTask = new GridBagConstraints();
			gbc_addTask.fill = GridBagConstraints.HORIZONTAL;
			gbc_addTask.gridwidth = 2;
			gbc_addTask.anchor = GridBagConstraints.SOUTH;
			gbc_addTask.insets = new Insets(0, 0, 5, 5);
			gbc_addTask.gridx = 2;
			gbc_addTask.gridy = 2;
			
			mcontentPanel.add(addTask, gbc_addTask);
		}
		{
			JScrollPane taskCstrScroll = new JScrollPane();
			GridBagConstraints gbc_taskCstrScroll = new GridBagConstraints();
			gbc_taskCstrScroll.fill = GridBagConstraints.BOTH;
			gbc_taskCstrScroll.gridheight = 2;
			gbc_taskCstrScroll.insets = new Insets(0, 0, 5, 0);
			gbc_taskCstrScroll.gridx = 4;
			gbc_taskCstrScroll.gridy = 2;
			mcontentPanel.add(taskCstrScroll, gbc_taskCstrScroll);
			{
				taskCstrModel = new ArrayListModel<Task>();
				taskCstrList = new JList<Task>(taskCstrModel);
				taskCstrList.addListSelectionListener(new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (taskCstrList.getMinSelectionIndex() == -1)
							removeTask.setEnabled(false);
						else
							removeTask.setEnabled(true);
						repaint();
					}
				});
        taskCstrList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        taskCstrList.setSelectionModel(new SpecialListSelectionModel());
        MouseMotionListener[] ml = taskCstrList.getMouseMotionListeners();
        taskCstrList.removeMouseMotionListener(ml[0] instanceof BasicListUI.MouseInputHandler ? ml[0] : ml[1]);
        taskCstrList.addMouseMotionListener(new SpecialMouseMotionListener());
				taskCstrScroll.setViewportView(taskCstrList);
			}
		}
		{
			removeTask = new JButton("Remove <<");
			removeTask.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (Task t : taskCstrList.getSelectedValuesList())
					{
						taskCstrModel.remove(t);
					}
					taskCstrList.repaint();
				}
			});
			GridBagConstraints gbc_removeTask = new GridBagConstraints();
			gbc_removeTask.gridwidth = 2;
			gbc_removeTask.anchor = GridBagConstraints.NORTH;
			gbc_removeTask.fill = GridBagConstraints.HORIZONTAL;
			gbc_removeTask.insets = new Insets(0, 0, 5, 5);
			gbc_removeTask.gridx = 2;
			gbc_removeTask.gridy = 3;
			mcontentPanel.add(removeTask, gbc_removeTask);
		}
		{
			JLabel toolConstraintLabel = new JLabel("Tool Constraint");
			GridBagConstraints gbc_toolConstraintLabel = new GridBagConstraints();
			gbc_toolConstraintLabel.gridheight = 3;
			gbc_toolConstraintLabel.insets = new Insets(0, 0, 5, 5);
			gbc_toolConstraintLabel.gridx = 0;
			gbc_toolConstraintLabel.gridy = 4;
			mcontentPanel.add(toolConstraintLabel, gbc_toolConstraintLabel);
		}
		{
			JScrollPane toolScroll = new JScrollPane();
			GridBagConstraints gbc_toolScroll = new GridBagConstraints();
			gbc_toolScroll.fill = GridBagConstraints.BOTH;
			gbc_toolScroll.gridheight = 3;
			gbc_toolScroll.insets = new Insets(0, 0, 5, 5);
			gbc_toolScroll.gridx = 1;
			gbc_toolScroll.gridy = 4;
			mcontentPanel.add(toolScroll, gbc_toolScroll);
			{
				toolModel = new ArrayListModel<Resource>();
				toolList = new JList<Resource>(toolModel);
				toolList.addListSelectionListener(new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (toolList.getMinSelectionIndex() == -1)
							addTool.setEnabled(false);
						else
							addTool.setEnabled(true);
						repaint();
					}
				});
				toolList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				toolList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						String str = "" + ((Resource)value).getMax() + ' ' + value;
						return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
					}
				});
        toolList.setSelectionModel(new SpecialListSelectionModel());
        MouseMotionListener[] ml = toolList.getMouseMotionListeners();
        toolList.removeMouseMotionListener(ml[0] instanceof BasicListUI.MouseInputHandler ? ml[0] : ml[1]);
        toolList.addMouseMotionListener(new SpecialMouseMotionListener());
				toolScroll.setViewportView(toolList);
			}
		}/*
		{
			JLabel toolAmount = new JLabel("Needed#");
			GridBagConstraints gbc_toolAmount = new GridBagConstraints();
			gbc_toolAmount.insets = new Insets(0, 0, 5, 5);
			gbc_toolAmount.anchor = GridBagConstraints.SOUTHEAST;
			gbc_toolAmount.gridx = 2;
			gbc_toolAmount.gridy = 4;
			mcontentPanel.add(toolAmount, gbc_toolAmount);
		}
		{
			toolSpinner = new JSpinner();
			toolSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			toolSpinner.setValue(1);
			toolSpinner.setToolTipText("Enter number of tools needed.");
			GridBagConstraints gbc_toolSpinner = new GridBagConstraints();
			gbc_toolSpinner.anchor = GridBagConstraints.SOUTH;
			gbc_toolSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_toolSpinner.insets = new Insets(0, 0, 5, 5);
			gbc_toolSpinner.gridx = 3;
			gbc_toolSpinner.gridy = 4;
			mcontentPanel.add(toolSpinner, gbc_toolSpinner);
		}*/
		{
			addTool = new JButton("Add >>");
			addTool.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					for (Resource r : toolList.getSelectedValuesList())
					{
						boolean addAsConstraint = true;	
						
						for (ResourceConstraint rc: toolCstrModel)
						{
							if (rc.getName().equals(r.getName()))
							{
								//rc.setAmount((Integer)toolSpinner.getValue());
								if (rc.getAmount() < r.getMax())
									rc.increment();
								else
									JOptionPane.showMessageDialog(addTool, "The entire quantity of this tool has already been added as constraint.", "", JOptionPane.ERROR_MESSAGE);
								//toolCstrList.repaint();
								//JOptionPane.showMessageDialog(addTool, "This tool has already been set as a constraint",
									//	"", JOptionPane.ERROR_MESSAGE);
								addAsConstraint = false;
								break;
							}								
						}
						if (addAsConstraint)
							toolCstrModel.add(new ResourceConstraint(r.getName()));//, (Integer)toolSpinner.getValue()));
						//else
							//JOptionPane.showMessageDialog(addTool, "This tool has already been set as a constraint", "", JOptionPane.ERROR_MESSAGE);
					}
					toolCstrList.repaint();
				}
			});
			{
				JScrollPane toolCstrScroll = new JScrollPane();
				GridBagConstraints gbc_toolCstrScroll = new GridBagConstraints();
				gbc_toolCstrScroll.fill = GridBagConstraints.BOTH;
				gbc_toolCstrScroll.gridheight = 3;
				gbc_toolCstrScroll.insets = new Insets(0, 0, 5, 0);
				gbc_toolCstrScroll.gridx = 4;
				gbc_toolCstrScroll.gridy = 4;
				mcontentPanel.add(toolCstrScroll, gbc_toolCstrScroll);
				{
					toolCstrModel = new ArrayListModel<ResourceConstraint>();
					toolCstrList = new JList<ResourceConstraint>(toolCstrModel);
					toolCstrList.addListSelectionListener(new ListSelectionListener()
					{
						@Override
						public void valueChanged(ListSelectionEvent e)
						{
							if (toolCstrList.getMinSelectionIndex() == -1)
								removeTool.setEnabled(false);
							else
								removeTool.setEnabled(true);
							repaint();
						}
					});
					toolCstrList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					toolCstrList.setCellRenderer(new DefaultListCellRenderer()
					{
						public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
						{
							ResourceConstraint rc = (ResourceConstraint)value;
							String str = "" + rc.getAmount() + '/' + Inventory.getTool(rc.getName()).getMax() + ' ' + value;
							return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
						}
					});
					toolCstrList.setSelectionModel(new SpecialListSelectionModel());
	        MouseMotionListener[] ml = toolCstrList.getMouseMotionListeners();
	        toolCstrList.removeMouseMotionListener(ml[0] instanceof BasicListUI.MouseInputHandler ? ml[0] : ml[1]);
	        toolCstrList.addMouseMotionListener(new SpecialMouseMotionListener());
					toolCstrScroll.setViewportView(toolCstrList);
				}
			}
			GridBagConstraints gbc_addTool = new GridBagConstraints();
			gbc_addTool.gridwidth = 2;
			gbc_addTool.fill = GridBagConstraints.HORIZONTAL;
			gbc_addTool.insets = new Insets(0, 0, 5, 5);
			gbc_addTool.gridx = 2;
			gbc_addTool.gridy = 5;
			mcontentPanel.add(addTool, gbc_addTool);
		}
		{
			removeTool = new JButton("Remove <<");
			removeTool.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (ResourceConstraint rc : toolCstrList.getSelectedValuesList())
					{
						toolCstrModel.remove(rc);
					}
					toolCstrList.repaint();
				}
			});
			GridBagConstraints gbc_removeTool = new GridBagConstraints();
			gbc_removeTool.anchor = GridBagConstraints.NORTH;
			gbc_removeTool.gridwidth = 2;
			gbc_removeTool.fill = GridBagConstraints.HORIZONTAL;
			gbc_removeTool.insets = new Insets(0, 0, 5, 5);
			gbc_removeTool.gridx = 2;
			gbc_removeTool.gridy = 6;
			mcontentPanel.add(removeTool, gbc_removeTool);
		}
		{
			JLabel partConstraintLabel = new JLabel("Part Constraint");
			GridBagConstraints gbc_partConstraintLabel = new GridBagConstraints();
			gbc_partConstraintLabel.gridheight = 3;
			gbc_partConstraintLabel.insets = new Insets(0, 0, 5, 5);
			gbc_partConstraintLabel.gridx = 0;
			gbc_partConstraintLabel.gridy = 7;
			mcontentPanel.add(partConstraintLabel, gbc_partConstraintLabel);
		}
		{
			JScrollPane partScroll = new JScrollPane();
			GridBagConstraints gbc_partScroll = new GridBagConstraints();
			gbc_partScroll.fill = GridBagConstraints.BOTH;
			gbc_partScroll.gridheight = 3;
			gbc_partScroll.insets = new Insets(0, 0, 5, 5);
			gbc_partScroll.gridx = 1;
			gbc_partScroll.gridy = 7;
			mcontentPanel.add(partScroll, gbc_partScroll);
			{
				partModel = new ArrayListModel<Resource>();
				partList = new JList<Resource>(partModel);
				partList.addListSelectionListener(new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (partList.getMinSelectionIndex() == -1)
							addPart.setEnabled(false);
						else
							addPart.setEnabled(true);
						repaint();
					}
				});
				partList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				partList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						String str = "" + ((Resource)value).getMax() + ' ' + value;
						return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
					}
				});
				partList.setSelectionModel(new SpecialListSelectionModel());
        MouseMotionListener[] ml = partList.getMouseMotionListeners();
        partList.removeMouseMotionListener(ml[0] instanceof BasicListUI.MouseInputHandler ? ml[0] : ml[1]);
				partList.addMouseMotionListener(new SpecialMouseMotionListener());
				partScroll.setViewportView(partList);
			}
		}/*
		{
			JLabel partAmount = new JLabel("Needed#");
			GridBagConstraints gbc_partAmount = new GridBagConstraints();
			gbc_partAmount.insets = new Insets(0, 0, 5, 5);
			gbc_partAmount.anchor = GridBagConstraints.SOUTHEAST;
			gbc_partAmount.gridx = 2;
			gbc_partAmount.gridy = 7;
			mcontentPanel.add(partAmount, gbc_partAmount);
		}
		{
			partSpinner = new JSpinner();
			partSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			partSpinner.setValue(1);
			partSpinner.setToolTipText("Enter number of parts needed.");
			GridBagConstraints gbc_partSpinner = new GridBagConstraints();
			gbc_partSpinner.anchor = GridBagConstraints.SOUTH;
			gbc_partSpinner.insets = new Insets(0, 0, 5, 5);
			gbc_partSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_partSpinner.gridx = 3;
			gbc_partSpinner.gridy = 7;
			mcontentPanel.add(partSpinner, gbc_partSpinner);
		}*/
		{
			JScrollPane partCstrScroll = new JScrollPane();
			GridBagConstraints gbc_partCstrScroll = new GridBagConstraints();
			gbc_partCstrScroll.fill = GridBagConstraints.BOTH;
			gbc_partCstrScroll.gridheight = 3;
			gbc_partCstrScroll.insets = new Insets(0, 0, 5, 0);
			gbc_partCstrScroll.gridx = 4;
			gbc_partCstrScroll.gridy = 7;
			mcontentPanel.add(partCstrScroll, gbc_partCstrScroll);
			{
				partCstrModel = new ArrayListModel<ResourceConstraint>();
				partCstrList = new JList<ResourceConstraint>(partCstrModel);
				partCstrList.addListSelectionListener(new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (partCstrList.getMinSelectionIndex() == -1)
							removePart.setEnabled(false);
						else
							removePart.setEnabled(true);
						repaint();
					}
				});
				partCstrList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				partCstrList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						ResourceConstraint rc = (ResourceConstraint)value;
						String str = "" + rc.getAmount() + '/' + Inventory.getPart(rc.getName()).getMax() + ' ' + value;
						return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
					}
				});
				partCstrList.setSelectionModel(new SpecialListSelectionModel());
        MouseMotionListener[] ml = partCstrList.getMouseMotionListeners();
        partCstrList.removeMouseMotionListener(ml[0] instanceof BasicListUI.MouseInputHandler ? ml[0] : ml[1]);
				partCstrList.addMouseMotionListener(new SpecialMouseMotionListener());
				partCstrScroll.setViewportView(partCstrList);
			}
		}
		{
			addPart = new JButton("Add >>");
			addPart.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (Resource r : partList.getSelectedValuesList())
					{
						boolean addAsConstraint = true;
						for (ResourceConstraint rc: partCstrModel)
						{
							if (rc.getName().equals(r.getName()))
							{
//								//rc.setAmount((Integer)toolSpinner.getValue());
								if (rc.getAmount() < r.getMax())
									rc.increment();
								else
									JOptionPane.showMessageDialog(addPart, "The entire quantity of this part has already been added as constraint.", "", JOptionPane.ERROR_MESSAGE);
								//toolCstrList.repaint();
								//JOptionPane.showMessageDialog(addTool, "This part has already been set as a constraint",
									//	"", JOptionPane.ERROR_MESSAGE);
								addAsConstraint = false;
							}
						}
						if (addAsConstraint)
							partCstrModel.add(new ResourceConstraint(r.getName()));// (Integer)partSpinner.getValue()));
						//else
							//JOptionPane.showMessageDialog(addPart, "This part has already been set as a constraint", "", JOptionPane.ERROR_MESSAGE);						
					}
					partCstrList.repaint();
				}
			});
			GridBagConstraints gbc_addPart = new GridBagConstraints();
			gbc_addPart.gridwidth = 2;
			gbc_addPart.fill = GridBagConstraints.HORIZONTAL;
			gbc_addPart.insets = new Insets(0, 0, 5, 5);
			gbc_addPart.gridx = 2;
			gbc_addPart.gridy = 8;
			mcontentPanel.add(addPart, gbc_addPart);
		}
		{
			removePart = new JButton("Remove <<");
			removePart.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (ResourceConstraint rc : partCstrList.getSelectedValuesList())
					{
						partCstrModel.remove(rc);
					}
					partCstrList.repaint();
				}
			});
			GridBagConstraints gbc_removePart = new GridBagConstraints();
			gbc_removePart.anchor = GridBagConstraints.NORTH;
			gbc_removePart.fill = GridBagConstraints.HORIZONTAL;
			gbc_removePart.gridwidth = 2;
			gbc_removePart.insets = new Insets(0, 0, 5, 5);
			gbc_removePart.gridx = 2;
			gbc_removePart.gridy = 9;
			mcontentPanel.add(removePart, gbc_removePart);
		}
		{
			JLabel folderLocationLabel = new JLabel("Folder Location");
			GridBagConstraints gbc_folderLocationLabel = new GridBagConstraints();
			gbc_folderLocationLabel.anchor = GridBagConstraints.EAST;
			gbc_folderLocationLabel.insets = new Insets(0, 0, 5, 5);
			gbc_folderLocationLabel.gridx = 0;
			gbc_folderLocationLabel.gridy = 10;
			mcontentPanel.add(folderLocationLabel, gbc_folderLocationLabel);
		}
		{
			folderPath = new JTextField();
			GridBagConstraints gbc_folderPath = new GridBagConstraints();
			gbc_folderPath.gridwidth = 3;
			gbc_folderPath.insets = new Insets(0, 0, 5, 5);
			gbc_folderPath.fill = GridBagConstraints.HORIZONTAL;
			gbc_folderPath.gridx = 1;
			gbc_folderPath.gridy = 10;
			mcontentPanel.add(folderPath, gbc_folderPath);
			folderPath.setColumns(10);
		}
		{
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			find = new JButton("Find...");
			find.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					switch(fileChooser.showOpenDialog(dialog))
					{
						case JFileChooser.APPROVE_OPTION:
							folderPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
							break;
					}
				}
			});
			GridBagConstraints gbc_find = new GridBagConstraints();
			gbc_find.anchor = GridBagConstraints.WEST;
			gbc_find.insets = new Insets(0, 0, 5, 0);
			gbc_find.gridx = 4;
			gbc_find.gridy = 10;
			mcontentPanel.add(find, gbc_find);
		}
		{
			startDate = new JLabel("Start: N/A");
			GridBagConstraints gbc_startDate = new GridBagConstraints();
			gbc_startDate.insets = new Insets(0, 0, 0, 5);
			gbc_startDate.gridx = 1;
			gbc_startDate.gridy = 11;
			mcontentPanel.add(startDate, gbc_startDate);
		}
		{
			timeSpent = new JLabel("Time: N/A");
			GridBagConstraints gbc_timeSpent = new GridBagConstraints();
			gbc_timeSpent.gridwidth = 2;
			gbc_timeSpent.insets = new Insets(0, 0, 0, 5);
			gbc_timeSpent.gridx = 2;
			gbc_timeSpent.gridy = 11;
			mcontentPanel.add(timeSpent, gbc_timeSpent);
		}
		{
			endDate = new JLabel("End: N/A");
			GridBagConstraints gbc_endDate = new GridBagConstraints();
			gbc_endDate.gridx = 4;
			gbc_endDate.gridy = 11;
			mcontentPanel.add(endDate, gbc_endDate);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						switch(status)
						{
							case CREATE:
								if (!checkNameLink())
									return;
								task = new Task(taskName.getText());
								task.addDependencies(taskCstrModel);
								task.addTools(toolCstrModel);
								task.addParts(partCstrModel);
								task.setPath(folderPath.getText());
								task.setSteps(stepsTextArea.getText());
								task.refreshStatus();
								change = true;
								break;
							case EDIT:
								change = !task.getName().equals(taskName.getText());
								if (change && !checkNameLink())
									return;
								if (change)
								{
									oldName = task.getName();
									nameChange = task.setName(taskName.getText());
								}
								/*if (!task.getBuilder().equals(builderName.getText()))
								{
									change = true;
									task.setBuilder(builderName.getText());
								}
								if (!task.getForeman().equals(foremanName.getText()))
								{
									change = true;
									task.setForeman(foremanName.getText());
								}*/
								if (!task.getPath().equals(folderPath.getText()))
								{
									change = true;
									task.setPath(folderPath.getText());
								}
								if (!task.getSteps().equals(stepsTextArea.getText()))
								{
									change = true;
									task.setSteps(stepsTextArea.getText());
								}

								if (task.getStatus() == Task.Status.UNAVAILABLE ||
										task.getStatus() == Task.Status.UNAVAILABLE_PAUSED ||
										task.getStatus() == Task.Status.UNSTARTED ||
										task.getStatus() == Task.Status.PAUSED)
								{
									if (!task.getDependencies().containsAll(taskCstrModel) || !taskCstrModel.containsAll(task.getDependencies()))
									{
										change = true;
										task.clearDependencies();
										task.addDependencies(taskCstrModel);
									}
									if (!task.getTools().containsAll(toolCstrModel) || !toolCstrModel.containsAll(task.getTools()))
									{
										change = true;
										task.clearTools();
										task.addTools(toolCstrModel);
									}
									if (!task.getParts().containsAll(partCstrModel) || !partCstrModel.containsAll(task.getParts()))
									{
										change = true;
										task.clearParts();
										task.addParts(partCstrModel);
									}
									task.refreshStatus();
								}
								break;
							case VIEW:
								task = null;
								change = false;
								break;
							default:
								break;
						}
						setVisible(false);
						Runner.notifyChange();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						change = false;
						task = null;
						setVisible(false);
					}
				});
				{
					deleteButton = new JButton("Delete");
					deleteButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{
							if (task.getDependers().size() == 0 ||
									JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(dialog, "Deleting this task removes it from the dependencies of other tasks. Delete this task?", "Delete Task", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE))
							{
								Collection<Task> deprs = task.getDependers();
								for (Task depr: deprs)
								{
									depr.removeDependency(task);
									depr.refreshStatus();
								}
								Runner.refreshTaskPanelTasks(deprs);
								task.clearDependencies();
								task.clearDependers();
								task.clearParts();
								task.clearTools();
								TaskManager.removeTask(task.getName());
								change = true;
								task = null;
								Runner.notifyChange();
								setVisible(false);
							}
						}
					});
					deleteButton.setVisible(false);
					buttonPane.add(deleteButton);
				}
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private boolean checkNameLink()
	{
		String name = taskName.getText(), path = folderPath.getText();
		if (name == null || "".equals(name))
		{
			JOptionPane.showMessageDialog(dialog, "Task name cannot be empty.", "", JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		else if (TaskManager.getTask(name) != null)
		{
			JOptionPane.showMessageDialog(dialog, "Task with same name already exists.", "", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if (path != null && !"".equals(path) && !(new File(path).exists()))
		{
			JOptionPane.showMessageDialog(dialog, "Cannot find given file.", "", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public static boolean showCreateDialog()
	{
		status = Status.CREATE;
		loadTask(null);
		enableText(true);
		enableLists(true);
		enableButtons(false);
		dialog.deleteButton.setVisible(false);
		dialog.setVisible(true);
		return change;
	}
	
	public static boolean showEditDialog(Task t)
	{
		status = Status.EDIT;
		loadTask(t);
		enableText(true);
		enableButtons(false);
		switch(t.getStatus())
		{
			case UNAVAILABLE:
			case UNAVAILABLE_PAUSED:
			case UNSTARTED:
			case PAUSED:
			case COMPLETE:
				enableLists(true);
				dialog.deleteButton.setVisible(true);
				break;
			case WORKING:
				enableLists(false);
				dialog.deleteButton.setVisible(false);
				break;
		}
		task = t;
		dialog.setVisible(true);
		return change;
	}
	
	private static void loadTask(Task t)
	{
		dialog.taskList.clearSelection();
		dialog.taskCstrList.clearSelection();
		dialog.taskModel.clear();
		dialog.taskModel.addAll(TaskManager.getTasks());
		Collections.sort(dialog.taskModel);
		dialog.taskCstrModel.clear();
		dialog.toolList.clearSelection();
		dialog.toolCstrList.clearSelection();
		dialog.toolModel.clear();
		dialog.toolModel.addAll(Inventory.getTools());
		Collections.sort(dialog.toolModel);
		dialog.toolCstrModel.clear();
		dialog.partList.clearSelection();
		dialog.partCstrList.clearSelection();
		dialog.partModel.clear();
		dialog.partModel.addAll(Inventory.getParts());
		Collections.sort(dialog.partModel);
		dialog.partCstrModel.clear();
		//dialog.toolSpinner.setValue(1);
		//dialog.toolSpinner.setValue(1);
		
		if (t == null)
		{
			dialog.stepsTextArea.setText("");
			dialog.taskName.setText("");
			//dialog.builderName.setText("");
			//dialog.foremanName.setText("");
			dialog.folderPath.setText("");
			dialog.startDate.setText("Start: N/A");
			dialog.timeSpent.setText("Time: N/A");
			dialog.endDate.setText("End: N/A");
		}
		else
		{
			dialog.stepsTextArea.setText(t.getSteps());
			dialog.taskName.setText(t.getName());
			/*if (t.getBuilder() == null)
				dialog.builderName.setText("");
			else
				dialog.builderName.setText(t.getBuilder());
			if (t.getForeman() == null)
				dialog.foremanName.setText("");
			else
				dialog.foremanName.setText(t.getForeman());*/
			if (t.getPath() == null)
				dialog.folderPath.setText("");
			else
				dialog.folderPath.setText(t.getPath());
			
			dialog.taskCstrModel.addAll(t.getDependencies());
			Collections.sort(dialog.taskCstrModel);
			dialog.toolCstrModel.addAll(t.getTools());
			Collections.sort(dialog.toolCstrModel);
			dialog.partCstrModel.addAll(t.getParts());
			Collections.sort(dialog.partCstrModel);
			
			if (t.getStartDate().isSet(Calendar.MINUTE))
				dialog.startDate.setText("Start: " + format.format(t.getStartDate().getTime()));
			else
				dialog.startDate.setText("Start: N/A");
			
			if (t.getTimeSpent() != 0)
			{
				long hrs = t.getTimeSpent() / 1000 / 60 / 60;
				long minutes = t.getTimeSpent() / 1000 / 60;
				//hrs %= 24;
				dialog.timeSpent.setText("Time: " + hrs + " hours, " + minutes + " minutes");
			}
			else
				dialog.timeSpent.setText("Time: N/A");
			
			if (t.getEndDate().isSet(Calendar.MINUTE))
				dialog.endDate.setText("End: " + format.format(t.getEndDate().getTime()));
			else
				dialog.endDate.setText("End: N/A");
		}
	}
	
	private static void enableText(boolean b)
	{
		//dialog.builderName.setEditable(b);
		//dialog.foremanName.setEditable(b);
		dialog.folderPath.setEditable(b);
		dialog.taskName.setEditable(b);
		dialog.find.setEnabled(b);
	}
	
	private static void enableLists(boolean b)
	{
		dialog.taskList.setEnabled(b);
		dialog.taskCstrList.setEnabled(b);
		dialog.toolList.setEnabled(b);
		dialog.toolCstrList.setEnabled(b);
		//dialog.toolSpinner.setEnabled(b);
		dialog.partList.setEnabled(b);
		dialog.partCstrList.setEnabled(b);
		//dialog.partSpinner.setEnabled(b);
	}
	
	private static void enableButtons(boolean b)
	{
		dialog.addPart.setEnabled(b);
		dialog.addTask.setEnabled(b);
		dialog.addTool.setEnabled(b);
		dialog.removePart.setEnabled(b);
		dialog.removeTask.setEnabled(b);
		dialog.removeTool.setEnabled(b);
	}
	
	public static Task getTask()
	{
		return task;
	}
	
	public static boolean nameChanged()
	{
		return nameChange;
	}
	
	public static String oldName()
	{
		return oldName;
	}
	
	private class SpecialListSelectionModel extends DefaultListSelectionModel
  {
    @Override
    public void setSelectionInterval(int index0, int index1)
    {
      if (index0 == index1)
      {
        if (super.isSelectedIndex(index0))
        {
          super.removeSelectionInterval(index0, index1);
        }
        else
        {
          super.addSelectionInterval(index0, index1);
        }
      }
    }
  }
	
	private class SpecialMouseMotionListener implements MouseMotionListener
  {
    @Override
    public void mouseDragged(MouseEvent arg0) {}
    @Override
    public void mouseMoved(MouseEvent arg0) {}
  }
}
