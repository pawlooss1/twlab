package pl.edu.agh.lab1;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Buffer {
    private String value;
    private boolean present = false;
    public static final int STEPS = 5;

    public synchronized void put(String message) {
        while (present) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        value = message;
        present = true;
        notifyAll();
    }

    public synchronized String take() {
        while (!present) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
