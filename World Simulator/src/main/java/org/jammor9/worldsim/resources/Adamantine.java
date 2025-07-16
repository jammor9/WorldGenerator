package org.jammor9.worldsim.resources;

public class Adamantine extends MetalDeposit{
    public Adamantine(int depositSize, boolean deepDeposit) {
        super(depositSize, deepDeposit);
        addTag(ResourceTags.MAGIC);
    }

    @Override
    public void smelt() {

    }

    @Override
    public void extract() {

    }
}
