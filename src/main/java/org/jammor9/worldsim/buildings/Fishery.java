package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.Food;

public class Fishery extends Building{
    private static final int DAYS_TO_CONSTRUCT = 30;
    private static final int DAYS_TO_UPGRADE = 15;
    private static final int BUILD_COST = 20;
    private static final String OUTPUT_RESOURCE = Food.class.toString();

    //Output | Workforce | Upkeep Cost
    private static final int[][] BUILDING_TIERS = new int[][] {
            new int[] {200, 50, 0},
            new int[] {400, 100, 5},
            new int[] {600, 150, 10}
    };

    public Fishery() {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILD_COST, DAYS_TO_UPGRADE, OUTPUT_RESOURCE);
        for (int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier(tier[0], tier[1], tier[2]));
    }
}
