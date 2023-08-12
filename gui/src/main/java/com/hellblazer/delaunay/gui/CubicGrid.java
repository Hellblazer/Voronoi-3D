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

package com.hellblazer.delaunay.gui;

import static com.hellblazer.delaunay.gui.Colors.blueMaterial;
import static com.hellblazer.delaunay.gui.Colors.greenMaterial;
import static com.hellblazer.delaunay.gui.Colors.redMaterial;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.vecmath.Vector3d;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Pair;
import mesh.Line;
import mesh.polyhedra.plato.Cube;

/**
 * @author halhildebrand
 *
 */
public class CubicGrid {

    public static enum Neighborhood {
        EIGHT, SIX
    }

    public static Point3D xAxis(Cube cube) {
        Vector3d vector = cube.getFaces().get(0).centroid();
        return new Point3D(vector.x, vector.y, vector.z);
    }

    public static Point3D yAxis(Cube cube) {
        Vector3d vector = cube.getFaces().get(1).centroid();
        return new Point3D(vector.x, vector.y, vector.z);
    }

    public static Point3D zAxis(Cube cube) {
        Vector3d vector = cube.getFaces().get(2).centroid();
        return new Point3D(vector.x, vector.y, vector.z);
    }

    private final double                 intervalX;
    private final double                 intervalY;
    private final double                 intervalZ;
    private final Neighborhood           neighborhood;
    private Point3D                      origin;
    private final Point3D                xAxis;
    private final Pair<Integer, Integer> xExtent;
    private final Point3D                yAxis;
    private final Pair<Integer, Integer> yExtent;
    private final Point3D                zAxis;
    private final Pair<Integer, Integer> zExtent;

    public CubicGrid(Neighborhood neighborhood) {
        this(neighborhood, new Point3D(0, 0, 0), new Pair<>(5, 5), new Point3D(1, 0, 0), 1, new Pair<>(5, 5),
             new Point3D(0, 1, 0), 1, new Pair<>(5, 5), new Point3D(0, 0, 1), 1);
    }

    public CubicGrid(Neighborhood neighborhood, Cube cube, int extent) {
        this(neighborhood, cube, new Pair<>(extent, extent), new Pair<>(extent, extent), new Pair<>(extent, extent));
    }

    public CubicGrid(Neighborhood neighborhood, Cube cube, Pair<Integer, Integer> xExtent,
                     Pair<Integer, Integer> yExtent, Pair<Integer, Integer> zExtent) {
        this(neighborhood, new Point3D(0, 0, 0), xExtent, xAxis(cube), cube.getEdgeLength(), yExtent, yAxis(cube),
             cube.getEdgeLength(), zExtent, zAxis(cube), cube.getEdgeLength());
    }

    public CubicGrid(Neighborhood neighborhood, Point3D origin, Pair<Integer, Integer> xExtent, Point3D xAxis,
                     double intervalX, Pair<Integer, Integer> yExtent, Point3D yAxis, double intervalY,
                     Pair<Integer, Integer> zExtent, Point3D zAxis, double intervalZ) {
        this.origin = origin;
        this.neighborhood = neighborhood;
        this.xExtent = xExtent;
        this.xAxis = xAxis.subtract(origin).normalize();
        this.intervalX = intervalX;
        this.yExtent = yExtent;
        this.yAxis = yAxis.subtract(origin).normalize();
        this.intervalY = intervalY;
        this.zExtent = zExtent;
        this.zAxis = zAxis.subtract(origin).normalize();
        this.intervalZ = intervalZ;
    }

    public Group construct(Material xaxis, Material yaxis, Material zaxis) {
        Group grid = new Group();
        Point3D pos;
        Point3D neg;
        double bodyOffset = neighborhood == Neighborhood.SIX ? 0.5 : 0;

        final Point3D deltaX = xAxis.multiply(intervalX);
        final Point3D deltaY = yAxis.multiply(intervalY);
        final Point3D deltaZ = zAxis.multiply(intervalZ);

        Point3D corner;
        corner = deltaY.multiply(yExtent.getKey() + bodyOffset).add(deltaZ.multiply(zExtent.getKey() + bodyOffset));
        neg = xAxis.multiply(-intervalX * (xExtent.getKey() + bodyOffset)).subtract(corner);
        pos = xAxis.multiply(intervalX * (xExtent.getValue() + bodyOffset)).subtract(corner);

        construct(grid, neg, pos, yExtent.getKey() + yExtent.getValue(), zExtent.getKey() + zExtent.getValue(), xaxis,
                  (i, p) -> p.add(deltaY.multiply(i)), p -> p.add(deltaZ));

        corner = deltaX.multiply(xExtent.getKey() + bodyOffset).add(deltaZ.multiply(zExtent.getKey() + bodyOffset));
        neg = yAxis.multiply(-intervalY * (yExtent.getKey() + bodyOffset)).subtract(corner);
        pos = yAxis.multiply(intervalY * (yExtent.getValue() + bodyOffset)).subtract(corner);

        construct(grid, neg, pos, xExtent.getKey() + xExtent.getValue(), zExtent.getKey() + zExtent.getValue(), yaxis,
                  (i, p) -> p.add(deltaX.multiply(i)), p -> p.add(deltaZ));

        corner = deltaX.multiply(xExtent.getKey() + bodyOffset).add(deltaY.multiply(yExtent.getKey() + bodyOffset));
        neg = zAxis.multiply(-intervalZ * (zExtent.getKey() + bodyOffset)).subtract(corner);
        pos = zAxis.multiply(intervalZ * (zExtent.getValue() + bodyOffset)).subtract(corner);

        construct(grid, neg, pos, xExtent.getKey() + xExtent.getValue(), yExtent.getKey() + yExtent.getValue(), zaxis,
                  (i, p) -> p.add(deltaX.multiply(i)), p -> p.add(deltaY));

        addAxes(grid);
        return grid;
    }

