package pl.edu.agh.lab4.zad2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveUnorderedBuffer implements UnorderedBuffer {
    private final Lock lock = new ReentrantLock();
    private final Condition canPut = lock.newCondition();
    private final Condition canTake = lock.newCondition();
    private int bufferSize;
    private List<Integer> values;

    public NaiveUnorderedBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.values = new ArrayList<>(bufferSize);
    }

    public void put(Collection<Integer> valuesToPut) {
        lock.lock();
        try {
            while (valuesToPut.size() > availableSpace()) {
                canPut.await();
            }
            values.addAll(valuesToPut);
            canTake.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public Collection<Integer> take(int howMany) {
        lock.lock();
        try {
            while (howMany > values.size()) {
                canTake.await();
            }
            List<Integer> sublist = new ArrayList<>(values.subList(0, howMany));
            values.removeAll(sublist);
            canPut.signalAll();
            return sublist;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private int availableSpace() {
        return bufferSize - values.size();
    }
}
