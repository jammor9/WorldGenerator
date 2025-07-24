package org.jammor9.worldsim;

import java.util.Random;

public class HistorySimulator {

    private WorldMap worldMap;
    private Random worldGenRng;
    private Random gameRng;

    public HistorySimulator(int seed, WorldMap worldMap) {
        this.worldMap = worldMap;
        this.worldGenRng = new Random(seed);
        this.gameRng = new Random(); //History generation should be random even if the same world is generated
    }

    //Generates mineral deposits on the map
    public void generateMineralMap() {

    }

}
