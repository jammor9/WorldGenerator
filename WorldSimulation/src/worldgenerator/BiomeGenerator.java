package worldgenerator;

//Climate mapping based off Artifexian's climate series on youtube
public class BiomeGenerator {

    private Terrain terrain;

    public BiomeGenerator(Terrain terrain) {
        this.terrain = terrain;
    }

    //Yeah this isn't realistic at all, but it's good enough for what the program needs to do
    public void generateBiomes() {
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Node n = terrain.getNode(x, y);
                if (n.getElevation() < terrain.getOceanLevel()) {
                    n.setBiome(Biome.OCEAN);
                    continue;
                }

                double temp = n.getTemperature();
                double precip = n.getPrecipitation();
                double elev = n.getElevation();

                //Trying to mimic the effect of the tropics
                if (temp >= 35) {
                    if (temp < 37) {
                        n.setBiome(Biome.HOT_STEPPE);
                        n.setTemperature(temp+5);
                    }
                    else {
                        n.setBiome(Biome.SAVANNAH);
                        if (precip > 1) {
                            n.setBiome(Biome.TROPICAL);
                            n.setPrecipitation(precip * 1.5);
                        }
                        n.setTemperature(temp-5);
                    }
                }
                else if (temp < 35 && temp >= 30) {
                    n.setBiome(Biome.HOT_DESERT);
                    n.setTemperature(temp+10);
                    n.setPrecipitation(precip*0.2);
                }
                else if (temp < 30 && temp >= 28) {
                    n.setBiome(Biome.HOT_STEPPE);
                    n.setTemperature(temp+5);
                    n.setPrecipitation(precip*0.5);
                }
                //Cold deserts occur at higher elevations than hotter deserts, higher precipitation leads to forest growth
                else if (temp < 28 && temp >= 10) {
                    n.setBiome(Biome.HUMID_CONTINENTAL);
                    if (precip > 1.2) n.setBiome(Biome.TEMPERATE_FOREST);
                    else if (precip <= 0.2 && elev >= 0.3) n.setBiome(Biome.COLD_DESERT);
                    else if (precip <= 0.3 && elev >= 0.25) n.setBiome(Biome.COLD_STEPPE);
                }
                else if (temp < 10 && temp >= 0) {
                    n.setBiome(Biome.SUBARCTIC_CONTINENTAL);
                    if (precip > 1.2) n.setBiome(Biome.BOREAL_FOREST);
                }
                //Below 0 and the land becomes tundra
                else {
                    n.setBiome(Biome.TUNDRA);
                }
            }
        }
    }

}
