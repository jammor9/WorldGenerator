package org.jammor9.worldsim;

import org.jammor9.worldsim.worldgenerator.Biome;

public class WorldTile {

    public int x;
    public int y;
    private double elevation; //Self explanatory, range between 0.0 and 1.0
    private double precipitation;
    private double temperature;
    private WorldTile flowTile; //Used for modelling rivers, shows the tile that this tile will flow to
    private Biome biome;
    private int riverSize;

    public WorldTile(int x, int y, double elevation, double temperature, double precipitation, Biome biome, int riverSize) {
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.biome = biome;
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

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public Biome getBiome() {
        return this.biome;
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
