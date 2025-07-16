package org.jammor9.worldsim;

import org.jammor9.worldsim.resources.*;

import java.util.HashMap;
import java.util.Random;

enum Metals {
    IRON,
    COPPER,
    TIN,
    SILVER,
    GOLD,
    ADAMANTINE
}

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

    //Deposit sizes
    private static final int SMALL_DEPOSIT = 2000;
    private static final int MEDIUM_DEPOSIT = 4000;
    private static final int LARGE_DEPOSIT = 6000;

    //Chances for certain resources to spawn
    private static final float MAGIC_SPAWN_CHANCE = 0.05F;
    private static final float WHALE_SPAWN_CHANCE = 0.05F;
    private static final float COAL_SPAWN_CHANCE = 0.2F;
    private static final float IRON_SPAWN_CHANCE = 0.2F;
    private static final float COPPER_SPAWN_CHANCE = 0.15F;
    private static final float TIN_SPAWN_CHANCE = 0.1F;
    private static final float SILVER_SPAWN_CHANCE = 0.07F;
    private static final float GOLD_SPAWN_CHANCE = 0.05F;
    private static final float ADAMANTINE_SPAWN_CHANCE = 0.25F;

    //Requirements for certain resources to spawn
    private static final float MIN_COAL_PRECIP = 0.5F;
    private static final float MIN_CLAY_PRECIP = 0.8F;
    private static final int MIN_ADAMANTINE_MAGIC = 25;
    private final double MIN_METALS_ELEVATION;
    private final double MAX_COAL_ELEVATION;

    private WorldMap worldMap;
    private Random rng;

    public ResourceGenerator(WorldMap worldMap, Random rng) {
        this.worldMap = worldMap;
        this.rng = rng;
        this.MIN_METALS_ELEVATION = worldMap.getHillLevel();
        this.MAX_COAL_ELEVATION = worldMap.getMountainLevel();
    }

    public void generateResources() {
        for (int y = 0; y < worldMap.getHeight(); y++) {
            for (int x = 0; x < worldMap.getWidth(); x++) {
                WorldTile t = worldMap.getTile(x, y);
                if (t.getClimate() == Climate.OCEAN) continue;
                calculateSoilFertility(t);
                calculateMagic(t);
                generateOrganicDeposits(t);
                generateMetalDeposits(t);
                generateNonOrganicDeposits(t);
            }
        }
    }

    //Calculates how fertile a specific tile is. Uses climate as a basis, then applies river size and precipitation as modifiers to increase fertility
    private void calculateSoilFertility(WorldTile t) {
        double precip = t.getPrecipitation();
        int riverSize = t.getRiverSize();
        int fertility = biomeFertility.get(t.getClimate()) + (riverSize * 10) + (int) (precip * 10);
        t.setFertility(fertility);
    }

    //Randomly spreads magic tiles throughout the map
    private void calculateMagic(WorldTile t) {
        float p = rng.nextFloat();
        if (p <= MAGIC_SPAWN_CHANCE) t.setMagic(rng.nextInt(101));
    }

    //Generates forests, fish, and whales
    private void generateOrganicDeposits(WorldTile t) {
        Climate c = t.getClimate();
        int riverSize = t.getRiverSize();

        switch(c) {
            case BOREAL_FOREST, WOODLAND -> t.addOrganicDeposit(new Wood(SMALL_DEPOSIT));
            case SEASONAL_FOREST -> t.addOrganicDeposit(new Wood(MEDIUM_DEPOSIT));
            case TEMPERATE_RAINFOREST, TROPICAL -> t.addOrganicDeposit(new Wood(LARGE_DEPOSIT));
        }

        if (t.isCoastal()) {
            float p = rng.nextFloat();
            if (p <= WHALE_SPAWN_CHANCE) t.addOrganicDeposit(new Whales(SMALL_DEPOSIT));
            else t.addOrganicDeposit(new Fish(LARGE_DEPOSIT));
        }
        else if (riverSize >= 1 && riverSize <= 4) t.addOrganicDeposit(new Fish(SMALL_DEPOSIT));
        else if (riverSize > 4 && riverSize <= 8) t.addOrganicDeposit(new Fish(MEDIUM_DEPOSIT));
        else if (riverSize > 8) t.addOrganicDeposit(new Fish(LARGE_DEPOSIT));
    }

    /*
    Generates the metal deposit at a specific tile
    First checks if the tile is above the minimum elevation for metals to spawn, if not then returns with no metals at that tile
    Then it calculates the chance for a resource to spawn, the size of the deposit, and whether it is a deep deposit
    A random metal is then selected from the Metals enumerator
    The random metal is compared against the spawn chance for that metal, and if it passes then is added as a metal deposit to the tile
     */
    private void generateMetalDeposits(WorldTile t) {
        double elev = t.getElevation();
        if (elev < MIN_METALS_ELEVATION) return;
        float spawnChance = rng.nextFloat();
        int depositSize = getRandomDepositSize();
        boolean deep = rng.nextBoolean();
        Metals randomMetal = Metals.values()[rng.nextInt(Metals.values().length)];
        switch(randomMetal) {
            case IRON: if (spawnChance <= IRON_SPAWN_CHANCE) t.addMetalDeposit(new Iron(depositSize, deep));
            case COPPER: if (spawnChance <= COPPER_SPAWN_CHANCE) t.addMetalDeposit(new Copper(depositSize, deep));
            case TIN: if (spawnChance <= TIN_SPAWN_CHANCE) t.addMetalDeposit(new Tin(depositSize, deep));
            case SILVER: if (spawnChance <= SILVER_SPAWN_CHANCE) t.addMetalDeposit(new Silver(depositSize, deep));
            case GOLD: if (spawnChance <= GOLD_SPAWN_CHANCE) t.addMetalDeposit(new Gold(depositSize, deep));
            case ADAMANTINE: if (t.getMagic() >= MIN_ADAMANTINE_MAGIC && spawnChance <= ADAMANTINE_SPAWN_CHANCE) t.addMetalDeposit(new Adamantine(depositSize, deep));
        }
    }

    private void generateNonOrganicDeposits(WorldTile t) {
        double elev = t.getElevation();
        double precip = t.getPrecipitation();
        int riverSize = t.getRiverSize();
        float coalSpawnChance = rng.nextFloat();

        //Spawn coal if precipitation is high enough and elevation is low enough
        if (elev <= MAX_COAL_ELEVATION && precip > MIN_COAL_PRECIP && coalSpawnChance <= COAL_SPAWN_CHANCE) t.addNonOrganicDeposits(new Coal(getRandomDepositSize(), rng.nextBoolean()));

        //Spawn stone in any region above a certain elevation
        if (elev >= MIN_METALS_ELEVATION) t.addNonOrganicDeposits(new Stone(getRandomDepositSize(), false));

        //Spawn clay in any region of high precipitation, or near a river
        if (riverSize >= 1 || precip >= MIN_CLAY_PRECIP) t.addNonOrganicDeposits(new Clay(getRandomDepositSize(), false));
    }

    private int getRandomDepositSize() {
        int r = rng.nextInt(3);
        if (r == 0) return SMALL_DEPOSIT;
        else if (r == 1) return MEDIUM_DEPOSIT;
        else return LARGE_DEPOSIT;
    }
}
