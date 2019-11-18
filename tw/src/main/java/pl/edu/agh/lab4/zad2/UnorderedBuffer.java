package pl.edu.agh.lab4.zad2;

import java.util.Collection;

public interface UnorderedBuffer {
    void put(Collection<Integer> valuesToPut);
    Collection<Integer> take(int howMany);
}
