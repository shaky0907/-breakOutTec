package breakout;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.lang.*;

/**
 * Clase Paddle extiende Sprite
 */
public class Paddle extends Sprite  {

    private Integer dx = 0;
    private Integer size = 1;

    /**
     * Construtor de Paddle
     * @param size tamaño del paddle
     */
    public Paddle(String size) {
        
        initPaddle(size);
    }

    /**
     * initPaddle inicializa el paddle
     * @param size tamaño
     */
    private void initPaddle(String size) {

        loadImage(size);
        getImageDimensions();

        resetState();
    }

    /**
     * loadImage carga la imagen segun el tamaño
     * @param size tamaño
     */
    private void loadImage(String size) {
        
        var ii = new ImageIcon("src/resources/paddle" + size + ".png");
        image = ii.getImage();        
    }

    /**
     * move mueve el paddle
     */
    void move() {
        x += dx;

        if (x <= 0) {

            x = 0;
        }

        if (x >= Commons.WIDTH - imageWidth) {

            x = Commons.WIDTH - imageWidth;
        }
    }

    /**
     * keyPressed revisa el evento del teclado presionado
     * @param e evento
     */
    void keyPressed(KeyEvent e) {

        Integer key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -5;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 5;
        }
    }

    /**
     * keyPressed revisa el evento del teclado soltado
     * @param e evento
     */
    void keyReleased(KeyEvent e) {

        Integer key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }

    /**
     * resetState resetea el estado del paddle
     */
    private void resetState() {

        x = Commons.INIT_PADDLE_X;
        y = Commons.INIT_PADDLE_Y;
    }

    /**
     * getX retorna la posicion en X
     * @return posicion en X
     */
    Integer getX(){

        return x;
    }

    /**
     * getY retorna la posicion en Y
     * @return posicion en Y
     */
    Integer getY(){
        return y;
    }


    void change_size(Integer sizel){
        this.size = sizel;
        switch (this.size){
            case 0:
                loadImage("small");
                break;
            case 1:
                loadImage("normal");
                break;
            case 2:
                loadImage("big");
                break;
        }
    };

    /**
     * get_size retorna el tamaño del paddle
     * @return tamaño
     */
    Integer get_size(){
        return this.size;
    };

    /**
     * set_size setea el tamaño del paddle
     * @param size tamaño
     */
    void set_size(Integer size) {
        this.size = size;
    }
}


