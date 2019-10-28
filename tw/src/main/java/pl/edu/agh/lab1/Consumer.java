package pl.edu.agh.lab1;

public class Consumer extends Thread {
    private static final int STEPS = 10;
    private Buffer<String> buffer;

    public Consumer(Buffer<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        for (int i = 0; i < STEPS; i++) {
            String message = buffer.take();
            System.out.println(message);
        }

    }
}
