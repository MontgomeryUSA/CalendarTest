package Main;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.IOException;
import java.text.NumberFormat;
public class Countdown extends JFrame implements ActionListener {
    long remaining;
    long lastUpdate;
    JLabel label;
    Timer timer;
    NumberFormat format;
    JTextField minuteField, secondField;
    JButton setTimeButton, startButton;
    String fileName;
    public Countdown(String s) {
        fileName =s;
        init(); }
    public void init() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
            timer.stop();

                try {
                    new game(fileName,"test1");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }


            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Minutes:"));
        minuteField = new JTextField("00", 2);
        inputPanel.add(minuteField);
        inputPanel.add(new JLabel("Seconds:"));
        secondField = new JTextField("00", 2);
        inputPanel.add(secondField);
        setTimeButton = new JButton("Set Time");
        setTimeButton.addActionListener(new ActionListener()
        { @Override public void actionPerformed(ActionEvent e)
        { setTime(); } }); inputPanel.add(setTimeButton);
        startButton = new JButton("Start Countdown");
        startButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e)
            { start(); } }); inputPanel.add(startButton);
        label = new JLabel("00:00"); label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(label, BorderLayout.CENTER);
        format = NumberFormat.getNumberInstance(); format.setMinimumIntegerDigits(2);
        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
    }
    public void setTime() {
        int minutes = Integer.parseInt(minuteField.getText());
        int seconds = Integer.parseInt(secondField.getText());
        remaining = (minutes * 60000) + (seconds * 1000);
        label.setText(format.format(minutes) + ":" + format.format(seconds)); }
    public void start() { resume(); } void resume() {
        lastUpdate = System.currentTimeMillis(); timer.start();  }
    void pause() {
        long now = System.currentTimeMillis();
        remaining -= (now - lastUpdate); timer.stop();  }
    void updateDisplay() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        remaining -= elapsed;
        lastUpdate = now;
        if (remaining < 0) remaining = 0;{
        int minutes = (int) (remaining / 60000);
        int seconds = (int) ((remaining / 1000) % 60);
        label.setText(format.format(minutes) + ":" + format.format(seconds)); }
        if (remaining == 0) { timer.stop(); } }
    public void actionPerformed(ActionEvent e) { updateDisplay(); }
    }


