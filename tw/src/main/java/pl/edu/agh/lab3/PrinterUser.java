package pl.edu.agh.lab3;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import pl.edu.agh.util.Utils;

import java.util.stream.IntStream;

public class PrinterUser extends Thread {
    private PrinterMonitor monitor;
    private String taskToPrint;

    public PrinterUser(PrinterMonitor monitor) {
        this.monitor = monitor;
    }

    private void print(int printerNumber) {
        monitor.print(printerNumber, taskToPrint);
    }

    @Override
    public void run() {
        while (true) {
            taskToPrint = RandomStringUtils.randomAlphabetic(6) + this.getId();
            try {
                sleep(0, RandomUtils.nextInt(0, 1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //Utils.waitUnchecked(RandomUtils.nextInt(0, 5));
            int printerNumber = monitor.takePrinter();
            print(printerNumber);
            monitor.releasePrinter(printerNumber);
        }
    }

    public static void main(String[] args) {
        PrinterMonitor monitor = new PrinterMonitor(10);
        IntStream.range(0, 15).mapToObj(i -> new PrinterUser(monitor)).forEach(Thread::start);
    }
}
