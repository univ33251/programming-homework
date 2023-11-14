import java.awt.*;

class ItemB extends MovingObject {// 回復アイテム

    ItemB() {
        w = 2; // アイテムの大きさ
        h = 7;
        dx = 0;
        dy = 3;
        hp = 0; // 初期値は非アクティブ
        ItemB = true;
    }

    void move(Graphics buf, int apWidth, int apHeight) {
        if (hp > 0) {
            buf.setColor(Color.BLUE); // 色を青に設定
            buf.fillRect(x - 2 * w, y, 2 * w, 2 * w); // アイテムを描く
            buf.fillRect(x + w, y, 2 * w, 2 * w);
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
