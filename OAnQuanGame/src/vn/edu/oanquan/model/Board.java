package vn.edu.oanquan.model;

/**
 * Quản lý 12 ô theo vòng tròn: 0 = quan phải, 1..5 = hàng trên (phải sang trái),
 * 6 = quan trái, 7..11 = hàng dưới (trái sang phải).
 */
public class Board {
    public static final int SIZE = 12;
    public final Cell[] cells = new Cell[SIZE];

    public Board() {
        for (int i = 0; i < SIZE; i++) cells[i] = (i % 6 == 0) ? new HalfCircle() : new Square();
    }

    public static int wrap(int i) { return (i + SIZE) % SIZE; }
    public boolean quansEmpty() { return cells[0].isEmpty() && cells[6].isEmpty(); }
    public boolean rowEmpty(Player p) { for (int i : p.rows()) if (!cells[i].isEmpty()) return false; return true; }
}
