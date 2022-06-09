package breakout;

import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JFrame{
    public JPanel panel1;
    private JButton WATCHButton;
    private JButton PLAYButton;
    private JLabel logoLabel;
    ImageIcon imageLogo = new ImageIcon("src/resources/logo.png");

    public MenuScreen(){
        logoLabel.setIcon(imageLogo);
        logoLabel.setText("");
        WATCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EventQueue.invokeLater(() -> {
                    Breakout game = null;
                    try {
                        game = new Breakout(2);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    game.setVisible(true);
                });
                MenuScreen.this.dispose();
            }
        });
        PLAYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EventQueue.invokeLater(() -> {
                    Breakout game = null;
                    try {
                        game = new Breakout(1);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    game.setVisible(true);
                });
                MenuScreen.this.dispose();
            }
        });
    }
    public static void main(String[] args){
        MenuScreen m = new MenuScreen();
        m.setContentPane(m.panel1);
        m.setSize(900,600);
        m.setVisible(true);
        m.setTitle("Menu");
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
