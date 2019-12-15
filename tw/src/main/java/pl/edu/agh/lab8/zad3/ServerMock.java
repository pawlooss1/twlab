package pl.edu.agh.lab8.zad3;

import org.apache.commons.lang3.RandomUtils;
import pl.edu.agh.util.Utils;

public class ServerMock {
    public double getPrice() {
        double price = RandomUtils.nextDouble(100, 1000);
//        System.out.println("elo");
        Utils.sleepUnchecked(100);
        return price;
    }
}
