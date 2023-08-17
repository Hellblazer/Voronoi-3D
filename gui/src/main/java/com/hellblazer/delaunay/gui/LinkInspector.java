package com.hellblazer.delaunay.gui;

import static com.hellblazer.delaunay.gui.Colors.blackMaterial;

import java.util.Random;

import javax.vecmath.Point3d;

import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.gui.CubicGrid.Neighborhood;
import com.javafx.experiments.jfx3dviewer.Jfx3dViewerApp;

import javafx.scene.Group;

public class LinkInspector extends Jfx3dViewerApp {
    public static class Launcher {

        public static void main(String[] argv) {
            LinkInspector.main(argv);
        }
    }

    public static void main(String[] argv) {
        launch(argv);
    }

    private LinkView view;

    @Override
    protected void initializeContentModel() {
        final Tetrahedralization tet = new Tetrahedralization(new Random(666));
        Point3d[] vertices = Examples.getCubicCrystalStructure();
        Vertex vert = null;
        for (var v : vertices) {
            vert = tet.insert(v);
        }
        view = new LinkView(vert, vert.getEars(), vert.getVoronoiRegion());

        var content = getContentModel();
        var group = new Group();

        var grid = new CubicGrid(Neighborhood.EIGHT, PhiCoordinates.Cubes[3], 1);
        group.getChildren().add(grid.construct(blackMaterial, blackMaterial, blackMaterial));
        group.getChildren().add(view);

        content.setContent(group);
    }
}
