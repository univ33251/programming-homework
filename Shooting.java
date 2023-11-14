import java.awt.*;

class Shooting extends Frame implements Runnable {
    Thread th; // threadクラスのオブジェクト宣言
    GameMaster gm; // ゲームマスタークラス

    public static void main(String[] args) {
        new Shooting(); // 自身のオブジェクト作成
    }

    Shooting() { // init()はApplet クラスのメソッド
        super("Shooting Game"); // 親クラスのコンストラクタを呼び出す
        int cW = 640, cH = 480; // キャンバスのサイズ 
        this.setSize(cW+30, cH+40);
        // フレームのサイズを指定
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // キャンバスをフレームに配置

        gm = new GameMaster(cW, cH);  // GameMaster クラスのオブジェクトを作成
        this.add(gm);   // キャンバスをフレームに配置
        this.setVisible(true);  // 可視化
        th = new Thread(this);  // Thread クラスのオブジェクトの作成
        th.start(); // 最後にスレッドを start メソッドで開始
        // フォーカスを得る
        requestFocusInWindow();
    }

    // メソッド (Runnable インターフェース用)
    public void run() {
        try {
            while (true) { // 無限ループ
                Thread.sleep(20); // ウィンドウを更新する前に指定時間だけ休止
                gm.repaint(); // 再描画を要求する. repaint() は update () を呼び出す
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}