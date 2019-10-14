package pl.edu.agh.lab2;

public class Cart {
    private int number;
    private String content;

    public Cart(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
