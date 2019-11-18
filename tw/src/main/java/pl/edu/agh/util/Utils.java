package pl.edu.agh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class Utils {

    private static final long NANOSECONDS_IN_SECOND = 1_000_000_000;

    public static void measureExecutionTime(Executable function) {
        long startTime = System.nanoTime();
        function.execute();
        long endTime = System.nanoTime();
        long estimatedTime = endTime - startTime;
        System.out.println(String.format("It took %d.%d seconds", estimatedTime / NANOSECONDS_IN_SECOND,
                estimatedTime % NANOSECONDS_IN_SECOND));
    }

    public static void joinUnchecked(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitUnchecked(Object object) {
        try {
            object.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleepUnchecked(Thread thread, int timeInMillis) {
        try {
            thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String appendTimestampPrefix(String message) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
        return String.format("[%s] %s", timeStamp, message);
    }

    public static <E> String collectionToString(Collection<E> list, String prefix, Function<E, String> mapper) {
        return list.stream().map(mapper).reduce(prefix, (s1, s2) -> String.join(" ", s1, s2));
    }
}
