package breakout;

import javax.swing.ImageIcon;
import java.lang.*;

/**
 * Clase Brick se encarga de lo relacionado con los ladrillos
 */
public class Brick extends Sprite {

    private Boolean destroyed;

    /**
     * Constructor principal
     * @param x posicion x
     * @param y posicion y
     * @param color colo del ladrillo
     * @param value el puntaje
     * @param power poder
     */
    public Brick(Integer x, Integer y, String color, Integer value, Integer power) {
        
        initBrick(x, y, color, value, power);
    }

    /**
     * initBrick inicializa el ladrillo
     * @param x posicion x
     * @param y posicion y
     * @param color colo del ladrillo
     * @param value el puntaje
     * @param power poder
     */
    private void initBrick(Integer x, Integer y, String color, Integer value, Integer power) {
        
        this.x = x;
        this.y = y;
        this.color = color;
        this.value = value;
        this.power = power;
        
        destroyed = false;

        loadImage(color);
        getImageDimensions();
    }

    /**
     * loadImage carga la imagen del ladrillo segun su color
     * @param color color del ladridllo
     */
    private void loadImage(String color) {
        String directory = "src/resources/" + color + "Brick.png";
        var ii = new ImageIcon(directory);
        image = ii.getImage();        
    }

    /**
     * isDestroyed el estado de destruido del ladrillo
     * @return boolean destroyed
     */
    Boolean isDestroyed() {
        
        return destroyed;
    }

    /**
     * setDestroyed setea el valor de destroyed
     * @param val bool esta destruido
     */
    void setDestroyed(Boolean val) {
        
        destroyed = val;
    }

    /**
     * get_power retorna el poder del ladrillo
     * @return poder
     */
    int get_power(){
      return this.power;
    };

    void setColor(String color){
        loadImage(color);
    }
}
