package util;

import javax.vecmath.Vector3d;

/**
 * A utility class involving geometric computations.
 *
 * @author Brian Yao
 */
public class Geometry {

    /**
     * Computes the distance from a point to a line in 3D.
     * 
     * @param point
     *            The point we are calculating distance from.
     * @param linePoint1
     *            The first of two points defining the line.
     * @param linePoint2
     *            The second of two points defining the line.
     * @return The distance of point from the line defined by the two line
     *         points.
     */
    public static double pointLineDist(Vector3d point, Vector3d linePoint1,
                                       Vector3d linePoint2) {
        return Math.sqrt(pointLineDistSq(point, linePoint1, linePoint2));
    }

    /**
     * Computes the square of the distance from a point to a line in 3D.
     * 
     * @param point
     *            The point we are calculating distance from.
     * @param linePoint1
     *            The first of two points defining the line.
     * @param linePoint2
     *            The second of two points defining the line.
     * @return The square of the distance of point from the line defined by the
     *         two line points.
     */
    public static double pointLineDistSq(Vector3d point, Vector3d linePoint1,
                                         Vector3d linePoint2) {
        Vector3d lineVector = new Vector3d();
        lineVector.sub(linePoint2, linePoint1);
        Vector3d linePointVector = new Vector3d();
        linePointVector.sub(linePoint1, point);
        double lineVectorLengthSq = lineVector.lengthSquared();

        if (lineVectorLengthSq == 0) {
            return linePointVector.lengthSquared();
        } else {
            Vector3d scaledLineVector = new Vector3d();
            scaledLineVector.scale(linePointVector.dot(lineVector), lineVector);
            Vector3d distanceVector = new Vector3d();
            distanceVector.sub(linePointVector, scaledLineVector);
            return distanceVector.lengthSquared();
        }
    }

}
