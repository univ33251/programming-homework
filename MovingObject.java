import java.awt.Graphics;

abstract class MovingObject { // 抽象クラス
    // フィールド変数
    int x; // X座標
    int y; // Y座標
    int dx; // X速度
    int dy; // Y速度
    int w; // 幅
    int h; // 高さ
    int hp; // ヒットポイント（ゼロ以下で死亡）
    boolean bullet = false; // 弾を発射するか否か
    int delaytime; // 弾を発射するまでの初期待ち時間
    boolean judge = false;// 画面内であるかどうかの判定
    boolean ItemA = false;// アイテムか否かの判定
    boolean ItemB = false;

    // コンストラクタ1: 引数がない場合は初期設定しない（フィールド変数は初期値0を持つ）
    MovingObject() {
    }

    // コンストラクタ2: 描画領域の大きさを引数にし、出現の初期値をランダムに設定
    MovingObject(int width, int height) {
        x = (int) (Math.random() * width); // 画面の内部でランダムなX座標
        y = (int) (Math.random() * height); // 画面の内部でランダムなY座標
        dx = (int) (Math.random() * 6 - 3); // -3から+3の範囲でランダムなX速度
        dy = (int) (Math.random() * 6 - 3); // -3から+3の範囲でランダムなY速度
        w = 2;
        h = 2;
        hp = 10;
    }

    // オブジェクトを動かし、位置を更新する抽象メソッド
    // MovingObjectを継承する全てのクラスに同名での実装を強制
    // moveでは、まず描いてからX速度、Y速度だけ移動させる
    // moveを呼び出す前に衝突判定し、合格したものだけ描く
    abstract void move(Graphics buf, int apWidth, int apHeight);

    // オブジェクトを生き返らせる抽象メソッド
    // MovingObjectを継承する全てのクラスに同名での実装を強制
    // 引数は通常はアプレットの縦横の大きさ。弾の場合はオブジェクトの位置
    abstract void revive(int width, int height);

    // 衝突判定の共通メソッド
    boolean collisionCheck(MovingObject obj) {
        // 引数は相手のオブジェクト
        // 衝突判定の共通メソッド
        if ((obj.ItemA || obj.ItemB) && Math.abs(this.x - obj.x) <= (this.w + obj.w)
                && Math.abs(this.y - obj.y) <= (this.h + obj.h)) {
            obj.hp--;
            return true;
        } else if (Math.abs(this.x - obj.x) <= (this.w + obj.w) && Math.abs(this.y - obj.y) <= (this.h + obj.h)) {
            this.hp--; // 自分のhpを減らす
            obj.hp--; // 相手のhpを減らす
            return true;
        } else {
            return false;
        }
    }
}
