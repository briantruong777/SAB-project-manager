import java.awt.*;
import javax.swing.*;

public class GUIFrame extends JFrame
{
  JMenuBar menuBar;
  JMenu menu;
  JMenuItem menuItem;

  JScrollPane scrollPane;
  JPanel rootPanel;
  JPanel mainPanel;

  public GUIFrame()
  {
    super("SAB Project Manager");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    menuBar = new JMenuBar();
    menu = new JMenu("A Menu");
    menuItem = new JMenuItem("A text-only menu item");
    menu.add(menuItem);
    menuBar.add(menu);
    setJMenuBar(menuBar);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    GUITaskPanel p = new GUITaskPanel();
    mainPanel.add(p);
    for (int i = 0; i < 10; i++)
    {
      p = new GUITaskPanel();
      mainPanel.add(p);
    }

    scrollPane = new JScrollPane(mainPanel);

    rootPanel = (JPanel) getContentPane();
    rootPanel.add(scrollPane);

    pack();
    setVisible(true);
  }

  public static void main(String[] args)
  {
    GUIFrame g = new GUIFrame();
  }
}
