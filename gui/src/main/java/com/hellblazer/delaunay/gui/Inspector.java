package com.hellblazer.delaunay.gui;

import java.util.Random;

import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Vertex;
import com.javafx.experiments.jfx3dviewer.Jfx3dViewerApp;

import javafx.scene.Group;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class Inspector extends Jfx3dViewerApp {
    public static class Launcher {

        public static void main(String[] argv) {
            Inspector.main(argv);
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

    private TetrahedralizationView view;

    public Inspector() {

    }

    public Inspector(Tetrahedralization vd) {
        view = new TetrahedralizationView(vd);
    }

    @Override
    protected void initializeContentModel() {
        final var random = new Random(666);
        final var tet = new Tetrahedralization(random);
        Vertex ourPoints[] = Vertex.getRandomPoints(random, 120, 40.0D, false);
        for (Vertex v : ourPoints) {
            tet.insert(v);
        }
        view = new TetrahedralizationView(tet);
        view.update(true, false, false, true);

        var content = getContentModel();
        var group = new Group();

//        group.getChildren().add(new CubicGrid(Neighborhood.EIGHT, PhiCoordinates.Cubes[3], 1).construct(blackMaterial, blackMaterial, blackMaterial));
        group.getChildren().add(view);

        content.setContent(group);
    }
}
