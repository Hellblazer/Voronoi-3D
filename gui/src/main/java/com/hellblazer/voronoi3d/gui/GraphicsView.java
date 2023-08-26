package com.hellblazer.voronoi3d.gui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import com.hellblazer.delaunay.Vertex;
import com.hellblazer.voronoi3d.gui.mesh.Face;
import com.hellblazer.voronoi3d.gui.mesh.Mesh;
import com.hellblazer.voronoi3d.gui.mesh.PolyLine;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;

public class GraphicsView extends Group {

    public static Point3D p(Tuple3d v) {
        return new Point3D(v.x, v.y, v.z);
    }

    public void newFace(Tuple3d[] verts, PhongMaterial color, boolean showFace, Group group) {
        List<Point3D> vertices;
        if (showFace) {
            Mesh mesh = new Mesh();
            Face face = new Face(verts.length + 1);
            for (var i = 0; i < verts.length; i++) {
                face.setVertexIndex(i, i);
                mesh.addVertexPosition(new Vector3d(verts[i]));
            }
            face.setVertexIndex(verts.length, 0);
            mesh.addFace(face);

            mesh.addVertexNormal(face.getFaceNormal());
            MeshView view = face.constructMeshView();
            view.setCullFace(CullFace.BACK);
            view.setMaterial(color);
            group.getChildren().addAll(view);
        } else {
            vertices = Stream.of(verts).map(v -> p(v)).collect(Collectors.toList());
            vertices.add(vertices.get(0));
            group.getChildren().add(new PolyLine(vertices, 0.01, Colors.blackMaterial));
        }
    }

    public Sphere sphere(double radius, Point3D position, Material material) {
        var sphere = new Sphere();
        sphere.setMaterial(material);
        sphere.setRadius(radius);
        sphere.setTranslateX(position.getX());
        sphere.setTranslateY(position.getY());
        sphere.setTranslateZ(position.getZ());
        return sphere;
    }

    protected void displaySpheres(Stream<Vertex> selected, double aRadius, PhongMaterial aColor, Group group) {
        final var children = group.getChildren();
        selected.forEach(v -> {
            children.add(sphere(aRadius, p(v), aColor));
        });
    }

    protected boolean isAuxillary(Tuple3d[] face) {
        return false;
    }

    protected void render(List<Tuple3d[]> region, PhongMaterial color, boolean showFaces, Group group) {
        for (var face : region) {
            color = render(face, color, showFaces, group);
        }
    }

    protected PhongMaterial render(Tuple3d[] face, PhongMaterial color, boolean showFaces, Group group) {
        if (!isAuxillary(face)) {
            final var c = color.getDiffuseColor();
            color = new PhongMaterial(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0.1));
            newFace(face, color, showFaces, group);
        }
        return color;
    }
}
