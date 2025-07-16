package org.jammor9.worldsim.resources;

public abstract class ResourceDeposit extends Resource {

    private final int depositSize; //Determines the total amount of resources in the deposit
    private final boolean deepDeposit; //Determines whether the deposit is located deep underground, locking it behind requirements to access
    private final boolean renewable; //Determines whether a resource can regrow given the passage of time

    public ResourceDeposit(int depositSize, boolean deepDeposit, boolean renewable) {
        this.depositSize = depositSize;
        this.deepDeposit = deepDeposit;
        this.renewable = renewable;
    }

    //Extract resources from the deposit
    public abstract void extract();

    public int getDepositSize() {
        return this.depositSize;
    }

    public boolean isDeepDeposit() {
        return this.deepDeposit;
    }

    public boolean isRenewable() {
        return this.renewable;
    }
}
