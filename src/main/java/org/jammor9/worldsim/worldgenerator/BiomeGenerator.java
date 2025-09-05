package org.jammor9.worldsim.worldgenerator;

public class BiomeGenerator {

    private Terrain terrain;

    public BiomeGenerator(Terrain terrain) {
        this.terrain = terrain;
    }

    //Based off biome map from https://www.jgallant.com/procedurally-generating-wrapping-world-maps-in-unity-csharp-part-4/
    public void generateBiomes() {
        for (int y = 0; y < terrain.getHeight(); y++) {
            for (int x = 0; x < terrain.getWidth(); x++) {
                Node n = terrain.getNode(x, y);
                double precip = n.getPrecipitation();
                double elev = n.getElevation();
                double temp = n.getTemperature();

                if (elev < terrain.getOceanLevel()) {
                    n.setBiome(Biome.OCEAN);
                    continue;
                }

                if (temp <= -10) {
                    n.setBiome(Biome.ICE);
                }
                else if (temp > -10 && temp <= 0) {
                    n.setBiome(Biome.TUNDRA);
                }
                else if (temp > 0 && temp <= 10) {
                    if (precip < 0.4) n.setBiome(Biome.GRASSLAND);
                    else if (precip >= 0.4 && precip < 0.6) n.setBiome(Biome.WOODLAND);
                    else if (precip >= 0.6) n.setBiome(Biome.BOREAL_FOREST);
                }
                else if (temp > 10 && temp <= 28) {
                    if (precip < 0.1) {
                        if (elev > Math.pow(0.3, WorldGenImageSaver.getExponential())) n.setBiome(Biome.DESERT);
                        else n.setBiome(Biome.GRASSLAND);
                    }
                    else if (precip >= 0.1 && precip < 0.6) n.setBiome(Biome.WOODLAND);
                    else if (precip >= 0.6 && precip < 1.0) n.setBiome(Biome.SEASONAL_FOREST);
                    else if (precip >= 1.0) n.setBiome(Biome.TEMPERATE_RAINFOREST);
                }
                else if (temp > 28) {
                    if (precip < 0.2) n.setBiome(Biome.DESERT);
                    else if (precip >= 0.2 && precip < 0.8) n.setBiome(Biome.SAVANNAH);
                    else if (precip >= 0.8) n.setBiome(Biome.TROPICAL);
                }
            }
        }
    }

}
