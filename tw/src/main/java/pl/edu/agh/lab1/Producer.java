package pl.edu.agh.lab1;

public class Producer extends Thread {
    private static final int STEPS = 10;
    private Buffer<String> buffer;

    public Producer(Buffer<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        for (int i = 0; i < STEPS; i++) {
            buffer.put(String.format("message no %d from %d", i, getId()));
        }

    }
}
