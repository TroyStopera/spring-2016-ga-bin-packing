package binpacking;

/*
    This class represents a potential solution to the bin packing problem.
 */
public class Solution implements Comparable<Solution> {

    //all of the bins used for this solution
    private final Bin[] bins;

    public Solution(Bin[] bins) {
        this.bins = bins;
    }

    public Bin[] getBins() {
        return bins;
    }

    //returns this solution's fitness
    public int getFitness() {
        //invert sign on the number of bins so that less bins is a higher fitness
        return -1 * bins.length;
    }

    @Override
    public int compareTo(Solution solution) {
        //solutions with less bins will be closer to index 0 when sorted
        return Integer.compare(bins.length, solution.bins.length);
    }
}
