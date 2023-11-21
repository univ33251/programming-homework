import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Canvas implements KeyListener {
    // フィールド変数
    Image buf; // 仮の画面としての buffer に使うオブジェクト (Image クラス)
    Graphics buf_gc; // buffer のグラフィックスコンテキスト (c) 用オブジェクト
    Dimension d; // アプレットの大きさを管理するオブジェクト

    private int i, j;
    private int imgW, imgH; // キャンバスの大きさ
    private int Maxhp = 10; // 体力の最大値
    private int enmyNum = 30; // 敵の数
    private double enmyprobability = 0.1; // 敵の出現確率
    private double Itemprobability = 0.3; // アイテムの出現確率
    private int ftrBltNum = 20; // 自機の弾の数
    private int ItemBltNum = 40; // アイテムの弾の数
    private boolean ItemB = false; // アイテムフラグ
    private int ItemBcount = 0; // アイテムBのクールタイム
    private int enmyBltNum;
    private int n = 3; // n角形
    private int rand;
    private int BltUser = 0; // 弾を使う敵の数
    private int ItemNum = 20; // アイテムの数
    private int mode = -1; // -1: タイトル画面、-2: ゲームオーバー、1: ゲームステージ
    private int score = 0; // スコア
    private boolean addscore = false; // スコア加算フラグ
    private boolean adddy = false; // 加速フラグ
    private int firstdy = 0; // 初期の速さ
    // 背景,自機,敵機,弾,アイテム,爆発
    private Image scenery, GameOverImage, TitleImage, ftrImage, enmyImage, BulletImage, ItemAImage, ItemBImage,
            explosion;
    Fighter ftr; // 自機
    FighterBullet ftrBlt[] = new FighterBullet[ftrBltNum]; // 自機の弾
    FighterBullet ItemBlt[] = new FighterBullet[ItemBltNum]; // アイテムの弾
    EnemyBullet enmyBlt[];
    MovingObject enmy[] = new MovingObject[enmyNum], enmys[] = new MovingObject[3]; // 敵
    MovingObject Item[] = new MovingObject[ItemNum], Items[] = new MovingObject[2]; // アイテム
    // コンストラクタ

    GameMaster(int imgW, int imgH) { // 引数はアプレット本体 ゲームの初期化を行う
        this.imgW = imgW;
        this.imgH = imgH;
        setSize(imgW, imgH);
        GameOverImage = getToolkit().getImage("img/GameOverImage.jpeg");
        TitleImage = getToolkit().getImage("img/TitleImage.jpeg");
        ftrImage = getToolkit().getImage("img/ftrImage.png");
        enmyImage = getToolkit().getImage("img/Enemy2.png");
        BulletImage = getToolkit().getImage("img/BulletImage2.png");
        ItemAImage = getToolkit().getImage("img/ItemAImage.png");
        ItemBImage = getToolkit().getImage("img/ItemBImage.png");
        explosion = getToolkit().getImage("img/explosion2.png");
        scenery = getToolkit().getImage("img/scenery3.jpg");
        //背景用の画像の取り込みが完了しているか確認
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(scenery, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addKeyListener(this);

        ftr = new Fighter(imgW, imgH);
        for (i = 0; i < ftrBltNum; i++)
            ftrBlt[i] = new FighterBullet();

        for (i = 0; i < ItemBltNum; i++)
            ItemBlt[i] = new FighterBullet();

        for (i = 0; i < enmyNum; i++) {
            rand = (int) Math.round(Math.random() * 2);
            enmys[0] = new EnemyA(imgW, imgH);
            enmys[1] = new EnemyB(imgW, imgH);
            enmys[2] = new EnemyC(imgW, imgH, n);
            enmy[i] = enmys[rand];
            if (rand == 2)
                BltUser += 1;
        }
        for (i = 0; i < ItemNum; i++) {
            rand = (int) Math.round(Math.random());
            Items[0] = new ItemA();
            Items[1] = new ItemB();
            Item[i] = Items[rand];
        }
        enmyBltNum = 20 * BltUser;
        enmyBlt = new EnemyBullet[enmyBltNum];
        for (i = 0; i < enmyBltNum; i++)
            enmyBlt[i] = new EnemyBullet();
    }

    public void addNotify() {
        super.addNotify();
        buf = createImage(imgW, imgH);
        buf_gc = buf.getGraphics();
    }

    public void paint(Graphics g) {

        buf_gc.setColor(Color.white);
        // buf_gc.fillRect(0, 0, imgW, imgH);
        switch (mode) {
            case -2: // ゲームオーバー画面
                buf_gc.drawImage(GameOverImage, 0, 0, imgW, imgH, this);
                break;

            case -1: // タイトル画面
                // 初期化
                score = 0;
                ftr.hp = Maxhp;
                enmyprobability = 0.3;
                ftr.x = (int) (imgW / 2);
                ftr.y = (int) (imgH * 0.9);
                ItemBcount = 0;
                ItemB = false;
                for (i = 0; i < ftrBltNum; i++)
                    ftrBlt[i].hp = 0;
                for (i = 0; i < ItemBltNum; i++)
                    ItemBlt[i].hp = 0;
                for (i = 0; i < ItemNum; i++)
                    Item[i].hp = 0;
                for (i = 0; i < enmyNum; i++) {
                    enmy[i].hp = 0;
                    enmy[i].dy -= firstdy;
                    enmy[i].addexpcond = false;
                }
                for (i = 0; i < enmyBltNum; i++) {
                    enmyBlt[i].hp = 0;
                    enmyBlt[i].dy -= firstdy;
                }
                firstdy = 0;
                buf_gc.drawImage(TitleImage, 0, 0, imgW, imgH, this);
                break;

            default: // ゲーム中
                // 背景の表示
                buf_gc.drawImage(scenery, 0, 0, imgW, imgH, this);
                // ランダムに敵を生成
                makeEnemy: if (Math.random() < enmyprobability) {
                    for (i = 0; i < enmyNum; i++) {
                        if (enmy[i].hp < 1) {
                            enmy[i].revive(imgW, imgH);
                            break makeEnemy;
                        }
                    }
                }

                // 自機の弾を発射
                if (ftr.sflag && ftr.delaytime == 0) { // スペースキーとクールタイムの判定
                    for (i = 0; i < ftrBltNum; i++) {
                        if (ftrBlt[i].hp == 0) {
                            ftrBlt[i].revive(ftr.x, ftr.y);
                            if (ItemB) {
                                ItemBlt[2 * i].revive(ftr.x - 20, ftr.y - 20);
                                ItemBlt[2 * i + 1].revive(ftr.x + 20, ftr.y - 20);
                            }
                            ftr.delaytime = 5;
                            break;
                        }
                    }
                } else if (ftr.delaytime > 0)
                    ftr.delaytime--;

                // 敵機の弾
                int a = BltUser; // 保存用の変数
                for (i = 0; i < enmyNum; i++) {
                    if (enmy[i].bullet && BltUser > 0) {// 弾を持つ敵であるか判定
                        if (enmy[i].delaytime == 0 && enmy[i].judge && enmy[i].hp > 0) {// クールタイム
                            for (j = BltUser * 20 - 1; (BltUser * 20 - 20) <= j; j--) {
                                if (enmyBlt[j].hp == 0) {// 弾が存在していなければ発射
                                    enmyBlt[j].revive(enmy[i].x, enmy[i].y + enmy[i].w + 1);
                                    enmy[i].delaytime = 50;
                                    break;
                                }
                            }
                        } else if (enmy[i].delaytime > 0)
                            enmy[i].delaytime--;
                        BltUser -= 1;
                    }
                }
                BltUser = a;

                // 各オブジェクト間のチェック
                for (i = 0; i < enmyNum; i++) {
                    if (enmy[i].hp > 0) {
                        ftr.collisionCheck(enmy[i]);
                        for (j = 0; j < ftrBltNum; j++) {
                            if (ftrBlt[j].hp > 0) {
                                ftrBlt[j].collisionCheck(enmy[i]);
                            }
                            if (ItemBlt[j].hp > 0) {
                                ItemBlt[2 * j].collisionCheck(enmy[i]);
                                ItemBlt[2 * j + 1].collisionCheck(enmy[i]);
                            }
                            if (enmy[i].hp == 0) {
                                addscore = true; // 自機との接触か弾で敵機が死んだらフラグon
                                explosion(buf_gc, enmy[i]);// 爆発開始
                                enmy[i].expcond = 1;
                            }
                        }
                        if (addscore) {
                            score += 10;
                            addscore = false;
                            if (Math.random() < Itemprobability) {// 確率でアイテムドロップ
                                for (j = 0; j < ItemNum - 5; j++) {
                                    j += (int) Math.round(Math.random() * 5);
                                    if (Item[j].hp == 0) {
                                        Item[j].revive(enmy[i].x, enmy[i].y);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    // 爆発アニメーション
                    if (enmy[i].expcond > 0) {
                        explosion(buf_gc, enmy[i]);
                        if (enmy[i].addexpcond) {
                            enmy[i].expcond++;
                            enmy[i].addexpcond = false;
                        } else
                            enmy[i].addexpcond = true;
                        if (enmy[i].expcond >= 5)
                            enmy[i].expcond = 0;
                    }

                }
                for (i = 0; i < enmyBltNum; i++) {
                    if (enmyBlt[i].hp > 0) {
                        ftr.collisionCheck(enmyBlt[i]);
                        for (j = 0; j < ftrBltNum; j++) {
                            if (ftrBlt[j].hp > 0) {
                                ftrBlt[j].collisionCheck(enmyBlt[i]);
                            }
                            if (ItemBlt[j].hp > 0) {
                                ItemBlt[2 * j].collisionCheck(enmyBlt[i]);
                                ItemBlt[2 * j + 1].collisionCheck(enmyBlt[i]);
                            }
                        }
                    }
                }
                for (i = 0; i < ItemNum; i++) {
                    if (Item[i].hp > 0) {
                        if (ftr.collisionCheck(Item[i])) {
                            if (Item[i].ItemA)
                                ftr.hp += 1;
                            if (Item[i].ItemB) {
                                ItemB = true;
                                ItemBcount = 0;
                            }
                        }
                    }

                }
                if (ItemB)
                    ItemBcount += 1;
                if (ItemBcount > 150)
                    ItemB = false;

                // 自機の生死判断
                if (ftr.hp < 1)
                    mode = -2; // ゲーム終了

                // ゲームオブジェクトの描画&移動
                for (i = 0; i < enmyNum; i++) {
                    enmy[i].move(buf_gc, imgW, imgH, enmyImage);
                }

                for (i = 0; i < ftrBltNum; i++) {
                    ftrBlt[i].move(buf_gc, imgW, imgH, BulletImage);
                    ItemBlt[2 * i].move(buf_gc, imgW, imgH, BulletImage);
                    ItemBlt[2 * i + 1].move(buf_gc, imgW, imgH, BulletImage);
                }

                for (i = 0; i < enmyBltNum; i++) {
                    enmyBlt[i].move(buf_gc, imgW, imgH, BulletImage);
                }

                for (i = 0; i < ItemNum; i++) {
                    if (Item[i].ItemA)
                        Item[i].move(buf_gc, imgW, imgH, ItemAImage);
                    else
                        Item[i].move(buf_gc, imgW, imgH, ItemBImage);
                }

                ftr.move(buf_gc, imgW, imgH, ftrImage);

                if (score % 100 == 0 && adddy) {// スコア100ごとに加速
                    firstdy += 1;
                    for (i = 0; i < enmyNum; i++) {
                        enmy[i].dy += 1;
                    }
                    for (i = 0; i < enmyBltNum; i++) {
                        enmyBlt[i].dy += 1;
                    }
                    enmyprobability += 0.05; // 出現確率の調整
                    adddy = false;
                }
                if (score % 100 != 0)
                    adddy = true;

                // 状態チェック
                // for (i = 0; i < enmyNum; i++) {
                // System.out.print(enmy[i].hp + " ");
                // }
                // System.out.print(ftr.hp);
                // System.out.println("");
        }
        // HPとスコアの表示
        buf_gc.setColor(Color.blue);
        buf_gc.drawString("HP:" + ftr.hp, 10, 10);
        buf_gc.drawString("SCORE:" + score, 10, 20);
        g.drawImage(buf, 0, 0, this); // bufferの貼り付け
    }

    public void update(Graphics gc) {// repaintに呼ばれる
        paint(gc);
    }

    public void keyTyped(KeyEvent ke) {
        // 今回は使わないが実装が必要
    }

    public void keyPressed(KeyEvent ke) {
        int code = ke.getKeyCode();

        switch (code) {
            case KeyEvent.VK_LEFT: // ←キーを押したら
                ftr.lflag = true; // フラグon
                break;

            case KeyEvent.VK_RIGHT: // →キー
                ftr.rflag = true;
                break;

            case KeyEvent.VK_UP: // ↑キー
                ftr.uflag = true;
                break;

            case KeyEvent.VK_DOWN: // ↓キー
                ftr.dflag = true;
                break;

            case KeyEvent.VK_SPACE: // スペースキー
                ftr.sflag = true;
                if (mode != 1) {
                    mode++;
                }
                break;
        }
    }

    public void keyReleased(KeyEvent ke) {
        int code = ke.getKeyCode();

        switch (code) {
            case KeyEvent.VK_LEFT: // ←キーを離したら
                ftr.lflag = false; // フラグoff
                break;

            case KeyEvent.VK_RIGHT: // →キー
                ftr.rflag = false;
                break;

            case KeyEvent.VK_UP: // ↑キー
                ftr.uflag = false;
                break;

            case KeyEvent.VK_DOWN: // ↓キー
                ftr.dflag = false;
                break;

            case KeyEvent.VK_SPACE: // スペースキー
                ftr.sflag = false;
                break;
        }
    }

    // 爆発する画像を表示
    public void explosion(Graphics buf, MovingObject enmy) {
        if (enmy.expcond >= 3)
            buf.drawImage(explosion, enmy.x - enmy.w, enmy.y - enmy.h, enmy.x + enmy.w, enmy.y + enmy.h,
                    300 * (enmy.expcond - 2), 382, 300 * (enmy.expcond - 2) + 290, 652, null);
        else
            buf.drawImage(explosion, enmy.x - enmy.w, enmy.y - enmy.h, enmy.x + enmy.w, enmy.y + enmy.h,
                    300 * enmy.expcond, 29, 300 * enmy.expcond + 290, 299, null);
    }
}
