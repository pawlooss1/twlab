package pl.edu.agh.lab9.standard;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.Semaphore;

public class Agent implements Runnable {
    private Semaphore smokerTobacco;
    private Semaphore smokerPaper;
    private Semaphore smokerMatch;
    private Semaphore agent;
    private Semaphore lock;

    public Agent(Semaphore smokerTobacco, Semaphore smokerPaper, Semaphore smokerMatch, Semaphore agent, Semaphore lock) {
        this.smokerTobacco = smokerTobacco;
        this.smokerPaper = smokerPaper;
        this.smokerMatch = smokerMatch;
        this.agent = agent;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.acquire();
                int random = RandomUtils.nextInt(0, 3);
                if (random == 0) {
                    // Put tobacco on table
                    // Put paper on table
                    smokerMatch.release();  // Wake up smoker with match
                } else if (random == 1) {
                    // Put tobacco on table
                    // Put match on table
                    smokerPaper.release(); // Wake up smoker with paper
                } else {
                    // Put match on table
                    // Put paper on table
                    smokerTobacco.release(); // Wake up smoker with tobacco
                }
                lock.release();
                agent.acquire(); //  Agent sleeps
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
