package Main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Test extends JFrame {
    private JTable table;
    private JButton toggleSelectionButton;

    public Test() {
        setTitle("Toggle JTable Selection Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sample data for the JTable
        String[] columnNames = {"Column 1", "Column 2", "Column 3"};
        Object[][] data = {
                {"Row 1, Col 1", "Row 1, Col 2", "Row 1, Col 3"},
                {"Row 2, Col 1", "Row 2, Col 2", "Row 2, Col 3"},
                {"Row 3, Col 1", "Row 3, Col 2", "Row 3, Col 3"}
        };

        // Create the JTable with sample data
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Create a button to toggle the selection
        toggleSelectionButton = new JButton("Toggle Selection");
        add(toggleSelectionButton, BorderLayout.SOUTH);

        // Add action listener to the button
        toggleSelectionButton.addActionListener(e -> {
            // Check if a row or cell is selected
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();

            if (selectedRow != -1 && selectedColumn != -1) {
                // If something is selected, clear the selection (unselect)
                table.clearSelection();
            } else {
                // Optionally, you can do something if nothing is selected
                System.out.println("No selection to clear.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the example
        SwingUtilities.invokeLater(Test::new);
    }
}