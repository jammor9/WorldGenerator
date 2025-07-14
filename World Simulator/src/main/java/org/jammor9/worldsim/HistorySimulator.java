package org.jammor9.worldsim;

import java.util.Random;

public class HistorySimulator {

    private WorldMap worldMap;
    private Random rng;

    public HistorySimulator(int seed, WorldMap worldMap) {
        this.worldMap = worldMap;
        this.rng = new Random(seed);
    }

    //Generates mineral deposits on the map
    public void generateMineralMap() {

    }

}
