package pl.edu.agh.lab1;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Buffer {
    public static final int STEPS = 5;
    private String value;
    private boolean present = false;

    public synchronized void put(String message) {
        while (present) {
            Utils.waitUnchecked(this);
        }
        value = message;
        present = true;
        notifyAll();
    }

    public synchronized String take() {
        while (!present) {
            Utils.waitUnchecked(this);
        }
        present = false;
        notifyAll();
        return value;
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        List<Producer> producers = IntStream.range(0, 2).mapToObj(i -> new Producer(buffer))
                .collect(Collectors.toList());
        List<Consumer> consumers = IntStream.range(0, 2).mapToObj(i -> new Consumer(buffer))
                .collect(Collectors.toList());
        Utils.measureExecutionTime(() -> {
            consumers.forEach(Thread::start);
            producers.forEach(Thread::start);
            consumers.forEach(Utils::joinUnchecked);
            producers.forEach(Utils::joinUnchecked);
        });
    }
}
