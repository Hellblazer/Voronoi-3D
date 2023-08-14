package com.hellblazer.delaunay.gui;

import static com.hellblazer.delaunay.gui.Colors.blackMaterial;

import java.util.ArrayList;
import java.util.Random;

import com.hellblazer.delaunay.Examples;
import com.hellblazer.delaunay.TetrahedralizationD;
import com.hellblazer.delaunay.Tetrahedron;
import com.hellblazer.delaunay.V;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.Vertex.DoubleType;
import com.hellblazer.delaunay.gui.CubicGrid.Neighborhood;
import com.javafx.experiments.jfx3dviewer.Jfx3dViewerApp;

import javafx.scene.Group;

public class OrientedFaceInspector extends Jfx3dViewerApp {
    public static class Launcher {

        public static void main(String[] argv) {
            OrientedFaceInspector.main(argv);
        }
    }

    public static void main(String[] argv) {
        launch(argv);
    }

    private OrientedFaceView view;

    @Override
    protected void initializeContentModel() {
        final TetrahedralizationD tet = new TetrahedralizationD(new Random(666));
        for (Vertex<DoubleType> v : Examples.getCubicCrystalStructure()) {
            tet.insert(v);
        }
        ArrayList<Tetrahedron<DoubleType>> tets = new ArrayList<Tetrahedron<DoubleType>>(tet.getTetrahedrons());
        view = new OrientedFaceView(tets.get(2).getFace(V.C));

        var content = getContentModel();
        var group = new Group();

        var grid = new CubicGrid(Neighborhood.EIGHT, PhiCoordinates.Cubes[3], 1);
        group.getChildren().add(grid.construct(blackMaterial, blackMaterial, blackMaterial));
        group.getChildren().add(view);

        content.setContent(group);

    }
}
