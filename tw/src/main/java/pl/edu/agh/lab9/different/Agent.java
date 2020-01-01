package pl.edu.agh.lab9.different;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Agent extends ResourceHolder {
    private static final Agent instance = new Agent();
    private final List<Runnable> agents;

    private Agent() {
        this.agents = Arrays.asList(agentA, agentB, agentC);
    }

    public static Agent getInstance() {
        return instance;
    }

    public Collection<Runnable> getAgents() {
        return this.agents;
    }

    private Runnable agentA = () -> {
        while (true) {
            try {
                agentSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            tobacco.release();
            paper.release();
        }
    };

    private Runnable agentB = () -> {
        while (true) {
            try {
                agentSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            paper.release();
            match.release();
        }
    };

    private Runnable agentC = () -> {
        while (true) {
            try {
                agentSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            tobacco.release();
            match.release();
        }
    };
}
