package com.hellblazer.delaunay.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.vecmath.Point3f;

import com.hellblazer.delaunay.Examples;
import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Vertex;

public class LinkInspector {
    public static void main(String[] argv) {
        final Tetrahedralization tet = new Tetrahedralization(new Random(666));
        Vertex[] vertices = Examples.getCubicCrystalStructure();
        for (Vertex v : vertices) {
            tet.insert(v);
        }
        Vertex v = vertices[13];
        new LinkInspector(v, tet.getEars(v), tet.getVoronoiRegion(v)).open();
    }

    private final JFrame   frame;

    private final LinkView view;

    public LinkInspector(Vertex v, List<OrientedFace> ears,
                         List<Point3f[]> voronoiRegion) {
        frame = new JFrame();
        view = new LinkView(v, ears, voronoiRegion);
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("Center", view);
    }

    public void open() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
