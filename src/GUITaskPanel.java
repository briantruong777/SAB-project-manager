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
    setPreferredSize(new Dimension(500, 50));
    setMaximumSize(new Dimension(500, 50));

    ImageIcon ia = new ImageIcon("res/incomplete.png", "Incomplete");
    ImageIcon ib = new ImageIcon("res/work.png", "Working");
    ImageIcon ic = new ImageIcon("res/complete.png", "Complete");
    ImageIcon ja = new ImageIcon("res/incomplete_bw.png", "Incomplete");
    ImageIcon jb = new ImageIcon("res/work_bw.png", "Working");
    ImageIcon jc = new ImageIcon("res/complete_bw.png", "Complete");
    a = new JRadioButton(ja);
    b = new JRadioButton(jb);
    c = new JRadioButton(jc);
    a.setSelectedIcon(ia);
    b.setSelectedIcon(ib);
    c.setSelectedIcon(ic);
    a.setSelected(true);

    grp = new ButtonGroup();
    grp.add(a);
    grp.add(b);
    grp.add(c);

    add(a);
    add(b);
    add(c);

    lbl = new JLabel("Task 1");
    add(lbl);
  }
}
