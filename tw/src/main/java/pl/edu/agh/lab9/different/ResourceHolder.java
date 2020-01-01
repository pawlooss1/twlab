package pl.edu.agh.lab9.different;

import java.util.concurrent.Semaphore;

public abstract class ResourceHolder {
    protected static final Semaphore agentSemaphore = new Semaphore(1);
    protected static final Semaphore tobacco = new Semaphore(0);
    protected static final Semaphore paper = new Semaphore(0);
    protected static final Semaphore match = new Semaphore(0);
}
