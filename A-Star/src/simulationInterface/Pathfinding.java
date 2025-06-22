package simulationInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//Following Sebastian Lague's tutorial on A* algorithms. Adapted from C# to Java

public class Pathfinding{

    private ArrayList<TileState> openSet = new ArrayList<>();
    private HashSet<TileState> closedSet = new HashSet<>();

    public void findPath(GridPanel gridPanel, TileState start, TileState end) {
        openSet.add(start);

        while (openSet.size() > 0) {
            TileState currentTile = openSet.get(0);
            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).fCost() < currentTile.fCost() || openSet.get(i).fCost() == currentTile.fCost()){
                    if (openSet.get(i).hCost < currentTile.hCost )  currentTile = openSet.get(i);
                }
            }

            openSet.remove(currentTile);
            closedSet.add(currentTile);

            if (currentTile == end) {
                retracePath(gridPanel, start, end);
                return;
            }

            for (TileState neighbour : currentTile.getNeighbours()) {
                if (neighbour.getTileType() == TileType.CLOSED || closedSet.contains(neighbour)) continue;
                
                int newMovementCostToNeighbour = currentTile.gCost + getDistance(currentTile, neighbour);
                if (newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
                    neighbour.gCost = newMovementCostToNeighbour;
                    neighbour.hCost = getDistance(neighbour, end);
                    neighbour.parent = currentTile;

                    if (!openSet.contains(neighbour)) openSet.add(neighbour);
                }
            }
        }
    }

    public int getDistance(TileState a, TileState b) {
        int dstX = Math.abs(a.x - b.x);
        int dstY = Math.abs(a.y - b.y);

        if (dstX > dstY) return 14 * dstY + 10 * (dstX - dstY);
        else return 14 * dstX + 10 * (dstY - dstX);
    }

    public void retracePath(GridPanel gridPanel, TileState start, TileState end) {
        List<TileState> path = new ArrayList<>();
        TileState currentTile = end;

        while (currentTile != start) {
            path.add(currentTile);
            currentTile = currentTile.parent;
        }

        path = path.reversed();

        for (TileState tile : path) {
            gridPanel.setCell(TileType.PATH, tile.x, tile.y);
        }

        gridPanel.update();
        openSet.clear();
        closedSet.clear();
    }
} 
