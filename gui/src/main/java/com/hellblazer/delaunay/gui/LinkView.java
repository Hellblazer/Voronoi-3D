package com.hellblazer.delaunay.gui;

import java.util.List;

import javax.vecmath.Point3f;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;
import com.hellblazer.delaunay.Vertex.DoubleType;
import com.hellblazer.delaunay.VertexD;

/**
 * A visualization of the link set of a vertex in a delaunay tetrahedralization.
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */
public class LinkView extends GraphicsView {
    private final List<OrientedFace<Vertex.DoubleType>> ears;
    private final VertexD                               v;
    private final List<Point3f[]>                       voronoiRegion;

    public LinkView(VertexD v, List<OrientedFace<Vertex.DoubleType>> ears, List<Point3f[]> voronoiRegion) {
        this.v = v;
        this.ears = ears;
        this.voronoiRegion = voronoiRegion;
        update();
    }

    public void update() {
        getChildren().clear();
        for (OrientedFace<Vertex.DoubleType> ear : ears) {
            Vertex<DoubleType>[] edge = ear.getEdge(v);
            Point3f[] line = new Point3f[] { edge[0].asPoint3f(), edge[1].asPoint3f() };
            newFace(line, Colors.yellowMaterial, false, this);
        }
        sphere(0.01F, new Point3f(new float[] { (float) v.x, (float) v.y, (float) v.z }), Colors.violetMaterial);
        render(voronoiRegion, Colors.redMaterial, true, this);
    }
}
