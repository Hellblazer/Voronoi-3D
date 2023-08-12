package mesh.polyhedra.archimedes;

import mesh.polyhedra.Polyhedron;
import mesh.polyhedra.plato.Cube;

/**
 * An implementation of a regular cuboctahedron mesh.
 *
 * @author Brian Yao
 */
public class Cuboctahedron extends ArchimedeanSolid {

    /**
     * Construct a cuboctahedron mesh centered at the origin with the specified
     * edge length.
     * 
     * @param edgeLength
     *            The length of each edge of this mesh.
     */
    public Cuboctahedron(double edgeLength) {
        Cube cube = new Cube(Math.sqrt(2.0) * edgeLength);
        Polyhedron cubeAmbo = cube.ambo();
        addVertexPositions(cubeAmbo.getVertexPositions());
        addVertexNormals(cubeAmbo.getVertexNormals());
        addFaces(cubeAmbo.getFaces());
        setVertexNormalsToFaceNormals();
    }

}
