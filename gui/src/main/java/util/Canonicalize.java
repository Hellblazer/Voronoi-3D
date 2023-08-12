package util;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import math.VectorMath;
import mesh.Face;
import mesh.polyhedra.Polyhedron;

/**
 * A Java implementation of precisely the iterative algorithm for computing
 * canonical polyhedra designed by George W. Hart. All the code in this class is
 * based directly off of his work.
 *
 * Further information on this algorithm and what it does is on Hart's website.
 * See this link: http://www.georgehart.com/canonical/canonical-supplement.html
 *
 * @author Brian Yao
 */
public class Canonicalize {

    private static final double MAX_VERTEX_CHANGE = 1.0;

    /**
     * Canonicalizes a polyhedron by adjusting its vertices iteratively. When no
     * vertex moves more than the given threshold, the algorithm terminates.
     * 
     * @param poly
     *            The polyhedron whose vertices to adjust.
     * @param threshold
     *            The threshold of vertex movement after an iteration.
     * @return The number of iterations that were executed.
     */
    public static int adjust(Polyhedron poly, double threshold) {
        return canonicalize(poly, threshold, false);
    }

    /**
     * Canonicalizes a polyhedron by adjusting its vertices iteratively.
     * 
     * @param poly
     *            The polyhedron whose vertices to adjust.
     * @param numIterations
     *            The number of iterations to adjust for.
     */
    public static void adjust(Polyhedron poly, int numIterations) {
        Polyhedron dual = poly.dual();
        for (int i = 0; i < numIterations; i++) {
            List<Vector3d> newDualPositions = reciprocalCenters(poly);
            dual.setVertexPositions(newDualPositions);
            List<Vector3d> newPositions = reciprocalCenters(dual);
            poly.setVertexPositions(newPositions);
        }
        poly.setVertexNormalsToFaceNormals();
    }

    /**
     * Modifies a polyhedron's vertices such that faces are closer to planar.
     * When no vertex moves more than the given threshold, the algorithm
     * terminates.
     *
     * @param poly
     *            The polyhedron to canonicalize.
     * @param threshold
     *            The threshold of vertex movement after an iteration.
     * @return The number of iterations that were executed.
     */
    public static int planarize(Polyhedron poly, double threshold) {
        return canonicalize(poly, threshold, true);
    }

    /**
     * Modifies a polyhedron's vertices such that faces are closer to planar.
     * The more iterations, the closer the faces are to planar. If a vertex
     * moves by an unexpectedly large amount, or if the new vertex position has
     * an NaN component, the algorithm automatically terminates.
     *
     * @param poly
     *            The polyhedron whose faces to planarize.
     * @param numIterations
     *            The number of iterations to planarize for.
     */
    public static void planarize(Polyhedron poly, int numIterations) {
        Polyhedron dual = poly.dual();
        for (int i = 0; i < numIterations; i++) {
            List<Vector3d> newDualPositions = reciprocalVertices(poly);
            dual.setVertexPositions(newDualPositions);
            List<Vector3d> newPositions = reciprocalVertices(dual);

            double maxChange = 0.;
            for (int j = 0; j < poly.getVertexPositions()
                                    .size(); j++) {
                Vector3d newPos = poly.getVertexPositions()
                                      .get(j);
                Vector3d diff = VectorMath.diff(newPos,
                                                poly.getVertexPositions()
                                                    .get(j));
                maxChange = Math.max(maxChange, diff.length());
            }

            // Check if an error occurred in computation. If so, terminate
            // immediately. This likely occurs when faces are already planar.
            if (VectorMath.isNaN(newPositions.get(0))) {
                break;
            }

            // Check if the position changed by a significant amount so as to
            // be erroneous. If so, terminate immediately
            if (maxChange > MAX_VERTEX_CHANGE) {
                break;
            }

            poly.setVertexPositions(newPositions);
        }
        poly.setVertexNormalsToFaceNormals();
    }