    public double getIntervalX() {
        return intervalX;
    }

    public double getIntervalY() {
        return intervalY;
    }

    public double getIntervalZ() {
        return intervalZ;
    }

    public Point3D getOrigin() {
        return origin;
    }

    public Point3D getxAxis() {
        return xAxis;
    }

    public Pair<Integer, Integer> getxExtent() {
        return xExtent;
    }

    public Point3D getyAxis() {
        return yAxis;
    }

    public Pair<Integer, Integer> getyExtent() {
        return yExtent;
    }

    public Point3D getzAxis() {
        return zAxis;
    }

    public Pair<Integer, Integer> getzExtent() {
        return zExtent;
    }

    public void postition(double i, double j, double k, Node node) {
        Point3D vector = xAxis.multiply(i * intervalX)
                              .add(yAxis.multiply(j * intervalY))
                              .add(zAxis.multiply(k * intervalZ));
        node.getTransforms().add(new Translate(vector.getX(), vector.getY(), vector.getZ()));
    }

    public Transform postitionTransform(double i, double j, double k) {
        Point3D vector = xAxis.multiply(i * intervalX)
                              .add(yAxis.multiply(j * intervalY))
                              .add(zAxis.multiply(k * intervalZ));
        return new Translate(vector.getX(), vector.getY(), vector.getZ());
    }

    private void addAxes(Group grid) {
        Point3D xPositive = xAxis.multiply(intervalX * xExtent.getKey());
        Line axis = new Line(0.025, xAxis.multiply(-intervalX * xExtent.getKey()), xPositive);
        axis.setMaterial(redMaterial);
        grid.getChildren().addAll(axis);

        Sphere sphere = new Sphere();
        sphere.setMaterial(redMaterial);
        sphere.setRadius(0.25);
        sphere.setTranslateX(xPositive.getX());
        sphere.setTranslateY(xPositive.getY());
        sphere.setTranslateZ(xPositive.getZ());
        grid.getChildren().add(sphere);

        Point3D yPositive = yAxis.multiply(intervalY * yExtent.getKey());
        axis = new Line(0.025, yAxis.multiply(-intervalY * yExtent.getKey()), yPositive);
        axis.setMaterial(blueMaterial);
        grid.getChildren().addAll(axis);
        sphere = new Sphere();
        sphere.setMaterial(blueMaterial);
        sphere.setRadius(0.25);
        sphere.setTranslateX(yPositive.getX());
        sphere.setTranslateY(yPositive.getY());
        sphere.setTranslateZ(yPositive.getZ());
        grid.getChildren().add(sphere);

        Point3D zPositive = zAxis.multiply(intervalZ * zExtent.getKey());
        axis = new Line(0.025, zAxis.multiply(-intervalZ * zExtent.getKey()), zPositive);
        axis.setMaterial(greenMaterial);
        grid.getChildren().addAll(axis);
        sphere = new Sphere();
        sphere.setMaterial(greenMaterial);
        sphere.setRadius(0.25);
        sphere.setTranslateX(zPositive.getX());
        sphere.setTranslateY(zPositive.getY());
        sphere.setTranslateZ(zPositive.getZ());
        grid.getChildren().add(sphere);
    }

    private void construct(Group grid, Point3D neg, Point3D pos, Integer a, Integer b, Material material,
                           BiFunction<Integer, Point3D, Point3D> advanceA, Function<Point3D, Point3D> advanceB) {
        a = neighborhood == Neighborhood.SIX ? a + 1 : a;
        b = neighborhood == Neighborhood.SIX ? b + 1 : b;
        Point3D start = neg;
        Point3D end = pos;
        Line axis;
        axis = new Line(0.015, start, end);
        axis.setMaterial(material);
        grid.getChildren().addAll(axis);
        for (int x = 0; x <= a; x++) {
            start = advanceA.apply(x, neg);
            end = advanceA.apply(x, pos);
            axis = new Line(0.015, start, end);
            axis.setMaterial(material);
            grid.getChildren().addAll(axis);
            for (int z = 0; z < b; z++) {
                start = advanceB.apply(start);
                end = advanceB.apply(end);
                axis = new Line(0.015, start, end);
                axis.setMaterial(material);
                grid.getChildren().addAll(axis);
            }
        }
        if (neighborhood == Neighborhood.SIX) {
            start = advanceA.apply(a, neg);
            end = advanceA.apply(a, pos);
            axis = new Line(0.015, start, end);
            axis.setMaterial(material);
            grid.getChildren().addAll(axis);
        }
    }
}
