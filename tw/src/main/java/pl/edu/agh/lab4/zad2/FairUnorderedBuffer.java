package pl.edu.agh.lab4.zad2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairUnorderedBuffer implements UnorderedBuffer {
    private final Lock lock = new ReentrantLock();
    private final Condition firstProducer = lock.newCondition();
    private final Condition firstConsumer = lock.newCondition();
    private final Condition remainingProducers = lock.newCondition();
    private final Condition remainingConsumers = lock.newCondition();
    private boolean firstProducerWaiting = false;
    private boolean firstConsumerWaiting = false;
    private int bufferSize;
    private List<Integer> values;

    public FairUnorderedBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.values = new ArrayList<>(bufferSize);
    }

    public void put(Collection<Integer> valuesToPut) {
        lock.lock();
        try {
            if (firstProducerWaiting) {
                remainingProducers.await();
            }
            firstProducerWaiting = true;
            while (valuesToPut.size() > availableSpace()) {
                firstProducer.await();
            }
            values.addAll(valuesToPut);
            firstProducerWaiting = false;
            remainingProducers.signal();
            firstConsumer.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public Collection<Integer> take(int howMany) {
        lock.lock();
        try {
            if (firstConsumerWaiting) {
                remainingConsumers.await();
            }
            firstConsumerWaiting = true;
            while (howMany > values.size()) {
                firstConsumer.await();
            }
            List<Integer> sublist = new ArrayList<>(values.subList(0, howMany));
            values.removeAll(sublist);
            firstConsumerWaiting = false;
            remainingConsumers.signal();
            firstProducer.signal();
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
