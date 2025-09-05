package org.jammor9.worldsim;

import java.util.ArrayList;
import java.util.List;

public class WorldMap {
    private final int WIDTH;
    private final int HEIGHT;
    private final double OCEAN_LEVEL;
    private final double HILL_LEVEL;
    private final double MOUNTAIN_LEVEL;
    private WorldTile[][] tileMap;

    private Province[] provinces;

    public WorldMap(int width, int height, double oceanLevel, double hillLevel, double mountainLevel) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.OCEAN_LEVEL = oceanLevel;
        this.HILL_LEVEL = hillLevel;
        this.MOUNTAIN_LEVEL = mountainLevel;
        this.tileMap = new WorldTile[height][width];
    }

    //Returns a tile if it is within the bounds of the array
    public WorldTile getTile(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return tileMap[y][x];
    }

    public void setTile(WorldTile tile, int x, int y) {
        if (!isInBounds(x, y)) return;
        tileMap[y][x] = tile;
    }

    //Returns all 8 neighbours of a particular grid space
    public List<WorldTile> getNeighbours(int x, int y) {
        List<WorldTile> neighbours = new ArrayList<>();

        for (int ix = x-1; ix<=x+1; ix++) {
            for (int iy = y-1; iy<=y+1; iy++) {
                if (iy == y && ix == x) continue;
                WorldTile neighbour = getTile(ix, iy);
                if (neighbour != null) neighbours.add(neighbour);
            }
        }
        return neighbours;
    }

    //Returns 4 neighbours, more useful for river generation
    public List<WorldTile> getFourNeighbours(int x, int y) {
        List<WorldTile> neighbours = new ArrayList<>();
        for (int ix = x-1; ix <=x+1; ix+=2) {
            WorldTile neighbour = getTile(ix, y);
            if (neighbour != null) neighbours.add(neighbour);
        }

        for (int iy = y-1; iy <=y+1; iy+=2) {
            WorldTile neighbour = getTile(x, iy);
            if (neighbour != null) neighbours.add(neighbour);
        }

        return neighbours;
    }

    //Checks if a given set of coordinates are in the bounds of the grid
    private boolean isInBounds(int x, int y) {
        return !(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT);
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public double getOceanLevel() {
        return this.OCEAN_LEVEL;
    }

    public double getHillLevel() {
        return this.HILL_LEVEL;
    }

    public double getMountainLevel() {
        return this.MOUNTAIN_LEVEL;
    }

    public void setProvinces(Province[] provinces) {
        this.provinces = provinces;
    }

    public Province[] getProvinces() {
        return this.provinces;
    }

    public WorldTile[][] getHeightmap() {
        return this.tileMap;
    }
}
