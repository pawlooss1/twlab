import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Buffer {
    private String value;
    private boolean present = false;
    public static final int STEPS = 3;

    public synchronized void put(String message){
        while (present) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        value = message;
        present = true;
        notifyAll();
    }

    public synchronized String take() {
        while (!present) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        present = false;
        notifyAll();
        return value;
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Stream<Producer> producerStream = IntStream.range(0, 2).mapToObj(i -> new Producer(buffer));
        Stream<Consumer> consumerStream = IntStream.range(0, 5).mapToObj(i -> new Consumer(buffer));
        consumerStream.forEach(Thread::start);
        producerStream.forEach(Thread::start);
    }
}
