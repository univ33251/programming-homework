import java.awt.*;

class EnemyA extends MovingObject {
    // コンストラクタ (初期値設定)
    EnemyA(int apWidth, int apHeight) {
        super(apWidth, apHeight);
        x = (int) (Math.random() * (apWidth - 2 * w) + w);
        y = -h;
        dy = (int) (Math.ceil(Math.random() * 3) + 2);
        w = 12;
        h = 12;
        hp = 0; // 初期状態では全て死亡
    }

    // 〇を描き更新するメソッド
    void move(Graphics buf, int apWidth, int apHeight) {
        judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
        buf.setColor(Color.black); // 色を黒に設定
        if (hp > 0) { // もし生きていれば
            buf.drawOval(x - w, y - h, 2 * w, 2 * h); // 〇を描く
            y = y + dy;

            if (y > apHeight + h) {
                hp = 0; // 画面外に出たら死亡
            }
        }
    }

    void revive(int apWidth, int apHeight) {
        x = (int) (Math.random() * (apWidth - 2 * w) + w);
        y = -h;

        if (x < apWidth / 2) {
            dx = (int) (Math.random() * 2);
        } else {
            dx = -(int) (Math.random() * 2);
        }

        hp = 1; // 生き返る
    }
}
