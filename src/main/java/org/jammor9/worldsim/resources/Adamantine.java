package org.jammor9.worldsim.resources;

public class Adamantine extends OreDeposit {
    public static final String NAME = Adamantine.class.toString();

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
