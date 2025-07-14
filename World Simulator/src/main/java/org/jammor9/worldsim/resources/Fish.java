package org.jammor9.worldsim.resources;

public class Fish extends OrganicDeposit{

    public Fish(int depositSize) {
        super(depositSize);
    }

    @Override
    public void extract() {
        return;
    }

    @Override
    public void regrow() {

    }
}
