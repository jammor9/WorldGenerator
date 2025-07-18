package org.jammor9.worldsim;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jammor9.worldsim.resources.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageGenerator {

    private int height;
    private int width;
    private WorldMap worldMap;

    public ImageGenerator(int width, int height, WorldMap worldMap) {
        this.height = height;
        this.width = width;
        this.worldMap = worldMap;
    }

    public Image generateElevationImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x= 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                double v = t.getElevation();
                double EXPONENTIAL = WorldGenRunnable.getExponential();
                double OCEAN_LEVEL = worldMap.getOceanLevel();
                int rgb;
                if (v <= Math.pow(.02, EXPONENTIAL)) rgb = new Color(15, 76, 209).getRGB();
                else if (v > Math.pow(.02, EXPONENTIAL) && v <= OCEAN_LEVEL) rgb = new Color(28, 135, 235).getRGB();
                else if (v >  OCEAN_LEVEL && v <= Math.pow(.13, EXPONENTIAL)) rgb = new Color(145, 230, 124).getRGB();
                else if (v > Math.pow(.13, EXPONENTIAL) && v <= Math.pow(.19, EXPONENTIAL)) rgb = new Color(91, 156, 75).getRGB();
                else if (v > Math.pow(.1, EXPONENTIAL) && v <= Math.pow(.30, EXPONENTIAL)) rgb = new Color(75, 133, 61).getRGB();
                else if (v > Math.pow(.30, EXPONENTIAL) && v <= Math.pow(.45, EXPONENTIAL)) rgb = new Color(50, 97, 39).getRGB();
                else if (v > Math.pow(.45, EXPONENTIAL) && v <= Math.pow(.55, EXPONENTIAL)) rgb = new Color(129, 130, 129).getRGB();
                else if (v > Math.pow(.55, EXPONENTIAL) && v <= Math.pow(.75, EXPONENTIAL)) rgb = new Color(100, 102, 99).getRGB();
                else rgb = new Color(73, 74, 72).getRGB();

                image.setRGB(x, y, rgb);
            }
        }

        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generatePrecipitationImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x= 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                double precip = t.getPrecipitation();
                double EXPONENTIAL = WorldGenRunnable.getExponential();
                double OCEAN_LEVEL = worldMap.getOceanLevel();
                int rgb;
                if (precip <= 0) rgb = new Color(0, 0, 0).getRGB();
                else if (precip > 0 && precip < 0.2) rgb = new Color(64, 169, 255).getRGB();
                else if (precip >= 0.2 && precip < 0.5) rgb = new Color(3, 140, 252).getRGB();
                else if (precip >= 0.5 && precip < 1) rgb = new Color(4, 120, 214).getRGB();
                else if (precip >= 1 && precip < 1.5) rgb = new Color(0, 88, 161).getRGB();
                else if (precip >= 1.5 && precip < 2) rgb = new Color(0, 59, 107).getRGB();
                else if (precip >= 2 && precip < 2.5) rgb = new Color(0, 38, 69).getRGB();
                else rgb = new Color(0, 25, 46).getRGB();

                image.setRGB(x, y, rgb);
            }
        }

        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generateTemperatureImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x= 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                double temp = t.getTemperature();
                double EXPONENTIAL = WorldGenRunnable.getExponential();
                double OCEAN_LEVEL = worldMap.getOceanLevel();
                int rgb;
                if (t.getElevation() < worldMap.getOceanLevel()) {
                    image.setRGB(x, y, new Color(15, 76, 209).getRGB());
                    continue;
                }

                if (temp >= 45) rgb = new Color(191, 25, 0).getRGB();
                else if (temp < 45 && temp >= 40) rgb = new Color(191, 67, 0).getRGB();
                else if (temp < 40 && temp >= 35) rgb = new Color(201, 103, 4).getRGB();
                else if (temp < 35 && temp >= 30) rgb = new Color(214, 149, 9).getRGB();
                else if (temp < 30 && temp >= 25) rgb = new Color(237, 171, 28).getRGB();
                else if (temp < 25 && temp >= 20) rgb = new Color(183, 204, 47).getRGB();
                else if (temp < 20 && temp >= 15) rgb = new Color(47, 204, 118).getRGB();
                else if (temp < 15 && temp >= 10) rgb = new Color(47, 204, 167).getRGB();
                else if (temp < 10 && temp >= 5) rgb = new Color(47, 201, 204).getRGB();
                else if (temp < 5 && temp >= 0) rgb = new Color(47, 160, 204).getRGB();
                else if (temp < 0 && temp >= -5) rgb = new Color(47, 120, 204).getRGB();
                else rgb = new Color(47, 57, 204).getRGB();

                image.setRGB(x, y, rgb);
            }
        }

        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generateClimateImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x= 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                Climate climate = t.getClimate();
                double EXPONENTIAL = WorldGenRunnable.getExponential();
                double OCEAN_LEVEL = worldMap.getOceanLevel();
                int rgb = new Color(15, 76, 209).getRGB();
                switch(climate) {
                    case TUNDRA -> rgb = new Color(117, 117, 117).getRGB();
                    case TEMPERATE_FOREST -> rgb = new Color(14, 92, 8).getRGB();
                    case BOREAL_FOREST -> rgb = new Color(8, 92, 63).getRGB();
                    case SAVANNAH -> rgb = new Color(157, 207, 83).getRGB();
                    case TROPICAL -> rgb = new Color(55, 82, 16).getRGB();
                    case ICE -> rgb = new Color(200, 200, 200).getRGB();
                    case DESERT -> rgb = new Color(222, 205, 120).getRGB();
                    case GRASSLAND -> rgb = new Color(110, 201, 85).getRGB();
                    case SEASONAL_FOREST -> rgb = new Color(54, 102, 41).getRGB();
                    case TEMPERATE_RAINFOREST -> rgb = new Color(20, 56, 10).getRGB();
                    case WOODLAND -> rgb = new Color(32, 74, 21).getRGB();
                }

                image.setRGB(x, y, rgb);
            }
        }

        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generateRiverImage() {
        BufferedImage image = SwingFXUtils.fromFXImage(generateElevationImage(), null);
        for (int y = 0; y < height; y++) {
            for (int x= 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                int riverSize = t.getRiverSize();

                int rgb = new Color(0, 0, 0).getRGB();
                if (riverSize >= 9) rgb = new Color(1, 43, 82).getRGB();
                else if (riverSize >= 7) rgb = new Color(2, 58, 110).getRGB();
                else if (riverSize >= 5) rgb = new Color(4, 72, 135).getRGB();
                else if (riverSize >= 3) rgb = new Color(12, 94, 171).getRGB();
                else if (riverSize >= 1) rgb = new Color(28, 135, 235).getRGB();
                else if (riverSize == 0) continue;
                image.setRGB(x, y, rgb);

            }
        }


        return SwingFXUtils.toFXImage(image, null);
    }

    //Generates a tile fertility map
    public Image generateFertilityImage() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                int fert = t.getFertility();
                int rgb = new Color(15, 76, 209).getRGB();

                if (t.getClimate() != Climate.OCEAN) {
                    if (fert >= 90) rgb = new Color(47, 77, 22).getRGB();
                    else if (fert >= 80) rgb = new Color(70, 110, 37).getRGB();
                    else if (fert >= 70) rgb = new Color(92, 138, 55).getRGB();
                    else if (fert >= 60) rgb = new Color(110, 158, 71).getRGB();
                    else if (fert >= 50) rgb = new Color(141, 176, 91).getRGB();
                    else if (fert >= 40) rgb = new Color(172, 194, 105).getRGB();
                    else if (fert >= 30) rgb = new Color(190, 201, 113).getRGB();
                    else if (fert >= 20) rgb = new Color(202, 204, 112).getRGB();
                    else if (fert >= 10) rgb = new Color(214, 197, 122).getRGB();
                    else if (fert >= 0) rgb = new Color(191, 161, 99).getRGB();
                }

                img.setRGB(x, y, rgb);
            }
        }
        return SwingFXUtils.toFXImage(img, null);
    }

    //Generates a map of organic deposits
    public Image generateOrganicImage() {
        BufferedImage image = SwingFXUtils.fromFXImage(generateElevationImage(), null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                ArrayList<OrganicDeposit> organicDeposits = t.getOrganicDeposits();
                OrganicDeposit largestDeposit = null;
                int rgb = new Color(0, 0, 0).getRGB();

                for (OrganicDeposit o : organicDeposits){
                    if (largestDeposit == null || o.getDepositSize() > largestDeposit.getDepositSize()) largestDeposit = o;
                }

                if (largestDeposit == null) continue;
                else if (largestDeposit.getClass() == Wood.class) rgb = new Color(82, 53, 7).getRGB();
                else if (largestDeposit.getClass() == Fish.class) rgb = new Color(85, 201, 212).getRGB();
                else if (largestDeposit.getClass() == Whales.class) rgb = new Color(42, 104, 110).getRGB();

                image.setRGB(x, y, rgb);
            }
        }
        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generateMetalImage() {
        BufferedImage image = SwingFXUtils.fromFXImage(generateElevationImage(), null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                ArrayList<MetalDeposit> metalDeposits = t.getMetalDeposits();
                MetalDeposit largestDeposit = null;
                int rgb = new Color(0, 0, 0).getRGB();

                for (MetalDeposit m : metalDeposits) {
                    if (largestDeposit == null || m.getDepositSize() > largestDeposit.getDepositSize()) largestDeposit = m;
                }

                if (largestDeposit == null) continue;
                else if (largestDeposit.getClass() == Iron.class) rgb = new Color(140, 138, 132).getRGB();
                else if (largestDeposit.getClass() == Copper.class) rgb = new Color(176, 100, 53).getRGB();
                else if (largestDeposit.getClass() == Tin.class) rgb = new Color(130, 130, 130).getRGB();
                else if (largestDeposit.getClass() == Silver.class) rgb = new Color(201, 201, 201).getRGB();
                else if (largestDeposit.getClass() == Gold.class) rgb = new Color(196, 162, 24).getRGB();
                else if (largestDeposit.getClass() == Adamantine.class) rgb = new Color(17, 124, 150).getRGB();

                image.setRGB(x, y, rgb);
            }
        }
        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generateNonOrganicsImage() {
        BufferedImage image = SwingFXUtils.fromFXImage(generateElevationImage(), null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                ArrayList<ResourceDeposit> resourceDeposits = t.getNonOrganicDeposits();
                ResourceDeposit largestDeposit = null;
                int rgb = new Color(0, 0, 0).getRGB();

                for (ResourceDeposit r : resourceDeposits) {
                    if (largestDeposit == null) largestDeposit = r;
                    else if (r.getDepositSize() > largestDeposit.getDepositSize()) {
                        if (r.getClass() == Stone.class && largestDeposit.getClass() == Stone.class) largestDeposit = r;
                        else if (r.getClass() != Stone.class && largestDeposit.getClass() == Stone.class) largestDeposit = r;
                    }
                }

                if (largestDeposit == null) continue;
                else if (largestDeposit.getClass() == Stone.class) rgb = new Color(117, 117, 117).getRGB();
                else if (largestDeposit.getClass() == Clay.class) rgb = new Color(138, 53, 14).getRGB();
                else if (largestDeposit.getClass() == Coal.class) rgb = new Color(59, 59, 59).getRGB();

                image.setRGB(x, y, rgb);
            }
        }
        return SwingFXUtils.toFXImage(image, null);
    }

    public Image generateProvincesImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Province[] provinces = worldMap.getProvinces();

        for (Province p : provinces) {
            if (p == null) continue;
            for (WorldTile t : p.getProvinceTiles()) {
                image.setRGB(t.x, t.y, p.getColor());
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WorldTile t = worldMap.getTile(x, y);
                if (t.getClimate() == Climate.OCEAN) image.setRGB(x, y, new Color(15, 76, 209).getRGB());
            }
        }

        return SwingFXUtils.toFXImage(image, null);
    }
}
