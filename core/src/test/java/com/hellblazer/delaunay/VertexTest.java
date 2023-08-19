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

import static com.hellblazer.delaunay.Vertex.getRandomPoints;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point3d;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class VertexTest {
    @Test
    public void testDeterminant() {
        var a = new Vertex(7, 8, 4);
        var b = new Point3d(8, 4, 6);
        var c = new Point3d(2, 1, 3);

        final var det = a.determinant(b, c);
        assertEquals(-54, det, 0.1);
    }

    @Test
    public void testFlip4to1() {
        Tetrahedralization tetrahedralization = new Tetrahedralization();
        Tetrahedron U = tetrahedralization.myOwnPrivateIdaho();
        Vertex N = new Vertex(100, 100, 100);

        List<OrientedFace> unlinkedFacets = new ArrayList<>();

        U.flip1to4(N, unlinkedFacets);

        tetrahedralization.flip4to1(N);
    }

    @Test
    public void testMaxStep() {
        Random random = new Random(666);
        Point3d ourPoints[] = getRandomPoints(random, 60000, 100.0D, false);

        Tetrahedralization T = new Tetrahedralization(random);

        for (var v : ourPoints) {
            T.insert(v);
        }
        Point3d N = new Point3d(100, 100, 100);
        var vertex = T.insert(N);

        var step = vertex.maxStep(N);
        assertTrue(step <= 1.0);
    }

    @Test
    public void testOrientation() {
        Vertex[] fourCorners = Tetrahedralization.getFourCorners();
        assertEquals(1, fourCorners[3].orientation(fourCorners[0], fourCorners[1], fourCorners[2]));
        assertEquals(-1, fourCorners[3].orientation(fourCorners[1], fourCorners[0], fourCorners[2]));
        assertEquals(0, new Vertex(100, 100, 0).orientation(new Vertex(1000, 100000, 0), new Vertex(0, -1456, 0),
                                                            new Vertex(-2567, 0, 0)));
        assertEquals(1, fourCorners[0].orientation(fourCorners[2], fourCorners[1], fourCorners[3]));

        assertEquals(1, fourCorners[1].orientation(fourCorners[3], fourCorners[0], fourCorners[2]));

        assertEquals(1, fourCorners[2].orientation(fourCorners[0], fourCorners[3], fourCorners[1]));

        assertEquals(1, fourCorners[3].orientation(fourCorners[1], fourCorners[2], fourCorners[0]));
    }

    @Test
    public void testOrientation2() {
        Vertex[] fourCorners = Tetrahedralization.getFourCorners();
        Vertex N = new Vertex(0, 0, 0);
        Tetrahedron t = new Tetrahedron(fourCorners);
        for (OrientedFace face : t) {
            assertEquals(1, face.orientationOf(N));
        }
        Vertex query = new Vertex(3949, 3002, 8573);
        for (OrientedFace face : t) {
            assertEquals(1, face.orientationOf(query));
        }
    }

}
