package CalendarFunctions;

import java.io.*;
import java.util.ArrayList;

public class Sorter {
    String[] events;
    String textFile = null;
    ArrayList<String> lines = new ArrayList<>();

    public Sorter(String s) throws IOException {
        textFile = s;
        setImportedEvents();
        String tempVar;


        // Print events before sorting
        System.out.println("Before sorting:");
        for (String event : events) {
            System.out.println(event);
        }

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

                int startVar2 = variable2.indexOf("@") + 1;  // Get the index after the first @
                int middleVar2 = variable2.indexOf(":", startVar2);  // Find the colon for time
                int endVar2 = variable2.indexOf("@", startVar2);  // Find the second @
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

        // Print events after sorting
        System.out.println("After sorting:");
        for (String event : events) {
            System.out.println(event);
        }
        //Overwrites file with new sorted list
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
            int p = 0;

            while (events[p] != null) {
                writer.write(events[p]);
                writer.newLine();
                p++;
            }
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setImportedEvents() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
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
        events = new String[lines.size() + 1];
        lines.toArray(events);
        int i = 0;
        while (events[i] != null) {
            System.out.println(events[i]);
            i++;
        }
    }
}
