package breakout;

import javax.swing.*;
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
import java.util.concurrent.TimeUnit;

import org.json.*;

/**
 * Clase Board extiende JPanel
 */
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
    private String lastPower;
    private Boolean frozen;

    private static Board board_instance = null;

    /**
     * Construtor Board
     * @throws JSONException Json error
     */
    public Board() throws JSONException {

        initBoard();
    }

    public static Board getInstance() throws JSONException {
        if(board_instance == null){
            board_instance = new Board();
        }
        return board_instance;
    }

    /**
     * initBoard inicializa el board
     * @throws JSONException Json error
     */
    private void initBoard() throws JSONException {

        setBackground(Color.DARK_GRAY);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));
        socket = new SocketClient("0.0.0.0", 3550);
        socket.sentString("{\"type\": 1 }");
        String info = socket.receiveString();
        System.out.println(info);
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
        lastPower = "none";
        paddle = new Paddle("normal");
        ball = new Ball[5];
        frozen = false;
        gameInit(0, 3 ,1, 1, bricks);
    }

    /**
     * gameInit inicializa el juego
     * @param scr
     * @param lvs
     * @param lvl
     * @param balls
     * @param bricks1
     */
    private void gameInit(Integer scr, Integer lvs, Integer lvl, Integer balls, Brick[] bricks1) {

        numBalls = balls;

        for (Integer i = 0; i < numBalls; i++) {
            ball[i] = new Ball(lvl, lvl * -1);
        }

        score = scr;
        ballsLeft = lvs;
        level = lvl;

        Integer k = 0;

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

    /**
     * paintComponent override de la clase JPanel
     * @param g the object to protect
     */
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

    /**
     * drawObjects se encarga de dibujar todos los objetos en el board
     * @param g2d the object to protect
     */
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
        var font3 = new Font("Verdana", Font.BOLD, 36);
        FontMetrics fontMetrics3 = this.getFontMetrics(font3);

        g2d.setColor(Color.WHITE);
        g2d.setFont(font1);

        g2d.drawString(score_msg, 5, 15);
        g2d.drawString(ballsLeft_msg, (Commons.WIDTH - fontMetrics.stringWidth(ballsLeft_msg)) / 2, 15);
        g2d.drawString(level_msg, (Commons.WIDTH -5) - fontMetrics.stringWidth(level_msg), 15);

        g2d.setFont(font2);
        g2d.drawString(score.toString(), 5, 35);
        g2d.drawString(ballsLeft.toString() + "/3", (Commons.WIDTH / 2) - (fontMetrics2.stringWidth(ballsLeft_msg) / 2), 35);
        g2d.drawString(level.toString(), (Commons.WIDTH -5) - fontMetrics2.stringWidth(level.toString()), 35);
        g2d.drawString("LAST POWER: " + lastPower, (Commons.WIDTH / 2) - fontMetrics.stringWidth("LAST POWER: " + lastPower), (Commons.HEIGHT - 15));

        if (frozen) {
            g2d.setFont(font3);
            g2d.drawString("PAUSED", (Commons.WIDTH / 2) - fontMetrics2.stringWidth("PAUSED"), ((Commons.HEIGHT / 2 - 15)));
            timer.stop();
        }

        for (Integer i = 0; i < Commons.N_OF_BRICKS; i++) {

            if (!bricks[i].isDestroyed()) {

                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                        bricks[i].getY(), bricks[i].getImageWidth(),
                        bricks[i].getImageHeight(), this);
            }
        }
    }

    /**
     * gameFinished revisa si se acaba el juego y cambia la pantalla respectivamente
     * @param g2d the object to protect
     */
    private void gameFinished(Graphics2D g2d) {

        var font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(message,
                (Commons.WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.HEIGHT / 2 - 20);
        /*try {
            Thread.sleep(5000);
            socket.disconnect();
            MenuScreen m = new MenuScreen();
            m.setContentPane(m.panel1);
            m.setSize(900,600);
            m.setVisible(true);
            m.setTitle("Menu");
            m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(false);
            System.exit(0);
        }
        catch (Exception ignore) {}*/
    }

    /**
     * Clase TAdapter
     */
    private class TAdapter extends KeyAdapter {

        /**
         * Override keyRelease
         * @param e the event to be processed
         */
        @Override
        public void keyReleased(KeyEvent e) {

            paddle.keyReleased(e);
        }

        /**
         * Override keyPressed
         * @param e the event to be processed
         */
        @Override
        public void keyPressed(KeyEvent e) {

            paddle.keyPressed(e);
            Integer key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE && !frozen){
                frozen = true;
            }
            else if (key == KeyEvent.VK_SPACE && frozen){
                timer.start();
                frozen = false;
            }
        }
    }

    /**
     * Clase GameCyle implementa ActionListener
     */
    private class GameCycle implements ActionListener {

        /**
         * Override actionPerfomed
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                doGameCycle();
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * doGameCyle corre el juego
     * @throws JSONException JSON error
     */
    private void doGameCycle() throws JSONException {
        for (Ball value : ball) {
            if (value == null) {
                continue;
            }
            value.move();
        }
        paddle.move();
        checkCollision();
        repaint();
        socket.sentString(parseJson(ballsLeft, score, numBalls, level, paddle, ball, bricks, lastPower));
    }

    /**
     * stopGame para el juego
     */
    private void stopGame() {

        inGame = false;
        timer.stop();
    }

    /**
     * checkCollision revisa las colisiones del juego
     * @throws JSONException JSON ERROR
     */
    private void checkCollision() throws JSONException {

        for (Integer i = 0; i < 5; i++) {
            if (ball[i] == null) {
                continue;
            }
            else if (ball[i].getRect().getMaxY() > Commons.BOTTOM_EDGE) {
                if (ballsLeft > 1 && numBalls == 1) {
                    timer.stop();
                    ballsLeft--;
                    flag++;
                    ball[i] = null;
                    gameInit(score, ballsLeft, level, numBalls, bricks);
                }
                else if (ballsLeft > 1 && numBalls > 1) {
                    ball[i] = null;
                    numBalls--;
                }
                else {
                    socket.sentString("lost");
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
                socket.disconnect();
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
                for(Integer k=0; k<size; k++) {
                    scoresArr[k] = Integer.parseInt(scoresArray[k]);
                }
                size = powersArray.length;
                powersArr = new Integer[size];
                for(Integer k=0; k<size; k++) {
                    powersArr[k] = Integer.parseInt(powersArray[k]);
                }
                gameInit(score, ballsLeft ,level, numBalls, bricks);
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
                        Integer power = bricks[i].get_power();
                        switch (power){
                            case 1:
                                //Incrementa la cantidad de vidas
                                ballsLeft++;
                                lastPower = "Added new life";
                                break;
                            case 2:
                                //incrementa la cantidad de bolas en el board
                                if (numBalls < 5) {
                                    /*Ball new_ball = new Ball(level, level * -1);
                                    List<Ball> list = new ArrayList<Ball>((Collection<? extends Ball>) Arrays.asList(ball));
                                    list.add(new_ball);*/
                                    for (Integer a = 0; a < 5; a++){
                                        if (ball[a] == null){
                                            ball[a] = new Ball(level, level * -1);
                                            break;
                                        }
                                    }
                                    numBalls++;
                                    lastPower = "Added new ball";
                                }
                                break;
                            case 3:
                                //duplicar tamaño
                                if (paddle.get_size() == 1){
                                    Integer x = paddle.getX();
                                    Integer y = paddle.getY();
                                    paddle = new Paddle("big");
                                    paddle.setX(x);
                                    paddle.setY(y);
                                    paddle.set_size(2);
                                }
                                else if (paddle.get_size() == 0) {
                                    Integer x = paddle.getX();
                                    Integer y = paddle.getY();
                                    paddle = new Paddle ("normal");
                                    paddle.setX(x);
                                    paddle.setY(y);
                                    paddle.set_size(1);
                                }
                                lastPower = "Doubled paddle size";
                                break;
                            case 4:
                                //disminuir tamaño
                                if (paddle.get_size() == 1){
                                    Integer x = paddle.getX();
                                    Integer y = paddle.getY();
                                    paddle = new Paddle("small");
                                    paddle.setX(x);
                                    paddle.setY(y);
                                    paddle.set_size(0);
                                }
                                else if (paddle.get_size() == 2) {
                                    Integer x = paddle.getX();
                                    Integer y = paddle.getY();
                                    paddle = new Paddle ("normal");
                                    paddle.setX(x);
                                    paddle.setY(y);
                                    paddle.set_size(1);
                                }
                                lastPower = "Halfed paddle size";
                                break;
                            case 5:
                                //aumentar velocidad
                                for (Ball bola : ball) {
                                    if (bola != null) {
                                        bola.setInitialXDir(bola.getInitialXDir() + 1);
                                        bola.setInitialYDir(bola.getInitialYDir() - 1);
                                        if (bola.getXDir() > 0)
                                            bola.setXDir(bola.getXDir() + 1);
                                        else
                                            bola.setXDir(bola.getXDir() - 1);
                                        if (bola.getYDir() < 0)
                                            bola.setYDir(bola.getYDir() - 1);
                                        else
                                            bola.setYDir(bola.getYDir() + 1);

                                    }
                                }
                                lastPower = "Increased ball speed";
                                break;
                            case 6:
                                //disminuir velocidad
                                for (Ball bola : ball) {
                                    if (bola != null) {
                                        if (bola.getInitialXDir() > 1 && bola.getInitialYDir() < -1) {
                                            bola.setInitialXDir(bola.getInitialXDir() - 1);
                                            bola.setInitialYDir(bola.getInitialYDir() + 1);
                                            if (bola.getXDir() > 1)
                                                bola.setXDir(bola.getXDir() - 1);
                                            else if (bola.getXDir() < -1)
                                                bola.setXDir(bola.getXDir() + 1);
                                            if (bola.getYDir() > 1)
                                                bola.setYDir(bola.getYDir() - 1);
                                            else if (bola.getYDir() < -1)
                                                bola.setYDir(bola.getYDir() + 1);
                                        }
                                    }
                                }
                                lastPower = "Decreased ball speed";
                                break;
                        }
                    }
                }
            }
        }
    }
    String parseJson(Integer lives, Integer score, Integer numBalls, Integer level, Paddle paddle, Ball[] ball, Brick[] bricks, String lastPower){
        String paddlePos = "\"";
        StringBuilder ballsPos = new StringBuilder("\"");
        StringBuilder bricksLeft = new StringBuilder("\"");
        String json = "";

        paddlePos += paddle.getX().toString() + "," + paddle.getY().toString() + "," + paddle.get_size() + "\"";

        for (Integer i = 0;  i < 5; i++){
            if (ball[i] != null) {
                ballsPos.append(i.toString()).append(",").append(ball[i].getX().toString()).append(",").append(ball[i].getY().toString()).append(",");
            }
        }
        if (ballsPos.charAt(ballsPos.length() - 1 ) == ',') {
            ballsPos = new StringBuilder(ballsPos.substring(0, ballsPos.length() - 1));
        }
        ballsPos.append("\"");


        for (Integer i = 0; i < 112; i++){
            if(!bricks[i].isDestroyed()) {
                bricksLeft.append(i.toString()).append(",").append(bricks[i].getX().toString()).append(",").append(bricks[i].getY().toString()).append(",");
            }
        }
        if (bricksLeft.charAt(bricksLeft.length() - 1 ) == ',') {
            bricksLeft = new StringBuilder(bricksLeft.substring(0, bricksLeft.length() - 1));
        }
        bricksLeft.append("\"");

        /*if (bricksLeft.equals("\"\"")) {
            bricksLeft = new StringBuilder("\"new level\"");
        }*/

        json =
                "{ \"lives\": " + lives.toString() +
                        ", \"score\": " + score.toString() +
                        ", \"level\": " + level.toString() +
                        ", \"lastPower\": " + lastPower +
                        ", \"numBalls\": " + numBalls.toString() +
                        ", \"paddlePos\": " + paddlePos +
                        ", \"ballsPos\": " + ballsPos +
                        ", \"bricksLeft\": " + bricksLeft +
                        " }";

        return json;
    }
}
