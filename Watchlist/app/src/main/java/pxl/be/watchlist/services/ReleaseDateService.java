package pxl.be.watchlist.services;

public class ReleaseDateService {

    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static String getFormattedDate(String dateString) {
        String[] elements = dateString.split("-");
        if (elements.length == 3)
            return String.format(" %s %s %s", elements[2], MONTHS[Integer.parseInt(elements[1]) - 1], elements[0]);
        return null;
    }
}
