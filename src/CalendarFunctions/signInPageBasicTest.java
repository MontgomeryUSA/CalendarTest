package CalendarFunctions;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import Main.*;

public class signInPageBasicTest {
    String password = null;
    String username = null;
    String fileName = "";
    public signInPageBasicTest(){
        createSignInPage();
    }

    public void createSignInPage(){
        // Creating the main frame
        JFrame f = new JFrame("Login");
        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating components
        JLabel title = new JLabel("Login");
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField jtu = new JTextField(20);  // Increased width for better fit
        JPasswordField jtp = new JPasswordField(20);  // Increased width for better fit
        JButton b = new JButton("Confirm");

        // Set a border around the text fields
        Border fieldBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
        jtu.setBorder(fieldBorder);
        jtp.setBorder(fieldBorder);

        // Customize fonts
        title.setFont(new Font("Arial", Font.BOLD, 30));  // Larger and bolder font
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        jtu.setFont(new Font("Arial", Font.PLAIN, 14));
        jtp.setFont(new Font("Arial", Font.PLAIN, 14));
        b.setFont(new Font("Arial", Font.BOLD, 14));

        // Set up the layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding around components
        gbc.anchor = GridBagConstraints.CENTER;

        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        // Add username label and text field
        gbc.gridy = 1;
        panel.add(userLabel, gbc);
        gbc.gridy = 2;
        panel.add(jtu, gbc);

        // Add password label and text field
        gbc.gridy = 3;
        panel.add(passLabel, gbc);
        gbc.gridy = 4;
        panel.add(jtp, gbc);

        // Add confirm button
        gbc.gridy = 5;
        panel.add(b, gbc);

        // Add action listener for the button
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = new String(jtp.getPassword());
                username = jtu.getText();
                if (createFile()) {
                    try {
                        new game(fileName,"test1");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    f.dispose(); // Close the sign-in window
                }
            }
        });

        // Add panel to the frame
        f.add(panel);
        f.setVisible(true);
    }

    public boolean createFile() {
        if (password != null && username != null && !password.isEmpty() && !username.isEmpty()) {
            fileName = username +password+ ".txt";  // Use username and password as file name
            File file = new File(fileName);

            try {
                // Create the file
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
                return false;
            }

            return true;
        } else {
            System.out.println("Username or password is null or empty.");
            return false;
        }
    }

    // Dummy mainPageTest class to demonstrate structure (adjust this based on your actual implementation)
    public class mainPageTest {
        public mainPageTest(String password) throws IOException {
            // Simulate main page logic
            System.out.println("Main page opened with password: " + password);
        }
    }
}
