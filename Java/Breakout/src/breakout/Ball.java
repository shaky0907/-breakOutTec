package breakout;

import javax.swing.ImageIcon;
import java.lang.*;

/**
 *Clase Ball se encarga del movimiento de la bola
 */
public class Ball extends Sprite {


    private Integer xdir;
    private Integer ydir;
    private Integer initialXDir;
    private Integer initialYDir;

    /**
     *Constructor principal
     * @param xd posicion x
     * @param yd posicion y
     */
    public Ball(Integer xd, Integer yd) {

        initBall(xd, yd);
    }

    /**
     *initBall se encarga de inicializar la bola
     * @param xd posicion x
     * @param yd posicion y
     */
    private void initBall(Integer xd, Integer yd) {
        
        this.xdir = xd;
        this.ydir = yd;
        this.initialXDir = xd;
        this.initialYDir = yd;

        loadImage();
        getImageDimensions();
        resetState();
    }

    /**
     *loadImage carga el sprite de la bola
     */
    private void loadImage() {

        var ii = new ImageIcon("src/resources/ball.png");
        image = ii.getImage();
    }


    /**
     *move se encarga de mover la bola
     */
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

    /**
     * resetState se encarga de reiniciar la bola
     */
    private void resetState() {

        x = Commons.INIT_BALL_X;
        y = Commons.INIT_BALL_Y;
    }

    /**
     * setXDir cambia la velocidad de la bola en el eje x
     * @param x velocidadX
     */
    void setXDir(Integer x) {

        xdir = x;
    }

    /**
     * setYDir cambia la velocidad de la bola en el eje y
     * @param y velocidadY
     */
    void setYDir(Integer y) {

        ydir = y;
    }

    /**
     * getXdir retorna la posicion X
     * @return xdir
     */
    Integer getXDir() {

        return xdir;
    }

    /**
     * getYDir retorna la posicion y
     * @return ydir
     */
    Integer getYDir() {

        return ydir;
    }


    /**
     * getInitialXdir
     * @return velocidad inicialX
     */
    Integer getInitialXDir() {

        return initialXDir;
    }

    /**
     * getInitialYdir
     * @return velocidad inicialY
     */
    Integer getInitialYDir() {

        return initialYDir;
    }

    /**
     * setInitialXDir indica cual es la velocidad inicial X
     * @param newXDir velocidad X
     */
    void setInitialXDir(Integer newXDir) {

        initialXDir = newXDir;
    }

    /**
     * setInitialYDir indica cual es la velocidad inicial X
     * @param newYDir velocidad Y
     */
    void setInitialYDir(Integer newYDir) {

        initialYDir = newYDir;
    }
}
