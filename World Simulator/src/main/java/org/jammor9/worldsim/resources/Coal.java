package org.jammor9.worldsim.resources;

public class Coal extends ResourceDeposit{
    public Coal(int depositSize, boolean deepDeposit) {
        super(depositSize, deepDeposit, false);
        addTag(ResourceTags.FUEL);
    }

    @Override
    public void extract() {

    }
}
