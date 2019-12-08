package pl.edu.agh.philosophers;

public interface Fork {
    void take(int i);

    void putBack(int i);
}
