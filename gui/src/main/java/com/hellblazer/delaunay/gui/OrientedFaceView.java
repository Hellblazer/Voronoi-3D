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

import javax.vecmath.Point3f;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.Vertex.DoubleType;

import javafx.geometry.Point3D;

/**
 * A visualization of an oriented face of a tetrahedron in a delaunay
 * tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class OrientedFaceView extends GraphicsView {
    private Point3f                        adjacent;
    private List<Point3f[]>                adjacentFaces = new ArrayList<>();
    private final OrientedFace<DoubleType> face;
    private Point3f                        incident;
    private List<Point3f[]>                incidentFaces = new ArrayList<>();
    private Point3f[]                      myFace        = new Point3f[3];

    public OrientedFaceView(OrientedFace<DoubleType> face) {
        super();
        this.face = face;
        update();
    }

    public void update() {
        updateDiagram();

        for (Point3f p : myFace) {
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
        for (Vertex<DoubleType> v : face) {
            myFace[i++] = v.asPoint3f();
        }
        incidentFaces.clear();
        adjacentFaces.clear();
        incident = face.getIncidentVertex().asPoint3f();
        face.getIncident().addFacesCoordinates(incidentFaces);
        if (face.hasAdjacent()) {
            adjacent = face.getAdjacentVertex().asPoint3f();
            face.getAdjacent().addFacesCoordinates(adjacentFaces);
        }
    }
}
