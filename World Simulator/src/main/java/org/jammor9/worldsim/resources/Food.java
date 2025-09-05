package org.jammor9.worldsim.resources;

public class Food extends ResourceStockpile {

    public Food() {
        super(0);
        addTag(ResourceTags.FOOD);
    }
}
