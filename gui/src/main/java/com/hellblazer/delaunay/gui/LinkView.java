package com.hellblazer.delaunay.gui;

import java.util.List;

import javax.vecmath.Tuple3d;

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
    private final List<Tuple3d[]>    voronoiRegion;

    public LinkView(Vertex v, List<OrientedFace> ears, List<Tuple3d[]> voronoiRegion) {
        this.v = v;
        this.ears = ears;
        this.voronoiRegion = voronoiRegion;
        update();
    }

    public void update() {
        getChildren().clear();
        for (OrientedFace ear : ears) {
            Vertex[] edge = ear.getEdge(v);
            Tuple3d[] line = new Tuple3d[] { edge[0], edge[1] };
            newFace(line, Colors.yellowMaterial, false, this);
        }
        sphere(0.01F, new Point3D(v.x, v.y, v.z), Colors.violetMaterial);
        render(voronoiRegion, Colors.redMaterial, true, this);
    }
}
