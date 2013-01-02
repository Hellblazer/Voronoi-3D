package com.hellblazer.delaunay.gui;

/**
 * Copyright (C) 2011 Hal Hildebrand. All rights reserved.
 * 
 * This file is part of the 3D Incremental Voronoi GUI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.hellblazer.delaunay.Tetrahedralization;
import com.hellblazer.delaunay.Vertex;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * 
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 * 
 */

@SuppressWarnings("restriction")
public class Inspector {
    private class Listener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            view.update(displayVoronoi.isSelected(),
                        displayDelaunay.isSelected(),
                        displayFaces.isSelected(), displayPoints.isSelected());
            highlightVoronoiRegions();
        }
    }

    private class TableSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            List<Vertex> selectedVertices = new ArrayList<Vertex>();
            for (int selected : table.getSelectedRows()) {
                selectedVertices.add(vertices.get(selected));
            }
            if (displayVoronoi.isEnabled()) {
                view.highlightRegions(true, selectedVertices);
            } else {
                view.highlightRegions(false, selectedVertices);
            }
        }
    }

    public static final Color COLOR_OF_SELECTED_ROWS = new Color(1.0F, 0.0F,
                                                                 0.0F);

    public static void main(String args[]) {
        final Tetrahedralization tet = new Tetrahedralization(new Random(666));
        for (Vertex v : Examples.getCubicCrystalStructure()) {
            tet.insert(v);
        }
        Inspector insp = new Inspector(tet);
        insp.open();
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

    private JRadioButton displayFaces;
    private JRadioButton displayLines;
    private JCheckBox displayDelaunay;
    private JCheckBox displayPoints;
    private JTable table;
    private JCheckBox displayVoronoi;
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    private TetrahedralizationView view;
    private JFrame frame;

    private ItemListener listener = new Listener();

    public Inspector(Tetrahedralization vd) {
        vertices = new ArrayList<Vertex>(vd.getVertices());
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayPoints = new JCheckBox();
        view = new TetrahedralizationView(
                                          SimpleUniverse.getPreferredConfiguration(),
                                          vd);
        try {
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add("Center", view);
            createWestControlPanel();
            if (displayVoronoi.isEnabled()) {
                displayVoronoi.getItemListeners()[0].itemStateChanged(new ItemEvent(
                                                                                    displayVoronoi,
                                                                                    701,
                                                                                    null,
                                                                                    1));
            }
            if (displayDelaunay.isEnabled()) {
                displayDelaunay.getItemListeners()[0].itemStateChanged(new ItemEvent(
                                                                                     displayDelaunay,
                                                                                     701,
                                                                                     null,
                                                                                     1));
            }
        } catch (Throwable t) {
            t.printStackTrace();
            JOptionPane.showMessageDialog(frame.getParent(),
                                          "An unexpected error occured!\n" + t);
        }
        view.update(displayVoronoi.isSelected(), displayDelaunay.isSelected(),
                    displayFaces.isSelected(), displayPoints.isSelected());
    }

    private void createDiagramTypePanel(JComponent aComponent) {
        aComponent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                                              "Type"));
        displayVoronoi = new JCheckBox("VD", true);
        displayVoronoi.setToolTipText("Toggle to show Voronoi diagram");
        aComponent.add(displayVoronoi);
        displayVoronoi.addItemListener(listener);
        displayDelaunay = new JCheckBox("DT", true);
        displayDelaunay.setToolTipText("Toggle to show Delaunay triangulation");
        aComponent.add(displayDelaunay);
        displayDelaunay.addItemListener(listener);
        aComponent.add(Box.createVerticalStrut(37));
    }

    private void createShowingPanel(JComponent aComponent) {
        javax.swing.border.TitledBorder bF = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                                                              "Show");
        aComponent.setBorder(bF);
        Box showBox = Box.createVerticalBox();
        Box radioButtonBox = Box.createHorizontalBox();
        radioButtonBox.setAlignmentX(0.0F);
        ButtonGroup dtDrawBG = new ButtonGroup();
        displayFaces = new JRadioButton("Faces");
        displayFaces.setToolTipText("Select to show faces of diagrams");
        displayFaces.setAlignmentX(0.0F);
        radioButtonBox.add(displayFaces);
        dtDrawBG.add(displayFaces);
        radioButtonBox.add(Box.createHorizontalStrut(20));
        displayLines = new JRadioButton("Lines");
        displayLines.setSelected(true);
        displayLines.setToolTipText("Select to show diagrams as lines");
        radioButtonBox.add(displayLines);
        dtDrawBG.add(displayLines);
        showBox.add(radioButtonBox);
        showBox.add(Box.createVerticalStrut(15));
        if ("Lines".equalsIgnoreCase("faces")) {
            displayFaces.setSelected(true);
        } else if ("Lines".equalsIgnoreCase("lines")) {
            displayLines.setSelected(true);
        } else {
            displayLines.setSelected(true);
        }
        Box checkBoxBox = Box.createVerticalBox();
        displayPoints = new JCheckBox("Calculated points", true);
        displayPoints.setToolTipText("Toggle to show calculated points");
        displayPoints.setEnabled(true);
        checkBoxBox.add(displayPoints);
        showBox.add(checkBoxBox);
        aComponent.add(showBox);
        displayFaces.addItemListener(listener);
        displayLines.addItemListener(listener);
        displayPoints.addItemListener(listener);
    }

    private void createSliderPanel(JComponent aComponent) {
        if (aComponent == null) {
            aComponent = new JPanel();
        }
        javax.swing.border.TitledBorder bF = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                                                              "Transparency");
        aComponent.setBorder(bF);
        ChangeListener sliderListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider) event.getSource();
                if (source.getValue() > 0) {
                    view.setTransparency(source.getValue());
                }
            }

        };
        JSlider transparencySlider = new JSlider();
        transparencySlider.setToolTipText("Move button to change transparency of diagrams");
        transparencySlider.setMinimum(30);
        transparencySlider.setValue(180);
        transparencySlider.setMaximum(200);
        transparencySlider.addChangeListener(sliderListener);
        view.setTransparency(transparencySlider.getValue());
        Box sliderButtonBox = Box.createHorizontalBox();
        sliderButtonBox.add(transparencySlider);
        aComponent.add(sliderButtonBox);
    }

    private void createWestControlPanel() {
        Box westControlPanel = Box.createVerticalBox();
        Box displayingPanel = Box.createHorizontalBox();
        Box diagramTypePanel = Box.createVerticalBox();
        createDiagramTypePanel(diagramTypePanel);
        displayingPanel.add(diagramTypePanel);
        Box showingPanel = Box.createVerticalBox();
        createShowingPanel(showingPanel);
        displayingPanel.add(showingPanel);
        westControlPanel.add(displayingPanel);
        Box sliderPanel = Box.createHorizontalBox();
        createSliderPanel(sliderPanel);
        westControlPanel.add(sliderPanel);
        JPanel tablePanel = new JPanel(new GridLayout(1, 0));
        table = new JTable(new PointModel(vertices));
        table.setPreferredScrollableViewportSize(new Dimension(200, 1000));
        table.setSelectionBackground(COLOR_OF_SELECTED_ROWS);
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        tablePanel.add(new JScrollPane(table));
        westControlPanel.add(tablePanel);
        westControlPanel.doLayout();
        frame.getContentPane().add("West", westControlPanel);
    }

    private void highlightVoronoiRegions() {
        if (table.getSelectedRowCount() > 0) {
            TableSelectionListener aTSL = new TableSelectionListener();
            aTSL.valueChanged(new ListSelectionEvent(table, 0, 0, false));
        }
    }
}
