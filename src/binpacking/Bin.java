package binpacking;

import java.util.ArrayList;
import java.util.List;

/*
    THis class represents a Bin. It is used to "store" elements.
 */
public class Bin {

    //the maximum capacity for this bin
    private final int capacity;
    //a list of all elements in this bin
    private final List<Element> elements = new ArrayList<>();

    //the amount in the bin
    private int filled = 0;

    public Bin(int capacity) {
        this.capacity = capacity;
    }

    //adds an element and returns the new fill level
    public int add(Element e) {
        elements.add(e);
        filled += e.size;
        return filled;
    }

    //removes an element and returns the new fill level
    public int remove(Element e) {
        elements.remove(e);
        filled -= e.size;
        return filled;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Element> getElements() {
        return elements;
    }

    public int getFilled() {
        return filled;
    }

    public Bin copy() {
        Bin bin = new Bin(capacity);
        bin.filled = filled;
        bin.elements.addAll(elements);
        return bin;
    }
}
