import java.awt.*;

class ItemA extends MovingObject {// 回復アイテム

    ItemA() {
        w = 5; // アイテムの大きさ
        h = 7;
        dx = 0;
        dy = 3;
        hp = 0; // 初期値は非アクティブ
        ItemA = true;
    }

    void move(Graphics buf, int apWidth, int apHeight, Image Image) {
        if (hp > 0) {
            buf.drawImage(Image, x - w, y - h, x + w, y + h, 206, 117, 570, 642, null);
            judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
            if (judge) {
                y = y + dy; // アイテムの位置を更新
            } else {
                hp = 0; // 画面外に出たらアイテムを非アクティブに
            }
        }
    }

    void revive(int x, int y) {
        this.x = x;
        this.y = y;
        hp = 1; // アイテムをドロップしたらアクティブにする
    }

}
