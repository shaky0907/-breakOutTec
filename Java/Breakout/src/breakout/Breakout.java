package breakout;

import javax.swing.JFrame;
import java.awt.*;
import java.lang.*;

public class Breakout extends JFrame {
    public Breakout() {
        initUI();
    }

    public void initUI() {

        add(new Board());
        setTitle("Breakout");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }
}
