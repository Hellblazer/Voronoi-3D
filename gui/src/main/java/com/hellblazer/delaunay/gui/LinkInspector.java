package com.hellblazer.delaunay.gui;

import static com.hellblazer.delaunay.gui.Colors.blackMaterial;

import java.util.Random;

import com.hellblazer.delaunay.Examples;
import com.hellblazer.delaunay.TetrahedralizationD;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.Vertex.DoubleType;
import com.hellblazer.delaunay.VertexD;
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
        final TetrahedralizationD tet = new TetrahedralizationD(new Random(666));
        VertexD[] vertices = Examples.getCubicCrystalStructure();
        for (Vertex<DoubleType> v : vertices) {
            tet.insert(v);
        }
        VertexD v = vertices[13];
        view = new LinkView(v, tet.getEars(v), tet.getVoronoiRegion(v));

        var content = getContentModel();
        var group = new Group();

        var grid = new CubicGrid(Neighborhood.EIGHT, PhiCoordinates.Cubes[3], 1);
        group.getChildren().add(grid.construct(blackMaterial, blackMaterial, blackMaterial));
        group.getChildren().add(view);

        content.setContent(group);
    }
}
