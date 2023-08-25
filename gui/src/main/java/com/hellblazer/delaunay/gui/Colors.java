/**
 * Copyright (C) 2016 Hal Hildebrand. All rights reserved.
 *
 * This file is part of the 3D Incremental Voronoi system
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

package com.hellblazer.delaunay.gui;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.CYAN;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.LAVENDER;
import static javafx.scene.paint.Color.LIME;
import static javafx.scene.paint.Color.MAGENTA;
import static javafx.scene.paint.Color.OLIVE;
import static javafx.scene.paint.Color.ORANGE;
import static javafx.scene.paint.Color.PURPLE;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.VIOLET;
import static javafx.scene.paint.Color.YELLOW;

import javafx.scene.paint.PhongMaterial;

/**
 * @author halhildebrand
 *
 */
public class Colors {
    public static final PhongMaterial   blackMaterial;
    public static final PhongMaterial[] blackMaterials;
    public static final PhongMaterial   blueMaterial;
    public static final PhongMaterial   cyanMaterial;
    public static final PhongMaterial[] eight4Materials;
    public static final PhongMaterial[] eightMaterials;
    public static final PhongMaterial   greenMaterial;
    public static final PhongMaterial   lavenderMaterial;
    public static final PhongMaterial   limeMaterial;
    public static final PhongMaterial   magentaMaterial;
    public static final PhongMaterial[] materials;
    public static final PhongMaterial   oliveMaterial;
    public static final PhongMaterial   orangeMaterial;
    public static final PhongMaterial   purpleMaterial;
    public static final PhongMaterial   redMaterial;
    public static final PhongMaterial   violetMaterial;
    public static final PhongMaterial   yellowMaterial;

    static {
        redMaterial = new PhongMaterial(RED);
        blueMaterial = new PhongMaterial(BLUE);
        greenMaterial = new PhongMaterial(GREEN);
        yellowMaterial = new PhongMaterial(YELLOW);
        violetMaterial = new PhongMaterial(VIOLET);
        orangeMaterial = new PhongMaterial(ORANGE);
        cyanMaterial = new PhongMaterial(CYAN);
        purpleMaterial = new PhongMaterial(PURPLE);
        magentaMaterial = new PhongMaterial(MAGENTA);
        lavenderMaterial = new PhongMaterial(LAVENDER);
        oliveMaterial = new PhongMaterial(OLIVE);
        limeMaterial = new PhongMaterial(LIME);

        blackMaterial = new PhongMaterial(BLACK);

        materials = new PhongMaterial[] { redMaterial, blueMaterial, greenMaterial, yellowMaterial, cyanMaterial,
                                          purpleMaterial, orangeMaterial, violetMaterial, magentaMaterial,
                                          lavenderMaterial, oliveMaterial, limeMaterial };
        blackMaterials = new PhongMaterial[] { blackMaterial, blackMaterial };
        eightMaterials = new PhongMaterial[] { redMaterial, blueMaterial, greenMaterial, yellowMaterial, redMaterial,
                                               blueMaterial, greenMaterial, yellowMaterial };
        eight4Materials = new PhongMaterial[] { redMaterial, blueMaterial, greenMaterial, yellowMaterial, blackMaterial,
                                                blackMaterial, blackMaterial, blackMaterial };
    }

}
