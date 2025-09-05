package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.ResourceStockpile;

import java.util.HashMap;

public class ConstructionOffice extends Building{
    private static final int DAYS_TO_CONSTRUCT = 90;
    private static final int DAYS_TO_UPGRADE = 150;
    private static final int BUILDING_COST = 300;
    private static final String OUTPUT_RESOURCE = "";

    //Building Tiers
    //Construction Speed | Workforce | Upkeep
    private static final int[][] BUILDING_TIERS = new int[][]{
            new int[] {1, 200, 10},
            new int[] {2, 200, 20},
            new int[] {3, 250, 30}
    };

    private Boolean activeProject = false;
    private int daysToCompletion;

    public ConstructionOffice() {
        super(BUILDING_TIERS.length, DAYS_TO_CONSTRUCT, BUILDING_COST, DAYS_TO_UPGRADE, OUTPUT_RESOURCE);
        for (int[] tier : BUILDING_TIERS) buildingTiers.add(new BuildingTier(tier[0], tier[1], tier[2]));
    }

    public boolean work() {
        if (!activeProject) return true; //Check if the current project is empty, and if so return a signal saying that a new building can be assigned
        daysToCompletion -= buildingTiers.get(upgradeTier).output();
        if (daysToCompletion == 0) {
            activeProject = false;
            return true;
        }
        return false;
    }

    public void newProject(Building newProject) {
        if (!activeProject) return;
        activeProject = true;
        daysToCompletion = newProject.getDaysToConstruct();
    }

    public boolean isActive() {
        return this.activeProject;
    }
}
