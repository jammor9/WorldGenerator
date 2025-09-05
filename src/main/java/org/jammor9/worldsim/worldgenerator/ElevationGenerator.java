package org.jammor9.worldsim.worldgenerator;

//Modified from Sebastian Lague's Procedural Landmass tutorials for Unity. Go check out his YouTube channel, was really useful for this.

import java.math.BigDecimal;

public class ElevationGenerator {
    /*
    FREQUENCY, AMPLITUDE, STARTING_NOISE_HEIGHT: Starting constants for OpenSimplex algorithm
    OCTAVES: How many times OpenSimplex is repeated, higher octaves create more detailed maps
    PERSISTENCE: Also known as gain, used to increase amplitude of frequency during each octave
    LACUNARITY: Increases frequency during each octave
    SCALE: How zoomed in the OpenSimplex algorithm is. For a 1024x1024 map 300 seems to work well. Larger maps require a bigger scale.
            Roughly speaking, if you double the size of the grid you will need to double the scale. Although it's best to play around.
     */
    private static final int FREQUENCY = 1;
    private static final int AMPLITUDE = 1;
    private static final int STARTING_NOISE_HEIGHT = 0;
    private static final int OCTAVES = 25; //How many times Simplex is repeated, higher octaves creates more detailed maps
    private static final double PERSISTENCE = 0.5;
    private static final double LACUNARITY = 2;
    private static final int SCALE = 150; //How zoomed in the OpenSimplex algorithm is.

    //Number of drops to simulate for hydralic erosion
    private double[][] elevationGrid;
    private int width;
    private int height;
    private long seed;
    private final double EXPONENTIAL;

    public ElevationGenerator(int width, int height, long seed, double exp) {
        this.seed = seed;
        this.elevationGrid = new double[height][width];
        this.width = width;
        this.height = height;
        this.EXPONENTIAL = exp;
    }

    //Uses OpenSimplex algorithm to randomly generate a heightmap
    //A square gradient moving inwards is then generated and applied to the heightmap, creating good island shapes
    public double[][] generateElevation() {
        double minNoiseHeight = Double.MAX_VALUE;
        double maxNoiseHeight = Double.MIN_VALUE;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double freq = FREQUENCY;
                double amp = AMPLITUDE;
                double noiseHeight = STARTING_NOISE_HEIGHT; //Always starts at 0

                for (int i = 0; i < OCTAVES; i++) {
                    double value = OpenSimplex2S.noise3_ImproveXY(seed, (double) x / SCALE * freq, (double) y / SCALE * freq, 0.0);
//                    double value = ridgeNoise(seed, freq, x, y);
                    noiseHeight += value * amp;
                    amp *= PERSISTENCE;
                    freq *= LACUNARITY;
                }

                if (noiseHeight > maxNoiseHeight) maxNoiseHeight = noiseHeight;
                else if (noiseHeight < minNoiseHeight) minNoiseHeight = noiseHeight;

                elevationGrid[y][x] = noiseHeight;
            }
        }

        double[][] falloff = generateCircleGradient();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                elevationGrid[y][x] = Math.clamp(inverseLerp(minNoiseHeight, maxNoiseHeight, elevationGrid[y][x]) - falloff[y][x], 0.0, 1.0);
                elevationGrid[y][x] = Math.pow(elevationGrid[y][x], EXPONENTIAL);
                if (elevationGrid[y][x] < Math.pow(0.01, EXPONENTIAL)) elevationGrid[y][x] = Math.pow(0.01, EXPONENTIAL);
                int temp = (int) (elevationGrid[y][x] * 1000);
                elevationGrid[y][x] = (double) temp / 1000.0;
            }
        }

        return elevationGrid;
    }

    private double[][] generateSquareGradient() {
        double[][] squareGrid = new double[height][width];
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

    private double[][] generateCircleGradient() {
        int centreX = width/2-1;
        int centreY = height/2-1;

        double[][] circleGrid = new double[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double distanceX = Math.pow(centreX-x, 2);
                double distanceY = Math.pow(centreY-y, 2);

                double distanceToCenter = Math.sqrt(distanceX + distanceY);

                distanceToCenter /= height*0.6;
                circleGrid[y][x] = distanceToCenter;
            }
        }

        return circleGrid;
    }

    //Clamps range of the grid values between 0.0 and 1.0
    private static double inverseLerp(double a, double b, double v) {
        return (v-a)/(b-a);
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static double ridgeNoise(long seed, double freq, int x, int y) {
        return 2 * (0.5 - Math.abs(0.5 - OpenSimplex2S.noise3_ImproveXY(seed, (double) x / SCALE * freq, (double) y / SCALE * freq, 0.0)));
    }
}
