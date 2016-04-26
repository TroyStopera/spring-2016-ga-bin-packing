package binpacking;

import java.util.*;

public class BinPacking {

    private static final Random random = new Random();
    private final int binCapacity;
    private final Genetics genetics;
    private Element[] elements;
    //the current population, the TreeSet will keep the solutions sorted by fitness, the list provides access by index
    private TreeSet<Solution> sortedPopulation = new TreeSet<>();

    public BinPacking(int[] elements, int binCapacity, Genetics genetics) {
        this.elements = new Element[elements.length];
        for (int i = 0; i < elements.length; i++)
            this.elements[i] = new Element(elements[i]);
        this.binCapacity = binCapacity;
        this.genetics = genetics;
    }

    //generates a random solution given the elements
    private static Solution generateRandomSolution(Element[] elems, int binCapacity) {
        //the bins used in this solution
        List<Bin> bins = new ArrayList<>();

        //create a list of the elements, and shuffle it
        List<Element> elements = new LinkedList<>();
        Collections.addAll(elements, elems);
        Collections.shuffle(elements);

        //form the solution
        Bin currentBin = new Bin(binCapacity);
        for (Element e : elements) {
            //if the bin would be overfilled, add it to the list, then create a new one
            if (currentBin.getFilled() + e.size > currentBin.getCapacity()) {
                bins.add(currentBin);
                currentBin = new Bin(binCapacity);
            }
            //add the element to the bin
            currentBin.add(e);
        }
        //make sure the last bin is recorded
        bins.add(currentBin);

        return new Solution(bins);
    }

    /*
        This is the main genetic algorithm. It will return the best generated Solution, and can be ran multiple times
        for better results. The general idea is that if dominance is enabled, the most fit Solution will mate with the
        next 'n' most fit Solutions - where n is determined by dominance. The next most most fit Solution will mate
        with the next n-1 most fit solutions, and so on. Once all dominants are mated, or if dominance was not enabled,
        the population will undergo random mating until the population size quota is met.
     */
    public Solution run(long timeout) {
        final long startTime = System.currentTimeMillis();

        //create P(0) if empty
        if (sortedPopulation.isEmpty())
            for (int i = 0; i < genetics.popSize; i++) {
                sortedPopulation.add(generateRandomSolution(elements, binCapacity));
                sortedPopulation.add(generateRandomSolution(elements, binCapacity));
            }

        //keeps track of current best so that new generations can't remove the best
        Solution overallBest = sortedPopulation.first();

        //begin breeding
        while (System.currentTimeMillis() - startTime < timeout) {
            List<Solution> nextPopulation = new ArrayList<>();

            //if dominance is in effect, start with the most fit solutions
            if (genetics.domRate > 0 && genetics.dominants > 0) {
                //list of the dominant Solutions as they are processed
                List<Solution> dominants = new LinkedList<>();

                //iterates through solutions in order of fitness
                Iterator<Solution> iterator = sortedPopulation.iterator();

                //add the first dominant to the list
                dominants.add(iterator.next());

                //loop through each harem size until it's 0
                for (int harem = (int) (genetics.popSize * genetics.domRate); harem > 0; harem--) {
                    //if all dominants have been processed or the population is full, break
                    if (!(iterator.hasNext()) || dominants.size() > genetics.dominants || nextPopulation.size() >= genetics.popSize) break;

                    //get the next dominant
                    Solution nextDominant = iterator.next();
                    //breed all previous dominants with this dominant, then add this dominant to the dominants list
                    for (Solution dominant : dominants) {
                        Solution child = Crossover.mate(dominant, nextDominant);
                        if (random.nextDouble() <= genetics.mutationRate)
                            Crossover.mutate(child);
                        nextPopulation.add(child);
                    }

                    dominants.add(nextDominant);
                }
            }

            //random mating until population is filled
            while (nextPopulation.size() < genetics.popSize) {
                //put the tree set into an array list for random access, NOTE: slows things down a little
                List<Solution> pop = new ArrayList<>(sortedPopulation);

                int index1 = random.nextInt(pop.size());
                int index2 = random.nextInt(pop.size());;
                //ensure a solution doesn't mate with itself
                /*do {
                    index2 = random.nextInt(pop.size());
                } while (index1 == index2);*/

                if (index1 == index2) index2 = (index2 == 0) ? (index2 + 1) : (index2 - 1);

                Solution child = Crossover.mate(pop.get(index1), pop.get(index2));
                if (random.nextDouble() <= genetics.mutationRate)
                    Crossover.mutate(child);

                nextPopulation.add(child);
            }

            //check the currentBest in this next population and if it is better than overallBest, replace
            //Solution currentBest = nextPopulation.first();

            Solution currentBest = nextPopulation.get(0);

            for(Solution pBS : nextPopulation) {
                if(pBS.getFitness() > currentBest.getFitness()) {
                    currentBest = pBS;
                }
            }

            if (currentBest.getFitness() >= overallBest.getFitness()) overallBest = currentBest;
        }

        //ensure overallBest is added to the population, then return
        sortedPopulation.add(overallBest);
        return overallBest;
    }

    /*
        Simple class for holding genetic variables, for cleaner method signatures
     */
    public static class Genetics {

        public final int popSize, dominants;
        public final double mutationRate, domRate;

        public Genetics(int popSize, int dominants, double domRate, double mutationRate) {
            this.dominants = dominants;
            //ensure the population size is at least 2
            this.popSize = popSize < 2 ? 2 : popSize;
            //ensure mutation rate is between 0 and 1
            this.mutationRate = mutationRate < 0 ? 0 : mutationRate > 1 ? 1 : mutationRate;
            //ensure dominance is between 0 and 1
            this.domRate = domRate < 0 ? 0 : domRate > 1 ? 1 : domRate;
        }

    }


}
