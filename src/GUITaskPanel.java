import java.awt.*;
import javax.swing.*;

public class GUITaskPanel extends JPanel
{
  JRadioButton a;
  JRadioButton b;
  JRadioButton c;
  ButtonGroup grp;

  JLabel lbl;

  public GUITaskPanel()
  {
    super();
    setAlignmentX(JComponent.CENTER_ALIGNMENT);
    setPreferredSize(new Dimension(500, 50));
    setMaximumSize(new Dimension(500, 50));

    ImageIcon ia = new ImageIcon("res/incomplete.png", "Incomplete");
    ImageIcon ib = new ImageIcon("res/work.png", "Working");
    ImageIcon ic = new ImageIcon("res/complete.png", "Complete");
    a = new JRadioButton("Incomplete");
    b = new JRadioButton("Working");
    c = new JRadioButton("Complete");

    grp = new ButtonGroup();
    grp.add(a);
    grp.add(b);
    grp.add(c);

    add(new JLabel(ia));
    add(a);
    add(new JLabel(ib));
    add(b);
    add(new JLabel(ic));
    add(c);
    
    lbl = new JLabel("Task 1");
    add(lbl);
  }
  
  public String getStatus()
  {
	  return ((Button) grp.getSelection()).getLabel();
  }
}
