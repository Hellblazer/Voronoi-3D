/**
 * Copyright (C) 2009 Hal Hildebrand. All rights reserved.
 *
 * This file is part of the 3D Incremental Voronoi GUI
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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import com.hellblazer.delaunay.Vertex.FloatType;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class OrientedFaceTestF {

    @Test
    public void testFlip2to3() {
        VertexF a = new VertexF(0, 100, 0);
        VertexF b = new VertexF(100, 0, 0);
        VertexF c = new VertexF(50, 50, 0);
        VertexF d = new VertexF(0, -50, -100);
        VertexF e = new VertexF(0, -50, 100);

        TetrahedronF tA = new TetrahedronF(a, b, c, d);
        TetrahedronF tB = new TetrahedronF(e, b, c, a);
        tA.setNeighbor(D, tB);
        tB.setNeighbor(A, tA);

        OrientedFace<Vertex.FloatType> face = tA.getFace(D);
        assertTrue(face.hasAdjacent());
        assertEquals(A, face.getAdjacentVertexOrdinal());
        Tetrahedron<FloatType>[] created = face.flip2to3();
        assertNotNull(created);
        Tetrahedron<FloatType> tI = created[1];
        assertNotNull(tI);
        Tetrahedron<FloatType> tII = created[0];
        assertNotNull(tII);
        Tetrahedron<FloatType> tIII = created[2];
        assertNotNull(tIII);

        assertSame(c, tI.getVertex(A));
        assertSame(d, tI.getVertex(B));
        assertSame(a, tI.getVertex(C));
        assertSame(e, tI.getVertex(D));

        assertSame(b, tII.getVertex(A));
        assertSame(d, tII.getVertex(B));
        assertSame(c, tII.getVertex(C));
        assertSame(e, tII.getVertex(D));

        assertSame(b, tIII.getVertex(A));
        assertSame(a, tIII.getVertex(B));
        assertSame(d, tIII.getVertex(C));
        assertSame(e, tIII.getVertex(D));

        assertSame(tII, tI.getNeighbor(C));
        assertSame(tIII, tI.getNeighbor(A));

        assertSame(tI, tII.getNeighbor(A));
        assertSame(tIII, tII.getNeighbor(C));
    }

    @Test
    public void testIsConvex() {
        VertexF a = new VertexF(-1, -1, 1);
        VertexF b = new VertexF(1, 1, 1);
        VertexF c = new VertexF(-1, 1, -1);
        VertexF d = new VertexF(1, -1, -1);
        VertexF e = new VertexF(-1, 1, 1);

        TetrahedronF tA = new TetrahedronF(a, b, c, d);
        TetrahedronF tB = new TetrahedronF(b, c, a, e);
        tA.setNeighbor(D, tB);
        tB.setNeighbor(D, tA);

        OrientedFace<Vertex.FloatType> faceAB = tA.getFace(D);

        for (int i = 0; i < 2; i++) {
            assertTrue(faceAB.isConvex(i));
            assertFalse(faceAB.isReflex(i));
        }
    }

    @Test
    public void testIsReflex() {
        VertexF a = new VertexF(-1, -1, 1);
        VertexF b = new VertexF(1, 1, 1);
        VertexF c = new VertexF(-1, 1, -1);
        VertexF d = new VertexF(1, -1, 100);
        VertexF e = new VertexF(-1, 1, 100);

        TetrahedronF tA = new TetrahedronF(a, b, c, d);
        TetrahedronF tB = new TetrahedronF(b, c, a, e);
        tA.setNeighbor(D, tB);
        tB.setNeighbor(D, tA);

        OrientedFace<Vertex.FloatType> faceAB = tA.getFace(D);
        assertFalse(faceAB.isReflex(0));
        assertTrue(faceAB.isConvex(0));
        assertTrue(faceAB.isReflex(1));
        assertFalse(faceAB.isConvex(1));
        assertFalse(faceAB.isReflex(2));
        assertTrue(faceAB.isConvex(2));
    }
}
