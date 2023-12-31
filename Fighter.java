import java.awt.*;

class Fighter extends MovingObject {
    boolean lflag;      // ←キーが押されているか
    boolean rflag;      // →キーが押されているか
    boolean uflag;      // ↑キーが押されているか
    boolean dflag;      // ↓キーが押されているか
    boolean sflag;      // スペースキーが押されているか
    int delaytime;      // 弾を発射するまでの待ち時間

    // コンストラクタ
    Fighter(int apWidth, int apHeight) {
        x = (int) (apWidth / 2);    // 画面の中央に初期位置
        y = (int) (apHeight * 0.9); // 画面下部に初期位置
        dx = 5;                     // 初期X速度
        dy = 5;                     // 初期Y速度
        w = 10;                    // 幅
        h = 10;                    // 高さ
        lflag = false;
        rflag = false;
        uflag = false;
        dflag = false;
        sflag = false;
        delaytime = 5;              // 弾を発射するまでの初期待ち時間
    }

    void revive(int apWidth, int apHeight) {
        // 必要に応じて復活ロジックを処理
    }

    void move(Graphics buf, int apWidth, int apHeight) {
        judge = y > 0 && y < apHeight && x > 0 && x < apWidth;
        buf.setColor(Color.blue);   // 色を青に設定
        buf.fillRect(x - w, y - h, 2 * w, 2 * h); // 自機を描く

        if (lflag && !rflag && x > w) {
            x = x - dx; // 左に移動
        }

        if (rflag && !lflag && x < apWidth - w) {
            x = x + dx; // 右に移動
        }

        if (uflag && !dflag && y > h) {
            y = y - dy; // 上に移動
        }

        if (dflag && !uflag && y < apHeight - h) {
            y = y + dy; // 下に移動
        }

        // デバッグ情報を表示する場合（必要に応じてコメントを解除）
        // System.out.println("flags: " + lflag + rflag + delaytime );
        // System.out.println("(x, y)=(" + x + ", " + y + ")");
    }
}
