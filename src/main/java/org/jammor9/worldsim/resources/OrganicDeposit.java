package org.jammor9.worldsim.resources;

public abstract class OrganicDeposit extends ResourceDeposit implements Organic{
    public OrganicDeposit(int depositSize) {
        super(depositSize, false, true);
    }
}
