package CalendarFunctions;

import Main.game;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;

    public class calendarMainPage extends JFrame {
        private JTable calendarTable;
        private JLabel monthLabel, eventInfoLabel;
        private YearMonth currentYearMonth;
        private JPanel bottomPanel, buttonPanel, navigationPanel;
        private JButton addButton, editButton, pButton, nButton;
        String fileName;
        String selectedDate;
        HashSet<LocalDate> highlightDays = new HashSet<>();
        ArrayList<String> lines = new ArrayList<>();
        String[] importedEvents;
        int[] dateIntArrayForCalendar;
        String[] clearedImportedEvents;
        public calendarMainPage(String s) throws IOException {
            fileName = s;
            setImportedEvents();
            getIntArray();
            clarifyImportedEvents();
            setSize(600, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            currentYearMonth = YearMonth.now();
            monthLabel = new JLabel(currentYearMonth.getMonth() + " " + currentYearMonth.getYear(), JLabel.CENTER);
            monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
            add(monthLabel, BorderLayout.NORTH);
            addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {
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
            String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            calendarTable = new JTable(tableModel) {
                @Override
                public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    Object value = getValueAt(row, column);
                    if (value != null && !value.toString().isEmpty()) {
                        int day = Integer.parseInt(value.toString());
                        LocalDate date = currentYearMonth.atDay(day);
                        if (highlightDays.contains(date)) {
                            c.setBackground(Color.YELLOW); // Highlight color
                        } else {
                            c.setBackground(Color.WHITE); // Default color
                        }
                    }
                    return c;
                }
            };
            calendarTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        int selectedRow = calendarTable.getSelectedRow();
                        int selectedColumn = calendarTable.getSelectedColumn();

                        if (selectedRow != -1 && selectedColumn != -1) {
                            Object selectedValueObj = calendarTable.getValueAt(selectedRow, selectedColumn);


                            if (selectedValueObj != null && !selectedValueObj.toString().isEmpty()) {
                                try {
                                    int selectedDay = Integer.parseInt(selectedValueObj.toString());
                                    LocalDate selectedDate = currentYearMonth.atDay(selectedDay);


                                    highlightDates(dateIntArrayForCalendar);


                                    if (highlightDays.contains(selectedDate)) {

                                        updateBottomPanelForHighlightedDate(selectedDate);
                                    } else {

                                        updateBottomPanelForNonHighlightedDate();
                                    }

                                } catch (NumberFormatException ex) {
                                    System.out.println("Selected value is not a valid number: " + selectedValueObj);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else {
                                System.out.println("Empty or invalid cell selected.");
                            }
                        }
                    }
                }
            });
            add(new JScrollPane(calendarTable), BorderLayout.CENTER);

            // Panel for navigation buttons and bottom panel components
            bottomPanel = new JPanel(new BorderLayout());
            add(bottomPanel, BorderLayout.SOUTH);
            addNavigationPanel();
            addBottomPanelComponents();
            updateCalendar();
            setVisible(true);
        }

        private void updateCalendar() throws IOException {
            DefaultTableModel model = (DefaultTableModel) calendarTable.getModel();
            model.setRowCount(0);
            monthLabel.setText(currentYearMonth.getMonth() + " " + currentYearMonth.getYear());

            int daysInMonth = currentYearMonth.lengthOfMonth();
            LocalDate firstOfMonth = currentYearMonth.atDay(1);
            int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;

            int day = 1;
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
            setImportedEvents();
            getIntArray();
            clarifyImportedEvents();
            highlightDates(dateIntArrayForCalendar);
            calendarTable.repaint();
        }

        private void addNavigationPanel() {
            // Navigation panel with Previous and Next buttons
            navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pButton = new JButton("Previous");
            nButton = new JButton("Next");
            navigationPanel.add(pButton);
            navigationPanel.add(nButton);

            bottomPanel.add(navigationPanel, BorderLayout.CENTER);

            pButton.addActionListener(e -> {
                calendarTable.clearSelection();
                currentYearMonth = currentYearMonth.minusMonths(1);
                try {
                    updateCalendar();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            nButton.addActionListener(e -> {
                calendarTable.clearSelection();
                currentYearMonth = currentYearMonth.plusMonths(1);
                try {
                    updateCalendar();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        private void addBottomPanelComponents() {
            // Panel for buttons
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(buttonPanel, BorderLayout.EAST);

            eventInfoLabel = new JLabel("");
            bottomPanel.add(eventInfoLabel, BorderLayout.WEST);
        }

        private void updateBottomPanelForHighlightedDate(LocalDate selectedDate) {
            buttonPanel.removeAll();
            adButton("Edit Event", 100, 30, buttonPanel, importedEvents);
            String eventDetails = getEventDetailsForDate(selectedDate);
            eventInfoLabel.setText(eventDetails);
            buttonPanel.revalidate();
            buttonPanel.repaint();
        }

        private void updateBottomPanelForNonHighlightedDate() {
            buttonPanel.removeAll();
            adButton("Add Event", 100, 30, buttonPanel, importedEvents);
            adButton("Edit Event", 100, 30, buttonPanel, importedEvents);

            eventInfoLabel.setText("");

            buttonPanel.revalidate();
            buttonPanel.repaint();
        }

        private String getEventDetailsForDate(LocalDate selectedDate) {
            String dateString = String.format("%04d%02d%02d", selectedDate.getYear(), selectedDate.getMonthValue(), selectedDate.getDayOfMonth());
            int index = 0;
            for (String event : importedEvents) {
                if (event.startsWith(dateString)) {
                    String temp = clearedImportedEvents[index];
                    return temp;
                }
                index++;
            }
            return "No event details found.";
        }

        public void highlightDates(int[] dateArray) throws IOException {
            highlightDays.clear();
            setImportedEvents();
            clarifyImportedEvents();
            for (int dateInt : dateArray) {
                int year = dateInt / 10000;
                System.out.println(year);
                int month = (dateInt / 100) % 100;
                System.out.println(month);
                int day = dateInt % 100;
                System.out.println(day);

                if (month >= 1 && month <= 12) {
                    try {
                        LocalDate date = LocalDate.of(year, month, day);
                        highlightDays.add(date);
                    } catch (DateTimeException ex) {
                        System.out.println("Invalid date: " + year + "-" + month + "-" + day);
                    }
                } else {
                    System.out.println("Invalid month value: " + month);
                }
            }
        }

        public void adButton(String string, int width, int height, JPanel buttonPanel, String[] events) {
            JButton b = new JButton(string);
            b.setMinimumSize(new Dimension(width, height));
            b.setPreferredSize(new Dimension(width, height));
            b.setMaximumSize(new Dimension(width, height));
            b.setFont(new Font("Arial", Font.PLAIN, height / 2));  // Font size relative to button height
            b.setBackground(new Color(126, 217, 87));  // Red, Green, Blue

            b.addActionListener(e -> {
                if (string.substring(0).contains("Add")) {
                    // To do list
                    new AddButton(fileName,"Cal");
                }
                if (string.substring(0).contains("Edit")) {
                    try {
                        new EditEvent(fileName, null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            buttonPanel.add(b);
        }

        public void setImportedEvents() throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                if (!br.ready()) {
                    throw new IOException();
                }
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            importedEvents = new String[lines.size() + 1];
            lines.toArray(importedEvents);
            int i = 0;
            while (importedEvents[i] != null) {
                System.out.println(importedEvents[i]);
                i++;
            }
        }

        public void getIntArray() {
            dateIntArrayForCalendar = new int[importedEvents.length - 1];
            for (int i = 0; i < importedEvents.length - 1; i++) {
                dateIntArrayForCalendar[i] = Integer.parseInt(importedEvents[i].substring(0, 8));
                System.out.println(dateIntArrayForCalendar[i]);
            }
        }
        public void clarifyImportedEvents(){
            clearedImportedEvents = new String[importedEvents.length];
            int i=0;
            while (i<=importedEvents.length-2){
                String temp = importedEvents[i];
                temp = temp.replace("@","");
                temp = temp.replace("#","");
                int intDateBefore = Integer.parseInt(temp.substring(0,8));
                int tempDay = intDateBefore%100;
                int tempMonth = (intDateBefore/100)%100;
                int tempYear = intDateBefore/10000;
                String monthName = null;
                switch(tempMonth){
                    case 1:
                        monthName = "Jan";
                        break;
                    case 2:
                        monthName = "Feb";
                        break;
                    case 3:
                        monthName = "Mar";
                        break;
                    case 4:
                        monthName = "Apr";
                        break;
                    case 5:
                        monthName = "May";
                        break;
                    case 6:
                        monthName = "Jun";
                        break;
                    case 7:
                        monthName = "Jul";
                        break;
                    case 8:
                        monthName = "Aug";
                        break;
                    case 9:
                        monthName = "Sep";
                        break;
                    case 10:
                        monthName = "Oct";
                        break;
                    case 11:
                        monthName = "Nov";
                        break;
                    case 12:
                        monthName = "Dec";
                        break;
                }
                String temptemp = ""+tempDay;
                if (temptemp.length() == 1){
                    temptemp = temptemp.format("%01d",tempDay);
                }
                clearedImportedEvents[i] = monthName +" "+temptemp + " "+tempYear+ ","+temp.substring(9);
                System.out.println(clearedImportedEvents[i]);
                i++;
            }

        }
    }