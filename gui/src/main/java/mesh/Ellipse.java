/**
 * Copyright (c) 2018 Chiral Behaviors, LLC, all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mesh;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import com.hellblazer.delaunay.gui.PhiCoordinates;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import mesh.polyhedra.plato.Octahedron;

/**
 * @author halhildebrand
 *
 */
public class Ellipse {
    private static double       SQRT_3 = Math.sqrt(3);
    private static final double TWO_PI = 2 * Math.PI;

    private final Point3D center;
    private Point3D       keyVertex;
    private final Point3D u;
    private final Point3D v;

    public Ellipse(int f, Octahedron oct, int vt) {
        center = new Point3D(0, 0, 0);

        Face face = oct.getFaces().get(f);
        Vector3d c = face.centroid();
        Vector3d vx = face.getVertices().get(vt);

        Point3D centroid = new Point3D(c.x, c.y, c.z);
        Point3D vertex = new Point3D(vx.x, vx.y, vx.z);

        Point3D axis = new Point3D(centroid.getX(), centroid.getY(), centroid.getZ());

        Rotate rotation = new Rotate();
        rotation.setPivotX(centroid.getX());
        rotation.setPivotY(centroid.getY());
        rotation.setPivotZ(centroid.getZ());
        rotation.setAxis(axis.normalize());

        Translate translation = new Translate();
        translation.setX(-axis.getX());
        translation.setY(-axis.getY());
        translation.setZ(-axis.getZ());

        if (PhiCoordinates.JITTERBUG_INVERSES[f]) {
            rotation.setAngle(60);
        } else {
            rotation.setAngle(-60);
        }
        vertex = rotation.transform(vertex);
        vertex = translation.transform(vertex);
        keyVertex = vertex;

        double Z = (oct.getEdgeLength() * Math.sqrt(2)) / SQRT_3;
        Point3D translate = axis.normalize().multiply(Z * Math.cos(0));
        translation.setX(translate.getX());
        translation.setY(translate.getY());
        translation.setZ(translate.getZ());
        rotation.setAngle(0);
        vertex = rotation.transform(vertex);
        vertex = translation.transform(vertex);

        u = vertex.subtract(center);
        v = vertex.subtract(centroid).crossProduct(centroid).normalize().multiply(oct.getEdgeLength() / SQRT_3);
    }

    public Ellipse(Point3D center, Point3D a, Point3D b) {
        u = a.subtract(center);
        v = b.subtract(center);
        this.center = center;
    }

    public PolyLine construct(int segments, Material material, double radius) {
        List<Point3D> points = new ArrayList<>();
        double increment = TWO_PI / segments;
        double theta = 0;
        for (int i = 0; i <= segments; i++) {
            points.add(u.multiply(Math.sin(theta)).add(v.multiply(Math.cos(theta))));
            theta += increment;
        }
        return new PolyLine(points, radius, material);
    }

    public Point3D getCenter() {
        return center;
    }

    public Sphere getKeyVertexSphere() {
        Sphere sphere = new Sphere();
        sphere.setRadius(1);
        sphere.setTranslateX(keyVertex.getX());
        sphere.setTranslateY(keyVertex.getY());
        sphere.setTranslateZ(keyVertex.getZ());
        return sphere;
    }

    public Point3D getU() {
        return u;
    }

    public Point3D getV() {
        return v;
    }
}
