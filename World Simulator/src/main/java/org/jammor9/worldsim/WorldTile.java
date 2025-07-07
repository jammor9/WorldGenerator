package org.jammor9.worldsim;

public class WorldTile {

    public int x;
    public int y;
    private double elevation; //Self explanatory, range between 0.0 and 1.0
    private double precipitation;
    private double temperature;
    private WorldTile flowTile; //Used for modelling rivers, shows the tile that this tile will flow to
    private Climate climate;
    private int riverSize;

    public WorldTile(int x, int y, double elevation, double temperature, double precipitation, Climate climate, int riverSize) {
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.climate = climate;
        this.riverSize = riverSize;
    }

    public double getElevation() {
        return this.elevation;
    }

    public WorldTile getFlowTile() {
        return this.flowTile;
    }

    public double getPrecipitation() {
        return this.precipitation;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setClimate(Climate climate) {
        this.climate = climate;
    }

    public Climate getClimate() {
        return this.climate;
    }

    public int getRiverSize() {
        return this.riverSize;
    }

    public void setFlowTile(WorldTile flowTile) {
        this.flowTile = flowTile;
    }

    @Override
    public String toString() {
        return "[" + x + ", "+ y + "]";
    }

}
