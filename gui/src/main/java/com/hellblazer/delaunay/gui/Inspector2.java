package com.hellblazer.delaunay.gui;

import static com.hellblazer.delaunay.gui.Colors.blackMaterial;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Random;

import com.hellblazer.delaunay.Examples;
import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.gui.CubicGrid.Neighborhood;
import com.javafx.experiments.jfx3dviewer.Jfx3dViewerApp;

import javafx.scene.Group;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class Inspector2 extends Jfx3dViewerApp {
    public static class Launcher {

        public static void main(String[] argv) {
            Inspector2.main(argv);
        }
    }

    public static final Color COLOR_OF_SELECTED_ROWS = new Color(1.0F, 0.0F, 0.0F);

    public static void main(String args[]) {
        final Tetrahedralization tet = new Tetrahedralization(new Random(666));
        for (Vertex v : Examples.getCubicCrystalStructure()) {
            tet.insert(v);
        }
        Inspector2 insp = new Inspector2(tet);
        insp.launch(args);
    }

    private ArrayList<Vertex>       vertices = new ArrayList<>();
    private TetrahedralizationView2 view;

    public Inspector2() {

    }

    public Inspector2(Tetrahedralization vd) {
        vertices = new ArrayList<>(vd.getVertices());
        view = new TetrahedralizationView2(vd);
    }

    public void open() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initializeContentModel() {
        final var tet = new Tetrahedralization(new Random(666));
        for (Vertex v : Examples.getCubicCrystalStructure()) {
            tet.insert(v);
        }
        view = new TetrahedralizationView2(tet);
        view.update(true, false, false, true);
        vertices = new ArrayList<>(tet.getVertices());

        var content = getContentModel();
        var group = new Group();

        var grid = new CubicGrid(Neighborhood.EIGHT, PhiCoordinates.Cubes[3], 1);
        group.getChildren().add(grid.construct(blackMaterial, blackMaterial, blackMaterial));
        group.getChildren().add(view);

        content.setContent(group);
    }
}
