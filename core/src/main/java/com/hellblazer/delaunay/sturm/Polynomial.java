/* This file is part of Gralog, Copyright (c) 2016-2018 LaS group, TU Berlin.
 * License: https://www.gnu.org/licenses/gpl.html GPL version 3 or later. */

package com.hellblazer.delaunay.sturm;

import java.util.Arrays;

/**
 *
 */
public class Polynomial {

    public double[] coeff;
    public int      n;

    /**
     * ax^5+.....+f
     *
     * Coefficient to largest monomial first.
     */
    public Polynomial(double... params) {
        this.n = params.length - 1;
        this.coeff = params;
    }

    public double eval(double x) {
        double result = 0;
        for (int i = 0; i < n + 1; i++) {
            result = coeff[i] + (x * result);
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(coeff);
    }

}
