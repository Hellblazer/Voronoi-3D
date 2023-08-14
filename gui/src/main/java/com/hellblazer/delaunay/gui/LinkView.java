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

import java.util.List;

import javax.vecmath.Point3f;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.Vertex.DoubleType;
import com.hellblazer.delaunay.VertexD;

/**
 * A visualization of the link set of a vertex in a delaunay tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class LinkView extends GraphicsView {
    private final List<OrientedFace<Vertex.DoubleType>> ears;
    private final VertexD                               v;
    private final List<Point3f[]>                       voronoiRegion;

    public LinkView(VertexD v, List<OrientedFace<Vertex.DoubleType>> ears, List<Point3f[]> voronoiRegion) {
        this.v = v;
        this.ears = ears;
        this.voronoiRegion = voronoiRegion;
        update();
    }

    public void update() {
        getChildren().clear();
        for (OrientedFace<Vertex.DoubleType> ear : ears) {
            Vertex<DoubleType>[] edge = ear.getEdge(v);
            Point3f[] line = new Point3f[] { edge[0].asPoint3f(), edge[1].asPoint3f() };
            newFace(line, Colors.yellowMaterial, false, this);
        }
        sphere(0.01F, new Point3f(new float[] { (float) v.x, (float) v.y, (float) v.z }), Colors.violetMaterial);
        render(voronoiRegion, Colors.redMaterial, true, this);
    }
}
