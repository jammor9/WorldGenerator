package org.jammor9.worldsim.buildings;
import org.jammor9.worldsim.resources.Food;

public class Farm extends Building{
    private static final int DAYS_TO_CONSTRUCT = 30;
    private static final int DAYS_TO_UPGRADE = 45;
    private static final int BUILDING_COST = 0;
    private static final String OUTPUT_RESOURCE = Food.class.toString();

    //Building Tiers
    //Output Multi | Workforce | Upkeep
    private static final int[][] BUILDING_TIERS = new int[][]{
            new int[] {1, 400, 0},
            new int[] {2, 400, 5},
            new int[] {3, 300, 10}
    };

    public Farm( int fertility) {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, DAYS_TO_UPGRADE, BUILDING_COST, OUTPUT_RESOURCE);
        for(int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier(fertility * 10 * tier[0], tier[1], tier[2]));
    }
}