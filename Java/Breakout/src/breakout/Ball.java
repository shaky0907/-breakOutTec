package breakout;

import javax.swing.ImageIcon;
import java.lang.*;

public class Ball extends Sprite {

    private Integer xdir;
    private Integer ydir;

    private Integer initialXDir;

    private Integer initialYDir;

    public Ball(Integer xd, Integer yd) {

        initBall(xd, yd);
    }

    private void initBall(Integer xd, Integer yd) {
        
        this.xdir = xd;
        this.ydir = yd;
        this.initialXDir = xd;
        this.initialYDir = yd;

        loadImage();
        getImageDimensions();
        resetState();
    }

    private void loadImage() {

        var ii = new ImageIcon("src/resources/ball.png");
        image = ii.getImage();
    }

    void move() {

        x += xdir;
        y += ydir;

        if (x <= 0 && xdir < 0) {
            setXDir(xdir * -1);
        }

        if (x >= Commons.WIDTH - imageWidth && xdir > 0) {

            //System.out.println(imageWidth);
            setXDir(xdir * -1);
        }

        if (y <= 0 && ydir < 0) {

            setYDir(initialYDir * -1);
        }
    }

    private void resetState() {

        x = Commons.INIT_BALL_X;
        y = Commons.INIT_BALL_Y;
    }

    void setXDir(Integer x) {

        xdir = x;
    }

    void setYDir(Integer y) {

        ydir = y;
    }

    Integer getXDir() {

        return xdir;
    }
    Integer getYDir() {

        return ydir;
    }

    Integer getInitialXDir() {

        return initialXDir;
    }
    Integer getInitialYDir() {

        return initialYDir;
    }

    void setInitialXDir(Integer newXDir) {

        initialXDir = newXDir;
    }
    void setInitialYDir(Integer newYDir) {

        initialYDir = newYDir;
    }
}
