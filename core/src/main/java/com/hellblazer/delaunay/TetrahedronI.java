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

import com.hellblazer.delaunay.Vertex.IntType;

/**
 * @author hal.hildebrand
 */
public class TetrahedronI extends Tetrahedron<Vertex.IntType> {

    public TetrahedronI(Vertex<IntType> x, Vertex<IntType> y, Vertex<IntType> z, Vertex<IntType> w) {
        super(x, y, z, w);
    }

    public TetrahedronI(Vertex<IntType>[] vertices) {
        super(vertices);
    }

    @Override
    Tetrahedron<IntType> newT0(Vertex<IntType> n) {
        return new TetrahedronI(a, b, c, n);
    }

    @Override
    Tetrahedron<IntType> newT1(Vertex<IntType> n) {
        return new TetrahedronI(a, d, b, n);
    }

    @Override
    Tetrahedron<IntType> newT2(Vertex<IntType> n) {
        return new TetrahedronI(a, c, d, n);
    }

    @Override
    Tetrahedron<IntType> newT3(Vertex<IntType> n) {
        return new TetrahedronI(b, d, c, n);
    }

    @Override
    Tetrahedron<IntType> newTet(Vertex<IntType> a, Vertex<IntType> b, Vertex<IntType> c, Vertex<IntType> d) {
        return new TetrahedronI(a, b, c, d);
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
    void traverseVoronoiFace(Tetrahedron<Vertex.IntType> origin, Tetrahedron<Vertex.IntType> from,
                             Vertex<Vertex.IntType> vC, Vertex<Vertex.IntType> axis, List<Point3f> face) {
        if (origin == this) {
            return;
        }
        double[] center = new double[3];
        centerSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(), b.vertices().getX(),
                     b.vertices().getY(), b.vertices().getZ(), c.vertices().getX(), c.vertices().getY(),
                     c.vertices().getZ(), d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V next = VORONOI_FACE_NEXT[ordinalOf(from).ordinal()][ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron<Vertex.IntType> t = getNeighbor(next);
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
    void traverseVoronoiFace(Vertex<Vertex.IntType> vC, Vertex<Vertex.IntType> axis, List<Point3f[]> faces) {
        ArrayList<Point3f> face = new ArrayList<>();
        double[] center = new double[3];
        centerSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(), b.vertices().getX(),
                     b.vertices().getY(), b.vertices().getZ(), c.vertices().getX(), c.vertices().getY(),
                     c.vertices().getZ(), d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V v = VORONOI_FACE_ORIGIN[ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron<Vertex.IntType> next = getNeighbor(v);
        if (next != null) {
            next.traverseVoronoiFace(this, this, vC, axis, face);
        }
        faces.add(face.toArray(new Point3f[face.size()]));
    }

}
