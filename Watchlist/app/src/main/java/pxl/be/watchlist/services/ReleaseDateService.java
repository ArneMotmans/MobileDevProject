package pxl.be.watchlist.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 11501537 on 21/10/2017.
 */

public class ReleaseDateService {

    public static final String[] MONTHS = {"January","February","March","April","May","June","July","August","September","October","November", "December"};

    public static String getFormattedDate(String dateString){
        String[] elements = dateString.split("-");
        return String.format(" %s %s %s", elements[2], MONTHS[Integer.parseInt(elements[1])], elements[0]);
    }
}
