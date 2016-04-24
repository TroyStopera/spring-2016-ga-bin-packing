package binpacking;

import java.util.*;

public class Crossover {

    private static final Random random = new Random();

    public static Solution mate(Solution male, Solution female) {
        Iterator<Bin> mGenes = male.getBins().iterator();
        Iterator<Bin> fGenes = female.getBins().iterator();

        //the offspring of the two parents, as a list of bins
        List<Bin> zygote = new ArrayList<>();

        //keeps track of which elements have been passed into zygote
        LinkedHashSet<Element> inheritedElements = new LinkedHashSet<>();

        //used to store bins that can be further filled from recessive genes
        Bin recessiveOverflow = new Bin(male.getBins().get(0).getCapacity());

        /*
            This loops while both iterators still have elements. Within this loop, a dominant and recessive bin is
            taken, one from each iterator (at random which is dominant). Every element in each bin, both dominant and
            recessive, will be added to zygote by the end of each iteration. Therefore, once the shortest iterator is
            empty, all elements must have been processed. By using the shortest iterator (which implies the fewest bins)
            as the one that determines when the crossover is done, it will weed out solutions with more bins,
            improving the overall fitness of each generation.
         */
        while (mGenes.hasNext() && fGenes.hasNext()) {
            final Bin dominant;
            final Bin recessive;
            //every iteration there is a 50% chance that male genes are dominant, 50% chance female is dominant
            if (random.nextBoolean()) {
                dominant = mGenes.next();
                recessive = fGenes.next();
            } else {
                dominant = fGenes.next();
                recessive = mGenes.next();
            }

            //process the dominant gene first
            Bin newBin = new Bin(dominant.getCapacity());
            for (Element e : dominant.getElements()) {
                if (!inheritedElements.contains(e)) {
                    newBin.add(e);
                    inheritedElements.add(e);
                }
            }

            //process the recessive gene
            for (Element e : recessive.getElements()) {
                //if the element has not been inherited...
                if (!inheritedElements.contains(e)) {
                    //try to add it to the newBin...
                    if (newBin.getCapacity() - newBin.getFilled() >= e.size) {
                        newBin.add(e);
                        inheritedElements.add(e);
                    }
                    //if it won't fit...
                    else {
                        //place newBin in zygote and set newBin to be recessive overflow
                        zygote.add(newBin);
                        newBin = recessiveOverflow;
                        //if e can now fit in newBin, add it
                        if (newBin.getCapacity() - newBin.getFilled() >= e.size) {
                            newBin.add(e);
                            inheritedElements.add(e);
                        }
                        //otherwise place newBin in zygote, and create a new recessive bin, then set that as newBin and add e
                        else {
                            zygote.add(newBin);
                            recessiveOverflow = new Bin(recessiveOverflow.getCapacity());
                            newBin = recessiveOverflow;
                            newBin.add(e);
                            inheritedElements.add(e);
                        }
                    }
                }
            }
        }
        //if recessiveOverflow is not empty, make sure to add it to zygote
        if (!recessiveOverflow.getElements().isEmpty()) zygote.add(recessiveOverflow);

        return new Solution(zygote);
    }

    public static void mutate(Solution solution) {
        List<Bin> bins = solution.getBins();

        //find the element that will be moved
        int srcBinIndex = random.nextInt(bins.size());
        int srcElementIndex = random.nextInt(bins.get(srcBinIndex).getElements().size());
        Element mutationElem = bins.get(srcBinIndex).getElements().get(srcElementIndex);
        bins.get(srcBinIndex).remove(mutationElem);

        for (int i = 0; i < bins.size(); i++) {
            //skip if we are looking in the srcBin
            if (i == srcBinIndex) continue;

            Bin bin = bins.get(i);

            //if bin i can hold mutationElem, then place the elem in bin i and return the new Solution
            if (bin.getCapacity() - bin.getFilled() >= mutationElem.size) {
                bin.add(mutationElem);
                return;
            }
        }

        //if the method has not returned, then make a new bin and add the elem
        Bin bin = new Bin(bins.get(0).getCapacity());
        bin.add(mutationElem);

        //add new bin to bins
        bins.add(bin);
    }

}
