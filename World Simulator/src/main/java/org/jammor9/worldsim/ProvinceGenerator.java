package org.jammor9.worldsim;

//Logic taken from https://gamedev.stackexchange.com/questions/101130/algorithm-for-dividing-a-2d-grid-into-organic-looking-plates

import java.util.Random;

public class ProvinceGenerator {
    private WorldMap worldMap;
    private Random worldRng;
    private Random gameRng;
    private final int plateNum;

    private int[][] grid;
    private int width;
    private int height;

    public ProvinceGenerator(WorldMap worldMap, Random worldRng, Random gameRng) {
        this.worldMap = worldMap;
        this.worldRng = worldRng;
        this.gameRng = gameRng;
        this.plateNum = worldMap.getHeight() + worldMap.getWidth();
        this.width = worldMap.getWidth();
        this.height = worldMap.getHeight();
        this.grid = new int[height][width];
    }

    private void fill() {
        boolean[][] claimed = new boolean[height][width];
        int[] cells = new int[width * height - plateNum];
        int numEmptyCells = 0;

        for (int plate = 1; plate <= plateNum; plate++) {
            int x = worldRng.nextInt(width);
            int y = worldRng.nextInt(height);

            while (grid[y][x] != 0) {
                x = worldRng.nextInt(width);
                y = worldRng.nextInt(height);
            }

            numEmptyCells = setCell(x, y, plate, cells, numEmptyCells, claimed);
            claimed[y][x] = true;
        }

        while (numEmptyCells > 0) {
            int idx = worldRng.nextInt(numEmptyCells);
            int cell = cells[idx];
            cells[idx] = cells[--numEmptyCells];

            int x = cell % width;
            int y = cell / width;
            int plate = selectPlate(cells, x, y);

            numEmptyCells = setCell(x, y, plate, cells, numEmptyCells, claimed);
        }

    }

    private int setCell(int x, int y, int plate, int[] cells, int cellsIdx, boolean[][] claimed) {
        assert plate > 0 && plate <= plateNum;

        grid[y][x] = plate;

        int left = (x == 0 ? width - 1 : x - 1);
        int right = (x == width - 1 ? 0 : x + 1);
        int up = (y == 0 ? height - 1 : y - 1);
        int down = (y == height - 1 ? 0 : y + 1);

        if(!claimed[y][left]) {
            cells[cellsIdx++] = left + y * width;
            claimed[y][left] = true;
        }
        if(!claimed[up][x]) {
            cells[cellsIdx++] = x + up * width;
            claimed[up][x] = true;
        }
        if(!claimed[down][x]) {
                cells[cellsIdx++] = x + down * width;
                claimed[down][x] = true;
        }

        return cellsIdx;
    }

    private int selectPlate(int[] cells, int x, int y) {
        int left = (x == 0 ? width - 1: x - 1);
        int right = (x == width - 1 ? 0 : x + 1);
        int up = (y == 0 ? height - 1 : y - 1);
        int down = (y == height - 1 ? 0 : y + 1);

        final int plates[] = new int[4];
        int count = 0;

        if(grid[y][left] != 0) plates[count++] = grid[y][left];
        if(grid[y][right] != 0) plates[count++] = grid[y][right];
        if(grid[up][x] != 0) plates[count++] = grid[up][x];
        if(grid[down][x] != 0) plates[count++] = grid[down][x];

        return plates[worldRng.nextInt(count)];
    }

    private void claimOcean(boolean[][] claimed) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(worldMap.getTile(x, y).getClimate() == Climate.OCEAN) claimed[y][x] = true;
            }
        }
    }

    public Province[] generateRegions() {
        fill();
        int[] colors = new int[plateNum];
        Province[] provinces = new Province[plateNum];
        for (int i = 0; i < colors.length; i++) colors[i] = worldRng.nextInt(0xFFFFFF);

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                if (grid[y][x] > 0) {
                    if (worldMap.getTile(x, y).getClimate() == Climate.OCEAN) continue;
                    if (provinces[grid[y][x]-1] == null) provinces[grid[y][x]-1] = new Province(colors[grid[y][x]-1], gameRng);
                    provinces[grid[y][x]-1].addTile(worldMap.getTile(x, y));
                }
            }
        }

        return provinces;

    }

}
