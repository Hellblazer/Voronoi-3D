/**
 * Copyright (C) 2008-2023 Hal Hildebrand. All rights reserved.
 * 
 * This file is part of the Prime Mover Event Driven Simulation Framework.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.hellblazer.delaunay;

import static com.hellblazer.delaunay.Geometry.centerSphere;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;

import com.hellblazer.delaunay.Vertex.DoubleType;

/**
 * @author hal.hildebrand
 */
public class TetrahedronD extends Tetrahedron<Vertex.DoubleType> {

    public TetrahedronD(Vertex<DoubleType> x, Vertex<DoubleType> y, Vertex<DoubleType> z, Vertex<DoubleType> w) {
        super(x, y, z, w);
    }

    public TetrahedronD(Vertex<DoubleType>[] vertices) {
        super(vertices);
    }

    @Override
    Tetrahedron<DoubleType> newT0(Vertex<DoubleType> n) {
        return new TetrahedronD(a, b, c, n);
    }

    @Override
    Tetrahedron<DoubleType> newT1(Vertex<DoubleType> n) {
        return new TetrahedronD(a, d, b, n);
    }

    @Override
    Tetrahedron<DoubleType> newT2(Vertex<DoubleType> n) {
        return new TetrahedronD(a, c, d, n);
    }

    @Override
    Tetrahedron<DoubleType> newT3(Vertex<DoubleType> n) {
        return new TetrahedronD(b, d, c, n);
    }

    @Override
    Tetrahedron<DoubleType> newTet(Vertex<DoubleType> a, Vertex<DoubleType> b, Vertex<DoubleType> c,
                                   Vertex<DoubleType> d) {
        return new TetrahedronD(a, b, c, d);
    }

    /**
     * Traverse the points which define the voronoi face defined by the dual of the
     * line segement defined by the center point and the axis. Terminate the
     * traversal if we have returned to the originating tetrahedron.
     * <p>
     *
     * @param origin
     * @param from
     * @param vC
     * @param axis
     * @param face
     */
    @Override
    void traverseVoronoiFace(Tetrahedron<Vertex.DoubleType> origin, Tetrahedron<Vertex.DoubleType> from,
                             Vertex<Vertex.DoubleType> vC, Vertex<Vertex.DoubleType> axis, List<Point3f> face) {
        if (origin == this) {
            return;
        }
        double[] center = new double[3];
        centerSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(), b.vertices().getX(),
                     b.vertices().getY(), b.vertices().getZ(), c.vertices().getX(), c.vertices().getY(),
                     c.vertices().getZ(), d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V next = VORONOI_FACE_NEXT[ordinalOf(from).ordinal()][ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron<Vertex.DoubleType> t = getNeighbor(next);
        if (t != null) {
            t.traverseVoronoiFace(origin, this, vC, axis, face);
        }

    }

    /**
     * Traverse the points which define the voronoi face defined by the dual of the
     * line segement defined by the center point and the axis.
     * <p>
     *
     * @param vC
     * @param axis
     * @param face
     */
    @Override
    void traverseVoronoiFace(Vertex<Vertex.DoubleType> vC, Vertex<Vertex.DoubleType> axis, List<Point3f[]> faces) {
        ArrayList<Point3f> face = new ArrayList<>();
        double[] center = new double[3];
        centerSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(), b.vertices().getX(),
                     b.vertices().getY(), b.vertices().getZ(), c.vertices().getX(), c.vertices().getY(),
                     c.vertices().getZ(), d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V v = VORONOI_FACE_ORIGIN[ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron<Vertex.DoubleType> next = getNeighbor(v);
        if (next != null) {
            next.traverseVoronoiFace(this, this, vC, axis, face);
        }
        faces.add(face.toArray(new Point3f[face.size()]));
    }

}
