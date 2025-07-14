package org.jammor9.worldsim;

import java.util.HashMap;

public class ResourceGenerator {

    private static final HashMap<Climate, Integer> biomeFertility = new HashMap<>() {{
        put(Climate.TROPICAL, 30);
        put(Climate.OCEAN, 0);
        put(Climate.SAVANNAH, 25);
        put(Climate.TEMPERATE_FOREST, 50);
        put(Climate.SEASONAL_FOREST, 50);
        put(Climate.BOREAL_FOREST, 40);
        put(Climate.TUNDRA, 10);
        put(Climate.ICE, 0);
        put(Climate.GRASSLAND, 55);
        put(Climate.TEMPERATE_RAINFOREST, 55);
        put(Climate.WOODLAND, 50);
        put(Climate.DESERT, 0);
    }
    };

    private WorldMap worldMap;

    public ResourceGenerator(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    //Calculates how fertile a specific tile is. Uses climate as a basis, then applies river size and precipitation as modifiers to increase fertility
    public void calculateSoilFertility() {
        for (int y = 0; y < worldMap.getHeight(); y++) {
            for (int x = 0; x < worldMap.getWidth(); x++) {
                WorldTile t = worldMap.getTile(x, y);
                double precip = t.getPrecipitation();
                int riverSize = t.getRiverSize();
                int fertility = biomeFertility.get(t.getClimate()) + (riverSize * 10) + (int) (precip * 10);
                t.setFertility(fertility);
            }
        }
    }

    public void generateResources() {

    }
}
