package mesh.polyhedra.plato;

import javax.vecmath.Vector3d;

import mesh.Face;

/**
 * An implementation of a regular dodecahedron mesh.
 *
 * @author Brian Yao
 */
public class Dodecahedron extends PlatonicSolid {

    private static final double RADIUS_FACTOR = Math.sqrt(3.0) / 4.0
                                                * (1.0 + Math.sqrt(5));

    /**
     * Construct a dodecahedron mesh centered at the origin with the specified
     * edge length.
     * 
     * @param edgeLength
     *            The length of each edge of this mesh.
     */
    public Dodecahedron(double edgeLength) {
        super(edgeLength);

        // Construct vertices
        double goldenRatio = (1.0 + Math.sqrt(5.0)) / 2.0;
        double goldenRatioInv = 1.0 / goldenRatio;
        double edgeScaleFactor = edgeLength / (Math.sqrt(5.0) - 1.0);
        Vector3d[] cubePoints = new Vector3d[8];
        for (int i = 0; i < 8; i++) {
            Vector3d vcube = new Vector3d();
            vcube.z = (i & 1) == 1 ? -1.0 : 1.0;
            vcube.x = ((i >> 1) & 1) == 1 ? -1.0 : 1.0;
            vcube.y = ((i >> 2) & 1) == 1 ? -1.0 : 1.0;
            vcube.scale(edgeScaleFactor);
            cubePoints[i] = vcube;
        }

        Vector3d[] greenVertices = new Vector3d[4];
        Vector3d[] pinkVertices = new Vector3d[4];
        Vector3d[] blueVertices = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            Vector3d vgreen = new Vector3d();
            vgreen.x = (i & 1) == 1 ? -goldenRatio : goldenRatio;
            vgreen.y = ((i >> 1) & 1) == 1 ? -goldenRatioInv : goldenRatioInv;
            vgreen.scale(edgeScaleFactor);
            greenVertices[i] = vgreen;

            Vector3d vpink = new Vector3d();
            vpink.z = (i & 1) == 1 ? -goldenRatio : goldenRatio;
            vpink.x = ((i >> 1) & 1) == 1 ? -goldenRatioInv : goldenRatioInv;
            vpink.scale(edgeScaleFactor);
            pinkVertices[i] = vpink;

            Vector3d vblue = new Vector3d();
            vblue.y = (i & 1) == 1 ? -goldenRatio : goldenRatio;
            vblue.z = ((i >> 1) & 1) == 1 ? -goldenRatioInv : goldenRatioInv;
            vblue.scale(edgeScaleFactor);
            blueVertices[i] = vblue;
        }

        // Cube points: 0-7, green: 8-11, pink: 12-15, blue: 16-19
        addVertexPositions(cubePoints);
        addVertexPositions(greenVertices);
        addVertexPositions(pinkVertices);
        addVertexPositions(blueVertices);

        Face[] faces = new Face[12];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new Face(5);
        }

        // Construct faces
        faces[0].setAllVertexIndices(0, 16, 2, 14, 12);
        faces[1].setAllVertexIndices(1, 13, 15, 3, 18);
        faces[2].setAllVertexIndices(4, 12, 14, 6, 17);
        faces[3].setAllVertexIndices(5, 19, 7, 15, 13);
        faces[4].setAllVertexIndices(0, 12, 4, 10, 8);
        faces[5].setAllVertexIndices(2, 9, 11, 6, 14);
        faces[6].setAllVertexIndices(1, 8, 10, 5, 13);
        faces[7].setAllVertexIndices(3, 15, 7, 11, 9);
        faces[8].setAllVertexIndices(0, 8, 1, 18, 16);
        faces[9].setAllVertexIndices(4, 17, 19, 5, 10);
        faces[10].setAllVertexIndices(2, 16, 18, 3, 9);
        faces[11].setAllVertexIndices(6, 11, 7, 19, 17);

        addFaces(faces);
        setVertexNormalsToFaceNormals();
    }

    /**
     * Construct a dodecahedron mesh with the specified circumradius.
     * 
     * @param circumradius
     *            The circumradius this polyhedron will have.
     * @param dummy
     *            A dummy variable.
     */
    public Dodecahedron(double circumradius, boolean dummy) {
        this(circumradius / RADIUS_FACTOR);
    }

    public Dodecahedron(Vector3d[] vertices) {
        super(PlatonicSolid.edgeLength(vertices));
        addVertexPositions(vertices);

        Face[] faces = new Face[12];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new Face(5);
        }

        @SuppressWarnings("unused")
        int[] v = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                    17, 18, 19 };
        @SuppressWarnings("unused")
        int[][][] faceMap = { { { 0, 1, 2 }, { 2, 1, 5 }, { 2, 5, 6 } },
                              { { 0, 3, 7 }, { 0, 7, 1 }, { 1, 7, 4 } },
                              { { 0, 2, 8 }, { 0, 8, 9 }, { 0, 9, 3 } },
                              { { 1, 4, 10 }, { 1, 10, 5 }, { 5, 10, 11 } },
                              { { 2, 6, 8 }, { 6, 14, 8 }, { 6, 12, 14 } },
                              { { 3, 9, 7 }, { 7, 9, 13 }, { 13, 9, 15 } },
                              { { 4, 7, 10 }, { 10, 7, 16 }, { 16, 7, 13 } },
                              { { 5, 11, 17 }, { 5, 17, 12 }, { 5, 12, 6 } },
                              { { 8, 14, 18 }, { 8, 18, 15 }, { 8, 15, 9 } },
                              { { 10, 16, 19 }, { 10, 19, 11 },
                                { 11, 19, 17 } },
                              { { 12, 17, 19 }, { 12, 19, 18 },
                                { 12, 18, 14 } },
                              { { 13, 15, 18 }, { 13, 18, 19 },
                                { 13, 19, 16 } } };
        // Construct faces
        faces[0].setAllVertexIndices(0, 1, 6, 5, 2);
        faces[1].setAllVertexIndices(0, 3, 7, 1, 4);
        faces[2].setAllVertexIndices(0, 2, 8, 9, 3);
        faces[3].setAllVertexIndices(1, 4, 10, 5, 11);
        faces[4].setAllVertexIndices(2, 6, 8, 14, 12);
        faces[5].setAllVertexIndices(3, 9, 7, 13, 15);
        faces[6].setAllVertexIndices(4, 7, 10, 16, 13);
        faces[7].setAllVertexIndices(5, 11, 17, 12, 6);
        faces[8].setAllVertexIndices(8, 14, 18, 15, 9);
        faces[9].setAllVertexIndices(10, 16, 19, 11, 17);
        faces[10].setAllVertexIndices(12, 17, 19, 18, 14);
        faces[11].setAllVertexIndices(13, 15, 18, 19, 16);

        addFaces(faces);
        setVertexNormalsToFaceNormals();
    }

}
