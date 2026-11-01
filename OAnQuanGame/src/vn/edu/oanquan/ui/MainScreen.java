package vn.edu.oanquan.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Màn hình bắt đầu: Start, Help, Exit. */
public class MainScreen extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final GameScreen gameScreen = new GameScreen(() -> cards.show(root, "menu"));

    public MainScreen() {
        super("Ô ăn quan");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { askExit(); }
        });
        root.add(menuPanel(), "menu");
        root.add(gameScreen, "game");
        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(700, 520));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel menuPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        JPanel col = new JPanel(new GridLayout(0, 1, 0, 12));
        JLabel title = new JLabel("Ô ăn quan", SwingConstants.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 54));
        col.add(title);
        col.add(new JLabel("Trò chơi dân gian dành cho 2 người chơi", SwingConstants.CENTER));
        JButton start = new JButton("Start"), help = new JButton("Help"), exit = new JButton("Exit");
        for (JButton b : new JButton[] { start, help, exit }) { b.setFont(b.getFont().deriveFont(18f)); col.add(b); }
        start.addActionListener(e -> { gameScreen.startGame(); cards.show(root, "game"); });
        help.addActionListener(e -> new HelpDialog(this).setVisible(true));
        exit.addActionListener(e -> askExit());
        p.add(col);
        return p;
    }

    private void askExit() {
        if (JOptionPane.showConfirmDialog(this, "Bạn có thực sự muốn thoát game?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) System.exit(0);
    }
}
