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

import com.hellblazer.delaunay.Vertex.FloatType;

/**
 * 
 */
public class TetrahedralizationF extends Tetrahedralization<Vertex.FloatType> {

    /**
     * Scale of the universe
     */
    private static float SCALE = (float) Math.pow(2F, 30F);

    public TetrahedralizationF() {
        super();
    }

    public TetrahedralizationF(Random random) {
        super(random);
    }

    @Override
    public Vertex<Vertex.FloatType>[] getFourCorners() {
        VertexF[] fourCorners = new VertexF[4];
        fourCorners[0] = new VertexF(-1, 1, -1, SCALE);
        fourCorners[1] = new VertexF(1, 1, 1, SCALE);
        fourCorners[2] = new VertexF(1, -1, -1, SCALE);
        fourCorners[3] = new VertexF(-1, -1, 1, SCALE);
        return fourCorners;
    }

    @Override
    Tetrahedron<FloatType> newTet(Vertex<FloatType> a, Vertex<FloatType> b, Vertex<FloatType> c, Vertex<FloatType> d) {
        return new TetrahedronF(a, b, c, d);
    }

    @Override
    Tetrahedron<FloatType> newTet(Vertex<FloatType>[] vertices) {
        return new TetrahedronF(vertices);
    }
}
