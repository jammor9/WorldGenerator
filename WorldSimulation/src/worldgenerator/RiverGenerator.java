package worldgenerator;

import java.util.*;

public class RiverGenerator {

    private Terrain terrain;
    private final int HEIGHT;
    private final int WIDTH;

    private List<Node> localMaxima; //Used to determine river start locations
    private HashSet<Node> visitedNodes;
    private HashSet<Node> riverStarts;
    private Random random; //Intake the world seed to maintain consistency

    private static final int RIVER_MAXIMA_RATIO = 10; //Determines what % of local maxima should be used as river points
    private static final int MAX_RIVER_SIZE = 10; //Maximum size of a river, increases with tributary connections

    public RiverGenerator(Terrain terrain, Random random) {
        this.terrain = terrain;
        this.HEIGHT = terrain.getHeight();
        this.WIDTH = terrain.getWidth();
        this.random = random;
        this.localMaxima = new ArrayList<>();
        this.visitedNodes = new HashSet<>();
        this.riverStarts = new HashSet<>();
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
            Node start = localMaxima.get(random.nextInt(localMaxima.size()));
            riverStarts.add(start);
        }

        //Then chart the downhill flow at each river tile
        for (Node n : riverStarts) {
            HashSet<Node> river = new HashSet<>();
            Node current = n;
            Node next;
            while (current.getFlowTile() != null) {
                river.add(current);
                next = current.getFlowTile();
                if (next.getElevation() > current.getElevation()) next.setElevation(current.getElevation());
                current.setRiverSize(current.getRiverSize()+1);
                current = next;
                if (current.getElevation() <= terrain.getOceanLevel()) break;
            }
            //Add the river to the list of rivers, and add all tiles to the visitedNodes set to handle merging rivers
            visitedNodes.addAll(river);
        }

        return terrain;
    }

    //Find all points in the array that are a peak compared to their neighbours and are above water level
    private void findLocalMaxima() {

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Node tile = terrain.getNode(x,y);
                if (tile.getElevation() <= Math.pow(.3, WorldGen.getExponential())) continue;

                List<Node> neighbours = terrain.getNeighbours(x, y);
                boolean highest = true;
                for (Node neighbour : neighbours) {
                    if (neighbour.getElevation() >= tile.getElevation()) {
                        highest = false;
                        break;
                    }
                }
                if (highest) localMaxima.add(tile);
            }
        }
    }

    public HashSet<Node> getRivers() {
        return this.visitedNodes;
    }

    public static int getMaxRiverSize() {
        return MAX_RIVER_SIZE;
    }
}