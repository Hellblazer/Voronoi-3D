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

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.vecmath.Point3f;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * A visualization of an oriented face of a tetrahedron in a delaunay
 * tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class OrientedFaceView extends GraphicsView {
    private static final long  serialVersionUID = 1L;
    private Point3f            adjacent;
    private List<Point3f[]>    adjacentFaces    = new ArrayList<Point3f[]>();
    private final OrientedFace face;
    private Point3f            incident;
    private List<Point3f[]>    incidentFaces    = new ArrayList<Point3f[]>();
    private Point3f[]          myFace           = new Point3f[3];

    public OrientedFaceView(OrientedFace face) {
        this(face, SimpleUniverse.getPreferredConfiguration());
    }

    public OrientedFaceView(OrientedFace face, GraphicsConfiguration gC) {
        super(gC);
        this.face = face;
        createSceneGraph();
        update();
    }

    public void update() {
        createDiagram();
        updateDiagram();

        for (Point3f p : myFace) {
            diagram.addChild(createSphereAround(p, COLOR_OF_VD, 0.01F));
        }
        Appearance appearance = getCapabilities();
        diagram.addChild(createSphereAround(incident,
                                            COLOR_OF_HIGHLIGHTED_REGION, 0.01F));
        if (adjacent != null) {
            diagram.addChild(createSphereAround(adjacent, COLOR_OF_DT, 0.01F));
            render(adjacentFaces, COLOR_OF_DT, diagram, false, appearance);
        }
        render(incidentFaces, COLOR_OF_HIGHLIGHTED_REGION, diagram, false,
               appearance);
        transformGroup.addChild(diagram);
        doLayout();
    }

    private void updateDiagram() {
        int i = 0;
        for (Vertex v : face) {
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
