package pl.edu.agh.lab3;

import pl.edu.agh.lab1.Buffer;
import pl.edu.agh.lab1.Consumer;
import pl.edu.agh.lab1.Producer;
import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoundedBuffer implements Buffer<String> {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private String value;
    private boolean present = false;

    public void put(String value) {
        lock.lock();
        try {
            while (present) {
                notFull.await();
            }
            this.value = value;
            present = true;
            notEmpty.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public String take() {
        lock.lock();
        try {
            while (!present) {
                notEmpty.await();
            }
            String value = this.value;
            present = false;
            notFull.signal();
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Buffer<String> buffer = new BoundedBuffer();
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