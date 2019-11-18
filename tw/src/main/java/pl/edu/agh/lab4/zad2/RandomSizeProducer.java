package pl.edu.agh.lab4.zad2;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomSizeProducer extends Thread {
    private int producerNo;
    private int maxSize;
    private UnorderedBuffer buffer;

    public RandomSizeProducer(int producerNo, int maxSize, UnorderedBuffer buffer) {
        this.producerNo = producerNo;
        this.maxSize = maxSize;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int size = RandomUtils.nextInt(1, maxSize + 1);
            List<Integer> product = IntStream.range(0, size).boxed().collect(Collectors.toList());
            buffer.put(product);
        }
    }
}
