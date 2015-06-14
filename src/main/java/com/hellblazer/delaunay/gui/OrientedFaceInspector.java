package com.hellblazer.delaunay.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.hellblazer.delaunay.OrientedFace;
import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Tetrahedron;
import com.hellblazer.delaunay.V;
import com.hellblazer.delaunay.Vertex;

public class OrientedFaceInspector {
    public static void main(String[] argv) {
        final Tetrahedralization tet = new Tetrahedralization(new Random(666));
        for (Vertex v : Examples.getCubicCrystalStructure()) {
            tet.insert(v);
        }
        ArrayList<Tetrahedron> tets = new ArrayList<Tetrahedron>(
                                                                 tet.getTetrahedrons());
        OrientedFaceInspector insp = new OrientedFaceInspector(
                                                               tets.get(2).getFace(V.C));
        insp.open();
    }

    private final JFrame           frame;

    private final OrientedFaceView view;

    public OrientedFaceInspector(OrientedFace face) {
        frame = new JFrame();
        view = new OrientedFaceView(face);
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
