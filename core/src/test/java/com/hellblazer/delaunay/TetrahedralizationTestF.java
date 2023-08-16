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

import static com.hellblazer.delaunay.VertexF.getRandomPoints;
import static junit.framework.Assert.assertEquals;

import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.hellblazer.delaunay.Vertex.FloatType;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TetrahedralizationTestF {

    public void testDelete() {
        Tetrahedralization<FloatType> T = new TetrahedralizationF(new Random(666));
        Vertex<FloatType> N = new VertexF(100, 100, 100);
        T.insert(N);
        Vertex<FloatType> O = new VertexF(5000, -1003, 101);
        T.insert(O);
        T.delete(N);
        assertEquals(1, T.getTetrahedrons().size());
    }

    @Test
    public void testFlip4to1() {
        Tetrahedralization<FloatType> T = new TetrahedralizationF(new Random(0));
        Vertex<FloatType> N = new VertexF(100, 100, 100);
        T.insert(N);
        T.flip4to1(N);
        assertEquals(1, T.getTetrahedrons().size());
    }

//    @Test
    public void testLargeRandom() {
        Random random = new Random(666);
        Vertex<FloatType> ourPoints[] = getRandomPoints(random, 60000, 100.0F, false);

        Tetrahedralization<FloatType> T = new TetrahedralizationF(random);

        for (Vertex<FloatType> v : ourPoints) {
            T.insert(v);
        }

        Set<Tetrahedron<FloatType>> L = T.getTetrahedrons();
        assertEquals(403094, L.size());
    }
}
