package org.jammor9.worldsim.resources;

public class CoalDeposit extends ResourceDeposit{

    public CoalDeposit(int depositSize, boolean deepDeposit) {
        super(depositSize, deepDeposit, false);
        addTag(ResourceTags.FUEL);
    }

    @Override
    public void extract() {

    }
}
