package pl.edu.agh.lab2;

import pl.edu.agh.util.Utils;

import java.util.stream.LongStream;

public class Counter {
    public static final long STEPS = 50_000_000;
    private int value = 0;
    private Semaphore semaphore = new BinarySemaphore();

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        Runnable incrementor = () -> LongStream.range(0, STEPS).forEach(i -> {
            counter.semaphore.p();
            counter.increment();
            counter.semaphore.v();
        });
        Runnable decrementor = () -> LongStream.range(0, STEPS).forEach(i -> {
            counter.semaphore.p();
            counter.decrement();
            counter.semaphore.v();
        });
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
