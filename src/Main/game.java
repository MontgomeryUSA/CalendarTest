package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class game {
    private GameFrame gameframe;
    private GamePanel gamepanel;
    private gamePanelList gamepanelList;
    private GameFrameList gameframeList;
    String fileName;
    String[] events;
    String[] clearedImportedEvents;
    ArrayList<String> lines = new ArrayList<>();
    public game(String filename, String state) throws IOException {
        // Define the events array and sort them
        fileName = filename;

        // Sort the events as you did in the Test class
        setImportedEvents();
        sortEvents(events);
        clarifyImportedEvents();
        orderedIndex();

        // Create the GamePanel and pass the events array
        if (state.equalsIgnoreCase("todo")){
            gamepanelList = new gamePanelList(clearedImportedEvents,fileName);
        }
        if(state.equalsIgnoreCase("test1")){
            gamepanel = new GamePanel(clearedImportedEvents,fileName);
        }
    }

    private void sortEvents(String[] events) throws IOException {
        String tempVar;

        int i = 0;
        // Gets rid of "null" array indices so they are not considered
        while (i < events.length && events[i] != null) {
            i++;
        }

        // Organize in order from nearest to latest date
        for (int k = 0; k < i - 1; k++) {
            for (int j = 0; j < i - 1 - k; j++) {
                String variable1 = events[j];
                String variable2 = events[j + 1];

                // Extracting date part and parsing to integer
                int var3 = Integer.parseInt(variable1.substring(0, 8));
                int var4 = Integer.parseInt(variable2.substring(0, 8));

                // Extracting time part (hours and minutes)
                int startVar1 = variable1.indexOf("#") + 1;  // Get the index after the first #
                int middleVar1 = variable1.indexOf(":", startVar1);  // Find the index of the colon
                int endVar1 = variable1.indexOf("#", startVar1);  // Find the second #
                String hour1 = variable1.substring(startVar1, middleVar1);  // Get hours
                String minute1 = variable1.substring(middleVar1 + 1, endVar1);  // Get minutes

                int startVar2 = variable2.indexOf("#") + 1;  // Get the index after the first #
                int middleVar2 = variable2.indexOf(":", startVar2);  // Find the colon for time
                int endVar2 = variable2.indexOf("#", startVar2);  // Find the second #
                String hour2 = variable2.substring(startVar2, middleVar2);  // Get hours
                String minute2 = variable2.substring(middleVar2 + 1, endVar2);  // Get minutes

                // Combine hours and minutes as "HHMM" format for comparison
                String var1Combined = hour1 + minute1;
                String var2Combined = hour2 + minute2;

                // Parse the times as integers for comparison
                int var5 = Integer.parseInt(var1Combined);  // Convert time1 to int (HHMM)
                int var6 = Integer.parseInt(var2Combined);  // Convert time2 to int (HHMM)

                // Swap based on the date first, and if dates are equal, swap based on the time
                if (var3 > var4 || (var3 == var4 && var5 > var6)) {
                    tempVar = events[j];
                    events[j] = events[j + 1];
                    events[j + 1] = tempVar;
                }
            }
        }


    }
    public void clarifyImportedEvents() {
        clearedImportedEvents = new String[events.length];
        int i = 0;
        while (i < events.length) {
            String temp = events[i];
            if (temp != null) { // Check if temp is not null
                temp = temp.replace("@", "");
                temp = temp.replace("#", "");

                int intDateBefore = Integer.parseInt(temp.substring(0, 8));
                int tempDay = intDateBefore % 100;
                int tempMonth = (intDateBefore / 100) % 100;
                int tempYear = intDateBefore / 10000;

                String monthName = null;
                switch (tempMonth) {
                    case 1: monthName = "Jan"; break;
                    case 2: monthName = "Feb"; break;
                    case 3: monthName = "Mar"; break;
                    case 4: monthName = "Apr"; break;
                    case 5: monthName = "May"; break;
                    case 6: monthName = "Jun"; break;
                    case 7: monthName = "Jul"; break;
                    case 8: monthName = "Aug"; break;
                    case 9: monthName = "Sep"; break;
                    case 10: monthName = "Oct"; break;
                    case 11: monthName = "Nov"; break;
                    case 12: monthName = "Dec"; break;
                }

                String temptemp = "" + tempDay;
                if (temptemp.length() == 1) {
                    temptemp = "0" + temptemp; // Use String concatenation for leading zero
                }

                clearedImportedEvents[i] = monthName + " " + temptemp + " " + tempYear + "," + temp.substring(9);

            }
            i++;
        }
    }
    public void orderedIndex(){
        int i = 0;
        // Gets rid of "null" array indices so they are not considered
        while (i < clearedImportedEvents.length && clearedImportedEvents[i] != null) {
            i++;
        }
        for (int k = 0; k < i; k++) {
            String variable1 = clearedImportedEvents[k];
            int firstComma = variable1.indexOf(",") +1;
            String date = variable1.substring(0, firstComma +1);
            int secondComma = variable1.indexOf(",", firstComma);
            String name = variable1.substring(firstComma + 1, secondComma + 2);
            int length = clearedImportedEvents[k].length();
            String time = variable1.substring(secondComma + 2, length);

            clearedImportedEvents[k] = "Event " + (k+1) + ": " + name + date + time;


        }



        //int startVar1 = variable1.indexOf("#") + 1;  // Get the index after the first #
        //int middleVar1 = variable1.indexOf(":", startVar1);  // Find the index of the colon
        //int endVar1 = variable1.indexOf("#", startVar1);  // Find the second #
        //String hour1 = variable1.substring(startVar1, middleVar1);  // Get hours
        //Sring minute1 = variable1.substring(middleVar1 + 1, endVar1);  // Get minutes
    }
    public void setImportedEvents() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
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
        events = new String[lines.size() +1];
        lines.toArray(events);
        int i = 0;
        while (events[i] != null){
            System.out.println(events[i]);
            System.out.println("test");
            i++;
        }
    }
}