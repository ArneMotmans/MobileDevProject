package pxl.be.watchlist.services;

/**
 * Created by 11501537 on 21/10/2017.
 */

public class RunTimeService {
    public static String getRunTime(long runtime){
        long hours = runtime / 60;
        long minutes = runtime % 60;
        return String.format(" %sh %smin",hours,minutes);
    }
}
