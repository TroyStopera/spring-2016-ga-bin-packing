package binpacking;

import java.util.List;

/*
    This class represents a potential solution to the bin packing problem.
 */
public class Solution implements Comparable<Solution> {

    //all of the bins used for this solution
    private final List<Bin> bins;

    public Solution(List<Bin> bins) {
        this.bins = bins;
    }

    public List<Bin> getBins() {
        return bins;
    }

    //returns this solution's fitness
    public int getFitness() {
        //invert sign on the number of bins so that less bins is a higher fitness
        return -1 * bins.size();
    }

    @Override
    public int compareTo(Solution solution) {
        //solutions with less bins will be closer to index 0 when sorted
        return Integer.compare(bins.size(), solution.bins.size());
    }
}
