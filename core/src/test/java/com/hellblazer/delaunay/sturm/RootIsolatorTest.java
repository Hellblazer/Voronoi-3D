package com.hellblazer.delaunay.sturm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class RootIsolatorTest {

    private static final double epsilon = 0.00001;

    private static boolean validateDoubleArrays(double[] input, double... expected) {
        for (int i = 0; i < input.length; i++) {
            assertEquals(input[i], expected[i], epsilon);
        }
        return true;
    }

    @Test
    public void testRootFinding() {
        Polynomial p = new Polynomial(1, -2.9, 3.2, -1.66, 0.3984, -0.03456);
        assertEquals(0.6, SturmRootIsolator.findRootDN(p, 0.42, 0.79, 100), 0.01);
        assertEquals(0.9, SturmRootIsolator.findRootDN(p, 0.82, 0.98, 100), 0.01);
    }

    @Test
    public void testSturmIsolation() {
        // Roots are found in [k,b)
        // 5 roots are between 0 and 1
        Polynomial[] p = SturmRootIsolator.sturmSequence(1, -2.9, 3.2, -1.66, 0.3984, -0.03456);
        List<Interval> intervals = SturmRootIsolator.findIntervals(p);

        for (Interval v : intervals) {
            assertTrue(v.contains(0.2) || v.contains(0.4) || v.contains(0.6) || v.contains(0.8) || v.contains(0.9));
        }
    }

    @Test
    public void testSturmSequenceIsolation() {
        Polynomial[] sequence = SturmRootIsolator.sturmSequence(1, -3, 3.4, -1.8, 0.4384, -0.0384);
        validateDoubleArrays(sequence[2].coeff, 2d / 25, -18d / 125, 254d / 3125, -222d / 15625);
        validateDoubleArrays(sequence[3].coeff, 7d / 25, -42d / 125, 59d / 625);
        validateDoubleArrays(sequence[4].coeff, 72d / 21875, -216d / 109375);
        validateDoubleArrays(sequence[5].coeff, 4d / 625);
    }
}
