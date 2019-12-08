package pl.edu.agh.philosophers;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingFork implements Fork {
    private final Lock lock = new ReentrantLock();
    private List<Boolean> occupied;
    private List<Condition> forks;

    public BlockingFork() {
        occupied = Utils.createObjects(5, () -> false);
        forks = Utils.createObjects(5, lock::newCondition);
    }

    @Override
    public void take(int i) {
        try {
            lock.lock();
            while (occupied.get(i)) {
                forks.get(i).await();
            }
            occupied.set(i, true);
            while (occupied.get((i + 1) % 5)) {
                forks.get((i + 1) % 5).await();
            }
            occupied.set((i + 1) % 5, true);
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
            occupied.set(i, false);
            forks.get(i).signal();
            occupied.set((i + 1) % 5, false);
            forks.get((i + 1) % 5).signal();
        } finally {
            lock.unlock();
        }
    }
}
