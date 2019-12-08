package pl.edu.agh.philosophers;

import org.apache.commons.lang3.RandomUtils;
import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Philosopher extends Thread {
    private int number;
    private Fork fork;

    public Philosopher(int number, Fork fork) {
        this.number = number;
        this.fork = fork;
    }

    public void think() {
//        System.out.println(String.format("Philosopher %d started thinking", number));
//        Utils.sleepUnchecked(this, RandomUtils.nextInt(100, 200));
//        System.out.println(String.format("Philosopher %d ended thinking", number));
    }

    public void eat() {
        long waitTime = Utils.measureExecutionTime(() -> fork.take(number));
        Statistician.getInstance().addWaitTime(number, waitTime);
//        System.out.println(String.format("Philosopher %d started eating", number));
//        Utils.sleepUnchecked(this, RandomUtils.nextInt(100, 200));
//        System.out.println(String.format("Philosopher %d ended eating", number));
        fork.putBack(number);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            think();
            eat();
        }
    }

    public static void main(String[] args) {
        Fork fork = new AsymmetricFork();
        List<Thread> philosophers = IntStream.range(0, 5)
                .mapToObj(i -> new Philosopher(i, fork)).collect(Collectors.toList());
        philosophers.forEach(Thread::start);
        philosophers.forEach(Utils::joinUnchecked);
        Utils.printMeasurements(Statistician.getInstance().waitTimeMean());
    }
}
