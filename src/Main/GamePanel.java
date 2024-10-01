package Main;

import CalendarFunctions.AddButton;
import CalendarFunctions.calendarMainPage;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import javax.swing.JFrame;


public class GamePanel extends JPanel {
    private BufferedImage img;
    private int panelX = 1920;
    private int panelY = 1000;
    String fileName;
    JFrame jFrame;
    public GamePanel(String[] events, String s) {
        fileName = s;
        importImg();
        setPanelSize();
        mainPage(events);
    }

    private void importImg() {
        try {
            img = ImageIO.read(new File("H:/CS/Calendar/res/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(panelX, panelY);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate the aspect ratio of the image
        double imgWidth = img.getWidth();
        double imgHeight = img.getHeight();
        double panelWidth = getWidth();
        double panelHeight = getHeight();

        // Calculate the new dimensions while maintaining the aspect ratio
        double aspectRatio = imgWidth / imgHeight;
        int newWidth = (int) panelWidth;
        int newHeight = (int) (panelWidth / aspectRatio);

        if (newHeight > panelHeight) {
            newHeight = (int) panelHeight;
            newWidth = (int) (panelHeight * aspectRatio);
        }

        // Center the image
        int x = (getWidth() - newWidth) / 2;
        int y = (getHeight() - newHeight) / 2;

        g.drawImage(img, x, y, newWidth, newHeight, null);

    }

    public void createLabel(String strLabel, int width, int height) {
        JLabel label = new JLabel(strLabel, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setFont(new Font("Arial", Font.PLAIN, 25));
        label.setAlignmentX(CENTER_ALIGNMENT);
        this.add(label);
        this.revalidate();
        this.repaint();
    }

    public void adButton(String string, int width, int height, JPanel buttonPanel, String[] events) {
        JButton b = new JButton(string);
        b.setMinimumSize(new Dimension(width, height));
        b.setPreferredSize(new Dimension(width, height));
        b.setMaximumSize(new Dimension(width, height));
        b.setFont(new Font("Arial", Font.PLAIN, height / 2));  // Font size relative to button height
        b.setBackground(new Color(126, 217, 87));// Red, Green, Blue

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(string.substring(5).contains("To")){
                    //To do list
                    try {
                        new game(fileName, "todo");
                        jFrame.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(string.substring(5).contains("Calendar")){
                    try {
                        new calendarMainPage(fileName);
                        jFrame.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(string.substring(5).contains("Timer")){
                    new Countdown(fileName);
                    jFrame.dispose();
                }
            }
        });
        buttonPanel.add(b);
    }
    public void mainPage(String[] events) {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.add(this);
        jFrame.pack();
        jFrame.setVisible(true);

        // Set the layout of the main panel (this) to GridBagLayout for centering
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create a panel for buttons and set its layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Set button panel to Y_AXIS
        buttonPanel.setBackground(Color.WHITE); // Optional: Set background color

        // Create a panel for the first button and label
        JPanel firstButtonPanel = new JPanel();
        firstButtonPanel.setLayout(new BoxLayout(firstButtonPanel, BoxLayout.Y_AXIS)); // Vertical for label and button
        firstButtonPanel.setBackground(Color.WHITE); // Set background color to white

        JLabel label = new JLabel("To-Do List Preview:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        firstButtonPanel.add(label);
        firstButtonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        int i = Math.min(events.length, 3); // Make sure to not exceed events length

        for (int k = 0; k < i; k++) {
            JLabel firstButtonLabel = new JLabel(events[k], SwingConstants.CENTER);
            firstButtonLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            firstButtonPanel.add(firstButtonLabel);
        }

        firstButtonPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between button panels

        adButton("Open To-Do List", 380, 50, firstButtonPanel, events);
        buttonPanel.add(firstButtonPanel); // Add first button panel to button panel

        // Create a panel for the second button and label
        JPanel secondButtonPanel = new JPanel();
        secondButtonPanel.setLayout(new BoxLayout(secondButtonPanel, BoxLayout.Y_AXIS)); // Vertical for label and button
        secondButtonPanel.setBackground(Color.WHITE); // Set background color to white
        secondButtonPanel.add(Box.createRigidArea(new Dimension(0, 150)));

        adButton("Open Calendar", 380, 50, secondButtonPanel, events);
        buttonPanel.add(secondButtonPanel); // Add second button panel to button panel

        JPanel thirdButtonPanel = new JPanel();
        thirdButtonPanel.setLayout(new BoxLayout(thirdButtonPanel, BoxLayout.Y_AXIS)); // Vertical for label and button
        thirdButtonPanel.setBackground(Color.WHITE); // Set background color to white
        thirdButtonPanel.add(Box.createRigidArea(new Dimension(0, 150)));

        adButton("Open Timer", 380, 50, thirdButtonPanel, events);
        buttonPanel.add(thirdButtonPanel);
        // Add button panel to the main panel (this) using GridBagConstraints
        gbc.gridx = 0;  // Set column position
        gbc.gridy = 0;  // Set row position
        this.add(buttonPanel, gbc); // Add button panel to the main panel

        // Revalidate and repaint after adding components
        this.revalidate();
        this.repaint();
    }
    }