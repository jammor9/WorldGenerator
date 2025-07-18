package org.jammor9.worldsim;

import java.util.ArrayList;

public class Province {
    private ArrayList<WorldTile> provinceTiles = new ArrayList<>();
    private int regionColor;

    public Province(int regionColor) {
        this.regionColor = regionColor;
    }

    public void addTile(WorldTile t) {
        provinceTiles.add(t);
    }

    public void removeTile(WorldTile t) {
        provinceTiles.remove(t);
    }

    public ArrayList<WorldTile> getProvinceTiles() {
        return this.provinceTiles;
    }

    public int getColor() {
        return this.regionColor;
    }

}
