package org.jammor9.worldsim;

import org.jammor9.worldsim.resources.MetalDeposit;
import org.jammor9.worldsim.resources.OrganicDeposit;
import org.jammor9.worldsim.resources.ResourceDeposit;

import java.util.ArrayList;

public class WorldTile {

    //Data imported from WorldGenerator, describes climate of a tile
    public int x;
    public int y;
    private double elevation; //Self explanatory, range between 0.0 and 1.0
    private double precipitation;
    private double temperature;
    private double[] flowTile; //Used for modelling rivers, shows the tile that this tile will flow to
    private Climate climate;
    private int riverSize;

    //Data important to simulation
    private int fertility; //Determines the quality and quantity of crops in this tile, range 0-100
    private int magic; //Inate magic of the tile, range 0-100. Determines the likelihood of fantastical events, spawning adamantine, and attractivenes to mage colleges
    private boolean coastal; //Whether a tile is adjacenet to the coast
    private ArrayList<OrganicDeposit> organicDeposits = new ArrayList<>(); //List of organic deposits, i.e. forests
    private ArrayList<MetalDeposit> metalDeposits = new ArrayList<>(); //List of metal deposits, i.e. iron
    private ArrayList<ResourceDeposit> nonOrganicDeposits = new ArrayList<>(); //List of non-organic deposits i.e. stone

    public WorldTile(int x, int y, double elevation, double temperature, double precipitation, Climate climate, int riverSize, double[] flowTile, boolean coastal) {
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.climate = climate;
        this.riverSize = riverSize;
        this.flowTile = flowTile;
        this.coastal = coastal;
    }

    public void addOrganicDeposit(OrganicDeposit o) {
        organicDeposits.add(o);
    }

    public void addMetalDeposit(MetalDeposit m) {
        metalDeposits.add(m);
    }

    public void addNonOrganicDeposits(ResourceDeposit r) {
        nonOrganicDeposits.add(r);
    }

    public double getElevation() {
        return this.elevation;
    }

    public double[] getFlowTile() {
        return this.flowTile;
    }

    public double getPrecipitation() {
        return this.precipitation;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public Climate getClimate() {
        return this.climate;
    }

    public int getRiverSize() {
        return this.riverSize;
    }

    public void setFertility(int fertility) {
        if (fertility < 0) this.fertility = 0;
        else if (fertility > 100) this.fertility = 100;
        else this.fertility = fertility;
    }

    public int getFertility() {
        return this.fertility;
    }

    public boolean isCoastal() {
        return this.coastal;
    }

    public void setMagic(int magic) {
        if (magic < 0) this.magic = 0;
        else if (magic > 100) this.magic = 100;
        else this.magic = magic;
    }

    public int getMagic() {
        return this.magic;
    }

    @Override
    public String toString() {
        return "[" + x + ", "+ y + "]";
    }

}
