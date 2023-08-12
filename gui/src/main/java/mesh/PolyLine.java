/**
 * Copyright (c) 2016 Chiral Behaviors, LLC, all rights reserved.
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

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Material;

/**
 * @author halhildebrand
 *
 */
public class PolyLine extends Group {

    public static PolyLine ellipse(Point3D center, Point3D a, Point3D b,
                                   double radius, int segments,
                                   Material material) {
        Point3D u = a.subtract(center);
        Point3D v = b.subtract(center);

        List<Point3D> points = new ArrayList<>();
        double increment = (2 * Math.PI) / segments;
        double theta = 0;
        for (int i = 0; i <= segments; i++) {
            points.add(u.multiply(Math.cos(theta))
                        .add(v.multiply(Math.sin(theta))));
            theta += increment;
        }

        return new PolyLine(points, radius, material);
    }

    public PolyLine(List<Point3D> points, double radius, Material material) {
        Point3D start = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Line line = new Line(radius, start, points.get(i));
            line.setMaterial(material);
            getChildren().add(line);
            start = points.get(i);
        }
    }
}
