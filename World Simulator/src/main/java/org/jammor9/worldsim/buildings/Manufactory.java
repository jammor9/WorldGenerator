package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.ResourceStockpile;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Manufactory extends Building{
    ArrayList<HashMap<String, Integer>> manufactoryInputTierCosts;

    public Manufactory(int maxUpgradeTier, int daysToConstruct, int buildCost, int daysToUpgrade, String outputResource) {
        super(maxUpgradeTier, daysToConstruct, buildCost, daysToUpgrade, outputResource);
    }

    @Override
    public boolean work(HashMap<String, ResourceStockpile> stockpile) {
        HashMap<String, Integer> inputCost = manufactoryInputTierCosts.get(upgradeTier);

        //First check if required resources are available
        for (String k : inputCost.keySet()) {
            if(!isStockAvailable(stockpile, k, inputCost.get(k))) return false;
        }

        //Then actually remove the resources from the stockpile
        for (String k : inputCost.keySet()) {
            stockpile.get(k).removeFromStockpile(inputCost.get(k));
        }

        //Finally, return output
        return super.work(stockpile);
    }

    private boolean isStockAvailable(HashMap<String, ResourceStockpile> stockpile, String resource, int inputSize) {
        return stockpile.get(resource).getStockpileSize() >= inputSize;
    }
}
