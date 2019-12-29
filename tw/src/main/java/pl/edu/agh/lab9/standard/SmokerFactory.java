package pl.edu.agh.lab9.standard;

import java.util.concurrent.Semaphore;

public class SmokerFactory {
    private Semaphore smokerTobacco;
    private Semaphore smokerPaper;
    private Semaphore smokerMatch;
    private Semaphore agent;
    private Semaphore lock;

    public SmokerFactory(Semaphore smokerTobacco, Semaphore smokerPaper, Semaphore smokerMatch, Semaphore agent, Semaphore lock) {
        this.smokerTobacco = smokerTobacco;
        this.smokerPaper = smokerPaper;
        this.smokerMatch = smokerMatch;
        this.agent = agent;
        this.lock = lock;
    }

    public Runnable newSmokerTobacco() {
        return fillSmokerTemplate(smokerTobacco, "Tobacco");
    }

    public Runnable newSmokerPaper() {
        return fillSmokerTemplate(smokerPaper, "Paper");
    }

    public Runnable newSmokerMatch() {
        return fillSmokerTemplate(smokerMatch, "Match");
    }

    private Runnable fillSmokerTemplate(Semaphore smoker, String name) {
        return () -> {
            while (true) {
                try {
                    smoker.acquire();  // Sleep right away
                    lock.acquire();
                    agent.release();
                    lock.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(name);
                // Smoke (but don't inhale).
            }
        };
    }
}
