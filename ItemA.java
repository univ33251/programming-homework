import java.awt.*;

class ItemA extends MovingObject {// 回復アイテム

    ItemA() {
        w = 3; // アイテムの大きさ
        h = 7;
        dx = 0;
        dy = 3;
        hp = 0; // 初期値は非アクティブ
        ItemA = true;
    }

    void move(Graphics buf, int apWidth, int apHeight) {
        if (hp > 0) {
            buf.setColor(Color.GREEN); // 色を緑に設定
            buf.fillRect(x - w, y - h, 2 * w, 2 * h); // アイテムを描く
            buf.fillRect(x - h, y - w, 2 * h, 2 * w);
            buf.drawOval(x - h, y - h, 2 * h, 2 * h);
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
        hp = 1; // アイテムをドロップしたらアクティブにする
    }

}
