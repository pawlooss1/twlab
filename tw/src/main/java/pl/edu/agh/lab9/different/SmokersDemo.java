package pl.edu.agh.lab9.different;

public class SmokersDemo {
    public static void main(String[] args) {
        Agent.getInstance().getAgents().forEach(r -> new Thread(r).start());
        Pushers pushers = new Pushers();
        pushers.runPushers();
        SmokerFactory smokerFactory = new SmokerFactory(pushers);
        new Thread(smokerFactory.newSmokerMatch()).start();
        new Thread(smokerFactory.newSmokerPaper()).start();
        new Thread(smokerFactory.newSmokerTobacco()).start();
    }
}
