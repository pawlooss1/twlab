package pl.edu.agh.util;

import io.vavr.Tuple2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    private static final double NANOSECONDS_IN_SECOND = 1_000_000_000;

    public static void printExecutionTime(Executable function) {
        long estimatedTime = measureExecutionTime(function);
        System.out.println(String.format("It took %f seconds", estimatedTime / NANOSECONDS_IN_SECOND));
    }

    public static long measureExecutionTime(Executable function) {
        long startTime = System.nanoTime();
        function.execute();
        long endTime = System.nanoTime();
        return endTime - startTime;
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

    public static void sleepUnchecked(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
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

    public static <E> List<E> createObjects(int howMany, Supplier<E> supplier) {
        return IntStream.range(0, howMany).mapToObj(i -> supplier.get()).collect(Collectors.toList());
    }

    public static double listMean(List<Double> list) {
        if (list.size() <= 0) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        Double sum = list.stream().reduce(0.0, Double::sum);
        return sum / list.size();
    }

    public static void printMeasurements(List<Tuple2<Integer, Double>> measurements) {
        for (Tuple2<Integer, Double> measurement : measurements) {
            System.out.println(String.format("%f", measurement._2));
        }
    }

    public static List<String> loadText(String filePath) {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "");
        map.put(1, "");
        map.put(2, "");
        map.put(3, "");
        try {
            Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)
                    .filter(s -> !s.equals(""))
                    .forEach(s -> map.put(s.length() % 4, map.get(s.length() % 4) + " " + s));
            return new ArrayList<>(map.values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