    /**
     * A helper method for threshold-based termination in both planarizing and
     * adjusting. If a vertex moves by an unexpectedly large amount, or if the
     * new vertex position has an NaN component, the algorithm automatically
     * terminates.
     *
     * @param poly
     *            The polyhedron to canonicalize.
     * @param threshold
     *            The threshold of vertex movement after an iteration.
     * @param planarize
     *            True if we are planarizing, false if we are adjusting.
     * @return The number of iterations that were executed.
     */
    private static int canonicalize(Polyhedron poly, double threshold,
                                    boolean planarize) {
        Polyhedron dual = poly.dual();
        List<Vector3d> currentPositions = Struct.copyVectorList(poly.getVertexPositions());

        int iterations = 0;
        while (true) {
            List<Vector3d> newDualPositions = planarize ? reciprocalVertices(poly)
                                                        : reciprocalCenters(poly);
            dual.setVertexPositions(newDualPositions);
            List<Vector3d> newPositions = planarize ? reciprocalVertices(dual)
                                                    : reciprocalCenters(dual);

            double maxChange = 0.;
            for (int i = 0; i < currentPositions.size(); i++) {
                Vector3d newPos = poly.getVertexPositions()
                                      .get(i);
                Vector3d diff = VectorMath.diff(newPos,
                                                currentPositions.get(i));
                maxChange = Math.max(maxChange, diff.length());
            }

            // Check if an error occurred in computation. If so, terminate
            // immediately
            if (VectorMath.isNaN(newPositions.get(0))) {
                break;
            }

            // Check if the position changed by a significant amount so as to
            // be erroneous. If so, terminate immediately
            if (planarize && maxChange > MAX_VERTEX_CHANGE) {
                break;
            }

            poly.setVertexPositions(newPositions);

            if (maxChange < threshold) {
                break;
            }

            currentPositions = Struct.copyVectorList(poly.getVertexPositions());
            iterations++;
        }

        poly.setVertexNormalsToFaceNormals();
        return iterations;
    }

    /**
     * A port of the "reciprocalC" function written by George Hart. Reflects the
     * centers of faces across the unit sphere.
     * 
     * @param poly
     *            The polyhedron whose centers to invert.
     * @return The list of inverted face centers.
     */
    private static List<Vector3d> reciprocalCenters(Polyhedron poly) {
        List<Vector3d> faceCenters = new ArrayList<>();
        for (Face face : poly.getFaces()) {
            Vector3d newCenter = new Vector3d(face.vertexAverage());
            newCenter.scale(1.0 / newCenter.lengthSquared());
            faceCenters.add(newCenter);
        }
        return faceCenters;
    }

    /**
     * A port of the "reciprocalN" function written by George Hart.
     *
     * @param poly
     *            The polyhedron to apply this canonicalization to.
     * @return A list of the new vertices of the dual polyhedron.
     */
    private static List<Vector3d> reciprocalVertices(Polyhedron poly) {
        List<Vector3d> newVertices = new ArrayList<>();

        List<Vector3d> vertexPositions = poly.getVertexPositions();
        for (Face face : poly.getFaces()) {
            // Initialize values which will be updated in the loop below
            Vector3d centroid = face.vertexAverage();
            Vector3d normalSum = new Vector3d();
            double avgEdgeDistance = 0.;

            // Retrieve the indices of the vertices defining this face
            int[] faceVertexIndices = face.getVertexIndices();

            // Keep track of the "previous" two vertices in CCW order
            int lastLastVertexIndex = faceVertexIndices[faceVertexIndices.length
                                                        - 2];
            int lastVertexIndex = faceVertexIndices[faceVertexIndices.length
                                                    - 1];
            for (int vertexIndex : faceVertexIndices) {
                Vector3d vertex = vertexPositions.get(vertexIndex);

                // Compute the normal of the plane defined by this vertex and
                // the previous two
                Vector3d lastlastVertex = vertexPositions.get(lastLastVertexIndex);
                Vector3d lastVertex = vertexPositions.get(lastVertexIndex);
                Vector3d v1 = new Vector3d(lastlastVertex);
                v1.sub(lastVertex);
                Vector3d v2 = new Vector3d(vertex);
                v2.sub(lastVertex);
                Vector3d normal = new Vector3d();
                normal.cross(v1, v2);
                normalSum.add(normal);

                // Compute distance from edge to origin
                avgEdgeDistance += Geometry.pointLineDist(new Vector3d(),
                                                          lastlastVertex,
                                                          lastVertex);

                // Update the previous vertices for the next iteration
                lastLastVertexIndex = lastVertexIndex;
                lastVertexIndex = vertexIndex;
            }

            normalSum.normalize();
            avgEdgeDistance /= faceVertexIndices.length;

            Vector3d resultingVector = new Vector3d();
            resultingVector.scale(centroid.dot(normalSum), normalSum);
            resultingVector.scale(1.0 / resultingVector.lengthSquared());
            resultingVector.scale((1.0 + avgEdgeDistance) / 2.0);
            newVertices.add(resultingVector);
        }

        return newVertices;
    }

}
