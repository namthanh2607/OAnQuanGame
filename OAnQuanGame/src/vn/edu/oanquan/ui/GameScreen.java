package vn.edu.oanquan.ui;

import vn.edu.oanquan.controller.GameController;
import vn.edu.oanquan.controller.GameController.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.IntConsumer;

/** Màn hình chơi game: bàn cờ, điểm, lượt chơi. */
public class GameScreen extends JPanel {
    private final BoardPanel boardPanel = new BoardPanel();
    private final JLabel[] scoreLabels = { new JLabel(), new JLabel() };
    private final JLabel msg = new JLabel(" ", SwingConstants.CENTER);
    private final JButton left = new JButton("\u25C0 Rải trái"), right = new JButton("Rải phải \u25B6");
    private GameController game;
    private int selected = -1;
    private boolean busy = false;

    public GameScreen(Runnable onQuit) {
        super(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel hud = new JPanel(new GridLayout(1, 2, 10, 0));
        for (JLabel l : scoreLabels) {
            l.setFont(l.getFont().deriveFont(Font.BOLD, 18f)); l.setOpaque(true);
            l.setHorizontalAlignment(SwingConstants.CENTER); hud.add(l);
        }
        msg.setFont(msg.getFont().deriveFont(15f));
        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.add(hud, BorderLayout.NORTH); top.add(msg, BorderLayout.SOUTH);
        JButton quit = new JButton("Về menu");
        JPanel ctl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        ctl.add(left); ctl.add(right); ctl.add(quit);
        left.addActionListener(e -> move(-1));
        right.addActionListener(e -> move(1));
        quit.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn muốn bỏ ván này và về menu?", "Về menu", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) onQuit.run();
        });
        boardPanel.setOnSquare(this::pick);
        add(top, BorderLayout.NORTH); add(boardPanel, BorderLayout.CENTER); add(ctl, BorderLayout.SOUTH);
    }

    public void startGame() {
        game = new GameController(); selected = -1; busy = false;
        render(game.frame("Lượt của Người chơi 1: hãy chọn một ô."));
    }

    private void render(Frame f) {
        boardPanel.show(f, selected);
        String[] names = { "Người chơi 1 (dưới)", "Người chơi 2 (trên)" };
        for (int i = 0; i < 2; i++) {
            scoreLabels[i].setText(names[i] + ": " + f.scores()[i]);
            scoreLabels[i].setBackground(!f.over() && f.turn() == i ? new Color(0xF2D58A) : UIManager.getColor("Panel.background"));
            scoreLabels[i].setForeground(Color.BLACK);
        }
        msg.setText(f.msg());
        boolean can = selected >= 0 && !busy && !f.over();
        left.setEnabled(can); right.setEnabled(can);
    }

    private void pick(int i) {
        if (busy || !game.canPlay(i)) return;
        selected = i;
        render(game.frame(game.current().name() + " đã chọn ô. Hãy chọn hướng rải.", i));
    }

    private void move(int screenDir) {
        if (selected < 0 || busy) return;
        int dir = game.turn() == 0 ? screenDir : -screenDir;   // vòng +1: trái→phải ở hàng dưới, phải→trái ở hàng trên
        int pos = selected;
        selected = -1; busy = true;
        List<Frame> frames = game.play(pos, dir);
        int[] k = { 0 };
        Timer timer = new Timer(380, null);
        timer.addActionListener(e -> {
            if (k[0] == frames.size() - 1) busy = false;
            render(frames.get(k[0]++));
            if (k[0] >= frames.size()) timer.stop();
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    /** Vẽ bàn cờ và báo ô được nhấn. */
    private static class BoardPanel extends JPanel {
        private static final Color WOOD = new Color(0x9A6A3C), CELL = new Color(0xD9B98A), CELL_HL = new Color(0xEBD2A6),
                SMALL = new Color(0x3D6B4A), BIG = new Color(0xB3402A);
        private final Rectangle[] rects = new Rectangle[12];
        private Frame frame;
        private int selected = -1;
        private IntConsumer onSquare = i -> { };

        BoardPanel() {
            setPreferredSize(new Dimension(820, 340));
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    for (int i = 0; i < 12; i++) if (rects[i] != null && rects[i].contains(e.getPoint())) onSquare.accept(i);
                }
            });
        }

        void setOnSquare(IntConsumer c) { onSquare = c; }
        void show(Frame f, int selected) { this.frame = f; this.selected = selected; repaint(); }

        private void layoutCells() {
            int m = 12, gap = 8, w = getWidth(), h = getHeight();
            double unit = (w - 2.0 * m - 6 * gap) / 7.2;
            int qw = (int) (unit * 1.1), cw = (int) unit, ch = (h - 2 * m - gap) / 2;
            rects[6] = new Rectangle(m, m, qw, h - 2 * m);
            rects[0] = new Rectangle(w - m - qw, m, qw, h - 2 * m);
            for (int k = 0; k < 5; k++) {
                int x = m + qw + gap + k * (cw + gap);
                rects[5 - k] = new Rectangle(x, m, cw, ch);
                rects[7 + k] = new Rectangle(x, m + ch + gap, cw, ch);
            }
        }

        private void outline(Graphics2D g, Rectangle r, boolean quan, boolean fill) {
            int arc = quan ? r.width : 18;
            if (fill) g.fillRoundRect(r.x, r.y, r.width, r.height, arc, arc);
            else g.drawRoundRect(r.x, r.y, r.width, r.height, arc, arc);
        }

        @Override protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            layoutCells();
            g.setColor(WOOD);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            if (frame == null) return;
            for (int i = 0; i < 12; i++) {
                Rectangle r = rects[i];
                boolean quan = i % 6 == 0, hl = frame.highlight().contains(i);
                g.setColor(hl ? CELL_HL : CELL);
                outline(g, r, quan, true);
                if (i == selected || hl) {
                    g.setColor(BIG); g.setStroke(new BasicStroke(i == selected ? 4 : 2));
                    outline(g, r, quan, false);
                }
                drawGems(g, r, frame.big()[i], frame.small()[i]);
                g.setColor(Color.DARK_GRAY);
                g.setFont(getFont().deriveFont(Font.BOLD, 12f));
                g.drawString(String.valueOf(frame.small()[i] + 5 * frame.big()[i]), r.x + r.width - 20, r.y + r.height - 8);
            }
        }

        private void drawGems(Graphics2D g, Rectangle r, int big, int small) {
            int total = big + small, slot = 18;
            if (total == 0) return;
            int cols = Math.max(1, Math.min(total, (r.width - 16) / slot)), rows = (total + cols - 1) / cols;
            int x0 = r.x + (r.width - cols * slot) / 2, y0 = r.y + (r.height - rows * slot) / 2;
            for (int k = 0; k < total; k++) {
                int cx = x0 + (k % cols) * slot + slot / 2, cy = y0 + (k / cols) * slot + slot / 2;
                boolean isBig = k < big;
                int d = isBig ? 17 : 11;
                g.setColor(isBig ? BIG : SMALL);
                g.fillOval(cx - d / 2, cy - d / 2, d, d);
            }
        }
    }
}
