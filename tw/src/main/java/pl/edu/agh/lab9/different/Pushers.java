package pl.edu.agh.lab9.different;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pushers extends ResourceHolder {
    private boolean isTobacco = false;
    private boolean isPaper = false;
    private boolean isMatch = false;
    private final Semaphore tobaccoSemaphore = new Semaphore(0);
    private final Semaphore paperSemaphore = new Semaphore(0);
    private final Semaphore matchSemaphore = new Semaphore(0);
    private final Lock lock = new ReentrantLock();

    public void runPushers() {
        new Thread(pusherA).start();
        new Thread(pusherB).start();
        new Thread(pusherC).start();
    }

    public Semaphore getAgentSemaphore() {
        return agentSemaphore;
    }

    public Semaphore getTobaccoSemaphore() {
        return tobaccoSemaphore;
    }

    public Semaphore getPaperSemaphore() {
        return paperSemaphore;
    }

    public Semaphore getMatchSemaphore() {
        return matchSemaphore;
    }

    private Runnable pusherA = () -> {
        while (true) {
            try {
                tobacco.acquire();
                lock.lock();
                if (isPaper) {
                    isPaper = false;
                    matchSemaphore.release();
                } else if (isMatch) {
                    isMatch = false;
                    paperSemaphore.release();
                } else {
                    isTobacco = true;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    };

    private Runnable pusherB = () -> {
        while (true) {
            try {
                paper.acquire();
                lock.lock();
                if (isTobacco) {
                    isTobacco = false;
                    matchSemaphore.release();
                } else if (isMatch) {
                    isMatch = false;
                    tobaccoSemaphore.release();
                } else {
                    isPaper = true;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    };

    private Runnable pusherC = () -> {
        while (true) {
            try {
                match.acquire();
                lock.lock();
                if (isPaper) {
                    isPaper = false;
                    tobaccoSemaphore.release();
                } else if (isTobacco) {
                    isTobacco = false;
                    paperSemaphore.release();
                } else {
                    isMatch = true;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    };
}
