package org.jammor9.worldsim.worldgenerator;

import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private Node[][] heightmap;
    private final int WIDTH;
    private final int HEIGHT;
    private final double OCEAN_LEVEL;

    public Terrain(int width, int height, double oceanLevel) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.OCEAN_LEVEL = oceanLevel;
        this.heightmap = new Node[height][width];
    }

    //Returns a tile if it is within the bounds of the array
    public Node getNode(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return heightmap[y][x];
    }

    public void setNode(Node tile, int x, int y) {
        if (!isInBounds(x, y)) return;
        heightmap[y][x] = tile;
    }

    //Returns all 8 neighbours of a particular grid space
    public List<Node> getNeighbours(int x, int y) {
        List<Node> neighbours = new ArrayList<>();

        for (int ix = x-1; ix<=x+1; ix++) {
            for (int iy = y-1; iy<=y+1; iy++) {
                if (iy == y && ix == x) continue;
                Node neighbour = getNode(ix, iy);
                if (neighbour != null) neighbours.add(neighbour);
            }
        }
        return neighbours;
    }

    //Returns 4 neighbours, more useful for river generation
    public List<Node> getFourNeighbours(int x, int y) {
        List<Node> neighbours = new ArrayList<>();
        for (int ix = x-1; ix <=x+1; ix+=2) {
            Node neighbour = getNode(ix, y);
            if (neighbour != null) neighbours.add(neighbour);
        }

        for (int iy = y-1; iy <=y+1; iy+=2) {
            Node neighbour = getNode(x, iy);
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

    public Node[][] getHeightmap() {
        return this.heightmap;
    }
}
