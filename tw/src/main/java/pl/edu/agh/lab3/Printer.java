package pl.edu.agh.lab3;

import pl.edu.agh.util.Utils;

public class Printer {
    private int number;
    private boolean available;

    public Printer(int number) {
        available = true;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailability(boolean available) {
        this.available = available;
    }

    public void print(String content) {
        System.out.println(String.format("%s (printer no. %d)", Utils.appendTimestampPrefix(content), number));
    }
}
