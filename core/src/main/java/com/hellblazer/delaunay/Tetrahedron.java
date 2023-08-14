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

import static com.hellblazer.delaunay.Geometry.centerSphere;
import static com.hellblazer.delaunay.V.A;
import static com.hellblazer.delaunay.V.B;
import static com.hellblazer.delaunay.V.C;
import static com.hellblazer.delaunay.V.D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.vecmath.Point3f;

/**
 * An oriented, delaunay tetrahedral cell. The vertices of the tetrahedron are
 * A, B, C and D. The vertices {A, B, C} are positively oriented with respect to
 * the fourth vertex D.
 * <p>
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class Tetrahedron implements Iterable<OrientedFace> {
    /**
     * Represents the oriented face opposite vertex C
     *
     * @author hhildebrand
     *
     */
    private class FaceADB extends OrientedFace {

        @Override
        public Tetrahedron getAdjacent() {
            return nC;
        }

        @Override
        public VertexD[] getEdge(VertexD v) {
            switch (ordinalOf(v)) {
            case A: {
                return new VertexD[] { d, b };
            }
            case D: {
                return new VertexD[] { b, a };
            }
            case B: {
                return new VertexD[] { a, d };
            }
            default:
                throw new IllegalArgumentException("Invalid vertex ordinal");
            }
        }

        @Override
        public Tetrahedron getIncident() {
            return Tetrahedron.this;
        }

        @Override
        public VertexD getIncidentVertex() {
            return c;
        }

        @Override
        public VertexD getVertex(int v) {
            switch (v) {
            case 0:
                return a;
            case 1:
                return d;
            case 2:
                return b;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + v);
            }
        }

        @Override
        public boolean includes(VertexD v) {
            if ((a == v) || (d == v) || (b == v)) {
                return true;
            }
            return false;
        }

        @Override
        public int indexOf(VertexD v) {
            if (v == a) {
                return 0;
            }
            if (v == d) {
                return 1;
            }
            if (v == b) {
                return 2;
            }
            throw new IllegalArgumentException("Vertex is not on face: " + v);
        }

        @Override
        public boolean isConvex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }
            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(c, d, b) == -1;
            case 1:
                return adjacentVertex.orientation(a, c, b) == -1;
            case 2:
                return adjacentVertex.orientation(a, d, c) == -1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public boolean isReflex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }
            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(c, d, b) == 1;
            case 1:
                return adjacentVertex.orientation(a, c, b) == 1;
            case 2:
                return adjacentVertex.orientation(a, d, c) == 1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public int orientationOf(VertexD query) {
            return orientationWrtADB(query);
        }

        @Override
        public String toString() {
            return "Face ADB";
        }

    }

    /**
     * Represents the oriented face opposite of vertex D
     *
     * @author hhildebrand
     *
     */
    private class FaceBCA extends OrientedFace {

        @Override
        public Tetrahedron getAdjacent() {
            return nD;
        }

        @Override
        public VertexD[] getEdge(VertexD v) {
            switch (ordinalOf(v)) {
            case B: {
                return new VertexD[] { c, a };
            }
            case C: {
                return new VertexD[] { a, b };
            }
            case A: {
                return new VertexD[] { b, c };
            }
            default:
                throw new IllegalArgumentException("Invalid vertex ordinal");
            }
        }

        @Override
        public Tetrahedron getIncident() {
            return Tetrahedron.this;
        }

        @Override
        public VertexD getIncidentVertex() {
            return d;
        }

        @Override
        public VertexD getVertex(int v) {
            switch (v) {
            case 0:
                return b;
            case 1:
                return c;
            case 2:
                return a;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + v);
            }
        }

        @Override
        public boolean includes(VertexD v) {
            if ((b == v) || (c == v) || (a == v)) {
                return true;
            }
            return false;
        }

        @Override
        public int indexOf(VertexD v) {
            if (v == b) {
                return 0;
            }
            if (v == c) {
                return 1;
            }
            if (v == a) {
                return 2;
            }
            throw new IllegalArgumentException("Vertex is not on face: " + v);
        }

        @Override
        public boolean isConvex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }

            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(d, c, a) == -1;
            case 1:
                return adjacentVertex.orientation(b, d, a) == -1;
            case 2:
                return adjacentVertex.orientation(b, c, d) == -1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public boolean isReflex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }

            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(d, c, a) == 1;
            case 1:
                return adjacentVertex.orientation(b, d, a) == 1;
            case 2:
                return adjacentVertex.orientation(b, c, d) == 1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public int orientationOf(VertexD query) {
            return orientationWrtBCA(query);
        }

        @Override
        public String toString() {
            return "Face BCA";
        }

    }

    /**
     * Represents the oriented face opposite of vertex A
     *
     * @author hhildebrand
     *
     */
    private class FaceCBD extends OrientedFace {

        @Override
        public Tetrahedron getAdjacent() {
            return nA;
        }

        @Override
        public VertexD[] getEdge(VertexD v) {
            switch (ordinalOf(v)) {
            case C: {
                return new VertexD[] { b, d };
            }
            case B: {
                return new VertexD[] { d, c };
            }
            case D: {
                return new VertexD[] { c, b };
            }
            default:
                throw new IllegalArgumentException("Invalid vertex ordinal");
            }
        }

        @Override
        public Tetrahedron getIncident() {
            return Tetrahedron.this;
        }

        @Override
        public VertexD getIncidentVertex() {
            return a;
        }

        @Override
        public VertexD getVertex(int v) {
            switch (v) {
            case 0:
                return c;
            case 1:
                return b;
            case 2:
                return d;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + v);
            }
        }

        @Override
        public boolean includes(VertexD v) {
            if ((c == v) || (b == v) || (d == v)) {
                return true;
            }
            return false;
        }

        @Override
        public int indexOf(VertexD v) {
            if (v == c) {
                return 0;
            }
            if (v == b) {
                return 1;
            }
            if (v == d) {
                return 2;
            }
            throw new IllegalArgumentException("Vertex is not on face: " + v);
        }

        @Override
        public boolean isConvex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }
            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(a, b, d) == -1;
            case 1:
                return adjacentVertex.orientation(c, a, d) == -1;
            case 2:
                return adjacentVertex.orientation(c, b, a) == -1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public boolean isReflex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }
            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(a, b, d) == 1;
            case 1:
                return adjacentVertex.orientation(c, a, d) == 1;
            case 2:
                return adjacentVertex.orientation(c, b, a) == 1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public int orientationOf(VertexD query) {
            return orientationWrtCBD(query);
        }

        @Override
        public String toString() {
            return "Face CBD";
        }

    }

    /**
     * Represents the oriented face opposite of vertex B
     *
     * @author hhildebrand
     *
     */
    private class FaceDAC extends OrientedFace {

        @Override
        public Tetrahedron getAdjacent() {
            return nB;
        }

        @Override
        public VertexD[] getEdge(VertexD v) {
            switch (ordinalOf(v)) {
            case D: {
                return new VertexD[] { a, c };
            }
            case A: {
                return new VertexD[] { c, d };
            }
            case C: {
                return new VertexD[] { d, a };
            }
            default:
                throw new IllegalArgumentException("Invalid vertex ordinal");
            }
        }

        @Override
        public Tetrahedron getIncident() {
            return Tetrahedron.this;
        }

        @Override
        public VertexD getIncidentVertex() {
            return b;
        }

        @Override
        public VertexD getVertex(int v) {
            switch (v) {
            case 0:
                return d;
            case 1:
                return a;
            case 2:
                return c;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + v);
            }
        }

        @Override
        public boolean includes(VertexD v) {
            if ((d == v) || (a == v) || (c == v)) {
                return true;
            }
            return false;
        }

        @Override
        public int indexOf(VertexD v) {
            if (v == d) {
                return 0;
            }
            if (v == a) {
                return 1;
            }
            if (v == c) {
                return 2;
            }
            throw new IllegalArgumentException("Vertex is not on face: " + v);
        }

        @Override
        public boolean isConvex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }
            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(b, a, c) == -1;
            case 1:
                return adjacentVertex.orientation(d, b, c) == -1;
            case 2:
                return adjacentVertex.orientation(d, a, b) == -1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public boolean isReflex(int vertex) {
            VertexD adjacentVertex = getAdjacentVertex();
            if (adjacentVertex == null) {
                return false;
            }
            switch (vertex) {
            case 0:
                return adjacentVertex.orientation(b, a, c) == 1;
            case 1:
                return adjacentVertex.orientation(d, b, c) == 1;
            case 2:
                return adjacentVertex.orientation(d, a, b) == 1;
            default:
                throw new IllegalArgumentException("Invalid vertex index: " + vertex);
            }
        }

        @Override
        public int orientationOf(VertexD query) {
            return orientationWrtDAC(query);
        }

        @Override
        public String toString() {
            return "Face DAC";
        }

    }

    /**
     * Matrix used to determine the next neighbor in a voronoi face traversal
     */
    private static final V[][][] VORONOI_FACE_NEXT = { { null, { null, null, D, C }, { null, D, null, B },
                                                         { null, C, B, null } },
                                                       { { null, null, D, C }, null, { D, null, null, A },
                                                         { C, null, A, null } },
                                                       { { null, D, null, B }, { D, null, null, A }, null,
                                                         { B, A, null, null } },
                                                       { { null, C, B, null }, { C, null, A, null },
                                                         { B, A, null, null }, null } };

    /**
     * Matrix used to determine the origin neighbor in a vororoni face traversal
     */
    private static final V[][] VORONOI_FACE_ORIGIN = { { null, C, D, B }, { C, null, D, A }, { D, A, null, B },
                                                       { B, C, A, null } };

    /**
     * Vertex A
     */
    private VertexD a;

    /**
     * Vertx B
     */
    private VertexD b;

    /**
     * Vertex C
     */
    private VertexD c;

    /**
     * Vertex D
     */
    private VertexD d;

    /**
     * The neighboring tetrahedron opposite of vertex A
     */
    private Tetrahedron nA;

    /**
     * The neighboring tetrahedron opposite of vertex B
     */
    private Tetrahedron nB;

    /**
     * The neighboring tetrahedron opposite of vertex C
     */
    private Tetrahedron nC;

    /**
     * The neighboring tetrahedron opposite of vertex D
     */
    private Tetrahedron nD;

    /**
     * Construct a tetrahedron from the four vertices
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public Tetrahedron(VertexD x, VertexD y, VertexD z, VertexD w) {
        assert x != null & y != null & z != null & w != null;

        a = x;
        b = y;
        c = z;
        d = w;

        a.setAdjacent(this);
        b.setAdjacent(this);
        c.setAdjacent(this);
        d.setAdjacent(this);
    }

    /**
     * Construct a tetrahedron from the array of four vertices
     *
     * @param vertices
     */
    public Tetrahedron(VertexD[] vertices) {
        this(vertices[0], vertices[1], vertices[2], vertices[3]);
        assert vertices.length == 4;
    }

    /**
     * Add the four faces defined by the tetrahedron to the list of faces
     *
     * @param faces
     */
    public void addFaces(List<VertexD[]> faces) {
        faces.add(new VertexD[] { a, d, b });
        faces.add(new VertexD[] { b, c, a });
        faces.add(new VertexD[] { c, b, d });
        faces.add(new VertexD[] { d, a, c });
    }

    /**
     * Add the four faces defined by the tetrahedron to the list of faces
     *
     * @param faces
     */
    public void addFacesCoordinates(List<Point3f[]> faces) {
        faces.add(new Point3f[] { a.asPoint3f(), d.asPoint3f(), b.asPoint3f() });
        faces.add(new Point3f[] { b.asPoint3f(), c.asPoint3f(), a.asPoint3f() });
        faces.add(new Point3f[] { c.asPoint3f(), b.asPoint3f(), d.asPoint3f() });
        faces.add(new Point3f[] { d.asPoint3f(), a.asPoint3f(), c.asPoint3f() });
    }

    /**
     *
     * Perform the 1 -> 4 bistellar flip. This produces 4 new tetrahedron from the
     * original tetrahdron, by inserting the new point in the interior of the
     * receiver tetrahedron. The star set of the newly inserted point is pushed onto
     * the supplied stack.
     * <p>
     *
     * @param n    - the inserted point
     * @param ears - the stack of oriented faces that make up the ears of the
     *             inserted point
     * @return one of the four new tetrahedra
     */
    public Tetrahedron flip1to4(VertexD n, List<OrientedFace> ears) {
        Tetrahedron t0 = new Tetrahedron(a, b, c, n);
        Tetrahedron t1 = new Tetrahedron(a, d, b, n);
        Tetrahedron t2 = new Tetrahedron(a, c, d, n);
        Tetrahedron t3 = new Tetrahedron(b, d, c, n);

        t0.setNeighborA(t3);
        t0.setNeighborB(t2);
        t0.setNeighborC(t1);

        t1.setNeighborA(t3);
        t1.setNeighborB(t0);
        t1.setNeighborC(t2);

        t2.setNeighborA(t3);
        t2.setNeighborB(t1);
        t2.setNeighborC(t0);

        t3.setNeighborA(t2);
        t3.setNeighborB(t0);
        t3.setNeighborC(t1);

        patch(D, t0, D);
        patch(C, t1, D);
        patch(B, t2, D);
        patch(A, t3, D);

        delete();

        OrientedFace newFace = t0.getFace(D);
        if (newFace.hasAdjacent()) {
            ears.add(newFace);
        }
        newFace = t1.getFace(D);
        if (newFace.hasAdjacent()) {
            ears.add(newFace);
        }
        newFace = t2.getFace(D);
        if (newFace.hasAdjacent()) {
            ears.add(newFace);
        }
        newFace = t3.getFace(D);
        if (newFace.hasAdjacent()) {
            ears.add(newFace);
        }
        return t1;
    }

    /**
     * Answer the oriented face of the tetrahedron
     * <p>
     *
     * @param v - the vertex opposite the face
     * @return the OrientedFace
     */
    public OrientedFace getFace(V v) {
        // return new OrientedFace(this, v);
        switch (v) {
        case A:
            return new FaceCBD();
        case B:
            return new FaceDAC();
        case C:
            return new FaceADB();
        case D:
            return new FaceBCA();
        default:
            throw new IllegalArgumentException("Invalid vertex: " + v);
        }
    }

    /**
     * Answer the oriented face opposite the vertex
     *
     * @param v
     * @return
     */
    public OrientedFace getFace(VertexD v) {
        return getFace(ordinalOf(v));
    }

    public List<Point3f[]> getFacesCoordinates() {
        var faces = new ArrayList<Point3f[]>();
        faces.add(new Point3f[] { a.asPoint3f(), d.asPoint3f(), b.asPoint3f() });
        faces.add(new Point3f[] { b.asPoint3f(), c.asPoint3f(), a.asPoint3f() });
        faces.add(new Point3f[] { c.asPoint3f(), b.asPoint3f(), d.asPoint3f() });
        faces.add(new Point3f[] { d.asPoint3f(), a.asPoint3f(), c.asPoint3f() });
        return faces;
    }

    /**
     * Answer the neighbor that is adjacent to the face opposite of the vertex
     * <p>
     *
     * @param v - the opposing vertex defining the face
     * @return the neighboring tetrahedron, or null if none.
     */
    public Tetrahedron getNeighbor(V v) {
        switch (v) {
        case A:
            return nA;
        case B:
            return nB;
        case C:
            return nC;
        case D:
            return nD;
        default:
            throw new IllegalArgumentException("Invalid opposing vertex: " + v);
        }
    }

    /**
     * Answer the neighbor that is adjacent to the face opposite of the vertex
     * <p>
     *
     * @param vertex
     * @return
     */
    public Tetrahedron getNeighbor(VertexD vertex) {
        return getNeighbor(ordinalOf(vertex));
    }

    /**
     * Answer the vertex of the tetrahedron
     *
     * @param v the vertex
     * @return the vertex
     */
    public VertexD getVertex(V v) {
        switch (v) {
        case A:
            return a;
        case B:
            return b;
        case C:
            return c;
        case D:
            return d;
        default:
            throw new IllegalStateException("No such point");
        }
    }

    /**
     * Answer the four vertices that define the tetrahedron
     *
     * @return
     */
    public VertexD[] getVertices() {
        return new VertexD[] { a, b, c, d };
    }

    public boolean includes(VertexD query) {
        return a == query || b == query || c == query || d == query;
    }

    /**
     * Answer true if the query point is contained in the circumsphere of the
     * tetrahedron
     *
     * @param query
     * @return
     */
    public boolean inSphere(VertexD query) {
        return query.inSphere(a, b, c, d) > 0;
    }

    public boolean isDeleted() {
        return a == null;
    }

    /**
     * Answer the iterator over the faces of the tetrahedron
     * <p>
     *
     * @return the iterator of the faces, in the order of the index their opposite
     *         vertex
     */
    @Override
    public Iterator<OrientedFace> iterator() {
        return new Iterator<>() {
            OrientedFace[] faces = { getFace(A), getFace(B), getFace(C), getFace(D) };
            int            i     = 0;

            @Override
            public boolean hasNext() {
                return i < 4;
            }

            @Override
            public OrientedFace next() {
                return faces[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Answer the vertex indicator of the the point
     *
     * @param v - the vertex
     * @return the indicator of this vertex or null if not a vertex of this
     *         tetrahedron or the supplied vertex is null
     */
    public V ordinalOf(VertexD v) {
        if (v == null) {
            return null;
        }
        if (v == a) {
            return A;
        } else if (v == b) {
            return B;
        } else if (v == c) {
            return C;
        } else if (v == d) {
            return D;
        } else {
            return null;
        }
    }

    /**
     * Answer 1 if the query point is positively oriented with respect to the face
     * opposite the vertex, -1 if negatively oriented, 0 if the query point is
     * coplanar to the face
     *
     * @param face
     * @param query
     * @return
     */
    public int orientationWrt(V face, VertexD query) {
        switch (face) {
        case A:
            return orientationWrtCBD(query);
        case B:
            return orientationWrtDAC(query);
        case C:
            return orientationWrtADB(query);
        case D:
            return orientationWrtBCA(query);
        default:
            throw new IllegalArgumentException("Invalid face: " + face);
        }
    }

    /**
     * Answer 1 if the query point is positively oriented with respect to the face
     * ADB, -1 if negatively oriented, 0 if the query point is coplanar to the face
     *
     * @param query
     * @return
     */
    public int orientationWrtADB(VertexD query) {
        return query.orientation(a, d, b);
    }

    /**
     * Answer 1 if the query point is positively oriented with respect to the face
     * BCA, -1 if negatively oriented, 0 if the query point is coplanar to the face
     *
     * @param query
     * @return
     */
    public int orientationWrtBCA(VertexD query) {
        return query.orientation(b, c, a);
    }

    /**
     * Answer 1 if the query point is positively oriented with respect to the face
     * CBD, -1 if negatively oriented, 0 if the query point is coplanar to the face
     *
     * @param query
     * @return
     */
    public int orientationWrtCBD(VertexD query) {
        return query.orientation(c, b, d);
    }

    /**
     * Answer 1 if the query point is positively oriented with respect to the face
     * DAC, -1 if negatively oriented, 0 if the query point is coplanar to the face
     *
     * @param query
     * @return
     */
    public int orientationWrtDAC(VertexD query) {
        return query.orientation(d, a, c);
    }

    public void removeAnyDegenerateTetrahedronPair() {
        if (nA != null) {
            if (nA == nB) {
                removeDegenerateTetrahedronPair(V.A, V.B, V.C, V.D);
                return;
            }
            if (nA == nC) {
                removeDegenerateTetrahedronPair(V.A, V.C, V.B, V.D);
                return;
            }
            if (nA == nD) {
                removeDegenerateTetrahedronPair(V.A, V.D, V.B, V.C);
                return;
            }
        }

        if (nB != null) {
            if (nB == nC) {
                removeDegenerateTetrahedronPair(V.B, V.C, V.A, V.D);
                return;
            }
            if (nB == nD) {
                removeDegenerateTetrahedronPair(V.B, V.D, V.A, V.C);
                return;
            }
        }

        if (nC != null)
            if (nC == nD) {
                removeDegenerateTetrahedronPair(V.C, V.D, V.A, V.B);
                return;
            }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Tetrahedron [");
        if (isDeleted()) {
            buf.append("DELETED]");
            return buf.toString();
        }
        for (VertexD v : getVertices()) {
            buf.append(v);
            buf.append(", ");
        }
        buf.append(']');
        return buf.toString();
    }

    protected void children(Stack<Tetrahedron> stack, Set<Tetrahedron> processed) {
        if (nA != null && !processed.contains(nA)) {
            stack.push(nA);
        }
        if (nB != null && !processed.contains(nB)) {
            stack.push(nB);
        }
        if (nC != null && !processed.contains(nC)) {
            stack.push(nC);
        }
        if (nD != null && !processed.contains(nD)) {
            stack.push(nD);
        }
    }

    /**
     * Clean up the pointers
     */
    void delete() {
        a.deleteAdjacent();
        b.deleteAdjacent();
        c.deleteAdjacent();
        d.deleteAdjacent();
        nA = nB = nC = nD = null;
        a = b = c = d = null;
    }

    VertexD getA() {
        return a;
    }

    VertexD getB() {
        return b;
    }

    VertexD getC() {
        return c;
    }

    VertexD getD() {
        return d;
    }

    /**
     * Answer the canonical ordinal of the opposite vertex of the neighboring
     * tetrahedron
     *
     * @param neighbor
     * @return
     */
    V ordinalOf(Tetrahedron neighbor) {
        if (neighbor == null) {
            return null;
        }
        if (nA == neighbor) {
            return A;
        }
        if (nB == neighbor) {
            return B;
        }
        if (nC == neighbor) {
            return C;
        }
        if (nD == neighbor) {
            return D;
        }
        throw new IllegalArgumentException("Not a neighbor: " + neighbor);
    }

    /**
     * Patch the new tetrahedron created by a flip of the receiver by seting the
     * neighbor to the value in the receiver
     * <p>
     *
     * @param vOld - the opposing vertex the neighboring tetrahedron in the receiver
     * @param n    - the new tetrahedron to patch
     * @param vNew - the opposing vertex of the neighbor to assign in the new
     *             tetrahedron
     */
    void patch(V vOld, Tetrahedron n, V vNew) {
        Tetrahedron neighbor = getNeighbor(vOld);
        if (neighbor != null) {
            neighbor.setNeighbor(neighbor.ordinalOf(this), n);
            n.setNeighbor(vNew, neighbor);
        }
    }

    /**
     * Patch the new tetrahedron created by a flip of the receiver by seting the
     * neighbor to the value in the receiver
     * <p>
     *
     * @param old
     * @param n
     * @param vNew
     */
    void patch(VertexD old, Tetrahedron n, V vNew) {
        patch(ordinalOf(old), n, vNew);
    }

    void setNeighbor(V v, Tetrahedron n) {
        switch (v) {
        case A:
            nA = n;
            break;
        case B:
            nB = n;
            break;
        case C:
            nC = n;
            break;
        case D:
            nD = n;
            break;
        default:
            throw new IllegalArgumentException("Invalid vertex: " + v);
        }
    }

    void setNeighborA(Tetrahedron t) {
        nA = t;
    }

    void setNeighborB(Tetrahedron t) {
        nB = t;
    }

    void setNeighborC(Tetrahedron t) {
        nC = t;
    }

    void setNeighborD(Tetrahedron t) {
        nD = t;
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
    void traverseVoronoiFace(Tetrahedron origin, Tetrahedron from, VertexD vC, VertexD axis, List<Point3f> face) {
        if (origin == this) {
            return;
        }
        double[] center = new double[3];
        centerSphere(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, d.x, d.y, d.z, center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V next = VORONOI_FACE_NEXT[ordinalOf(from).ordinal()][ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron t = getNeighbor(next);
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
    void traverseVoronoiFace(VertexD vC, VertexD axis, List<Point3f[]> faces) {
        ArrayList<Point3f> face = new ArrayList<>();
        double[] center = new double[3];
        centerSphere(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z, d.x, d.y, d.z, center);
        face.add(new Point3f((float) center[0], (float) center[1], (float) center[2]));
        V v = VORONOI_FACE_ORIGIN[ordinalOf(vC).ordinal()][ordinalOf(axis).ordinal()];
        Tetrahedron next = getNeighbor(v);
        if (next != null) {
            next.traverseVoronoiFace(this, this, vC, axis, face);
        }
        faces.add(face.toArray(new Point3f[face.size()]));
    }

    /**
     * Visit the star tetrahedra set of the of the center vertex
     *
     * @param vC      - the center vertex
     * @param visitor - the visitor to invoke for each tetrahedron in the star
     */
    void visitStar(VertexD vC, StarVisitor visitor) {
        IdentitySet<Tetrahedron> visited = new IdentitySet<>(10);
        visitStar(vC, visitor, visited);
    }

    /**
     * Visit the star tetrahedra set of the of the center vertex
     *
     * @param vC      - the center vertex
     * @param visitor - the visitor to invoke for each tetrahedron in the star
     * @param visited - the set of previously visited tetrahedra
     */
    void visitStar(VertexD vC, StarVisitor visitor, IdentitySet<Tetrahedron> visited) {
        if (visited.add(this)) {
            switch (ordinalOf(vC)) {
            case A:
                visitor.visit(A, this, c, b, d);
                if (nC != null) {
                    nC.visitStar(vC, visitor, visited);
                }
                if (nB != null) {
                    nB.visitStar(vC, visitor, visited);
                }
                if (nD != null) {
                    nD.visitStar(vC, visitor, visited);
                }
                break;
            case B:
                visitor.visit(B, this, d, a, c);
                if (nD != null) {
                    nD.visitStar(vC, visitor, visited);
                }
                if (nA != null) {
                    nA.visitStar(vC, visitor, visited);
                }
                if (nC != null) {
                    nC.visitStar(vC, visitor, visited);
                }
                break;
            case C:
                visitor.visit(C, this, a, d, b);
                if (nA != null) {
                    nA.visitStar(vC, visitor, visited);
                }
                if (nD != null) {
                    nD.visitStar(vC, visitor, visited);
                }
                if (nB != null) {
                    nB.visitStar(vC, visitor, visited);
                }
                break;
            case D:
                visitor.visit(D, this, b, c, a);
                if (nB != null) {
                    nB.visitStar(vC, visitor, visited);
                }
                if (nA != null) {
                    nA.visitStar(vC, visitor, visited);
                }
                if (nC != null) {
                    nC.visitStar(vC, visitor, visited);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid center vertex: " + vC);
            }
        }
    }

    private void removeDegenerateTetrahedronPair(V ve1, V ve2, V vf1, V vf2) {
        Tetrahedron nE = getNeighbor(ve1);
        Tetrahedron nF1_that = nE.getNeighbor(getVertex(vf1));
        Tetrahedron nF2_that = nE.getNeighbor(getVertex(vf2));

        patch(vf1, nF1_that, nF1_that.ordinalOf(nE));
        patch(vf2, nF2_that, nF2_that.ordinalOf(nE));

        VertexD e1 = getVertex(ve1);
        VertexD e2 = getVertex(ve2);
        VertexD f1 = getVertex(vf1);
        VertexD f2 = getVertex(vf2);

        delete();
        nE.delete();

        e1.freshenAdjacent(nF1_that);
        f2.freshenAdjacent(nF1_that);
        e2.freshenAdjacent(nF2_that);
        f1.freshenAdjacent(nF2_that);
    }
}
