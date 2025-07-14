package org.jammor9.worldsim.resources;

public class Forest extends OrganicDeposit{
    public Forest(int depositSize) {
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
