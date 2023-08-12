package mesh.polyhedra;

import javax.vecmath.Vector3d;

import mesh.Face;

/**
 * A class for generating right pyramid meshes.
 *
 * @author Brian Yao
 */
public class Pyramid extends Polyhedron {

    private static double HEIGHT = 1.0;
    private static double RADIUS = 0.5;

    /**
     * Constructs a pyramid with a default radius (0.5) and default height
     * (1.0). The number of sides of the base is the only parameter.
     * 
     * @param numSides
     *            The number of sides the base has.
     */
    public Pyramid(int numSides) {
        this(numSides, RADIUS, HEIGHT);
    }

    /**
     * Constructs a pyramid whose base has the given circumradius and has the
     * given height. The base is a regular n-gon, where n is given as a
     * parameter. With high enough n, the geometry is an approximate cylinder.
     * 
     * @param numSides
     *            The number of sides the base has.
     * @param radius
     *            The circumradius of the base.
     * @param height
     *            The height of the pyramid.
     */
    public Pyramid(int numSides, double radius, double height) {
        double centralAngle = 2.0 * Math.PI / numSides;

        Face bottomFace = new Face(numSides);
        int vertexIndex = 0;
        for (int i = 0; i < numSides; i++) {
            double totalAngle = centralAngle * i;
            double x = radius * Math.cos(totalAngle);
            double y = radius * Math.sin(totalAngle);
            Vector3d bottomVertex = new Vector3d(x, y, -height / 2.0);

            addVertexPosition(bottomVertex);

            bottomFace.setVertexIndex(numSides - i - 1, vertexIndex);

            Face triangle = new Face(3);
            int nextVertex = (vertexIndex + 1) % numSides;
            triangle.setAllVertexIndices(vertexIndex++, nextVertex, numSides);

            addFace(triangle);
        }

        addVertexPosition(new Vector3d(0.0, 0.0, height / 2));
        addFace(bottomFace);

        setVertexNormalsToFaceNormals();
    }

}
