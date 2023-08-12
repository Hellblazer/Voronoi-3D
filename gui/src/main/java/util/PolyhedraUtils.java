package util;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3d;

import math.VectorMath;
import mesh.Edge;
import mesh.Face;
import mesh.polyhedra.Polyhedron;

/**
 * A utility class for polyhedra.
 *
 * @author Brian Yao
 */
public class PolyhedraUtils {

    /**
     * Generates new vertices on each face located midway on the line segment
     * whose endpoints are the face's centroid and the midpoint of one of the
     * edges bordering that face. For a face with n sides, n new vertices are
     * generated for that face.
     *
     * The new vertices are generated using the faces in the "source"
     * polyhedron, and added to the "modify" polyhedron (see params). To do this
     * in-place, ensure that source and modify refer to the same polyhedron.
     *
     * A map is returned to help keep track of the vertex indices of the new
     * vertices. The map is structured such that it maps an ordered pair (a,b)
     * of integers to another integer c. The integer c is the index of one of
     * the newly generated points midway between the midpoint of the edge (a,b)
     * and the centroid of the face to the left of (a,b). This notion of "left"
     * is defined by the face for which following the vertices in the order of a
     * -> b -> c -> a defines a traversal of the perimeter of triangle (a,b,c)
     * in counterclockwise order (as seen from outside the face). This is why we
     * map ordered pairs, so that (a,b) is mapped to the new vertex of one face,
     * and (b,a) is mapped to the new vertex on the other face.
     *
     * @param source
     *            The polyhedron whose faces to use as input.
     * @param modify
     *            The polyhedron to add the new vertices to.
     * @return A map from edges to new vertices as described above.
     */
    public static Map<Integer, Map<Integer, Integer>> addEdgeToCentroidVertices(Polyhedron source,
                                                                                Polyhedron modify) {
        int vertexIndex = modify.numVertexPositions();
        Map<Integer, Map<Integer, Integer>> edgeToVertex = new HashMap<>();
        for (Face face : source.getFaces()) {
            Vector3d centroid = face.centroid();
            Edge[] edges = face.getEdges();
            int[] newFaceVertices = new int[face.numVertices()];

            // Generate new vertices partway from edge midpoint to face centroid
            for (int i = 0; i < edges.length; i++) {
                Vector3d edgeMidpt = edges[i].midpoint();
                Vector3d newVertex = VectorMath.interpolate(edgeMidpt, centroid,
                                                            0.3); // 0 < arbitrary scale factor < 1

                modify.addVertexPosition(newVertex);
                newFaceVertices[i] = vertexIndex++;
            }

            // Populate map from edge to new vertices
            for (int i = 0; i < edges.length; i++) {
                int[] ends = edges[i].getEnds();
                edgeToVertex.computeIfAbsent(ends[0],
                                             a -> new HashMap<Integer, Integer>());
                edgeToVertex.get(ends[0])
                            .put(ends[1], newFaceVertices[i]);
            }
        }

        return edgeToVertex;
    }

    /**
     * Generates rhombic faces in place of the edges in the source polyhedron.
     * This requires new vertices on each face. These new vertices are assumed
     * to be precomputed by
     * {@link util.PolyhedraUtils#addEdgeToCentroidVertices(Polyhedron, Polyhedron)}
     * and are inputs to this method as the third parameter.
     *
     * The new faces are added to the "modify" polyhedron (see params).
     *
     * @param source
     *            The polyhedron whose edges to use as input.
     * @param modify
     *            The polyhedron to add the new faces to.
     * @param edgeToVertex
     *            The map of new vertices, as returned by
     *            {@link util.PolyhedraUtils#addEdgeToCentroidVertices(Polyhedron, Polyhedron)}.
     */
    public static void addRhombicFacesAtEdges(Polyhedron source,
                                              Polyhedron modify,
                                              Map<Integer, Map<Integer, Integer>> edgeToVertex) {
        // Create rhombic faces
        for (Edge edge : source.getEdges()) {
            Face rhombus = new Face(4);
            int[] ends = edge.getEnds();
            int v0 = edgeToVertex.get(ends[0])
                                 .get(ends[1]);
            int v2 = edgeToVertex.get(ends[1])
                                 .get(ends[0]);
            rhombus.setAllVertexIndices(v0, ends[0], v2, ends[1]);

            modify.addFace(rhombus);
        }
    }

    /**
     * Generates equally spaced vertices along each edge such that each edge is
     * divided into n equal segments, where n is the "segments" parameter. The
     * number of vertices added per edge is one less than the "segments"
     * parameter.
     * 
     * For each edge (a,b) where a and b are vertex indices, an entry of the map
     * will map a to another map, which maps b to an array containing the
     * indices of the new vertices along the edge (a,b). The array will be in
     * order, with the first index being the vertex closest to a, and the last
     * index being the vertex closest to b. The reverse mapping is also present,
     * with (b,a) being mapped to the same array, but reversed. The length of
     * each array is one less than the "segments" parameter.
     * 
     * @param source
     *            The polyhedron whose edges to use.
     * @param modify
     *            The polyhedron we are adding the new vertices to.
     * @param segments
     *            The number of segments to divide each edge into.
     * @return The mapping of each edge to the new vertices along it.
     */
    public static Map<Integer, Map<Integer, int[]>> subdivideEdges(Polyhedron source,
                                                                   Polyhedron modify,
                                                                   int segments) {
        Map<Integer, Map<Integer, int[]>> newVertices = new HashMap<>();
        int vertexIndex = modify.getVertexPositions()
                                .size(); // next index
        for (Edge edge : source.getEdges()) {
            int[] ends = edge.getEnds();

            Vector3d[] endPositions = edge.getEndLocations();

            int[] newIndices = new int[segments - 1];
            int[] newIndicesReverse = new int[segments - 1];

            // Generate and add vertices for this edge
            double denom = segments;
            for (int i = 1; i <= segments - 1; i++) {
                Vector3d newVertex = VectorMath.interpolate(endPositions[0],
                                                            endPositions[1],
                                                            i / denom);

                modify.addVertexPosition(newVertex);
                newIndices[i - 1] = vertexIndex;
                newIndicesReverse[segments - i - 1] = vertexIndex;
                vertexIndex++;
            }

            // Map the existing edge to the new vertices along it
            newVertices.computeIfAbsent(ends[0],
                                        a -> new HashMap<Integer, int[]>());
            newVertices.get(ends[0])
                       .put(ends[1], newIndices);

            newVertices.computeIfAbsent(ends[1],
                                        a -> new HashMap<Integer, int[]>());
            newVertices.get(ends[1])
                       .put(ends[0], newIndicesReverse);
        }

        return newVertices;
    }

}
