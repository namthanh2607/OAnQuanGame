package vn.edu.oanquan.ui;

import javax.swing.*;
import java.awt.*;

/** Cửa sổ hướng dẫn chơi. */
public class HelpDialog extends JDialog {
    public HelpDialog(Frame owner) {
        super(owner, "Hướng dẫn chơi", true);
        JTextArea t = new JTextArea("""
                BÀN CỜ
                - 10 ô dân chia thành 2 hàng, cùng 2 ô quan hình bán nguyệt ở hai đầu.
                - Mỗi ô dân có 5 quân nhỏ (1 điểm/quân). Mỗi ô quan có 1 quân to (5 điểm).
                - Người chơi 1 sở hữu hàng dưới, người chơi 2 sở hữu hàng trên.

                LƯỢT CHƠI
                - Chọn một ô của mình còn quân, rồi chọn hướng rải (trái hoặc phải).
                - Nhấc toàn bộ quân trong ô và rải mỗi ô một quân, kể cả ô quan.
                - Sau quân cuối: nếu ô kế tiếp có quân (không phải ô quan) thì nhấc lên và rải tiếp.
                - Nếu ô kế tiếp trống và ô sau đó có quân thì ăn toàn bộ quân ở ô đó.
                  Nếu tiếp theo lại là một ô trống rồi đến ô có quân thì ăn tiếp.
                - Mất lượt khi ô kế tiếp là ô quan còn quân, hoặc gặp hai ô trống liên tiếp.

                HẾT QUÂN TRÊN HÀNG CỦA MÌNH
                - Trả 5 điểm để đặt mỗi ô dân của mình 1 quân. Nếu không đủ 5 điểm, game kết thúc.

                KẾT THÚC
                - Game kết thúc khi cả hai ô quan đã hết quân. Mỗi người cộng số quân còn trên hàng của mình.
                - Ai nhiều điểm hơn sẽ thắng.
                """);
        t.setEditable(false); t.setLineWrap(true); t.setWrapStyleWord(true);
        t.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        t.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton close = new JButton("Đóng");
        close.addActionListener(e -> dispose());
        add(new JScrollPane(t), BorderLayout.CENTER);
        add(close, BorderLayout.SOUTH);
        setSize(640, 560);
        setLocationRelativeTo(owner);
    }
}
