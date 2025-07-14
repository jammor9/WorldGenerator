package org.jammor9.worldsim.resources;

public class Stone extends ResourceDeposit{
    public Stone(int depositSize, boolean deepDeposit, boolean renewable) {
        super(depositSize, deepDeposit, false);
    }

    @Override
    public void extract() {

    }
}
