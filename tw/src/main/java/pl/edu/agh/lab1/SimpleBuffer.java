package pl.edu.agh.lab1;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleBuffer implements Buffer<String> {
    public static final int STEPS = 5;
    private String value;
    private boolean present = false;

    public synchronized void put(String value) {
        while (present) {
            Utils.waitUnchecked(this);
        }
        this.value = value;
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
        SimpleBuffer buffer = new SimpleBuffer();
        List<Producer> producers = IntStream.range(0, 2).mapToObj(i -> new Producer(buffer))
                .collect(Collectors.toList());
        List<Consumer> consumers = IntStream.range(0, 2).mapToObj(i -> new Consumer(buffer))
                .collect(Collectors.toList());
        Utils.printExecutionTime(() -> {
            consumers.forEach(Thread::start);
            producers.forEach(Thread::start);
            consumers.forEach(Utils::joinUnchecked);
            producers.forEach(Utils::joinUnchecked);
        });
    }
}
