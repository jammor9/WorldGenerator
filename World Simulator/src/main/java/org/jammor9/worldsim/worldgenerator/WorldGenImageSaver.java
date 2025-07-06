package org.jammor9.worldsim.worldgenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.HashSet;

enum TerrainType {
    OCEAN,
    COAST,
    BEACH,
    FLATLANDS,
    LOWLANDS,
    HIGHLANDS,
    MOUNTAINS,
    MIDLANDS,
    PEAKS
}

public class WorldGenImageSaver
{
    private static long seed;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private static final double EXPONENTIAL = 2; //Higher exponential makes mountains more pronounced but flattens out elevation map
    private static final double OCEAN_LEVEL = Math.pow(0.09, EXPONENTIAL);
    private static final double BASE_TEMPERATURE = 21;
    private static Terrain terrain;

    public void run() throws IOException {
        terrain = new Terrain(WIDTH, HEIGHT, OCEAN_LEVEL);

        Random seedGen = new Random();
        seed = seedGen.nextInt();
//        seed = 8888; //Debug Seed
        Random random = new Random(seed);
        ElevationGenerator elevationGenerator = new ElevationGenerator(terrain.getWidth(), terrain.getHeight(), seed, EXPONENTIAL);
        double[][] elevationGrid = elevationGenerator.generateElevation();

        //Initialises elevation data into the worldgenerator.Terrain map
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Node t = new Node(x, y, elevationGrid[y][x], BASE_TEMPERATURE);
                terrain.setNode(t, x, y);
            }
        }

        HashMap<TerrainType, Integer> terrainType = initTerrainMap();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

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

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int rgb = 0x010101 * (int)((terrain.getNode(x, y).getElevation()+ 1) * 127.5);
                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File("heightmap.png"));

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                double precip = terrain.getNode(x, y).getPrecipitation();
                int rgb;

                if (precip <= 0) rgb = new Color(255, 255, 255).getRGB();
                else if (precip > 0 && precip < 0.2) rgb = new Color(64, 169, 255).getRGB();
                else if (precip >= 0.2 && precip < 0.5) rgb = new Color(3, 140, 252).getRGB();
                else if (precip >= 0.5 && precip < 1) rgb = new Color(4, 120, 214).getRGB();
                else if (precip >= 1 && precip < 1.5) rgb = new Color(0, 88, 161).getRGB();
                else if (precip >= 1.5 && precip < 2) rgb = new Color(0, 59, 107).getRGB();
                else if (precip >= 2 && precip < 2.5) rgb = new Color(0, 38, 69).getRGB();
                else rgb = new Color(0, 25, 46).getRGB();

                image.setRGB(x, y, rgb);

            }
        }
        ImageIO.write(image, "png", new File("rainfall.png"));

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                double temp = terrain.getNode(x, y).getTemperature();
                int rgb;

                if (terrain.getNode(x, y).getElevation() < OCEAN_LEVEL) {
                    rgb = new Color(255, 255, 255).getRGB();
                    continue;
                }

                if (temp >= 45) rgb = new Color(191, 25, 0).getRGB();
                else if (temp < 45 && temp >= 40) rgb = new Color(191, 67, 0).getRGB();
                else if (temp < 40 && temp >= 35) rgb = new Color(201, 103, 4).getRGB();
                else if (temp < 35 && temp >= 30) rgb = new Color(214, 149, 9).getRGB();
                else if (temp < 30 && temp >= 25) rgb = new Color(237, 171, 28).getRGB();
                else if (temp < 25 && temp >= 20) rgb = new Color(183, 204, 47).getRGB();
                else if (temp < 20 && temp >= 15) rgb = new Color(47, 204, 118).getRGB();
                else if (temp < 15 && temp >= 10) rgb = new Color(47, 204, 167).getRGB();
                else if (temp < 10 && temp >= 5) rgb = new Color(47, 201, 204).getRGB();
                else if (temp < 5 && temp >= 0) rgb = new Color(47, 160, 204).getRGB();
                else if (temp < 0 && temp >= -5) rgb = new Color(47, 120, 204).getRGB();
                else rgb = new Color(47, 57, 204).getRGB();

                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File("temperature.png"));

        for (int y= 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int rgb = new Color(255, 255, 255).getRGB();

                Biome biome = terrain.getNode(x, y).getBiome();
                switch(biome) {
                    case HOT_DESERT -> rgb = new Color(217, 7, 7).getRGB();
                    case HUMID_CONTINENTAL -> rgb = new Color(34, 159, 227).getRGB();
                    case TUNDRA -> rgb = new Color(117, 117, 117).getRGB();
                    case SUBARCTIC_CONTINENTAL -> rgb = new Color(17, 103, 150).getRGB();
                    case COLD_DESERT -> rgb = new Color(214, 135, 171).getRGB();
                    case TEMPERATE_FOREST -> rgb = new Color(14, 92, 8).getRGB();
                    case BOREAL_FOREST -> rgb = new Color(8, 92, 63).getRGB();
                    case SAVANNAH -> rgb = new Color(157, 207, 83).getRGB();
                    case TROPICAL -> rgb = new Color(55, 82, 16).getRGB();
                    case COLD_STEPPE -> rgb = new Color(227, 179, 91).getRGB();
                    case HOT_STEPPE -> rgb = new Color(217, 121, 26).getRGB();
                    case ICE -> rgb = new Color(200, 200, 200).getRGB();
                    case DESERT -> rgb = new Color(222, 205, 120).getRGB();
                    case GRASSLAND -> rgb = new Color(110, 201, 85).getRGB();
                    case SEASONAL_FOREST -> rgb = new Color(54, 102, 41).getRGB();
                    case TEMPERATE_RAINFOREST -> rgb = new Color(20, 56, 10).getRGB();
                    case WOODLAND -> rgb = new Color(32, 74, 21).getRGB();
                }

                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File("biomes.png"));

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x ++) {
                int rgb;
                //From what I see the value is never > 0.9. Probably due to how the squareGrid subtracts from grid.
                double v = terrain.getNode(x, y).getElevation();

                //Static Implementation
                if (v <= Math.pow(.02, EXPONENTIAL)) rgb = terrainType.get(TerrainType.OCEAN);
                else if (v > Math.pow(.02, EXPONENTIAL) && v <= OCEAN_LEVEL) rgb = terrainType.get(TerrainType.COAST);
                else if (v >  OCEAN_LEVEL && v <= Math.pow(.13, EXPONENTIAL)) rgb = terrainType.get(TerrainType.BEACH);
                else if (v > Math.pow(.13, EXPONENTIAL) && v <= Math.pow(.19, EXPONENTIAL)) rgb = terrainType.get(TerrainType.FLATLANDS);
                else if (v > Math.pow(.1, EXPONENTIAL) && v <= Math.pow(.30, EXPONENTIAL)) rgb = terrainType.get(TerrainType.LOWLANDS);
                else if (v > Math.pow(.30, EXPONENTIAL) && v <= Math.pow(.45, EXPONENTIAL)) rgb = terrainType.get(TerrainType.MIDLANDS);
                else if (v > Math.pow(.45, EXPONENTIAL) && v <= Math.pow(.55, EXPONENTIAL)) rgb = terrainType.get(TerrainType.HIGHLANDS);
                else if (v > Math.pow(.55, EXPONENTIAL) && v <= Math.pow(.75, EXPONENTIAL)) rgb = terrainType.get(TerrainType.MOUNTAINS);
                else rgb = terrainType.get(TerrainType.PEAKS);

                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File("noise.png"));

        for (Node n : rivers) {
            int rgb = new Color(0, 0, 0).getRGB();
            if (n.getRiverSize() == 0) continue;
            if (n.getRiverSize() >= 1) rgb = new Color(28, 135, 235).getRGB();
            if (n.getRiverSize() >= 3) rgb = new Color(12, 94, 171).getRGB();
            if (n.getRiverSize() >= 5) rgb = new Color(4, 72, 135).getRGB();
            if (n.getRiverSize() >= 7) rgb = new Color(2, 58, 110).getRGB();
            if (n.getRiverSize() >= 9) rgb = new Color(1, 43, 82).getRGB();
            image.setRGB(n.x, n.y, rgb);
        }
        ImageIO.write(image, "png", new File("river.png"));

        double minElev = Double.MAX_VALUE;
        double maxElev = Double.MIN_VALUE;
        double minTemp = Double.MAX_VALUE;
        double maxTemp = Double.MIN_VALUE;

        for (int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                Node n = terrain.getNode(x, y);
                if (n.getElevation() < minElev) minElev = n.getElevation();
                if (n.getElevation() > maxElev) maxElev = n.getElevation();
                if (n.getBiome() != Biome.OCEAN) {
                    if (n.getTemperature() < minTemp) minTemp = n.getTemperature();
                    if (n.getTemperature() > maxTemp) maxTemp = n.getTemperature();
                }
            }
        }

        System.out.println(maxElev);
        System.out.println("Temps:" + minTemp + " " + maxTemp);
    }

    public static HashMap<TerrainType, Integer> initTerrainMap() {
        HashMap<TerrainType, Integer> terrainTypes = new HashMap<>();

        //Rough elevation guides?
        terrainTypes.put(TerrainType.OCEAN, new Color(15, 76, 209).getRGB());
        terrainTypes.put(TerrainType.COAST, new Color(28, 135, 235).getRGB());
        terrainTypes.put(TerrainType.BEACH, new Color(227, 213, 136).getRGB()); //0-50m
        terrainTypes.put(TerrainType.FLATLANDS, new Color(145, 230, 124).getRGB()); //50-150
        terrainTypes.put(TerrainType.LOWLANDS, new Color(91, 156, 75).getRGB()); //150-250
        terrainTypes.put(TerrainType.MIDLANDS, new Color(75, 133, 61).getRGB()); //250-350
        terrainTypes.put(TerrainType.HIGHLANDS, new Color(50, 97, 39).getRGB()); //350-600
        terrainTypes.put(TerrainType.MOUNTAINS, new Color(100, 102, 99).getRGB()); //600-1000
        terrainTypes.put(TerrainType.PEAKS, new Color(73, 74, 72).getRGB()); //1000-2000 ?

        return terrainTypes;
    }

    public static double getExponential() {
        return EXPONENTIAL;
    }
}
