package guiElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resourceModel.Inventory;
import resourceModel.Resource;
import resourceModel.ResourceConstraint;
import taskModel.Task;
import taskModel.TaskManager;

@SuppressWarnings("serial")
public class TaskInfoDialog extends JDialog
{
	private JTextField mtextTask;
	private JTextField mtextForeman;
	private JTextField mtextBuilder;
	private JSpinner toolNumSpinner;
	private JSpinner partNumSpinner;
	private JTextField mtextLink;
	
	private JButton btnAddTask;
	private JButton btnRemoveTask;
	private JButton btnAddTool;
	private JButton btnRemoveTool;
	private JButton btnAddPart;
	private JButton btnRemovePart;
	private JButton btnFindLink;

	private JList<Task> taskList;
	private JList<Task> taskSelectList;
	private JList<Resource> toolList;
	private JList<ResourceConstraint> toolSelectList;
	private JList<Resource> partList;
	private JList<ResourceConstraint> partSelectList;
	
	private ArrayListModel<Task> taskModel;
	private ArrayListModel<Task> taskSelectModel;
	private ArrayListModel<Resource> toolModel;
	private ArrayListModel<ResourceConstraint> toolSelectModel;
	private ArrayListModel<Resource> partModel;
	private ArrayListModel<ResourceConstraint> partSelectModel;
	
	private JLabel lblStartDate;
	private JLabel lblTimeSpent;
	private JLabel lblEndDate;
	
	private JFileChooser fileChooser;

	private static TaskInfoDialog dialog;
	private static Task task;
	private static Status status;
	private static boolean change;

	private enum Status
	{
		CREATE, RUN, EDIT, VIEW;
	}
	
