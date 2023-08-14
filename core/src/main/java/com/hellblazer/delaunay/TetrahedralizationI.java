/**
 * Copyright (C) 2008-2023 Hal Hildebrand. All rights reserved.
 * 
 * This file is part of the Prime Mover Event Driven Simulation Framework.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.hellblazer.delaunay;

import java.util.Random;

import com.hellblazer.delaunay.Vertex.IntType;

/**
 * 
 */
public class TetrahedralizationI extends Tetrahedralization<Vertex.IntType> {

    /**
     * Scale of the universe
     */
    private static int SCALE = (int) Math.pow(2D, 30D);

    public TetrahedralizationI() {
        super();
    }

    public TetrahedralizationI(Random random) {
        super(random);
    }

    @Override
    public Vertex<Vertex.IntType>[] getFourCorners() {
        VertexI[] fourCorners = new VertexI[4];
        fourCorners[0] = new VertexI(-1, 1, -1, SCALE);
        fourCorners[1] = new VertexI(1, 1, 1, SCALE);
        fourCorners[2] = new VertexI(1, -1, -1, SCALE);
        fourCorners[3] = new VertexI(-1, -1, 1, SCALE);
        return fourCorners;
    }

    @Override
    Tetrahedron<IntType> newTet(Vertex<IntType> a, Vertex<IntType> b, Vertex<IntType> c, Vertex<IntType> d) {
        return new TetrahedronI(a, b, c, d);
    }

    @Override
    Tetrahedron<IntType> newTet(Vertex<IntType>[] vertices) {
        return new TetrahedronI(vertices);
    }
}
