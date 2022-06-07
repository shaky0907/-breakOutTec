package breakout;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.lang.*;

public class Paddle extends Sprite  {

    private Integer dx = 0;

    public Paddle() {
        
        initPaddle();        
    }
    
    private void initPaddle() {

        loadImage();
        getImageDimensions();

        resetState();
    }
    
    private void loadImage() {
        
        var ii = new ImageIcon("src/resources/paddle.png");
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
}
