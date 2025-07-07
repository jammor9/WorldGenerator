package org.jammor9.worldsim.worldgenerator;

public class Node implements Comparable<Node> {

    public int x;
    public int y;
    private double elevation; //Self explanatory, range between 0.0 and 1.0
    private double precipitation;
    private double temperature;
    private Node flowTile; //Used for modelling rivers, shows the tile that this tile will flow to
    private Biome biome;
    private int riverSize;

    //Decimal Truncation to save space
    private final int ELEVATION_TRUNCATION = 10_000;
    private final int TEMPERATURE_TRUNCATION = 100;
    private final int PRECIPITATION_TRUNCATION = 1000;
    private double MIN_ELEV = Math.pow(0.001, WorldGenImageSaver.getExponential());

    public Node(int x, int y, double elevation, double temperature) {
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.temperature = temperature;
        this.flowTile = null;
        this.riverSize = 0;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        if (elevation < MIN_ELEV) elevation = MIN_ELEV;
        int temp = (int) (elevation * ELEVATION_TRUNCATION);
        this.elevation = (double) temp / ELEVATION_TRUNCATION;
    }

    public Node getFlowTile() {
        return this.flowTile;
    }

    public void setFlowTile(Node flowTile) {
        this.flowTile = flowTile;
    }

    public void setPrecipitation(double precipitation) {
        int temp = (int) (precipitation * PRECIPITATION_TRUNCATION);
        this.precipitation = (double) temp / PRECIPITATION_TRUNCATION;
    }

    public double getPrecipitation() {
        return this.precipitation;
    }

    public void setTemperature(double temperature) {
        int temp = (int) (temperature * TEMPERATURE_TRUNCATION);
        this.temperature = (double) temp / TEMPERATURE_TRUNCATION;
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

    public void setRiverSize(int riverSize) {
        if (riverSize <= RiverGenerator.getMaxRiverSize()) this.riverSize = riverSize;
    }

    @Override
    public String toString() {
        return "[" + x + ", "+ y + "]";
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.getElevation(), o.getElevation());
    }
}
