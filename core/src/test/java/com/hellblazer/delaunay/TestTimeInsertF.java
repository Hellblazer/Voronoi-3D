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

import static com.hellblazer.delaunay.VertexF.getRandomPoints;

import java.util.Random;

import com.hellblazer.delaunay.Vertex.FloatType;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TestTimeInsertF {

    int iterations = 100;

//    @Test
    public void testLargeRandom() {
        Random random = new Random(666);
        Vertex<FloatType> ourPoints[] = getRandomPoints(random, 600, 1.0F, false);

        TetrahedralizationF tet = new TetrahedralizationF(random);

        for (Vertex<FloatType> v : ourPoints) {
            tet.insert(v);
        }
        long now = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            tet = new TetrahedralizationF(random);
            for (Vertex<FloatType> v : ourPoints) {
                tet.insert(v);
            }
        }
        long iter = (System.nanoTime() - now) / iterations;
        System.out.println("insert random case (" + ourPoints.length + " points): " + iter / 1E6 + " Ms");
    }

//    @Test
    public void testSuperLargeRandom() {
        Random random = new Random(666);
        Vertex<FloatType> ourPoints[] = getRandomPoints(random, 6000, 10.0F, false);

        TetrahedralizationF tet = new TetrahedralizationF(random);

        for (Vertex<FloatType> v : ourPoints) {
            tet.insert(v);
        }
        long now = System.nanoTime();
        for (int i = 0; i < 2; i++) {
            tet = new TetrahedralizationF(random);
            for (Vertex<FloatType> v : ourPoints) {
                tet.insert(v);
            }
        }
        long iter = (System.nanoTime() - now) / 2;
        System.out.println("insert random case (" + ourPoints.length + " points): " + iter / 1E6 + " Ms");
    }

}
