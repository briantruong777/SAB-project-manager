package guiElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resourceModel.ResourceConstraint;
import resourceModel.Widget;
import taskModel.Task;

public class TaskInfoDialog extends JDialog
{
	private final JPanel mcontentPanel = new JPanel();
	private JTextField mtextTask;
	private JTextField mtextForeman;
	private JTextField mtextBuilder;
	private JTextField mtextToolNum;
	private JTextField mtextPartNum;
	private JTextField mtextLink;
	
	private JButton btnAddTask;
	private JButton btnRemoveTask;
	private JButton btnAddTool;
	private JButton btnRemoveTool;
	private JButton btnAddPart;
	private JButton btnRemovePart;

	private JList<Task> taskList;
	private JList<Task> taskSelectList;
	private JList<Widget> toolList;
	private JList<Widget> toolSelectList;
	private JList<Widget> partList;
	private JList<Widget> partSelectList;
	private JTextField mtextStatus;
	
	private JFileChooser fileChooser;
	private Task curTask;
	
	/**
	 * Create the dialog.
	 */
	public TaskInfoDialog()
	{
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setBounds(100, 100, 552, 511);
		
		getContentPane().setLayout(new BorderLayout());
		mcontentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(mcontentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 45, 50, 30, 0, 0, 30, 0, 0, 0, 0, 0, 0};
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
				taskList = new JList<Task>(new ArrayListModel<Task>());
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
					ArrayListModel<Task> selectModel = (ArrayListModel<Task>) taskSelectList.getModel();
					if (taskList.getSelectedValue() != null && selectModel.indexOf(taskList.getSelectedValue()) == -1)
					{
						selectModel.add(taskList.getSelectedValue());
						taskSelectList.repaint();
					}
					updateStatus("");
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
				taskSelectList = new JList<Task>(new ArrayListModel<Task>());
				taskSelectList.addListSelectionListener(new ListButtonEnabler(btnRemoveTask));
				taskSelectScroll.setViewportView(taskSelectList);
				taskSelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			btnRemoveTask = new JButton("Remove <<");
			btnRemoveTask.addActionListener(new ListElementRemover<Task>(taskSelectList));
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
				JList toolList = new JList<Widget>(new ArrayListModel<Widget>());
				toolList.addListSelectionListener(new ListButtonEnabler(btnAddTool));
				toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				toolScroll.setViewportView(toolList);
			}
		}
		{
			JLabel toolAmount = new JLabel("#");
			GridBagConstraints gbc_toolAmount = new GridBagConstraints();
			gbc_toolAmount.insets = new Insets(0, 0, 5, 5);
			gbc_toolAmount.anchor = GridBagConstraints.EAST;
			gbc_toolAmount.gridx = 2;
			gbc_toolAmount.gridy = 4;
			mcontentPanel.add(toolAmount, gbc_toolAmount);
		}
		{
			mtextToolNum = new JTextField();
			mtextToolNum.setToolTipText("Enter number of tools needed.");
			GridBagConstraints gbc_textToolNum = new GridBagConstraints();
			gbc_textToolNum.fill = GridBagConstraints.HORIZONTAL;
			gbc_textToolNum.insets = new Insets(0, 0, 5, 5);
			gbc_textToolNum.gridx = 3;
			gbc_textToolNum.gridy = 4;
			mcontentPanel.add(mtextToolNum, gbc_textToolNum);
		}
		{
			btnAddTool = new JButton("Add >>");
			btnAddTool.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					int num;
					// check for number format exception
					try
					{
						num = Integer.parseInt(mtextToolNum.getText());
						if (num <= 0)
						{
							updateStatus("Please enter a positive number");
							return;
						}
					}
					catch (NumberFormatException ex)
					{
						updateStatus("Please enter a positive number");
						return;
					}
					
					Object obj = toolList.getSelectedValue();
					ArrayListModel model = (ArrayListModel)toolSelectList.getModel();
					int i = model.indexOf(obj);
					if (i == -1)
						model.add(new ResourceConstraint(obj.toString(), num));
					else
						((ResourceConstraint) model.getElementAt(i)).setAmount(num);
					updateStatus("");
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
					toolSelectList = new JList<Widget>(new ArrayListModel<Widget>());
					toolSelectList.addListSelectionListener(new ListButtonEnabler(btnRemoveTool));
					toolSelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					toolSelectList.setCellRenderer(new DefaultListCellRenderer()
					{
						public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
						{
							String str = "" + ((ResourceConstraint)value).getAmount() + ' ' + value;
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
			btnRemoveTool.addActionListener(new ListElementRemover<Widget>(toolSelectList));
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
				partList = new JList<Widget>(new ArrayListModel<Widget>());
				partList.addListSelectionListener(new ListButtonEnabler(btnAddPart));
				partList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				partScroll.setViewportView(partList);
			}
		}
		{
			JLabel partAmount = new JLabel("#");
			GridBagConstraints gbc_partAmount = new GridBagConstraints();
			gbc_partAmount.insets = new Insets(0, 0, 5, 5);
			gbc_partAmount.anchor = GridBagConstraints.EAST;
			gbc_partAmount.gridx = 2;
			gbc_partAmount.gridy = 7;
			mcontentPanel.add(partAmount, gbc_partAmount);
		}
		{
			mtextPartNum = new JTextField();
			mtextPartNum.setToolTipText("Enter number of parts needed.");
			GridBagConstraints gbc_textPartNum = new GridBagConstraints();
			gbc_textPartNum.insets = new Insets(0, 0, 5, 5);
			gbc_textPartNum.fill = GridBagConstraints.HORIZONTAL;
			gbc_textPartNum.gridx = 3;
			gbc_textPartNum.gridy = 7;
			mcontentPanel.add(mtextPartNum, gbc_textPartNum);
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
				partSelectList = new JList<Widget>(new ArrayListModel<Widget>());
				partSelectList.addListSelectionListener(new ListButtonEnabler(btnRemovePart));
				partSelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				partSelectList.setCellRenderer(new DefaultListCellRenderer()
				{
					public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						String str = "" + ((ResourceConstraint)value).getAmount() + ' ' + value;
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
					int num;
					// check for number format exception
					try
					{
						num = Integer.parseInt(mtextToolNum.getText());
						if (num <= 0)
						{
							updateStatus("Please enter a positive number");
							return;
						}
					}
					catch (NumberFormatException ex)
					{
						updateStatus("Please enter a positive number");
						return;
					}
					
					Object obj = partList.getSelectedValue();
					ArrayListModel model = (ArrayListModel)partSelectList.getModel();
					int i = model.indexOf(obj);
					if (i == -1)
						model.add(new ResourceConstraint(obj.toString(), num));
					else
						((ResourceConstraint) model.getElementAt(i)).setAmount(num);
					updateStatus("");
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
			btnRemovePart.addActionListener(new ListElementRemover<Widget>(partSelectList));
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
			JButton btnFindLink = new JButton("Find...");
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
			JLabel lblStartDate = new JLabel("Start Date");
			GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
			gbc_lblStartDate.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartDate.gridx = 1;
			gbc_lblStartDate.gridy = 11;
			mcontentPanel.add(lblStartDate, gbc_lblStartDate);
		}
		{
			JLabel lblTimeSpent = new JLabel("Time Spent");
			GridBagConstraints gbc_lblTimeSpent = new GridBagConstraints();
			gbc_lblTimeSpent.insets = new Insets(0, 0, 5, 5);
			gbc_lblTimeSpent.gridx = 3;
			gbc_lblTimeSpent.gridy = 11;
			mcontentPanel.add(lblTimeSpent, gbc_lblTimeSpent);
		}
		{
			JLabel lblEndDate = new JLabel("End Date");
			GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
			gbc_lblEndDate.insets = new Insets(0, 0, 5, 0);
			gbc_lblEndDate.gridx = 4;
			gbc_lblEndDate.gridy = 11;
			mcontentPanel.add(lblEndDate, gbc_lblEndDate);
		}
		{
			mtextStatus = new JTextField();
			mtextStatus.setEditable(false);
			GridBagConstraints gbc_textStatus = new GridBagConstraints();
			gbc_textStatus.gridwidth = 5;
			gbc_textStatus.fill = GridBagConstraints.HORIZONTAL;
			gbc_textStatus.gridx = 0;
			gbc_textStatus.gridy = 13;
			mcontentPanel.add(mtextStatus, gbc_textStatus);
			mtextStatus.setColumns(10);
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
						
						updateStatus("");
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
						curTask = null;
						updateStatus("");
						setVisible(false);
						
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void updateStatus(String str)
	{
		mtextStatus.setText(str);
		mtextStatus.repaint();
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
			updateStatus("");
			target.repaint();
		}
	}
	
	private class ListElementRemover<E> implements ActionListener
	{
		private JList<E> target;
		
		public ListElementRemover(JList<E> target)
		{
			this.target = target;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if (target.getMinSelectionIndex() >= 0)
				target.remove(target.getMinSelectionIndex());
			target.repaint();
			updateStatus("");
		}
		
	}
}