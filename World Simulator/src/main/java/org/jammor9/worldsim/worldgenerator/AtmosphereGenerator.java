package org.jammor9.worldsim.worldgenerator;

public class AtmosphereGenerator {

    private static final int[] windDirection = new int[] {1, 0};     //Wind direction is west to east by default
    private static final int poleDirection = 1; //Pole is north by default, -1 is south
    private static final double BASE_PRECIPITATION_LEVEL = WorldGenImageSaver.getExponential(); //Default precipitation
    private static final double OROGRAPHIC_FACTOR = 0.5; //Determines how much precipitation increases with increasing altitude
    private static final double SHADOW_DECAY = 0.7; //Determines how much rain shadows influence precipitation decrease. Lower values = faster decay
    private static final double TEMPERATURE_STEP = 0.03; //Determines temperature decrease with altitude, lower value leads to higher temp drops
    private static final double FALLOFF_ELEVATION = 0.005; //Minimum elevation required for precipitation to change

    private Terrain terrain;
    private double poleStrength;

    public AtmosphereGenerator(Terrain terrain) {
        this.terrain = terrain;
        this.poleStrength = terrain.getHeight() / 12_800.0 ;
    }

    //Heavily modified this method from ChatGPT code
    public void calculatePrecipitation() {
        Node[][] heightmap = terrain.getHeightmap();

        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {

                if (heightmap[y][x].getElevation() < terrain.getOceanLevel()) {
                    heightmap[y][x].setPrecipitation(0);
                    continue;
                }

                //Get next tile over
                int upwindX = x - windDirection[0];
                int upwindY = y - windDirection[1];

                if (upwindX < 0 || upwindX >= terrain.getWidth() || upwindY < 0 || upwindY >= terrain.getHeight()) continue;
                if (heightmap[y][x].getElevation() < terrain.getOceanLevel()) continue;

                double prevPrecip = heightmap[upwindY][upwindX].getPrecipitation();
                double prevElev = heightmap[upwindY][upwindX].getPrecipitation();

                /*
                Calculates height difference between current elevation and the upwind elevation.
                If the elevation is greater, increase the precipitation by the orographic factor and the height difference
                Otherwise, reduce by the shadow decay
                 */

                //If tile is an onshore wind coast, set precipitation to base precipitation
                if (prevElev < terrain.getOceanLevel()) {
                    heightmap[y][x].setPrecipitation(BASE_PRECIPITATION_LEVEL);
                    continue;
                }

                //Current code seems to raise precipitation too quickly and lower too fast. Will need to look at tweaking values.
                if (upwindX < terrain.getWidth() && upwindY < terrain.getHeight()) {
                    double heightDifference = heightmap[y][x].getElevation() - heightmap[upwindY][upwindX].getElevation();

                    if (heightDifference > FALLOFF_ELEVATION) heightmap[y][x].setPrecipitation(BASE_PRECIPITATION_LEVEL + OROGRAPHIC_FACTOR + heightDifference);
                    else if (heightDifference <= FALLOFF_ELEVATION && heightDifference > -FALLOFF_ELEVATION  && prevPrecip <= 0.5) {
                        if (prevPrecip <= 0.1) heightmap[y][x].setPrecipitation(prevPrecip);
                        else heightmap[y][x].setPrecipitation(prevPrecip * 0.9);
                    }
                    else heightmap[y][x].setPrecipitation(prevPrecip * (SHADOW_DECAY-heightDifference));
                }
            }
        }
        gaussianBlur();
        gaussianBlur();
    }

    //Lowers the temperature by the current elevation divided by the temperature step
    public void calculateTemperature() {
        Node[][] heightmap = terrain.getHeightmap();

        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                double temp = heightmap[y][x].getTemperature();
                double elevChange = heightmap[y][x].getElevation()/TEMPERATURE_STEP;
                temp -= elevChange; //Calculate for elevation

                //Equation for distance from pole
                //(Height/2 - Distance from pole) * poleStrength
                double poleChange = (terrain.getHeight() / 2.0 - y) * poleStrength;

                if (poleDirection == 1) temp -= poleChange;
                else temp += poleChange;

                heightmap[y][x].setTemperature(temp);
            }
        }
    }

    //Blurs the precipitation map to avoid long lines and make it look more natural
    public Node[][] gaussianBlur() {

        Node[][] heightmap = terrain.getHeightmap();

        double[][] kernel = new double[][] {
                new double[] {1/16.0, 2/16.0, 1/16.0},
                new double[] {2/16.0, 4/16.0, 2/16.0},
                new double[] {1/16.0, 2/16.0, 1/16.0}
        };

        int k = 3/2;

        for (int y = 0; y < terrain.getHeight(); y++) {
            for(int x = 0; x < terrain.getWidth(); x++) {
                double sum = 0.0;
                for (int ky = -k; ky <= k; ky++) {
                    for (int kx = -k; kx <= k; kx++) {
                        int iy = Math.clamp(y+ky, 0, terrain.getHeight()-1);
                        int ix = Math.clamp(x+kx, 0, terrain.getWidth()-1);
                        sum +=  heightmap[iy][ix].getPrecipitation() * kernel[ky+k][kx+k];
                    }
                }

                if (heightmap[y][x].getElevation() > terrain.getOceanLevel()) heightmap[y][x].setPrecipitation(sum);
            }
        }

        return heightmap;
    }

}