	static
	{
		dialog = new TaskInfoDialog();
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
		setBounds(100, 100, 606, 511);
		
		getContentPane().setLayout(new BorderLayout());
		JPanel mcontentPanel = new JPanel();
		mcontentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(mcontentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 45, 50, 30, 0, 0, 30, 0, 0, 0, 0};
		gbl_contentPanel.columnWidths = new int[]{0, 150, 0, 0, 150};
		mcontentPanel.setLayout(gbl_contentPanel);
		
		{
			JLabel mlblTaskName = new JLabel("Task Name");
			GridBagConstraints gbc_mlblTaskName = new GridBagConstraints();
			gbc_mlblTaskName.insets = new Insets(0, 0, 5, 5);
			gbc_mlblTaskName.anchor = GridBagConstraints.EAST;
			gbc_mlblTaskName.gridx = 0;
			gbc_mlblTaskName.gridy = 0;
			mcontentPanel.add(mlblTaskName, gbc_mlblTaskName);
		}
		{
			mtextTask = new JTextField();
			GridBagConstraints gbc_textTask = new GridBagConstraints();
			gbc_textTask.fill = GridBagConstraints.HORIZONTAL;
			gbc_textTask.insets = new Insets(0, 0, 5, 5);
			gbc_textTask.gridx = 1;
			gbc_textTask.gridy = 0;
			mcontentPanel.add(mtextTask, gbc_textTask);
		}
		{
			JLabel lblBuilder = new JLabel("Builder");
			GridBagConstraints gbc_lblBuilder = new GridBagConstraints();
			gbc_lblBuilder.anchor = GridBagConstraints.EAST;
			gbc_lblBuilder.insets = new Insets(0, 0, 5, 5);
			gbc_lblBuilder.gridx = 0;
			gbc_lblBuilder.gridy = 1;
			mcontentPanel.add(lblBuilder, gbc_lblBuilder);
		}
		{
			mtextBuilder = new JTextField();
			GridBagConstraints gbc_textBuilder = new GridBagConstraints();
			gbc_textBuilder.fill = GridBagConstraints.HORIZONTAL;
			gbc_textBuilder.insets = new Insets(0, 0, 5, 5);
			gbc_textBuilder.gridx = 1;
			gbc_textBuilder.gridy = 1;
			mcontentPanel.add(mtextBuilder, gbc_textBuilder);
		}
		{
			JLabel lblForeman = new JLabel("Foreman");
			GridBagConstraints gbc_lblForeman = new GridBagConstraints();
			gbc_lblForeman.anchor = GridBagConstraints.EAST;
			gbc_lblForeman.insets = new Insets(0, 0, 5, 5);
			gbc_lblForeman.gridx = 3;
			gbc_lblForeman.gridy = 1;
			mcontentPanel.add(lblForeman, gbc_lblForeman);
		}
		{
			mtextForeman = new JTextField();
			GridBagConstraints gbc_textForeman = new GridBagConstraints();
			gbc_textForeman.insets = new Insets(0, 0, 5, 0);
			gbc_textForeman.fill = GridBagConstraints.HORIZONTAL;
			gbc_textForeman.gridx = 4;
			gbc_textForeman.gridy = 1;
			mcontentPanel.add(mtextForeman, gbc_textForeman);
		}
		{
			JLabel lblTaskConstraint = new JLabel("Task Constraint");
			GridBagConstraints gbc_lblTaskConstraint = new GridBagConstraints();
			gbc_lblTaskConstraint.gridheight = 2;
			gbc_lblTaskConstraint.insets = new Insets(0, 0, 5, 5);
			gbc_lblTaskConstraint.gridx = 0;
			gbc_lblTaskConstraint.gridy = 2;
			mcontentPanel.add(lblTaskConstraint, gbc_lblTaskConstraint);
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
				taskList.addListSelectionListener(new ListButtonEnabler(btnAddTask));
				taskScroll.setViewportView(taskList);
				taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			btnAddTask = new JButton("Add >>");
			btnAddTask.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (!taskSelectModel.contains(taskList.getSelectedValue()))
					{
						taskSelectModel.add(taskList.getSelectedValue());
						taskSelectList.repaint();
					}
				}
			});
			GridBagConstraints gbc_btnAddTask = new GridBagConstraints();
			gbc_btnAddTask.gridwidth = 2;
			gbc_btnAddTask.anchor = GridBagConstraints.SOUTH;
			gbc_btnAddTask.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnAddTask.insets = new Insets(0, 0, 5, 5);
			gbc_btnAddTask.gridx = 2;
			gbc_btnAddTask.gridy = 2;
			
			mcontentPanel.add(btnAddTask, gbc_btnAddTask);
		}
		{
			JScrollPane taskSelectScroll = new JScrollPane();
			GridBagConstraints gbc_taskSelectScroll = new GridBagConstraints();
			gbc_taskSelectScroll.fill = GridBagConstraints.BOTH;
			gbc_taskSelectScroll.gridheight = 2;
			gbc_taskSelectScroll.insets = new Insets(0, 0, 5, 0);
			gbc_taskSelectScroll.gridx = 4;
			gbc_taskSelectScroll.gridy = 2;
			mcontentPanel.add(taskSelectScroll, gbc_taskSelectScroll);
			{
				taskSelectModel = new ArrayListModel<Task>();
				taskSelectList = new JList<Task>(taskSelectModel);
				taskSelectList.addListSelectionListener(new ListButtonEnabler(btnRemoveTask));
				taskSelectScroll.setViewportView(taskSelectList);
				taskSelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			btnRemoveTask = new JButton("Remove <<");
			btnRemoveTask.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (taskSelectList.getMinSelectionIndex() >= 0)
					{
						taskSelectList.remove(taskSelectList.getMinSelectionIndex());
						taskSelectList.clearSelection();
					}
					taskSelectList.repaint();
				}
			});
			GridBagConstraints gbc_btnRemoveTask = new GridBagConstraints();
			gbc_btnRemoveTask.gridwidth = 2;
			gbc_btnRemoveTask.anchor = GridBagConstraints.NORTH;
			gbc_btnRemoveTask.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnRemoveTask.insets = new Insets(0, 0, 5, 5);
			gbc_btnRemoveTask.gridx = 2;
			gbc_btnRemoveTask.gridy = 3;
			mcontentPanel.add(btnRemoveTask, gbc_btnRemoveTask);
		}
		{
			JLabel lblToolConstraint = new JLabel("Tool Constraint");
			GridBagConstraints gbc_lblToolConstraint = new GridBagConstraints();
			gbc_lblToolConstraint.gridheight = 3;
			gbc_lblToolConstraint.insets = new Insets(0, 0, 5, 5);
			gbc_lblToolConstraint.gridx = 0;
			gbc_lblToolConstraint.gridy = 4;
			mcontentPanel.add(lblToolConstraint, gbc_lblToolConstraint);
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
				toolList.addListSelectionListener(new ListButtonEnabler(btnAddTool));
				toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				toolList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						String str = "" + ((Resource)value).getMax() + ' ' + value;
						return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
					}
				});
				toolScroll.setViewportView(toolList);
			}
		}
		{
			JLabel toolAmount = new JLabel("Needed#");
			GridBagConstraints gbc_toolAmount = new GridBagConstraints();
			gbc_toolAmount.insets = new Insets(0, 0, 5, 5);
			gbc_toolAmount.anchor = GridBagConstraints.EAST;
			gbc_toolAmount.gridx = 2;
			gbc_toolAmount.gridy = 4;
			mcontentPanel.add(toolAmount, gbc_toolAmount);
		}
		{
			toolNumSpinner = new JSpinner();
			toolNumSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			toolNumSpinner.setValue(1);
			toolNumSpinner.setToolTipText("Enter number of tools needed.");
			GridBagConstraints gbc_toolNumSpinner = new GridBagConstraints();
			gbc_toolNumSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_toolNumSpinner.insets = new Insets(0, 0, 5, 5);
			gbc_toolNumSpinner.gridx = 3;
			gbc_toolNumSpinner.gridy = 4;
			mcontentPanel.add(toolNumSpinner, gbc_toolNumSpinner);
		}
		{
			btnAddTool = new JButton("Add >>");
			btnAddTool.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					Object obj = toolList.getSelectedValue();
					int i = toolSelectModel.indexOf(obj);
					if (i == -1)
						toolSelectModel.add(new ResourceConstraint(obj.toString(), (Integer)toolNumSpinner.getValue()));
					else
						toolSelectModel.getElementAt(i).setAmount((Integer)toolNumSpinner.getValue());
					toolSelectList.repaint();
				}
			});
			{
				JScrollPane toolSelectScroll = new JScrollPane();
				GridBagConstraints gbc_toolSelectScroll = new GridBagConstraints();
				gbc_toolSelectScroll.fill = GridBagConstraints.BOTH;
				gbc_toolSelectScroll.gridheight = 3;
				gbc_toolSelectScroll.insets = new Insets(0, 0, 5, 0);
				gbc_toolSelectScroll.gridx = 4;
				gbc_toolSelectScroll.gridy = 4;
				mcontentPanel.add(toolSelectScroll, gbc_toolSelectScroll);
				{
					toolSelectModel = new ArrayListModel<ResourceConstraint>();
					toolSelectList = new JList<ResourceConstraint>(toolSelectModel);
					toolSelectList.addListSelectionListener(new ListButtonEnabler(btnRemoveTool));
					toolSelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					toolSelectList.setCellRenderer(new DefaultListCellRenderer()
					{
						public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
						{
							ResourceConstraint rc = (ResourceConstraint)value;
							String str = "" + rc.getAmount() + '/' + Inventory.getTool(rc.getName()).getMax() + ' ' + value;
							return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
						}
					});
					toolSelectScroll.setViewportView(toolSelectList);
				}
			}
			GridBagConstraints gbc_btnAddTool = new GridBagConstraints();
			gbc_btnAddTool.gridwidth = 2;
			gbc_btnAddTool.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnAddTool.insets = new Insets(0, 0, 5, 5);
			gbc_btnAddTool.gridx = 2;
			gbc_btnAddTool.gridy = 5;
			mcontentPanel.add(btnAddTool, gbc_btnAddTool);
		}
		{
			btnRemoveTool = new JButton("Remove <<");
			btnRemoveTool.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (toolSelectList.getMinSelectionIndex() >= 0)
					{
						toolSelectList.remove(toolSelectList.getMinSelectionIndex());
						toolSelectList.clearSelection();
					}
					toolSelectList.repaint();
				}
			});
			GridBagConstraints gbc_btnRemoveTool = new GridBagConstraints();
			gbc_btnRemoveTool.gridwidth = 2;
			gbc_btnRemoveTool.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnRemoveTool.insets = new Insets(0, 0, 5, 5);
			gbc_btnRemoveTool.gridx = 2;
			gbc_btnRemoveTool.gridy = 6;
			mcontentPanel.add(btnRemoveTool, gbc_btnRemoveTool);
		}
		{
			JLabel lblPartConstraint = new JLabel("Part Constraint");
			GridBagConstraints gbc_lblPartConstraint = new GridBagConstraints();
			gbc_lblPartConstraint.gridheight = 3;
			gbc_lblPartConstraint.insets = new Insets(0, 0, 5, 5);
			gbc_lblPartConstraint.gridx = 0;
			gbc_lblPartConstraint.gridy = 7;
			mcontentPanel.add(lblPartConstraint, gbc_lblPartConstraint);
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
				partList.addListSelectionListener(new ListButtonEnabler(btnAddPart));
				partList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				partList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						String str = "" + ((Resource)value).getMax() + ' ' + value;
						return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
					}
				});
				partScroll.setViewportView(partList);
			}
		}
		{
			JLabel partAmount = new JLabel("Needed#");
			GridBagConstraints gbc_partAmount = new GridBagConstraints();
			gbc_partAmount.insets = new Insets(0, 0, 5, 5);
			gbc_partAmount.anchor = GridBagConstraints.EAST;
			gbc_partAmount.gridx = 2;
			gbc_partAmount.gridy = 7;
			mcontentPanel.add(partAmount, gbc_partAmount);
		}
		{
			partNumSpinner = new JSpinner();
			partNumSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
			partNumSpinner.setValue(1);
			partNumSpinner.setToolTipText("Enter number of parts needed.");
			GridBagConstraints gbc_partNumSpinner = new GridBagConstraints();
			gbc_partNumSpinner.insets = new Insets(0, 0, 5, 5);
			gbc_partNumSpinner.fill = GridBagConstraints.HORIZONTAL;
			gbc_partNumSpinner.gridx = 3;
			gbc_partNumSpinner.gridy = 7;
			mcontentPanel.add(partNumSpinner, gbc_partNumSpinner);
		}
		{
			JScrollPane partSelectScroll = new JScrollPane();
			GridBagConstraints gbc_partSelectScroll = new GridBagConstraints();
			gbc_partSelectScroll.fill = GridBagConstraints.BOTH;
			gbc_partSelectScroll.gridheight = 3;
			gbc_partSelectScroll.insets = new Insets(0, 0, 5, 0);
			gbc_partSelectScroll.gridx = 4;
			gbc_partSelectScroll.gridy = 7;
			mcontentPanel.add(partSelectScroll, gbc_partSelectScroll);
			{
				partSelectModel = new ArrayListModel<ResourceConstraint>();
				partSelectList = new JList<ResourceConstraint>(partSelectModel);
				partSelectList.addListSelectionListener(new ListButtonEnabler(btnRemovePart));
				partSelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				partSelectList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						ResourceConstraint rc = (ResourceConstraint)value;
						String str = "" + rc.getAmount() + '/' + Inventory.getPart(rc.getName()).getMax() + ' ' + value;
						return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
					}
				});
				partSelectScroll.setViewportView(partSelectList);
			}
		}
		{
			btnAddPart = new JButton("Add >>");
			btnAddPart.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Object obj = partList.getSelectedValue();
					int i = partSelectModel.indexOf(obj);
					if (i == -1)
						partSelectModel.add(new ResourceConstraint(obj.toString(), (Integer)partNumSpinner.getValue()));
					else
						((ResourceConstraint) partSelectModel.getElementAt(i)).setAmount((Integer)partNumSpinner.getValue());
					partSelectList.repaint();
				}
			});
			GridBagConstraints gbc_btnAddPart = new GridBagConstraints();
			gbc_btnAddPart.gridwidth = 2;
			gbc_btnAddPart.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnAddPart.insets = new Insets(0, 0, 5, 5);
			gbc_btnAddPart.gridx = 2;
			gbc_btnAddPart.gridy = 8;
			mcontentPanel.add(btnAddPart, gbc_btnAddPart);
		}
		{
			btnRemovePart = new JButton("Remove <<");
			btnRemovePart.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (partSelectList.getMinSelectionIndex() >= 0)
					{
						partSelectList.remove(partSelectList.getMinSelectionIndex());
						partSelectList.clearSelection();
					}
					partSelectList.repaint();
				}
			});
			GridBagConstraints gbc_btnRemove = new GridBagConstraints();
			gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnRemove.gridwidth = 2;
			gbc_btnRemove.insets = new Insets(0, 0, 5, 5);
			gbc_btnRemove.gridx = 2;
			gbc_btnRemove.gridy = 9;
			mcontentPanel.add(btnRemovePart, gbc_btnRemove);
		}
		{
			JLabel mlblFolderLocation = new JLabel("Folder Location");
			GridBagConstraints gbc_mlblFolderLocation = new GridBagConstraints();
			gbc_mlblFolderLocation.anchor = GridBagConstraints.EAST;
			gbc_mlblFolderLocation.insets = new Insets(0, 0, 5, 5);
			gbc_mlblFolderLocation.gridx = 0;
			gbc_mlblFolderLocation.gridy = 10;
			mcontentPanel.add(mlblFolderLocation, gbc_mlblFolderLocation);
		}
		{
			mtextLink = new JTextField();
			GridBagConstraints gbc_textLink = new GridBagConstraints();
			gbc_textLink.gridwidth = 3;
			gbc_textLink.insets = new Insets(0, 0, 5, 5);
			gbc_textLink.fill = GridBagConstraints.HORIZONTAL;
			gbc_textLink.gridx = 1;
			gbc_textLink.gridy = 10;
			mcontentPanel.add(mtextLink, gbc_textLink);
			mtextLink.setColumns(10);
		}
		{
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			btnFindLink = new JButton("Find...");
			btnFindLink.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					switch(fileChooser.showOpenDialog(null))
					{
						case JFileChooser.APPROVE_OPTION:
							mtextLink.setText(fileChooser.getSelectedFile().toString());
							break;
					}
				}
			});
			GridBagConstraints gbc_btnFindLink = new GridBagConstraints();
			gbc_btnFindLink.anchor = GridBagConstraints.WEST;
			gbc_btnFindLink.insets = new Insets(0, 0, 5, 0);
			gbc_btnFindLink.gridx = 4;
			gbc_btnFindLink.gridy = 10;
			mcontentPanel.add(btnFindLink, gbc_btnFindLink);
		}
		{
			lblStartDate = new JLabel("Start Date: N/A");
			GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
			gbc_lblStartDate.insets = new Insets(0, 0, 0, 5);
			gbc_lblStartDate.gridx = 1;
			gbc_lblStartDate.gridy = 11;
			mcontentPanel.add(lblStartDate, gbc_lblStartDate);
		}
		{
			lblTimeSpent = new JLabel("Time Spent: N/A");
			GridBagConstraints gbc_lblTimeSpent = new GridBagConstraints();
			gbc_lblTimeSpent.gridwidth = 2;
			gbc_lblTimeSpent.insets = new Insets(0, 0, 0, 5);
			gbc_lblTimeSpent.gridx = 2;
			gbc_lblTimeSpent.gridy = 11;
			mcontentPanel.add(lblTimeSpent, gbc_lblTimeSpent);
		}
		{
			lblEndDate = new JLabel("End Date: N/A");
			GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
			gbc_lblEndDate.gridx = 4;
			gbc_lblEndDate.gridy = 11;
			mcontentPanel.add(lblEndDate, gbc_lblEndDate);
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
								break;
							case RUN:
								break;
							case EDIT:
								break;
							case VIEW:
								task = null;
								change = false;
								break;
							default:
								break;
						}
						setVisible(false);
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
						task = null;
						change = false;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public static boolean showCreateDialog()
	{
		status = Status.CREATE;
		loadTask(null);
		enableText(true);
		enableLists(true);
		enableButtons(false);
		dialog.setVisible(true);
		return change;
	}
	
	public static boolean showRunDialog(Task t)
	{
		status = Status.RUN;
		loadTask(t);
		enableText(true);
		// cannot change dependencies when trying to start task
		enableLists(false);
		enableButtons(false);
		task = t;
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
			case INCOMPLETE:
				enableLists(true);
				break;
			case STOPPED:
			case WORKING:
			case PAUSED:
			case COMPLETE:
				enableLists(false);
				break;
		}
		task = t;
		dialog.setVisible(true);
		return change;
	}
	
	public static void showViewDialog(Task t)
	{
		status = Status.VIEW;
		loadTask(t);
		enableText(false);
		enableLists(false);
		enableButtons(false);
		dialog.setVisible(true);
	}
	
	private static void loadTask(Task t)
	{
		dialog.taskList.clearSelection();
		dialog.taskSelectList.clearSelection();
		dialog.taskModel.clear();
		dialog.taskModel.addAll(TaskManager.getTasks());
		dialog.taskSelectModel.clear();
		dialog.toolList.clearSelection();
		dialog.toolSelectList.clearSelection();
		dialog.toolModel.clear();
		dialog.toolModel.addAll(Inventory.getTools());
		dialog.toolSelectModel.clear();
		dialog.partList.clearSelection();
		dialog.partSelectList.clearSelection();
		dialog.partModel.clear();
		dialog.partModel.addAll(Inventory.getParts());
		dialog.partSelectModel.clear();
		dialog.toolNumSpinner.setValue(1);
		dialog.toolNumSpinner.setValue(1);
		
		if (t == null)
		{
			dialog.mtextTask.setText("");
			dialog.mtextBuilder.setText("");
			dialog.mtextBuilder.setText("");
			dialog.mtextLink.setText("");
			dialog.lblStartDate.setText("Start Date: N/A");
			dialog.lblTimeSpent.setText("Time Spent: N/A");
			dialog.lblEndDate.setText("End Date: N/A");
		}
		else
		{
			dialog.mtextTask.setText(t.getName());
			if (t.getBuilder() == null)
				dialog.mtextBuilder.setText("");
			else
				dialog.mtextBuilder.setText(t.getBuilder());
			if (t.getForeman() == null)
				dialog.mtextBuilder.setText("");
			else
				dialog.mtextBuilder.setText(t.getForeman());
			if (t.getPath() == null)
				dialog.mtextLink.setText("");
			else
				dialog.mtextLink.setText(t.getPath());
			
			dialog.taskSelectModel.addAll(t.getDependencies());
			dialog.toolSelectModel.addAll(t.getTools());
			dialog.partSelectModel.addAll(t.getParts());
			
			//!!! set date/time
		}
	}
	
	private static void enableText(boolean b)
	{
		dialog.mtextBuilder.setEditable(b);
		dialog.mtextForeman.setEditable(b);
		dialog.mtextLink.setEditable(b);
		dialog.mtextTask.setEditable(b);
		dialog.btnFindLink.setEnabled(b);
	}
	
	private static void enableLists(boolean b)
	{
		dialog.taskList.setEnabled(b);
		dialog.taskSelectList.setEnabled(b);
		dialog.toolList.setEnabled(b);
		dialog.toolSelectList.setEnabled(b);
		dialog.toolNumSpinner.setEnabled(b);
		dialog.partList.setEnabled(b);
		dialog.partSelectList.setEnabled(b);
		dialog.partNumSpinner.setEnabled(b);
	}
	
	private static void enableButtons(boolean b)
	{
		dialog.btnAddPart.setEnabled(b);
		dialog.btnAddTask.setEnabled(b);
		dialog.btnAddTool.setEnabled(b);
		dialog.btnRemovePart.setEnabled(b);
		dialog.btnRemoveTask.setEnabled(b);
		dialog.btnRemoveTool.setEnabled(b);
	}
	
	public static Task getTask()
	{
		return task;
	}
	
	private class ListButtonEnabler implements ListSelectionListener
	{
		private JButton target;
		
		public ListButtonEnabler(JButton target)
		{
			this.target = target;
		}
		@Override
		public void valueChanged(ListSelectionEvent arg0)
		{
			if (arg0.getFirstIndex() >= 0)
				target.setEnabled(true);
			else
				target.setEnabled(false);
			target.repaint();
		}
	}
}
