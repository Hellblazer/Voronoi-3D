package mesh.polyhedra.plato;

import javax.vecmath.Vector3d;

import mesh.Edge;
import mesh.Face;
import mesh.polyhedra.Polyhedron;

/**
 * An implementation of a regular icosahedron mesh.
 *
 * @author Brian Yao
 */
public class Icosahedron extends PlatonicSolid {

    private static final double RADIUS_FACTOR = Math.sin(2.0 * Math.PI / 5.0);

    /**
     * Construct an icosahedron mesh centered at the origin with the specified
     * edge length.
     * 
     * @param edgeLength
     *            The length of each edge of this mesh.
     */
    public Icosahedron(double edgeLength) {
        super(edgeLength);

        // An icosahedron is the dual polyhedron of a dodecahedron
        Dodecahedron dodec = new Dodecahedron(1.0);
        Polyhedron dualDodecahedron = dodec.dual();
        addVertexPositions(dualDodecahedron.getVertexPositions());
        addVertexNormals(dualDodecahedron.getVertexNormals());
        addFaces(dualDodecahedron.getFaces());

        // Scale vertex positions
        Edge sampleEdge = new Edge(faces.get(0)
                                        .getVertexIndex(0),
                                   faces.get(0)
                                        .getVertexIndex(1));
        sampleEdge.setMesh(this);
        double scaleFactor = edgeLength / sampleEdge.length();
        for (Vector3d vertexPos : vertexPositions) {
            vertexPos.scale(scaleFactor);
        }
    }

    /**
     * Construct a icosahedron mesh with the specified circumradius.
     * 
     * @param circumradius
     *            The circumradius this polyhedron will have.
     * @param dummy
     *            A dummy variable.
     */
    public Icosahedron(double circumradius, boolean dummy) {
        this(circumradius / RADIUS_FACTOR);
    }

    public Icosahedron(Vector3d[] vs) {
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
        Face f8 = new Face(3);
        Face f9 = new Face(3);
        Face f10 = new Face(3);
        Face f11 = new Face(3);
        Face f12 = new Face(3);
        Face f13 = new Face(3);
        Face f14 = new Face(3);
        Face f15 = new Face(3);
        Face f16 = new Face(3);
        Face f17 = new Face(3);
        Face f18 = new Face(3);
        Face f19 = new Face(3);

        int[][] faces = { { 0, 1, 3 }, { 0, 2, 1 }, { 0, 3, 7 }, { 0, 7, 4 },
                          { 0, 4, 2 }, { 7, 10, 4 }, { 4, 10, 8 }, { 4, 8, 2 },
                          { 2, 8, 5 }, { 2, 5, 1 }, { 1, 5, 6 }, { 1, 6, 3 },
                          { 3, 6, 9 }, { 3, 9, 7 }, { 7, 9, 10 }, { 11, 10, 9 },
                          { 11, 8, 10 }, { 11, 5, 8 }, { 11, 6, 5 },
                          { 11, 9, 6 } };

        f0.setAllVertexIndices(faces[0]);
        f1.setAllVertexIndices(faces[1]);
        f2.setAllVertexIndices(faces[2]);
        f3.setAllVertexIndices(faces[3]);
        f4.setAllVertexIndices(faces[4]);
        f5.setAllVertexIndices(faces[5]);
        f6.setAllVertexIndices(faces[6]);
        f7.setAllVertexIndices(faces[7]);
        f8.setAllVertexIndices(faces[8]);
        f9.setAllVertexIndices(faces[9]);
        f10.setAllVertexIndices(faces[10]);
        f11.setAllVertexIndices(faces[11]);
        f12.setAllVertexIndices(faces[12]);
        f13.setAllVertexIndices(faces[13]);
        f14.setAllVertexIndices(faces[14]);
        f15.setAllVertexIndices(faces[15]);
        f16.setAllVertexIndices(faces[16]);
        f17.setAllVertexIndices(faces[17]);
        f18.setAllVertexIndices(faces[18]);
        f19.setAllVertexIndices(faces[19]);

        addFaces(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13,
                 f14, f15, f16, f17, f18, f19);
        setVertexNormalsToFaceNormals();
    }

}
