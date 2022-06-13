package breakout;

import org.json.JSONException;

import javax.swing.JFrame;
import java.awt.*;
import java.lang.*;

/**
 * Clase Breakout extiende JFrame
 * es la clase principal del juego
 */
public class Breakout extends JFrame {
    /**
     * Contructor de Breakout
     * @param mode modo de juego
     * @throws JSONException Error de JSON
     */
    public Breakout(Integer mode) throws JSONException {
        if (mode == 1) {
            initUIPlayer();
        }
        if (mode == 2) {
            initUISpectator();
        }
    }

    /**
     * initUIPlayer inicializa el juego como jugador
     * @throws JSONException Error JSON
     */
    public void initUIPlayer() throws JSONException {

        add(new Board());
        setTitle("Breakout Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }

    /**
     * initUISpectator inicializa el juego como expectador
     * @throws JSONException Error JSON
     */
    public void initUISpectator() throws JSONException {

        add(new Spectator());
        setTitle("Breakout Spectator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }
}
