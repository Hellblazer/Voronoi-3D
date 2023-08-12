package mesh.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesh.Face;
import mesh.Mesh;

/**
 * A data structure which, given a mesh, maps each vertex to a list of faces
 * which contain that vertex. This is different from VertexToAdjacentFace in
 * that the faces are arranged in the same winding order as the vertices of
 * faces. All faces of the mesh must follow the same winding order for this to
 * succeed.
 *
 * For example, by convention, each face has its vertex positions specified in
 * counterclockwise order; then, for each vertex, its adjacent faces are listed
 * in counterclockwise order as well.
 *
 * @author Brian Yao
 */
public class OrderedVertexToAdjacentFace {

    private Map<Integer, List<Face>> vertexToFace;

    /**
     * Construct a mapping using the geometry in the specified mesh.
     * 
     * @param mesh
     *            The mesh whose geometry to use to create the mapping.
     */
    public OrderedVertexToAdjacentFace(Mesh mesh) {
        vertexToFace = new HashMap<Integer, List<Face>>();
        VertexToAdjacentFace v2af = new VertexToAdjacentFace(mesh);

        Map<Face, Integer> indexOfI = new HashMap<>();
        for (int i : v2af.getVertices()) {
            Set<Face> adjacentFaces = v2af.getAdjacentFaces(i);
            indexOfI.clear();

            if (adjacentFaces != null) {
                // Find at which index vertex i appears in each face
                for (Face adjFace : adjacentFaces) {
                    int ind = -1;

                    findIndexOfI: for (int j = 0; j < adjFace.numVertices(); j++) {
                        if (adjFace.getVertexIndex(j) == i) {
                            ind = j;
                            break findIndexOfI;
                        }
                    }

                    indexOfI.put(adjFace, ind);
                }

                List<Face> orderedAdjacentFaces = new ArrayList<>();
                Face first = adjacentFaces.iterator()
                                          .next();
                orderedAdjacentFaces.add(first);
                while (orderedAdjacentFaces.size() < adjacentFaces.size()) {
                    // In each iteration, find face "next" in order to the last
                    // element in the current state of the list
                    Face lastFace = orderedAdjacentFaces.get(orderedAdjacentFaces.size()
                                                             - 1);

                    // Compute which vertex comes right before vertex i
                    int indexBeforeI = indexOfI.get(lastFace) - 1;
                    if (indexBeforeI < 0) {
                        indexBeforeI += lastFace.numVertices();
                    }
                    int vertexBeforeI = lastFace.getVertexIndex(indexBeforeI);

                    // Find next face in order
                    findNextFace: for (Face adjFace : adjacentFaces) {
                        if (adjFace != lastFace) {
                            int indexAfterI = (indexOfI.get(adjFace) + 1)
                                              % adjFace.numVertices();
                            int vertexAfterI = adjFace.getVertexIndex(indexAfterI);
                            if (vertexAfterI == vertexBeforeI) {
                                orderedAdjacentFaces.add(adjFace);
                                break findNextFace;
                            }
                        }
                    }
                }

                vertexToFace.put(i, orderedAdjacentFaces);
            }
        }
    }

    /**
     * Get the list of faces adjacent to the specified vertex. The faces will be
     * listed in the same order as the face vertices.
     * 
     * @param vertexIndex
     *            The index of the vertex whose adjacent faces to return.
     * @return A list of faces adjacent to the vertex at the specified index.
     */
    public List<Face> getAdjacentFaces(int vertexIndex) {
        return vertexToFace.get(vertexIndex);
    }

    /**
     * Get the set of vertices (or rather, vertex indices) this structure maps
     * to lists of adjacent faces.
     * 
     * @return The set of vertices which this structure maps to adjacent faces.
     */
    public Set<Integer> getVertices() {
        return vertexToFace.keySet();
    }

}
