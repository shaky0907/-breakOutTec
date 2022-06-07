package breakout;

import java.awt.Image;
import java.awt.Rectangle;
import java.lang.*;

public class Sprite {
    Integer x;
    Integer y;
    String color;
    Integer value;
    Integer power;
    Integer imageWidth;
    Integer imageHeight;
    Image image;
    protected void setX(Integer x) {

        this.x = x;
    }

    Integer getX() {

        return x;
    }

    protected void setY(Integer y) {

        this.y = y;
    }

    Integer getY() {

        return y;
    }

    Integer getImageWidth() {

        return imageWidth;
    }

    Integer getImageHeight() {

        return imageHeight;
    }

    Image getImage() {

        return image;
    }

    Rectangle getRect() {

        return new Rectangle(x, y,
                image.getWidth(null), image.getHeight(null));
    }

    void getImageDimensions() {

        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
    }

    Integer getValue(){
        return value;
    }
}
