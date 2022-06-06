package breakout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JFrame{
    private JPanel panel1;
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

            }
        });
        PLAYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EventQueue.invokeLater(() -> {
                    var game = new Breakout();
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
