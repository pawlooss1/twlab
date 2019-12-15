package pl.edu.agh.lab8.zad2;

import pl.edu.agh.util.Utils;

import java.util.concurrent.RecursiveTask;

public class ArraySumer {
    private final static long TEN_MILLION = 10_000_000_000L;
    private final static int MILLION = 100_000_000;
    private int[] array;

    public ArraySumer() {
        this.array = new int[MILLION];
        for (long i = 0; i < MILLION; i++) {
            array[(int) i] = 1;
        }
    }

    public int sum() {
        return new RecursiveSumer(0, MILLION).compute();
    }

    class RecursiveSumer extends RecursiveTask<Integer> {
        private int from;
        private int to;
        private int width;

        public RecursiveSumer(int from, int to) {
            this.from = from;
            this.to = to;
            this.width = to - from;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if(width < 1000000) {
                for (int i = from; i < to; i++) {
                    sum += array[i];
                }
                return sum;
            }
            int cut = from + width / 2;
            RecursiveSumer rs1 = new RecursiveSumer(from, cut);
            rs1.fork();
            RecursiveSumer rs2 = new RecursiveSumer(cut, to);
            return rs2.compute() + rs1.join();
        }
    }

    public static void main(String[] args) {
        ArraySumer sumer = new ArraySumer();
//        System.out.println(sumer.sum());
        Utils.printExecutionTime(sumer::sum);
    }
}
