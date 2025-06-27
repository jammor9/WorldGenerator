package com.jammor9.worldgen.WorldGen;

import com.jammor9.worldgen.Terrain;
import com.jammor9.worldgen.Tile;

import java.util.*;

/*
Source for fillBasin:
Barnes, Lehman, Mulla. “Priority-Flood: An Optimal Depression-Filling and Watershed-Labeling
Algorithm for Digital Elevation Models”. Computers & Geosciences. Vol 62, Jan 2014, pp 117–127. doi:
“10.1016/j.cageo.2013.04.024
 */

public class FillBasins {

    Terrain terrain;
    int gridSize;
    HashSet<Tile> edges;
    HashSet<Tile> localMinima;

    public FillBasins(Terrain terrain) {
        this.terrain = terrain;
        this.gridSize = terrain.size();
        this.edges = new HashSet<>();
        this.localMinima = new HashSet<>();
        findEdges();
    }

    //Implements algorithm 3 from the above citation, solving the issue of flat terrain
    public Terrain filLBasins() {
        PriorityQueue<Tile> open = new PriorityQueue<>();
        boolean[][] closed = new boolean[gridSize][gridSize];
        LinkedList<Tile> pit = new LinkedList<>();
        Tile none = new Tile(-1, -1, -1);
        Tile pitTop = none;

        for (Tile edge : edges) {
            open.add(edge);
            closed[edge.y][edge.x] = true;
        }
        Tile c;

        while (!open.isEmpty() || !pit.isEmpty()) {
            if (open.peek() == pit.peek()) {
                c = open.poll();
                pitTop = none;
            }
            else if (!pit.isEmpty()) {
                c = pit.poll();
                if (pitTop == none) pitTop = c;
            }
            else {
                c = open.poll();
                pitTop = none;
            }

            List<Tile> neighbours = terrain.getFourNeighbours(c.x, c.y);

            for (Tile n : neighbours) {
                if (closed[n.y][n.x]) continue;
                closed[n.y][n.x] = true;
                if (n.getElevation() == -1) pit.push(n);
                else if (n.getElevation() <= Math.nextAfter(c.getElevation(), Double.POSITIVE_INFINITY)) {
                    if (pitTop.getElevation() < n.getElevation() && Math.nextAfter(c.getElevation(), Double.POSITIVE_INFINITY) >= n.getElevation()) {
                        n.setElevation(Math.nextAfter(c.getElevation(), Double.POSITIVE_INFINITY));
                        pit.push(n);
                    }
                    n.setElevation(Math.nextAfter(c.getElevation(), Double.POSITIVE_INFINITY));
                    pit.push(n);
                }
                else {
                    open.add(n);
                }

            }
        }

        return terrain;
    }

    //Implements algorithm 4 of the above citation, useful for natural rivers as algorithm 3 tends to create very straight rivers
    public Terrain calculateFlow() {
        PriorityQueue<Tile> open = new PriorityQueue<>();
        boolean[][] closed = new boolean[gridSize][gridSize];

        for (Tile c : edges) {
            open.add(c);
            closed[c.y][c.x] = true;

            c.setFlowTile(null);
        }

        while(!open.isEmpty()) {
            Tile c = open.poll();

            List<Tile> neighbours = terrain.getFourNeighbours(c.x, c.y);

            for (Tile n : neighbours) {
                if (closed[n.y][n.x]) continue;
                closed[n.y][n.x] = true;
                if (n.getElevation() == -1) n.setFlowTile(null);
                else n.setFlowTile(c);
                open.add(n);
            }
        }

        return terrain;
    }

    //Finds all edges of the map, helper method
    public void findEdges() {
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (y == 0 || y == gridSize - 1 || x == 0 || x == gridSize -1 ) {
                    edges.add(terrain.getTile(x, y));
                }
            }
        }
    }
}
