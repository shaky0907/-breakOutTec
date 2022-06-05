package breakout;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Board<T> extends JPanel {

    private Timer timer;
    private String message = "GAME OVER";

    private String score_msg = "SCORE";

    private Integer score;

    private String ballsLeft_msg = "LIVES";

    private Integer ballsLeft;

    private String level_msg = "LEVEL";

    private Integer level;
    private Ball[] ball;
    private Paddle paddle;
    private Brick[] bricks;
    private Boolean inGame = true;

    private Integer numBalls;

    public Board() {

        initBoard();
    }

    private void initBoard() {

        setBackground(Color.DARK_GRAY);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));

        gameInit(0, 3 ,1, 1);
    }

    private void gameInit(Integer scr, Integer lvs, Integer lvl, Integer balls) {

        numBalls = balls;
        //System.out.println(numBalls);
        bricks = new Brick[Commons.N_OF_BRICKS];
        ball = new Ball[numBalls];

        for (Integer i = 0; i < ball.length; i++){

            ball[i] = new Ball(lvl, lvl * -1);
        }
        paddle = new Paddle();
        score = scr;
        ballsLeft = lvs;
        level = lvl;

        Integer k = 0;

        //bricks[0] = new Brick(11 * 42 + 7, 1 * 7 + 50, "red");
        //bricks[1] = new Brick(13 * 42 + 7, 1 * 7 + 50, "red");
        for (Integer i = 0; i < 8; i++) {
            for (Integer j = 0; j < 14; j++) {
                if (i < 2) {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "red");
                    k++;
                }
                else if (i < 4) {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "orange");
                    k++;
                }
                else if (i < 6) {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "yellow");
                    k++;
                }
                else {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "green");
                    k++;
                }
            }
        }

        timer = new Timer(Commons.PERIOD, new GameCycle());
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (inGame) {

            drawObjects(g2d);
        } else {

            gameFinished(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics2D g2d) {
        for (Ball value : ball) {
            if (value == null) {
                continue;
            }
            g2d.drawImage(value.getImage(), value.getX(), value.getY(),
                    value.getImageWidth(), value.getImageHeight(), this);
        }
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                paddle.getImageWidth(), paddle.getImageHeight(), this);

        var font1 = new Font("Verdana", Font.PLAIN, 12);
        FontMetrics fontMetrics = this.getFontMetrics(font1);
        var font2 = new Font("Verdana", Font.BOLD, 18);
        FontMetrics fontMetrics2 = this.getFontMetrics(font2);

        g2d.setColor(Color.WHITE);
        g2d.setFont(font1);

        g2d.drawString(score_msg, 5, 15);
        g2d.drawString(ballsLeft_msg, (Commons.WIDTH / 2) - (fontMetrics.stringWidth(ballsLeft_msg) / 2), 15);
        g2d.drawString(level_msg, (Commons.WIDTH -5) - fontMetrics.stringWidth(level_msg), 15);

        g2d.setFont(font2);
        g2d.drawString(score.toString(), 5, 35);
        g2d.drawString(ballsLeft.toString() + "/3", (Commons.WIDTH / 2) - (fontMetrics.stringWidth(ballsLeft_msg) / 2), 35);
        g2d.drawString(level.toString(), (Commons.WIDTH -5) - fontMetrics.stringWidth(level.toString()), 35);

        for (Integer i = 0; i < Commons.N_OF_BRICKS; i++) {

            if (!bricks[i].isDestroyed()) {

                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                        bricks[i].getY(), bricks[i].getImageWidth(),
                        bricks[i].getImageHeight(), this);
            }
        }
    }

    private void gameFinished(Graphics2D g2d) {

        var font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(message,
                (Commons.WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.HEIGHT / 2 - 20);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            paddle.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            paddle.keyPressed(e);
        }
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private void doGameCycle() {
        for (Ball value : ball) {
            if (value == null) {
                continue;
            }
            value.move();
        }
        paddle.move();
        checkCollision();
        repaint();
    }

    private void stopGame() {

        inGame = false;
        timer.stop();
    }

    private void checkCollision() {

        for (Ball value : ball) {
            if (value == null) {
                continue;
            }
            if (value.getRect().getMaxY() > Commons.BOTTOM_EDGE) {
                if (ballsLeft > 0 && numBalls == 1) {
                    timer.stop();
                    ballsLeft--;
                    gameInit(score, ballsLeft, level, numBalls);
                }
                else if (ballsLeft > 0 && numBalls > 1) {
                    List<Ball> list = new ArrayList<Ball>((Collection<? extends Ball>) Arrays.asList(ball));
                    list.remove(value);
                    ball = list.toArray(ball);
                    numBalls--;
                }
                else {
                    stopGame();
                }
            }
        }
        for (Integer i = 0, j = 0; i < Commons.N_OF_BRICKS; i++) {

            if (bricks[i].isDestroyed()) {

                j++;
            }

            if (j == Commons.N_OF_BRICKS) {

                timer.stop();
                level++;
                gameInit(score, ballsLeft ,level, numBalls);
            }
        }
        for (Ball value : ball) {
            if (value == null) {
                continue;
            }
            if ((value.getRect()).intersects(paddle.getRect())) {
                Double paddleLPosAux = paddle.getRect().getMinX();
                Integer paddleLPos = paddleLPosAux.intValue();
                Double ballLPosAux = value.getRect().getMinX();
                Integer ballLPos = ballLPosAux.intValue();

                Double first = paddleLPos + paddle.getRect().getWidth()/5;
                Double second = paddleLPos + (paddle.getRect().getWidth()/5)*2;
                Double third = paddleLPos + (paddle.getRect().getWidth()/5)*3;
                Double fourth = paddleLPos + (paddle.getRect().getWidth()/5)*4;

                if (ballLPos < first) {

                    value.setXDir((value.getInitialXDir() * -1) - 1);
                    value.setYDir(value.getInitialYDir());
                }
                if (ballLPos >= first && ballLPos < second) {

                    value.setXDir(value.getInitialXDir() * -1);
                    value.setYDir(value.getInitialYDir());
                }
                if (ballLPos >= second && ballLPos < third) {

                    value.setXDir(0);
                    value.setYDir(value.getInitialYDir());
                }
                if (ballLPos >= third && ballLPos < fourth) {

                    value.setXDir(value.getInitialXDir());
                    value.setYDir(value.getInitialYDir());
                }
                if (ballLPos >= fourth) {

                    value.setXDir(value.getInitialXDir() + 1);
                    value.setYDir(value.getInitialYDir());
                }
            }
        }

        for (Ball value : ball) {
            if (value == null) {
                continue;
            }
            for (Integer i = 0; i < Commons.N_OF_BRICKS; i++) {

                if ((value.getRect()).intersects(bricks[i].getRect())) {

                    Double ballLeftAux = value.getRect().getMinX();
                    Integer ballLeft = ballLeftAux.intValue();
                    Double ballHeightAux = value.getRect().getHeight();
                    Integer ballHeight = ballHeightAux.intValue();
                    Double ballWidthAux = value.getRect().getWidth();
                    Integer ballWidth = ballWidthAux.intValue();
                    Double ballTopAux = value.getRect().getMinY();
                    Integer ballTop = ballTopAux.intValue();

                    var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                    var pointLeft = new Point(ballLeft - 1, ballTop);
                    var pointTop = new Point(ballLeft, ballTop - 1);
                    var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                    if (!bricks[i].isDestroyed()) {

                        if (bricks[i].getRect().contains(pointRight)) {

                            value.setXDir(value.getInitialXDir() * -1);
                        } else if (bricks[i].getRect().contains(pointLeft)) {

                            value.setXDir(value.getInitialXDir());
                        }

                        if (bricks[i].getRect().contains(pointTop)) {

                            value.setYDir(value.getInitialYDir() * -1);
                        } else if (bricks[i].getRect().contains(pointBottom)) {

                            value.setYDir(value.getInitialYDir());
                        }

                        bricks[i].setDestroyed(true);
                    }
                }
            }
        }
    }
}
