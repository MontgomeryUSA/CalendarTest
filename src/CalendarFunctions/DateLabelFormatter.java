package CalendarFunctions;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    //sets value for the format of the string
    private String datePattern = "yyyy-MM-dd";
    //sets the Date Format (because the input it a date value) to the string pattern above
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    //sets a string to an object date formatter
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    //Does the opposite of what is above
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        //error return
        return "";
    }

}
