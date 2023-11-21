import java.awt.*;

class FighterBullet extends MovingObject {
    // コンストラクタ
    FighterBullet() {
        w = h = 3; // 弾の半径
        dx = 0;
        dy = -6;
        hp = 0; // 初期値は非アクティブ
    }

    // メソッド
    void move(Graphics buf, int apWidth, int apHeight, Image Image) {
        if (hp > 0) {
            buf.drawImage(Image, x - w, y - h, x + w, y + h, 196, 93, 253, 150, null);
            judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
            if (judge) {
                y = y + dy; // 弾の位置を更新
            } else {
                hp = 0; // 画面外に出たら弾を非アクティブに
            }
        }
    }

    void revive(int x, int y) {
        this.x = x;
        this.y = y;
        hp = 1; // 弾を発射したらアクティブにする
    }
}
