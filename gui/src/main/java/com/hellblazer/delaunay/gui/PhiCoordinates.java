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

import static com.hellblazer.delaunay.gui.Constants.PHI;
import static com.hellblazer.delaunay.gui.Constants.PHI_CUBED;
import static com.hellblazer.delaunay.gui.Constants.PHI_SQUARED;

import javax.vecmath.Vector3d;

import mesh.polyhedra.plato.Cube;
import mesh.polyhedra.plato.Dodecahedron;
import mesh.polyhedra.plato.Icosahedron;
import mesh.polyhedra.plato.Octahedron;
import mesh.polyhedra.plato.Tetrahedron;

/**
 * @author halhildebrand
 *
 *         Coordinates for all the polyhedra in the 120 face polyhedron, in phi
 *         coordinates.
 *
 *         Originally from Richard Gray
 *         (http://www.rwgrayprojects.com/Lynn/NCH/coordinates.html)
 *
 */
public final class PhiCoordinates {

    public static final Cube[]       Cubes;
    public static final Dodecahedron Dodecahedron;

    public static final Icosahedron Icosahedron;

    public static boolean[] JITTERBUG_INVERSES = new boolean[] { false, false, false, false, true, true, true, true };

    public static Octahedron[] Octahedrons;

    public static final Tetrahedron[] Tetrahedrons;

    // 1 icosahedron
    private static int[] IcosaVertices = { 2, 6, 12, 17, 27, 31, 33, 37, 46, 51, 54, 58 };

    private static final float[] MeshPoints;

    // 5 differnet octahedra
    private static int[][] OctahedronVertices = { { 7, 10, 22, 43, 49, 55 }, { 9, 21, 14, 53, 42, 57 },
                                                  { 25, 3, 15, 44, 59, 40 }, { 19, 5, 24, 39, 61, 48 },

                                                  { 26, 1, 35, 29, 62, 32 } };

    @SuppressWarnings("unused")
    private static int[] P120EdgeMap = { 1, 2, 1, 4, 1, 6, 1, 8, 2, 3, 2, 4, 2, 8, 2, 9, 2, 10, 2, 11, 2, 18, 2, 19, 2,
                                         20, 3, 4, 3, 11, 3, 12, 4, 5, 4, 6, 4, 12, 5, 6, 5, 12, 5, 13, 6, 7, 6, 8, 6,
                                         13, 6, 14, 6, 15, 6, 16, 6, 23, 7, 8, 7, 16, 7, 17, 8, 9, 8, 17, 9, 17, 9, 18,
                                         10, 11, 10, 20, 10, 27, 11, 12, 11, 21, 11, 27, 12, 13, 12, 21, 12, 28, 12, 29,
                                         12, 22, 12, 30, 13, 14, 13, 22, 13, 31, 14, 23, 14, 31, 15, 16, 15, 23, 15, 33,
                                         16, 17, 16, 24, 16, 33, 17, 18, 17, 24, 17, 25, 17, 34, 17, 35, 17, 36, 18, 19,
                                         18, 25, 18, 37, 19, 20, 19, 37, 20, 26, 20, 27, 20, 37, 21, 27, 21, 28, 22, 30,
                                         22, 31, 23, 31, 23, 32, 23, 33, 24, 33, 24, 34, 25, 36, 25, 37, 26, 27, 26, 37,
                                         26, 38, 27, 28, 27, 38, 27, 39, 27, 44, 27, 45, 28, 29, 28, 39, 28, 46, 29, 30,
                                         29, 46, 30, 31, 30, 40, 30, 46, 31, 32, 31, 40, 31, 41, 31, 47, 31, 48, 32, 33,
                                         32, 41, 33, 34, 33, 41, 33, 42, 33, 49, 33, 50, 34, 35, 34, 42, 34, 51, 35, 36,
                                         35, 51, 36, 37, 36, 43, 36, 51, 37, 38, 37, 43, 37, 52, 37, 53, 38, 44, 38, 53,
                                         38, 54, 39, 45, 39, 46, 40, 46, 40, 47, 41, 48, 41, 49, 41, 58, 42, 50, 42, 51,
                                         43, 51, 43, 52, 44, 45, 44, 54, 45, 55, 45, 46, 45, 54, 46, 47, 46, 55, 46, 56,
                                         46, 57, 47, 48, 47, 57, 47, 58, 48, 58, 49, 50, 49, 58, 50, 51, 50, 58, 50, 59,
                                         51, 52, 51, 59, 51, 60, 51, 61, 52, 53, 52, 61, 52, 54, 53, 54, 54, 55, 54, 56,
                                         54, 60, 54, 61, 54, 62, 55, 56, 56, 57, 56, 58, 56, 62, 57, 58, 58, 59, 58, 60,
                                         58, 62, 59, 60, 60, 61, 60, 62, };

