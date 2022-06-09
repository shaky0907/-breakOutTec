package breakout;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.lang.*;

public class Paddle extends Sprite  {

    private Integer dx = 0;
    private Integer size = 1;

    public Paddle(String size) {
        
        initPaddle(size);
    }
    
    private void initPaddle(String size) {

        loadImage(size);
        getImageDimensions();

        resetState();
    }
    
    private void loadImage(String size) {
        
        var ii = new ImageIcon("src/resources/paddle" + size + ".png");
        image = ii.getImage();        
    }    

    void move() {
        x += dx;

        if (x <= 0) {

            x = 0;
        }

        if (x >= Commons.WIDTH - imageWidth) {

            x = Commons.WIDTH - imageWidth;
        }
    }

    void keyPressed(KeyEvent e) {

        Integer key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -5;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 5;
        }
    }

    void keyReleased(KeyEvent e) {

        Integer key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }

    private void resetState() {

        x = Commons.INIT_PADDLE_X;
        y = Commons.INIT_PADDLE_Y;
    }

    Integer getX(){

        return x;
    }

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

    Integer get_size(){
        return this.size;
    };
    void set_size(Integer size) {
        this.size = size;
    }
}


