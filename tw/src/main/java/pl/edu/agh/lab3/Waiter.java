package pl.edu.agh.lab3;

import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Waiter {
    private final Lock lock = new ReentrantLock();
    private final Condition notAvailable = lock.newCondition();
    private final List<Condition> partnerReady;
    private int seatsTaken = 0;
    private int pairAtTable;

    private List<Tuple2<Client, Client>> pairs;

    public Waiter() {
        partnerReady = new ArrayList<>();
        pairs = new ArrayList<>();
    }

    public void takeTable(int pairNumber) {
        lock.lock();
        try {
            partnerReady.get(pairNumber).signal();
            while (!isPairReady(pairNumber)) {
                partnerReady.get(pairNumber).await();
            }
            while ((seatsTaken != 0) && !isPartnerSeating(pairNumber)) {
                notAvailable.await();
            }
            pairAtTable = pairNumber;
            seatsTaken++;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void releaseTable() {
        lock.lock();
        try {
            seatsTaken--;
            pairAtTable = -1;
            notAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean isPartnerSeating(int pairNumber) {
        return pairAtTable == pairNumber;
    }

    private boolean isPairReady(int pairNumber) {
        return pairs.get(pairNumber).apply((c1, c2) -> c1.hasRequestedTable() && c2.hasRequestedTable());
    }

    private Tuple2<Client, Client> register(int pairNumber) {
        Tuple2<Client, Client> pair = new Tuple2<>(new Client(this, pairNumber), new Client(this, pairNumber));
        partnerReady.add(lock.newCondition());
        pairs.add(pair);
        return pair;
    }

    private void startPair(Tuple2<Client, Client> pair) {
        pair._1.start();
        pair._2.start();
    }

    public static void main(String[] args) {
        Waiter waiter = new Waiter();
        IntStream.range(0, 20).mapToObj(waiter::register).forEach(waiter::startPair);
    }
}