    @SuppressWarnings("unused")
    private static int[] P120FaceMap = { 1, 2, 4, 2, 3, 4, 2, 20, 10, 2, 10, 11, 2, 11, 3, 3, 11, 12, 3, 12, 4, 20, 26,
                                         27, 20, 27, 10, 10, 27, 11, 11, 27, 21, 11, 21, 12, 21, 27, 28, 12, 21, 28, 12,
                                         28, 29, 1, 4, 6, 4, 12, 5, 4, 5, 6, 5, 12, 13, 5, 13, 6, 6, 13, 14, 6, 14, 23,
                                         12, 29, 30, 12, 30, 22, 12, 22, 13, 13, 22, 31, 22, 30, 31, 13, 31, 14, 14, 31,
                                         23, 23, 31, 32, 1, 6, 8, 6, 23, 15, 6, 15, 16, 6, 16, 7, 6, 7, 8, 8, 7, 17, 7,
                                         16, 17, 23, 32, 33, 15, 23, 33, 16, 15, 33, 24, 16, 33, 34, 24, 33, 17, 16, 24,
                                         17, 24, 34, 17, 34, 35, 1, 8, 2, 8, 17, 9, 8, 9, 2, 9, 17, 18, 9, 18, 2, 2, 18,
                                         19, 2, 19, 20, 17, 35, 36, 17, 36, 25, 17, 25, 18, 18, 25, 37, 25, 36, 37, 19,
                                         18, 37, 20, 19, 37, 20, 37, 26, 27, 26, 38, 27, 38, 44, 27, 44, 45, 27, 45, 39,
                                         27, 39, 28, 28, 39, 46, 28, 46, 29, 39, 45, 46, 38, 54, 44, 55, 45, 54, 45, 44,
                                         54, 45, 55, 46, 46, 55, 56, 55, 54, 56, 56, 54, 62, 30, 29, 46, 30, 46, 40, 31,
                                         30, 40, 40, 46, 47, 31, 40, 47, 31, 47, 48, 31, 48, 41, 31, 41, 32, 46, 56, 57,
                                         47, 46, 57, 47, 57, 58, 48, 47, 58, 41, 48, 58, 57, 56, 58, 58, 56, 62, 33, 32,
                                         41, 33, 41, 49, 33, 49, 50, 33, 50, 42, 33, 42, 34, 34, 42, 51, 42, 50, 51, 35,
                                         34, 51, 49, 41, 58, 50, 49, 58, 50, 58, 59, 51, 50, 59, 51, 59, 60, 59, 58, 60,
                                         60, 58, 62, 36, 35, 51, 36, 51, 43, 37, 36, 43, 43, 51, 52, 37, 43, 52, 37, 52,
                                         53, 37, 53, 38, 37, 38, 26, 51, 60, 61, 52, 51, 61, 52, 61, 54, 53, 52, 54, 38,
                                         53, 54, 54, 61, 60, 54, 60, 62 };

