package pl.edu.agh.lab2;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Shop {
    private CartTable cartTable;
    private Semaphore cartTableSemaphore;

    public Shop(int numberOfCarts) {
        cartTable = new CartTable(numberOfCarts);
        cartTableSemaphore = new CountingSemaphore(numberOfCarts);
    }

    public Cart takeCart() {
        cartTableSemaphore.p();
        return cartTable.takeCart();
    }

    public void returnCart(Cart cart) {
        cartTable.returnCart(cart);
        cartTableSemaphore.v();
    }

    public static void main(String[] args) {
        Shop shop1 = new Shop(4);
        System.out.println("Concurrent:");
        Utils.measureExecutionTime(() -> {
            List<Shopper> shoppers = IntStream.range(0, 200)
                    .mapToObj(i -> new Shopper(shop1)).collect(Collectors.toList());
            shoppers.forEach(Thread::start);
            shoppers.forEach(Utils::joinUnchecked);
        });
        /*System.out.println("\n\n\n\n");
        Shop shop2 = new Shop(10);
        System.out.println("Non-concurrent:");
        Utils.measureExecutionTime(() -> {
            IntStream.range(0, 200).mapToObj(i -> new Shopper(shop2)).forEach(Shopper::run);
        });*/
    }
}
