package org.jammor9.worldsim;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Builder;

import java.util.Objects;

public class ViewBuilder implements Builder<Region> {

    private final WorldGenRunnable worldGen;
    private final SaveAndLoad saveAndLoad;
    private WorldMap worldMap;
    private Canvas canvas;
    private int width;
    private int height;
    private ListView<Label> listView;
    private Stage stage;

    //Button names
    private final String ELEV_BUTTON = "ELEVATION";
    private final String PRECIP_BUTTON = "PRECIPITATION";
    private final String RIVER_BUTTON = "RIVERS";
    private final String TEMP_BUTTON = "TEMPERATURE";
    private final String CLIMATE_BUTTON = "CLIMATE";
    private final String NEW_WORLD_BUTTON = "NEW WORLD";
    private final String SAVE_BUTTON = "SAVE";
    private final String LOAD_BUTTON = "LOAD";

    //Pane Settings
    private final int SCROLL_PANE_MAX_HEIGHT = 900;
    private final int SCROLL_PANE_MAX_WIDTH = 1500;
    private final int MAX_LABELS = 30;

    //Left and Right Sidebars
    private final int SIDEBAR_WIDTH = 400;
    private final int INSET_SIZE = 10;
    private final int SIDEBAR_SPACING = 10;
    private final String SIDEBAR_BACKGROUND_COLOR = "#262626";

    public ViewBuilder(Stage stage, WorldGenRunnable worldGen){
        this.worldGen = worldGen;
        this.width = worldGen.getWidth();
        this.height = worldGen.getHeight();
        this.saveAndLoad = new SaveAndLoad(this, stage);
        this.stage = stage;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("css/display.css")).toExternalForm());
        results.setCenter(createCenterMap(width, height));
        results.setBottom(createBottom());
        results.setLeft(createLeftMenu());
        results.setRight(createRightText());
        return results;
    }

    private Node createCenterMap(int width, int height) {
        Canvas canvas = new Canvas(width, height);
        ScrollPane scrollPane = new ScrollPane(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        scrollPane.setMaxHeight(Math.min(height, SCROLL_PANE_MAX_HEIGHT));
        scrollPane.setMaxWidth(Math.min(width, SCROLL_PANE_MAX_WIDTH));
        this.canvas = canvas;
        scrollPane.setPannable(true);
        return scrollPane;
    }

    private Node createBottom() {
        Button newWorld = new Button(NEW_WORLD_BUTTON);
        Button save = new Button(SAVE_BUTTON);
        Button load = new Button(LOAD_BUTTON);
        newWorld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addLabel("NEW WORLD GENERATED");
                worldGen.run();
                worldMap = worldGen.getWorldMap();
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, width, height);
                ImageGenerator imageGenerator = new ImageGenerator(width, height, worldMap);
                Image image = imageGenerator.generateElevationImage();
                gc.drawImage(image, 0, 0);
            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (worldMap == null) return;
                saveAndLoad.saveWorld(worldMap);
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                saveAndLoad.loadWorld();
            }
        });

        HBox start = new HBox(10, newWorld, save, load);
        start.setAlignment(Pos.CENTER);
        return start;
    }

    //Generates left menu buttons for world generation
    private Node createLeftMenu() {
        Button elev = generateMapButton(ELEV_BUTTON);
        Button precip = generateMapButton(PRECIP_BUTTON);
        Button rivers = generateMapButton(RIVER_BUTTON);
        Button temp = generateMapButton(TEMP_BUTTON);
        Button climate = generateMapButton(CLIMATE_BUTTON);

        VBox left = new VBox(elev, precip, rivers, temp, climate);
        left.setStyle("-fx-background-color: " + SIDEBAR_BACKGROUND_COLOR);
        left.setSpacing(SIDEBAR_SPACING);
        left.setPrefWidth(SIDEBAR_WIDTH);
        left.setPadding(new Insets(INSET_SIZE));
        return left;
    }

    //Calls imageGenerator to produce the pixel images to be displayed in the centre
    private void generateImage(String button) {
        if (worldMap == null || !worldGen.isGenerated()) return;
        ImageGenerator imageGenerator = new ImageGenerator(width, height, worldMap);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image img = null;

        if (button.equals(ELEV_BUTTON)) img = imageGenerator.generateElevationImage();
        else if (button.equals(PRECIP_BUTTON)) img = imageGenerator.generatePrecipitationImage();
        else if (button.equals(RIVER_BUTTON)) img = imageGenerator.generateRiverImage();
        else if (button.equals(TEMP_BUTTON)) img = imageGenerator.generateTemperatureImage();
        else if (button.equals(CLIMATE_BUTTON)) img = imageGenerator.generateClimateImage();

        gc.drawImage(img, 0, 0);
    }

    //Generates each map button
    private Button generateMapButton(String s) {
        Button b = new Button(s);
        b.setOnAction(actionEvent -> generateImage(s));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPadding(new Insets(INSET_SIZE));
        return b;
    }

    //Generates the right pane, which dispalys text about world generation and history simulation
    private Node createRightText() {
        Label label = new Label("START\nSTART");
        label.setStyle("-fx-background-color: #000000");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        VBox right = new VBox(label);
        ListView<Label> listView = new ListView<>();
        listView.getItems().add(label);
        right.setStyle("-fx-background-color: " + SIDEBAR_BACKGROUND_COLOR);
        right.setAlignment(Pos.TOP_CENTER);
        right.setSpacing(SIDEBAR_SPACING);
        right.setPrefWidth(SIDEBAR_WIDTH);
        right.setPadding(new Insets(INSET_SIZE));
        listView.setPrefWidth(SIDEBAR_WIDTH);
        this.listView = listView;
        return listView;
    }

    protected void addLabel(String s) {
        addLabel(s, "#000000");
    }

    protected void addLabel(String s, String c) {
        Label label = new Label(s);
        label.setStyle("-fx-background-color: " + c);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        listView.getItems().addFirst(label);
//        if (textBox.getChildren().size() > MAX_LABELS) textBox.getChildren().removeLast();
    }

    protected void loadNewMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        generateImage(ELEV_BUTTON);
    }

}
