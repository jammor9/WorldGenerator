package org.jammor9.worldsim.resources;

public class Stone extends ResourceDeposit{
    public Stone(int depositSize, boolean deepDeposit) {
        super(depositSize, deepDeposit, false);
        addTag(ResourceTags.HEAVY_CONSTRUCTION);
    }

    @Override
    public void extract() {

    }
}
