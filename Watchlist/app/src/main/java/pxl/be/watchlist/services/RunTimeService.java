package pxl.be.watchlist.services;

public class RunTimeService {
    public static String getRunTime(long runtime) {
        long hours = runtime / 60;
        long minutes = runtime % 60;
        return String.format(" %sh %smin", hours, minutes);
    }
}
