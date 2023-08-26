/**
 * Copyright (C) 2008 Hal Hildebrand. All rights reserved.
 *
 * This file is part of the 3F Incremental Voronoi GUI
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

package com.hellblazer.voronoi3d.gui;

import java.util.ArrayList;

import javax.vecmath.Point3f;

/**
 *
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 *
 */

public class TestCases {

    public static Point3f[] getCubicCrystalStructure() {
        ArrayList<Point3f> list = new ArrayList<>();
        list.add(new Point3f(-0.89999799999999996F, -0.89999200000000001F, -0.900003F));
        list.add(new Point3f(-0.90000199999999997F, -0.90000800000000003F, -6.9999999999999999E-06F));
        list.add(new Point3f(-0.89999499999999999F, -0.90000199999999997F, 0.900003F));
        list.add(new Point3f(-0.89999099999999999F, 5.0000000000000004E-06F, -0.89999099999999999F));
        list.add(new Point3f(-0.89999399999999996F, 9.9999999999999995E-07F, 5.0000000000000004E-06F));
        list.add(new Point3f(-0.90000800000000003F, 5.0000000000000004E-06F, 0.90000500000000005F));
        list.add(new Point3f(-0.89999499999999999F, 0.89999099999999999F, -0.89999600000000002F));
        list.add(new Point3f(-0.90000100000000005F, 0.90000000000000002F, -1.9999999999999999E-06F));
        list.add(new Point3f(-0.89999499999999999F, 0.90000199999999997F, 0.89999899999999999F));
        list.add(new Point3f(-6.0000000000000002E-06F, -0.89999600000000002F, -0.90000599999999997F));
        list.add(new Point3f(-6.0000000000000002E-06F, -0.900007F, -6.0000000000000002E-06F));
        list.add(new Point3f(-3.0000000000000001E-06F, -0.90000100000000005F, 0.89999600000000002F));
        list.add(new Point3f(3.9999999999999998E-06F, 0.0F, -0.89999399999999996F));
        list.add(new Point3f(-9.0000000000000002E-06F, 5.0000000000000004E-06F, 1.0000000000000001E-05F));
        list.add(new Point3f(-6.0000000000000002E-06F, -1.9999999999999999E-06F, 0.89999799999999996F));
        list.add(new Point3f(9.0000000000000002E-06F, 0.89999600000000002F, -0.90000599999999997F));
        list.add(new Point3f(1.0000000000000001E-05F, 0.90000100000000005F, 3.9999999999999998E-06F));
        list.add(new Point3f(-6.9999999999999999E-06F, 0.90000899999999995F, 0.89999600000000002F));
        list.add(new Point3f(0.89999799999999996F, -0.90000800000000003F, -0.89999600000000002F));
        list.add(new Point3f(0.90000500000000005F, -0.89999899999999999F, -1.9999999999999999E-06F));
        list.add(new Point3f(0.89999600000000002F, -0.89999099999999999F, 0.90000199999999997F));
        list.add(new Point3f(0.89999499999999999F, 9.9999999999999995E-07F, -0.89999700000000005F));
        list.add(new Point3f(0.90000500000000005F, -5.0000000000000004E-06F, -3.9999999999999998E-06F));
        list.add(new Point3f(0.89999799999999996F, -3.9999999999999998E-06F, 0.89999899999999999F));
        list.add(new Point3f(0.90000599999999997F, 0.900003F, -0.89999600000000002F));
        list.add(new Point3f(0.89999600000000002F, 0.89999899999999999F, -6.9999999999999999E-06F));
        list.add(new Point3f(0.90000400000000003F, 0.89999499999999999F, 0.900003F));
        list.add(new Point3f(-0.44999499999999998F, -0.44999099999999997F, -0.45001000000000002F));
        list.add(new Point3f(-0.45000200000000001F, -0.45000499999999999F, 0.44999499999999998F));
        list.add(new Point3f(-0.449992F, 0.44999099999999997F, -0.449992F));
        list.add(new Point3f(-0.44999499999999998F, 0.45000600000000002F, 0.44999099999999997F));
        list.add(new Point3f(0.45000099999999998F, -0.44999600000000001F, -0.44999600000000001F));
        list.add(new Point3f(0.44999800000000001F, -0.45000899999999999F, 0.44999299999999998F));
        list.add(new Point3f(0.44999400000000001F, 0.44999499999999998F, -0.45000800000000002F));
        list.add(new Point3f(0.45000800000000002F, 0.44999600000000001F, 0.45000699999999999F));
        return list.toArray(new Point3f[list.size()]);
    }

