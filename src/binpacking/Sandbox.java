package binpacking;

public class Sandbox {

    public static void main(String[] args) {

        //BinSize, NumTrials, Timeout
        AlgTest at = new AlgTest(100, 100, 100);

        //PopSize, DomNum, DomRate, MuRate
        at.runTest(100, 5, 0.01, 0.25);

    }
}
