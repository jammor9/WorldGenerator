package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.*;

import java.util.HashMap;

public class OreSmelter extends Manufactory {

    private static final int DAYS_TO_CONSTRUCT = 21;
    private static final int DAYS_TO_UPGRADE = 60;
    private static final int BUILDING_COST = 125;
    private static final String OUTPUT_RESOURCE = CharcoalStockpile.class.toString();

    //Building Tiers
    //Output(Metal) | Workforce | Upkeep
    private static final int[][] BUILDING_TIERS = new int[][]{
            new int[] {10, 5, 6},
            new int[] {15, 10, 12},
            new int[] {20, 15, 18}
    };

    private static final HashMap<ResourceTags, Integer> GENERAL_INPUT_COST;

    //Inputs
    static {
        HashMap<ResourceTags, Integer> map = new HashMap<>();
        map.put(ResourceTags.FUEL, 200);
        GENERAL_INPUT_COST = new HashMap<>(map);
    }

    public OreSmelter(OreDeposit oreDeposit) {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILDING_COST, DAYS_TO_UPGRADE, oreDeposit.getClass().toString(), null, GENERAL_INPUT_COST);
    }
}
