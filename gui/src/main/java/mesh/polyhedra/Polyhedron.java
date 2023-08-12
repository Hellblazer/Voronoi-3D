package mesh.polyhedra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

import math.VectorMath;
import mesh.Edge;
import mesh.Face;
import mesh.Mesh;
import mesh.struct.EdgeToAdjacentFace;
import mesh.struct.OrderedVertexToAdjacentEdge;
import mesh.struct.OrderedVertexToAdjacentFace;
import util.Canonicalize;
import util.PolyhedraUtils;

/**
 * A class for generic closed polyhedra meshes. The class contains several
 * operations for creating new polyhedra from existing ones. The operation names
 * and behaviors are based on Conway's polyhedron notation. Each such operation
 * creates an entirely new mesh (the original remains unchanged by the
 * operations).
 *
 * These operations rely heavily on the convention that faces' vertices are all
 * specified in counter-clockwise order.
 *
 * @author Brian Yao
 */
public class Polyhedron extends Mesh {

    /**
     * Create an empty polyhedron.
     */
    public Polyhedron() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param polyhedron
     *            The polyhedron to copy.
     */
    public Polyhedron(Polyhedron polyhedron) {
        super();
        for (Vector3d vertexPos : vertexPositions) {
            addVertexPosition(new Vector3d(vertexPos));
        }
        for (Vector3d normal : vertexNormals) {
            addVertexNormal(new Vector3d(normal));
        }
        for (Face face : faces) {
            addFace(new Face(face));
        }
    }

    /**
     * Computes the rectification of this polyhedron. This involves creating new
     * vertices at the midpoints of the edges of the original polyhedron, and
     * discarding the vertices of the original (they become faces). "Ambo"
     * refers to the name of the operation under Conway's notation.
     * 
     * @return The ambo polyhedron.
     */
    public Polyhedron ambo() {
        Polyhedron amboPolyhedron = new Polyhedron();

        // Create new vertices, one at the midpoint of each edge
        Map<Edge, Integer> newVertices = new HashMap<>();
        int vertexIndex = 0;
        for (Face face : faces) {
            // Create a new face for each face on this polyhedron
            Face newFace = new Face(face.numVertices());
            Edge[] edges = face.getEdges();
            for (int i = 0; i < face.numVertices(); i++) {
                Edge edge = edges[i];
                if (newVertices.get(edge) == null) {
                    Vector3d edgeMidpt = edge.midpoint();
                    amboPolyhedron.addVertexPosition(edgeMidpt);
                    newVertices.put(edge, vertexIndex++);
                }
                newFace.setVertexIndex(i, newVertices.get(edge));
            }
            amboPolyhedron.addFace(newFace);
        }

        // Construct new faces in place of vertices of original
        OrderedVertexToAdjacentEdge ovtae = new OrderedVertexToAdjacentEdge(this);
        for (int i = 0; i < numVertexPositions(); i++) {
            List<Edge> adjacentEdges = ovtae.getAdjacentEdges(i);
            Face newVertexFace = new Face(adjacentEdges.size());
            for (int j = 0; j < adjacentEdges.size(); j++) {
                newVertexFace.setVertexIndex(j,
                                             newVertices.get(adjacentEdges.get(j)));
            }
            amboPolyhedron.addFace(newVertexFace);
        }

        amboPolyhedron.setVertexNormalsToFaceNormals();
        return amboPolyhedron;
    }

    /**
     * Computes the "bevel" polyhedron of this polyhedron. Behaves similarly to
     * expand in that there is one new face for each vertex and edge. Also known
     * as cantitruncation.
     * 
     * @return The bevel polyhedron.
     */
    public Polyhedron bevel() {
        return this.ambo()
                   .truncate();
    }

    /**
     * Canonicalizes this polyhedron until the change in position does not
     * exceed the given threshold. That is, the algorithm terminates when no
     * vertex moves more than the threshold after one iteration.
     * 
     * @param thresholdAdjust
     *            The threshold for change in one "adjust" iteration.
     * @param thresholdPlanarize
     *            The threshold for change in one "planarize" iteration.
     * @return The canonicalized version of this polyhedron.
     */
    public Polyhedron canonicalize(double thresholdAdjust,
                                   double thresholdPlanarize) {
        Polyhedron canonicalized = this.clone();
        Canonicalize.adjust(canonicalized, thresholdAdjust);
        Canonicalize.planarize(canonicalized, thresholdPlanarize);
        return canonicalized;
    }

    /**
     * Canonicalizes this polyhedron for the given number of iterations. See
     * util.Canonicalize for more details. Performs "adjust" followed by
     * "planarize".
     * 
     * @param iterationsAdjust
     *            The number of iterations to "adjust" for.
     * @param iterationsPlanarize
     *            The number of iterations to "planarize" for.
     * @return The canonicalized version of this polyhedron.
     */
    public Polyhedron canonicalize(int iterationsAdjust,
                                   int iterationsPlanarize) {
        Polyhedron canonicalized = this.clone();
        Canonicalize.adjust(canonicalized, iterationsAdjust);
        Canonicalize.planarize(canonicalized, iterationsPlanarize);
        return canonicalized;
    }

    /**
     * Computes the "chamfer" polyhedron of this polyhedron. Truncates edges and
     * replaces them with hexagonal faces.
     * 
     * @return The chamfer polyhedron.
     */
    public Polyhedron chamfer() {
        return this.dual()
                   .subdivide()
                   .dual();
    }

