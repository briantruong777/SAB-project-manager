package guiElements;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import resourceModel.*;

@SuppressWarnings("serial")
public class ResourcePanel extends JPanel
{
	private JTextField toolsNameText;
	private JTextField partsNameText;
	private JList<Resource> toolsList;
	private JList<Resource> partsList;
	private ArrayListModel<Resource> toolsModel;
	private ArrayListModel<Resource> partsModel;
	private JSpinner toolsMaxSpinner;
	private JSpinner partsMaxSpinner;
	private JButton toolsChange;
	private JButton partsChange;
	private JButton toolsRemove;
	private JButton partsRemove;
	
	/**
	 * Create the panel.
	 */
	public ResourcePanel()
	{
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				toolsList.clearSelection();
				partsList.clearSelection();
			}
		});
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_panel);
		
		JLabel toolsLabel = new JLabel("Tools (Available/Max)");
		GridBagConstraints gbc_toolsLabel = new GridBagConstraints();
		gbc_toolsLabel.gridwidth = 3;
		gbc_toolsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toolsLabel.gridx = 0;
		gbc_toolsLabel.gridy = 0;
		add(toolsLabel, gbc_toolsLabel);
		
		JLabel partsLabel = new JLabel("Parts (Available/Max)");
		GridBagConstraints gbc_partsLabel = new GridBagConstraints();
		gbc_partsLabel.gridwidth = 3;
		gbc_partsLabel.insets = new Insets(0, 0, 5, 0);
		gbc_partsLabel.gridx = 3;
		gbc_partsLabel.gridy = 0;
		add(partsLabel, gbc_partsLabel);
		
		JScrollPane toolsScroll = new JScrollPane();
		GridBagConstraints gbc_toolsScroll = new GridBagConstraints();
		gbc_toolsScroll.fill = GridBagConstraints.BOTH;
		gbc_toolsScroll.gridheight = 7;
		gbc_toolsScroll.insets = new Insets(0, 0, 0, 5);
		gbc_toolsScroll.gridx = 0;
		gbc_toolsScroll.gridy = 1;
		add(toolsScroll, gbc_toolsScroll);
		
		toolsModel = new ArrayListModel<Resource>();
		toolsList = new JList<Resource>(toolsModel);
		toolsList.setCellRenderer(new DefaultListCellRenderer()
		{
			public Component getListCellRendererComponent(JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
			{
				Resource r = (Resource)value; 
				String s = "" + r.getAvailable() + '/' + r.getMax() + ' ' + value;
				return super.getListCellRendererComponent(list, s, index, isSelected, cellHasFocus);
			}
		});
		toolsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				Resource r = toolsList.getSelectedValue();
				// nothing is selected
				if (r == null)
				{
					toolsNameText.setText("");
					toolsMaxSpinner.setValue(0);
					toolsChange.setEnabled(false);
					toolsRemove.setEnabled(false);
				}
				else
				{
					toolsNameText.setText(r.getName());
					toolsMaxSpinner.setValue(r.getMax());
					toolsChange.setEnabled(true);
					toolsRemove.setEnabled(true);
					partsList.clearSelection();
				}
			}
		});
		toolsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		toolsScroll.setViewportView(toolsList);
		
		JScrollPane partsScroll = new JScrollPane();
		GridBagConstraints gbc_partsScroll = new GridBagConstraints();
		gbc_partsScroll.fill = GridBagConstraints.BOTH;
		gbc_partsScroll.gridheight = 7;
		gbc_partsScroll.insets = new Insets(0, 0, 0, 5);
		gbc_partsScroll.gridx = 3;
		gbc_partsScroll.gridy = 1;
		add(partsScroll, gbc_partsScroll);
		
		partsModel = new ArrayListModel<Resource>();
		partsList = new JList<Resource>(partsModel);
		partsList.setCellRenderer(new DefaultListCellRenderer()
		{
			public Component getListCellRendererComponent(JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
			{
				Resource r = (Resource)value; 
				String s = "" + r.getAvailable() + '/' + r.getMax() + ' ' + value;
				return super.getListCellRendererComponent(list, s, index, isSelected, cellHasFocus);
			}
		});
		partsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				Resource r = partsList.getSelectedValue();
				// nothing is selected
				if (r == null)
				{
					partsNameText.setText("");
					partsMaxSpinner.setValue(0);
					partsChange.setEnabled(false);
					partsRemove.setEnabled(false);
				}
				else
				{
					partsNameText.setText(r.getName());
					partsMaxSpinner.setValue(r.getMax());
					partsChange.setEnabled(true);
					partsRemove.setEnabled(true);
					toolsList.clearSelection();
				}
			}
		});
		partsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		partsScroll.setViewportView(partsList);
		
		JLabel toolsNameLabel = new JLabel("Name");
		GridBagConstraints gbc_toolsNameLabel = new GridBagConstraints();
		gbc_toolsNameLabel.anchor = GridBagConstraints.EAST;
		gbc_toolsNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toolsNameLabel.gridx = 1;
		gbc_toolsNameLabel.gridy = 2;
		add(toolsNameLabel, gbc_toolsNameLabel);
		
		toolsNameText = new JTextField();
		toolsNameText.setColumns(10);
		GridBagConstraints gbc_toolsNameText = new GridBagConstraints();
		gbc_toolsNameText.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsNameText.insets = new Insets(0, 0, 5, 5);
		gbc_toolsNameText.gridx = 2;
		gbc_toolsNameText.gridy = 2;
		add(toolsNameText, gbc_toolsNameText);
		
		JLabel partsNameLabel = new JLabel("Name");
		GridBagConstraints gbc_partsNameLabel = new GridBagConstraints();
		gbc_partsNameLabel.anchor = GridBagConstraints.EAST;
		gbc_partsNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_partsNameLabel.gridx = 4;
		gbc_partsNameLabel.gridy = 2;
		add(partsNameLabel, gbc_partsNameLabel);
		
		partsNameText = new JTextField();
		partsNameText.setColumns(10);
		GridBagConstraints gbc_partsNameText = new GridBagConstraints();
		gbc_partsNameText.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsNameText.insets = new Insets(0, 0, 5, 0);
		gbc_partsNameText.gridx = 5;
		gbc_partsNameText.gridy = 2;
		add(partsNameText, gbc_partsNameText);
		
		JLabel toolsMaxLabel = new JLabel("Max#");
		GridBagConstraints gbc_toolsMaxLabel = new GridBagConstraints();
		gbc_toolsMaxLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toolsMaxLabel.gridx = 1;
		gbc_toolsMaxLabel.gridy = 3;
		add(toolsMaxLabel, gbc_toolsMaxLabel);
		
		toolsMaxSpinner = new JSpinner();
		toolsMaxSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_toolsMaxSpinner = new GridBagConstraints();
		gbc_toolsMaxSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsMaxSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_toolsMaxSpinner.gridx = 2;
		gbc_toolsMaxSpinner.gridy = 3;
		add(toolsMaxSpinner, gbc_toolsMaxSpinner);
		
		JLabel partsMaxLabel = new JLabel("Max#");
		GridBagConstraints gbc_partsMaxLabel = new GridBagConstraints();
		gbc_partsMaxLabel.insets = new Insets(0, 0, 5, 5);
		gbc_partsMaxLabel.gridx = 4;
		gbc_partsMaxLabel.gridy = 3;
		add(partsMaxLabel, gbc_partsMaxLabel);
		
		partsMaxSpinner = new JSpinner();
		partsMaxSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_partsMaxSpinner = new GridBagConstraints();
		gbc_partsMaxSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsMaxSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_partsMaxSpinner.gridx = 5;
		gbc_partsMaxSpinner.gridy = 3;
		add(partsMaxSpinner, gbc_partsMaxSpinner);
		
		JButton toolsAdd = new JButton("Add");
		toolsAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ("".equals(toolsNameText.getText()))
				{
					toolsNameText.setText("Name of tool cannot be blank.");
					return;
				}
				Resource newTool = new Resource(toolsNameText.getText(), (Integer)toolsMaxSpinner.getValue());
				int i = toolsModel.indexOf(newTool);
				
				// does not contain given resource
				if (i == -1)
				{
					toolsModel.add(newTool);
					Inventory.addTool(newTool);
				}
				else
				{
					Resource existTool = toolsModel.get(i);
					existTool.setMax(existTool.getMax() + newTool.getMax());
					existTool.setAvailable(existTool.getAvailable() + newTool.getMax());
					toolsModel.notifyChanged(i);
				}
			}
		});
		GridBagConstraints gbc_toolsAdd = new GridBagConstraints();
		gbc_toolsAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsAdd.gridwidth = 2;
		gbc_toolsAdd.insets = new Insets(0, 0, 5, 5);
		gbc_toolsAdd.gridx = 1;
		gbc_toolsAdd.gridy = 4;
		add(toolsAdd, gbc_toolsAdd);
		
		JButton partsAdd = new JButton("Add");
		partsAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ("".equals(partsNameText.getText()))
				{
					partsNameText.setText("Name of part cannot be blank.");
					return;
				}
				Resource newPart = new Resource(partsNameText.getText(), (Integer)partsMaxSpinner.getValue());
				int i = partsModel.indexOf(newPart);
				
				// does not contain given resource
				if (i == -1)
				{
					partsModel.add(newPart);
					Inventory.addPart(newPart);
				}
				else
				{
					Resource existPart = partsModel.get(i);
					existPart.setMax(existPart.getMax() + newPart.getMax());
					existPart.setAvailable(existPart.getAvailable() + newPart.getMax());
					partsModel.notifyChanged(i);
				}
			}
		});
		GridBagConstraints gbc_partsAdd = new GridBagConstraints();
		gbc_partsAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsAdd.gridwidth = 2;
		gbc_partsAdd.insets = new Insets(0, 0, 5, 0);
		gbc_partsAdd.gridx = 4;
		gbc_partsAdd.gridy = 4;
		add(partsAdd, gbc_partsAdd);
		
		toolsChange = new JButton("Change");
		toolsChange.setEnabled(false);
		toolsChange.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = toolsList.getSelectedValue();
				String text = toolsNameText.getText();
				if (!r.getName().equals(text) && toolsModel.contains(new Resource(text)))
				{
					toolsNameText.setText("New name conflicts with existing tool.");
					return;
				}
				
				int amount = r.getAvailable() + (Integer)toolsMaxSpinner.getValue() - r.getMax();
				if (amount < 0)
				{
					toolsNameText.setText("Please pause some tasks that are using this tool first.");
					return;
					// !!!ask which tasks to pause
				}
				else
				{
					r.setAvailable(amount);
					r.setMax((Integer)toolsMaxSpinner.getValue());
				}
				// need to rehash tool
				Inventory.removeTool(r);
				r.setName(text);
				toolsModel.notifyChanged(toolsList.getMinSelectionIndex());
				Inventory.addTool(r);
			}
		});
		GridBagConstraints gbc_toolsChange = new GridBagConstraints();
		gbc_toolsChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsChange.gridwidth = 2;
		gbc_toolsChange.insets = new Insets(0, 0, 5, 5);
		gbc_toolsChange.gridx = 1;
		gbc_toolsChange.gridy = 5;
		add(toolsChange, gbc_toolsChange);
		
		partsChange = new JButton("Change");
		partsChange.setEnabled(false);
		partsChange.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = partsList.getSelectedValue();
				String text = partsNameText.getText();
				if (!r.getName().equals(text) && partsModel.contains(new Resource(text)))
				{
					partsNameText.setText("New name conflicts with existing part.");
					return;
				}
				int amount = r.getAvailable() + (Integer)partsMaxSpinner.getValue() - r.getMax();
				if (amount < 0)
				{
					partsNameText.setText("Please pause some tasks that are using this part.");
					return;
					// !!!ask which tasks to pause
				}
				else
				{
					r.setAvailable(amount);
					r.setMax((Integer)partsMaxSpinner.getValue());
				}
				// need to rehash part
				Inventory.removePart(r);
				r.setName(text);
				partsModel.notifyChanged(partsList.getMinSelectionIndex());
				Inventory.addPart(r);
			}
		});
		GridBagConstraints gbc_partsChange = new GridBagConstraints();
		gbc_partsChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsChange.gridwidth = 2;
		gbc_partsChange.insets = new Insets(0, 0, 5, 0);
		gbc_partsChange.gridx = 4;
		gbc_partsChange.gridy = 5;
		add(partsChange, gbc_partsChange);
		
		toolsRemove = new JButton("Remove");
		toolsRemove.setEnabled(false);
		toolsRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = toolsList.getSelectedValue();
				if (r.hasConstraint())
				{
					toolsNameText.setText("There are tasks that need this tool. Set max to 0 if all tools cannot be used");
					return;
				}
				toolsList.clearSelection();
				toolsModel.remove(r);
				Inventory.removeTool(r);
			}
		});
		GridBagConstraints gbc_toolsRemove = new GridBagConstraints();
		gbc_toolsRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolsRemove.gridwidth = 2;
		gbc_toolsRemove.insets = new Insets(0, 0, 5, 5);
		gbc_toolsRemove.gridx = 1;
		gbc_toolsRemove.gridy = 6;
		add(toolsRemove, gbc_toolsRemove);
		
		partsRemove = new JButton("Remove");
		partsRemove.setEnabled(false);
		partsRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Resource r = partsList.getSelectedValue();
				if (r.hasConstraint())
				{
					partsNameText.setText("There are tasks that need this part. Set max to 0 if all parts cannot be used");
					return;
				}
				partsList.clearSelection();
				partsModel.remove(r);
				Inventory.removePart(r);
			}
		});
		GridBagConstraints gbc_partsRemove = new GridBagConstraints();
		gbc_partsRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_partsRemove.gridwidth = 2;
		gbc_partsRemove.insets = new Insets(0, 0, 5, 0);
		gbc_partsRemove.gridx = 4;
		gbc_partsRemove.gridy = 6;
		add(partsRemove, gbc_partsRemove);
	}
}
