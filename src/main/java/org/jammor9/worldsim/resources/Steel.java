package org.jammor9.worldsim.resources;

public class Steel extends ResourceStockpile{

    public Steel(int stockpileSize) {
        super(stockpileSize);
        addTag(ResourceTags.METAL);
    }
}
