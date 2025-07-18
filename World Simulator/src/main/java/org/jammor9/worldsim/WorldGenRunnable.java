package org.jammor9.worldsim;

import org.jammor9.worldsim.worldgenerator.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WorldGenRunnable implements Runnable{

    private static long seed;

    private static final double EXPONENTIAL = 2; //Higher exponential makes mountains more pronounced but flattens out elevation map
    private static final double OCEAN_LEVEL = Math.pow(0.09, EXPONENTIAL);
    private static final double HILL_LEVEL = Math.pow(.1, EXPONENTIAL);
    private static final double MOUNTAIN_LEVEL = Math.pow(.45, EXPONENTIAL);
    private static final double BASE_TEMPERATURE = 21;

    private Terrain terrain;
    private WorldMap worldMap;
    private final int WIDTH;
    private final int HEIGHT;
    private boolean generated;

    public WorldGenRunnable(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.generated = false;
    }

    @Override
    public void run() {
        terrain = new Terrain(WIDTH, HEIGHT, OCEAN_LEVEL);
        Random seedGen = new Random();
        seed = seedGen.nextInt();
//        seed = 8888; //Debug Seed
        Random random = new Random(seed);
        ElevationGenerator elevationGenerator = new ElevationGenerator(terrain.getWidth(), terrain.getHeight(), seed, EXPONENTIAL);
        double[][] elevationGrid = elevationGenerator.generateElevation();

        //Initialises elevation data into the terrain map
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Node t = new Node(x, y, elevationGrid[y][x], BASE_TEMPERATURE);
                terrain.setNode(t, x, y);
            }
        }

        FillBasins fillBasins = new FillBasins(terrain);
        RiverGenerator riverGenerator = new RiverGenerator(terrain, random);
        Erosion erosion = new Erosion(terrain, random, 100_000);
        AtmosphereGenerator atmosphereGenerator = new AtmosphereGenerator(terrain);
        BiomeGenerator biomeGenerator = new BiomeGenerator(terrain);

        /*
        Terrain Generation Process
        -Start by filling basins
        -Then erode the terrain and apply blur to give terrain effect
        -Calculate the flow of rivers before a second fill, going straight to fill tends to make the rivers look unnaturally straight. Will have to look at this later.
        -Fill basins again
        -Now, using the flow calculated earlier, calculate the path of all rivers from random highpoints on the map.
         */
        fillBasins.fillBasins();
        erosion.hydraulicErosion();
        for (int i = 0; i < (int) EXPONENTIAL; i++) erosion.gaussianBlur();
        fillBasins.fillBasins();
        fillBasins.calculateFlow();
        HashSet<Node> rivers = riverGenerator.getRivers();
        riverGenerator.generateRivers();
        atmosphereGenerator.calculatePrecipitation();
        atmosphereGenerator.calculateTemperature();
        biomeGenerator.generateBiomes();
        adaptTerrain();
        ResourceGenerator resourceGenerator = new ResourceGenerator(worldMap, random);
        resourceGenerator.generateResources();
        RegionsGenerator regionsGenerator = new RegionsGenerator(worldMap, random);
        worldMap.setProvinces(regionsGenerator.generateRegions());
        this.generated = true;
    }

    //Converts terrain and node objects from the package to their application WorldMap and WorldTile equivelants
    private void adaptTerrain() {
        this.worldMap = new WorldMap(WIDTH, HEIGHT, OCEAN_LEVEL, HILL_LEVEL, MOUNTAIN_LEVEL);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Node n = terrain.getNode(x, y);
                double elev = n.getElevation();
                double temp = n.getTemperature();
                double precip = n.getPrecipitation();
                Climate climate = Climate.valueOf(n.getBiome().toString());
                int riverSize = n.getRiverSize();
                Node flowTile = n.getFlowTile();
                double[] flowCoords = null;
                if (flowTile != null) flowCoords = new double[] {flowTile.x, flowTile.y};

                boolean coastal = false;
                ArrayList<Node> neighbours = (ArrayList<Node>) terrain.getNeighbours(x, y);

                for (Node neighbour : neighbours) {
                    if (neighbour.getBiome() == Biome.OCEAN) {
                        coastal = true;
                        break;
                    }
                }

                WorldTile t = new WorldTile(x, y, elev, temp, precip, climate, riverSize, flowCoords, coastal);
                worldMap.setTile(t, x, y);
            }
        }
    }

    public WorldMap getWorldMap() {
        return this.worldMap;
    }

    public static double getExponential() {
        return EXPONENTIAL;
    }

    public Boolean isGenerated() {
        return this.generated;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public int getWidth() {
        return this.WIDTH;
    }
}