    public static Point3f[] getGrid() {
        ArrayList<Point3f> list;
        list = new ArrayList<>();
        list.add(new Point3f(-0.74999800000000005F, -0.74999199999999999F, -0.75000299999999998F));
        list.add(new Point3f(-0.75000199999999995F, -0.75000800000000001F, -0.25000699999999998F));
        list.add(new Point3f(-0.74999499999999997F, -0.75000199999999995F, 0.25000299999999998F));
        list.add(new Point3f(-0.74999099999999996F, -0.74999499999999997F, 0.75000900000000004F));
        list.add(new Point3f(-0.74999400000000005F, -0.249999F, -0.74999499999999997F));
        list.add(new Point3f(-0.75000800000000001F, -0.24999499999999999F, -0.24999499999999999F));
        list.add(new Point3f(-0.74999499999999997F, -0.25000899999999998F, 0.250004F));
        list.add(new Point3f(-0.75000100000000003F, -0.25F, 0.74999800000000005F));
        list.add(new Point3f(-0.74999499999999997F, 0.250002F, -0.75000100000000003F));
        list.add(new Point3f(-0.75000599999999995F, 0.250004F, -0.25000600000000001F));
        list.add(new Point3f(-0.75000599999999995F, 0.24999299999999999F, 0.24999399999999999F));
        list.add(new Point3f(-0.75000299999999998F, 0.249999F, 0.749996F));
        list.add(new Point3f(-0.749996F, 0.75F, -0.74999400000000005F));
        list.add(new Point3f(-0.75000900000000004F, 0.75000500000000003F, -0.24998999999999999F));
        list.add(new Point3f(-0.75000599999999995F, 0.74999800000000005F, 0.249998F));
        list.add(new Point3f(-0.74999099999999996F, 0.749996F, 0.74999400000000005F));
        list.add(new Point3f(-0.24998999999999999F, -0.74999899999999997F, -0.749996F));
        list.add(new Point3f(-0.25000699999999998F, -0.74999099999999996F, -0.250004F));
        list.add(new Point3f(-0.250002F, -0.75000800000000001F, 0.250004F));
        list.add(new Point3f(-0.24999499999999999F, -0.74999899999999997F, 0.74999800000000005F));
        list.add(new Point3f(-0.250004F, -0.24999099999999999F, -0.74999800000000005F));
        list.add(new Point3f(-0.25000499999999998F, -0.249999F, -0.249997F));
        list.add(new Point3f(-0.24999499999999999F, -0.25000499999999998F, 0.249996F));
        list.add(new Point3f(-0.250002F, -0.250004F, 0.74999899999999997F));
        list.add(new Point3f(-0.24999399999999999F, 0.25000299999999998F, -0.749996F));
        list.add(new Point3f(-0.250004F, 0.249999F, -0.25000699999999998F));
        list.add(new Point3f(-0.249996F, 0.24999499999999999F, 0.25000299999999998F));
        list.add(new Point3f(-0.24999499999999999F, 0.25000899999999998F, 0.74999000000000005F));
        list.add(new Point3f(-0.250002F, 0.74999499999999997F, -0.75000500000000003F));
        list.add(new Point3f(-0.24999199999999999F, 0.74999099999999996F, -0.24999199999999999F));
        list.add(new Point3f(-0.24999499999999999F, 0.75000599999999995F, 0.24999099999999999F));
        list.add(new Point3f(-0.249999F, 0.750004F, 0.750004F));
        list.add(new Point3f(0.249998F, -0.75000900000000004F, -0.75000699999999998F));
        list.add(new Point3f(0.24999399999999999F, -0.75000500000000003F, -0.25000800000000001F));
        list.add(new Point3f(0.25000800000000001F, -0.750004F, 0.25000699999999998F));
        list.add(new Point3f(0.25000099999999997F, -0.75F, 0.75000800000000001F));
        list.add(new Point3f(0.250002F, -0.25000699999999998F, -0.75000900000000004F));
        list.add(new Point3f(0.249997F, -0.249997F, -0.24999299999999999F));
        list.add(new Point3f(0.249996F, -0.24999499999999999F, 0.25001000000000001F));
        list.add(new Point3f(0.25000800000000001F, -0.25000699999999998F, 0.75000100000000003F));
        list.add(new Point3f(0.24999499999999999F, 0.249996F, -0.749996F));
        list.add(new Point3f(0.249998F, 0.250004F, -0.24999099999999999F));
        list.add(new Point3f(0.249998F, 0.25000099999999997F, 0.25000299999999998F));
        list.add(new Point3f(0.25000099999999997F, 0.24999399999999999F, 0.74999300000000002F));
        list.add(new Point3f(0.25001000000000001F, 0.749996F, -0.75000699999999998F));
        list.add(new Point3f(0.250004F, 0.750004F, -0.25000899999999998F));
        list.add(new Point3f(0.25000800000000001F, 0.74999099999999996F, 0.249996F));
        list.add(new Point3f(0.24999299999999999F, 0.74999000000000005F, 0.75000100000000003F));
        list.add(new Point3f(0.75000299999999998F, -0.74999700000000002F, -0.75000900000000004F));
        list.add(new Point3f(0.74999899999999997F, -0.75000199999999995F, -0.24999399999999999F));
        list.add(new Point3f(0.74999700000000002F, -0.75000599999999995F, 0.249997F));
        list.add(new Point3f(0.75000900000000004F, -0.75000800000000001F, 0.75F));
        list.add(new Point3f(0.75000900000000004F, -0.24999499999999999F, -0.74999499999999997F));
        list.add(new Point3f(0.74999800000000005F, -0.25000899999999998F, -0.250002F));
        list.add(new Point3f(0.75F, -0.250004F, 0.25000699999999998F));
        list.add(new Point3f(0.749996F, -0.25F, 0.749996F));
        list.add(new Point3f(0.75000199999999995F, 0.24999199999999999F, -0.75000500000000003F));
        list.add(new Point3f(0.750004F, 0.25000899999999998F, -0.25F));
        list.add(new Point3f(0.75000500000000003F, 0.25001000000000001F, 0.250002F));
        list.add(new Point3f(0.74999800000000005F, 0.25000699999999998F, 0.75000599999999995F));
        list.add(new Point3f(0.75000599999999995F, 0.75000299999999998F, -0.75F));
        list.add(new Point3f(0.74999199999999999F, 0.74999800000000005F, -0.25000600000000001F));
        list.add(new Point3f(0.74999300000000002F, 0.74999400000000005F, 0.24998999999999999F));
        list.add(new Point3f(0.75000299999999998F, 0.75000699999999998F, 0.75000199999999995F));
        return list.toArray(new Point3f[list.size()]);
    }

