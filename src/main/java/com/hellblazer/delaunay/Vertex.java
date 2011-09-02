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
public class Vertex {
    public class CircumSphere {
        private double x;
        private double y;
        private double z;
        private double radiusSquared;

        public CircumSphere(double i, double j, double k, double rSquared) {
            x = i;
            y = j;
            z = k;
            radiusSquared = rSquared;
        }

        public final double distanceSquared(Vertex p1) {
            double dx, dy, dz;

            dx = x - p1.x;
            dy = y - p1.y;
            dz = z - p1.z;
            return dx * dx + dy * dy + dz * dz;
        }

        /**
         * Answer true if the query vertex is contained in the circumsphere
         * 
         * @param query
         * @return
         */
        public final boolean inSphere(Vertex query) {
            return distanceSquared(query) <= radiusSquared;
        }

        /**
         * Answer the center point of the sphere as a Point3f
         * 
         * @return
         */
        Point3f asPoint3f() {
            return new Point3f((float) x, (float) y, (float) z);
        }

    }

    private double x;
    private double y;
    private double z;

    /**
     * One of the tetrahedra adjacent to the vertex
     */
    private Tetrahedron adjacent;

    /**
     * The number of tetrahedra adjacent to the vertex
     */
    private int order = 0;

    /**
     * Minimal zero
     */
    static final double EPSILON = Math.pow(10D, -20D);

    static final Vertex ORIGIN = new Vertex(0, 0, 0);

