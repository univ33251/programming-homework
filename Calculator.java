import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// CalculatorクラスはJFrameを拡張し、ActionListenerを実装
public class Calculator extends JFrame implements ActionListener {
  // フィールド変数
  JButton bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, btClr, reverse, percent, equal, decimal, plus, minus, multi, div;
  JTextField txt1;  // 計算結果や入力を表示するテキストフィールド
  String ss = "0";  // テキストフィールドに表示される文字列
  double value1 = 0.0, value2 = 0.0;  // 計算に使用する値
  boolean isArithmetic = false;  // 四則演算子であるかどうか
  boolean operator = false; // 四則演算子の入力後か否か
  int calcmode = -1;  // どの四則演算を行っているか
  boolean numstart = true;  // 数字の先頭であるか
  boolean calc = false;  // 計算済みであるか
  boolean result = false;  // 結果が出ているか
  double percentage;  // 百分率を保存するための変数
  boolean percentflag;  // 百分率保存フラグ

  // メインメソッド
  public static void main(String[] args) {
    Calculator cal = new Calculator();
  }

  // コンストラクタ
  Calculator() {
    super("Calculator");  // JFrameのタイトルを設定
    setSize(300, 200);  // JFrameのサイズを設定

    setLayout(new BorderLayout());  // レイアウトマネージャーをBorderLayoutに設定

    // NORTHにテキストフィールドを設定する
    txt1 = new JTextField();
    add(txt1, BorderLayout.NORTH);

    // パネルを一枚作成し、CENTERにパネルをはめこむ内部をGridLayoutにする
    // パネルをGridLayoutにし、パネル内部にボタン16個配置する
    JPanel p_center = new JPanel();
    p_center.setLayout(new GridLayout(4, 4));

    // ボタンの配置
    btClr = new JButton("AC");
    p_center.add(btClr);
    reverse = new JButton("+/-");
    p_center.add(reverse);
    percent = new JButton("%");
    p_center.add(percent);
    div = new JButton("÷");
    p_center.add(div);
    bt7 = new JButton("7");
    p_center.add(bt7);
    bt8 = new JButton("8");
    p_center.add(bt8);
    bt9 = new JButton("9");
    p_center.add(bt9);
    multi = new JButton("×");
    p_center.add(multi);
    bt4 = new JButton("4");
    p_center.add(bt4);
    bt5 = new JButton("5");
    p_center.add(bt5);
    bt6 = new JButton("6");
    p_center.add(bt6);
    minus = new JButton("-");
    p_center.add(minus);
    bt1 = new JButton("1");
    p_center.add(bt1);
    bt2 = new JButton("2");
    p_center.add(bt2);
    bt3 = new JButton("3");
    p_center.add(bt3);
    plus = new JButton("+");
    p_center.add(plus);

    // ActionListenerの設定
    btClr.addActionListener(new ClearButton());
    reverse.addActionListener(new OperatorButton());
    percent.addActionListener(new OperatorButton());
    plus.addActionListener(new OperatorButton());
    minus.addActionListener(new OperatorButton());
    multi.addActionListener(new OperatorButton());
    div.addActionListener(new OperatorButton());
    bt1.addActionListener(new NumberButton());
    bt2.addActionListener(new NumberButton());
    bt3.addActionListener(new NumberButton());
    bt4.addActionListener(new NumberButton());
    bt5.addActionListener(new NumberButton());
    bt6.addActionListener(new NumberButton());
    bt7.addActionListener(new NumberButton());
    bt8.addActionListener(new NumberButton());
    bt9.addActionListener(new NumberButton());

    add(p_center, BorderLayout.CENTER);

    // パネルをもう一枚作成し、SOUTHにパネルをはめこむ
    // そのパネルをGridLayoutにし、内部にボタンを3個配置する
    JPanel p_south = new JPanel();
    p_south.setLayout(new GridLayout(1, 3));

    // ボタンの配置
    bt0 = new JButton("0");
    p_south.add(bt0);
    decimal = new JButton(".");
    p_south.add(decimal);
    equal = new JButton("=");
    p_south.add(equal);

    // ActionListenerの設定
    bt0.addActionListener(new NumberButton());
    decimal.addActionListener(new NumberButton());
    equal.addActionListener(new OperatorButton());

    add(p_south, BorderLayout.SOUTH);

    txt1.setText(ss);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ウィンドウを閉じる時の動作を設定
    setVisible(true);  // ウィンドウを表示
  }

  // 数字ボタンのアクションを定義するクラス
  class NumberButton implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JButton btn = (JButton) e.getSource();
      btClr.setText("C");

      // 前回の計算結果が表示されている場合
      if (result && !isArithmetic) {
        plus.setBackground(null);
        minus.setBackground(null);
        multi.setBackground(null);
        div.setBackground(null);
        value1 = value2;
      }

