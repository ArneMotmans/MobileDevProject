package pxl.be.watchlist.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReleaseDateService {

    public static final String[] MONTHS = {"January","February","March","April","May","June","July","August","September","October","November", "December"};

    public static String getFormattedDate(String dateString){
        String[] elements = dateString.split("-");
        if (elements.length == 3)
            return String.format(" %s %s %s", elements[2], MONTHS[Integer.parseInt(elements[1])-1], elements[0]);
        return null;
    }
}
