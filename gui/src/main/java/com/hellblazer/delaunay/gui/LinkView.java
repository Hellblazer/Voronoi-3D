package com.hellblazer.delaunay.gui;

import java.util.List;

import javax.vecmath.Point3f;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;

import javafx.geometry.Point3D;

/**
 * A visualization of the link set of a vertex in a delaunay tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class LinkView extends GraphicsView {
    private final List<OrientedFace> ears;
    private final Vertex             v;
    private final List<Point3f[]>    voronoiRegion;

    public LinkView(Vertex v, List<OrientedFace> ears, List<Point3f[]> voronoiRegion) {
        this.v = v;
        this.ears = ears;
        this.voronoiRegion = voronoiRegion;
        update();
    }

    public void update() {
        createDiagram();
        for (OrientedFace ear : ears) {
            Vertex[] edge = ear.getEdge(v);
            Point3f[] line = new Point3f[] { edge[0].asPoint3f(), edge[1].asPoint3f() };
            newFace(line, Colors.yellowMaterial, false);
        }
        sphere(0.01F, new Point3D(v.x, v.y, v.z), Colors.violetMaterial);
        render(voronoiRegion, Colors.redMaterial, true);
    }
}
