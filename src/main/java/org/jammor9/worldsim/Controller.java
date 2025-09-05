package org.jammor9.worldsim;


import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;

public class Controller {
    private Builder<Region> viewBuilder;
    private Interactor interactor;
    private WorldGenRunnable runnable;
    private Stage stage;

    public Controller(Stage stage, WorldGenRunnable runnable) {
        viewBuilder = new ViewBuilder(stage, runnable);
        interactor = new Interactor();
        this.runnable = runnable;
        this.stage = stage;
    }

    public Region getView() {
        return viewBuilder.build();
    }
}