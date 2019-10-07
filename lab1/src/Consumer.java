public class Consumer extends Thread{
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        for(int i = 0;  i < Counter.STEPS;   i++) {
            String message = buffer.take();
        }

    }
}
