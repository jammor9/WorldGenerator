package worldgenerator;

import java.util.ArrayList;
import java.util.List;

class Terrain {

    private Node[][] terrainGrid;
    private final int WIDTH;
    private final int HEIGHT;

    public Terrain(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.terrainGrid = new Node[height][width];
    }

    //Returns a tile if it is within the bounds of the array
    public Node getTile(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return terrainGrid[y][x];
    }

    public void setTile(Node tile, int x, int y) {
        if (!isInBounds(x, y)) return;
        terrainGrid[y][x] = tile;
    }

    //Returns all 8 neighbours of a particular grid space
    public List<Node> getNeighbours(int x, int y) {
        List<Node> neighbours = new ArrayList<>();

        for (int ix = x-1; ix<=x+1; ix++) {
            for (int iy = y-1; iy<=y+1; iy++) {
                if (iy == y && ix == x) continue;
                Node neighbour = getTile(ix, iy);
                if (neighbour != null) neighbours.add(neighbour);
            }
        }

        return neighbours;
    }

    //Returns 4 neighbours, more useful for river generation
    public List<Node> getFourNeighbours(int x, int y) {
        List<Node> neighbours = new ArrayList<>();
        for (int ix = x-1; ix <=x+1; ix+=2) {
            Node neighbour = getTile(ix, y);
            if (neighbour != null) neighbours.add(neighbour);
        }

        for (int iy = y-1; iy <=y+1; iy+=2) {
            Node neighbour = getTile(x, iy);
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

    public Node[][] getTerrainGrid() {
        return this.terrainGrid;
    }
}
