/**
 * Copyright (C) 2008 Hal Hildebrand. All rights reserved.
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
import java.util.List;

import javax.vecmath.Tuple3d;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;

import javafx.geometry.Point3D;

/**
 * A visualization of an oriented face of a tetrahedron in a delaunay
 * tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class OrientedFaceView extends GraphicsView {
    private Tuple3d            adjacent;
    private List<Tuple3d[]>    adjacentFaces = new ArrayList<>();
    private final OrientedFace face;
    private Tuple3d            incident;
    private List<Tuple3d[]>    incidentFaces = new ArrayList<>();
    private Tuple3d[]          myFace        = new Tuple3d[3];

    public OrientedFaceView(OrientedFace face) {
        super();
        this.face = face;
        update();
    }

    public void update() {
        updateDiagram();

        for (Tuple3d p : myFace) {
            getChildren().add(sphere(0.01F, new Point3D(p.x, p.y, p.z), Colors.blueMaterial));
        }
        getChildren().add(sphere(0.01F, new Point3D(incident.x, incident.y, incident.z), Colors.cyanMaterial));
        if (adjacent != null) {
            getChildren().add(sphere(0.01F, new Point3D(adjacent.x, adjacent.y, adjacent.z), Colors.redMaterial));
            render(adjacentFaces, Colors.yellowMaterial, false, this);
        }
        render(incidentFaces, Colors.purpleMaterial, false, this);
    }

    private void updateDiagram() {
        int i = 0;
        for (Vertex v : face) {
            myFace[i++] = v;
        }
        incidentFaces.clear();
        adjacentFaces.clear();
        incident = face.getIncidentVertex();
        incidentFaces.addAll(face.getIncident().getFaces());
        if (face.hasAdjacent()) {
            adjacent = face.getAdjacentVertex();
            adjacentFaces.addAll(face.getAdjacent().getFaces());
        }
    }
}
