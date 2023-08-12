package mesh.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesh.Edge;
import mesh.Face;
import mesh.Mesh;

/**
 * A data structure which, given a mesh, maps each vertex to a list of edges
 * which share that vertex as an endpoint. The edges are arranged in the same
 * winding order as the vertices of faces. All faces of the mesh must follow the
 * same winding order for this to succeed.
 *
 * For example, by convention, each face has its vertex positions specified in
 * counterclockwise order; then, for each vertex, its adjacent edges are listed
 * in counterclockwise order as well.
 *
 * @author Brian Yao
 */
public class OrderedVertexToAdjacentEdge {

    private Map<Integer, List<Edge>> vertexToEdge;

    /**
     * Construct a mapping using the geometry in the specified mesh.
     * 
     * @param mesh
     *            The mesh whose geometry to use to create the mapping.
     */
    public OrderedVertexToAdjacentEdge(Mesh mesh) {
        vertexToEdge = new HashMap<Integer, List<Edge>>();

        OrderedVertexToAdjacentFace ovtaf = new OrderedVertexToAdjacentFace(mesh);
        for (int i : ovtaf.getVertices()) {
            List<Face> adjacentFaces = ovtaf.getAdjacentFaces(i);
            List<Edge> adjacentEdges = new ArrayList<>();
            for (Face adjFace : adjacentFaces) {
                int indOfI = -1;
                for (int j = 0; j < adjFace.numVertices(); j++) {
                    if (adjFace.getVertexIndex(j) == i) {
                        indOfI = j;
                        break;
                    }
                }

                int beforeI = indOfI - 1;
                if (beforeI < 0) {
                    beforeI += adjFace.numVertices();
                }

                Edge adjEdge = new Edge(adjFace.getVertexIndex(beforeI), i);
                adjEdge.setMesh(mesh);
                adjacentEdges.add(adjEdge);
            }
            vertexToEdge.put(i, adjacentEdges);
        }
    }

    /**
     * Get the list of edges adjacent to the specified vertex. The faces will be
     * listed in the same order as the face vertices.
     * 
     * @param vertexIndex
     *            The index of the vertex whose adjacent edges to return.
     * @return A list of edges adjacent to the vertex at the specified index.
     */
    public List<Edge> getAdjacentEdges(int vertexIndex) {
        return vertexToEdge.get(vertexIndex);
    }

    /**
     * Get the set of vertices (or rather, vertex indices) this structure maps
     * to lists of adjacent edges.
     * 
     * @return The set of vertices which this structure maps to adjacent edges.
     */
    public Set<Integer> getVertices() {
        return vertexToEdge.keySet();
    }

}
