package pl.edu.agh.lab4.zad1;

import io.vavr.Tuple2;
import pl.edu.agh.lab1.Buffer;
import pl.edu.agh.lab3.BoundedBuffer;
import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamBuffer {
    private final Lock lock = new ReentrantLock();
    private final Condition canProcess = lock.newCondition();
    private int size;
    private final List<Tuple2<Buffer<String>, AtomicInteger>> array;

    public StreamBuffer(int size) {
        this.size = size;
        this.array = IntStream.range(0, size)
                .mapToObj(i -> new Tuple2<Buffer<String>, AtomicInteger>(new BoundedBuffer(), new AtomicInteger(0)))
                .collect(Collectors.toList());
    }

    public int getSize() {
        return size;
    }

    public void put(String value, int cellNumber, int processorNo) {
        lock.lock();
        try {
            while (array.get(cellNumber)._2.intValue() < processorNo) {
                canProcess.await();
            }
            array.get(cellNumber)._2.incrementAndGet();
            canProcess.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        array.get(cellNumber)._1.put(value);
    }

    public String take(int cellNumber) {
        return array.get(cellNumber)._1.take();
    }
}
