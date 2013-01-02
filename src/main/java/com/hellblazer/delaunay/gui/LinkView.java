package com.hellblazer.delaunay.gui;

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3f;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Vertex;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * A visualization of the link set of a vertex in a delaunay tetrahedralization.
 * 
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 * 
 */
@SuppressWarnings("restriction")
public class LinkView extends GraphicsView {
    private static final long serialVersionUID = 1L;
    private final Vertex v;
    private final List<OrientedFace> ears;
    private final List<Point3f[]> voronoiRegion;

    public LinkView(GraphicsConfiguration gc, Vertex v,
                    List<OrientedFace> ears, List<Point3f[]> voronoiRegion) {
        super(gc);
        this.v = v;
        this.ears = ears;
        this.voronoiRegion = voronoiRegion;
        createSceneGraph();
        update();
    }

    public LinkView(Vertex v, List<OrientedFace> ears) {
        this(SimpleUniverse.getPreferredConfiguration(), v, ears,
             new ArrayList<Point3f[]>());
    }

    public LinkView(Vertex v, List<OrientedFace> ears,
                    List<Point3f[]> voronoiRegion) {
        this(SimpleUniverse.getPreferredConfiguration(), v, ears, voronoiRegion);
    }

    public void update() {
        createDiagram();
        for (OrientedFace ear : ears) {
            Vertex[] edge = ear.getEdge(v);
            Point3f[] line = new Point3f[] { edge[0].asPoint3f(),
                                            edge[1].asPoint3f() };
            diagram.addChild(newFace(line, false, COLOR_OF_DT));
        }
        diagram.addChild(createSphereAround(v.asPoint3f(),
                                            COLOR_OF_HIGHLIGHTED_REGION, 0.03F));
        transformGroup.addChild(diagram);
        TransparencyAttributes highlightTransparency = new TransparencyAttributes(
                                                                                  2,
                                                                                  (float) (Math.log(50) / Math.log(200D)),
                                                                                  2,
                                                                                  1);
        Appearance appearance = getCapabilities(highlightTransparency);
        BranchGroup highlightedRegions = new BranchGroup();
        highlightedRegions.setCapability(GROUP_ALLOW_CHILDREN_EXTEND);
        highlightedRegions.setCapability(GROUP_ALLOW_CHILDREN_READ);
        highlightedRegions.setCapability(BRANCH_GROUP_ALLOW_DETACH);
        render(voronoiRegion, COLOR_OF_HIGHLIGHTED_REGION, highlightedRegions,
               true, appearance);
        diagram.addChild(highlightedRegions);
        doLayout();
    }
}
