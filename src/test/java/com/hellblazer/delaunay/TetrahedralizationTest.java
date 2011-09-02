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

import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import com.hellblazer.delaunay.gui.Examples;

/**
 * 
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 * 
 */

public class TetrahedralizationTest extends TestCase {

    private static class OC implements StarVisitor {
        int order = 0;

        @Override
        public void visit(V vertex, Tetrahedron t, Vertex x, Vertex y, Vertex z) {
            order++;
        }
    }

    public void testCubic() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (Vertex v : Examples.getCubicCrystalStructure()) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(188, L.size());
    }

    public void testFlip4to1() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        Vertex N = new Vertex(100, 100, 100);
        T.insert(N);
        T.flip4to1(N);
        assertEquals(1, T.getTetrahedrons().size());
    }

    public void testGrid() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (Vertex v : Examples.getGrid()) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(387, L.size());
    }

    public void testLargeRandom() {
        Random random = new Random(666);
        Vertex ourPoints[] = getRandomPoints(random, 600, 1.0D, false);

        Tetrahedralization T = new Tetrahedralization(random);

        for (Vertex v : ourPoints) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(3903, L.size());
    }

    /**
     * Verify that the order of a vertex in a Tetrahedralization is correctly
     * maintained.
     */
    public void testOrderMaintenance() throws Exception {
        verifyOrder(Examples.getWorstCase());
        verifyOrder(Examples.getCubicCrystalStructure());
        verifyOrder(Examples.getGrid());
        Random random = new Random(666);
        verifyOrder(getRandomPoints(random, 600, 1.0D, false));
    }

    public void testWorstCase() {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (Vertex v : Examples.getWorstCase()) {
            T.insert(v);
        }

        Set<Tetrahedron> L = T.getTetrahedrons();
        assertEquals(610, L.size());
    }

    public void teztDelete() {
        Tetrahedralization T = new Tetrahedralization(new Random(666));
        Vertex N = new Vertex(100, 100, 100);
        T.insert(N);
        Vertex O = new Vertex(5000, -1003, 101);
        T.insert(O);
        T.delete(N);
        assertEquals(1, T.getTetrahedrons().size());
    }

    private void verifyOrder(Vertex[] vertices) {
        Tetrahedralization T = new Tetrahedralization(new Random(0));
        for (Vertex v : vertices) {
            T.insert(v);
        }
        for (Vertex v : vertices) {
            OC oc = new OC();
            v.getAdjacent().visitStar(v, oc);
            assertEquals(oc.order, v.getOrder());
        }
    }
}
