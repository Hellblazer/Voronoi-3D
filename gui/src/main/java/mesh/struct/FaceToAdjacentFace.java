package mesh.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mesh.Face;
import mesh.Mesh;

/**
 * A data structure which, given a mesh, contains a map from each face in a mesh
 * to adjacent faces in the mesh. The adjacent faces will be arranged in the
 * same order as the vertices of the face (counterclockwise if the faces follow
 * convention).
 *
 * Two faces are defined to be neighbors if they share an edge (not just one
 * vertex).
 *
 * @author Brian Yao
 */
public class FaceToAdjacentFace {

    private Map<Face, List<Face>> neighborMap;

    /**
     * Construct a mapping using the faces in the specified mesh.
     * 
     * @param mesh
     *            The mesh whose faces to use to create the mapping to neigbors.
     */
    public FaceToAdjacentFace(Mesh mesh) {
        neighborMap = new HashMap<Face, List<Face>>();

        // Populate a map which maps vertices to neighboring faces
        VertexToAdjacentFace v2af = new VertexToAdjacentFace(mesh);

        // Populate face neighbor map
        for (Face face : mesh.getFaces()) {
            List<Face> neighborList = new ArrayList<>();
            for (int i = 0; i < face.numVertices(); i++) {
                int next = (i + 1) % face.numVertices();
                Face neighbor = null;

                // Find neighbor by looking at intersection of the faces neighboring the ith
                // vertex and (i + 1)th vertex of the face
                for (Face ini : v2af.getAdjacentFaces(face.getVertexIndex(i))) {
                    if (v2af.getAdjacentFaces(next)
                            .contains(ini)) {
                        neighbor = ini;
                        break;
                    }
                }

                // Add neighbor to list of neighbors
                if (neighbor != null) {
                    neighborList.add(neighbor);
                }
            }

            if (!neighborList.isEmpty()) {
                neighborMap.put(face, neighborList);
            }
        }
    }

    /**
     * Get the faces neighboring the specified face in the mesh provided to the
     * constructor of this instance. The order of the neighbors is the same as
     * the order of the vertices of the specified face.
     * 
     * @param face
     *            The face whose neighbors to return.
     * @return A list of the neighboring faces.
     */
    public List<Face> getNeighborFaces(Face face) {
        return neighborMap.get(face);
    }

}
