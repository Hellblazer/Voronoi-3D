/**
 * Copyright (C) 2009 Hal Hildebrand. All rights reserved.
 *
 * This file is part of the 3D Incremental Voronoi GUI
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.vecmath.Tuple3d;

import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Tetrahedron;
import com.hellblazer.delaunay.Vertex;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TetrahedralizationView extends GraphicsView {

    private static final PhongMaterial COLOR_OF_HIGHLIGHTED_REGION = null;

    private final Group              delaunay           = new Group();
    private final Set<Tuple3d>       fourCorners        = new HashSet<>();
    private final Group              highlightedRegions = new Group();
    private final Tetrahedralization tetrahedralization;
    private final Group              vertexes           = new Group();
    private final Group              voronoi            = new Group();

    public TetrahedralizationView() {
        this(new Tetrahedralization());
    }

    public TetrahedralizationView(Tetrahedralization t) {
        super();
        tetrahedralization = t;
        for (Vertex v : tetrahedralization.getUniverse()) {
            fourCorners.add(v);
        }
        updateDiagram();
    }

    public Tetrahedralization getTetrahedralization() {
        return tetrahedralization;
    }

    public void highlightRegions(boolean highlight, List<Vertex> vertices) {
        assert vertices != null;
        highlightedRegions.getChildren().clear();
        if (highlight) {
            for (Vertex v : vertices) {
                render(v.getVoronoiRegion(), COLOR_OF_HIGHLIGHTED_REGION, true, highlightedRegions);
            }
            displaySpheres(vertices, 0.03F, COLOR_OF_HIGHLIGHTED_REGION, highlightedRegions);
        }
        getChildren().add(highlightedRegions);
    }

    public void update(boolean showVD, boolean showDT, boolean showAllPoints) {
        var children = getChildren();
        children.clear();

        if (showVD) {
            children.add(voronoi);
        }
        if (showDT) {
            children.add(delaunay);
        }
        if (showAllPoints) {
            children.add(vertexes);
        }
    }

    public void updateDiagram() {
        var tetrahedrons = new HashSet<Tetrahedron>();
        var vertices = new HashSet<Vertex>();
        tetrahedralization.traverse(tetrahedrons, vertices);
        voronoi.getChildren().clear();
        delaunay.getChildren().clear();
        for (Tetrahedron t : tetrahedrons) {
            for (var face : t.getFaces()) {
                render(face, Colors.yellowMaterial, false, delaunay);
            }
        }
        for (Vertex v : vertices) {
            for (var face : v.getVoronoiRegion()) {
                render(face, Colors.cyanMaterial, false, voronoi);
            }
        }
        displaySpheres(vertices, 0.01, Colors.yellowMaterial, vertexes);
    }

    @Override
    protected boolean isAuxillary(Tuple3d[] face) {
        if (face.length < 3) {
            return true;
        }
        for (var v : face) {
            if (fourCorners.contains(v)) {
                return true;
            } else if (lengthSquared(v) > 1000000) {
                return true;
            }
        }
        return false;
    }

    private double lengthSquared(Tuple3d test) {
        return (test.x * test.x + test.y * test.y + test.z * test.z);
    }
}
