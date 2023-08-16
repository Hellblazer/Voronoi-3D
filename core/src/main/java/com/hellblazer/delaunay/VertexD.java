/**
 * Copyright (C) 2009 Hal Hildebrand. All rights reserved.
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

package com.hellblazer.delaunay;

import java.util.Random;

import javax.vecmath.Point3f;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class VertexD implements Vertex<Vertex.DoubleType> {
    /**
     * Minimal zero
     */
    static final double  EPSILON = Math.pow(10D, -20D);
    static final VertexD ORIGIN  = new VertexD(0, 0, 0);

    /**
     * Create some random points in a sphere
     *
     * @param random
     * @param numberOfPoints
     * @param radius
     * @param inSphere
     * @return
     */
    public static Vertex<DoubleType>[] getRandomPoints(Random random, int numberOfPoints, double radius,
                                                       boolean inSphere) {
        double radiusSquared = radius * radius;
        VertexD ourPoints[] = new VertexD[numberOfPoints];
        for (int i = 0; i < ourPoints.length; i++) {
            if (inSphere) {
                do {
                    ourPoints[i] = randomPoint(random, -radius, radius);
                } while (ourPoints[i].distanceSquared(ORIGIN) >= radiusSquared);
            } else {
                ourPoints[i] = randomPoint(random, -radius, radius);
            }
        }

        return ourPoints;
    }

    /**
     * Generate a bounded random double
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static double random(Random random, double min, double max) {
        double result = random.nextDouble();
        if (min > max) {
            result *= min - max;
            result += max;
        } else {
            result *= max - min;
            result += min;
        }
        return result;
    }

    /**
     * Generate a random point
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static VertexD randomPoint(Random random, double min, double max) {
        return new VertexD(random(random, min, max), random(random, min, max), random(random, min, max));
    }

    public final double x;
    public final double y;
    public final double z;

    /**
     * One of the tetrahedra adjacent to the vertex
     */
    private Tetrahedron<Vertex.DoubleType> adjacent;

    /**
     * The number of tetrahedra adjacent to the vertex
     */
    private int order = 0;

    public VertexD(double i, double j, double k) {
        x = i;
        y = j;
        z = k;
    }

    public VertexD(double i, double j, double k, double scale) {
        this(i * scale, j * scale, k * scale);
    }

    @Override
    public Point3f asPoint3f() {
        return new Point3f((float) x, (float) y, (float) z);
    }

    /**
     * Account for the deletion of an adjacent tetrahedron.
     */
    @Override
    public final void deleteAdjacent() {
        order--;
        assert order >= 0;
    }

    public final double distanceSquared(VertexD p1) {
        double dx, dy, dz;

        dx = x - p1.x;
        dy = y - p1.y;
        dz = z - p1.z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public void freshenAdjacent(Tetrahedron<Vertex.DoubleType> tetrahedron) {
        if (adjacent == null || adjacent.isDeleted())
            adjacent = tetrahedron;
    }

    /**
     * Answer one of the adjacent tetrahedron
     *
     * @return
     */
    @Override
    public final Tetrahedron<Vertex.DoubleType> getAdjacent() {
        return adjacent;
    }

    /**
     * Answer the number of tetrahedra adjacent to the receiver vertex in the
     * tetrahedralization
     * <p>
     *
     * @return
     */
    @Override
    public final int getOrder() {
        return order;
    }

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

    @Override
    public final int inSphere(Vertex<DoubleType> a, Vertex<DoubleType> b, Vertex<DoubleType> c, Vertex<DoubleType> d) {
        double result = Geometry.inSphere(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(),
                                          b.vertices().getX(), b.vertices().getY(), b.vertices().getZ(),
                                          c.vertices().getX(), c.vertices().getY(), c.vertices().getZ(),
                                          d.vertices().getX(), d.vertices().getY(), d.vertices().getZ(), x, y, z);
        if (result > 0.0) {
            return 1;
        } else if (result < 0.0) {
            return -1;
        }
        return 0;

    }

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
    @Override
    public final int orientation(Vertex<DoubleType> a, Vertex<DoubleType> b, Vertex<DoubleType> c) {
        double result = Geometry.leftOfPlane(a.vertices().getX(), a.vertices().getY(), a.vertices().getZ(),
                                             b.vertices().getX(), b.vertices().getY(), b.vertices().getZ(),
                                             c.vertices().getX(), c.vertices().getY(), c.vertices().getZ(), x, y, z);
        if (result > 0.0) {
            return 1;
        } else if (result < 0.0) {
            return -1;
        }
        return 0;
    }

    /**
     * Reset the state associated with a tetrahedralization.
     */
    @Override
    public final void reset() {
        adjacent = null;
        order = 0;
    }

    /**
     * Note one of the adjacent tetrahedron
     * <p>
     *
     * @param tetrahedron
     */
    @Override
    public final void setAdjacent(Tetrahedron<Vertex.DoubleType> tetrahedron) {
        order++;
        adjacent = tetrahedron;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + ", " + z + "}";
    }

    @Override
    public DoubleType vertices() {
        return new DoubleType() {

            @Override
            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }

            @Override
            public double getZ() {
                return z;
            }
        };
    }
}