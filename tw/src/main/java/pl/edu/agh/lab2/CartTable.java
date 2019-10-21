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
        Cart cart = carts.removeFirst();
        //System.out.printf("Took cart %d%n", cart.getNumber());
        //System.out.println(getCartQueue());
        return cart;
    }

    public void returnCart(Cart cart) {
        carts.addLast(cart);
        semaphore.v();
    }

    private String getCartQueue() {
        return carts.stream().map(cart -> String.valueOf(cart.getNumber()))
                .reduce("Table:", (s1, s2) -> String.join(" ", s1, s2));
    }
}
