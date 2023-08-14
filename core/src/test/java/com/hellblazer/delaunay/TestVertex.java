package com.hellblazer.delaunay;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.hellblazer.delaunay.Vertex.DoubleType;

public class TestVertex {

    @Test
    public void testOrientation() {
        Vertex<DoubleType>[] fourCorners = new TetrahedralizationD().getFourCorners();
        assertEquals(1, fourCorners[3].orientation(fourCorners[0], fourCorners[1], fourCorners[2]));
        assertEquals(-1, fourCorners[3].orientation(fourCorners[1], fourCorners[0], fourCorners[2]));
        assertEquals(0, new VertexD(100, 100, 0).orientation(new VertexD(1000, 100000, 0), new VertexD(0, -1456, 0),
                                                             new VertexD(-2567, 0, 0)));
        assertEquals(1, fourCorners[0].orientation(fourCorners[2], fourCorners[1], fourCorners[3]));

        assertEquals(1, fourCorners[1].orientation(fourCorners[3], fourCorners[0], fourCorners[2]));

        assertEquals(1, fourCorners[2].orientation(fourCorners[0], fourCorners[3], fourCorners[1]));

        assertEquals(1, fourCorners[3].orientation(fourCorners[1], fourCorners[2], fourCorners[0]));
    }

    @Test
    public void testOrientation2() {
        Vertex<DoubleType>[] fourCorners = new TetrahedralizationD().getFourCorners();
        Vertex<DoubleType> N = new VertexD(0, 0, 0);
        TetrahedronD t = new TetrahedronD(fourCorners);
        for (OrientedFace<Vertex.DoubleType> face : t) {
            assertEquals(1, face.orientationOf(N));
        }
        Vertex<DoubleType> query = new VertexD(3949, 3002, 8573);
        for (OrientedFace<DoubleType> face : t) {
            assertEquals(1, face.orientationOf(query));
        }
    }
}
