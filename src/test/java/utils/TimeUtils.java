package utils;

public class TimeUtils {

    public static void waitTimeMillis(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        }catch(InterruptedException ie) {}
    }
}
