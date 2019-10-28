package pl.edu.agh.lab1;

public interface Buffer<T> {
    void put(T value);
    T take();
}
