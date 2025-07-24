package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.OreDeposit;

public class OreMine extends Building{
    // Output | Workforce | Upkeep Cost
    private static final int[][] BUILDING_TIERS = new int[][] {
            new int[] {200, 50, 5},
            new int[] {400, 100, 10},
            new int[] {600, 150, 15},
    };
    private static final int DAYS_TO_CONSTRUCT = 75;
    private static final int DAYS_TO_UPGRADE = 30;
    private static final int BUILD_COST = 200;

    public OreMine(OreDeposit oreDeposit) {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILD_COST, DAYS_TO_UPGRADE, oreDeposit.getClass().toString());
        for (int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier(tier[0], tier[1], tier[2]));
    }
}
