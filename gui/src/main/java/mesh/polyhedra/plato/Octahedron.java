package mesh.polyhedra.plato;

import javax.vecmath.Vector3d;

import mesh.Face;

/**
 * An implementation of a regular octahedron mesh.
 *
 * @author Brian Yao
 */
public class Octahedron extends PlatonicSolid {

    private static final double RADIUS_FACTOR = 1.0 / Math.sqrt(2.0);

    public static Vector3d[] standardPositions(double edgeLength) {
        // Generate vertices
        double distOrigin = edgeLength / Math.sqrt(2.0);
        return new Vector3d[] { // The square 
                                new Vector3d(distOrigin, 0.0, 0.0),
                                new Vector3d(0.0, 0.0, distOrigin),
                                new Vector3d(-distOrigin, 0.0, 0.0),
                                new Vector3d(0.0, 0.0, -distOrigin),

                                // poles
                                new Vector3d(0.0, distOrigin, 0.0),
                                new Vector3d(0.0, -distOrigin, 0.0) };
    }

    /**
     * Construct a octahedron mesh centered at the origin with the specified
     * edge length.
     * 
     * @param edgeLength
     *            The length of each edge of this mesh.
     */
    public Octahedron(double edgeLength) {
        this(edgeLength, standardPositions(edgeLength));
    }

    /**
     * Construct a octahedron mesh with the specified circumradius.
     * 
     * @param circumradius
     *            The circumradius this polyhedron will have.
     * @param dummy
     *            A dummy variable.
     */
    public Octahedron(double circumradius, boolean dummy) {
        this(circumradius / RADIUS_FACTOR);
    }

    public Octahedron(double edgeLength, Vector3d... vectors) {
        super(edgeLength);
        addVertexPositions(vectors);

        // Generate faces
        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;

            Face topFace = new Face(3);
            topFace.setAllVertexIndices(4, next, i);

            Face bottomFace = new Face(3);
            bottomFace.setAllVertexIndices(5, i, next);

            addFaces(topFace, bottomFace);
        }
        setVertexNormalsToFaceNormals();
    }

    public Octahedron(Vector3d[] vs) {
        super(PlatonicSolid.edgeLength(vs));
        addVertexPositions(vs);

        Face f0 = new Face(3);
        Face f1 = new Face(3);
        Face f2 = new Face(3);
        Face f3 = new Face(3);
        Face f4 = new Face(3);
        Face f5 = new Face(3);
        Face f6 = new Face(3);
        Face f7 = new Face(3);

        f0.setAllVertexIndices(5, 4, 2);
        f1.setAllVertexIndices(0, 4, 3);
        f2.setAllVertexIndices(1, 2, 0);
        f3.setAllVertexIndices(5, 1, 3);
        f4.setAllVertexIndices(3, 1, 0);
        f5.setAllVertexIndices(5, 2, 1);
        f6.setAllVertexIndices(5, 3, 4);
        f7.setAllVertexIndices(2, 4, 0);

        addFaces(f0, f1, f2, f3, f4, f5, f6, f7);
        setVertexNormalsToFaceNormals();
    }
}
