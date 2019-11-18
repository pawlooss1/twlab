package pl.edu.agh.lab4.zad2;

import org.apache.commons.lang3.RandomUtils;
import pl.edu.agh.util.Utils;

import java.util.Collection;

public class RandomSizeConsumer extends Thread {
    private int consumerNo;
    private int maxSize;
    private UnorderedBuffer buffer;

    public RandomSizeConsumer(int consumerNo, int maxSize, UnorderedBuffer buffer) {
        this.consumerNo = consumerNo;
        this.maxSize = maxSize;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0; true; i++) {
            int size = RandomUtils.nextInt(1, maxSize + 1);
            long startTime = System.nanoTime();
            Collection<Integer> product = buffer.take(size);
            long endTime = System.nanoTime();
            Statistician.getInstance().addTakeTime(size, endTime - startTime);
            //System.out.println(Utils.collectionToString(product, String.format("%d: ", consumerNo), Object::toString));
        }
    }
}
