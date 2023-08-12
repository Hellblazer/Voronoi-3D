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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point3f;

import com.hellblazer.delaunay.IdentitySet;
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

    private List<Point3f[]>    delaunayFaces = new ArrayList<>();
    private Set<Point3f>       fourCorners   = new HashSet<>();
    private Group              highlightedRegions;
    private Tetrahedralization tetrahedralization;
    private Set<Tetrahedron>   tetrahedrons  = new IdentitySet<>(100);
    private final Set<Vertex>  vertices      = new IdentitySet<>(100);
    private List<Point3f[]>    voronoiFaces  = new ArrayList<>();

    public TetrahedralizationView() {
        this(new Tetrahedralization());
    }

    public TetrahedralizationView(Tetrahedralization t) {
        super();
        tetrahedralization = t;
        for (Vertex v : tetrahedralization.getUniverse()) {
            fourCorners.add(v.asPoint3f());
        }
        updateDiagram();
    }

    public Tetrahedralization getTetrahedralization() {
        return tetrahedralization;
    }

    public void highlightRegions(boolean highlight, List<Vertex> vertices) {
        assert vertices != null;
        if (highlight) {
            for (Vertex v : vertices) {
                render(tetrahedralization.getVoronoiRegion(v), COLOR_OF_HIGHLIGHTED_REGION, true);
            }
            displaySpheres(vertices, 0.03F, COLOR_OF_HIGHLIGHTED_REGION);
        }
        getChildren().add(highlightedRegions);
    }

    public void update(boolean showVD, boolean showDT, boolean showFaces, boolean showAllPoints) {
        updateDiagram();
        createDiagram();
        if (showVD) {
            render(voronoiFaces, Colors.blueMaterial, showFaces);
        }
        if (showDT) {
            render(delaunayFaces, Colors.yellowMaterial, showFaces);
        }
        if (showAllPoints) {
            displaySpheres(vertices, 0.01F, Colors.redMaterial);
        }
    }

    public void updateDiagram() {
        tetrahedrons.clear();
        vertices.clear();
        tetrahedralization.traverse(tetrahedrons, vertices);
        voronoiFaces.clear();
        delaunayFaces.clear();
        for (Tetrahedron t : tetrahedrons) {
            t.addFacesCoordinates(delaunayFaces);
        }
        for (Vertex v : vertices) {
            for (Point3f[] face : tetrahedralization.getVoronoiRegion(v)) {
                voronoiFaces.add(face);
            }
        }
    }

    @Override
    protected boolean isAuxillary(Point3f[] face) {
        if (face.length < 3) {
            return true;
        }
        for (var v : face) {
            if (fourCorners.contains(v)) {
                return true;
            } else if (v.distance(new Point3f()) > 1000000) {
                return true;
            }
        }
        return false;
    }
}
