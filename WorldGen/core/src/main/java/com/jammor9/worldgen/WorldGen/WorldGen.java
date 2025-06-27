package com.jammor9.worldgen.WorldGen;

/*
 * OpenSimplex2S Noise sample class.
 */

import com.jammor9.worldgen.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.HashSet;
import java.util.List;

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

public class WorldGen
{
    private static long seed;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private static final double OCEAN_LEVEL = .1;
    private static Terrain terrain;

    public static void main(String[] args)
        throws IOException {
        terrain = new Terrain(WIDTH, HEIGHT);

        Random seedGen = new Random();
        seed = seedGen.nextInt();
        Random random = new Random(seed);
        ElevationGenerator elevationGenerator = new ElevationGenerator(terrain.getWidth(), terrain.getHeight(), seed);
        double[][] elevationGrid = elevationGenerator.generateElevation();

        //Initialises elevation data into the Terrain map
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Tile t = new Tile(x, y, elevationGrid[y][x]);
                terrain.setTile(t, x, y);
            }
        }

        HashMap<TerrainType, Integer> terrainType = initTerrainMap();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        FillBasins fillBasins = new FillBasins(terrain);

        RiverGenerator riverGenerator = new RiverGenerator(terrain, random);
        terrain = fillBasins.filLBasins();



        elevationGrid = elevationGenerator.hydraulicErosion(elevationGrid);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                terrain.getTile(x, y).setElevation(elevationGrid[y][x]);
            }
        }

        fillBasins.calculateFlow();
        fillBasins.filLBasins();


        List<HashSet<Tile>> rivers = riverGenerator.getRivers();
        Terrain riverTerrain = riverGenerator.generateRivers();



        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x ++) {
                int rgb;
                //From what I see the value is never > 0.9. Probably due to how the squareGrid subtracts from grid.
                double v = terrain.getTile(x, y).getElevation();

                //Static Implementation
                if (v <= .02) rgb = terrainType.get(TerrainType.OCEAN);
                else if (v > .02 && v <= .1) rgb = terrainType.get(TerrainType.COAST);
                else if (v > .1 && v <= .13) rgb = terrainType.get(TerrainType.BEACH);
                else if (v > .13 && v <= .19) rgb = terrainType.get(TerrainType.FLATLANDS);
                else if (v > .1 && v <= .30) rgb = terrainType.get(TerrainType.LOWLANDS);
                else if (v > .30 && v <= .45) rgb = terrainType.get(TerrainType.MIDLANDS);
                else if (v > .45 && v <= .55) rgb = terrainType.get(TerrainType.HIGHLANDS);
                else if (v > .55&& v <= .75) rgb = terrainType.get(TerrainType.MOUNTAINS);
                else rgb = terrainType.get(TerrainType.PEAKS);

                image.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(image, "png", new File("noise.png"));

        int rgb = terrainType.get(TerrainType.COAST);
        for (HashSet<Tile> river : rivers) {
            System.out.println(river);
            for (Tile tile : river) {
                System.out.println(tile);
                image.setRGB(tile.x, tile.y, rgb);
            }
        }

        ImageIO.write(image, "png", new File("noise2.png"));
    }

    public static HashMap<TerrainType, Integer> initTerrainMap() {
        HashMap<TerrainType, Integer> terrainTypes = new HashMap<>();

        terrainTypes.put(TerrainType.OCEAN, new Color(15, 76, 209).getRGB());
        terrainTypes.put(TerrainType.COAST, new Color(28, 135, 235).getRGB());
        terrainTypes.put(TerrainType.BEACH, new Color(227, 213, 136).getRGB());
        terrainTypes.put(TerrainType.FLATLANDS, new Color(145, 230, 124).getRGB());
        terrainTypes.put(TerrainType.LOWLANDS, new Color(91, 156, 75).getRGB());
        terrainTypes.put(TerrainType.MIDLANDS, new Color(75, 133, 61).getRGB());
        terrainTypes.put(TerrainType.HIGHLANDS, new Color(50, 97, 39).getRGB());
        terrainTypes.put(TerrainType.MOUNTAINS, new Color(100, 102, 99).getRGB());
        terrainTypes.put(TerrainType.PEAKS, new Color(73, 74, 72).getRGB());

        return terrainTypes;
    }
}
