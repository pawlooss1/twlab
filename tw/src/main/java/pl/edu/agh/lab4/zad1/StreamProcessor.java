package pl.edu.agh.lab4.zad1;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamProcessor {
    private static final int FIELDS_NUMBER = 20;
    private static final int PROCESSORS_NUMBER = 40;
    private int size;
    private int processorsNo;
    private StreamBuffer buffer;

    public StreamProcessor(int size, int processorsNo, StreamBuffer buffer) {
        this.size = size;
        this.buffer = buffer;
        this.processorsNo = processorsNo;
    }

    private Runnable createProcessor(int number) {
        return () -> {
            for (int i = 0; i < size; i++) {
                String newValue = String.format("%s, %d", buffer.take(i, number), number);
                buffer.put(newValue, i, number);
            }
        };
    }

    private void run() {
        Runnable producer = new Producer(buffer);
        List<Runnable> processors = IntStream.range(1, processorsNo + 1)
                .mapToObj(this::createProcessor).collect(Collectors.toList());
        Runnable consumer = new Consumer(buffer, processorsNo + 1);
        new Thread(producer).start();
        processors.stream().map(Thread::new).forEach(Thread::start);
        new Thread(consumer).start();
    }


    public static void main(String[] args) {
        new StreamProcessor(FIELDS_NUMBER, PROCESSORS_NUMBER, new StreamBuffer(FIELDS_NUMBER)).run();
    }
}
