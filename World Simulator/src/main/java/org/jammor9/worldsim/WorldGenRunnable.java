package org.jammor9.worldsim;

import org.jammor9.worldsim.worldgenerator.*;

import java.util.HashSet;
import java.util.Random;

public class WorldGenRunnable implements Runnable{

    private static long seed;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private static final double EXPONENTIAL = 2; //Higher exponential makes mountains more pronounced but flattens out elevation map
    private static final double OCEAN_LEVEL = Math.pow(0.09, EXPONENTIAL);
    private static final double BASE_TEMPERATURE = 21;
    private Terrain terrain;
    private WorldMap worldMap;

    public WorldGenRunnable() {}

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
        biomeGenerator.generateBiomesTwo();
        adaptTerrain();
    }

    public void adaptTerrain() {
        this.worldMap = new WorldMap(WIDTH, HEIGHT, OCEAN_LEVEL);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Node n = terrain.getNode(x, y);
                double elev = n.getElevation();
                double temp = n.getTemperature();
                double precip = n.getPrecipitation();
                Biome biome = n.getBiome();
                int riverSize = n.getRiverSize();
                WorldTile t = new WorldTile(x, y, elev, temp, precip, biome, riverSize);
                worldMap.setTile(t, x, y);
            }
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Node n = terrain.getNode(x, y);
                if (n.getFlowTile() == null) continue;
                int fx = n.getFlowTile().x;
                int fy = n.getFlowTile().y;
                worldMap.getTile(x, y).setFlowTile(worldMap.getTile(fx, fy));
            }
        }
    }

    public WorldMap getWorldMap() {
        return this.worldMap;
    }
}
