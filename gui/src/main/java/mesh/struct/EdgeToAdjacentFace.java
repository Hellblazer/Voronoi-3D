package mesh.struct;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mesh.Edge;
import mesh.Face;
import mesh.Mesh;

/**
 * A data structure which computes a mapping from each edge to the two unique
 * faces of the mesh which share that edge. The two faces are stored in an array
 * of length 2.
 *
 * @author Brian Yao
 */
public class EdgeToAdjacentFace {

    private Map<Edge, Face[]> edgeToFace;

    /**
     * Construct a mapping using the geometry in the specified mesh.
     * 
     * @param mesh
     *            The mesh whose geometry to use to create the mapping.
     */
    public EdgeToAdjacentFace(Mesh mesh) {
        edgeToFace = new HashMap<Edge, Face[]>();

        // Compute mapping from edge to adjacent faces
        for (Face face : mesh.getFaces()) {
            Edge[] edges = face.getEdges();
            for (Edge edge : edges) {
                Face[] inMap = edgeToFace.get(edge);
                if (inMap == null) {
                    Face[] facePair = new Face[2];
                    facePair[0] = face;
                    edgeToFace.put(edge, facePair);
                } else if (inMap[1] == null) {
                    inMap[1] = face;
                }
            }
        }
    }

    /**
     * Get the unordered pair of faces which share the specified edge. Returns
     * null if no such edge exists in the mesh.
     * 
     * @param edge
     *            The edge to find adjacent faces of.
     * @return An array of two faces which share the specified edge.
     */
    public Face[] getAdjacentFaces(Edge edge) {
        return edgeToFace.get(edge);
    }

    /**
     * Get the set of edges we have mapped to adjacent faces. (For a closed
     * mesh, this equates to obtaining a set of all edges in the mesh.)
     * 
     * @return The set of mapped edges.
     */
    public Set<Edge> getEdges() {
        return edgeToFace.keySet();
    }

}
