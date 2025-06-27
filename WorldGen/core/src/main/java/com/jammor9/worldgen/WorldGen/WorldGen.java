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

    /*
    Seed: Self Explanatory, provides the seed for the world
    WIDTH & HEIGHT: Determine the size of the terrain map
    FREQUENCY: Part of OpenSimplex Algorithm
    OCTAVES: How many times OpenSimplex is repeated, higher octaves create more detailed maps
    PERSISTENCE: Also known as gain, used to increase amplitude of frequency during each octave
    LACUNARITY: Increases frequency during each octave
    Scale: How zoomed in the OpenSimplex algorithm is. For a 1024x1024 map 300 seems to work well. Larger maps require a bigger scale.
            Roughly speaking, if you double the size of the grid you will need to double the scale. Although it's best to play around.
    Grid: Stores the elevation values for the terrain map
     */
    private static final long SEED = 1;
    private static long seed;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private static final double FREQUENCY = 1;
    private static final int OCTAVES = 25; //How many times Simplex is repeated, higher octaves creates more detailed maps
    private static final double PERSISTENCE = 0.5;
    private static final double LACUNARITY = 2;
    private static double scale = 150; //How zoomed in the OpenSimplex algorithm is. ~250-350 seems to provide best landforms
    private static Terrain terrain;

    public static void main(String[] args)
        throws IOException {
        terrain = new Terrain(HEIGHT);
        double maxNoiseHeight = Double.MIN_VALUE;
        double minNoiseHeight = Double.MAX_VALUE;
        Random seedGen = new Random();
        seed = seedGen.nextInt();
        Random random = new Random(seed);

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                double freq = FREQUENCY;
                double amplitude = 1;
                double noiseHeight = 0;

                for (int i = 0; i < OCTAVES; i++) {
                    double value = OpenSimplex2S.noise3_ImproveXY(seed, x / scale * freq, y / scale * freq, 0.0);

                    noiseHeight += value * amplitude;
                    amplitude *= PERSISTENCE;
                    freq *= LACUNARITY;
                }

                if (noiseHeight > maxNoiseHeight) maxNoiseHeight = noiseHeight;
                else if (noiseHeight < minNoiseHeight) minNoiseHeight = noiseHeight;

                Tile newTile = new Tile(x, y, noiseHeight);
                terrain.setTile(newTile, x, y);
            }
        }

        double[][] squareGrid = generateSquareGradient(WIDTH, HEIGHT);
        TerrainType test = null;

        HashMap<TerrainType, Integer> terrainType = initTerrainMap();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                terrain.getTile(x, y).setElevation(inverseLerp(minNoiseHeight, maxNoiseHeight, terrain.getTile(x, y).getElevation()) - squareGrid[y][x]);
            }
        }


        FillBasins fillBasins = new FillBasins(terrain);
        terrain = fillBasins.calculateFlow();

        RiverGenerator riverGenerator = new RiverGenerator(terrain, random);
        terrain = fillBasins.filLBasins();
        Terrain riverTerrain = riverGenerator.generateRivers();


//        terrain = fillBasins.filLBasins();

        List<HashSet<Tile>> rivers = riverGenerator.getRivers();

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
            for (Tile tile : river) {
                image.setRGB(tile.x, tile.y, rgb);
            }
        }

        ImageIO.write(image, "png", new File("noise2.png"));


    }

    //Clamps range of the grid values between 0.0 and 1.0
    public static double inverseLerp(double a, double b, double v) {
        return (v-a)/(b-a);
    }

    //Generates a square gradient that when applied to the OpenSimplex algorithm creates island shapes
    public static double[][] generateSquareGradient(int width, int height) {
        double[][] squareGrid = new double[HEIGHT][WIDTH];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double xValue = Math.abs(x * 2f - width) / width;
                double yValue = Math.abs(y * 2f - height) / height;
                double value = Math.max(xValue, yValue);
                squareGrid[y][x] = value;
            }
        }

        return squareGrid;
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

    public void chartRivers() {

    }
}
