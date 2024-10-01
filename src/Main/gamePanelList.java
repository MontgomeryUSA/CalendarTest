package Main;

import CalendarFunctions.AddButton;
import CalendarFunctions.EditEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class gamePanelList extends JPanel {
    private BufferedImage img;
    private int panelX = 1920;
    private int panelY = 1000;
    String fileName;
    JFrame todoFrame;

    public gamePanelList(String[] events, String s) {
        fileName = s;
        importImg();
        setPanelSize();
        ListScreen(events);
    }

    private void importImg() {
        try {
            img = ImageIO.read(new File("C:/Users/julia/IdeaProjects/Calendar/res/BackGroundList.png"));
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


        g.drawRect(160, 298, 380, 200);

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
                if (string.contains("Add"))
                {
                    System.out.println("add");
                    new AddButton(fileName);
                    todoFrame.dispose();
                }
                if (string.contains("Edit"))
                {
                    try {
                        System.out.println("Edit");
                        new EditEvent(fileName,null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    todoFrame.dispose();
                }
            }
        });

        buttonPanel.add(b);
    }

public void ListScreen(String[] events) {
    // Create a new JFrame for the To-Do list
    todoFrame = new JFrame("To-Do List");
    todoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    todoFrame.setSize(this.getPreferredSize());
    todoFrame.setLayout(new BorderLayout());

    // Create a panel for the top image (1/6th of the frame height)
    JPanel topImagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        }
    };
    topImagePanel.setPreferredSize(new Dimension(todoFrame.getWidth(), todoFrame.getHeight() / 6));

    // Create the list panel for the To-Do items
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

    // Add some spacing at the top
    listPanel.add(Box.createRigidArea(new Dimension(0, 20)));

    // Add the events to the list panel with larger font size
    for (String event : events) {
        if (event != null) {
            JLabel eventLabel = new JLabel(event, SwingConstants.CENTER);
            eventLabel.setFont(new Font("Arial", Font.PLAIN, 25)); // Increased font size
            eventLabel.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(eventLabel);
            listPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Space between events
        }
    }

    // Make the list panel scrollable and set a smaller size
    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(500, 400)); // Set a smaller size for the scroll pane

    // Create the panel for the buttons on the left
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setBackground(Color.WHITE);

    // Add buttons to the buttonPanel
    adButton("Open Add Event Menu", 200, 50, buttonPanel, events);
    adButton("Open Edit Event Menu", 200, 50, buttonPanel, events); // Example for a second button

    // Add some spacing around the buttons
    buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

    // Add the top image panel to the top of the frame
    todoFrame.add(topImagePanel, BorderLayout.NORTH);

    // Add the button panel to the left of the frame
    todoFrame.add(buttonPanel, BorderLayout.WEST);

    // Add the scrollable list panel to the center of the frame
    todoFrame.add(scrollPane, BorderLayout.CENTER);

    // Center the frame on the screen
    todoFrame.setLocationRelativeTo(null);

    // Make the frame visible
    todoFrame.setVisible(true);
}
}