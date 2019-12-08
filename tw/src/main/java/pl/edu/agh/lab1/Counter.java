package pl.edu.agh.lab1;

import pl.edu.agh.util.Utils;

import java.util.stream.LongStream;

public class Counter {
    public static final long STEPS = 500_000_000;
    public int value = 0;

    public synchronized void increment() {
        value++;
    }

    public synchronized void decrement() {
        value--;
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        Runnable incrementor = () -> LongStream.range(0, STEPS).forEach(i -> counter.increment());
        Runnable decrementor = () -> LongStream.range(0, STEPS).forEach(i -> counter.decrement());
        Thread incrementorThread = new Thread(incrementor);
        Thread decrementorThread = new Thread(decrementor);
        Utils.printExecutionTime(() -> {
            incrementorThread.start();
            decrementorThread.start();
            Utils.joinUnchecked(incrementorThread);
            Utils.joinUnchecked(decrementorThread);
        });
        System.out.println(counter.value);
    }
}
