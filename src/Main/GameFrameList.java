package Main;


import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.*;

public class GameFrameList {
    private JFrame jFrame;

    public GameFrameList(gamePanelList gamePanel) {
        jFrame = new JFrame();
        jFrame.add(gamePanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
