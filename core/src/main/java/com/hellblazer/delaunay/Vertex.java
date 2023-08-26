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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class Vertex extends Vector3d {
    /**
     * Minimal zero
     */
    static final double       EPSILON          = Math.pow(10D, -20D);
    static final Point3d      ORIGIN           = new Point3d(0, 0, 0);
    private static final long serialVersionUID = 1L;

    public static double determinant(Tuple3d a, Tuple3d b, Tuple3d c) {
        return a.x * b.y * c.z + b.x * c.y * a.z + c.x * a.y * b.z - b.x * a.y * c.z - c.x * b.y * a.z
        - a.x * c.y * b.z;
    }

    /**
     * Create some random points in a sphere
     *
     * @param random
     * @param numberOfPoints
     * @param radius
     * @param inSphere
     * @return
     */
    public static Point3d[] getRandomPoints(Random random, int numberOfPoints, double radius, boolean inSphere) {
        double radiusSquared = radius * radius;
        Point3d ourPoints[] = new Point3d[numberOfPoints];
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

    public static double pseudoOrientation(Tuple3d a, Tuple3d b, Tuple3d c, Tuple3d d) {
        var b1 = new Point3d(b);
        b1.sub(c);
        var c1 = new Point3d(b);
        c1.sub(d);
        return determinant(a, b1, c1);
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
    public static Point3d randomPoint(Random random, double min, double max) {
        return new Point3d(random(random, min, max), random(random, min, max), random(random, min, max));
    }

    /**
     * One of the tetrahedra adjacent to the vertex
     */
    private Tetrahedron adjacent;

    Vertex(double i, double j, double k) {
        x = i;
        y = j;
        z = k;
    }

    Vertex(double i, double j, double k, double scale) {
        this(i * scale, j * scale, k * scale);
    }

    Vertex(Tuple3d p) {
        this(p.x, p.y, p.z);
    }

    public double determinant(Tuple3d b, Tuple3d c) {
        return x * b.y * c.z + b.x * c.y * z + c.x * y * b.z - b.x * y * c.z - c.x * b.y * z - x * c.y * b.z;
    }

    public final double distanceSquared(Tuple3d p1) {
        double dx, dy, dz;

        dx = x - p1.x;
        dy = y - p1.y;
        dz = z - p1.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Answer one of the adjacent tetrahedron
     *
     * @return
     */
    public final Tetrahedron getAdjacent() {
        return adjacent;
    }

    public LinkedList<OrientedFace> getEars() {
        assert adjacent != null;
        EarSet aggregator = new EarSet();
        adjacent.visitStar(this, aggregator);
        return aggregator.getEars();
    }

    /**
     * Answer the collection of neighboring vertices around the indicated vertex.
     *
     * @param v - the vertex determining the neighborhood
     * @return the collection of neighboring vertices
     */
    public Collection<Vertex> getNeighbors() {
        assert adjacent != null;

        final Set<Vertex> neighbors = new IdentitySet<>();
        adjacent.visitStar(this, (vertex, t, x, y, z) -> {
            neighbors.add(x);
            neighbors.add(y);
            neighbors.add(z);
        });
        return neighbors;
    }

    public Deque<OrientedFace> getStar() {
        assert adjacent != null;

        final Deque<OrientedFace> star = new ArrayDeque<>();
        adjacent.visitStar(this, (vertex, t, x, y, z) -> {
            star.push(t.getFace(vertex));
        });
        return star;
    }

    /**
     * Answer the faces of the voronoi region around the receiver
     *
     * @param v - the vertex of interest
     * @return the list of faces defining the voronoi region defined by the receiver
     */
    public List<Tuple3d[]> getVoronoiRegion() {
        assert adjacent != null;

        final List<Tuple3d[]> faces = new ArrayList<>();
        Set<Vertex> neighbors = new IdentitySet<>(10);
        adjacent.visitStar(this, (vertex, t, x, y, z) -> {
            if (neighbors.add(x)) {
                t.traverseVoronoiFace(this, x, faces);
            }
            if (neighbors.add(y)) {
                t.traverseVoronoiFace(this, y, faces);
            }
            if (neighbors.add(z)) {
                t.traverseVoronoiFace(this, z, faces);
            }
        });
        return faces;
    }

    /**
     * Return +1 if the receiver lies inside the sphere passing through a, b, c, and
     * d; -1 if it lies outside; and 0 if the five points are cospherical. The
     * vertices a, b, c, and d must be ordered so that they have a positive
     * orientation (as defined by {@link #orientation(Vertex, Vertex, Vertex)}), or
     * the sign of the result will be reversed.
     * <p>
     *
     * @param a , b, c, d - the points defining the sphere, in oriented order
     * @return +1 if the receiver lies inside the sphere passing through a, b, c,
     *         and d; -1 if it lies outside; and 0 if the five points are
     *         cospherical
     */

    public final int inSphere(Tuple3d a, Tuple3d b, Tuple3d c, Tuple3d d) {
        double result = Geometry.inSphere(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, d.x, d.y, d.z, x, y, z);
        if (result > 0.0) {
            return 1;
        } else if (result < 0.0) {
            return -1;
        }
        return 0;
    }

    /**
     * Answer the maximum fraction of delta displacement the receiver before a
     * topological event occurs.
     *
     * @param delta - the displacement delta of the receiver
     * @return l - l <= 1 if a topological event will occur at this + (l * delta), l
     *         > 1 if no topological event will occur
     */
    public double maxStep(Tuple3d delta) {
        double[] min = new double[] { Double.MAX_VALUE };
        var target = new Point3d();
        target.add(this, delta);
        adjacent.visitStar(this, (vertex, t, a, b, c) -> {
            var pseudo = Geometry.leftOfPlaneFast(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, x, y, z);
            var newPos = new Vertex(this);
            newPos.add(delta);
            var pseudoDelta = Geometry.leftOfPlaneFast(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, newPos.x, newPos.y,
                                                       newPos.z);
            var l = pseudo / Math.abs(pseudoDelta);
            min[0] = Math.min(l, min[0]);
        });
        return min[0];
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
    public final int orientation(Tuple3d a, Tuple3d b, Tuple3d c) {
        double result = Geometry.leftOfPlane(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, x, y, z);
        if (result > 0.0) {
            return 1;
        } else if (result < 0.0) {
            return -1;
        }
        return 0;
    }

    public double pseudoOrientation(Tuple3d b, Tuple3d c, Tuple3d d) {
        var a1 = new Point3d(this);
        a1.sub(b);
        var b1 = new Point3d(b);
        b1.sub(c);
        var c1 = new Point3d(b);
        c1.sub(d);
        return determinant(a1, b1, c1);
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + ", " + z + "}";
    }

    void freshenAdjacent(Tetrahedron tetrahedron) {
        if (adjacent == null || adjacent.isDeleted())
            adjacent = tetrahedron;
    }

    /**
     * Note one of the adjacent tetrahedron
     * <p>
     *
     * @param tetrahedron
     */
    final void setAdjacent(Tetrahedron tetrahedron) {
        adjacent = tetrahedron;
    }

}
