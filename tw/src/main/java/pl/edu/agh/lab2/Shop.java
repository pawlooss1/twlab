package pl.edu.agh.lab2;

import pl.edu.agh.util.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Shop {
    private CartTable cartTable;
    private Semaphore takeCartSemaphore;
    private Semaphore returnCartSemaphore;

    public Shop(int numberOfCarts) {
        cartTable = new CartTable(numberOfCarts);
        takeCartSemaphore = new BinarySemaphore();
        returnCartSemaphore = new BinarySemaphore();
    }

    public Cart takeCart() {
        takeCartSemaphore.p();
        Cart cart = cartTable.takeCart();
        takeCartSemaphore.v();
        return cart;
    }

    public void returnCart(Cart cart) {
        returnCartSemaphore.p();
        cartTable.returnCart(cart);
        returnCartSemaphore.v();
    }

    public static void main(String[] args) {
        Shop shop1 = new Shop(10);
        System.out.println("Concurrent:");
        Utils.measureExecutionTime(() -> {
            List<Shopper> shoppers = IntStream.range(0, 200)
                    .mapToObj(i -> new Shopper(shop1)).collect(Collectors.toList());
            shoppers.forEach(Thread::start);
            shoppers.forEach(Utils::joinUnchecked);
        });
        System.out.println("\n\n\n\n");
        Shop shop2 = new Shop(10);
        System.out.println("Non-concurrent:");
        Utils.measureExecutionTime(() -> {
            IntStream.range(0, 200).mapToObj(i -> new Shopper(shop2)).forEach(Shopper::run);
        });
    }
}
