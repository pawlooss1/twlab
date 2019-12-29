package pl.edu.agh.lab9.standard;

import java.util.concurrent.Semaphore;

public class SmokersDemo {
    static Semaphore smokerTobacco = new Semaphore(0);
    static Semaphore smokerPaper = new Semaphore(0);
    static Semaphore smokerMatch = new Semaphore(0);
    static Semaphore agent = new Semaphore(0);
    static Semaphore lock = new Semaphore(1);

    public static void main(String[] args) {
        Thread agentThread = new Thread(new Agent(smokerTobacco, smokerPaper, smokerMatch, agent, lock));
        SmokerFactory smokerFactory = new SmokerFactory(smokerTobacco, smokerPaper, smokerMatch, agent, lock);
        Thread smokerTobaccoThread = new Thread(smokerFactory.newSmokerTobacco());
        Thread smokerPaperThread = new Thread(smokerFactory.newSmokerPaper());
        Thread smokerMatchThread = new Thread(smokerFactory.newSmokerMatch());
        agentThread.start();
        smokerMatchThread.start();
        smokerPaperThread.start();
        smokerTobaccoThread.start();
    }
}
