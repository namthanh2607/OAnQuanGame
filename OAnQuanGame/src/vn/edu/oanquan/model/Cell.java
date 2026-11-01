package vn.edu.oanquan.model;

/** Lớp ô bàn cờ tổng quát: chứa quân nhỏ (1 điểm) và quân to (5 điểm). */
public abstract class Cell {
    protected int small;
    protected int big;

    public abstract boolean isHalfCircle();

    public int small() { return small; }
    public int big() { return big; }
    public int value() { return small + 5 * big; }
    public boolean isEmpty() { return value() == 0; }
    public void addSmall() { small++; }
    public void setSmall(int n) { small = n; }
    public int takeSmall() { int n = small; small = 0; return n; }
    public int takeAll() { int v = value(); small = 0; big = 0; return v; }
}
