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

import java.util.Random;

import com.hellblazer.delaunay.TetrahedralizationD;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.Vertex.DoubleType;
import com.hellblazer.delaunay.VertexD;
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

    public Inspector(TetrahedralizationD vd) {
        view = new TetrahedralizationView(vd);
    }

    @Override
    protected void initializeContentModel() {
        final var random = new Random(666);
        final var tet = new TetrahedralizationD(random);
        Vertex<DoubleType> ourPoints[] = VertexD.getRandomPoints(random, 120, 5.0D, true);
        for (Vertex<DoubleType> v : ourPoints) {
            tet.insert(v);
        }
        view = new TetrahedralizationView(tet);
        view.update(true, false, true);

        var content = getContentModel();
        var group = new Group();

//        group.getChildren().add(new CubicGrid(Neighborhood.EIGHT, PhiCoordinates.Cubes[3], 1).construct(blackMaterial, blackMaterial, blackMaterial));
        group.getChildren().add(view);

        content.setContent(group);
    }
}
