package org.jammor9.worldsim.entities;

public abstract class AnimalEntity extends Entity{
    private int populationSize;

    public AnimalEntity(String name) {
        super(name);
    }

    public int getPopulationSize() {
        return this.populationSize;
    }
}
