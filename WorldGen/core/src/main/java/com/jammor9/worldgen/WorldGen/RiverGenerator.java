package com.jammor9.worldgen.WorldGen;

import com.jammor9.worldgen.Terrain;
import com.jammor9.worldgen.Tile;

import java.util.*;

public class RiverGenerator {

    private Terrain terrain;
    private int gridSize;
    private List<Tile> localMaxima; //Used to determine river start locations
    private List<HashSet<Tile>> rivers;
    private HashSet<Tile> visitedTiles;
    private HashSet<Tile> riverStarts;
    private Random random; //Intake the world seed to maintain consistency

    private static final int RIVER_MAXIMA_RATIO = 10; //Determines what % of local maxima should be used as river points

    public RiverGenerator(Terrain terrain, Random random) {
        this.terrain = terrain;
        this.gridSize = terrain.size();
        this.random = random;
        this.localMaxima = new ArrayList<>();
        this.visitedTiles = new HashSet<>();
        this.riverStarts = new HashSet<>();
        this.rivers = new ArrayList<>();
        findLocalMaxima();
    }

    /*
    1. Select 10% of localMaxima as starting points for rivers
    2. For each starting point, following the riverFlow taken from calculateFlow() until sea level is reached
        2a. If another river is touched, then river has merged with another and is now complete
    3. At each point, lower the elevation to be equal to the next tile in the flow to simulate erosion and ensure no rivers
       travel uphill.
    4. Once a river has finished, add it to a list of all rivers to be painted later, and add all visited tiles to the
       visited tiles set to handle merging
     */
    public Terrain generateRivers() {
        //First choose the river starting locations.
        int riverCount = localMaxima.size() / RIVER_MAXIMA_RATIO;

        while (riverStarts.size() < riverCount) {
            Tile start = localMaxima.get(random.nextInt(localMaxima.size()));
            riverStarts.add(start);
        }

//        //First carve all maxima "false rivers" to give the terrain a more natural look
//        for (Tile tile : localMaxima) {
//            HashSet<Tile> river = new HashSet<>();
//            Tile currentTile = tile;
//            Tile nextTile;
//            while (currentTile.getElevation() > 0.1) {
//                nextTile = currentTile.getFlowTile();
//                if (nextTile.getElevation() > currentTile.getElevation()) nextTile.setElevation(currentTile.getElevation());
//                currentTile = nextTile;
//            }
//        }

        for (Tile tile : riverStarts) {
            HashSet<Tile> river = new HashSet<>();
            Tile currentTile = tile;
            Tile nextTile;
            while (currentTile.getFlowTile() != null) {
                river.add(currentTile);
                nextTile = currentTile.getFlowTile();
                if (nextTile.getElevation() > currentTile.getElevation()) nextTile.setElevation(currentTile.getElevation());
                currentTile = nextTile;
                if (visitedTiles.contains(currentTile)) break;
            }

            rivers.add(river);
            visitedTiles.addAll(river);
        }

        return terrain;
    }

    //Find all points in the array that are a peak compared to their neighbours and are above water level
    private void findLocalMaxima() {

        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                Tile tile = terrain.getTile(x,y);
                if (tile.getElevation() <= .3) continue;

                List<Tile> neighbours = terrain.getNeighbours(x, y);
                boolean highest = true;
                for (Tile neighbour : neighbours) {
                    if (neighbour.getElevation() >= tile.getElevation()) {
                        highest = false;
                        break;
                    }
                }
                if (highest) localMaxima.add(tile);
            }
        }
    }

    public List<HashSet<Tile>> getRivers() {
        return this.rivers;
    }
}
