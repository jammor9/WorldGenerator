package org.jammor9.worldsim.resources;

public class CharcoalStockpile extends ResourceStockpile{
    public CharcoalStockpile(int stockpileSize) {
        super(stockpileSize);
        addTag(ResourceTags.FUEL);
    }
}
