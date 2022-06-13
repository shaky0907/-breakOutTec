package breakout;

import java.awt.Image;
import java.awt.Rectangle;
import java.lang.*;

/**
 * Clase Sprite
 */
public class Sprite {
    Integer x;
    Integer y;
    String color;
    Integer value;
    Integer power;
    Integer imageWidth;
    Integer imageHeight;
    Image image;

    /**
     * setX setea la posicion en X
     * @param x posicion en X
     */
    protected void setX(Integer x) {

        this.x = x;
    }

    /**
     * getX retorna la posicion en X
     * @return Xpos
     */
    Integer getX() {

        return x;
    }

    /**
     * setY setea la posicion en Y
     * @param y posicion en Y
     */
    protected void setY(Integer y) {

        this.y = y;
    }

    /**
     * getY retorna la posicion en Y
     * @return Ypos
     */
    Integer getY() {

        return y;
    }

    /**
     * getImageWidth retorna el largo de la imagen
     * @return width
     */
    Integer getImageWidth() {

        return imageWidth;
    }

    /**
     * getImageHeight retorna el largo de la imagen
     * @return height
     */
    Integer getImageHeight() {

        return imageHeight;
    }

    /**+
     * getImage retorna la imagen del sprite
     * @return Image
     */
    Image getImage() {

        return image;
    }

    /**
     * getRect retorna el rectangulo de la imagen
     * @return Rectangle
     */
    Rectangle getRect() {

        return new Rectangle(x, y,
                image.getWidth(null), image.getHeight(null));
    }

    /**
     * getImageDimensions setea los dimensions a null
     */
    void getImageDimensions() {

        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
    }

    /**
     * getValue retorna el valor del sprite
     * @return value
     */
    Integer getValue(){
        return value;
    }
}
