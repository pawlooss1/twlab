package pl.edu.agh.lab2;

public class Shop {
    private CartTable cartTable;


    public Shop(int numberOfCarts) {
        cartTable = new CartTable(numberOfCarts);
    }
}
