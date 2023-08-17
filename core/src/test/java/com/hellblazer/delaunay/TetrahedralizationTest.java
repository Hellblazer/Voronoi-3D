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

import java.util.Random;
import java.util.Set;

import javax.vecmath.Point3d;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TetrahedralizationTest {

    private static class OC implements StarVisitor {
        int order = 0;

        @Override
        public void visit(V vertex, Tetrahedron t, Vertex x, Vertex y, Vertex z) {
            order++;
        }
    }

    @Test
    public void testCubic() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (Point3d v : Examples.getCubicCrystalStructure()) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(189, L.size());
    }

    public void testDelete() {
        Tetrahedralization T = new Tetrahedralization(new Random(666));
        Point3d N = new Point3d(100, 100, 100);
        T.insert(N);
        Point3d O = new Point3d(5000, -1003, 101);
        var v = T.insert(O);
        T.delete(v);
        assertEquals(1, T.getTetrahedrons().size());
    }

    @Test
    public void testFlip4to1() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        Point3d N = new Point3d(100, 100, 100);
        var v = T.insert(N);
        T.flip4to1(v);
        assertEquals(1, T.getTetrahedrons().size());
    }

    @Test
    public void testGrid() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (Point3d v : Examples.getGrid()) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(386, L.size());
    }

    @Test
    public void testLargeRandom() {
        Random random = new Random(666);
        Point3d ourPoints[] = getRandomPoints(random, 60000, 100.0D, false);

        Tetrahedralization T = new Tetrahedralization(random);

        for (var v : ourPoints) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(403094, L.size());
    }

    /**
     * Verify that the order of a vertex in a Tetrahedralization is correctly
     * maintained.
     */
    @Test
    public void testOrderMaintenance() throws Exception {
        verifyOrder(Examples.getWorstCase());
        verifyOrder(Examples.getCubicCrystalStructure());
        verifyOrder(Examples.getGrid());
        Random random = new Random(666);
        verifyOrder(getRandomPoints(random, 600, 1.0D, false));
    }

    @Test
    public void testWorstCase() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (var v : Examples.getWorstCase()) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(610, L.size());
    }

    private void verifyOrder(Point3d[] vertices) {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (var v : vertices) {
            T.insert(v);
        }
        for (var v : T.getVertices()) {
            OC oc = new OC();
            v.getAdjacent().visitStar(v, oc);
            assertEquals(oc.order, v.getOrder());
        }
    }
}
