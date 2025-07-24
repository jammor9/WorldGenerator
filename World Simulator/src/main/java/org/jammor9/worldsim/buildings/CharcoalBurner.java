package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.CharcoalStockpile;
import org.jammor9.worldsim.resources.Wood;

import java.util.HashMap;

public class CharcoalBurner extends Manufactory{
    private static final int DAYS_TO_CONSTRUCT = 45;
    private static final int DAYS_TO_UPGRADE = 45;
    private static final int BUILDING_COST = 100;
    private static final String OUTPUT_RESOURCE = CharcoalStockpile.class.toString();

    //Building Tiers
    //Output | Workforce | Upkeep
    private static final int[][] BUILDING_TIERS = new int[][]{
            new int[] {100, 10, 5},
            new int[] {150, 15, 7},
            new int[] {200, 20, 10}
    };

    private static final HashMap<String, Integer> SPECIFIC_INPUT_COST;

    //Inputs
    static {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(Wood.class.toString(), 300);
        SPECIFIC_INPUT_COST = new HashMap<>(map);
    }

    public CharcoalBurner() {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILDING_COST, DAYS_TO_UPGRADE, OUTPUT_RESOURCE, SPECIFIC_INPUT_COST, null);
    }
}
