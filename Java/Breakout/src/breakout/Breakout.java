package breakout;

import org.json.JSONException;

import javax.swing.JFrame;
import java.awt.*;
import java.lang.*;

public class Breakout extends JFrame {
    public Breakout(Integer mode) throws JSONException {
        if (mode == 1) {
            initUIPlayer();
        }
        if (mode == 2) {
            initUISpectator();
        }
    }

    public void initUIPlayer() throws JSONException {

        add(new Board());
        setTitle("Breakout Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }

    public void initUISpectator() throws JSONException {

        add(new Spectator());
        setTitle("Breakout Spectator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }
}
