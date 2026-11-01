package vn.edu.oanquan.controller;

import vn.edu.oanquan.model.Board;
import vn.edu.oanquan.model.Cell;
import vn.edu.oanquan.model.Player;

import java.util.ArrayList;
import java.util.List;

/** Quản lý luồng chơi, rải quân, ăn điểm. */
public class GameController {
    /** Ảnh chụp trạng thái bàn cờ, dùng để làm animation từng bước. */
    public record Frame(int[] small, int[] big, int[] scores, String msg, List<Integer> highlight, int turn, boolean over) { }

    private final Board board = new Board();
    private final Player[] players = { new Player("Người chơi 1", 7, 8, 9, 10, 11), new Player("Người chơi 2", 1, 2, 3, 4, 5) };
    private final ScoreManager scoreManager = new ScoreManager(players);
    private int turn = 0;
    private boolean over = false;

    public int turn() { return turn; }
    public boolean isOver() { return over; }
    public Player current() { return players[turn]; }

    public Frame frame(String msg, Integer... hl) {
        int[] s = new int[Board.SIZE], b = new int[Board.SIZE];
        for (int i = 0; i < Board.SIZE; i++) { s[i] = board.cells[i].small(); b[i] = board.cells[i].big(); }
        return new Frame(s, b, scoreManager.scores(), msg, List.of(hl), turn, over);
    }

    public boolean canPlay(int i) { return !over && current().owns(i) && board.cells[i].small() > 0; }

    /** Thực hiện một nước đi (dir = +1 / -1 trên vòng) và trả về các frame để hiển thị. */
    public List<Frame> play(int pos, int dir) {
        List<Frame> frames = new ArrayList<>();
        Cell[] c = board.cells;
        Player me = current();
        int hand = c[pos].takeSmall(), i = pos;
        frames.add(frame(me.name() + " nhấc " + hand + " quân.", pos));
        while (true) {
            while (hand > 0) {
                i = Board.wrap(i + dir); c[i].addSmall(); hand--;
                frames.add(frame(me.name() + " đang rải quân...", i));
            }
            int n = Board.wrap(i + dir);
            if (!c[n].isHalfCircle() && c[n].small() > 0) {          // rải tiếp từ ô kế tiếp có quân
                hand = c[n].takeSmall(); i = n;
                frames.add(frame(me.name() + " nhấc thêm " + hand + " quân và rải tiếp.", n));
                continue;
            }
            if (c[n].isHalfCircle() && c[n].value() > 0) break;      // gặp ô quan còn quân: mất lượt
            int cur = n;                                             // ô trống: thử ăn quân
            while (!board.quansEmpty()) {
                int t = Board.wrap(cur + dir);
                if (c[t].isEmpty()) break;
                int pts = c[t].takeAll();
                scoreManager.add(me, pts);
                frames.add(frame(me.name() + " ăn " + pts + " điểm!", t));
                cur = Board.wrap(t + dir);
                if (!c[cur].isEmpty()) break;
            }
            break;
        }
        endTurn(frames);
        return frames;
    }

    private void endTurn(List<Frame> frames) {
        if (scoreManager.isGameOver(board)) { finish(frames, "Hai ô quan đã hết quân."); return; }
        turn = 1 - turn;
        Player p = current();
        if (board.rowEmpty(p)) {
            if (!scoreManager.canPay(p)) { finish(frames, p.name() + " không đủ 5 điểm để rải lại quân."); return; }
            scoreManager.pay(p);
            Integer[] row = new Integer[5];
            for (int k = 0; k < 5; k++) { row[k] = p.rows()[k]; board.cells[row[k]].setSmall(1); }
            frames.add(frame(p.name() + " hết quân, trả 5 điểm để đặt mỗi ô 1 quân.", row));
        }
        frames.add(frame("Lượt của " + current().name() + ": hãy chọn một ô."));
    }

    private void finish(List<Frame> frames, String why) {
        over = true;
        scoreManager.collectRemaining(board);
        frames.add(frame(why + " " + scoreManager.resultMessage()));
    }
}
