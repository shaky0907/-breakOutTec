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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.*;

public class Board extends JPanel {
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
    private Integer flag;
    private Integer[] scoresArr;
    private Integer[] powersArr;
    private static SocketClient socket;
    public Board() throws JSONException {

        initBoard();
    }

    private void initBoard() throws JSONException {

        setBackground(Color.DARK_GRAY);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));
        socket = new SocketClient("0.0.0.0", 3550);
        socket.sentString("{\"type\": 1 }");
        String info = socket.receiveString();
        JSONObject json = new JSONObject(info);
        String scores = json.getString("scores");
        String powers = json.getString("power");
        String[] scoresArray = scores.split(",");
        String[] powersArray = powers.split(",");
        Integer size = scoresArray.length;
        scoresArr = new Integer[size];
        for(Integer i=0; i<size; i++) {
            scoresArr[i] = Integer.parseInt(scoresArray[i]);
        }
        size = powersArray.length;
        powersArr = new Integer[size];
        for(Integer i=0; i<size; i++) {
            powersArr[i] = Integer.parseInt(powersArray[i]);
        }
        flag = 0;
        this.bricks = new Brick[Commons.N_OF_BRICKS];

        gameInit(0, 3 ,1, 3, bricks);
    }

    private void gameInit(Integer scr, Integer lvs, Integer lvl, Integer balls, Brick[] bricks1) {

        numBalls = balls;
        //System.out.println(numBalls);
        ball = new Ball[numBalls];

        for (Integer i = 0; i < numBalls; i++) {

            ball[i] = new Ball(lvl, lvl * -1);
        }
        paddle = new Paddle();
        score = scr;
        ballsLeft = lvs;
        level = lvl;

        Integer k = 0;

        //bricks[0] = new Brick(11 * 42 + 7, 1 * 7 + 50, "red");
        //bricks[1] = new Brick(13 * 42 + 7, 1 * 7 + 50, "red");
        if (flag == 0){
            for (Integer i = 0; i < 8; i++) {
                for (Integer j = 0; j < 14; j++) {
                    if (i < 2) {
                        bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "red", scoresArr[3], powersArr[k]);
                        k++;
                    } else if (i < 4) {
                        bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "orange", scoresArr[2], powersArr[k]);
                        k++;
                    } else if (i < 6) {
                        bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "yellow", scoresArr[1], powersArr[k]);
                        k++;
                    } else {
                        bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "green", scoresArr[0], powersArr[k]);
                        k++;
                    }
                }
            }
        }
        else {
            bricks = bricks1;
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
        socket.sentString(parseJson(ballsLeft, score, numBalls, level, paddle, ball, bricks));
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
                if (ballsLeft > 1 && numBalls == 1) {
                    timer.stop();
                    ballsLeft--;
                    flag++;
                    gameInit(score, ballsLeft, level, numBalls, bricks);
                }
                else if (ballsLeft > 1 && numBalls > 1) {
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
                flag = 0;
                gameInit(score, ballsLeft ,level, numBalls, bricks);
                socket.sentString("{ \"Type\":1 }");
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

                        score += bricks[i].getValue();
                        bricks[i].setDestroyed(true);
                    }
                }
            }
        }
    }
    String parseJson(Integer lives, Integer score, Integer numBalls, Integer level, Paddle paddle, Ball[] ball, Brick[] bricks){
        String paddlePos = "\"";
        StringBuilder ballsPos = new StringBuilder("\"");
        StringBuilder bricksLeft = new StringBuilder("\"");
        String json = "";

        //System.out.println(json);

        paddlePos += paddle.getX().toString() + "," + paddle.getY().toString() + "\"";

        for (Integer i = 0;  i < numBalls; i++){
            if (ball[i] != null) {
                if (i == numBalls - 1) {
                    ballsPos.append(i.toString()).append(",").append(ball[i].getX().toString()).append(",").append(ball[i].getY().toString());
                } else {
                    ballsPos.append(i.toString()).append(",").append(ball[i].getX().toString()).append(",").append(ball[i].getY().toString()).append(",");
                }
            }
        }
        ballsPos.append("\"");


        for (Integer i = 0; i < 112; i++){
            if(!bricks[i].isDestroyed()) {
                //System.out.println(i.toString() + "\",\"" + bricks[i].getX().toString()+ "\",\"" + bricks[i].getY().toString() + "\",\"");
                if (i == 111) {
                    bricksLeft.append(i.toString()).append(",").append(bricks[i].getX().toString()).append(",").append(bricks[i].getY().toString());
                } else {
                    bricksLeft.append(i.toString()).append(",").append(bricks[i].getX().toString()).append(",").append(bricks[i].getY().toString()).append(",");
                }
            }
        }
        if (bricksLeft.charAt(bricksLeft.length() - 1 ) == ',') {
            bricksLeft = new StringBuilder(bricksLeft.substring(0, bricksLeft.length() - 1));
        }
        bricksLeft.append("\"");

        json =
                "{ \"lives\": " + lives.toString() +
                        ", \"score\": " + score.toString() +
                        ", \"level\": " + level.toString() +
                        ", \"numBalls\": " + numBalls.toString() +
                        ", \"paddlePos\": " + paddlePos +
                        ", \"ballsPos\": " + ballsPos +
                        ", \"bricksLeft\": " + bricksLeft +
                        " }";

        return json;
    }
}
