package pl.edu.agh.lab4.zad2;

import io.vavr.Tuple2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Statistician {
    private static Statistician instance;
    private Map<Integer, List<Double>> putTime = new ConcurrentHashMap<>();
    private Map<Integer, List<Double>> takeTime = new ConcurrentHashMap<>();

    public static Statistician getInstance() {
        if (instance == null) {
            instance = new Statistician();
        }
        return instance;
    }

    public void addPutTime(int size, double measurement) {
        putTime.computeIfAbsent(size, i -> new LinkedList<>());
        putTime.get(size).add(measurement);
    }

    public void addTakeTime(int size, double measurement) {
        takeTime.computeIfAbsent(size, i -> new LinkedList<>());
        takeTime.get(size).add(measurement);
    }

    public List<Tuple2<Integer, Double>> putTimeMean() {
        return putTime.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), listMean(entry.getValue())))
                .collect(Collectors.toList());
    }

    public List<Tuple2<Integer, Double>> takeTimeMean() {
        return takeTime.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), listMean(entry.getValue())))
                .collect(Collectors.toList());
    }

    private double listMean(List<Double> list) {
        Double sum = list.stream().reduce((double) 0, Double::sum);
        return sum / list.size();
    }
}
