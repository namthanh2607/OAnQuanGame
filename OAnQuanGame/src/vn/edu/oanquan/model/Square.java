package vn.edu.oanquan.model;

/** Ô dân: ban đầu có 5 quân nhỏ. */
public class Square extends Cell {
    public Square() { this.small = 5; }
    @Override public boolean isHalfCircle() { return false; }
}
