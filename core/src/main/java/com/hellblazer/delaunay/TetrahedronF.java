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

import com.hellblazer.delaunay.Vertex.FloatType;

/**
 * @author hal.hildebrand
 */
public class TetrahedronF extends Tetrahedron<Vertex.FloatType> {

    public TetrahedronF(Vertex<FloatType> x, Vertex<FloatType> y, Vertex<FloatType> z, Vertex<FloatType> w) {
        super(x, y, z, w);
    }

    public TetrahedronF(Vertex<FloatType>[] vertices) {
        super(vertices);
    }

    @Override
    Tetrahedron<FloatType> newT0(Vertex<FloatType> n) {
        return new TetrahedronF(a, b, c, n);
    }

    @Override
    Tetrahedron<FloatType> newT1(Vertex<FloatType> n) {
        return new TetrahedronF(a, d, b, n);
    }

    @Override
    Tetrahedron<FloatType> newT2(Vertex<FloatType> n) {
        return new TetrahedronF(a, c, d, n);
    }

    @Override
    Tetrahedron<FloatType> newT3(Vertex<FloatType> n) {
        return new TetrahedronF(b, d, c, n);
    }

    @Override
    Tetrahedron<FloatType> newTet(Vertex<FloatType> a, Vertex<FloatType> b, Vertex<FloatType> c, Vertex<FloatType> d) {
        return new TetrahedronF(a, b, c, d);
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
    void traverseVoronoiFace(Tetrahedron<Vertex.FloatType> origin, Tetrahedron<Vertex.FloatType> from,
                             Vertex<Vertex.FloatType> vC, Vertex<Vertex.FloatType> axis, List<Point3f> face) {
        if (origin == this) {
            return;
        }
        double[] center = new double[3];
        centerSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(), b.vertices().getX(),
                     b.vertices().getY(), b.vertices().getZ(), c.vertices().getX(), c.vertices().getY(),
                     c.vertices().getZ(), d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V next = VORONOI_FACE_NEXT[ordinalOf(from).ordinal()][ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron<Vertex.FloatType> t = getNeighbor(next);
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
    void traverseVoronoiFace(Vertex<Vertex.FloatType> vC, Vertex<Vertex.FloatType> axis, List<Point3f[]> faces) {
        ArrayList<Point3f> face = new ArrayList<>();
        double[] center = new double[3];
        centerSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(), b.vertices().getX(),
                     b.vertices().getY(), b.vertices().getZ(), c.vertices().getX(), c.vertices().getY(),
                     c.vertices().getZ(), d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V v = VORONOI_FACE_ORIGIN[ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron<Vertex.FloatType> next = getNeighbor(v);
        if (next != null) {
            next.traverseVoronoiFace(this, this, vC, axis, face);
        }
        faces.add(face.toArray(new Point3f[face.size()]));
    }

}
