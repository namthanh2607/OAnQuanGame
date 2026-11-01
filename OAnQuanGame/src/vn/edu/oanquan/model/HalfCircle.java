package vn.edu.oanquan.model;

/** Ô quan (nửa hình tròn): ban đầu có 1 quân to. */
public class HalfCircle extends Cell {
    public HalfCircle() { this.big = 1; }
    @Override public boolean isHalfCircle() { return true; }
}
