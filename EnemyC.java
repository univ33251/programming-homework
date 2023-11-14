import java.awt.*;

class EnemyC extends MovingObject {
    int xpoints[]; // 各点のx座標
    int ypoints[]; // 各点のy座標
    int n; // 点の数
    double theta; // 中心角の半分(rad)

    // コンストラクタ (初期値設定)
    EnemyC(int apWidth, int apHeight, int n) {
        super(apWidth, apHeight);
        x = (int) (Math.random() * (apWidth - 2 * w) + w);
        y = -h;
        w = 12;
        h = 12;
        hp = 0; // 初期状態では全て死亡
        dy = (int) (Math.ceil(Math.random() * 2));
        bullet = true;
        delaytime = 50;
        this.n = n;
        this.xpoints = new int[n];
        this.ypoints = new int[n];
        this.theta = (360.0 / (double) n) / 180.0 * Math.PI;
    }

    // ▽を描き更新するメソッド
    void move(Graphics buf, int apWidth, int apHeight) {
        judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
        for (int i = 0; i < 3; i++) {
            xpoints[i] = (int) (x + w * Math.sin(theta * (double) i));
            ypoints[i] = (int) (y + w * Math.cos(theta * (double) i));
        }
        buf.setColor(Color.black); // 色を黒に設定
        if (hp > 0) { // もし生きていれば
            buf.drawPolygon(xpoints, ypoints, n); // ▽を描く
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
