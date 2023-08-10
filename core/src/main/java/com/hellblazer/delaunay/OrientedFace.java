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

import static com.hellblazer.delaunay.V.B;
import static com.hellblazer.delaunay.V.C;
import static com.hellblazer.delaunay.V.D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * An oriented face of a tetrahedron.
 * <p>
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public abstract class OrientedFace implements Iterable<Vertex> {

    /**
     * The vertex in the adjacent tetrahedron opposite of this face
     */
    protected final V adjacentVertexOrdinal;

    public OrientedFace() {
        Tetrahedron adjacent = getAdjacent();
        adjacentVertexOrdinal = adjacent == null ? null : adjacent.ordinalOf(getIncident());
    }

    /**
     * Perform a flip for deletion of the vertex from the tetrahedralization. The
     * incident and adjacent tetrahedra form an ear of the star set of tetrahedra
     * adjacent to v.
     * <p>
     *
     * @param index - the index of the receiver in the list of ears
     * @param ears  - the list of ears of the vertex
     * @param n     - the vertex to be deleted
     * @return true if the receiver is to be deleted from the list of ears
     */
    public boolean flip(int index, LinkedList<OrientedFace> ears, Vertex n) {
        if (!isValid()) {
            return true;
        }

        int reflexEdge = 0;
        int reflexEdges = 0;
        // Determine how many faces are visible from the tetrahedron
        for (int i = 0; reflexEdges < 2 && i < 3; i++) {
            if (isReflex(i)) {
                reflexEdge = i;
                reflexEdges++;
            }
        }

        if (reflexEdges == 0 && isConvex(indexOf(n)) && isLocallyDelaunay(index, n, ears)) {
            // Only one face of the opposing tetrahedron is visible
            Tetrahedron[] created = flip2to3();
            if (created[0].includes(n)) {
                if (created[1].includes(n)) {
                    ears.add(created[0].getFace(created[0].ordinalOf(created[1])));
                } else {
                    ears.add(created[0].getFace(created[0].ordinalOf(created[2])));
                }
            } else {
                ears.add(created[1].getFace(created[1].ordinalOf(created[2])));
            }
            return true;
        } else if (reflexEdges == 1) {
            // Two faces of the opposing tetrahedron are visible
            Vertex opposingVertex = getVertex(reflexEdge);
            Tetrahedron t1 = getIncident().getNeighbor(opposingVertex);
            Tetrahedron t2 = getAdjacent().getNeighbor(opposingVertex);
            if (t1 != null && t1 == t2 && isFlippable3ear(n) && isLocallyDelaunay(index, n, ears)) {
                flip3to2(reflexEdge);
                return true;
            }
        }
        // all three faces are visible, no action taken
        return false;
    }

    /**
     * Perform the flip which incrementally restores the delaunay condition after
     * the vertex has been inserted into the tetrahedralization.
     * <p>
     *
     * @param n    - the inserted vertex
     * @param ears - the stack of oriented faces left to process
     * @return - the last valid tetrahedron noted, or null if no flip was performed.
     */
    public Tetrahedron flip(Vertex n, List<OrientedFace> ears) {
        if (!isValid()) {
            return null;
        }
        int reflexEdge = 0;
        int reflexEdges = 0;
        // Determine how many faces are visible from the tetrahedron formed
        // by the inserted point and the popped facet
        for (int i = 0; reflexEdges < 2 && i < 3; i++) {
            if (isReflex(i)) {
                reflexEdge = i;
                reflexEdges++;
            }
        }

        Tetrahedron returned = null;
        if (reflexEdges == 0 && !isRegular()) {
            // Only one face of the opposing tetrahedron is visible
            for (Tetrahedron t : flip2to3()) {
                OrientedFace f = t.getFace(n);
                if (f.hasAdjacent()) {
                    ears.add(f);
                }
                returned = t;
            }
        } else if (reflexEdges == 1 && !isRegular()) {
            // Two faces of the opposing tetrahedron are visible
            Vertex opposingVertex = getVertex(reflexEdge);
            Tetrahedron t1 = getIncident().getNeighbor(opposingVertex);
            Tetrahedron t2 = getAdjacent().getNeighbor(opposingVertex);
            if (t1 != null && t1 == t2) {
                for (Tetrahedron t : flip3to2(reflexEdge)) {
                    OrientedFace f = t.getFace(n);
                    if (f.hasAdjacent()) {
                        ears.add(f);
                    }
                    returned = t;
                }
            }
        }
        // all three faces are visible, no action taken
        return returned;
    }

    /**
     * Perform the bistellar flip 2 -> 3. This produces three new tetrahedra from
     * the receiver and tetrahdron that shares the receiver face
     *
     * @return the three created tetrahedron
     */
    public Tetrahedron[] flip2to3() {
        assert adjacentVertexOrdinal != null;
        Tetrahedron incident = getIncident();

        Vertex opposingVertex = getAdjacentVertex();
        Vertex incidentVertex = getIncidentVertex();
        Tetrahedron t0 = new Tetrahedron(getVertex(0), incidentVertex, getVertex(1), opposingVertex);
        Tetrahedron t1 = new Tetrahedron(getVertex(1), incidentVertex, getVertex(2), opposingVertex);
        Tetrahedron t2 = new Tetrahedron(getVertex(0), getVertex(2), incidentVertex, opposingVertex);

        t0.setNeighborA(t1);
        t0.setNeighborC(t2);

        t1.setNeighborA(t2);
        t1.setNeighborC(t0);

        t2.setNeighborA(t1);
        t2.setNeighborB(t0);

        incident.patch(getVertex(2), t0, D);
        incident.patch(getVertex(0), t1, D);
        incident.patch(getVertex(1), t2, D);

        Tetrahedron adjacent = getAdjacent();

        adjacent.patch(getVertex(0), t1, B);
        adjacent.patch(getVertex(1), t2, C);
        adjacent.patch(getVertex(2), t0, B);

        incident.delete();
        adjacent.delete();

        t0.removeAnyDegenerateTetrahedronPair();
        t1.removeAnyDegenerateTetrahedronPair();
        t2.removeAnyDegenerateTetrahedronPair();

        if (t0.isDeleted())
            if (t1.isDeleted())
                if (t2.isDeleted())
                    return new Tetrahedron[] {};
                else
                    return new Tetrahedron[] { t2 };
            else if (t2.isDeleted())
                return new Tetrahedron[] { t1 };
            else
                return new Tetrahedron[] { t1, t2 };
        else if (t1.isDeleted())
            if (t2.isDeleted())
                return new Tetrahedron[] { t0 };
            else
                return new Tetrahedron[] { t0, t2 };
        else if (t2.isDeleted())
            return new Tetrahedron[] { t0, t1 };
        else
            return new Tetrahedron[] { t0, t1, t2 };
    }

    /**
     * Perform the bistellar 3->2 flip. This flip constructs two new tetrahedra from
     * the two tetraheda determined by the incident and adjacent neighbor of the
     * face, along with the tetrahedron on the reflexive edge of the face.
     * <p>
     *
     * @param reflexiveEdge - the vertex opposite the reflexive edge of the face
     * @return the two created tetrahedron
     */
    public Tetrahedron[] flip3to2(int reflexEdge) {
        assert adjacentVertexOrdinal != null;
        Tetrahedron incident = getIncident();
        Tetrahedron o2 = getIncident().getNeighbor(getVertex(reflexEdge));

        Vertex top0 = null;
        Vertex top1 = null;

        switch (reflexEdge) {
        case 0:
            top0 = getVertex(1);
            top1 = getVertex(2);
            break;
        case 1:
            top0 = getVertex(0);
            top1 = getVertex(2);
            break;
        case 2:
            top0 = getVertex(0);
            top1 = getVertex(1);
            break;
        default:
            throw new IllegalArgumentException("Invalid reflex edge index: " + reflexEdge);
        }

        Vertex x = getVertex(reflexEdge);
        Vertex y = getIncidentVertex();
        Vertex z = getAdjacentVertex();

        Tetrahedron t0;
        Tetrahedron t1;
        if (top0.orientation(x, y, z) > 0) {
            t0 = new Tetrahedron(x, y, z, top0);
            t1 = new Tetrahedron(y, x, z, top1);
        } else {
            t0 = new Tetrahedron(x, y, z, top1);
            t1 = new Tetrahedron(y, x, z, top0);
        }

        t0.setNeighborD(t1);
        t1.setNeighborD(t0);

        incident.patch(t0.getD(), t1, t1.ordinalOf(getAdjacentVertex()));
        incident.patch(t1.getD(), t0, t0.ordinalOf(getAdjacentVertex()));

        Tetrahedron adjacent = getAdjacent();

        adjacent.patch(t0.getD(), t1, t1.ordinalOf(getIncidentVertex()));
        adjacent.patch(t1.getD(), t0, t0.ordinalOf(getIncidentVertex()));

        o2.patch(t0.getD(), t1, t1.ordinalOf(getVertex(reflexEdge)));
        o2.patch(t1.getD(), t0, t0.ordinalOf(getVertex(reflexEdge)));

        incident.delete();
        adjacent.delete();
        o2.delete();

        return new Tetrahedron[] { t0, t1 };
    }

    /**
     * Answer the adjacent tetrahedron to the face
     *
     * @return
     */
    abstract public Tetrahedron getAdjacent();

    /**
     * Answer the vertex in the adjacent tetrahedron which is opposite of this face.
     *
     * @return
     */
    public Vertex getAdjacentVertex() {
        if (adjacentVertexOrdinal == null) {
            return null;
        }
        return getAdjacent().getVertex(adjacentVertexOrdinal);
    }

    /**
     * Answer the canonical ordinal of the vertex in the adjacent tetrahedron which
     * is opposite of this face.
     *
     * @return
     */
    public V getAdjacentVertexOrdinal() {
        return adjacentVertexOrdinal;
    }

    /**
     * Answer the two vertices defining the edge opposite of the vertex v
     *
     * @param v - the vertex defining the edge
     * @return the array of two vertices defining the edge
     */
    abstract public Vertex[] getEdge(Vertex v);

    /**
     * Answer the tetrahedron which is incident with this face
     *
     * @return
     */
    abstract public Tetrahedron getIncident();

    /**
     * Answer the vertex in the tetrahedron which is opposite of this face
     *
     * @return
     */
    abstract public Vertex getIncidentVertex();

    /**
     * Answer the canonical vertex for this face
     *
     * @param anIndex
     * @return
     */
    abstract public Vertex getVertex(int anIndex);

    public boolean hasAdjacent() {
        return adjacentVertexOrdinal != null;
    }

    abstract public boolean includes(Vertex v);

    /**
     * Answer the edge index corresponding to the vertex
     *
     * @param v - the vertex
     * @return the index of the edge
     */
    abstract public int indexOf(Vertex v);

    /**
     * Answer true if the faces joined by the edge are concave when viewed from the
     * originating tetrahedron.
     * <p>
     *
     * @param vertex - the vertex of the face that is opposite of the edge
     * @return true if the faces joined by the edge are convex, false if these faces
     *         are not convex
     */
    abstract public boolean isConvex(int vertex);

    /**
     * Answer true if the faces joined by the edge are not concave when viewed from
     * the originating tetrahedron.
     * <p>
     *
     * @param vertex - the vertex of the face that is opposite of the edge
     * @return true if the faces joined by the edge are reflex, false if these faces
     *         are not reflex
     */
    abstract public boolean isReflex(int vertex);

    /**
     * Answer true if the vertex in the adjacent tetrahedron is not contained in the
     * circumsphere of the incident tetrahedron
     *
     * @return
     */
    public boolean isRegular() {
        return !getIncident().inSphere(getAdjacentVertex());
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < 3;
            }

            @Override
            public Vertex next() {
                if (i == 3) {
                    throw new NoSuchElementException("No vertices left on this face");
                }
                return getVertex(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Deletion of vertices from face not supported");
            }
        };
    }

    /**
     * Answer +1 if the orientation of the query point is positive with respect to
     * this face, -1 if negative and 0 if the test point is coplanar with the face
     *
     * @param query - the point to be tested
     * @return +1 if the orientation of the query point is positive with respect to
     *         the face, -1 if negative and 0 if the query point is coplanar
     */
    abstract public int orientationOf(Vertex query);

    private boolean inSphere(Vertex query, Vertex b, Vertex c, Vertex d) {
        Vertex a = getIncidentVertex();
        if (d.orientation(a, b, c) < 0) {
            Vertex tmp = b;
            b = a;
            a = tmp;
        }
        return query.inSphere(a, b, c, d) > 0;
    }

    private boolean isFlippable3ear(Vertex n) {
        OrientedFace opposingFace = getIncident().getFace(n);
        opposingFace.getAdjacent().getFace(opposingFace.getAdjacentVertex());
        return opposingFace.orientationOf(n) > 0;

    }

    private boolean isLocallyDelaunay(int index, Vertex v, LinkedList<OrientedFace> ears) {
        Function<Vertex, Boolean> circumSphere = query -> {
            switch (indexOf(v)) {
            case 0:
                return inSphere(query, getVertex(1), getVertex(2), getVertex(0));
            case 1:
                return inSphere(query, getVertex(0), getVertex(2), getVertex(1));
            default:
                return inSphere(query, getVertex(0), getVertex(1), getVertex(2));
            }
        };
        for (int i = 0; i < ears.size(); i++) {
            if (index != i) {
                OrientedFace ear = ears.get(i);
                if (ear != this && ear.isValid()) {
                    for (Vertex e : ear) {
                        if (e != v && circumSphere.apply(e)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isValid() {
        return !getIncident().isDeleted() && hasAdjacent() && !getAdjacent().isDeleted();
    }
}
