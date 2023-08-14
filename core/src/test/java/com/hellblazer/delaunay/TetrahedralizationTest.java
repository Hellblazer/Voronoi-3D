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

import static com.hellblazer.delaunay.VertexD.getRandomPoints;
import static junit.framework.Assert.assertEquals;

import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.hellblazer.delaunay.Vertex.DoubleType;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TetrahedralizationTest {

    private static class OC implements StarVisitor<DoubleType> {
        int order = 0;

        @Override
        public void visit(V vertex, Tetrahedron<DoubleType> t, Vertex<DoubleType> x, Vertex<DoubleType> y,
                          Vertex<DoubleType> z) {
            order++;
        }
    }

    @Test
    public void testCubic() {
        Tetrahedralization<DoubleType> T = new TetrahedralizationD(new Random(0));
        for (Vertex<DoubleType> v : Examples.getCubicCrystalStructure()) {
            T.insert(v);
        }

        Set<Tetrahedron<DoubleType>> L = T.getTetrahedrons();
        assertEquals(189, L.size());
    }

    public void testDelete() {
        Tetrahedralization<DoubleType> T = new TetrahedralizationD(new Random(666));
        Vertex<DoubleType> N = new VertexD(100, 100, 100);
        T.insert(N);
        Vertex<DoubleType> O = new VertexD(5000, -1003, 101);
        T.insert(O);
        T.delete(N);
        assertEquals(1, T.getTetrahedrons().size());
    }

    @Test
    public void testFlip4to1() {
        Tetrahedralization<DoubleType> T = new TetrahedralizationD(new Random(0));
        Vertex<DoubleType> N = new VertexD(100, 100, 100);
        T.insert(N);
        T.flip4to1(N);
        assertEquals(1, T.getTetrahedrons().size());
    }

    @Test
    public void testGrid() {
        TetrahedralizationD T = new TetrahedralizationD(new Random(0));
        for (Vertex<DoubleType> v : Examples.getGrid()) {
            T.insert(v);
        }

        Set<Tetrahedron<DoubleType>> L = T.getTetrahedrons();
        assertEquals(386, L.size());
    }

    @Test
    public void testLargeRandom() {
        Random random = new Random(666);
        Vertex<DoubleType> ourPoints[] = getRandomPoints(random, 60000, 100.0D, false);

        Tetrahedralization<DoubleType> T = new TetrahedralizationD(random);

        for (Vertex<DoubleType> v : ourPoints) {
            T.insert(v);
        }

        Set<Tetrahedron<DoubleType>> L = T.getTetrahedrons();
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
        Tetrahedralization<DoubleType> T = new TetrahedralizationD(new Random(0));
        for (Vertex<DoubleType> v : Examples.getWorstCase()) {
            T.insert(v);
        }

        Set<Tetrahedron<DoubleType>> L = T.getTetrahedrons();
        assertEquals(610, L.size());
    }

    private void verifyOrder(Vertex<DoubleType>[] vertices) {
        Tetrahedralization<DoubleType> T = new TetrahedralizationD(new Random(0));
        for (Vertex<DoubleType> v : vertices) {
            T.insert(v);
        }
        for (Vertex<DoubleType> v : vertices) {
            OC oc = new OC();
            v.getAdjacent().visitStar(v, oc);
            assertEquals(oc.order, v.getOrder());
        }
    }
}
