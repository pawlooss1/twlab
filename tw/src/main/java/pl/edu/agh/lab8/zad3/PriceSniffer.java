package pl.edu.agh.lab8.zad3;

import pl.edu.agh.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PriceSniffer {
    private static final int PRODUCTS_NO = 200;
    private ServerMock server = new ServerMock();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public List<Double> sniffSequentially() {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < PRODUCTS_NO; i++) {
            result.add(server.getPrice());
        }
        return result;
    }

    public List<Double> sniffAsynchronously() {
        ArrayList<CompletableFuture<Double>> result = new ArrayList<>();
        executorService = Executors.newCachedThreadPool();
        for (int i= 0; i < PRODUCTS_NO; i++) {
            result.add(CompletableFuture.supplyAsync(() -> server.getPrice(), executorService));
        }
        return result.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public void tearDown() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        PriceSniffer sniffer = new PriceSniffer();
        Utils.printExecutionTime(sniffer::sniffAsynchronously);
        sniffer.tearDown();
        Utils.printExecutionTime(sniffer::sniffSequentially);
    }
}
