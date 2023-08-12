package mesh.polyhedra.plato;

import javax.vecmath.Vector3d;

import mesh.Face;

/**
 * An implementation of a regular tetrahedron mesh.
 *
 * @author Brian Yao
 */
public class Tetrahedron extends PlatonicSolid {

    private static final double RADIUS_FACTOR = Math.sqrt(6.0) / 4.0;

    public static Vector3d[] stdPositions(double edgeLength) {// Vertex positions
        double edgeScaleFactor = edgeLength / (2.0 * Math.sqrt(2.0));
        Vector3d v0 = new Vector3d(1.0, 1.0, 1.0);
        Vector3d v1 = new Vector3d(1.0, -1.0, -1.0);
        Vector3d v2 = new Vector3d(-1.0, 1.0, -1.0);
        Vector3d v3 = new Vector3d(-1.0, -1.0, 1.0);
        v0.scale(edgeScaleFactor);
        v1.scale(edgeScaleFactor);
        v2.scale(edgeScaleFactor);
        v3.scale(edgeScaleFactor);
        return new Vector3d[] { v0, v1, v2, v3 };
    }

    public Tetrahedron(boolean inverse, Vector3d... vectors) {
        super(PlatonicSolid.edgeLength(vectors));
        assert vectors != null && vectors.length == 4;
        addVertexPositions(vectors[0], vectors[1], vectors[2], vectors[3]);

        Face f0 = new Face(3);
        Face f1 = new Face(3);
        Face f2 = new Face(3);
        Face f3 = new Face(3);
        if (inverse) {
            f0.setAllVertexIndices(1, 3, 0);
            f1.setAllVertexIndices(2, 1, 0);
            f2.setAllVertexIndices(3, 1, 2);
            f3.setAllVertexIndices(3, 2, 0);
        } else {
            f0.setAllVertexIndices(0, 3, 1);
            f1.setAllVertexIndices(0, 1, 2);
            f2.setAllVertexIndices(2, 1, 3);
            f3.setAllVertexIndices(0, 2, 3);
        }

        addFaces(f0, f1, f2, f3);
        setVertexNormalsToFaceNormals();
    }

    /**
     * Construct a tetrahedron mesh centered at the origin with the specified
     * edge length.
     * 
     * @param edgeLength
     *            The length of each edge of this mesh.
     */
    public Tetrahedron(double edgeLength) {
        this(false, stdPositions(edgeLength));
    }

    /**
     * Construct a tetrahedron mesh with the specified circumradius.
     * 
     * @param circumradius
     *            The circumradius this polyhedron will have.
     * @param dummy
     *            A dummy variable.
     */
    public Tetrahedron(double circumradius, boolean dummy) {
        this(circumradius / RADIUS_FACTOR);
    }

}
