package CalendarFunctions;

import Main.game;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class EditEvent {
    String textFile = null;
    ArrayList<String> lines = new ArrayList<>();
    String[] importedEvents;
    JButton cb = new JButton("Exit");
    JTextField eventNameText = new JTextField();
    String[] hours ={"00","01" ,"02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    String[] minutes ={"00","01" ,"02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
    String[] clearedImportedEvents;
    JComboBox timeBoxStartH = new JComboBox(hours);
    JComboBox timeBoxStartM = new JComboBox(minutes);
    JComboBox timeBoxEndH = new JComboBox(hours);
    JComboBox timeBoxEndM = new JComboBox(minutes);
    JDatePickerImpl datePicker;
    int index= 0;
    public EditEvent(String s, String p) throws IOException {
        textFile = s;
        if(p!=null){
            setImportedEvents();
            loadEventDetails(p);
            openEditWindow(p);
        }
        else {
            setImportedEvents();
            clarifyImportedEvents();
            createGUI();
        }
    }

    public int IntSeperatorForDates(int i, String a){
        int day= 0;
        int month = 0;
        int year = 0;
        if (a.equalsIgnoreCase("day")){
            //gets last 2 digits
            day = i %100;
            return day;
        }
        else if (a.equalsIgnoreCase("month")){
            //divides by 100, int math rounds down, gets last 2 digits
            i = i/100;
            month = i%100;
            return month;
        }
        else if (a.equalsIgnoreCase("year")){
            //divides to just 4 digits, takes that as year
            i = i/10000;
            year = i;
            return year;
        }
        else{
            //error thrower
            return i;
        }
    }
    public void setImportedEvents() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))){
            if (!br.ready()) {
                throw new IOException();
            }
            String line;
            while ((line = br.readLine()) != null){
                lines.add(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        importedEvents = new String[lines.size() +1];
        lines.toArray(importedEvents);
        int i = 0;
        while (importedEvents[i] != null){
            System.out.println(importedEvents[i]);
            i++;
        }
    }
    int iobjectClickedNow = 0;
    int iobjectClickedPast = 0;
    JList list;

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
    private void loadEventDetails(String selectedEvent) {
        int dateNumFull = getDate(selectedEvent);
        setCalendar(dateNumFull);
        int indexInImportedEvents = getIndex(dateNumFull);
        index = indexInImportedEvents;
        getTimeIndex(importedEvents[indexInImportedEvents], "s1");
        getTimeIndex(importedEvents[indexInImportedEvents], "s2");
        eventNameText.setText(getName(importedEvents[indexInImportedEvents]));

    }
    public int getDate(String s){
        String tempMonth = s.substring(0,3);
        int eventDate=0;
        String eventDateS= null;
        switch(tempMonth){
            case "Jan":
                eventDate+=100;
                break;
            case "Feb":
                eventDate+=200;
                break;
            case "Mar":
                eventDate+=300;
                break;
            case "Apr":
                eventDate+=400;
                break;
            case "May":
                eventDate+=500;
                break;
            case "Jun":
                eventDate+=600;
                break;
            case "Jul":
                eventDate+=700;
                break;
            case "Aug":
                eventDate+=800;
                break;
            case "Sep":
                eventDate+=900;
                break;
            case "Oct":
                eventDate+=1000;
                break;
            case "Nov":
                eventDate+=1100;
                break;
            case "Dec":
                eventDate+=1200;
                break;

        }
        System.out.println(eventDate);
        if(s.substring(4,6).contains(" ")){
            System.out.println(s.substring(4,6));
            System.out.println(s.charAt(4));
            System.out.println(s.charAt(5));
            s= s.replaceFirst(" ","0");
            System.out.println(s);

        }
        else{
            s=s.replaceFirst(" ", "");
            System.out.println(s);
        }
        eventDate+=Integer.parseInt(s.substring(3,5));
        System.out.println(eventDateS);
        eventDate += Integer.parseInt((s.substring(6,10)))*10000;
        System.out.println(eventDate);
        return eventDate;
    }
    public int getIndex(int i){
        int k = 0;
        while (!importedEvents[k].contains(""+i)){
            k++;
        }
        return k;
    }
    public void getTimeIndex(String s, String p){
        System.out.println(s);
        int index = 0;
        if (p.equalsIgnoreCase("s1")){
            int startVar1 = s.indexOf("#") + 1;  // Get the index after the first #
            int middleVar1 = s.indexOf(":", startVar1);  // Find the index of the colon
            int endVar1 = s.indexOf("#", startVar1);  // Find the second #
            String hour1 = s.substring(startVar1, middleVar1);  // Get hours
            String minute1 = s.substring(middleVar1 + 1, endVar1);  // Get minutes
            timeBoxStartH.setSelectedIndex(Integer.parseInt(hour1));
            timeBoxStartM.setSelectedIndex(Integer.parseInt(minute1));

        }

        if (p.equalsIgnoreCase("s2")){
            int startVar1 = s.indexOf("#") + 1;  // Get the index after the first #
            int middleVar1 = s.indexOf(":", startVar1);  // Find the index of the colon
            int endVar1 = s.indexOf("#", startVar1);  // Find the second #
            String hour1 = s.substring(startVar1, middleVar1);  // Get hours
            String minute1 = s.substring(middleVar1 + 1, endVar1);  // Get minutes
            timeBoxEndH.setSelectedIndex(Integer.parseInt(hour1));
            timeBoxEndM.setSelectedIndex(Integer.parseInt(minute1));
        }
    }
    public String getName(String s){
        String name = null;
        int startVar1 = s.indexOf(",") + 1;  // Get the index after the first #
        int middleVar1 = s.indexOf(",", startVar1);  // Find the index of the colon
        name = s.substring(startVar1,middleVar1);
        System.out.println(name);
     return name;
    }
    public void setCalendar(int i){
        int d = 0; //day variable for start date
        int m = 0; //month variable for start date
        int y = 0; //year variable for start date
        d =IntSeperatorForDates(i, "day");
        System.out.println(d);
        m =IntSeperatorForDates(i, "month");
        System.out.println(m);
        y = IntSeperatorForDates(i, "year");
        System.out.println(y);
        UtilDateModel model = new UtilDateModel();
        model.setDate(y,m,d);
        Properties p  = new Properties();
        p.put("text.today", "Today");
        p.put("text.Month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }
    public void createGUI() {
        JFrame frame = new JFrame("Event Manager");
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        list = new JList<>(clearedImportedEvents);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(list);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 2));
        bottomPanel.add(new JLabel("Event Name:"));
        bottomPanel.add(eventNameText);
        bottomPanel.add(new JLabel("Start Time (HH:MM):"));
        JPanel startTimePanel = new JPanel(new FlowLayout());
        startTimePanel.add(timeBoxStartH);
        startTimePanel.add(timeBoxStartM);
        bottomPanel.add(startTimePanel);
        bottomPanel.add(new JLabel("End Time (HH:MM):"));
        JPanel endTimePanel = new JPanel(new FlowLayout());
        endTimePanel.add(timeBoxEndH);
        endTimePanel.add(timeBoxEndM);
        bottomPanel.add(endTimePanel);


        JPanel actionPanel = new JPanel();
        actionPanel.add(cb);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(listScroller, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(actionPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Listeners
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedValue = (String)list.getSelectedValue();
                    if (selectedValue != null) {
                        loadEventDetails(selectedValue);
                        openEditWindow(selectedValue);
                    }
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterList(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterList(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
                    int i = 0;

                    while (importedEvents[i] != null){
                        writer.write(importedEvents[i]);
                        writer.newLine();
                        i++;
                    }
                    writer.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    new Sorter(textFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    new game(textFile,"todo");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
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
    }
    public void filterList(String filter) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String event : clearedImportedEvents) {
            if (event != null && event.toLowerCase().contains(filter.toLowerCase())) {
                filteredList.add(event);
            }
        }
        list.setListData(filteredList.toArray(new String[0]));
    }
    public void saveEventChanges(String eventName, String startHour, String startMinute, String endHour, String endMinute, Date selectedDate) {
        selectedDate = (Date)datePicker.getModel().getValue();
        //TEST PRINT
        System.out.println(selectedDate);
        String temp = selectedDate.toString();
        int eventDate= 0;
        String tempMonth = ""+temp.charAt(4)+temp.charAt(5)+temp.charAt(6);
        switch(tempMonth){
            case "Jan":
                eventDate+=100;
                break;
            case "Feb":
                eventDate+=200;
                break;
            case "Mar":
                eventDate+=300;
                break;
            case "Apr":
                eventDate+=400;
                break;
            case "May":
                eventDate+=500;
                break;
            case "Jun":
                eventDate+=600;
                break;
            case "Jul":
                eventDate+=700;
                break;
            case "Aug":
                eventDate+=800;
                break;
            case "Sep":
                eventDate+=900;
                break;
            case "Oct":
                eventDate+=1000;
                break;
            case "Nov":
                eventDate+=1100;
                break;
            case "Dec":
                eventDate+=1200;
                break;
        }
        //gets the day digits and adds them to the event date, giving 1108 for nov 8
        eventDate += Integer.parseInt(""+temp.charAt(8)+temp.charAt(9));
        //adds the year to the front, results in 20241108 for the same date as above
        eventDate += 10000*Integer.parseInt(""+temp.charAt(24)+temp.charAt(25)+temp.charAt(26)+temp.charAt(27));
        //TEST PRINT
        System.out.println(eventDate);
        temp = String.valueOf(eventDate) + ", " + eventName+ ", start: #" +startHour + ":" +startMinute+"#" + " End: @"+ endHour + ":" + endMinute+"@";
        System.out.println(temp);
        importedEvents[index] = temp;

    }
    public void openEditWindow(String selectedEvent) {
        JFrame editFrame = new JFrame("Edit Event");
        editFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField eventNameText = new JTextField(getName(selectedEvent));

        // GridBagConstraints to arrange components
        gbc.gridx = 0;
        gbc.gridy = 0;
        editFrame.add(new JLabel("Event Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        editFrame.add(eventNameText, gbc);

        // Date Picker
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        editFrame.add(new JLabel("Event Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        editFrame.add(datePicker, gbc);

        // Start Time
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        editFrame.add(new JLabel("Start Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        editFrame.add(timeBoxStartH, gbc);
        gbc.gridx = 2;
        editFrame.add(timeBoxStartM, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        editFrame.add(new JLabel("End Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        editFrame.add(timeBoxEndH, gbc);
        gbc.gridx = 2;
        editFrame.add(timeBoxEndM, gbc);
        JButton saveButton = new JButton("Save");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        editFrame.add(saveButton, gbc);

        // Action listener for the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveEventChanges(eventNameText.getText(),
                        timeBoxStartH.getSelectedItem().toString(), timeBoxStartM.getSelectedItem().toString(),
                        timeBoxEndH.getSelectedItem().toString(), timeBoxEndM.getSelectedItem().toString(),
                        (Date) datePicker.getModel().getValue());
                editFrame.dispose();
            }
        });

        editFrame.setSize(500, 300);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
    }
    }








class MyListCellRenderer implements ListCellRenderer{

    private final JLabel jlblCell = new JLabel(" ", JLabel.LEFT);
    Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    @Override
    public Component getListCellRendererComponent(JList jList, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        jlblCell.setOpaque(true);

        if (isSelected) {
            jlblCell.setForeground(jList.getSelectionForeground());
            jlblCell.setBackground(jList.getSelectionBackground());
            jlblCell.setBorder(new LineBorder(Color.BLUE));
        } else {
            jlblCell.setForeground(jList.getForeground());
            jlblCell.setBackground(jList.getBackground());
        }

        jlblCell.setBorder(cellHasFocus ? lineBorder : emptyBorder);
        jlblCell.setText(value != null ? value.toString() : "");

        return jlblCell;
    }
}
