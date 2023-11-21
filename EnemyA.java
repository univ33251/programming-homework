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
        expcond = 0;// 爆発の状態
        addexpcond = false;
    }

    // 〇を描き更新するメソッド
    void move(Graphics buf, int apWidth, int apHeight, Image Image) {
        judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
        if (hp > 0) { // もし生きていれば
            //画像を表示
            buf.drawImage(Image, x - w, y - h, x + w, y + h, 315, 12, 359, 59, null);
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
