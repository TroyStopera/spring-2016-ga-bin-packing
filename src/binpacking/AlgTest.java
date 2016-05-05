package binpacking;

import java.util.Random;

public class AlgTest {

    private Random rand = new Random();

    // These values are defaults, and can be changed by values passed when the
    // the object is initiated.
    private int binSize = 100;
    private long timeout = 1000;
    private int numTrials = 25;

    public AlgTest(int binSize, int numTrials, long timeout) {
        this.binSize = binSize;
        this.numTrials = numTrials;
        this.timeout = timeout;
    }

    public void runTest(int popSize, int domNum, double domRate,
        double muRate) {

        // Create Genetics instance.
        BinPacking.Genetics gen = new
            BinPacking.Genetics(popSize, domNum, domRate, muRate);

        System.out.print("Genetics Parameters"
            + "\nPopulation Size: " + popSize
            + "\nNumber of Dominants: " + domNum
            + "\nDominant Rate: " + domRate
            + "\nMutation Rate: " + muRate
            + "\n\n");

        for(int i = 1000; i <= 1000; i *= 10) {

            int[] trialResults = new int[numTrials];

            for(int j = 0; j < numTrials; j++) {

                // Create random inputs.
                int[] elements = new int[i];

                for(int k = 0; k < i; k++) {
                    // Random number from 1 to bin size, inclusive.
                    elements[k] = rand.nextInt(binSize) + 1;
                }

                // Run binPacking.
                BinPacking bp = new BinPacking(elements, binSize, gen);

                trialResults[j] = bp.run(timeout).getBins().size();
            }

            // Find average number of bins.
            int trialResultsSum = 0;

            for(int tr : trialResults) {
                trialResultsSum += tr;
            }

            double aveBins = ((double)trialResultsSum) / ((double)numTrials);

            // Print results.
            System.out.print(i + " elements: " + aveBins + " average bins\n\n\n");
        }
    }
}
