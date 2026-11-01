package vn.edu.oanquan.controller;

import vn.edu.oanquan.model.Board;
import vn.edu.oanquan.model.Player;

/** Tính điểm và kiểm tra kết thúc game. */
public class ScoreManager {
    public static final int REFILL_COST = 5;
    private final Player[] players;

    public ScoreManager(Player[] players) { this.players = players; }

    public void add(Player p, int points) { p.addScore(points); }
    public boolean canPay(Player p) { return p.score() >= REFILL_COST; }
    public void pay(Player p) { p.addScore(-REFILL_COST); }
    public boolean isGameOver(Board b) { return b.quansEmpty(); }

    /** Mỗi người nhận số quân còn lại trên hàng của mình. */
    public void collectRemaining(Board b) {
        for (Player p : players) for (int i : p.rows()) p.addScore(b.cells[i].takeAll());
    }

    public int[] scores() { return new int[] { players[0].score(), players[1].score() }; }

    public String resultMessage() {
        int a = players[0].score(), b = players[1].score();
        String res = a == b ? "Hòa!" : (a > b ? players[0].name() : players[1].name()) + " thắng!";
        return "Kết quả: " + players[0].name() + " " + a + " - " + players[1].name() + " " + b + ". " + res;
    }
}
