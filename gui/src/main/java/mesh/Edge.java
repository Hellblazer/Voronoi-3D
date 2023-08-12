package mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3d;

import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;

/**
 * A class encapsulating a single edge between vertices of a mesh. The edge,
 * much like faces, is specified by the indexes of the vertex positions in the
 * Mesh. It requires exactly two vertices to define an edge by its endpoint
 * vertices.
 *
 * The edge behaves like an unordered pair of integers. The equals() and
 * hashCode() methods have been overridden to ensure this behavior.
 *
 * The edge stores a pointer to the mesh it belongs to, but this is null until
 * this edge is computed with some context. In particular, setMesh() needs to be
 * called for some methods to work properly. This is done implicitly for the
 * edges returned when calling getEdges() on a face.
 *
 * @author Brian Yao
 */
public class Edge {

    private int[] ends;

    private Mesh  mesh;

    /**
     * Create an edge whose endpoints are the specified vertices.
     *
     * @param vertex0
     *            Endpoint vertex.
     * @param vertex1
     *            Endpoint vertex.
     */
    public Edge(int vertex0, int vertex1) {
        ends = new int[2];
        ends[0] = vertex0;
        ends[1] = vertex1;
    }

    public Line createLine(double radius) {
        List<Point3D> endpoints = new ArrayList<>();
        for (Vector3d p : getEndLocations()) {
            endpoints.add(new Point3D(p.x, p.y, p.z));
        }
        return new Line(radius, endpoints.get(1), endpoints.get(0));
    }

    @Override
    public boolean equals(Object obj) {
        Edge other = (Edge) obj;
        return (ends[0] == other.ends[0] && ends[1] == other.ends[1])
               || (ends[0] == other.ends[1] && ends[1] == other.ends[0]);
    }

    /**
     * Get the locations of the endpoints of this edge. setMesh() must have been
     * called for this to succeed.
     *
     * @return The locations of the endpoints of this edge.
     */
    public Vector3d[] getEndLocations() {
        Vector3d[] endLocations = new Vector3d[2];
        endLocations[0] = mesh.vertexPositions.get(ends[0]);
        endLocations[1] = mesh.vertexPositions.get(ends[1]);
        return endLocations;
    }

    public List<Sphere> getEndpointSpheres(double radius) {
        List<Sphere> spheres = new ArrayList<>();
        for (Vector3d vertex : getEndLocations()) {
            Sphere sphere = new Sphere();
            sphere.setRadius(radius);
            sphere.setTranslateX(vertex.x);
            sphere.setTranslateY(vertex.y);
            sphere.setTranslateZ(vertex.z);
            spheres.add(sphere);
        }
        return spheres;
    }

    /**
     * Get the endpoints of this edge, represented by vertex position indices.
     *
     * @return The endpoints of this edge.
     */
    public int[] getEnds() {
        return ends;
    }

    /**
     * Given the vertex index of one of the endpoints, return the vertex index
     * of the other endpoint.
     *
     * @param end
     *            The vertex index of one of the endpoints.
     * @return The vertex index of the other endpoint.
     * @throws IllegalArgumentException
     *             If end is not one of the endpoints of this edge.
     */
    public int getOtherEnd(int end) {
        if (ends[0] == end) {
            return ends[1];
        } else if (ends[1] == end) {
            return ends[0];
        } else {
            throw new IllegalArgumentException(String.format("Vertex %d is not"
                                                             + " an endpoint of this edge.",
                                                             end));
        }
    }

    /**
     * Given the vertex index of one of the endpoints, return the position of
     * the other endpoint. setMesh() must have been called for this to succeed.
     *
     * @param end
     *            The vertex index of one of the endpoints.
     * @return The 3D location of the other endpoint.
     * @throws IllegalArgumentException
     *             If end is not one of the endpoints of this edge.
     */
    public Vector3d getOtherLocation(int end) {
        return mesh.vertexPositions.get(getOtherEnd(end));
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(ends[0])
                      .hashCode()
               * Integer.valueOf(ends[1])
                        .hashCode();
    }

    /**
     * Compute the length of this edge. This requires this edge's mesh to be
     * set.
     *
     * @return The edge's length.
     */
    public double length() {
        Vector3d diff = new Vector3d();
        diff.sub(mesh.vertexPositions.get(ends[0]),
                 mesh.vertexPositions.get(ends[1]));
        return diff.length();
    }

    /**
     * Compute the midpoint along this edge (arithmetic mean of the coordinates
     * of the edge's endpoints). This requires this edge's mesh to be set.
     *
     * @return The coordinate of the edge's midpoint.
     */
    public Vector3d midpoint() {
        Vector3d midpt = new Vector3d();
        midpt.add(mesh.vertexPositions.get(ends[0]),
                  mesh.vertexPositions.get(ends[1]));
        midpt.scale(0.5);
        return midpt;
    }

    /**
     * Set the mesh that this edge points to. Just like with faces, this method
     * needs to be called at some point for certain methods in this class and
     * others to work properly, since Edge objects do not store any geometry;
     * all geometry is stored in the Mesh.
     *
     * @param mesh
     *            The mesh this edge will now point to.
     */
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public String toString() {
        return "Edge:" + Arrays.toString(ends);
    }

}
