package worldgenerator;

public class Node implements Comparable<Node> {

    public int x;
    public int y;
    private double elevation; //Self explanatory, range between 0.0 and 1.0
    private double precipitation;
    private double temperature;
    private Node flowTile; //Used for modelling rivers, shows the tile that this tile will flow to
    private Biome biome;

    public Node(int x, int y, double elevation, double temperature) {
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.temperature = temperature;
        this.flowTile = null;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public Node getFlowTile() {
        return this.flowTile;
    }

    public void setFlowTile(Node flowTile) {
        this.flowTile = flowTile;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
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

    @Override
    public String toString() {

        return "[" + x + ", "+ y + "]";
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.getElevation(), o.getElevation());
    }
}
