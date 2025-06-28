package worldgenerator;

import java.util.Random;

//Modified from Sebastian Lague's Procedural Landmass tutorials for Unity. Go check out his YouTube channel, was really useful for this.
//Hydraulic Erosion and helper methods taken from https://gameidea.org/2023/12/22/simulating-hydraulic-erosion-of-terrain/

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
    private static final int SCALE = 150; //How zoomed in the OpenSimplex algorithm is. ~250-350 seems to provide best landforms

    //Number of drops to simulate for hydralic erosion
    private final int DROPS;

    private double[][] elevationGrid;
    private int width;
    private int height;
    private static long seed;

    private class Drop {
        double[] velocity;
        int oldX;
        int oldY;
        int x;
        int y;
        double sediment;

        static final double depositionRate = 0.021;
        static final double erosionRate = 0.051;
        static final double iterationScale = 0.01;
        static final double friction = 0.01;
        static final double speed = 0.9910;

        static final int DEFAULT_ITERATIONS = 8;

        public Drop(int x, int y, double[] velocity) {
            this.oldX = 0;
            this.oldY = 0;
            this.x = x;
            this.y = y;
            if (velocity == null) velocity = new double[2];
            this.velocity = velocity;
        }

        public double[][] erode(double[][] elevationMap) {
            return erode(elevationMap, DEFAULT_ITERATIONS);
        }

        public double[][] erode(double[][] elevationMap, int maxIterations) {
            for (int i = 0; i < maxIterations; i++) {
                double[] normal = interpolatedNormal(x, y);
                if (normal[2] >= 0.1) break;

                double deposit = sediment * depositionRate * normal[2];
                double erosion = erosionRate * (1 - normal[2] * Math.min(1, i * iterationScale));
                elevationMap[oldY][oldX] += (deposit - erosion);

                sediment += (erosion-deposit);

                velocity[0] = (friction * velocity[0] + normal[0] * speed);
                velocity[1] = (friction * velocity[1] + normal[1] * speed);

                oldX = x;
                oldY = y;

                x += velocity[0];
                y += velocity[1];
            }

            return elevationMap;
        }
    }

    public ElevationGenerator(int width, int height, long seed) {
        this.seed = seed;
        this.elevationGrid = new double[height][width];
        this.width = width;
        this.height = height;
        this.DROPS = width * 100;
    }

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
                    noiseHeight += value * amp;
                    amp *= PERSISTENCE;
                    freq *= LACUNARITY;
                }

                if (noiseHeight > maxNoiseHeight) maxNoiseHeight = noiseHeight;
                else if (noiseHeight < minNoiseHeight) minNoiseHeight = noiseHeight;

                elevationGrid[y][x] = noiseHeight;
            }
        }

        double[][] squareGrid = generateSquareGradient();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                elevationGrid[y][x] = inverseLerp(minNoiseHeight, maxNoiseHeight, elevationGrid[y][x]) - squareGrid[y][x];
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

    //Clamps range of the grid values between 0.0 and 1.0
    private static double inverseLerp(double a, double b, double v) {
        return (v-a)/(b-a);
    }

    public double[][] hydraulicErosion(double[][] elevationGrid) {
        Random random = new Random(seed);

        for (int i = 0; i < DROPS; i++) {
            Drop drop = new Drop(random.nextInt(width), random.nextInt(height), null);
            elevationGrid = drop.erode(elevationGrid);
        }
        return elevationGrid;
    }

    private double[] getNormal(int x, int y) {
        if (x == 0 || x >= width-1 || y == 0 || y >= height-1) return new double[]{0, 0, 1};

        double R = elevationGrid[x+1][y];
        double L = elevationGrid[x-1][y];
        double T = elevationGrid[x][y+1];
        double B = elevationGrid[x][y-1];

        double dx = (R-L) * 0.5;
        double dy = (B - T) * 0.5;
        double dz = -1.0;

        double[] normal = new double[]{dx, dy, dz};
        double normalDivisor = Math.sqrt(Math.pow(normal[0], 2) + Math.pow(normal[1], 2) + Math.pow(normal[2], 2));

        normal[0] /= normalDivisor;
        normal[1] /= normalDivisor;
        normal[2] /= normalDivisor;

        return normal;
    }

    private double[] interpolatedNormal(int x, int y) {
        if (x == 0 || x >= width-1 || y == 0 || y >= height-1) return new double[]{0, 0, 1};

        double[] normal = new double[3];

        for (int ix = x-1; ix<=x+1; ix++) {
            for (int iy = y-1; iy<=y+1; iy++) {
                if (iy == y && ix == x) continue;
                double[] n = getNormal(ix, iy);
                normal[0] += n[0];
                normal[1] += n[1];
                normal[2] += n[2];
            }
        }

        normal[0] /= 8;
        normal[1] /= 8;
        normal[2] /= 8;

        return normal;
    }

    private double[][] guassianBlur(double[][] elevationMap) {
        return null;
    }
}
