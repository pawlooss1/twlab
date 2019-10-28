package pl.edu.agh.lab3;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import pl.edu.agh.util.Utils;

public class Client extends Thread {
    private boolean requestedTable = false;
    private int pairNumber;
    private Waiter waiter;

    public Client(Waiter waiter, int pairNumber) {
        this.waiter = waiter;
        this.pairNumber = pairNumber;
    }

    public boolean hasRequestedTable() {
        return requestedTable;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(500 + RandomUtils.nextInt(0, 500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            requestedTable = true;
            waiter.takeTable(pairNumber);
            System.out.println(Utils.appendTimestampPrefix(pairNumber + " eating"));
            try {
                sleep(1000 + RandomUtils.nextInt(0, 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            requestedTable = false;
            waiter.releaseTable();
        }
    }
}
