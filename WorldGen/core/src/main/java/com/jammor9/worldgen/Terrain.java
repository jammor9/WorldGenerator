package com.jammor9.worldgen;

import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private Tile[][] terrainGrid;
    private final int WIDTH;
    private final int HEIGHT;

    public Terrain(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.terrainGrid = new Tile[height][width];
    }

    public int size() {
        return terrainGrid.length;
    }

    //Returns a tile if it is within the bounds of the array
    public Tile getTile(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return terrainGrid[y][x];
    }

    public void setTile(Tile tile, int x, int y) {
        if (!isInBounds(x, y)) return;
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

    private boolean isInBounds(int x, int y) {
        return !(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT);
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }
}
