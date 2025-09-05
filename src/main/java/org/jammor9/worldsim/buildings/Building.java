package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.ResourceStockpile;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Building {
    protected record BuildingTier(int output, int maxWorkforce, int upkeepCost) {}

    //Base Information
    protected final int maxUpgradeTier; //How many times the building can be upgraded
    protected final String outputResource;
    protected ArrayList<BuildingTier> buildingTiers;

    //Construction & Upgraded Information
    private final int daysToConstruct;
    private final int buildCost;
    private final int daysToUpgrade;

    protected int upgradeTier = 0; //All buildings start unupgraded
    protected HashMap<String, Integer> constructionMaterials = new HashMap<>();
    protected int workforce = 0;

    public Building(int maxUpgradeTier, int daysToConstruct, int buildCost, int daysToUpgrade, String outputResource) {
        this.maxUpgradeTier = maxUpgradeTier;
        this.daysToConstruct = daysToConstruct;
        this.buildCost = buildCost;
        this.daysToUpgrade = daysToUpgrade;
        this.outputResource = outputResource;
    }

    //Returns time it will take for the building to finish construction, thus being removed from a province's build queue to the working queue
    //If there are not enough resources available to build the structure, an error int will be returned and handled by the province
    public int build(HashMap<String, ResourceStockpile> stockpile, int currentDay) {
        if (!resourcesAvailable(stockpile)) return -1;
        return currentDay + daysToConstruct;
    }

    public int upgrade(HashMap<String, ResourceStockpile> stockpile, int currentDay) {
        if (!resourcesAvailable(stockpile)) return -1;
        upgradeTier++;
        return currentDay + daysToUpgrade;
    }

    //Checks if the required building materials are available, and if so expends them
    private boolean resourcesAvailable(HashMap<String, ResourceStockpile> stockpile) {
        for (String k : constructionMaterials.keySet()) {
            int rSize = stockpile.get(k).getStockpileSize();
            if ((rSize - constructionMaterials.get(k) < 0)) return false;
        }

        for (String k : constructionMaterials.keySet()) {
            stockpile.get(k).removeFromStockpile(constructionMaterials.get(k));
        }

        return true;
    }

    //Takes from the current tier of the building, and adds the appropriate amount to the provincial stockpile
    public boolean work(HashMap<String, ResourceStockpile> stockpile) {
        BuildingTier bt = buildingTiers.get(upgradeTier);
        int output = (int) (bt.output * ((double) workforce / (double) bt.maxWorkforce));
        stockpile.get(outputResource).addToStockpile(output);
        return true;
    }

    public int addWorkers(int workers) {
        this.workforce += workers;
        int maxWorkers = buildingTiers.get(upgradeTier).maxWorkforce;
        if (workforce > maxWorkers) {
            int returnValue = workforce;
            workforce = maxWorkers;
            return returnValue - maxWorkers;
        }
        return 0;
    }

    public int removeWorkers(int workers) {
        this.workforce -= workers;
        if (workforce < 1) {
            int returnValue = -workforce;
            workforce = 1;
            return -returnValue;
        }
        return 0;
    }

    public String getOutput() {
        return this.outputResource;
    }

    public int getUpkeepCost() {
        return buildingTiers.get(upgradeTier).upkeepCost;
    }

    public int getDaysToConstruct() {
        return this.daysToConstruct;
    }
}
