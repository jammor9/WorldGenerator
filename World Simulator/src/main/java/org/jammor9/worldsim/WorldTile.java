package org.jammor9.worldsim;

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
    private int fertility;
    private boolean coastal;

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

    @Override
    public String toString() {
        return "[" + x + ", "+ y + "]";
    }

}
