# Ô ăn quan (Java Swing)

Game Ô ăn quan cho 2 người chơi, viết theo hướng OOP (Java 17+).

## Chạy game
Dùng Ant:  `ant run`
Hoặc thủ công:
```
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin vn.edu.oanquan.Main
```

## Cấu trúc
- model/: Cell (abstract), Square, HalfCircle, Board, Player
- controller/: GameController (luật chơi, rải quân, ăn điểm), ScoreManager (tính điểm, kết thúc)
- ui/: MainScreen (Start/Help/Exit), GameScreen, HelpDialog
- docs/: đặt Requirements.pdf và ClassDiagram.png (sơ đồ lớp UML) tại đây
