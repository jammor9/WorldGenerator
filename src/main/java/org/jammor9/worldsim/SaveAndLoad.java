package org.jammor9.worldsim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.jammor9.worldsim.resources.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class SaveAndLoad{

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Resource.class, new AbstractSerializer())
            .registerTypeAdapter(OrganicDeposit.class, new AbstractSerializer())
            .registerTypeAdapter(OreDeposit.class, new AbstractSerializer())
            .registerTypeAdapter(ResourceDeposit.class, new AbstractSerializer()).create();

    //Text for popup boxes and returns
    private static final String INPUT_STRING = "Enter save name: ";
    private static final String SAVE_BUTTON = "SAVE";
    private static final String LOAD_BUTTON = "LOAD";
    private static final String SAVE_SUCCESSFUL = "SAVE SUCCESSFUL";
    private static final String NAME_EXISTS = "SAVE NAME ALREADY EXISTS!";
    private static final String LOAD_SUCCESSFUL = "LOAD SUCCESSFUL";

    //Popup Window Data
    private static final int POPUP_WIDTH = 300;
    private static final int POPUP_HEIGHT = 200;
    private static final String POPUP_BACKGROUND_COLOR = "#2f2f2f";

    private Stage stage;
    private ViewBuilder view;
    private final String SAVE_DIRECTORY;

    public SaveAndLoad(ViewBuilder view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.SAVE_DIRECTORY = System.getProperty("user.dir") + "/saves/";
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Creates a popup with an input, save button, and hidden error label
    //Takes a filename as an input from the user, if it already exists raises an error and shows error label
    public void saveWorld(WorldMap worldMap) {
        Button saveButton = new Button(SAVE_BUTTON);
        TextField input = new TextField(INPUT_STRING);
        Label errorLabel = new Label(NAME_EXISTS);
        VBox vbox = new VBox(10, input, saveButton);
        vbox.setAlignment(Pos.CENTER);

        Popup pop = new Popup();
        pop.setAutoHide(true);
        vbox.setPrefHeight(POPUP_HEIGHT);
        vbox.setPrefWidth(POPUP_WIDTH);
        vbox.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("css/display.css")).toExternalForm());
        vbox.getStyleClass().add("popup");
        errorLabel.getStyleClass().add("popup-error-label");
        pop.getContent().add(vbox);
        pop.show(stage);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String path = SAVE_DIRECTORY + input.getText() + ".json";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.createNewFile();
                        FileWriter w = new FileWriter(path);
                        gson.toJson(worldMap, w);
                        w.close();
                        pop.hide();
                        view.addLabel(SAVE_SUCCESSFUL);
                    }
                    else {
                        if (!vbox.getChildren().contains(errorLabel)) vbox.getChildren().add(errorLabel);
                        pop.show(stage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void loadWorld() {
        Button loadButton = new Button(LOAD_BUTTON);
        ListView<Label> loadFiles = new ListView<>();
        VBox vbox = new VBox(10, loadFiles, loadButton);
        vbox.setAlignment(Pos.CENTER);

        Popup pop = new Popup();
        pop.setAutoHide(true);
        vbox.setPrefHeight(POPUP_HEIGHT);
        vbox.setPrefWidth(POPUP_WIDTH);
        vbox.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("css/display.css")).toExternalForm());
        vbox.getStyleClass().add("popup");
        pop.getContent().add(vbox);

        File saveDirectory = new File(SAVE_DIRECTORY);
        for (File save : saveDirectory.listFiles()) {
            if (save.toString().endsWith(".json")) {
                Label label = new Label(save.getName());
                loadFiles.getItems().add(label);
            }
        }
        pop.show(stage);

        WorldMap loadMap;

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            try {
                String load = loadFiles.getSelectionModel().getSelectedItem().getText();
                String path = SAVE_DIRECTORY + load;
                FileReader r = new FileReader(path);
                WorldMap loadMap = gson.fromJson(r, WorldMap.class);
                view.loadNewMap(loadMap);
                view.addLabel(LOAD_SUCCESSFUL);
                r.close();
                pop.hide();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            }
        });
    }
}
