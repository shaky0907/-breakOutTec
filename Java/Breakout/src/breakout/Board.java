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

public class Board extends JPanel {

    private Timer timer;
    private String message = "Game Over";
    private Ball ball;
    private Paddle paddle;
    private Brick[] bricks;
    private Boolean inGame = true;

    public Board() {

        initBoard();
    }

    private void initBoard() {

        setBackground(Color.DARK_GRAY);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));

        gameInit();
    }

    private void gameInit() {

        bricks = new Brick[Commons.N_OF_BRICKS];

        ball = new Ball(1,-1);
        paddle = new Paddle();

        Integer k = 0;

        for (Integer i = 0; i < 8; i++) {
            for (Integer j = 0; j < 14; j++) {
                if (i < 2) {
                    bricks[k] = new Brick(j * 42 + 5, i * 7 + 100, "red");
                    k++;
                }
                else if (i < 4) {
                    bricks[k] = new Brick(j * 42 + 5, i * 7 + 100, "orange");
                    k++;
                }
                else if (i < 6) {
                    bricks[k] = new Brick(j * 42 + 5, i * 7 + 100, "yellow");
                    k++;
                }
                else {
                    bricks[k] = new Brick(j * 42 + 5, i * 7 + 100, "green");
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

        g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(),
                ball.getImageWidth(), ball.getImageHeight(), this);
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                paddle.getImageWidth(), paddle.getImageHeight(), this);

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

        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(message,
                (Commons.WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.WIDTH / 2);
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

        ball.move();
        paddle.move();
        checkCollision();
        repaint();
    }

    private void stopGame() {

        inGame = false;
        timer.stop();
    }

    private void checkCollision() {

        if (ball.getRect().getMaxY() > Commons.BOTTOM_EDGE) {

            stopGame();
        }

        for (Integer i = 0, j = 0; i < Commons.N_OF_BRICKS; i++) {

            if (bricks[i].isDestroyed()) {

                j++;
            }

            if (j == Commons.N_OF_BRICKS) {

                message = "Victory";
                stopGame();
            }
        }

        if ((ball.getRect()).intersects(paddle.getRect())) {
            Double paddleLPosAux = ball.getRect().getMinX();
            Integer paddleLPos = paddleLPosAux.intValue();
            Double ballLPosAux = ball.getRect().getMinX();
            Integer ballLPos = ballLPosAux.intValue();

            Integer first = paddleLPos + 8;
            Integer second = paddleLPos + 16;
            Integer third = paddleLPos + 24;
            Integer fourth = paddleLPos + 32;

            /*if (ballLPos < first) {

                ball.setXDir(ball.getInitialXDir() * -1);
                ball.setYDir(ball.getInitialYDir());
            }

            if (ballLPos >= first && ballLPos < second) {

                ball.setXDir(ball.getInitialXDir() * -1);
                ball.setYDir(ball.getInitialYDir());
            }

            if (ballLPos >= second && ballLPos < third) {

                ball.setXDir(0);
                ball.setYDir(ball.getInitialYDir());
            }

            if (ballLPos >= third && ballLPos < fourth) {

                ball.setXDir(ball.getInitialXDir());
                ball.setYDir(ball.getInitialYDir());
            }

            if (ballLPos > fourth) {

                ball.setXDir(ball.getInitialXDir());
                ball.setYDir(ball.getInitialYDir());
            }*/
            if (ballLPos < second) {

                ball.setXDir(ball.getInitialXDir() * -1);
                ball.setYDir(ball.getInitialYDir());
            }
            if (ballLPos >= second && ballLPos < third) {

                ball.setXDir(0);
                ball.setYDir(ball.getInitialYDir());
            }
            if (ballLPos >= third) {

                ball.setXDir(ball.getInitialXDir());
                ball.setYDir(ball.getInitialYDir());
            }
        }

        for (Integer i = 0; i < Commons.N_OF_BRICKS; i++) {

            if ((ball.getRect()).intersects(bricks[i].getRect())) {

                Double ballLeftAux = ball.getRect().getMinX();
                Integer ballLeft = ballLeftAux.intValue();
                Double ballHeightAux = ball.getRect().getHeight();
                Integer ballHeight = ballHeightAux.intValue();
                Double ballWidthAux = ball.getRect().getWidth();
                Integer ballWidth = ballWidthAux.intValue();
                Double ballTopAux = ball.getRect().getMinY();
                Integer ballTop = ballTopAux.intValue();

                var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                var pointLeft = new Point(ballLeft - 1, ballTop);
                var pointTop = new Point(ballLeft, ballTop - 1);
                var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                if (!bricks[i].isDestroyed()) {

                    if (bricks[i].getRect().contains(pointRight)) {

                        ball.setXDir(ball.getInitialXDir() * -1);
                    } else if (bricks[i].getRect().contains(pointLeft)) {

                        ball.setXDir(ball.getInitialXDir());
                    }

                    if (bricks[i].getRect().contains(pointTop)) {

                        ball.setYDir(ball.getInitialYDir() * -1);
                    } else if (bricks[i].getRect().contains(pointBottom)) {

                        ball.setYDir(ball.getInitialYDir());
                    }

                    bricks[i].setDestroyed(true);
                }
            }
        }
    }
}
