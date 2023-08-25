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

/**
 * @author halhildebrand
 *
 */
public class Constants {

    public static final float HALF_PI           = (float) (Math.PI / 2.0);
    public static float       PHI               = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);
    public static float       PHI_CUBED         = PHI * PHI * PHI;
    public static float       PHI_SQUARED       = PHI * PHI;
    public static final float QUARTER_PI        = (float) (Math.PI / 4.0);
    public static final float ROOT_2            = (float) Math.sqrt(2.0);
    public static final float ROOT_2_DIV_2      = (float) (ROOT_2 / 2.0);
    public static final float THREE_QUARTERS_PI = (float) (Math.PI * .75);
    public static final float TWO_PI            = (float) (2 * Math.PI);

}
