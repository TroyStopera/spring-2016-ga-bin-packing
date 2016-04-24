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
    private final List<Integer> elements = new ArrayList<>();

    //the amount in the bin
    private int filled = 0;

    public Bin(int capacity) {
        this.capacity = capacity;
    }

    //adds an element and returns the new fill level
    public int add(int i) {
        elements.add(i);
        filled += i;
        return filled;
    }

    //removes an element and returns the new fill level
    public int remove(int i) {
        //make sure not to remove index "i"
        elements.remove(new Integer(i));
        filled -= i;
        return filled;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Integer> getElements() {
        return elements;
    }

    public int getFilled() {
        return filled;
    }
}
