package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.Iron;
import org.jammor9.worldsim.resources.ResourceTags;
import org.jammor9.worldsim.resources.Steel;

import java.util.HashMap;

public class SteelSmelter extends Manufactory{
    private static final int DAYS_TO_CONSTRUCT = 30;
    private static final int DAYS_TO_UPGRADE = 45;
    private static final int BUILDING_COST = 200;
    private static final String OUTPUT_RESOURCE = Steel.class.toString();

    //Building Tiers
    //Output | Workforce | Upkeep
    private static final int[][] BUILDING_TIERS = new int[][]{
            new int[] {10, 3, 8},
            new int[] {20, 6, 16},
            new int[] {30, 9, 24}
    };

    private static final HashMap<ResourceTags, Integer> GENERAL_INPUT_COST;
    private static final HashMap<String, Integer> SPECIFIC_INPUT_COST;

    //Inputs
    static {
        HashMap<ResourceTags, Integer> gMap = new HashMap<>();
        gMap.put(ResourceTags.FUEL, 200);
        GENERAL_INPUT_COST = gMap;
        HashMap<String, Integer> sMap = new HashMap<>();
        sMap.put(Iron.class.toString(), 15);
        SPECIFIC_INPUT_COST = sMap;
    }

    public SteelSmelter() {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILDING_COST, DAYS_TO_UPGRADE, OUTPUT_RESOURCE, SPECIFIC_INPUT_COST, GENERAL_INPUT_COST);
    }
}
