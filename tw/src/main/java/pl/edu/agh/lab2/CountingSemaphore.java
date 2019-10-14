package pl.edu.agh.lab2;

import pl.edu.agh.util.Utils;

public class CountingSemaphore implements Semaphore{
    private int value;

    public CountingSemaphore(int initialValue) {
        value = initialValue;
    }

    @Override
    public synchronized void p() {
        value--;
        while (value < 0) {
            Utils.waitUnchecked(this);
        }
    }

    @Override
    public synchronized void v() {
        value++;
        notify();
    }
}
