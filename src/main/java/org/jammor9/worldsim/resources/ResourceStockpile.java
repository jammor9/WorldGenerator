package org.jammor9.worldsim.resources;

public abstract class ResourceStockpile extends Resource {
    int stockpileSize;

    public ResourceStockpile(int stockpileSize) {
        this.stockpileSize = stockpileSize;
    }

    public void addToStockpile(int newStock) {
        this.stockpileSize += newStock;
    }

    public void removeFromStockpile(int usedStock) {
        this.stockpileSize -= usedStock;
    }

    public int getStockpileSize() {
        return this.stockpileSize;
    }
}
