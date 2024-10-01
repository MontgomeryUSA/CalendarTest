package CalendarFunctions;
import Main.game;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

public class calendarMainPage extends JFrame {
    private JTable calendarTable;
    private JLabel monthLabel;
    private YearMonth currentYearMonth;
    String fileName;
    String selectedDate;
    public calendarMainPage(String s) {
        fileName = s;
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        currentYearMonth = YearMonth.now();
        monthLabel = new JLabel(currentYearMonth.getMonth() + " " + currentYearMonth.getYear(), JLabel.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(monthLabel, BorderLayout.NORTH);
        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        calendarTable = new JTable(tableModel);
        calendarTable.setSelectionMode(0);
        calendarTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            int selectedRow = calendarTable.getSelectedRow();
                            int selectedColumn = calendarTable.getSelectedColumn();

                            if (selectedRow != -1 && selectedColumn != -1) {
                                String selectedValue = (calendarTable.getValueAt(calendarTable.getSelectedRow(),calendarTable.getSelectedColumn())).toString();
                                System.out.println(selectedValue);
                                System.out.println(YearMonth.now());
                            } else {
                                System.out.println("Selection Cleared Or Error");
                            }
                        }
                    }
                });
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);
        JPanel swicthPanel = new JPanel();
        JButton pButton = new JButton("Previous");
        JButton nButton = new JButton("Next");
        swicthPanel.add(pButton);
        swicthPanel.add(nButton);
        add(swicthPanel, BorderLayout.SOUTH);
        updateCalendar();
        pButton.addActionListener(e -> {
            calendarTable.clearSelection();
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });
        nButton.addActionListener(e -> {
            calendarTable.clearSelection();
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });
        setVisible(true);
    }
    private void updateCalendar() {
        DefaultTableModel model = (DefaultTableModel) calendarTable.getModel();
        model.setRowCount(0);
        monthLabel.setText(currentYearMonth.getMonth() + " " + currentYearMonth.getYear());
        int daysInMonth = currentYearMonth.lengthOfMonth();
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int day = 1;
        int row = 0;
        Object[] week = new Object[7];

        for (int i = 0; i < startDayOfWeek; i++) {
            week[i] = "";
        }
        for (int i = startDayOfWeek; i < 7; i++) {
            week[i] = day++;
        }
        model.addRow(week);
        while (day <= daysInMonth) {
            week = new Object[7];
            for (int i = 0; i < 7 && day <= daysInMonth; i++) {
                week[i] = day++;
            }
            model.addRow(week);
        }
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
                if(string.substring(3).contains("Add")){
                    //To do list
                    new AddButton(fileName);
                }
                if(string.substring(3).contains("Edit")){
                    try {
                        new EditEvent(fileName, null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        buttonPanel.add(b);
    }
}