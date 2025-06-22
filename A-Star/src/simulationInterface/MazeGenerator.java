package simulationInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

//Credit to https://www.youtube.com/watch?v=cQVH4gcb3O4

public class MazeGenerator {
    private TileState origin;
    private List<TileState> frontierCells = new ArrayList<>();
    private Random rng;

    public MazeGenerator(GridPanel gridPanel, int gridWidth, int gridHeight) {
        int origY = (int)(Math.random() * gridHeight);
        int origX = (int)(Math.random() * gridWidth);
        this.origin = gridPanel.getCell(origX, origY);
        this.rng = new Random();
    }

    public void generateMaze(GridPanel gridPanel) {
        blockGrid(gridPanel);
        findFrontier(gridPanel, origin);

        while (!frontierCells.isEmpty()) {
            TileState targetTile = frontierCells.get(rng.nextInt(frontierCells.size()));
            targetTile.setTileType(TileType.OPEN);
            if (targetTile.x == targetTile.mazeLink.x) {
                if (targetTile.y > targetTile.mazeLink.y) gridPanel.getCell(targetTile.x, targetTile.y-1).setTileType(TileType.OPEN);
                else gridPanel.getCell(targetTile.x, targetTile.y+1).setTileType(TileType.OPEN);
            }
            else {
                if (targetTile.x > targetTile.mazeLink.x) gridPanel.getCell(targetTile.x-1, targetTile.y).setTileType(TileType.OPEN);
                else gridPanel.getCell(targetTile.x+1, targetTile.y).setTileType(TileType.OPEN);
            }
            findFrontier(gridPanel, targetTile);
            frontierCells.remove(targetTile);
            System.out.println();
        }
    }

    private void blockGrid(GridPanel gridPanel) {
        gridPanel.setGridBlock(TileType.CLOSED);
        gridPanel.update();
    }

    private void findFrontier(GridPanel gridPanel, TileState searchTile) {
        for (int x = searchTile.x-2; x <= searchTile.x+2; x+=4) {
            TileState frontierTile = gridPanel.getCell(x, searchTile.y);
            if (frontierTile != null && frontierTile.getTileType() == TileType.CLOSED) {
                frontierTile.mazeLink = searchTile;
                frontierCells.add(frontierTile);
            }
        }
        for (int y = searchTile.y-2; y <= searchTile.y+2; y+=4) {
            TileState frontierTile = gridPanel.getCell(searchTile.x, y);
            if (frontierTile != null && frontierTile.getTileType() == TileType.CLOSED) {
                frontierTile.mazeLink = searchTile;
                frontierCells.add(frontierTile);
            }
        }
    }
}
