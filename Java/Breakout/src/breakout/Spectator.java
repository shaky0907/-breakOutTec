package breakout;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;
import java.util.Arrays;

public class Spectator extends JPanel {
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
    private Integer[] paddlePosArr;
    private Integer[] ballsPosArr;
    private Integer[] bricksLeftArr;
    private Integer bricksLeft;
    private static SocketClient socket;
    public Spectator() throws JSONException {

        initSpectator();
    }

    private void initSpectator() throws JSONException {

        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));

        socket = new SocketClient("0.0.0.0", 3550);
        socket.sentString("{\"type\": 2 }");

        ballsLeft = 3;
        score = 0;
        level = 1;

        paddle = new Paddle();
        bricks = new Brick[Commons.N_OF_BRICKS];
        ball = new Ball[10];

        for (Integer i = 0; i < 10; i++) {
            ball[i] = null;
        }

        Integer k = 0;

        for (Integer i = 0; i < 8; i++) {
            for (Integer j = 0; j < 14; j++) {
                bricks[k] = null;
                k++;
            }
        }

        gameInit();
    }

    private void gameInit() {
        timer = new Timer(Commons.PERIOD, new Spectator.SpectatorCycle());
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
            if (value != null) {
                g2d.drawImage(value.getImage(), value.getX(), value.getY(),
                        value.getImageWidth(), value.getImageHeight(), this);
            }
        }
        if (paddle != null) {
            g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                    paddle.getImageWidth(), paddle.getImageHeight(), this);
        }

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

        for (Brick value : bricks) {
            if (value != null) {
                g2d.drawImage(value.getImage(), value.getX(),
                        value.getY(), value.getImageWidth(),
                        value.getImageHeight(), this);
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

    private class SpectatorCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                doSpectatorCycle();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void doSpectatorCycle() throws JSONException {
        String info = socket.receiveString();
        JSONObject json = new JSONObject(info);
        ballsLeft = json.getInt("lives");
        score = json.getInt("score");
        level = json.getInt("level");
        numBalls = json.getInt("numBalls");
        //bricksLeft = json.getInt("bricksLeft");
        String paddlePos = json.getString("paddlePos");
        String ballsPos = json.getString("ballsPos");
        String bricksLeft = json.getString("bricksLeft");

        String[] paddlePosArray = paddlePos.split(",");
        String[] ballsPosArray = ballsPos.split(",");
        String[] bricksLeftArray = bricksLeft.split(",");

        paddlePosArr = new Integer[paddlePosArray.length];
        for(Integer i=0; i<paddlePosArray.length; i++) {
            paddlePosArr[i] = Integer.parseInt(paddlePosArray[i]);
        }
        ballsPosArr = new Integer[ballsPosArray.length];
        for(Integer i=0; i<ballsPosArray.length; i++) {
            ballsPosArr[i] = Integer.parseInt(ballsPosArray[i]);
        }
        bricksLeftArr = new Integer[bricksLeftArray.length];
        for(Integer i=0; i<bricksLeftArray.length; i++) {
            bricksLeftArr[i] = Integer.parseInt(bricksLeftArray[i]);
        }

        paddle.setX(paddlePosArr[0]);
        paddle.setY(paddlePosArr[1]);

        for (Integer j = 0; j < 112; j++){
            if (contains(bricksLeftArr, j) && bricks[j] == null){
                if (j < 28) {
                    bricks[j] = new Brick(bricksLeftArr[(j * 3) + 1],bricksLeftArr[(j * 3) + 2],"red", 0,0);
                }
                else if (j < 56) {
                    bricks[j] = new Brick(bricksLeftArr[(j * 3) + 1],bricksLeftArr[(j * 3) + 2],"orange", 0,0);
                }
                else if (j < 84) {
                    bricks[j] = new Brick(bricksLeftArr[(j * 3) + 1],bricksLeftArr[(j * 3) + 2],"yellow", 0,0);
                }
                else {
                    bricks[j] = new Brick(bricksLeftArr[(j * 3) + 1],bricksLeftArr[(j * 3) + 2],"green", 0,0);
                }
            }
            if (!contains(bricksLeftArr, j) && bricks[j] != null) {
                bricks[j] = null;
            }
        }

        for (Integer j = 0; j < ballsPosArr.length / 3; j++){
            System.out.println(Arrays.toString(ballsPosArr));
            if (ball[j] != null && !ballsPosArr[j*3].equals(j)) {
                ball[j] = null;
            }
            if (ball[j] == null && ballsPosArr[j*3].equals(j)) {
                ball[j] = new Ball(0,0);
                ball[j].setX(ballsPosArr[(j * 3) + 1]);
                ball[j].setY(ballsPosArr[(j * 3) + 2]);
                break;
            }
            else if (ball[j] != null && ballsPosArr[j*3].equals(j)) {
                ball[j].setX(ballsPosArr[(j * 3) + 1]);
                ball[j].setY(ballsPosArr[(j * 3) + 2]);
            }
        }

        repaint();
    }
    private static Boolean contains(final Integer[] arr, final Integer key){
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
}
