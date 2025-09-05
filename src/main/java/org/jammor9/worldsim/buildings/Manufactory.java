package org.jammor9.worldsim.buildings;

import org.jammor9.worldsim.resources.ResourceStockpile;
import org.jammor9.worldsim.resources.ResourceTags;

import java.util.HashMap;
import java.util.Objects;

public abstract class Manufactory extends Building{
    HashMap<String, Integer> specificInputCosts;
    HashMap<ResourceTags, Integer> generalInputCosts;

    public Manufactory(int maxUpgradeTier, int daysToConstruct, int buildCost, int daysToUpgrade, String outputResource,
                       HashMap<String, Integer> specificInputCosts, HashMap<ResourceTags, Integer> generalInputCosts) {
        super(maxUpgradeTier, daysToConstruct, buildCost, daysToUpgrade, outputResource);
        this.specificInputCosts = Objects.requireNonNullElseGet(specificInputCosts, HashMap::new);
        this.generalInputCosts = Objects.requireNonNullElseGet(generalInputCosts, HashMap::new);
    }

    @Override
    public boolean work(HashMap<String, ResourceStockpile> stockpile) {

        HashMap<String, Integer> inputCost = new HashMap<>();

        //First check if specific resources are available
        for (String k : specificInputCosts.keySet()) {
            if (!isStockAvailable(stockpile, k, specificInputCosts.get(k))) return false;
            inputCost.put(k, specificInputCosts.get(k));
        }


        //Then check if general resources are available
        for (ResourceTags tag : generalInputCosts.keySet()) {
            for (String name : stockpile.keySet()) {
                if (stockpile.get(name).containsTag(tag)) {
                    inputCost.put(name, generalInputCosts.get(tag));
                    break;
                }
            }
        }

        //Check that the required specific inputs and general inputs are available and have been added to the input list
        //If not, then return failure signal
        if (inputCost.size() < (specificInputCosts.size() + generalInputCosts.size())) return false;

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