    public static Point3f[] getWorstCase() {
        ArrayList<Point3f> list;
        list = new ArrayList<>();

        list.add(new Point3f(-0.36000100000000002F, 7.9999999999999996E-06F, 0.96000099999999999F));
        list.add(new Point3f(0.120003F, 0.88000900000000004F, 0.120002F));
        list.add(new Point3f(-0.30000100000000002F, 0.0F, 0.900003F));
        list.add(new Point3f(0.100004F, 0.80000899999999997F, 0.100007F));
        list.add(new Point3f(-0.240008F, 7.9999999999999996E-06F, 0.84000399999999997F));
        list.add(new Point3f(0.080005999999999994F, 0.72000699999999995F, 0.080005000000000007F));
        list.add(new Point3f(-0.180009F, 6.0000000000000002E-06F, 0.780003F));
        list.add(new Point3f(0.060006999999999998F, 0.64000500000000005F, 0.060002F));
        list.add(new Point3f(-0.120005F, 3.9999999999999998E-06F, 0.72000900000000001F));
        list.add(new Point3f(0.040003999999999998F, 0.56000300000000003F, 0.040004999999999999F));
        list.add(new Point3f(-0.060003000000000001F, 1.9999999999999999E-06F, 0.66000300000000001F));
        list.add(new Point3f(0.020004000000000001F, 0.48000100000000001F, 0.020001999999999999F));
        list.add(new Point3f(6.0000000000000002E-06F, 9.0000000000000002E-06F, 0.60000600000000004F));
        list.add(new Point3f(6.0000000000000002E-06F, 0.40000799999999997F, 9.0000000000000002E-06F));
        list.add(new Point3f(0.060000999999999999F, 6.9999999999999999E-06F, 0.54000800000000004F));
        list.add(new Point3f(-0.020003F, 0.32000600000000001F, -0.020005999999999999F));
        list.add(new Point3f(0.120007F, 5.0000000000000004E-06F, 0.48000399999999999F));
        list.add(new Point3f(-0.040003999999999998F, 0.240004F, -0.040003999999999998F));
        list.add(new Point3f(0.180008F, 3.0000000000000001E-06F, 0.42000300000000002F));
        list.add(new Point3f(-0.060000999999999999F, 0.16000200000000001F, -0.060002F));
        list.add(new Point3f(0.240007F, 9.9999999999999995E-07F, 0.36000700000000002F));
        list.add(new Point3f(-0.080004000000000006F, 0.080004000000000006F, -0.080008999999999997F));
        list.add(new Point3f(0.30000199999999999F, 1.9999999999999999E-06F, 0.30000100000000002F));
        list.add(new Point3f(-0.10000299999999999F, 3.0000000000000001E-06F, -0.10000199999999999F));
        list.add(new Point3f(0.36000399999999999F, 6.0000000000000002E-06F, 0.240006F));
        list.add(new Point3f(-0.120005F, -0.080004000000000006F, -0.120003F));
        list.add(new Point3f(0.42000599999999999F, 6.9999999999999999E-06F, 0.180009F));
        list.add(new Point3f(-0.14000699999999999F, -0.160001F, -0.14000199999999999F));
        list.add(new Point3f(0.48000799999999999F, 3.0000000000000001E-06F, 0.120003F));
        list.add(new Point3f(-0.16000900000000001F, -0.240006F, -0.16000600000000001F));
        list.add(new Point3f(0.54000099999999995F, 1.9999999999999999E-06F, 0.060000600000000001F));
        list.add(new Point3f(-0.180002F, -0.32000299999999998F, -0.18000099999999999F));
        list.add(new Point3f(0.60000299999999995F, 3.9999999999999998E-06F, 5.0000000000000004E-06F));
        list.add(new Point3f(-0.20000399999999999F, -0.40000599999999997F, -0.20000200000000001F));
        list.add(new Point3f(0.66000499999999995F, 6.9999999999999999E-06F, -0.060003000000000001F));
        list.add(new Point3f(-0.22000600000000001F, -0.48000900000000002F, -0.22000600000000001F));
        list.add(new Point3f(0.72000699999999995F, 7.9999999999999996E-06F, -0.120004F));
        list.add(new Point3f(-0.240008F, -0.56000399999999995F, -0.24000299999999999F));
        list.add(new Point3f(0.78000899999999995F, 5.0000000000000004E-06F, -0.180007F));
        list.add(new Point3f(-0.26000200000000001F, -0.64000699999999999F, -0.26000400000000001F));
        list.add(new Point3f(0.840001F, 7.9999999999999996E-06F, -0.24000199999999999F));
        list.add(new Point3f(-0.280003F, -0.720001F, -0.280005F));
        list.add(new Point3f(0.90000500000000005F, 3.0000000000000001E-06F, -0.30000700000000002F));
        list.add(new Point3f(-0.30000300000000002F, -0.80000499999999997F, -0.30000300000000002F));
        list.add(new Point3f(0.96000099999999999F, 1.9999999999999999E-06F, -0.36000700000000002F));
        list.add(new Point3f(-0.32000899999999999F, -0.88000100000000003F, -0.32000299999999998F));
        return list.toArray(new Point3f[list.size()]);
    }
}
