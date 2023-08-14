/**
 * Copyright (C) 2010 Hal Hildebrand. All rights reserved.
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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hellblazer.delaunay.Vertex.DoubleType;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TetrahedronTest {
    @Test
    public void testCreateUniverse() {
        Tetrahedron<DoubleType> idaho = new TetrahedralizationD().myOwnPrivateIdaho();
        assertNotNull(idaho);
        Vertex<DoubleType>[] vertices = idaho.getVertices();
        assertNotNull(vertices);
        assertEquals(4, vertices.length);
        assertNotSame(vertices[0], vertices[1]);
        assertNotSame(vertices[0], vertices[2]);
        assertNotSame(vertices[0], vertices[3]);
        assertNotSame(vertices[1], vertices[2]);
        assertNotSame(vertices[1], vertices[3]);
        assertNotSame(vertices[2], vertices[3]);

        // There can only be one
        assertNull(idaho.getNeighbor(A));
        assertNull(idaho.getNeighbor(B));
        assertNull(idaho.getNeighbor(C));
        assertNull(idaho.getNeighbor(D));

        // Check our faces
        for (OrientedFace<DoubleType> face : idaho) {
            assertNotNull(face);
            assertSame(idaho, face.getIncident());
            assertNull(face.getAdjacent());
        }
    }

    @Test
    public void testFlip14() {
        Tetrahedron<DoubleType> U = new TetrahedralizationD().myOwnPrivateIdaho();
        Vertex<DoubleType> a = U.getVertex(A);
        Vertex<DoubleType> b = U.getVertex(B);
        Vertex<DoubleType> c = U.getVertex(C);
        Vertex<DoubleType> d = U.getVertex(D);

        Vertex<DoubleType> N = new VertexD(100, 100, 100);

        List<OrientedFace<DoubleType>> unlinkedFacets = new ArrayList<>();

        Tetrahedron<DoubleType> tIV = U.flip1to4(N, unlinkedFacets);
        assertNotNull(tIV);
        assertEquals(0, unlinkedFacets.size());

        Tetrahedron<DoubleType> tI = tIV.getNeighbor(C);
        Tetrahedron<DoubleType> tII = tIV.getNeighbor(B);
        Tetrahedron<DoubleType> tIII = tIV.getNeighbor(A);

        assertSame(tIII, tI.getNeighbor(A));
        assertSame(tIV, tI.getNeighbor(B));
        assertSame(tII, tI.getNeighbor(C));
        assertSame(null, tI.getNeighbor(D));

        assertSame(tIII, tII.getNeighbor(A));
        assertSame(tI, tII.getNeighbor(B));
        assertSame(tIV, tII.getNeighbor(C));
        assertSame(null, tII.getNeighbor(D));

        assertSame(tI, tIII.getNeighbor(A));
        assertSame(tII, tIII.getNeighbor(B));
        assertSame(tIV, tIII.getNeighbor(C));
        assertSame(null, tIII.getNeighbor(D));

        assertSame(tIII, tIV.getNeighbor(A));
        assertSame(tII, tIV.getNeighbor(B));
        assertSame(tI, tIV.getNeighbor(C));
        assertSame(null, tIV.getNeighbor(D));

        assertEquals(a, tI.getVertex(A));
        assertEquals(c, tI.getVertex(B));
        assertEquals(d, tI.getVertex(C));
        assertEquals(N, tI.getVertex(D));

        assertEquals(a, tII.getVertex(A));
        assertEquals(b, tII.getVertex(B));
        assertEquals(c, tII.getVertex(C));
        assertEquals(N, tII.getVertex(D));

        assertEquals(b, tIII.getVertex(A));
        assertEquals(d, tIII.getVertex(B));
        assertEquals(c, tIII.getVertex(C));
        assertEquals(N, tIII.getVertex(D));

        assertEquals(a, tIV.getVertex(A));
        assertEquals(d, tIV.getVertex(B));
        assertEquals(b, tIV.getVertex(C));
        assertEquals(N, tIV.getVertex(D));

        OrientedFace<DoubleType> face = tI.getFace(A);
        assertEquals(d, face.getVertex(0));
        assertEquals(c, face.getVertex(1));
        assertEquals(N, face.getVertex(2));

        face = tI.getFace(B);
        assertEquals(N, face.getVertex(0));
        assertEquals(a, face.getVertex(1));
        assertEquals(d, face.getVertex(2));

        face = tI.getFace(C);
        assertEquals(a, face.getVertex(0));
        assertEquals(N, face.getVertex(1));
        assertEquals(c, face.getVertex(2));

        face = tI.getFace(D);
        assertEquals(c, face.getVertex(0));
        assertEquals(d, face.getVertex(1));
        assertEquals(a, face.getVertex(2));

        face = tII.getFace(A);
        assertEquals(c, face.getVertex(0));
        assertEquals(b, face.getVertex(1));
        assertEquals(N, face.getVertex(2));

        face = tII.getFace(B);
        assertEquals(N, face.getVertex(0));
        assertEquals(a, face.getVertex(1));
        assertEquals(c, face.getVertex(2));

        face = tII.getFace(C);
        assertEquals(a, face.getVertex(0));
        assertEquals(N, face.getVertex(1));
        assertEquals(b, face.getVertex(2));

        face = tII.getFace(D);
        assertEquals(b, face.getVertex(0));
        assertEquals(c, face.getVertex(1));
        assertEquals(a, face.getVertex(2));

        face = tIII.getFace(A);
        assertEquals(c, face.getVertex(0));
        assertEquals(d, face.getVertex(1));
        assertEquals(N, face.getVertex(2));

        face = tIII.getFace(B);
        assertEquals(N, face.getVertex(0));
        assertEquals(b, face.getVertex(1));
        assertEquals(c, face.getVertex(2));

        face = tIII.getFace(C);
        assertEquals(b, face.getVertex(0));
        assertEquals(N, face.getVertex(1));
        assertEquals(d, face.getVertex(2));

        face = tIII.getFace(D);
        assertEquals(d, face.getVertex(0));
        assertEquals(c, face.getVertex(1));
        assertEquals(b, face.getVertex(2));

        face = tIV.getFace(A);
        assertEquals(b, face.getVertex(0));
        assertEquals(d, face.getVertex(1));
        assertEquals(N, face.getVertex(2));

        face = tIV.getFace(B);
        assertEquals(N, face.getVertex(0));
        assertEquals(a, face.getVertex(1));
        assertEquals(b, face.getVertex(2));

        face = tIV.getFace(C);
        assertEquals(a, face.getVertex(0));
        assertEquals(N, face.getVertex(1));
        assertEquals(d, face.getVertex(2));

        face = tIV.getFace(D);
        assertEquals(d, face.getVertex(0));
        assertEquals(b, face.getVertex(1));
        assertEquals(a, face.getVertex(2));
    }
}
