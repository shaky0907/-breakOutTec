package breakout;

import javax.swing.ImageIcon;
import java.lang.*;

public class Brick extends Sprite {

    private Boolean destroyed;

    public Brick(Integer x, Integer y, String color, Integer value, Integer power) {
        
        initBrick(x, y, color, value, power);
    }
    
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
    
    private void loadImage(String color) {
        String directory = "src/resources/" + color + "Brick.png";
        var ii = new ImageIcon(directory);
        image = ii.getImage();        
    }

    Boolean isDestroyed() {
        
        return destroyed;
    }

    void setDestroyed(Boolean val) {
        
        destroyed = val;
    }

    void setColor(String color){
        loadImage(color);
    }
}
