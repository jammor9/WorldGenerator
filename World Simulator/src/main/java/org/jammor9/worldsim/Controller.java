package org.jammor9.worldsim;


import javafx.scene.layout.Region;
import javafx.util.Builder;

public class Controller {
    private Builder<Region> viewBuilder;
    private Interactor interactor;
    private WorldGenRunnable runnable;

    public Controller(WorldGenRunnable runnable) {
        Model model = new Model();
        viewBuilder = new ViewBuilder(model, runnable);
        interactor = new Interactor(model);
        this.runnable = runnable;
    }

    public Region getView() {
        return viewBuilder.build();
    }
}