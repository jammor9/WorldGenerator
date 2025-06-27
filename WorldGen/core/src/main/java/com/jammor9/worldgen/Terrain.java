package com.jammor9.worldgen;

import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private Tile[][] terrainGrid;
    private int gridSize;

    public Terrain(int gridSize) {
        this.gridSize = gridSize;
        this.terrainGrid = new Tile[gridSize][gridSize];
    }

    public int size() {
        return terrainGrid.length;
    }

    //Returns a tile if it is within the bounds of the array
    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) return null;
        return terrainGrid[y][x];
    }

    public void setTile(Tile tile, int x, int y) {
        if (x < 0 || y < 0 || x > gridSize || y > gridSize) return;
        terrainGrid[y][x] = tile;
    }


    //Returns all 8 neighbours of a particular grid space
    public List<Tile> getNeighbours(int x, int y) {
        List<Tile> neighbours = new ArrayList<>();

        for (int ix = x-1; ix<=x+1; ix++) {
            for (int iy = y-1; iy<=y+1; iy++) {
                if (iy == y && ix == x) continue;
                Tile neighbour = getTile(ix, iy);
                if (neighbour != null) neighbours.add(neighbour);
            }
        }

        return neighbours;
    }

    //Returns 4 neighbours, more useful for river generation
    public List<Tile> getFourNeighbours(int x, int y) {
        List<Tile> neighbours = new ArrayList<>();
        for (int ix = x-1; ix <=x+1; ix+=2) {
            Tile neighbour = getTile(ix, y);
            if (neighbour != null) neighbours.add(neighbour);
        }

        for (int iy = y-1; iy <=y+1; iy+=2) {
            Tile neighbour = getTile(x, iy);
            if (neighbour != null) neighbours.add(neighbour);
        }

        return neighbours;
    }
}
