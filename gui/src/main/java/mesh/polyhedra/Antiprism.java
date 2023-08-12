package mesh.polyhedra;

import javax.vecmath.Vector3d;

import mesh.Face;

/**
 * A class for generating right antiprism meshes.
 *
 * @author Brian Yao
 */
public class Antiprism extends Polyhedron {

    private static double HEIGHT = 1.0;
    private static double RADIUS = 0.5;

    /**
     * Constructs an antiprism with a default radius (0.5) and default height
     * (1.0). The number of sides of the base is the only parameter.
     * 
     * @param numSides
     *            The number of sides the base has.
     */
    public Antiprism(int numSides) {
        this(numSides, RADIUS, HEIGHT);
    }

    /**
     * Constructs an antiprism whose base has the given circumradius and has the
     * given height. The base is a regular n-gon, where n is given as a
     * parameter. With high enough n, the geometry is an approximate cylinder.
     * 
     * @param numSides
     *            The number of sides the base has.
     * @param radius
     *            The circumradius of the base.
     * @param height
     *            The height of the antiprism.
     */
    public Antiprism(int numSides, double radius, double height) {
        double centralAngle = 2.0 * Math.PI / numSides;

        Face bottomFace = new Face(numSides);
        Face topFace = new Face(numSides);
        int vertexIndex = 0;
        for (int i = 0; i < numSides; i++) {
            double totalAngleBottom = centralAngle * i;
            double xb = radius * Math.cos(totalAngleBottom);
            double yb = radius * Math.sin(totalAngleBottom);
            Vector3d bottomVertex = new Vector3d(xb, yb, -height / 2.0);

            double totalAngleTop = totalAngleBottom + 0.5 * centralAngle;
            double xt = radius * Math.cos(totalAngleTop);
            double yt = radius * Math.sin(totalAngleTop);
            Vector3d topVertex = new Vector3d(xt, yt, height / 2.0);

            addVertexPositions(bottomVertex, topVertex);

            bottomFace.setVertexIndex(numSides - i - 1, vertexIndex);
            topFace.setVertexIndex(i, vertexIndex + 1);

            Face bottomTriangle = new Face(3);
            Face topTriangle = new Face(3);
            int nextBottom = (vertexIndex + 2) % (2 * numSides);
            int nextTop = nextBottom + 1;
            bottomTriangle.setAllVertexIndices(vertexIndex, nextBottom,
                                               vertexIndex + 1);
            topTriangle.setAllVertexIndices(vertexIndex + 1, nextBottom,
                                            nextTop);

            addFaces(bottomTriangle, topTriangle);

            vertexIndex += 2;
        }

        addFaces(bottomFace, topFace);

        setVertexNormalsToFaceNormals();
    }

}
