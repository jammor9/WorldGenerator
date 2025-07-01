package worldgenerator;

public class AtmosphereGenerator {

    private static final int[] windDirection = new int[] {1, 0};     //Wind direction is west to east by default
    private static final double BASE_PRECIPITATION_LEVEL = 0.5;
    private static final double OROGRAPHIC_FACTOR = 1;
    private static final double SHADOW_DECAY = 0.9;
    private static final double TEMPERATURE_STEP = 0.035;

    private Terrain terrain;

    public AtmosphereGenerator(Terrain terrain) {
        this.terrain = terrain;
    }

    //Modified this method from ChatGPT code
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
                if (heightmap[y][x].getElevation() < 0.09) continue;

                double prevPrecip = heightmap[upwindY][upwindX].getPrecipitation();

                /*
                Calculates height difference between current elevation and the upwind elevation.
                If the elevation is greater, increase the precipitation by the orographic factor and the height difference
                Otherwise, reduce by the shadow decay
                 */

                //Current code seems to raise precipitation too quickly and lower too fast. Will need to look at tweaking values.
                if (upwindX < terrain.getWidth() && upwindY < terrain.getHeight()) {
                    double heightDifference = heightmap[y][x].getElevation() - heightmap[upwindY][upwindX].getElevation();

                    if (heightDifference > 0.01) heightmap[y][x].setPrecipitation(BASE_PRECIPITATION_LEVEL + OROGRAPHIC_FACTOR + heightDifference);
                    else if (heightDifference <= 0.01 && heightDifference > 0 ) {

                        if (prevPrecip == 0) heightmap[y][x].setPrecipitation(BASE_PRECIPITATION_LEVEL);
                        else heightmap[y][x].setPrecipitation(prevPrecip);
                    }
                    else heightmap[y][x].setPrecipitation(prevPrecip * SHADOW_DECAY);
                }

//                System.out.println(prevPrecip);


            }
        }
        gaussianBlur();
    }

    //Lowers the temperature by the current elevation divided by the temperature step
    //Currently only dependant on elevation, will add a pole later for increased variance in temperature to allow for more biomes
    public void calculateTemperature() {
        Node[][] heightmap = terrain.getHeightmap();

        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                if(heightmap[y][x].getElevation() < terrain.getOceanLevel()) heightmap[y][x].setTemperature(-500);

                double temp = heightmap[y][x].getTemperature();
                double change = heightmap[y][x].getElevation()/TEMPERATURE_STEP;
                heightmap[y][x].setTemperature(temp - change);
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
