package pl.edu.agh.lab4.zad1;

public class Consumer implements Runnable {
    private int steps;
    private StreamBuffer buffer;
    private int consumerNo;

    public Consumer(StreamBuffer buffer, int number) {
        this.buffer = buffer;
        this.steps = buffer.getSize();
        this.consumerNo = number;
    }

    @Override
    public void run() {
        for (int i = 0; i < steps; i++) {
            System.out.println(buffer.take(i, consumerNo));
        }
    }
}
