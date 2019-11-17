package pl.edu.agh.lab4.zad1;

public class Producer implements Runnable {
    private static final int PRODUCER_NO = 0;
    private int steps;
    private StreamBuffer buffer;

    public Producer(StreamBuffer buffer) {
        this.buffer = buffer;
        this.steps = buffer.getSize();
    }

    @Override
    public void run() {
        for (int i = 0; i < steps; i++) {
            buffer.put("0", i, PRODUCER_NO);
        }
    }
}
