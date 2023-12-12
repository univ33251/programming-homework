import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrawingApli00 extends JFrame implements ActionListener {
  // ■ フィールド変数
  private JButton bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9; // フレームに配置するボタンの宣言
  private JPanel pnl; // ボタン配置用パネルの宣言
  private MyCanvas mc; // 別途作成した MyCanvas クラス型の変数の宣言
  public Color currentColor = Color.BLACK; // 現在の色

  // ■ main メソッド（スタート地点）
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new DrawingApli00();
    });
  }

  // ■ コンストラクタ
  DrawingApli00() {
    super("Drawing Appli");
    this.setSize(700, 400);

    pnl = new JPanel(); // Panel のオブジェクト（実体）を作成
    mc = new MyCanvas(this); // mc のオブジェクト（実体）を作成

    this.setLayout(new BorderLayout(10, 10)); // レイアウト方法の指定
    this.add(pnl, BorderLayout.EAST); // 右側にパネルを配置
    this.add(mc, BorderLayout.CENTER); // 左側に mc （キャンバス）を配置
    // BorerLayout の場合，West と East の幅は部品の大きさで決まる，Center は West と East の残り幅

    pnl.setLayout(new GridLayout(9, 1)); // ボタンを配置するため，９行１列のグリッドをパネル上にさらに作成
    bt1 = new JButton("Free Hand");
    bt1.addActionListener(this);
    bt2 = new JButton("Line");
    bt2.addActionListener(this);
    bt3 = new JButton("Rectangle");
    bt3.addActionListener(this);
    bt4 = new JButton("FillRect");
    bt4.addActionListener(this);
    bt5 = new JButton("Oval");
    bt5.addActionListener(this);
    bt6 = new JButton("FillOval");
    bt6.addActionListener(this);
    bt7 = new JButton("Clear");
    bt7.addActionListener(this);
    bt8 = new JButton("Color");
    bt8.addActionListener(this);
    bt9 = new JButton("Eraser");
    bt9.addActionListener(this);

    pnl.add(bt1);// ボタンを順に配置
    pnl.add(bt2);
    pnl.add(bt3);
    pnl.add(bt4);
    pnl.add(bt5);
    pnl.add(bt6);
    pnl.add(bt8);
    pnl.add(bt9);
    pnl.add(bt7);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true); // 可視化
  }

  // ■ メソッド
  // ActionListener を実装しているため、例え内容が空でも必ず記述しなければならない
  public void actionPerformed(ActionEvent e) { // フレーム上で生じたイベントを e で取得
    if (e.getSource() == bt1) // もしイベントが bt1 で生じたなら
      mc.mode = 1; // モードを1に
    else if (e.getSource() == bt2) // もしイベントが bt2 で生じたなら
      mc.mode = 2; // モードを2に
    else if (e.getSource() == bt3) // もしイベントが bt3 で生じたなら
      mc.mode = 3; // モードを3に
    else if (e.getSource() == bt4) // もしイベントが bt4 で生じたなら
      mc.mode = 4; // モードを4に
    else if (e.getSource() == bt5) // もしイベントが bt5 で生じたなら
      mc.mode = 5; // モードを5に
    else if (e.getSource() == bt6) // もしイベントが bt6 で生じたなら
      mc.mode = 6; // モードを6に
    else if (e.getSource() == bt7) { // もしイベントが bt7 で生じたなら
      mc.mode = 0; // モードを0に
      mc.clearCanvas(); // Canvasをクリアするメソッドを呼び出し
    } else if (e.getSource() == bt8) { // もしイベントが bt8 で生じたなら
      mc.mode = 0; // モードを0に
      // 色を選択するためのウィンドウを表示
      Color selectedColor = JColorChooser.showDialog(this, "Choose Color", getCurrentColor());
      if (selectedColor != null) {
        setCurrentColor(selectedColor);
      }
    } else if (e.getSource() == bt9) { // もしイベントが bt9 で生じたなら
      mc.mode = 9;
    }
  }

  // 現在の色を取得するメソッド
  public Color getCurrentColor() {
    return currentColor;
  }

  // 現在の色を設定するメソッド
  public void setCurrentColor(Color color) {
    currentColor = color;
  }
}

