package com.jammor9.worldgen;

public class Tile implements Comparable<Tile> {

    public int x;
    public int y;
    private double elevation; //Self explanatory, range between 0.0 and 1.0
    private Tile flowTile; //Used for modelling rivers, shows the tile that this tile will flow to

    public Tile(int x, int y, double elevation) {
        this.x = x;
        this.y = y;
        this.elevation = elevation;
        this.flowTile = null;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public Tile getFlowTile() {
        return this.flowTile;
    }

    public void setFlowTile(Tile flowTile) {
        this.flowTile = flowTile;
    }

    @Override
    public String toString() {

        return "[" + x + ", "+ y + "]";
    }

    @Override
    public int compareTo(Tile o) {
        return Double.compare(this.getElevation(), o.getElevation());
    }
}
