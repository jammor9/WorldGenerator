package org.jammor9.worldsim.worldgenerator;

import java.util.Random;

/*
Erodes terrain by simulating rainfall onto an elevation map
Adapted from a python implemenation on https://gameidea.org/2023/12/22/simulating-hydraulic-erosion-of-terrain/
 */

public class Erosion {

    private final Random random;
    private final int drops;
    private Terrain terrain;
    private Node[][] elevationMap;
    private int height;
    private int width;

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
            if (velocity == null) velocity = new double[3];
            this.velocity = velocity;
        }

        public Terrain erode(Terrain terrain) {
            return erode(terrain, DEFAULT_ITERATIONS);
        }

        public Terrain erode(Terrain terrain, int maxIterations) {
            double origElev = terrain.getNode(oldX, oldY).getElevation();

            for (int i = 0; i < maxIterations; i++) {
                double[] normal = interpolatedNormal(x, y);
                if (normal[2] >= 0.9) break;

                double deposit = sediment * depositionRate * normal[2];
                double erosion = erosionRate * (1 - normal[2]) * Math.min(1, i * iterationScale);
                elevationMap[oldY][oldX].setElevation(elevationMap[oldY][oldX].getElevation() + (deposit - erosion));

                sediment += (erosion-deposit);

                velocity[0] = (friction * velocity[0] + normal[0] * speed);
                velocity[1] = (friction * velocity[1] + normal[1] * speed);

                oldX = x;
                oldY = y;

                x += (int) velocity[0];
                y += (int) velocity[1];

                if (elevationMap[y][x].getElevation() <= Math.pow(0.09, WorldGenImageSaver.getExponential())) break;
            }

            return terrain;
        }
    }

    public Erosion(Terrain terrain, Random random, int drops) {
        this.random = random;
        this.terrain = terrain;
        this.elevationMap = terrain.getHeightmap();
        this.width = terrain.getWidth();
        this.height = terrain.getHeight();
        this.drops = width * height;
    }

    public Terrain hydraulicErosion() {
        for (int i = 0; i < drops; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Node t = terrain.getNode(x, y);

            x = random.nextInt(width);
            y = random.nextInt(height);
            t = terrain.getNode(x, y);

            Drop drop = new Drop(random.nextInt(width), random.nextInt(height), null);
            drop.erode(terrain);
        }
        return terrain;
    }

    private double[] getNormal(int x, int y) {
        if (x == 0 || x >= width-1 || y == 0 || y >= height-1) return new double[]{0, 0, 1};

        double R = elevationMap[x+1][y].getElevation();
        double L = elevationMap[x-1][y].getElevation();
        double T = elevationMap[x][y+1].getElevation();
        double B = elevationMap[x][y-1].getElevation();

        double dx = (R - L) * 0.5;
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

    //Apply the gaussian blur after hydraulic erosion has finished, helps to handle unnatural peaks and cliffs in the landscape
    public Node[][] gaussianBlur() {

        double[][] kernel = new double[][] {
                new double[] {1/16.0, 2/16.0, 1/16.0},
                new double[] {2/16.0, 4/16.0, 2/16.0},
                new double[] {1/16.0, 2/16.0, 1/16.0}
        };

        int k = 3/2;

        for (int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                double sum = 0.0;
                for (int ky = -k; ky <= k; ky++) {
                    for (int kx = -k; kx <= k; kx++) {
                        int iy = Math.clamp(y+ky, 0, height-1);
                        int ix = Math.clamp(x+kx, 0, width-1);
                        sum +=  elevationMap[iy][ix].getElevation() * kernel[ky+k][kx+k];
                    }
                }
                elevationMap[y][x].setElevation(sum);
            }
        }

        return elevationMap;
    }

}
