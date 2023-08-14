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

import javax.vecmath.Point3f;

/**
 * 
 */
public interface Vertex<T extends Vertex.Type> {

    interface DoubleType extends Type {
        double getX();

        double getY();

        double getZ();
    }

    interface FloatType extends Type {
        float getX();

        float getY();

        float getZ();
    }

    interface IntType extends Type {
        int getX();

        int getY();

        int getZ();
    }

    interface LongType extends Type {

        long getX();

        long getY();

        long getZ();
    }

    interface Type {
    }

    Point3f asPoint3f();

    /**
     * Account for the deletion of an adjacent tetrahedron.
     */
    void deleteAdjacent();

    void freshenAdjacent(Tetrahedron<T> tetrahedron);

    /**
     * Answer one of the adjacent tetrahedron
     *
     * @return
     */
    Tetrahedron<T> getAdjacent();

    /**
     * Answer the number of tetrahedra adjacent to the receiver vertex in the
     * tetrahedralization
     * <p>
     *
     * @return
     */
    int getOrder();

    /**
     * Return +1 if the receiver lies inside the sphere passing through a, b, c, and
     * d; -1 if it lies outside; and 0 if the five points are cospherical. The
     * vertices a, b, c, and d must be ordered so that they have a positive
     * orientation (as defined by {@link #orientation(VertexD, VertexD, VertexD)}),
     * or the sign of the result will be reversed.
     * <p>
     *
     * @param a , b, c, d - the points defining the sphere, in oriented order
     * @return +1 if the receiver lies inside the sphere passing through a, b, c,
     *         and d; -1 if it lies outside; and 0 if the five points are
     *         cospherical
     */
    int inSphere(Vertex<T> a, Vertex<T> b, Vertex<T> c, Vertex<T> d);

    /**
     * Answer +1 if the orientation of the receiver is positive with respect to the
     * plane defined by {a, b, c}, -1 if negative, or 0 if the test point is
     * coplanar
     * <p>
     *
     * @param a , b, c - the points defining the plane
     * @return +1 if the orientation of the query point is positive with respect to
     *         the plane, -1 if negative and 0 if the test point is coplanar
     */
    int orientation(Vertex<T> a, Vertex<T> b, Vertex<T> c);

    /**
     * Reset the state associated with a tetrahedralization.
     */
    void reset();

    /**
     * Note one of the adjacent tetrahedron
     * <p>
     *
     * @param tetrahedron
     */
    void setAdjacent(Tetrahedron<T> tetrahedron);

    T vertices();

}
