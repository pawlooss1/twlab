import java.util.stream.LongStream;

public class Counter {
    public static final long STEPS = 300000000;
    public int value = 0;

    public synchronized void increment() {
        value++;
    }

    public synchronized void decrement() {
        value--;
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        Runnable incrementor = () -> {
            LongStream.range(0, STEPS).forEach(i -> counter.increment());
        };
        Runnable decrementor = () -> {
            LongStream.range(0, STEPS).forEach(i -> counter.decrement());
        };
        Thread incrementorThread = new Thread(incrementor);
        Thread decrementorThread = new Thread(decrementor);
        long startTime = System.nanoTime();
        incrementorThread.start();
        decrementorThread.start();
        try {
            incrementorThread.join();
            decrementorThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.nanoTime();
        long estimatedTime = endTime - startTime;
        System.out.println(String.format("It took %d.%d seconds", estimatedTime / 1000000000, estimatedTime % 1000000000));
        System.out.println(counter.value);
    }
}
