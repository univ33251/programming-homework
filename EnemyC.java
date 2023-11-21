import java.awt.*;

class EnemyC extends MovingObject {
    // コンストラクタ (初期値設定)
    EnemyC(int apWidth, int apHeight, int n) {
        super(apWidth, apHeight);
        x = (int) (Math.random() * (apWidth - 2 * w) + w);
        y = -h;
        w = 12;
        h = 12;
        hp = 0; // 初期状態では全て死亡
        expcond = 0;// 爆発の状態
        addexpcond = false;
        dy = (int) (Math.ceil(Math.random() * 2));
        bullet = true;
        delaytime = 50;
    }

    // ▽を描き更新するメソッド
    void move(Graphics buf, int apWidth, int apHeight, Image Image) {
        judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
        if (hp > 0) { // もし生きていれば
            //画像を表示
            buf.drawImage(Image, x - w, y - h, x + w, y + h, 315, 162, 359, 209, null);
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