    /**
     * Create some random points in a sphere
     * 
     * @param random
     * @param numberOfPoints
     * @param radius
     * @param inSphere
     * @return
     */
    public static Vertex[] getRandomPoints(Random random, int numberOfPoints,
                                           double radius, boolean inSphere) {
        double radiusSquared = radius * radius;
        Vertex ourPoints[] = new Vertex[numberOfPoints];
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
    public static Vertex randomPoint(Random random, double min, double max) {
        return new Vertex(random(random, min, max), random(random, min, max),
                          random(random, min, max));
    }

    public Vertex(double i, double j, double k) {
        x = i;
        y = j;
        z = k;
    }

    public Point3f asPoint3f() {
        return new Point3f((float) x, (float) y, (float) z);
    }

    /**
     * Determine the circumsphere of the tetrahedron formed by the receiver and
     * the plane defined by {b, c, d}
     * <p>
     * 
     * @param b
     * @param c
     * @param d
     * @return the circumsphere defined by the tetrahedron
     */
    public final CircumSphere createCircumSphere(Vertex b, Vertex c, Vertex d) {
        assert d.orientation(this, b, c) > 0;

        /**
         * This is a highly optimized version of the nasty matrix calculation.
         * All values are calculated once.
         */
        double xSquaredLength = x * x + y * y + z * z;
        double ySquaredLength = b.x * b.x + b.y * b.y + b.z * b.z;
        double zSquaredLength = c.x * c.x + c.y * c.y + c.z * c.z;
        double wSquaredLength = d.x * d.x + d.y * d.y + d.z * d.z;

        double yX = b.x;
        double yY = b.y;
        double yZ = b.z;

        double zX = c.x;
        double zY = c.y;
        double zZ = c.z;

        double wX = d.x;
        double wY = d.y;
        double wZ = d.z;

        double xXyY = x * yY;
        double xYzX = y * zX;
        double yYzX = yY * zX;
        double xYyX = y * yX;
        double xXzY = x * zY;
        double xXyZ = x * yZ;
        double xZzX = z * zX;
        double yZzX = yZ * zX;
        double xZyX = z * yX;
        double xXzZ = x * zZ;
        double xYyZ = y * yZ;
        double xZzY = z * zY;
        double yYzZ = yY * zZ;
        double yZzY = yZ * zY;
        double xZyY = z * yY;
        double xYzZ = y * zZ;

        double a1 = xYyZ + xZzY + yYzZ - yZzY - xZyY - xYzZ;
        double a2 = xXyZ + xZzX + yX * zZ - yZzX - xZyX - xXzZ;
        double a3 = xXyY + xYzX + yX * zY - yYzX - xYyX - xXzY;
        double a4 = x * yY * zZ + y * yZ * zX + z * yX * zY - z * yY * zX - y
                    * yX * zZ - x * yZ * zY;

        double detM11 = a4 - wZ * a3 + wY * a2 - wX * a1;

        assert !(detM11 <= EPSILON && detM11 >= -EPSILON);

        double a5 = yX * xSquaredLength;
        double a6 = a5 * zY;
        double a7 = yYzZ * xSquaredLength + xYyZ * zSquaredLength + xZzY
                    * ySquaredLength - xZyY * zSquaredLength - xYzZ
                    * ySquaredLength - yZzY * xSquaredLength;
        double a8 = yY * xSquaredLength + y * zSquaredLength + zY
                    * ySquaredLength - yY * zSquaredLength - y * ySquaredLength
                    - zY * xSquaredLength;
        double a9 = yZ * xSquaredLength + z * zSquaredLength + zZ
                    * ySquaredLength - yZ * zSquaredLength - z * ySquaredLength
                    - zZ * xSquaredLength;
        double a10 = a5 * zZ + xXyZ * zSquaredLength + xZzX * ySquaredLength
                     - xZyX * zSquaredLength - xXzZ * ySquaredLength - yZzX
                     * xSquaredLength;
        double a11 = a5 + x * zSquaredLength + zX * ySquaredLength - yX
                     * zSquaredLength - x * ySquaredLength - zX
                     * xSquaredLength;
        double a12 = a6 + xXyY * zSquaredLength + xYzX * ySquaredLength - xYyX
                     * zSquaredLength - xXzY * ySquaredLength - yYzX
                     * xSquaredLength;

        double i = (a7 - wZ * a8 + wY * a9 - wSquaredLength * a1) / detM11 / 2;
        double j = (a10 - wZ * a11 + wX * a9 - wSquaredLength * a2) / detM11
                   / -2;
        double k = (a12 - wY * a11 + wX * a8 - wSquaredLength * a3) / detM11
                   / 2;
        double radiusSquared = i
                               * i
                               + j
                               * j
                               + k
                               * k
                               - (wZ * a12 - wY * a10 + wX * a7 - wSquaredLength
                                                                  * a4)
                               / detM11;
        CircumSphere sphere = new CircumSphere(i, j, k, radiusSquared);
        return sphere;
    }

    /**
     * Account for the deletion of an adjacent tetrahedron.
     */
    public final void deleteAdjacent() {
        order--;
        assert order >= 0;
    }

    public final int det1(Vertex a, Vertex b, Vertex c) {
        double det = (b.y * c.z - b.z * c.y) * (x - a.x)
                     + (b.x * c.z - b.z * c.x) * (a.y - y)
                     + (b.x * c.y - b.y * c.x) * (z - a.z)
                     + (y * a.z - z * a.y) * (b.x - c.x) + (x * a.z - z * a.x)
                     * (c.y - b.y) + (x * a.y - y * a.x) * (b.z - c.z);
        if (det <= Math.abs(EPSILON) && det >= -Math.abs(EPSILON)) {
            return 0;
        } else if (det < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public final int det2(Vertex a, Vertex b, Vertex c) {

        double adx = a.x - x;
        double bdx = b.x - x;
        double cdx = c.x - x;
        double ady = a.y - y;
        double bdy = b.y - y;
        double cdy = c.y - y;
        double adz = a.z - z;
        double bdz = b.z - z;
        double cdz = c.z - z;

        double det = adx * (bdy * cdz - bdz * cdy) + bdx
                     * (cdy * adz - cdz * ady) + cdx * (ady * bdz - adz * bdy);

        if (det <= Math.abs(EPSILON) && det >= -Math.abs(EPSILON)) {
            return 0;
        } else if (det < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public final double distanceSquared(Vertex p1) {
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

    /**
     * Answer the number of tetrahedra adjacent to the receiver vertex in the
     * tetrahedralization
     * <p>
     * 
     * @return
     */
    public final int getOrder() {
        return order;
    }

    /**
     * Return +1 if the receiver lies inside the sphere passing through a, b, c,
     * and d; -1 if it lies outside; and 0 if the five points are cospherical.
     * The vertices a, b, c, and d must be ordered so that they have a positive
     * orientation (as defined by {@link #orientation(Vertex, Vertex, Vertex)}),
     * or the sign of the result will be reversed.
     * <p>
     * 
     * @param a
     *            , b, c, d - the points defining the sphere, in oriented order
     * @return +1 if the receiver lies inside the sphere passing through a, b,
     *         c, and d; -1 if it lies outside; and 0 if the five points are
     *         cospherical
     */

    public final int inSphere(Vertex a, Vertex b, Vertex c, Vertex d) {

        double aex = a.x - x;
        double bex = b.x - x;
        double cex = c.x - x;
        double dex = d.x - x;

        double aey = a.y - y;
        double bey = b.y - y;
        double cey = c.y - y;
        double dey = d.y - y;

        double aez = a.z - z;
        double bez = b.z - z;
        double cez = c.z - z;
        double dez = d.z - z;

        double ab = aex * bey - bex * aey;
        double bc = bex * cey - cex * bey;
        double cd = cex * dey - dex * cey;
        double da = dex * aey - aex * dey;

        double ac = aex * cey - cex * aey;
        double bd = bex * dey - dex * bey;

        double abc = aez * bc - bez * ac + cez * ab;
        double bcd = bez * cd - cez * bd + dez * bc;
        double cda = cez * da + dez * ac + aez * cd;
        double dab = dez * ab + aez * bd + bez * da;

        double alift = aex * aex + aey * aey + aez * aez;
        double blift = bex * bex + bey * bey + bez * bez;
        double clift = cex * cex + cey * cey + cez * cez;
        double dlift = dex * dex + dey * dey + dez * dez;

        double det = dlift * abc - clift * dab + blift * cda - alift * bcd;

        if (det <= Math.abs(EPSILON) && det >= -Math.abs(EPSILON)) {
            return 0;
        } else if (det < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Answer +1 if the orientation of the receiver is positive with respect to
     * the plane defined by {a, b, c}, -1 if negative, or 0 if the test point is
     * coplanar
     * <p>
     * 
     * @param a
     *            , b, c - the points defining the plane
     * @return +1 if the orientation of the query point is positive with respect
     *         to the plane, -1 if negative and 0 if the test point is coplanar
     */
    public final int orientation(Vertex a, Vertex b, Vertex c) {
        return det1(a, b, c);
    }

    /**
     * Reset the state associated with a tetrahedralization.
     */
    public final void reset() {
        adjacent = null;
        order = 0;
    }

    public final void scale(double s) {
        x *= s;
        y *= s;
        z *= s;
    }

    /**
     * Note one of the adjacent tetrahedron
     * <p>
     * 
     * @param tetrahedron
     */
    public final void setAdjacent(Tetrahedron tetrahedron) {
        order++;
        adjacent = tetrahedron;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + ", " + z + "}";
    }

}