    // 1 120 Polyhedron
    @SuppressWarnings("unused")
    private static int[] P120Vertices = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                                          23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
                                          42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                                          61, 62 };

    private static Vector3d[] POLY_120_VECTORS = { new Vector3d(0.0, 0.0, 0.0), // 00 center of volume

                                                   new Vector3d(0.0, 0.0, 2.0 * PHI_SQUARED), // 01

                                                   new Vector3d(PHI_SQUARED, 0.0, PHI_CUBED), // 02
                                                   new Vector3d(PHI, PHI_SQUARED, PHI_CUBED), // 03
                                                   new Vector3d(0.0, PHI, PHI_CUBED), // 04
                                                   new Vector3d(-PHI, PHI_SQUARED, PHI_CUBED), // 05
                                                   new Vector3d(-PHI_SQUARED, 0.0, PHI_CUBED), // 06
                                                   new Vector3d(-PHI, -PHI_SQUARED, PHI_CUBED), // 07
                                                   new Vector3d(0.0, -PHI, PHI_CUBED), // 08
                                                   new Vector3d(PHI, -PHI_SQUARED, PHI_CUBED), // 09

                                                   new Vector3d(PHI_CUBED, PHI, PHI_SQUARED), // 10
                                                   new Vector3d(PHI_SQUARED, PHI_SQUARED, PHI_SQUARED), // 11
                                                   new Vector3d(0.0, PHI_CUBED, PHI_SQUARED), // 12
                                                   new Vector3d(-PHI_SQUARED, PHI_SQUARED, PHI_SQUARED), // 13
                                                   new Vector3d(-PHI_CUBED, PHI, PHI_SQUARED), // 14
                                                   new Vector3d(-PHI_CUBED, -PHI, PHI_SQUARED), // 15
                                                   new Vector3d(-PHI_SQUARED, -PHI_SQUARED, PHI_SQUARED), // 16
                                                   new Vector3d(0.0, -PHI_CUBED, PHI_SQUARED), // 17
                                                   new Vector3d(PHI_SQUARED, -PHI_SQUARED, PHI_SQUARED), // 18
                                                   new Vector3d(PHI_CUBED, -PHI, PHI_SQUARED), // 19

                                                   new Vector3d(PHI_CUBED, 0.0, PHI), // 20
                                                   new Vector3d(PHI_SQUARED, PHI_CUBED, PHI), // 21
                                                   new Vector3d(-PHI_SQUARED, PHI_CUBED, PHI), // 22
                                                   new Vector3d(-PHI_CUBED, 0.0, PHI), // 23
                                                   new Vector3d(-PHI_SQUARED, -PHI_CUBED, PHI), // 24
                                                   new Vector3d(PHI_SQUARED, -PHI_CUBED, PHI), // 25

                                                   new Vector3d(2.0 * PHI_SQUARED, 0.0, 0.0), // 26
                                                   new Vector3d(PHI_CUBED, PHI_SQUARED, 0.0), // 27
                                                   new Vector3d(PHI, PHI_CUBED, 0.0), // 28
                                                   new Vector3d(0.0, 2.0 * PHI_SQUARED, 0.0), // 29
                                                   new Vector3d(-PHI, PHI_CUBED, 0.0), // 30
                                                   new Vector3d(-PHI_CUBED, PHI_SQUARED, 0.0), // 31
                                                   new Vector3d(-2.0 * PHI_SQUARED, 0.0, 0.0), // 32
                                                   new Vector3d(-PHI_CUBED, -PHI_SQUARED, 0.0), // 33
                                                   new Vector3d(-PHI, -PHI_CUBED, 0.0), // 34
                                                   new Vector3d(0.0, -2.0 * PHI_SQUARED, 0.0), // 35
                                                   new Vector3d(PHI, -PHI_CUBED, 0.0), // 36
                                                   new Vector3d(PHI_CUBED, -PHI_SQUARED, 0.0), // 37

                                                   new Vector3d(PHI_CUBED, 0.0, -PHI), // 38
                                                   new Vector3d(PHI_SQUARED, PHI_CUBED, -PHI), // 39
                                                   new Vector3d(-PHI_SQUARED, PHI_CUBED, -PHI), // 40
                                                   new Vector3d(-PHI_CUBED, 0.0, -PHI), // 41
                                                   new Vector3d(-PHI_SQUARED, -PHI_CUBED, -PHI), // 42
                                                   new Vector3d(PHI_SQUARED, -PHI_CUBED, -PHI), // 43

                                                   new Vector3d(PHI_CUBED, PHI, -PHI_SQUARED), // 44
                                                   new Vector3d(PHI_SQUARED, PHI_SQUARED, -PHI_SQUARED), // 45
                                                   new Vector3d(0.0, PHI_CUBED, -PHI_SQUARED), // 46
                                                   new Vector3d(-PHI_SQUARED, PHI_SQUARED, -PHI_SQUARED), // 47
                                                   new Vector3d(-PHI_CUBED, PHI, -PHI_SQUARED), // 48
                                                   new Vector3d(-PHI_CUBED, -PHI, -PHI_SQUARED), // 49
                                                   new Vector3d(-PHI_SQUARED, -PHI_SQUARED, -PHI_SQUARED), // 50
                                                   new Vector3d(0.0, -PHI_CUBED, -PHI_SQUARED), // 51
                                                   new Vector3d(PHI_SQUARED, -PHI_SQUARED, -PHI_SQUARED), // 52
                                                   new Vector3d(PHI_CUBED, -PHI, -PHI_SQUARED), // 53

                                                   new Vector3d(PHI_SQUARED, 0.0, -PHI_CUBED), // 54
                                                   new Vector3d(PHI, PHI_SQUARED, -PHI_CUBED), // 55
                                                   new Vector3d(0.0, PHI, -PHI_CUBED), // 56
                                                   new Vector3d(-PHI, PHI_SQUARED, -PHI_CUBED), // 57
                                                   new Vector3d(-PHI_SQUARED, 0.0, -PHI_CUBED), // 58
                                                   new Vector3d(-PHI, -PHI_SQUARED, -PHI_CUBED), // 59
                                                   new Vector3d(0.0, -PHI, -PHI_CUBED), // 60
                                                   new Vector3d(PHI, -PHI_SQUARED, -PHI_CUBED), // 61

                                                   new Vector3d(0.0, 0.0, -2.0 * PHI_SQUARED) // 62
    };

    // 1 regular dodecahedron
    private static int[] RegDodecahedronVertices = { 4, 8, 11, 13, 16, 18, 20, 23, 28, 30, 34, 36, 38, 41, 45, 47, 50,
                                                     52, 56, 60 };

    // 5 rhombic dodecahedra
    @SuppressWarnings("unused")
    private static int[][] RhDodecahedronVertices = { { 4, 7, 10, 18, 22, 23, 28, 34, 38, 43, 47, 49, 55, 60 },
                                                      { 4, 9, 14, 16, 20, 21, 30, 36, 41, 42, 45, 53, 57, 60 },
                                                      { 3, 8, 13, 15, 20, 25, 28, 34, 40, 41, 44, 52, 56, 59 },
                                                      { 5, 8, 11, 19, 23, 24, 30, 36, 38, 39, 48, 50, 56, 61 },
                                                      { 1, 11, 13, 16, 18, 26, 29, 32, 35, 45, 47, 50, 52, 62 } };
    // 1 rhombic triacontahedron
    @SuppressWarnings("unused")
    private static int[] RhTriaVertices = { 2, 4, 6, 8, 11, 12, 13, 16, 17, 18, 20, 23, 27, 28, 30, 31, 33, 34, 36, 37,
                                            38, 41, 45, 46, 47, 50, 51, 52, 54, 56, 58, 60 };
    // 10 different tetrahedra
    private static int[][] TetrahedronVertices = { { 47, 34, 4, 38 }, { 18, 28, 60, 23 },

                                                   { 30, 16, 20, 60 }, { 36, 45, 41, 4 },

                                                   { 28, 41, 8, 52 }, { 34, 20, 56, 13 },

                                                   { 45, 50, 13, 18 }, { 16, 11, 52, 47 },

                                                   { 56, 23, 11, 36 }, { 8, 38, 50, 30 }

    };

    static {
        MeshPoints = new float[POLY_120_VECTORS.length * 3];
        int i = 0;
        for (Vector3d v : POLY_120_VECTORS) {
            MeshPoints[i++] = (float) v.x;
            MeshPoints[i++] = (float) v.y;
            MeshPoints[i++] = (float) v.z;
        }

        Cubes = cubes();
        Dodecahedron = dodecahedron();
        Icosahedron = icosahedron();
        Octahedrons = octahedrons();
        Tetrahedrons = tetrahedrons();
    }

    public static Vector3d[] pointsFrom(int[] indices) {
        Vector3d[] vectors = new Vector3d[indices.length];
        int i = 0;
        for (int index : indices) {
            vectors[i++] = POLY_120_VECTORS[index];
        }
        return vectors;
    }

    private static Cube[] cubes() {
        Cube[] cubes = new Cube[5];
        int j = 0;
        for (int i = 0; i < TetrahedronVertices.length / 2; i++) {
            int[] a = TetrahedronVertices[i * 2];
            int[] b = TetrahedronVertices[i * 2 + 1];
            int[] coordinates = new int[] { a[0], b[1], a[3], b[2], a[1], b[0], a[2], b[3] };
            cubes[j++] = new Cube(pointsFrom(coordinates));
        }
        return cubes;
    }

    private static Dodecahedron dodecahedron() {
        return new Dodecahedron(pointsFrom(RegDodecahedronVertices));
    }

    private static Icosahedron icosahedron() {
        return new Icosahedron(pointsFrom(IcosaVertices));
    }

    private static Octahedron[] octahedrons() {
        Octahedron[] octahedrons = new Octahedron[OctahedronVertices.length];
        int i = 0;
        for (int[] coordinates : OctahedronVertices) {
            Vector3d[] vectors = pointsFrom(coordinates);
            Vector3d[] normalized = new Vector3d[vectors.length];
            normalized[0] = vectors[2];
            normalized[1] = vectors[1];
            normalized[2] = vectors[3];
            normalized[3] = vectors[5];
            normalized[4] = vectors[0];
            normalized[5] = vectors[4];
            octahedrons[i++] = new Octahedron(vectors);
        }
        return octahedrons;
    }

    private static Tetrahedron[] tetrahedrons() {
        Tetrahedron[] tetrahedrons = new Tetrahedron[TetrahedronVertices.length];
        int i = 0;
        for (int[] coordinates : TetrahedronVertices) {
            tetrahedrons[i++] = new Tetrahedron((i & 1) == 0, pointsFrom(coordinates));
        }
        return tetrahedrons;
    }

    private PhiCoordinates() {
    }
}
