package org.jammor9.worldsim;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveWorld {

    private final WorldMap worldMap;
    private final int height;
    private final int width;
    private Gson gson;

    public SaveWorld(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.height = worldMap.getHeight();
        this.width = worldMap.getWidth();
        this.gson = new Gson();
    }

    public void saveWorld(String filename) {
        String path = filename + ".json";
        File file = new File(path);


        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter w = new FileWriter(path);
            gson.toJson(worldMap, w);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
