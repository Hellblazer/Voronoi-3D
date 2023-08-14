/**
 * Copyright (C) 2016 Hal Hildebrand. All rights reserved.
 *
 * This file is part of the 3D Incremental Voronoi system
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
