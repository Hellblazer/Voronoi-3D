/* This file is part of Gralog, Copyright (c) 2016-2018 LaS group, TU Berlin.
 * License: https://www.gnu.org/licenses/gpl.html GPL version 3 or later. */

package com.hellblazer.delaunay.sturm;

public interface Interval {

    boolean contains(double t);

    double lowerBound();

    double upperBound();
}
