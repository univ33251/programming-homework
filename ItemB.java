import java.awt.*;

class ItemB extends MovingObject {// 回復アイテム

    ItemB() {
        w = 7; // アイテムの大きさ
        h = 7;
        dx = 0;
        dy = 3;
        hp = 0; // 初期値は非アクティブ
        ItemB = true;
    }

    void move(Graphics buf, int apWidth, int apHeight, Image Image) {
        if (hp > 0) {
            buf.drawImage(Image, x - w, y - h, x + w, y + h, 18, 51, 726, 695, null);
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
