package pl.edu.agh.lab2;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Shopper extends Thread {
    private Shop shop;
    private Cart cart;

    public Shopper(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void run() {
        goShopping();
    }

    private void goShopping() {
        cart = shop.takeCart();
        int cartNumber = cart.getNumber();
        //System.out.printf("Shopper %d took cart %d%n", getId(), cartNumber);
        try {
            sleep(RandomUtils.nextInt(1, 100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        chooseItem();
        //System.out.printf("Shopper %d took item %s%n", getId(), cart.getContent());
        shop.returnCart(cart);
        //System.out.printf("Shopper %d done with the shopping. Returned cart %d%n", getId(), cartNumber);

    }

    private void chooseItem() {
        cart.setContent(RandomStringUtils.random(5));
    }
}
