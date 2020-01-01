package pl.edu.agh.lab9.different;

import java.util.concurrent.Semaphore;

public class SmokerFactory {
    private Pushers pushers;

    public SmokerFactory(Pushers pushers) {
        this.pushers = pushers;
    }

    public Runnable newSmokerTobacco() {
        return fillSmokerTemplate(pushers.getTobaccoSemaphore(), "Tobacco");
    }

    public Runnable newSmokerPaper() {
        return fillSmokerTemplate(pushers.getPaperSemaphore(), "Paper");
    }

    public Runnable newSmokerMatch() {
        return fillSmokerTemplate(pushers.getMatchSemaphore(), "Match");
    }

    private Runnable fillSmokerTemplate(Semaphore productSemaphore, String name) {
        return () -> {
            while (true) {
                try {
                    productSemaphore.acquire();
                    makeCigarette(name);
                    pushers.getAgentSemaphore().release();
                    smoke(name);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private void makeCigarette(String name) {
        System.out.println(String.format("%s making cigarette", name));
    }

    private void smoke(String name) {
        System.out.println(String.format("%s smoking", name));
    }
}
