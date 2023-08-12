package mesh.struct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mesh.Face;
import mesh.Mesh;

/**
 * A data structure which, given a mesh, maps each vertex to the set of faces
 * which contain that vertex. The faces are not in any particular order.
 *
 * @author Brian Yao
 */
public class VertexToAdjacentFace {

    private Map<Integer, Set<Face>> vertexToFace;

    /**
     * Construct a mapping using the geometry in the specified mesh.
     * 
     * @param mesh
     *            The mesh whose geometry to use to create the mapping.
     */
    public VertexToAdjacentFace(Mesh mesh) {
        vertexToFace = new HashMap<Integer, Set<Face>>();
        for (Face face : mesh.getFaces()) {
            for (int vertexIndex : face.getVertexIndices()) {
                if (vertexToFace.get(vertexIndex) == null) {
                    vertexToFace.put(vertexIndex, new HashSet<Face>());
                }
                vertexToFace.get(vertexIndex)
                            .add(face);
            }
        }
    }

    /**
     * Get the set of faces adjacent to the vertex specified by the given index.
     * The index is dependent on the mesh provided to the constructor.
     * 
     * @param vertexIndex
     *            The index of the vertex whose adjacent faces to return.
     * @return A set of faces adjacent to the vertex at the specified index.
     */
    public Set<Face> getAdjacentFaces(int vertexIndex) {
        return vertexToFace.get(vertexIndex);
    }

    /**
     * Get the set of vertices (or rather, vertex indices) this structure maps
     * to sets of adjacent faces.
     * 
     * @return The set of vertices which this structure maps to adjacent faces.
     */
    public Set<Integer> getVertices() {
        return vertexToFace.keySet();
    }

}
