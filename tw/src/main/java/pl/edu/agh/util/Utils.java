package pl.edu.agh.util;

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
}
