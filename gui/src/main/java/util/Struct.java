package util;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

/**
 * Utility class for data structure related operations.
 *
 * @author Brian Yao
 */
public class Struct {

    /**
     * Deep copy of a list of 3D vectors.
     * 
     * @param vectors
     *            The list of vectors to be copied.
     * @return A copy of the list of vectors.
     */
    public static List<Vector3d> copyVectorList(List<Vector3d> vectors) {
        List<Vector3d> copies = new ArrayList<>();
        for (Vector3d vector : vectors) {
            copies.add(new Vector3d(vector));
        }
        return copies;
    }

}
