package org.jammor9.worldsim.resources;

public abstract class OreDeposit extends ResourceDeposit implements Ore{
    public OreDeposit(int depositSize, boolean deepDeposit) {
        super(depositSize, deepDeposit, false);
        addTag(ResourceTags.SMELTABLE);
    }
}
