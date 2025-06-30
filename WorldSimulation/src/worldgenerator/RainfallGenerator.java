package worldgenerator;

public class RainfallGenerator {

    //Wind direction is west to east by default
    private static final int[] windDirection = new int[] {1, 0};
    private static final double BASE_PRECIPITATION_LEVEL = 0.5;
    private static final double OROGRAPHIC_FACTOR = 1;
    private static final double SHADOW_DECAY = 0.6;

    private Terrain terrain;

    public RainfallGenerator(Terrain terrain) {
        this.terrain = terrain;
    }

    //Modified from ChatGPT code
    public void calculatePrecipitation() {
        Node[][] heightmap = terrain.getTerrainGrid();

        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {

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
                    if (heightDifference > 0) heightmap[y][x].setPrecipitation(BASE_PRECIPITATION_LEVEL + OROGRAPHIC_FACTOR + heightDifference);
                    else heightmap[y][x].setPrecipitation(prevPrecip * SHADOW_DECAY);
                }


            }
        }
    }

}
