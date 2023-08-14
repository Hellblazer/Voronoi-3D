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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class VertexTest {
    @Test
    public void testFlip4to1() {
        Tetrahedralization tetrahedralization = new Tetrahedralization();
        Tetrahedron U = tetrahedralization.myOwnPrivateIdaho();
        VertexD N = new VertexD(100, 100, 100);

        List<OrientedFace> unlinkedFacets = new ArrayList<>();

        U.flip1to4(N, unlinkedFacets);

        tetrahedralization.flip4to1(N);

    }

}
