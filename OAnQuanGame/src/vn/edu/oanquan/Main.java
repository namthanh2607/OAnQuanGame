package vn.edu.oanquan;

import vn.edu.oanquan.ui.MainScreen;
import javax.swing.SwingUtilities;

/** Điểm khởi chạy chương trình. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::new);
    }
}
