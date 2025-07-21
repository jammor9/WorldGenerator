package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.entities.AnimalEntity;
import org.jammor9.worldsim.resources.Food;

public class Ranch extends Building{
    private static final int DAYS_TO_CONSTRUCT = 30;
    private static final int DAYS_TO_UPGRADE = 45;
    private static final int BUILD_COST = 50;
    private static final String OUTPUT_RESOURCE = Food.class.toString();
    // Output Mult | Workforce | Upkeep Cost
    private static final int[][] BUILDING_TIERS = new int[][] {
            new int[] {1, 15, 0},
            new int[] {2, 30, 5},
            new int[] {3, 45, 10}
    };

    //Object Data
    private AnimalEntity ranchedAnimal;

    public Ranch(AnimalEntity animalToRanch) {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILD_COST, DAYS_TO_UPGRADE, OUTPUT_RESOURCE);
        this.ranchedAnimal = animalToRanch;
        for (int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier((ranchedAnimal.getPopulationSize() / 10) * tier[0], tier[1], tier[2]));
    }
}
