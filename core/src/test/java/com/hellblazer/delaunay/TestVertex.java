package com.hellblazer.delaunay;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class TestVertex {

    @Test
    public void testOrientation() {
        Vertex[] fourCorners = Tetrahedralization.getFourCorners();
        assertEquals(1, fourCorners[3].orientation(fourCorners[0], fourCorners[1], fourCorners[2]));
        assertEquals(-1, fourCorners[3].orientation(fourCorners[1], fourCorners[0], fourCorners[2]));
        assertEquals(0, new Vertex(100, 100, 0).orientation(new Vertex(1000, 100000, 0), new Vertex(0, -1456, 0),
                                                            new Vertex(-2567, 0, 0)));
        assertEquals(1, fourCorners[0].orientation(fourCorners[2], fourCorners[1], fourCorners[3]));

        assertEquals(1, fourCorners[1].orientation(fourCorners[3], fourCorners[0], fourCorners[2]));

        assertEquals(1, fourCorners[2].orientation(fourCorners[0], fourCorners[3], fourCorners[1]));

        assertEquals(1, fourCorners[3].orientation(fourCorners[1], fourCorners[2], fourCorners[0]));
    }

    @Test
    public void testOrientation2() {
        Vertex[] fourCorners = Tetrahedralization.getFourCorners();
        Vertex N = new Vertex(0, 0, 0);
        Tetrahedron t = new Tetrahedron(fourCorners);
        for (OrientedFace face : t) {
            assertEquals(1, face.orientationOf(N));
        }
        Vertex query = new Vertex(3949, 3002, 8573);
        for (OrientedFace face : t) {
            assertEquals(1, face.orientationOf(query));
        }
    }
}
