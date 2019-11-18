package pl.edu.agh.lab4.zad2;

import io.vavr.Tuple2;
import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Presenter {
    private static final int M = 1000;
    private static final int PROCESSES = 10;
    private static final boolean NAIVE = false;

    public static void main(String[] args) {
        UnorderedBuffer buffer = createBuffer();
        List<RandomSizeProducer> producers = IntStream.range(0, PROCESSES)
                .mapToObj(i -> new RandomSizeProducer(i, M, buffer)).collect(Collectors.toList());
        List<RandomSizeConsumer> consumers = IntStream.range(0, PROCESSES)
                .mapToObj(i -> new RandomSizeConsumer(i, M, buffer)).collect(Collectors.toList());
        producers.forEach(Thread::start);
        consumers.forEach(Thread::start);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        producers.forEach(Thread::suspend);
        consumers.forEach(Thread::suspend);
        printMeasurements(Statistician.getInstance().putTimeMean());
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        printMeasurements(Statistician.getInstance().takeTimeMean());
//        System.out.println(Statistician.getInstance().putTimeMean());
//        System.out.println(Statistician.getInstance().takeTimeMean());
    }

    private static void printMeasurements(List<Tuple2<Integer, Double>> measurements) {
        for (Tuple2<Integer, Double> measurement : measurements) {
            System.out.println(String.format("%f", measurement._2));
        }
    }

    private static UnorderedBuffer createBuffer() {
        return NAIVE ? new NaiveUnorderedBuffer(2 * M) : new FairUnorderedBuffer(2 * M);
    }
}
