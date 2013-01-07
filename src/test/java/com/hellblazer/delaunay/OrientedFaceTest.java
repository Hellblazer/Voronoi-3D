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
import junit.framework.TestCase;

/**
 * 
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 * 
 */

public class OrientedFaceTest extends TestCase {

    public void testFlip2to3() {
        Vertex a = new Vertex(0, -100, 0);
        Vertex b = new Vertex(-100, 0, 0);
        Vertex c = new Vertex(100, 0, 0);
        Vertex d = new Vertex(0, -50, -100);
        Vertex e = new Vertex(0, -50, 100);

        Tetrahedron tA = new Tetrahedron(a, b, c, d);
        Tetrahedron tB = new Tetrahedron(e, b, c, a);
        tA.setNeighbor(D, tB);
        tB.setNeighbor(A, tA);

        OrientedFace face = tA.getFace(D);
        assertTrue(face.hasAdjacent());
        assertEquals(A, face.getAdjacentVertexOrdinal());
        Tetrahedron[] created = face.flip2to3();
        assertNotNull(created);
        Tetrahedron tI = created[1];
        assertNotNull(tI);
        Tetrahedron tII = created[0];
        assertNotNull(tII);
        Tetrahedron tIII = created[2];
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

    public void testIsConvex() {
        Vertex a = new Vertex(-1, -1, 1);
        Vertex b = new Vertex(1, 1, 1);
        Vertex c = new Vertex(-1, 1, -1);
        Vertex d = new Vertex(1, -1, -1);
        Vertex e = new Vertex(-1, 1, 1);

        Tetrahedron tA = new Tetrahedron(a, b, c, d);
        Tetrahedron tB = new Tetrahedron(b, c, a, e);
        tA.setNeighbor(D, tB);
        tB.setNeighbor(D, tA);

        OrientedFace faceAB = tA.getFace(D);

        for (int i = 0; i < 2; i++) {
            assertTrue(faceAB.isConvex(i));
            assertFalse(faceAB.isReflex(i));
        }
    }

    public void testIsReflex() {
        Vertex a = new Vertex(-1, -1, 1);
        Vertex b = new Vertex(1, 1, 1);
        Vertex c = new Vertex(-1, 1, -1);
        Vertex d = new Vertex(1, -1, -1);
        Vertex e = new Vertex(-1, 1, 1);

        Tetrahedron tA = new Tetrahedron(a, b, c, d);
        Tetrahedron tB = new Tetrahedron(b, c, a, e);
        tA.setNeighbor(D, tB);
        tB.setNeighbor(D, tA);

        OrientedFace faceAB = tA.getFace(D);

        assertTrue(faceAB.isReflex(0));
        assertFalse(faceAB.isConvex(0));
        assertTrue(faceAB.isReflex(1));
        assertFalse(faceAB.isConvex(1));
        assertFalse(faceAB.isReflex(2));
        assertTrue(faceAB.isConvex(2));
    }
}
