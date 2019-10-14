package pl.edu.agh.lab2;

import pl.edu.agh.util.Utils;

public class BinarySemaphore implements Semaphore {
    private boolean value = true;

    @Override
    public synchronized void p() {
        while (!value) {
            Utils.waitUnchecked(this);
        }
        value = false;
    }

    @Override
    public synchronized void v() {
        value = true;
        notify();
    }
}
