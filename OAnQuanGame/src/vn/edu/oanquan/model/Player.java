package vn.edu.oanquan.model;

/** Thông tin người chơi: tên, các ô sở hữu và điểm. */
public class Player {
    private final String name;
    private final int[] rows;
    private int score;

    public Player(String name, int... rows) { this.name = name; this.rows = rows; }

    public String name() { return name; }
    public int[] rows() { return rows; }
    public int score() { return score; }
    public void addScore(int n) { score += n; }
    public boolean owns(int i) { for (int r : rows) if (r == i) return true; return false; }
}