      if (numstart)
        ss = "";
      ss = ss + btn.getText();
      if (!operator) {
        if (btn != decimal) {
          try {
            value1 = Double.parseDouble(ss);
          } catch (NumberFormatException error) {
            ss = "エラー";
            btClr.setText("AC");
          }
        }
      }
      if (btn != decimal) {
        try {
          value2 = Double.parseDouble(ss);
        } catch (NumberFormatException error) {
          ss = "エラー";
          btClr.setText("AC");
        }
      }
      if (ss.equals(".")) {
        ss = "0.";
      }
      numstart = false;
      percentflag = false;
      txt1.setText(ss);
      repaint();
      System.out.println(value1 + " " + value2);
    }
  }

  // 演算子ボタンのアクションを定義するクラス
  class OperatorButton implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JButton btn = (JButton) e.getSource();
      if (btn == reverse) {
        if (percentflag) {
          ss = "-0";
        } else if (Double.parseDouble(ss) >= 0) {
          ss = "-" + ss;
        } else {
          ss = String.valueOf(Math.abs(Double.parseDouble(ss)));
        }
        try {
          if (!operator) {
            value1 = Double.parseDouble(ss);
            if (value1 == (int) value1)
              ss = String.valueOf((int) value1);
            else
              ss = String.valueOf(value1);
          } else {
            value2 = Double.parseDouble(ss);
            if (value2 == (int) value2)
              ss = String.valueOf((int) value2);
            else
              ss = String.valueOf(value1);
          }
        } catch (NumberFormatException error) {
          ss = "エラー";
          btClr.setText("AC");
        }
      } else if (btn == percent) {
        if (!operator) {
          value1 = Double.parseDouble(ss) / 100.0;
          if (value1 == (int) value1)
            ss = String.valueOf((int) value1);
          else
            ss = String.valueOf(value1);
        } else {
          if (calcmode == 2 || calcmode == 3) {
            value2 = Double.parseDouble(ss) / 100.0;
          } else {
            if (!percentflag) {
              percentage = Double.parseDouble(ss) / 100.0;
              percentflag = true;
            }
            value2 = Double.parseDouble(ss) * percentage;
          }
          if (value2 == (int) value2)
            ss = String.valueOf((int) value2);
          else
            ss = String.valueOf(value2);
        }
      } else if (isArithmetic && btn != equal) {
        switch (calcmode) {
          case 0:
            if (ss.equals("")) {
              value1 = 0.0;
            } else if (!numstart && operator) {
              value1 += value2;
            }
            break;

          case 1:
            if (ss.equals("")) {
              value1 = 0.0;
            } else if (!numstart && operator) {
              value1 -= value2;
            }
            break;
          case 2:
            if (ss.equals("")) {
              value1 = 0.0;
            } else if (!numstart && operator) {
              value1 *= value2;
            }
            break;
          case 3:
            if (ss.equals("")) {
              value1 = 0.0;
            } else if (!numstart && operator) {
              value1 /= value2;
            }
            if (value2 == 0) {
              ss = "エラー";
              btClr.setText("AC");
            }
          default:
            break;
        }
        if (value1 == (int) value1)
          ss = String.valueOf((int) value1);
        else
          ss = String.valueOf(value1);
      }
      isArithmetic = false;
      if (btn == plus) {
        // 色の初期化
        minus.setBackground(null);
        multi.setBackground(null);
        div.setBackground(null);
        plus.setBackground(Color.cyan);

        if (result) {
          value2 = Double.parseDouble(ss);
        }
        calcmode = 0;
        isArithmetic = true;
        numstart = true;
        operator = true;
      }

      else if (btn == minus) {
        // 色の初期化
        plus.setBackground(null);
        multi.setBackground(null);
        div.setBackground(null);
        minus.setBackground(Color.cyan);
        if (result) {
          value2 = Double.parseDouble(ss);
        }
        calcmode = 1;
        isArithmetic = true;
        numstart = true;
        operator = true;
      }

      else if (btn == multi) {
        // 色の初期化
        plus.setBackground(null);
        minus.setBackground(null);
        div.setBackground(null);
        multi.setBackground(Color.cyan);
        if (result) {
          value2 = Double.parseDouble(ss);
        }
        operator = true;
        numstart = true;
        calcmode = 2;
        isArithmetic = true;
      } else if (btn == div) {
        // 色の初期化
        plus.setBackground(null);
        minus.setBackground(null);
        multi.setBackground(null);
        div.setBackground(Color.cyan);
        if (ss.equals("")) {
          value1 = 0.0;
        } else if (!numstart && operator) {
          value1 /= value2;
        }
        operator = true;
        numstart = true;
        calcmode = 3;
        isArithmetic = true;
      }

      else if (btn == equal) {
        operator = false;
        switch (calcmode) {
          case 0:// 和
            value1 += value2;
            break;
          case 1:// 差
            value1 -= value2;
            break;
          case 2:// 積
            value1 *= value2;
            break;
          case 3:// 商
            value1 /= value2;
            if (value2 == 0) {
              ss = "エラー";
              btClr.setText("AC");
            }
            break;

          default:
            break;
        }
        if (value1 == (int) value1)
          ss = String.valueOf((int) value1);
        else
          ss = String.valueOf(value1);
        result = true;
        numstart = true;
        isArithmetic = false;
        percentflag = false;
      }

      txt1.setText(ss);
      repaint();
      System.out.println(value1 + " " + value2);
    }
  }

  // クリアボタンのアクションを定義するクラス
  class ClearButton implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JButton btn = (JButton) e.getSource();
      if (btn.getText() == "C") {
        if (operator)
          value2 = 0;
        else
          value1 = value2;
        btClr.setText("AC");
      } else {
        plus.setBackground(null);
        minus.setBackground(null);
        multi.setBackground(null);
        div.setBackground(null);
        value1 = 0;
        value2 = 0;
        calcmode = -1;
        operator = false;
        result = false;
      }
      percentflag = false;
      numstart = true;
      ss = "0";
      txt1.setText(ss);
      repaint();
      System.out.println(value1 + " " + value2);
    }
  }

  public void actionPerformed(ActionEvent e) {

  }
}

