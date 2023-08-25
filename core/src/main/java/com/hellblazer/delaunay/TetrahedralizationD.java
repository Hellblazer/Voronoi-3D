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

import com.hellblazer.delaunay.Vertex.DoubleType;

/**
 * 
 */
public class TetrahedralizationD extends Tetrahedralization<Vertex.DoubleType> {

    /**
     * Scale of the universe
     */
    private static double SCALE = Math.pow(2D, 30D);

    public TetrahedralizationD() {
        super();
    }

    public TetrahedralizationD(Random random) {
        super(random);
    }

    @Override
    public Vertex<Vertex.DoubleType>[] getFourCorners() {
        VertexD[] fourCorners = new VertexD[4];
        fourCorners[0] = new VertexD(-1, 1, -1, SCALE);
        fourCorners[1] = new VertexD(1, 1, 1, SCALE);
        fourCorners[2] = new VertexD(1, -1, -1, SCALE);
        fourCorners[3] = new VertexD(-1, -1, 1, SCALE);
        return fourCorners;
    }

    @Override
    Tetrahedron<DoubleType> newTet(Vertex<DoubleType> a, Vertex<DoubleType> b, Vertex<DoubleType> c,
                                   Vertex<DoubleType> d) {
        return new TetrahedronD(a, b, c, d);
    }

    @Override
    Tetrahedron<DoubleType> newTet(Vertex<DoubleType>[] vertices) {
        return new TetrahedronD(vertices);
    }
}
