package pl.edu.agh.lab3;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrinterMonitor {
    private final Lock lock = new ReentrantLock();
    private final Condition notAvailable = lock.newCondition();

    private final int printersNo;
    private int takenPrintersNo;
    private List<Printer> printers;

    public PrinterMonitor(int printersNo) {
        this.printersNo = printersNo;
        this.takenPrintersNo = 0;
        this.printers = IntStream.range(0, printersNo).mapToObj(Printer::new).collect(Collectors.toList());
    }

    public int takePrinter() {
        lock.lock();
        try {
            while ((printersNo - takenPrintersNo) <= 0) {
                notAvailable.await();
            }
            Printer printer = printers.stream().filter(Printer::isAvailable).findFirst().get();
            printer.setAvailability(false);
            takenPrintersNo++;
            return printer.getNumber();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void releasePrinter(int printerNumber) {
        lock.lock();
        try {
            Printer printer = printers.stream().filter(p -> p.getNumber() == printerNumber).findFirst().get();
            printer.setAvailability(true);
            takenPrintersNo--;
            notAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void print(int printerNumber, String content) {
        printers.stream().filter(p -> p.getNumber() == printerNumber).forEach(p -> p.print(content));
    }
}
