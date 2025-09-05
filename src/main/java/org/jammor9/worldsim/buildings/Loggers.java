package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.Wood;

public class Loggers extends Building{
    private static final int DAYS_TO_CONSTRUCT = 45;
    private static final int DAYS_TO_UPGRADE = 45;
    private static final int BUILD_COST = 25;
    private static final String OUTPUT_RESOURCE = Wood.class.toString();

    // Output | Workforce | Upkeep Cost
    private static final int[][] BUILDING_TIERS = new int[][] {
            new int[] {400, 50, 0},
            new int[] {600, 100, 5},
            new int[] {800, 150, 10},
    };

    public Loggers() {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILD_COST, DAYS_TO_UPGRADE, OUTPUT_RESOURCE);
        for (int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier(tier[0], tier[1], tier[2]));
    }
}
