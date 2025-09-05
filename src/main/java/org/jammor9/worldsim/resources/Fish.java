package org.jammor9.worldsim.resources;

public class Fish extends OrganicDeposit{

    public Fish(int depositSize) {
        super(depositSize);
        addTag(ResourceTags.FOOD);
    }

    @Override
    public void extract() {
        return;
    }

    @Override
    public void regrow() {

    }
}