    /**
     * Clones a polyhedron. Equivalent to the copy constructor. Modifications
     * made to the clone are separate from those made to the original.
     * 
     * @return A clone of this polyhedron.
     */
    @Override
    public Polyhedron clone() {
        Polyhedron clone = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            clone.addVertexPosition(new Vector3d(vertexPos));
        }
        for (Vector3d normal : vertexNormals) {
            clone.addVertexNormal(new Vector3d(normal));
        }
        for (Face face : faces) {
            clone.addFace(new Face(face));
        }
        return clone;
    }

    /**
     * Computes the dual polyhedron of this polyhedron. The dual polyhedron has
     * the "opposite" topology, in that it has one vertex for each face of the
     * original polyhedron, and one face for each vertex of the original
     * polyhedron.
     * 
     * @return The dual polyhedron.
     */
    public Polyhedron dual() {
        Polyhedron dualPolyhedron = new Polyhedron();

        // Create new vertices, one at the centroid of each face
        Map<Face, Integer> newVertices = new HashMap<>();
        int vertexIndex = 0;
        for (Face face : faces) {
            Vector3d cent = face.vertexAverage();
            cent.scale(1.0 / cent.lengthSquared()); // Necessary for faces to be planar
            dualPolyhedron.addVertexPosition(cent);
            newVertices.put(face, vertexIndex++);
        }

        // Construct new faces
        OrderedVertexToAdjacentFace ovtaf = new OrderedVertexToAdjacentFace(this);
        for (int i = 0; i < numVertexPositions(); i++) {
            if (ovtaf.getAdjacentFaces(i) != null) {
                List<Face> adjacentFaces = ovtaf.getAdjacentFaces(i);
                Face newFace = new Face(adjacentFaces.size());
                for (int j = 0; j < adjacentFaces.size(); j++) {
                    Face jthFace = adjacentFaces.get(j);
                    int newVertexIndex = newVertices.get(jthFace);
                    newFace.setVertexIndex(j, newVertexIndex);
                }
                dualPolyhedron.addFace(newFace);
            }
        }

        dualPolyhedron.setVertexNormalsToFaceNormals();
        return dualPolyhedron;
    }

    /**
     * Computes the "edge-medial" polyhedron of this polyhedron. Places a vertex
     * at the centroid of every face, and subdivides each edge into n segments,
     * with edges from these subdivision points to the centroid.
     *
     * For example, the "edge-medial-3" operator corresponds to n = 3.
     *
     * @param n
     *            The number of subdivisions on each edge.
     * @return The edge-medial polyhedron with n subdivisions per edge.
     */
    public Polyhedron edgeMedial(int n) {
        return medial(n, true);
    }

    /**
     * Computes the "exalt" polyhedron of this polyhedron. Equivalent to
     * applying needle twice.
     * 
     * @return The exalt polyhedron.
     */
    public Polyhedron exalt() {
        return this.needle()
                   .needle();
    }

    /**
     * Computes the expanded polyhedron of this polyhedron. Each vertex becomes
     * a face, and each edge becomes a quadrilateral face. The appearance of the
     * returned polyhedron looks as if the original polyhedron's faces had been
     * "pulled apart", and additional faces filled the gaps.
     * 
     * @return The expanded polyhedron.
     */
    public Polyhedron expand() {
        return this.ambo()
                   .ambo();
    }

    /**
     * Computes the "gyro" polyhedron of this polyhedron. Each face is divided
     * into n pentagonal faces, where n is the number of vertices in the face.
     * 
     * @return The gyro polyhedron.
     */
    public Polyhedron gyro() {
        Polyhedron gyroPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            gyroPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Create new vertices on edges
        Map<Integer, Map<Integer, int[]>> newVertices = PolyhedraUtils.subdivideEdges(this,
                                                                                      gyroPolyhedron,
                                                                                      3);

        // Generate one vertex per face
        Map<Face, Integer> centroidIndices = new HashMap<>();
        for (Face face : faces) {
            // The centroid of each face becomes a vertex
            Vector3d centroid = face.centroid();
            int centroidIndex = gyroPolyhedron.vertexPositions.size();
            gyroPolyhedron.addVertexPosition(centroid);
            centroidIndices.put(face, centroidIndex);
        }

        // Construct pentagonal faces
        for (Face face : faces) {
            Edge[] faceEdges = face.getEdges();

            int[] prevEnds = faceEdges[faceEdges.length - 1].getEnds();
            int[] prevIndices = newVertices.get(prevEnds[0])
                                           .get(prevEnds[1]);
            for (Edge faceEdge : faceEdges) {
                int[] ends = faceEdge.getEnds();
                int[] edgeIndices = newVertices.get(ends[0])
                                               .get(ends[1]);

                Face pentagon = new Face(5);
                pentagon.setAllVertexIndices(centroidIndices.get(face),
                                             prevIndices[1],
                                             faceEdge.getEnds()[0],
                                             edgeIndices[0], edgeIndices[1]);
                gyroPolyhedron.addFace(pentagon);

                // Update previous indices for next iteration
                prevIndices = edgeIndices;
            }
        }

        gyroPolyhedron.setVertexNormalsToFaceNormals();
        return gyroPolyhedron;
    }

    /**
     * Computes the "join" polyhedron of this polyhedron. Each face of the
     * original polyhedron becomes a raised pyramid, such that for two adjacent
     * pyramids, the faces of the pyramids which share an edge are coplanar. In
     * this way, we obtain a polyhedron made entirely of quadrilateral faces.
     * 
     * @return The join polyhedron.
     */
    public Polyhedron join() {
        return this.ambo()
                   .dual();
    }

    /**
     * Computes the "joined-lace" polyhedron of this polyhedron. Like lace, but
     * old edges are replaced by quadrilateral faces instead of two triangular
     * faces.
     *
     * @return The joined-lace polyhedron.
     */
    public Polyhedron joinedLace() {
        return this.lace(-1, true, true);
    }

    /**
     * Computes the "joined-medial" polyhedron of this polyhedron. The same as
     * medial, but with rhombic faces im place of original edges.
     *
     * @return The joined-medial polyhedron.
     */
    public Polyhedron joinedMedial() {
        Polyhedron medialPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            medialPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Generate new vertices and rhombic faces on original edges
        Map<Integer, Map<Integer, Integer>> edgeToVertex = PolyhedraUtils.addEdgeToCentroidVertices(this,
                                                                                                    medialPolyhedron);
        PolyhedraUtils.addRhombicFacesAtEdges(this, medialPolyhedron,
                                              edgeToVertex);

        // Generate triangular faces
        int vertexIndex = medialPolyhedron.numVertexPositions();
        for (Face face : faces) {
            Vector3d centroid = face.centroid();
            medialPolyhedron.addVertexPosition(centroid);

            Edge[] edges = face.getEdges();
            int[] prevEnds = edges[edges.length - 1].getEnds();
            int prevVertex = edgeToVertex.get(prevEnds[0])
                                         .get(prevEnds[1]);
            for (Edge edge : edges) {
                int[] ends = edge.getEnds();
                int currVertex = edgeToVertex.get(ends[0])
                                             .get(ends[1]);

                Face triangle1 = new Face(3);
                Face triangle2 = new Face(3);
                triangle1.setAllVertexIndices(vertexIndex, ends[0], currVertex);
                triangle2.setAllVertexIndices(vertexIndex, prevVertex, ends[0]);

                medialPolyhedron.addFaces(triangle1, triangle2);

                prevVertex = currVertex;
            }

            vertexIndex++;
        }

        medialPolyhedron.setVertexNormalsToFaceNormals();
        return medialPolyhedron;
    }

    /**
     * Computes the "kis" polyhedron of this polyhedron. Each face of the
     * original polyhedron becomes a raised pyramid; the returned polyhedron is
     * not necessarily convex.
     * 
     * @return The kis polyhedron
     */
    public Polyhedron kis() {
        return this.dual()
                   .needle();
    }

    /**
     * Same as kis, but only divides faces with exactly n sides. All other faces
     * in the original polyhedron are preserved.
     *
     * @param n
     *            The number of sides on the faces we want to kis.
     * @return The polyhedron with kis applied to faces with n sides.
     */
    public Polyhedron kis(int n) {
        Polyhedron kisPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            kisPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        int vertexIndex = kisPolyhedron.numVertexPositions();
        for (Face face : faces) {
            if (face.numVertices() == n) {
                // Only kis if the face has the desired number of sides
                Vector3d centroid = face.centroid();
                kisPolyhedron.addVertexPosition(centroid);

                int prevVertIndex = face.getVertexIndex(face.numVertices() - 1);
                for (int faceVertIndex : face.getVertexIndices()) {
                    Face triangle = new Face(3);
                    triangle.setAllVertexIndices(vertexIndex, prevVertIndex,
                                                 faceVertIndex);
                    kisPolyhedron.addFace(triangle);

                    prevVertIndex = faceVertIndex;
                }

                vertexIndex++;
            } else {
                // Otherwise, use original face
                kisPolyhedron.addFace(new Face(face));
            }
        }

        kisPolyhedron.setVertexNormalsToFaceNormals();
        return kisPolyhedron;
    }

    /**
     * Computes the "lace" polyhedron of this polyhedron. Like loft, but has on
     * each face an antiprism of the original face instead of a prism.
     * 
     * @return The lace polyhedron.
     */
    public Polyhedron lace() {
        return this.lace(-1, true, false);
    }

    /**
     * Computes the "lace" polyhedron of this polyhedron, except the operation
     * is only applied to faces with the specified number of sides.
     *
     * @param n
     *            The number of sides a face needs to have lace applied to it.
     * @return The polyhedron with lace applied to faces with n sides.
     */
    public Polyhedron lace(int n) {
        return this.lace(n, false, false);
    }

    /**
     * Computes the "loft" polyhedron of this polyhedron. Adds a smaller version
     * of this face, with n trapezoidal faces connecting the inner smaller
     * version and the outer original version, where n is the number of vertices
     * the face has.
     * 
     * @return The loft polyhedron.
     */
    public Polyhedron loft() {
        return this.loft(-1, true);
    }

    /**
     * Computes the "loft" polyhedron of this polyhedron, except only faces with
     * the specified number of sides are lofted.
     *
     * @param n
     *            The number of sides a face needs to have loft applied to it.
     * @return The polyhedron with loft applied to faces with n sides.
     */
    public Polyhedron loft(int n) {
        return this.loft(n, false);
    }

    /**
     * Computes the "medial" polyhedron of this polyhedron. Adds vertices at the
     * face centroids and edge midpoints. Each face is split into 2n triangles,
     * where n is the number of vertices in the face. These triangles share a
     * vertex at the face's centroid.
     * 
     * @return The medial polyhedron.
     */
    public Polyhedron medial() {
        return this.medial(2);
    }

    /**
     * Generalized medial, parametrized on the number of subdivisions on each
     * edge. The regular medial operation corresponds to n = 2 subdivisions.
     *
     * @param n
     *            The number of subdivisions on each edge.
     * @return The medial polyhedron with n subdivisions per edge.
     */
    public Polyhedron medial(int n) {
        return medial(n, false);
    }

    /**
     * Computes the "needle" polyhedron of this polyhedron. The centroid of each
     * face becomes a vertex, and for each edge, there are two triangular faces
     * which cross the original edge; the edge shared by these two triangular
     * faces goes between the centroids of the two original faces. The returned
     * polyhedron is not necessarily convex.
     * 
     * @return The needle polyhedron.
     */
    public Polyhedron needle() {
        Polyhedron needlePolyhedron = new Polyhedron();
        needlePolyhedron.addVertexPositions(vertexPositions);

        // Construct new vertices, one at the centroid of each face
        Map<Face, Integer> newVertices = new HashMap<>();
        int vertexIndex = vertexPositions.size();
        for (Face face : faces) {
            Vector3d cent = face.centroid();
            needlePolyhedron.addVertexPosition(cent);
            newVertices.put(face, vertexIndex++);
        }

        // Construct new faces, two per edge
        EdgeToAdjacentFace etaf = new EdgeToAdjacentFace(this);
        for (Edge edge : etaf.getEdges()) {
            int[] edgeEnds = edge.getEnds();
            Face[] adjacentFaces = etaf.getAdjacentFaces(edge);

            // Check orientation: make sure faces we make are specified in
            // CCW order
            for (int i = 0; i < adjacentFaces[0].numVertices(); i++) {
                int v = adjacentFaces[0].getVertexIndex(i);
                if (v == edgeEnds[0]) {
                    int vnext = (i + 1) % adjacentFaces[0].numVertices();
                    if (adjacentFaces[0].getVertexIndex(vnext) != edgeEnds[1]) {
                        // Swap order of adjacent faces
                        Face temp = adjacentFaces[0];
                        adjacentFaces[0] = adjacentFaces[1];
                        adjacentFaces[1] = temp;
                    }
                    break;
                }
            }

            Face topFace = new Face(3);
            Face bottomFace = new Face(3);
            int vface0 = newVertices.get(adjacentFaces[0]);
            int vface1 = newVertices.get(adjacentFaces[1]);
            topFace.setAllVertexIndices(vface0, vface1, edgeEnds[1]);
            bottomFace.setAllVertexIndices(vface1, vface0, edgeEnds[0]);
            needlePolyhedron.addFaces(topFace, bottomFace);
        }

        needlePolyhedron.setVertexNormalsToFaceNormals();
        return needlePolyhedron;
    }

    /**
     * Computes the "ortho" polyhedron of this polyhedron. Each face is divided
     * into n quadrilateral faces, where n is the number of vertices in the
     * face.
     * 
     * @return The ortho polyhedron.
     */
    public Polyhedron ortho() {
        return this.expand()
                   .dual();
    }

    /**
     * Computes the "propellor" polyhedron of this polyhedron. It is like gyro,
     * but instead of having a central vertex we have a central face. This
     * creates quadrilateral faces instead of pentagonal faces.
     * 
     * @return The propellor polyhedron.
     */
    public Polyhedron propellor() {
        Polyhedron propellorPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            propellorPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Create new vertices on edges
        Map<Integer, Map<Integer, int[]>> newVertices = PolyhedraUtils.subdivideEdges(this,
                                                                                      propellorPolyhedron,
                                                                                      3);

        // Create quadrilateral faces and one central face on each face
        for (Face face : faces) {
            Edge[] faceEdges = face.getEdges();

            Face centralFace = new Face(face.numVertices());
            int[] prevEnds = faceEdges[faceEdges.length - 1].getEnds();
            int[] prevEdgeVertices = newVertices.get(prevEnds[0])
                                                .get(prevEnds[1]);
            for (int i = 0; i < faceEdges.length; i++) {
                int[] ends = faceEdges[i].getEnds();
                int[] newEdgeVertices = newVertices.get(ends[0])
                                                   .get(ends[1]);

                Face quad = new Face(4);
                quad.setAllVertexIndices(ends[0], newEdgeVertices[0],
                                         prevEdgeVertices[0],
                                         prevEdgeVertices[1]);
                propellorPolyhedron.addFace(quad);

                centralFace.setVertexIndex(i, newEdgeVertices[0]);

                prevEnds = ends;
                prevEdgeVertices = newEdgeVertices;
            }

            propellorPolyhedron.addFace(centralFace);
        }

        propellorPolyhedron.setVertexNormalsToFaceNormals();
        return propellorPolyhedron;
    }

    /**
     * Compute the "quinto" polyhedron of this polyhedron. Equivalent to an
     * ortho but truncating the vertex at the center of original faces. This
     * creates a small copy of the original face (but rotated).
     * 
     * @return The quinto polyhedron.
     */
    public Polyhedron quinto() {
        Polyhedron quintoPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            quintoPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Create new vertices at the midpoint of each edge and toward the
        // face's centroid
        Map<Integer, Map<Integer, Integer>> edgeToVertex = PolyhedraUtils.addEdgeToCentroidVertices(this,
                                                                                                    quintoPolyhedron);

        int vertexIndex = quintoPolyhedron.numVertexPositions();
        Map<Edge, Integer> midptVertices = new HashMap<>();
        for (Edge edge : this.getEdges()) {
            quintoPolyhedron.addVertexPosition(edge.midpoint());
            midptVertices.put(edge, vertexIndex++);
        }

        // Generate new faces
        for (Face face : faces) {
            Face centralFace = new Face(face.numVertices());
            Edge[] edges = face.getEdges();

            int[] prevEnds = edges[edges.length - 1].getEnds();
            int prevVertex = edgeToVertex.get(prevEnds[0])
                                         .get(prevEnds[1]);
            int prevMidpt = midptVertices.get(edges[edges.length - 1]);
            int centralIndex = 0;
            for (Edge currEdge : edges) {
                int[] currEnds = currEdge.getEnds();
                int currVertex = edgeToVertex.get(currEnds[0])
                                             .get(currEnds[1]);
                int currMidpt = midptVertices.get(currEdge);

                Face pentagon = new Face(5);
                pentagon.setAllVertexIndices(prevVertex, prevMidpt, currEnds[0],
                                             currMidpt, currVertex);
                quintoPolyhedron.addFace(pentagon);

                centralFace.setVertexIndex(centralIndex++, currVertex);

                // Update previous vertex indices
                prevVertex = currVertex;
                prevMidpt = currMidpt;
            }
            quintoPolyhedron.addFace(centralFace);
        }

        quintoPolyhedron.setVertexNormalsToFaceNormals();
        return quintoPolyhedron;
    }

    /**
     * Computes the "snub" polyhedron of this polyhedron. Essentially, each face
     * is twisted, and each edge is replaced with two equilateral triangles.
     * Each vertex also becomes a new face.
     * 
     * @return The snub polyhedron.
     */
    public Polyhedron snub() {
        return this.gyro()
                   .dual();
    }

    /**
     * Computes the "stake" polyhedron of this polyhedron. Like lace, but
     * instead of having a central face, there is a central vertex and
     * quadrilaterals around the center.
     * 
     * @return The stake polyhedron.
     */
    public Polyhedron stake() {
        return this.stake(-1, true);
    }

    /**
     * Computes the "stake" polyhedron of this polyhedron, but only performs the
     * operation on faces with n sides.
     *
     * @param n
     *            The number of sides a face needs to have stake applied to it.
     * @return The polyhedron with stake applied to faces with n sides.
     */
    public Polyhedron stake(int n) {
        return this.stake(n, false);
    }

    /**
     * Computes the "subdivide" polyhedron of this polyhedron. Adds vertices at
     * the midpoints of edges, and creates new triangular faces around original
     * vertices. Equivalent to ambo without removing the original vertices.
     * 
     * @return The subdivide polyhedron.
     */
    public Polyhedron subdivide() {
        Polyhedron subdividePolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            subdividePolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Create new vertices, one at the midpoint of each edge
        Map<Edge, Integer> newVertices = new HashMap<>();
        int vertexIndex = subdividePolyhedron.numVertexPositions();
        for (Face face : faces) {
            // Create a new face for each face on this polyhedron
            Face newFace = new Face(face.numVertices());
            Edge[] edges = face.getEdges();
            for (int i = 0; i < face.numVertices(); i++) {
                Edge edge = edges[i];
                if (newVertices.get(edge) == null) {
                    Vector3d edgeMidpt = edge.midpoint();
                    subdividePolyhedron.addVertexPosition(edgeMidpt);
                    newVertices.put(edge, vertexIndex++);
                }
                newFace.setVertexIndex(i, newVertices.get(edge));
            }
            subdividePolyhedron.addFace(newFace);
        }

        // Create new faces for each vertex
        OrderedVertexToAdjacentEdge ovtae = new OrderedVertexToAdjacentEdge(this);
        for (int i = 0; i < this.numVertexPositions(); i++) {
            List<Edge> adjacentEdges = ovtae.getAdjacentEdges(i);

            Edge prevEdge = adjacentEdges.get(adjacentEdges.size() - 1);
            for (Edge edge : adjacentEdges) {
                int prevVertex = newVertices.get(prevEdge);
                int currVertex = newVertices.get(edge);
                Face triangle = new Face(3);
                triangle.setAllVertexIndices(i, prevVertex, currVertex);

                subdividePolyhedron.addFace(triangle);

                // Update previous edge
                prevEdge = edge;
            }
        }

        subdividePolyhedron.setVertexNormalsToFaceNormals();
        return subdividePolyhedron;
    }

    /**
     * Computes the truncated polyhedron of this polyhedron. Each vertex is
     * truncated, leaving behind a polygon face instead.
     * 
     * @return The truncated polyhedron.
     */
    public Polyhedron truncate() {
        return this.needle()
                   .dual();
    }

    /**
     * Same as truncate, but only truncates vertices with exactly n incident
     * faces. All other vertices in the original polyhedron are preserved.
     *
     * @param n
     *            The order of vertices to truncate.
     * @return The polyhedron with order n vertices truncated.
     */
    public Polyhedron truncate(int n) {
        Polyhedron truncatePolyhedron = new Polyhedron();

        // Compute new vertices
        Map<Integer, Map<Integer, Integer>> edgeVertices = new HashMap<>();
        Map<Integer, Integer> oldToNewIndices = new HashMap<>();
        OrderedVertexToAdjacentEdge ov2ae = new OrderedVertexToAdjacentEdge(this);
        int vertexIndex = 0;
        for (int i = 0; i < vertexPositions.size(); i++) {
            List<Edge> adjEdges = ov2ae.getAdjacentEdges(i);
            if (adjEdges.size() == n) {
                // Only truncate if the vertex has the desired order
                Face truncateFace = new Face(n);
                int faceVertexIndex = 0;
                Vector3d vertPos = vertexPositions.get(i);
                for (Edge edge : adjEdges) {
                    Vector3d otherPos = edge.getOtherLocation(i);
                    Vector3d newVert = VectorMath.interpolate(vertPos, otherPos,
                                                              0.3); // 0 < arbitrary scale factor < 0.5

                    truncatePolyhedron.addVertexPosition(newVert);
                    edgeVertices.computeIfAbsent(i,
                                                 a -> new HashMap<Integer, Integer>());
                    edgeVertices.get(i)
                                .put(edge.getOtherEnd(i), vertexIndex);
                    truncateFace.setVertexIndex(faceVertexIndex++, vertexIndex);
                    vertexIndex++;
                }

                truncatePolyhedron.addFace(truncateFace);
            } else if (!oldToNewIndices.containsKey(i)) {
                // Keep old vertex; only add it once
                truncatePolyhedron.addVertexPosition(vertexPositions.get(i));
                oldToNewIndices.put(i, vertexIndex++);
            }
        }

        for (Face face : faces) {
            List<Integer> newFaceVertices = new ArrayList<>();
            int prevIndex = face.getVertexIndex(face.numVertices() - 1);
            for (int j = 0; j < face.numVertices(); j++) {
                int currIndex = face.getVertexIndex(j);
                if (edgeVertices.containsKey(currIndex)) {
                    // If the vertex was truncated, use two new vertices
                    int nextIndex = face.getVertexIndex((j + 1)
                                                        % face.numVertices());
                    newFaceVertices.add(edgeVertices.get(currIndex)
                                                    .get(prevIndex));
                    newFaceVertices.add(edgeVertices.get(currIndex)
                                                    .get(nextIndex));
                } else {
                    // Otherwise, just use old vertex
                    newFaceVertices.add(oldToNewIndices.get(currIndex));
                }

                // Update previous index
                prevIndex = currIndex;
            }

            Face newFace = new Face(newFaceVertices.size());
            for (int k = 0; k < newFaceVertices.size(); k++) {
                newFace.setVertexIndex(k, newFaceVertices.get(k));
            }
            truncatePolyhedron.addFace(newFace);
        }

        truncatePolyhedron.setVertexNormalsToFaceNormals();
        return truncatePolyhedron;
    }

    /**
     * Computes the "volute" polyhedron of this polyhedron. Equivalent to a snub
     * operation followed by kis on the original faces. This is the dual of
     * whirl.
     * 
     * @return The volute polyhedron.
     */
    public Polyhedron volute() {
        return this.whirl()
                   .dual();
    }

    /**
     * Computes the "whirl" polyhedron of this polyhedron. Forms hexagon faces
     * at each edge, with a small copy of the original face at the center of the
     * original face.
     * 
     * @return The whirl polyhedron.
     */
    public Polyhedron whirl() {
        Polyhedron whirlPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            whirlPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Create new vertices on edges
        Map<Integer, Map<Integer, int[]>> newVertices = PolyhedraUtils.subdivideEdges(this,
                                                                                      whirlPolyhedron,
                                                                                      3);

        // Generate vertices near the center of each face
        Map<Face, int[]> centerVertices = new HashMap<>();
        int vertexIndex = whirlPolyhedron.vertexPositions.size();
        for (Face face : faces) {
            int[] newCenterIndices = new int[face.numVertices()];
            Vector3d centroid = face.centroid();
            int i = 0;
            for (Edge edge : face.getEdges()) {
                int[] ends = edge.getEnds();
                int[] edgeVertices = newVertices.get(ends[0])
                                                .get(ends[1]);
                Vector3d edgePoint = whirlPolyhedron.vertexPositions.get(edgeVertices[1]);
                Vector3d diff = new Vector3d();
                diff.sub(edgePoint, centroid);
                diff.scale(0.3); // 0 < arbitrary scale factor < 1

                Vector3d newFacePoint = new Vector3d();
                newFacePoint.add(centroid, diff);

                whirlPolyhedron.addVertexPosition(newFacePoint);
                newCenterIndices[i++] = vertexIndex++;
            }

            centerVertices.put(face, newCenterIndices);
        }

        // Generate hexagonal faces and central face
        for (Face face : faces) {
            Face centralFace = new Face(face.numVertices());

            Edge[] faceEdges = face.getEdges();
            int[] centralVertices = centerVertices.get(face);
            int[] pEnds = faceEdges[faceEdges.length - 1].getEnds();
            int[] prevEdgeVertices = newVertices.get(pEnds[0])
                                                .get(pEnds[1]);
            int prevCenterIndex = centralVertices[centralVertices.length - 1];
            for (int i = 0; i < face.numVertices(); i++) {
                int[] ends = faceEdges[i].getEnds();
                int[] edgeVertices = newVertices.get(ends[0])
                                                .get(ends[1]);
                int currCenterIndex = centralVertices[i];

                Face hexagon = new Face(6);
                hexagon.setAllVertexIndices(ends[0], edgeVertices[0],
                                            edgeVertices[1], currCenterIndex,
                                            prevCenterIndex,
                                            prevEdgeVertices[1]);
                whirlPolyhedron.addFace(hexagon);

                centralFace.setVertexIndex(i, currCenterIndex);

                prevEdgeVertices = edgeVertices;
                prevCenterIndex = currCenterIndex;
            }

            whirlPolyhedron.addFace(centralFace);
        }

        whirlPolyhedron.setVertexNormalsToFaceNormals();
        return whirlPolyhedron;
    }

    /**
     * Computes the "yank" polyhedron of this polyhedron. Equivalent to applying
     * zip twice.
     * 
     * @return The yank polyhedron.
     */
    public Polyhedron yank() {
        return this.zip()
                   .zip();
    }

    /**
     * Computes the "zip" polyhedron of this polyhedron. Also known as a
     * bitruncation operation, this is equivalent to the truncation of the dual
     * polyhedron and the dual of kis.
     * 
     * @return The zip polyhedron.
     */
    public Polyhedron zip() {
        return this.kis()
                   .dual();
    }

    /**
     * A helper method for implementing lace, parametrized lace, and
     * joined-lace.
     *
     * @param n
     *            The number of sides a face needs to have lace applied to it.
     * @param ignore
     *            True if we want to ignore the parameter n.
     * @param joined
     *            True if we want to compute joined-lace.
     * @return The lace polyhedron.
     */
    private Polyhedron lace(int n, boolean ignore, boolean joined) {
        Polyhedron lacePolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            lacePolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Generate new vertices
        Map<Integer, Map<Integer, Integer>> edgeToVertex = PolyhedraUtils.addEdgeToCentroidVertices(this,
                                                                                                    lacePolyhedron);

        if (joined) {
            PolyhedraUtils.addRhombicFacesAtEdges(this, lacePolyhedron,
                                                  edgeToVertex);
        }

        // Generate new faces
        for (Face face : faces) {
            if (ignore || face.numVertices() == n) {
                Face twist = new Face(face.numVertices());
                Edge[] edges = face.getEdges();

                for (int i = 0; i < edges.length; i++) {
                    // Build face at center of each original face
                    int[] ends = edges[i].getEnds();
                    int newVertex = edgeToVertex.get(ends[0])
                                                .get(ends[1]);
                    twist.setVertexIndex(i, newVertex);

                    // Always generate triangles from vertices to central face
                    int nextInd = (i + 1) % edges.length;
                    int[] nextEnds = edges[nextInd].getEnds();
                    int nextNewVertex = edgeToVertex.get(nextEnds[0])
                                                    .get(nextEnds[1]);

                    Face smallTriangle = new Face(3);
                    smallTriangle.setAllVertexIndices(nextNewVertex, newVertex,
                                                      ends[1]);

                    lacePolyhedron.addFace(smallTriangle);
                }

                lacePolyhedron.addFace(twist);

                if (!joined) {
                    // If not joined, generate triangle faces
                    for (Edge edge : edges) {
                        int[] ends = edge.getEnds();
                        int currVertex = edgeToVertex.get(ends[0])
                                                     .get(ends[1]);

                        Face largeTriangle = new Face(3);
                        largeTriangle.setAllVertexIndices(currVertex, ends[0],
                                                          ends[1]);

                        lacePolyhedron.addFace(largeTriangle);
                    }
                }
            } else {
                // Keep original face
                lacePolyhedron.addFace(new Face(face));
            }
        }

        lacePolyhedron.setVertexNormalsToFaceNormals();
        return lacePolyhedron;
    }

    /**
     * A helper method which implements the loft operation, both the version
     * parametrized on the number of sides of affected faces and the one without
     * the parameter. If the "ignore" flag is set to true, every face is
     * modified.
     *
     * @param n
     *            The number of sides a face needs to have loft applied to it.
     * @param ignore
     *            True if we want to ignore the parameter n.
     * @return The loft polyhedron.
     */
    private Polyhedron loft(int n, boolean ignore) {
        Polyhedron loftPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            loftPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Generate new vertices
        Map<Face, int[]> newVertices = new HashMap<>();
        int vertexIndex = loftPolyhedron.numVertexPositions();
        for (Face face : faces) {
            if (ignore || face.numVertices() == n) {
                Face shrunk = new Face(face.numVertices());
                int[] newFaceVertices = new int[face.numVertices()];

                Vector3d centroid = face.centroid();
                for (int i = 0; i < face.numVertices(); i++) {
                    int index = face.getVertexIndex(i);
                    Vector3d vertex = vertexPositions.get(index);
                    Vector3d newVertex = VectorMath.interpolate(vertex,
                                                                centroid, 0.3);

                    loftPolyhedron.addVertexPosition(newVertex);
                    newFaceVertices[i] = vertexIndex;
                    shrunk.setVertexIndex(i, vertexIndex);
                    vertexIndex++;
                }

                newVertices.put(face, newFaceVertices);
                loftPolyhedron.addFace(shrunk);
            }
        }

        // Generate new faces
        for (Face face : faces) {
            if (newVertices.containsKey(face)) {
                int[] newFaceVertices = newVertices.get(face);
                int prevIndex = face.getVertexIndex(face.numVertices() - 1);
                int newPrevIndex = newFaceVertices[face.numVertices() - 1];
                for (int i = 0; i < face.numVertices(); i++) {
                    int currIndex = face.getVertexIndex(i);
                    int newCurrIndex = newFaceVertices[i];

                    Face trapezoid = new Face(4);
                    trapezoid.setAllVertexIndices(prevIndex, currIndex,
                                                  newCurrIndex, newPrevIndex);
                    loftPolyhedron.addFace(trapezoid);

                    prevIndex = currIndex;
                    newPrevIndex = newCurrIndex;
                }
            } else {
                // Keep original face
                loftPolyhedron.addFace(new Face(face));
            }
        }

        loftPolyhedron.setVertexNormalsToFaceNormals();
        return loftPolyhedron;
    }

    /**
     * A helper method for computing edge-medial and medial (parametrized).
     *
     * @param n
     *            The number of subdivisions per edge.
     * @param edge
     *            True if computing edge-medial, false if regular medial.
     * @return The medial polyhedron subjected to the input constraints.
     */
    private Polyhedron medial(int n, boolean edge) {
        Polyhedron medialPolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            medialPolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Create new vertices on edges
        Map<Integer, Map<Integer, int[]>> newVertices = PolyhedraUtils.subdivideEdges(this,
                                                                                      medialPolyhedron,
                                                                                      n);

        int vertexIndex = medialPolyhedron.numVertexPositions();
        for (Face face : faces) {
            Vector3d centroid = face.centroid();

            Edge[] faceEdges = face.getEdges();

            Edge prevEdge = faceEdges[faceEdges.length - 1];
            int[] prevEnds = prevEdge.getEnds();
            for (Edge currEdge : faceEdges) {
                int[] currEnds = currEdge.getEnds();
                int[] currNewVerts = newVertices.get(currEnds[0])
                                                .get(currEnds[1]);

                int prevLastVert = newVertices.get(prevEnds[0])
                                              .get(prevEnds[1])[n - 2];
                if (edge) {
                    // One quadrilateral face
                    Face quad = new Face(4);
                    quad.setAllVertexIndices(currEnds[0], currNewVerts[0],
                                             vertexIndex, prevLastVert);

                    medialPolyhedron.addFace(quad);
                } else {
                    // Two triangular faces
                    Face triangle1 = new Face(3);
                    Face triangle2 = new Face(3);
                    triangle1.setAllVertexIndices(currEnds[0], currNewVerts[0],
                                                  vertexIndex);
                    triangle2.setAllVertexIndices(vertexIndex, prevLastVert,
                                                  currEnds[0]);

                    medialPolyhedron.addFaces(triangle1, triangle2);
                }

                // Create new triangular faces at edges
                for (int i = 0; i < currNewVerts.length - 1; i++) {
                    Face edgeTriangle = new Face(3);
                    edgeTriangle.setAllVertexIndices(vertexIndex,
                                                     currNewVerts[i],
                                                     currNewVerts[i + 1]);

                    medialPolyhedron.addFace(edgeTriangle);
                }
            }

            medialPolyhedron.addVertexPosition(centroid);
            vertexIndex++;
        }

        medialPolyhedron.setVertexNormalsToFaceNormals();
        return medialPolyhedron;
    }

    /**
     * A helper method for implementing stake and parametrized stake.
     *
     * @param n
     *            The number of sides a face needs to have stake applied to it.
     * @param ignore
     *            True if we want to ignore the parameter n.
     * @return The stake polyhedron.
     */
    private Polyhedron stake(int n, boolean ignore) {
        Polyhedron stakePolyhedron = new Polyhedron();
        for (Vector3d vertexPos : vertexPositions) {
            stakePolyhedron.addVertexPosition(new Vector3d(vertexPos));
        }

        // Generate new vertices
        Map<Integer, Map<Integer, Integer>> edgeToVertex = PolyhedraUtils.addEdgeToCentroidVertices(this,
                                                                                                    stakePolyhedron);

        int vertexIndex = stakePolyhedron.numVertexPositions();
        for (Face face : faces) {
            if (ignore || face.numVertices() == n) {
                Vector3d centroid = face.centroid();
                stakePolyhedron.addVertexPosition(centroid);
                int centroidIndex = vertexIndex++;

                Edge[] edges = face.getEdges();

                // Generate the quads and triangles on this face
                for (int i = 0; i < edges.length; i++) {
                    int[] ends = edges[i].getEnds();
                    int currVertex = edgeToVertex.get(ends[0])
                                                 .get(ends[1]);
                    int[] nextEnds = edges[(i + 1) % edges.length].getEnds();
                    int nextVertex = edgeToVertex.get(nextEnds[0])
                                                 .get(nextEnds[1]);

                    Face triangle = new Face(3);
                    Face quad = new Face(4);
                    triangle.setAllVertexIndices(currVertex, ends[0], ends[1]);
                    quad.setAllVertexIndices(nextVertex, centroidIndex,
                                             currVertex, ends[1]);

                    stakePolyhedron.addFaces(triangle, quad);
                }
            } else {
                // Keep original face
                stakePolyhedron.addFace(new Face(face));
            }
        }

        stakePolyhedron.setVertexNormalsToFaceNormals();
        return stakePolyhedron;
    }

}
