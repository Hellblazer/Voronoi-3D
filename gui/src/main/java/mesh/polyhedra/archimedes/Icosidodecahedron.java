package mesh.polyhedra.archimedes;

import mesh.polyhedra.Polyhedron;
import mesh.polyhedra.plato.Dodecahedron;

/**
 * An implementation of a regular icosidodecahedron mesh.
 *
 * @author Brian Yao
 */
public class Icosidodecahedron extends ArchimedeanSolid {

    private static final double SCALE_FACTOR = Math.sin(Math.toRadians(54));

    /**
     * Construct a icosidodecahedron mesh centered at the origin with the
     * specified edge length.
     * 
     * @param edgeLength
     *            The length of each edge of this mesh.
     */
    public Icosidodecahedron(double edgeLength) {
        Dodecahedron dodec = new Dodecahedron(edgeLength / SCALE_FACTOR);
        Polyhedron dodecAmbo = dodec.ambo();
        addVertexPositions(dodecAmbo.getVertexPositions());
        addVertexNormals(dodecAmbo.getVertexNormals());
        addFaces(dodecAmbo.getFaces());
        setVertexNormalsToFaceNormals();
    }

}
