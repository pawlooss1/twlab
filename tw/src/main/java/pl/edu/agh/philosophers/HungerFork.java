package pl.edu.agh.philosophers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HungerFork implements Fork {
    private final Lock lock = new ReentrantLock();
    private List<Integer> free;
    private List<Condition> philosophers;

    public HungerFork() {
        free = IntStream.range(0, 5).map(i -> 2).boxed().collect(Collectors.toList());
        philosophers = IntStream.range(0, 5).mapToObj(i -> lock.newCondition()).collect(Collectors.toList());
    }

    public void take(int i) {
        try {
            lock.lock();
            while (free.get(i) < 2) {
                philosophers.get(i).await();
            }
            free.set((i + 4) % 5, free.get((i + 4) % 5) - 1);
            free.set((i + 1) % 5, free.get((i + 1) % 5) - 1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void putBack(int i) {
        try {
            lock.lock();
            free.set((i + 4) % 5, free.get((i + 4) % 5) + 1);
            free.set((i + 1) % 5, free.get((i + 1) % 5) + 1);
            philosophers.get((i + 4) % 5).signal();
            philosophers.get((i + 1) % 5).signal();
        } finally {
            lock.unlock();
        }
    }
}
