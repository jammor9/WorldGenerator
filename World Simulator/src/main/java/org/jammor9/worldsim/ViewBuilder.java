package org.jammor9.worldsim;

import eu.hansolo.tilesfx.tools.Pixel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

public class ViewBuilder implements Builder<Region> {

    private final Model model;
    private final WorldGenRunnable worldGen;
    private WorldMap worldMap;
    private Canvas worldCanvas;
    private int width;
    private int height;
    public ViewBuilder(Model model, WorldGenRunnable worldGen) {
        this.model = model;
        this.worldGen = worldGen;
        this.width = 1024;
        this.height = 1024;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
//        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/display.css")).toExternalForm());
        results.setTop(headingLabel("World Simulator"));
        results.setCenter(createCenter(width, height));
        results.setBottom(createButtons());
        return results;
    }

    private Node createCenter(int width, int height) {
        Canvas grid = new Canvas();
        grid.setHeight(height);
        grid.setWidth(width);
        GraphicsContext gc = grid.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, height, width);
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setMaxHeight(500);
        scrollPane.setMaxWidth(500);
        this.worldCanvas = grid;
        return scrollPane;
    }

    private Node createButtons() {
        Button newButton = new Button("New World");
        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                worldGen.run();
            }
        });
        HBox start = new HBox(10, newButton);
        start.setAlignment(Pos.CENTER);
        return start;
    }

    private Node headingLabel(String contents) {
        return styledLabel(contents, "heading-label");
    }

    private Node styledLabel(String contents, String styleClass) {
        Label label = new Label(contents);
        label.getStyleClass().add(styleClass);
        return label;
    }

}
