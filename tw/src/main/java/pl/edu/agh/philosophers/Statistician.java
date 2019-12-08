package pl.edu.agh.philosophers;

import io.vavr.Tuple2;
import pl.edu.agh.util.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Statistician {
    private static Statistician instance;
    private Map<Integer, List<Double>> waitTime = new ConcurrentHashMap<>();

    public static Statistician getInstance() {
        if (instance == null) {
            instance = new Statistician();
        }
        return instance;
    }

    public void addWaitTime(int number, double measurement) {
        waitTime.computeIfAbsent(number, i -> new LinkedList<>());
        waitTime.get(number).add(measurement);
    }

    public List<Tuple2<Integer, Double>> waitTimeMean() {
        return waitTime.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), Utils.listMean(entry.getValue())))
                .collect(Collectors.toList());
    }
}
