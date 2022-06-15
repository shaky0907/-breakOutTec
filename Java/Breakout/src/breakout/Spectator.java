package breakout;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;
import java.util.Arrays;

/**
 * Clase Spectator extiende JPanel
 */
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
    private String lastPower;
    private static SocketClient socket;

    /**
     * Constructor Spectator
     * @throws JSONException JSON error
     */
    public Spectator() throws JSONException {
        socket = new SocketClient("0.0.0.0", 3550);
        socket.sentString("{\"type\": 2 }");
        initSpectator();
        lastPower = "none";
    }

    /**
     * initSpectator inicializa el espectador
     * @throws JSONException JSON error
     */
    private void initSpectator() throws JSONException {

        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));

        ballsLeft = 3;
        score = 0;
        level = 1;
        numBalls = 1;


        paddle = new Paddle("normal");
        bricks = new Brick[Commons.N_OF_BRICKS];
        ball = new Ball[5];

        for (Integer i = 0; i < 5; i++) {
            ball[i] = null;
        }

        Integer k = 0;

        for (Integer i = 0; i < 8; i++) {
            for (Integer j = 0; j < 14; j++) {
                if (i < 2) {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "red", 0, 0);
                    k++;
                } else if (i < 4) {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "orange", 0, 0);
                    k++;
                } else if (i < 6) {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "yellow", 0, 0);
                    k++;
                } else {
                    bricks[k] = new Brick(j * 42 + 7, i * 7 + 50, "green", 0, 0);
                    k++;
                }
            }
        }

        gameInit();
    }

    /**
     * gameInit inicia el juego
     */
    private void gameInit() {
        timer = new Timer(Commons.PERIOD, new Spectator.SpectatorCycle());
        timer.start();
    }

    /**
     * Override paintComponents se encarga de pintar componentes del juego
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
     * Se encarha de dibujar objetos
     * @param g2d the object to protect
     */
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
        g2d.drawString("LAST POWER: " + lastPower, (Commons.WIDTH / 2) - fontMetrics.stringWidth("LAST POWER: " + lastPower), (Commons.HEIGHT - 15));

        for (Brick value : bricks) {
            if (value != null) {
                g2d.drawImage(value.getImage(), value.getX(),
                        value.getY(), value.getImageWidth(),
                        value.getImageHeight(), this);
            }
        }
    }

    /**
     * se encarga de revisar si el juego termino y cambia la pantalla
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
    }

    private class SpectatorCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doSpectatorCycle();
        }
    }

    private void doSpectatorCycle() {
        try {
            String info = socket.receiveString();
            if (info.equals("lost")){
                inGame = false;
                timer.stop();
            }
            else{
                JSONObject json = new JSONObject(info);
                Integer livesLeft = json.getInt("lives");
                Integer numBallsLeft = json.getInt("numBalls");
                for (Integer j = 0; j < 5; j++){
                    ball[j] = null;
                }
                ballsLeft = livesLeft;
                score = json.getInt("score");
                level = json.getInt("level");
                numBalls = json.getInt("numBalls");
                lastPower = json.getString("lastPower");

                String paddlePos = json.getString("paddlePos");
                String ballsPos = json.getString("ballsPos");
                String bricksLeft = json.getString("bricksLeft");

                String[] paddlePosArray = paddlePos.split(",");
                String[] ballsPosArray = ballsPos.split(",");

                paddlePosArr = new Integer[paddlePosArray.length];
                for(Integer i=0; i<paddlePosArray.length; i++) {
                    paddlePosArr[i] = Integer.parseInt(paddlePosArray[i]);
                }
                ballsPosArr = new Integer[ballsPosArray.length];
                for(Integer i=0; i<ballsPosArray.length; i++) {
                    ballsPosArr[i] = Integer.parseInt(ballsPosArray[i]);
                }
                if (paddlePosArr[2] == 0){
                    paddle = new Paddle("small");
                }
                else if (paddlePosArr[2] == 1){
                    paddle = new Paddle("normal");
                }
                else if (paddlePosArr[2] == 2){
                    paddle = new Paddle("big");
                }

                paddle.setX(paddlePosArr[0]);
                paddle.setY(paddlePosArr[1]);

                Integer[] ballsNumLeftArr = new Integer[112];
                for(Integer i=0; i < ballsPosArr.length / 3; i++) {
                    ballsNumLeftArr[i] = ballsPosArr[i * 3];
                }
                if (bricksLeft.equals("")) {
                    initSpectator();
                }
                else {
                    String[] bricksLeftArray = bricksLeft.split(",");
                    bricksLeftArr = new Integer[bricksLeftArray.length];
                    for(Integer i=0; i<bricksLeftArray.length; i++) {
                        bricksLeftArr[i] = Integer.parseInt(bricksLeftArray[i]);
                    }
                    Integer[] bricksNumLeftArr = new Integer[112];
                    for(Integer i=0; i < bricksLeftArr.length / 3; i++) {
                        bricksNumLeftArr[i] = bricksLeftArr[i * 3];
                    }
                    for (Integer j = 0; j < 112; j++){
                        if (!contains(bricksNumLeftArr, j) && bricks[j] != null) {
                            bricks[j] = null;
                        }
                    }
                }

                for (Integer integer : ballsNumLeftArr) {
                    if (ball[integer] == null) {
                        Integer index = findIndex(ballsPosArr, integer);
                        if (index % 3 == 0) {
                            ball[integer] = new Ball(0, 0);
                            ball[integer].setX(ballsPosArr[index + 1]);
                            ball[integer].setY(ballsPosArr[index + 2]);
                        }
                    }
                }
            }
        } catch (Exception ignored) { }

        repaint();
    }

    /**
     * contains revisa si el integer pertenece al array
     * @param arr array
     * @param key elemento
     * @return boolean si pertenece
     */
    private static Boolean contains(final Integer[] arr, final Integer key){
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    /**
     * findIndex retorna el indice del elemento
     * @param arr array
     * @param t elemento
     * @return indice
     */
    private static Integer findIndex(Integer arr[], Integer t)
    {

        // if array is Null
        if (arr == null) {
            return -1;
        }

        // find length of array
        Integer len = arr.length;
        Integer i = 0;

        // traverse in the array
        while (i < len) {

            // if the i-th element is t
            // then return the index
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }
}
