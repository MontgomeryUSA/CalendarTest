package CalendarFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

import Main.GamePanel;
import Main.game;
import org.jdatepicker.impl.*;

public class AddButton {
    String docName= null;
    String[] events = new String[365];
    int d = 0; //day variable for start date
    int m = 0; //month variable for start date
    int y = 0; //year variable for start date
    //Label,textField, Button declarations
    JLabel eventName = new JLabel("Name of Event");
    JLabel eventTime = new JLabel("Time of Event");
    JButton confirmButton = new JButton("Confirm");
    JTextField eventNameText = new JTextField();
    //Time Box declarations and fitting
    //array creation for feeding into dropdown
    String[] hours ={"00","01" ,"02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    String[] minutes ={"00","01" ,"02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
    //Creates combo boxes(dropdowns) for the start time of events and adds action listeners
    JComboBox timeBoxStartH = new JComboBox(hours);
    JComboBox timeBoxStartM = new JComboBox(minutes);
    JDatePickerImpl datePicker;
    JFrame editFrame;
    String screen;
    //creates dropdowns for the end time
    JComboBox timeBoxEndH = new JComboBox(hours);
    JComboBox timeBoxEndM = new JComboBox(minutes);
    public AddButton(String p, String state){
        docName = p;
        screen = state;
        setStartThing();
        openEditWindow();
        System.out.println(docName);
    }

        public void openEditWindow (){
            editFrame = new JFrame("Add Event");
            editFrame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JTextField eventNameText = new JTextField();

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
            confirmButton = new JButton("Confirm");
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            editFrame.add(confirmButton, gbc);

            editFrame.setSize(500, 300);
            editFrame.setLocationRelativeTo(null);
            editFrame.setVisible(true);
            editFrame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(docName, true));
                        int i = 0;

                        while (events[i] != null) {
                            writer.write(events[i]);
                            writer.newLine();
                            i++;
                        }
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(screen.equalsIgnoreCase("todo")){
                        try {
                            new game(docName,"todo");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    try {
                        new Sorter(docName);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    editFrame.dispose();
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
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == confirmButton){
                        int i = 0;
                        //checks for first empty array spot
                        while (events[i] != null) {
                            i += 1;
                        }
                        //Gets the selected Date in a Date format
                        Date selectedDate = (Date)datePicker.getModel().getValue();
                        //TEST PRINT
                        System.out.println(selectedDate);
                        //Gets the date in a string format, returns, as an example, Fri Nov 08 13:47:25 CST 2024
                        String temp = selectedDate.toString();
                        //Declares int for date manipulation
                        int eventDate= 0;
                        //Gets the 3 letters for the month from the above string, and converts them to the numerical form so we can get the date like that
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
                        temp = String.valueOf(eventDate) + ", " + eventNameText.getText()+ ", start: #" +hours[timeBoxStartH.getSelectedIndex()] + ":" +minutes[timeBoxStartM.getSelectedIndex()]+"#" + " End: @"+ hours[timeBoxEndH.getSelectedIndex()] + ":" + minutes[timeBoxEndM.getSelectedIndex()]+"@";
                        System.out.println(temp);
                        events[i] = temp;
                        //STORAGE TEST
                        int k = 0;
                        while(events[k]!=null){
                            System.out.println(events[k]);
                            k++;
                        }
                        editFrame.dispatchEvent(new WindowEvent(editFrame, WindowEvent.WINDOW_CLOSING));
                    }
                }
            });
    }
    public void setStartThing(){
        //setting starting index for the dropdowns, starting at 00:00 (midnight)
        timeBoxStartH.setSelectedIndex(0);
        timeBoxStartM.setSelectedIndex(0);
        timeBoxEndH.setSelectedIndex(0);
        timeBoxEndM.setSelectedIndex(0);

        //Creates and Adds JDatePicker to frame
        UtilDateModel model = new UtilDateModel();
        //Pulls CST timezone for current date
        ZoneId zonedId = ZoneId.of("America/Chicago");
        //converts current date into string, as of writing returns 20240908
        String dateToday = LocalDate.now(zonedId).format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(dateToday); //TEST REPLACE THIS VARIABLE WITH SOMETHING REAL AT A LATER TIME
        //uses function written below to get the year, month, and day in int form out of the string above
        d = IntSeperatorForDates(Integer.parseInt(dateToday), "day");
        m = IntSeperatorForDates(Integer.parseInt(dateToday), "month");
        y = IntSeperatorForDates(Integer.parseInt(dateToday), "year");
        System.out.println("day:" + d + " month: " + m + " year: " + y); //Tester
        //sets start date for model
        model.setDate(y,m,d);
        //adds display properties
        Properties p  = new Properties();
        p.put("text.today", "Today");
        p.put("text.Month", "Month");
        p.put("text.year", "Year");
        //creates JDatePanel for implimentation
        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        //formats dateLabel to string
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        //adds datePicker to the frame
        //Action Listener add for button

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
}

