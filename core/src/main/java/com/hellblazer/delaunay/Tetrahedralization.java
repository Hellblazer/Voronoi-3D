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

import static com.hellblazer.delaunay.V.A;
import static com.hellblazer.delaunay.V.B;
import static com.hellblazer.delaunay.V.C;
import static com.hellblazer.delaunay.V.D;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.vecmath.Point3f;

/**
 * A Delaunay tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

abstract public class Tetrahedralization<T extends Vertex.Type> {

    /**
     * Cannonical enumeration of the vertex ordinals
     */
    public final static V[]      VERTICES = { A, B, C, D };
    /**
     * A pre-built table of all the permutations of remaining faces to check in
     * location.
     */
    private static final V[][][] ORDER    = new V[][][] { { { B, C, D }, { C, B, D }, { C, D, B }, { B, D, C },
                                                            { D, B, C }, { D, C, B } },

                                                          { { A, C, D }, { C, A, D }, { C, D, A }, { A, D, C },
                                                            { D, A, C }, { D, C, A } },

                                                          { { B, A, D }, { A, B, D }, { A, D, B }, { B, D, A },
                                                            { D, B, A }, { D, A, B } },

                                                          { { B, C, A }, { C, B, A }, { C, A, B }, { B, A, C },
                                                            { A, B, C }, { A, C, B } } };

    /**
     * The four corners of the maximally bounding tetrahedron
     */
    private final Vertex<T>[] fourCorners;

    /**
     * The last valid tetrahedron noted
     */
    private Tetrahedron<T> last;

    /**
     * A random number generator
     */
    private final Random random;

    /**
     * The number of points in this tetrahedralization
     */
    private int size = 0;

    /**
     * Construct a new tetrahedralization with the default random number generator
     */
    public Tetrahedralization() {
        this(new Random());
    }

    /**
     * Construct a tetrahedralizaion using the supplied random number generator
     *
     * @param random
     */
    public Tetrahedralization(Random random) {
        assert random != null;
        fourCorners = getFourCorners();
        this.random = random;
        final var vertices = fourCorners;
        last = newTet(vertices);
    }

    /**
     * Delete the vertex from the tetrahedralization. This algorithm is the
     * deleteInSphere algorithm from Ledoux. See "Flipping to Robustly Delete a
     * Vertex in a Delaunay Tetrahedralization", H. Ledoux, C.M. Gold and G. Baciu,
     * 2005
     * <p>
     *
     * @param v - the vertex to be deleted
     */
    public void delete(Vertex<T> v) {
        assert v != null;

        LinkedList<OrientedFace<T>> ears = getEars(v);
        while (v.getOrder() > 4) {
            for (int i = 0; i < ears.size();) {
                if (ears.get(i).flip(i, ears, v)) {
                    ears.remove(i);
                } else {
                    i++;
                }
            }
        }
        last = flip4to1(v);
        size--;
    }

    public LinkedList<OrientedFace<T>> getEars(Vertex<T> v) {
        assert v != null && v.getAdjacent() != null;
        EarSet<T> aggregator = new EarSet<T>();
        v.getAdjacent().visitStar(v, aggregator);
        return aggregator.getEars();
    }

    abstract public Vertex<T>[] getFourCorners();

    /**
     * Answer the collection of neighboring vertices around the indicated vertex.
     *
     * @param v - the vertex determining the neighborhood
     * @return the collection of neighboring vertices
     */
    public Collection<Vertex<T>> getNeighbors(Vertex<T> v) {
        assert v != null && v.getAdjacent() != null;

        final Set<Vertex<T>> neighbors = new IdentitySet<>();
        v.getAdjacent().visitStar(v, (V vertex, Tetrahedron<T> t, Vertex<T> x, Vertex<T> y, Vertex<T> z) -> {
            neighbors.add(x);
            neighbors.add(y);
            neighbors.add(z);
        });
        return neighbors;
    }

    public Deque<OrientedFace<T>> getStar(Vertex<T> v) {
        assert v != null && v.getAdjacent() != null;

        final Deque<OrientedFace<T>> star = new ArrayDeque<>();
        v.getAdjacent().visitStar(v, (V vertex, Tetrahedron<T> t, Vertex<T> x, Vertex<T> y, Vertex<T> z) -> {
            star.push(t.getFace(vertex));
        });
        return star;
    }

    /**
     * Answer the set of all tetrahedrons in this tetrahedralization
     *
     * @return
     */
    public Set<Tetrahedron<T>> getTetrahedrons() {
        Set<Tetrahedron<T>> all = new IdentitySet<>(size);
        var stack = new Stack<Tetrahedron<T>>();
        stack.push(last);
        while (!stack.isEmpty()) {
            var next = stack.pop();
            if (all.add(next)) {
                next.children(stack, all);
            }
        }
        return all;
    }

    /**
     * Answer the four corners of the universe
     *
     * @return
     */
    public Vertex<T>[] getUniverse() {
        return fourCorners;
    }

    /**
     * Answer the set of all vertices in this tetrahedralization
     *
     * @return
     */
    public Set<Vertex<T>> getVertices() {
        Set<Tetrahedron<T>> tetrahedrons = new IdentitySet<>(size);
        Set<Vertex<T>> vertices = new IdentitySet<Vertex<T>>(size);
        var stack = new Stack<Tetrahedron<T>>();
        stack.push(last);
        while (!stack.isEmpty()) {
            var next = stack.pop();
            if (tetrahedrons.add(next)) {
                vertices.add(next.getA());
                vertices.add(next.getB());
                vertices.add(next.getC());
                vertices.add(next.getD());
                next.children(stack, tetrahedrons);
            }
        }
        return vertices;
    }

    /**
     * Answer the faces of the voronoi region around the vertex
     *
     * @param v - the vertex of interest
     * @return the list of faces defining the voronoi region defined by v
     */
    public List<Point3f[]> getVoronoiRegion(final Vertex<T> v) {
        assert v != null && v.getAdjacent() != null;

        final ArrayList<Point3f[]> faces = new ArrayList<>();
        Set<Vertex<T>> neighbors = new IdentitySet<>(10);
        v.getAdjacent().visitStar(v, (V vertex, Tetrahedron<T> t, Vertex<T> x, Vertex<T> y, Vertex<T> z) -> {
            if (neighbors.add(x)) {
                t.traverseVoronoiFace(v, x, faces);
            }
            if (neighbors.add(y)) {
                t.traverseVoronoiFace(v, y, faces);
            }
            if (neighbors.add(z)) {
                t.traverseVoronoiFace(v, z, faces);
            }
        });
        return faces;
    }

    /**
     * Insert the vertex into the tetrahedralization. See "Computing the 3D Voronoi
     * Diagram Robustly: An Easy Explanation", by Hugo Ledoux
     * <p>
     *
     * @param v - the vertex to be inserted
     */
    public void insert(Vertex<T> v) {
        assert v != null;
        v.reset();
        List<OrientedFace<T>> ears = new ArrayList<>();
        last = locate(v).flip1to4(v, ears);
        while (!ears.isEmpty()) {
            Tetrahedron<T> l = ears.remove(ears.size() - 1).flip(v, ears);
            if (l != null) {
                last = l;
            }
        }
        size++;
    }

    /**
     * Locate the tetrahedron which contains the query point via a stochastic walk
     * through the delaunay triangulation. This location algorithm is a slight
     * variation of the 3D jump and walk algorithm found in: "Fast randomized point
     * location without preprocessing in two- and three-dimensional Delaunay
     * triangulations", Computational Geometry 12 (1999) 63-83.
     * <p>
     * In this variant, the intial "random" triangle used is simply the one of the
     * triangles in the last tetrahedron created by a flip, or the previously
     * located tetrahedron.
     * <p>
     * This location algorithm provides fast location results with no memory
     * overhead. Further, because there is no search structure to maintain, this
     * algorithm is ideally suited for incremental deletions and kinetic maintenance
     * of the delaunay tetrahedralization.
     * <p>
     *
     * @param query - the query point
     * @return
     */
    public Tetrahedron<T> locate(Vertex<T> query) {
        assert query != null;

        V o = null;
        for (V face : Tetrahedralization.VERTICES) {
            if (last.orientationWrt(face, query) < 0) {
                o = face;
                break;
            }
        }
        if (o == null) {
            // The query point is contained in the receiver
            return last;
        }
        Tetrahedron<T> current = last;
        while (true) {
            // get the tetrahedron on the other side of the face
            Tetrahedron<T> tetrahedron = current.getNeighbor(o);
            int i = 0;
            for (V v : Tetrahedralization.ORDER[tetrahedron.ordinalOf(current).ordinal()][random.nextInt(6)]) {
                o = v;
                current = tetrahedron;
                if (tetrahedron.orientationWrt(v, query) < 0) {
                    // we have found a face which the query point is on the other side
                    break;
                }
                if (i++ == 2) {
                    last = tetrahedron;
                    return last;
                }
            }
        }
    }

    /**
     * Construct a Tetrahedron which is set up to encompass the numerical span
     *
     * @return
     */
    public Tetrahedron<T> myOwnPrivateIdaho() {
        @SuppressWarnings("unchecked")
        Vertex<T>[] U = new Vertex[4];
        int i = 0;
        for (Vertex<T> v : fourCorners) {
            U[i++] = v;
        }
        return newTet(U);
    }

    /**
     * Traverse all the tetrahedrons in the tetrahedralization. The set of
     * tetrahedons will be filled with all the tetrahedrons and the set of vertices
     * will be filled with all the vertices defining the tetrahedralization.
     * <p>
     *
     * @param tetrahedrons
     * @param vertices
     */
    public void traverse(Set<Tetrahedron<T>> tetrahedrons, Set<Vertex<T>> vertices) {
        var stack = new Stack<Tetrahedron<T>>();
        stack.push(last);
        while (!stack.isEmpty()) {
            var next = stack.pop();
            if (tetrahedrons.add(next)) {
                vertices.add(next.getA());
                vertices.add(next.getB());
                vertices.add(next.getC());
                vertices.add(next.getD());
                next.children(stack, tetrahedrons);
            }
        }
    }

    /**
     * Perform the 4->1 bistellar flip. This flip is the inverse of the 1->4 flip.
     *
     * @param n - the vertex who's star defines the 4 tetrahedron
     *
     * @return the tetrahedron created from the flip
     */
    protected Tetrahedron<T> flip4to1(Vertex<T> n) {
        Deque<OrientedFace<T>> star = getStar(n);
        ArrayList<Tetrahedron<T>> deleted = new ArrayList<>();
        for (OrientedFace<T> f : star) {
            deleted.add(f.getIncident());
        }
        assert star.size() == 4;
        OrientedFace<T> base = star.pop();
        Vertex<T> a = base.getVertex(2);
        Vertex<T> b = base.getVertex(0);
        Vertex<T> c = base.getVertex(1);
        Vertex<T> d = null;
        OrientedFace<T> face = star.pop();
        for (Vertex<T> v : face) {
            if (!base.includes(v)) {
                d = v;
                break;
            }
        }
        assert d != null;
        Tetrahedron<T> t = newTet(a, b, c, d);
        base.getIncident().patch(base.getIncidentVertex(), t, D);
        if (face.includes(a)) {
            if (face.includes(b)) {
                assert !face.includes(c);
                face.getIncident().patch(face.getIncidentVertex(), t, C);
                face = star.pop();
                if (face.includes(a)) {
                    assert !face.includes(b);
                    face.getIncident().patch(face.getIncidentVertex(), t, B);
                    face = star.pop();
                    assert !face.includes(a);
                    face.getIncident().patch(face.getIncidentVertex(), t, A);
                } else {
                    face.getIncident().patch(face.getIncidentVertex(), t, A);
                    face = star.pop();
                    assert !face.includes(b);
                    face.getIncident().patch(face.getIncidentVertex(), t, B);
                }
            } else {
                face.getIncident().patch(face.getIncidentVertex(), t, B);
                face = star.pop();
                if (face.includes(a)) {
                    assert !face.includes(c);
                    face.getIncident().patch(face.getIncidentVertex(), t, C);
                    face = star.pop();
                    assert !face.includes(a);
                    face.getIncident().patch(face.getIncidentVertex(), t, A);
                } else {
                    face.getIncident().patch(face.getIncidentVertex(), t, A);
                    face = star.pop();
                    assert !face.includes(c);
                    face.getIncident().patch(face.getIncidentVertex(), t, C);
                }
            }
        } else {
            face.getIncident().patch(face.getIncidentVertex(), t, A);
            face = star.pop();
            if (face.includes(b)) {
                assert !face.includes(c);
                face.getIncident().patch(face.getIncidentVertex(), t, C);
                face = star.pop();
                assert !face.includes(b);
                face.getIncident().patch(face.getIncidentVertex(), t, B);
            } else {
                face.getIncident().patch(face.getIncidentVertex(), t, B);
                face = star.pop();
                assert !face.includes(c);
                face.getIncident().patch(face.getIncidentVertex(), t, C);
            }
        }

        for (Tetrahedron<T> tet : deleted) {
            tet.delete();
        }
        return t;
    }

    abstract Tetrahedron<T> newTet(Vertex<T> a, Vertex<T> b, Vertex<T> c, Vertex<T> d);

    abstract Tetrahedron<T> newTet(Vertex<T>[] vertices);
}
