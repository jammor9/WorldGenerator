package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.ResourceDeposit;

public class Quarry extends Building{
    private static final int DAYS_TO_CONSTRUCT = 45;
    private static final int DAYS_TO_UPGRADE = 25;
    private static final int BUILD_COST = 100;
    // Output | Workforce | Upkeep Cost
    private static final int[][] BUILDING_TIERS = new int[][] {
            new int[] {400, 100, 5},
            new int[] {600, 200, 10},
            new int[] {900, 300, 15},
    };

    public Quarry(ResourceDeposit resourceDeposit) {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILD_COST, DAYS_TO_UPGRADE, resourceDeposit.getClass().toString());
        for (int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier(tier[0], tier[1], tier[2]));
    }
}
