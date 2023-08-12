package math;

import javax.vecmath.Vector3d;

/**
 * A class with helper vector functions.
 *
 * @author Brian Yao
 */
public class VectorMath {

    private static final double EPSILON = 1e-15;

    /**
     * Returns the difference of two vectors.
     *
     * @param start
     *            The vector being subtracted.
     * @param end
     *            The vector we are subtracting from.
     * @return The difference end - start.
     */
    public static Vector3d diff(Vector3d start, Vector3d end) {
        Vector3d diff = new Vector3d();
        diff.sub(end, start);
        return diff;
    }

    /**
     * Returns the parametric interpolation of two vectors for parameter 0 <= t
     * <= 1. At t = 0 this returns the start, and at t = 1 returns the end. In
     * general, this returns start + (end - start) * t.
     *
     * @param start
     *            The start vector (t = 0).
     * @param end
     *            The end vector (t = 1).
     * @param t
     *            The parameter (0 <= t <= 1).
     * @return The interpolation at the parameter t.
     */
    public static Vector3d interpolate(Vector3d start, Vector3d end, double t) {
        Vector3d diff = diff(start, end);
        diff.scale(t);
        Vector3d newPoint = new Vector3d();
        newPoint.add(start, diff);
        return newPoint;
    }

    /**
     * Returns true if the given vector has at least one component which is NaN.
     *
     * @param vector
     *            The vector whose components we are comparing to NaN.
     * @return True if at least one component is NaN.
     */
    public static boolean isNaN(Vector3d vector) {
        return Double.isNaN(vector.x) || Double.isNaN(vector.y)
               || Double.isNaN(vector.z);
    }

    /**
     * Returns true if the given vector is the zero vector, within a small
     * margin of error (component-wise).
     *
     * @param vector
     *            The vector we are comparing to zero.
     * @return True if the vector is approximately zero.
     */
    public static boolean isZero(Vector3d vector) {
        return Math.abs(vector.x) < EPSILON && Math.abs(vector.y) < EPSILON
               && Math.abs(vector.z) < EPSILON;
    }

}
