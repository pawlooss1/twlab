package pl.edu.agh.lab2;

import pl.edu.agh.util.Utils;

public class CountingSemaphore implements Semaphore{
    private int value;

    public CountingSemaphore(int initialValue) {
        value = initialValue;
    }

    @Override
    public synchronized void p() {
        while (value < 1) {
            Utils.waitUnchecked(this);
        }
        value--;
    }

    @Override
    public synchronized void v() {
        value++;
        notify();
    }
}
