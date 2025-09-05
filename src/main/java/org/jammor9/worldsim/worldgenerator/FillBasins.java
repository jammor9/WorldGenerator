package org.jammor9.worldsim.worldgenerator;

import java.util.*;

/*
Source for fillBasin:
Barnes, Lehman, Mulla. “Priority-Flood: An Optimal Depression-Filling and Watershed-Labeling
Algorithm for Digital Elevation Models”. Computers & Geosciences. Vol 62, Jan 2014, pp 117–127. doi:
“10.1016/j.cageo.2013.04.024
 */

public class FillBasins {

    private Terrain terrain;
    private final int HEIGHT;
    private final int WIDTH;
    private HashSet<Node> edges;
    private HashSet<Node> localMinima;

    public FillBasins(Terrain terrain) {
        this.terrain = terrain;
        this.HEIGHT = terrain.getHeight();
        this.WIDTH = terrain.getWidth();
        this.edges = new HashSet<>();
        this.localMinima = new HashSet<>();
        findEdges();
    }

    //Implements algorithm 3 from the above citation, solving the issue of flat terrain
    public Terrain fillBasins() {
        PriorityQueue<Node> open = new PriorityQueue<>();
        boolean[][] closed = new boolean[HEIGHT][WIDTH];
        LinkedList<Node> pit = new LinkedList<>();
        Node none = new Node(-1, -1, -1, 21);
        Node pitTop = none;

        for (Node edge : edges) {
            open.add(edge);
            closed[edge.y][edge.x] = true;
        }
        Node c;

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

            List<Node> neighbours = terrain.getFourNeighbours(c.x, c.y);

            for (Node n : neighbours) {
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
        PriorityQueue<Node> open = new PriorityQueue<>();
        boolean[][] closed = new boolean[HEIGHT][WIDTH];

        for (Node c : edges) {
            open.add(c);
            closed[c.y][c.x] = true;

            c.setFlowTile(null);
        }

        while(!open.isEmpty()) {
            Node c = open.poll();

            List<Node> neighbours = terrain.getFourNeighbours(c.x, c.y);

            for (Node n : neighbours) {
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
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (y == 0 || y == HEIGHT - 1 || x == 0 || x == WIDTH -1 ) {
                    edges.add(terrain.getNode(x, y));
                }
            }
        }
    }
}