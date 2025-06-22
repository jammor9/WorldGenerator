package simulationInterface;

import java.util.ArrayList;

public class TileState {
    

    private TileType tileType;
    private GridPanel gridPanel;

    public int gCost;
    public int hCost;
    public int x;
    public int y;
    public TileState parent;
    public TileState mazeLink;
    public boolean origin;

    public TileState(GridPanel gridPanel, int x, int y, TileType tileType) {
        this.gridPanel = gridPanel;
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }

    public int[] getCoord() {
        return new int[] {y, x};
    }

    public TileType getTileType() {
        return tileType;
    }

    public TileType setTileType(TileType tileType) {
        return this.tileType = tileType;
    }

    @Override
    public String toString() {
        return "Coords: [x:" + x + ", y:" + y + "] Type: " + tileType;
    }

    public int fCost() {
        return gCost + hCost;
    }

    public ArrayList<TileState> getNeighbours() {
        ArrayList<TileState> neighbours = new ArrayList<>();

        for (int ix = -1; ix <= 1; ix++){
            for (int iy = -1; iy <= 1; iy++) {
                if (ix==0 & iy==0) continue;

                int checkX = x + ix;
                int checkY = y + iy;
                TileState neighbour = gridPanel.getCell(checkX, checkY);

                if (neighbour != null) neighbours.add(neighbour);
            }
        }
        return neighbours;
    }

}
