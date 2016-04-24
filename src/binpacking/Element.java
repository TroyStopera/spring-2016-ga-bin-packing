package binpacking;

/*
    Wrapper class for int. This allows the system to see different elements as different, even if their size is the same.
 */
public class Element {

    public final int size;

    public Element(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
