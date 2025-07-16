package org.jammor9.worldsim.resources;

public abstract class MetalDeposit extends ResourceDeposit implements Metal{
    public MetalDeposit(int depositSize, boolean deepDeposit) {
        super(depositSize, deepDeposit, false);
        addTag(ResourceTags.SMELTABLE);
    }
}
