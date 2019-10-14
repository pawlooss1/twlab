package pl.edu.agh.lab2;

import org.apache.commons.lang3.RandomStringUtils;

public class Shopper {
    Cart cart;

    public Shopper(Cart cart) {
        this.cart = cart;
    }

    public void chooseItem() {
        cart.setContent(RandomStringUtils.random(5));
    }
}
