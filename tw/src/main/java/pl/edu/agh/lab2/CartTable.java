package pl.edu.agh.lab2;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class CartTable {
    private LinkedList<Cart> carts = new LinkedList<>();
    private Semaphore semaphore;

    public CartTable(int numberOfCarts) {
        IntStream.range(0, numberOfCarts).mapToObj(Cart::new).forEach(carts::add);
        semaphore = new CountingSemaphore(numberOfCarts);
    }

    public Cart takeCart() {
        semaphore.p();
        return carts.removeFirst();
    }

    public void returnCart(Cart cart) {
        carts.addFirst(cart);
        semaphore.v();
    }
}