class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
  // ■ フィールド変数
  private int x, y; // mouse pointer position
  private int px, py; // preliminary position
  private int ow, oh; // width and height of the object
  protected int mode; // drawing mode associated as below
  private Image img = null, img2 = null; // 仮の画用紙
  private Graphics gc = null, gc2 = null; // 仮の画用紙用のペン
  private Dimension d; // キャンバスの大きさ取得用
  private DrawingApli00 drawingApp; // 色を変更する対象の描画アプリケーション
  private boolean isEraser = false; // ペンの色をクリアするか否か
  private int X, Y; // 左上の点を決定する変数

  // ■ コンストラクタ
  MyCanvas(DrawingApli00 obj) {
    mode = 0; // initial value
    this.drawingApp = obj; // 色を変更する対象の描画アプリケーションをセット
    this.addMouseListener(this); // マウスのボタンクリックなどを監視するよう指定
    this.addMouseMotionListener(this); // マウスの動きを監視するよう指定
  }

  // ■ メソッド（オーバーライド）
  // フレームに何らかの更新が行われた時の処理
  public void update(Graphics g) {
    paint(g); // 下記の paint を呼び出す

  }

  public void clearCanvas() {
    gc.clearRect(0, 0, d.width, d.height); // Canvasをクリア
    repaint(); // 再描画
  }

  public void paint(Graphics g) {
    d = getSize(); // キャンバスのサイズを取得
    if (img == null) // もし仮の画用紙の実体がまだ存在しなければ
      img = createImage(d.width, d.height); // 作成
    if (img2 == null) // もし仮の画用紙の実体がまだ存在しなければ
      img2 = createImage(d.width, d.height); // 作成
    if (gc == null) // もし仮の画用紙用のペン (GC) がまだ存在しなければ
      gc = img.getGraphics(); // 作成
    if (gc2 == null) // もし仮の画用紙用のペン (GC2) がまだ存在しなければ
      gc2 = img2.getGraphics(); // 作成

    gc.setColor(drawingApp.getCurrentColor()); // 色の設定

    if (mode != 0)
      gc.drawImage(img2, 0, 0, this);
    switch (mode) {
      case 1: // モードが１の場合
        gc.drawLine(px, py, x, y); // 仮の画用紙に描画
        gc2.drawImage(img, 0, 0, this);
        break;
      case 2: // モードが２の場合
        gc.drawLine(px, py, x, y); // 仮の画用紙に描画
        break;
      case 3: // モードが３の場合
        gc.drawRect(X, Y, ow, oh); // 仮の画用紙に描画
        break;
      case 4: // モードが４の場合
        gc.fillRect(X, Y, ow, oh); // 仮の画用紙に描画
        break;
      case 5:
        gc.drawOval(X, Y, ow, oh); // 仮の画用紙に描画
        break;
      case 6:
        gc.fillOval(X, Y, ow, oh); // 仮の画用紙に描画
        break;
      case 9:
        // gc.setColor(Color.BLACK);
        // gc.drawRect(x, y, 26, 26);
        if (isEraser) {//クリックされている間のみ
          gc.setColor(getBackground());//背景の色を取得
          gc.fillRect(x, y, 30, 30);//仮の画用紙に描画
          gc2.drawImage(img, 0, 0, this);//二つ目の仮の画用紙に描画
        }
    }
    g.drawImage(img, 0, 0, this); // 仮の画用紙の内容を MyCanvas に描画
  }

  // ■ メソッド
  // 下記のマウス関連のメソッドは，MouseListener をインターフェースとして実装しているため
  // 例え使わなくても必ず実装しなければならない
  public void mouseClicked(MouseEvent e) {
  } // 今回は使わないが、無いとコンパイルエラー

  public void mouseEntered(MouseEvent e) {
  } // 今回は使わないが、無いとコンパイルエラー

  public void mouseExited(MouseEvent e) {
  } // 今回は使わないが、無いとコンパイルエラー

  public void mousePressed(MouseEvent e) { // マウスボタンが押された時の処理
    switch (mode) {
      case 1: // mode が１の場合，次の内容を実行する
        x = e.getX();
        y = e.getY();
        break;
      case 2: // mode が２もしくは
      case 3: // ３もしくは
      case 4: // ４もしくは
      case 5: // ５もしくは
      case 6: // ６の場合
        px = e.getX();
        py = e.getY();
        ow = px;
        oh = py;
        break;
      case 9:
        x = e.getX() - 13;
        y = e.getY() - 13;
        isEraser = true;
        repaint();
    }
    gc2.drawImage(img, 0, 0, this);// 二枚目の仮画用紙にコピー
  }

  public void mouseReleased(MouseEvent e) { // マウスボタンが離された時の処理
    switch (mode) {
      case 2: // mode が２もしくは
      case 3: // ３もしくは
      case 4: // ４もしくは
      case 5: // ５もしくは
      case 6: // ６の場合
        x = e.getX();
        y = e.getY();
        ow = Math.abs(x - px);
        oh = Math.abs(y - py);
        X = Math.min(px, x); // 左上の点を決定
        Y = Math.min(py, y); // 左上の点を決定
        break;
      case 9:
        x = e.getX() - 13;
        y = e.getY() - 13;
        isEraser = false;
        gc2.drawImage(img, 0, 0, this);
    }
    if (mode != 9)
      gc.drawImage(img2, 0, 0, this);// 二枚目の仮画用紙にコピー

    repaint(); // 再描画
  }

  // ■ メソッド
  // 下記のマウス関連のメソッドは，MouseMotionListener をインターフェースとして実装しているため
  // 例え使わなくても必ず実装しなければならない
  public void mouseDragged(MouseEvent e) { // マウスがドラッグされた時の処理
    switch (mode) {
      case 1: // mode が１の場合，次の内容を実行する
        px = x;
        py = y;
        x = e.getX();
        y = e.getY();
        break;
      case 2: // mode が２もしくは
      case 3: // ３もしくは
      case 4: // ４もしくは
      case 5: // ５もしくは
      case 6: // ６の場合，次の内容を実行する
        x = e.getX();
        y = e.getY();
        ow = Math.abs(x - px);
        oh = Math.abs(y - py);
        X = Math.min(px, x); // 左上の点を決定
        Y = Math.min(py, y); // 左上の点を決定
        break;
      case 9:
        x = e.getX() - 13;
        y = e.getY() - 13;
    }
    repaint(); // 再描画
  }

  public void mouseMoved(MouseEvent e) {
    // switch (mode) {
    // case 9:
    // x = e.getX() - 13;
    // y = e.getY() - 13;
    // repaint(); // 再描画
    // break;
    // }
  }
}

// 色を変更するクラス
class SetColor extends JFrame implements ActionListener {
  private JButton colorButton; // 色選択ボタン
  private DrawingApli00 drawingApp; // 色を変更する対象の描画アプリケーション

  public SetColor(DrawingApli00 drawingApp) {
    this.drawingApp = drawingApp;

    colorButton = new JButton("Choose Color");
    colorButton.addActionListener(this);

    JPanel panel = new JPanel();
    panel.add(colorButton);

    add(panel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == colorButton) {
      // 色を選択するためのウィンドウを表示
      Color selectedColor = JColorChooser.showDialog(this, "Choose Color", drawingApp.getCurrentColor());
      if (selectedColor != null) {
        drawingApp.setCurrentColor(selectedColor);
      }
    }
  }
}
