package org.jammor9.worldsim.resources;

public class CoalStockpile extends ResourceStockpile{
    public CoalStockpile(int stockpileSize) {
        super(stockpileSize);
        addTag(ResourceTags.FUEL);
    }
}
