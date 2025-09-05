package org.jammor9.worldsim.resources;

public class Wood extends OrganicDeposit{
    public Wood(int depositSize) {
        super(depositSize);
        addTag(ResourceTags.FUEL);
        addTag(ResourceTags.LIGHT_CONSTRUCTION);
    }

    @Override
    public void extract() {
        return;
    }

    @Override
    public void regrow() {

    }
}
