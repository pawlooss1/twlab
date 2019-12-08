package pl.edu.agh.philosophers;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsymmetricFork implements Fork {
    private final Lock lock = new ReentrantLock();
    private List<Boolean> occupied;
    private List<Condition> forks;

    public AsymmetricFork() {
        occupied = Utils.createObjects(5, () -> false);
        forks = Utils.createObjects(5, lock::newCondition);
    }

    @Override
    public void take(int i) {
        try {
            lock.lock();
            while (occupied.get(firstFork(i))) {
                forks.get(firstFork(i)).await();
            }
            occupied.set(firstFork(i), true);
            while (occupied.get(secondFork(i))) {
                forks.get(secondFork(i)).await();
            }
            occupied.set(secondFork(i), true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putBack(int i) {
        try {
            lock.lock();
            occupied.set(firstFork(i), false);
            forks.get(firstFork(i)).signal();
            occupied.set(secondFork(i), false);
            forks.get(secondFork(i)).signal();
        } finally {
            lock.unlock();
        }
    }

    private int firstFork(int i) {
        if (i % 2 == 0) {
            return i;
        } else {
            return (i + 1) % 5;
        }
    }

    private int secondFork(int i) {
        if (i % 2 == 0) {
            return (i + 1) % 5;
        } else {
            return i;
        }
    }

}
