package org.jammor9.worldsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final int HEIGHT = 1024;
    private static final int WIDTH = 1024;
    private static WorldGenRunnable worldGenRunnable;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(new Controller(worldGenRunnable).getView()));
        stage.show();
    }

    public static void main(String[] args) {
        worldGenRunnable = new WorldGenRunnable();
        launch();
    }

    private Region createContent() {
        return new Label("Hello World!");
    }
}