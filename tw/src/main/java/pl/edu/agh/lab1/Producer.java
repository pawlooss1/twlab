package pl.edu.agh.lab1;

public class Producer extends Thread{
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        for(int i = 0;  i < Buffer.STEPS;   i++) {
            buffer.put(String.format("message no %d from %d", i, getId()));
        }

    }
}
