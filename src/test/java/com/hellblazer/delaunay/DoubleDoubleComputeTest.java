package com.hellblazer.delaunay;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Various tests involving computing known mathematical quantities using the
 * basic {@link DoubleDouble} arithmetic operations.
 * 
 * @author Martin Davis
 * 
 */
public class DoubleDoubleComputeTest extends TestCase {
	public static void main(String args[]) {
		TestRunner.run(DoubleDoubleComputeTest.class);
	}

	public DoubleDoubleComputeTest(String name) {
		super(name);
	}

	public void testEByTaylorSeries() {
		DoubleDouble testE = computeEByTaylorSeries();
		double err = Math.abs(testE.subtract(DoubleDouble.E).doubleValue());
		System.out.println(err);
		assertTrue(err < 64 * DoubleDouble.EPS);
	}

	/**
	 * Uses Taylor series to compute e
	 * 
	 * e = 1 + 1 + 1/2! + 1/3! + 1/4! + ...
	 * 
	 * @return an approximation to e
	 */
	private DoubleDouble computeEByTaylorSeries() {
		DoubleDouble s = DoubleDouble.valueOf(2.0);
		DoubleDouble t = DoubleDouble.valueOf(1.0);
		double n = 1.0;
		@SuppressWarnings("unused")
		int i = 0;

		while (t.doubleValue() > DoubleDouble.EPS) {
			i++;
			n += 1.0;
			t = t.divide(DoubleDouble.valueOf(n));
			s = s.add(t);
			System.out.println(s);
		}
		return s;
	}

	public void testPiByMachin() {
		DoubleDouble testE = computePiByMachin();
		double err = Math.abs(testE.subtract(DoubleDouble.PI).doubleValue());
		System.out.println(err);
		assertTrue(err < 8 * DoubleDouble.EPS);
	}

	/**
	 * Uses Machin's arctangent formula to compute Pi:
	 * 
	 * Pi / 4 = 4 arctan(1/5) - arctan(1/239)
	 * 
	 * @return an approximation to Pi
	 */
	private DoubleDouble computePiByMachin() {
		DoubleDouble t1 = DoubleDouble.valueOf(1.0).divide(
				DoubleDouble.valueOf(5.0));
		DoubleDouble t2 = DoubleDouble.valueOf(1.0).divide(
				DoubleDouble.valueOf(239.0));

		DoubleDouble pi4 = (DoubleDouble.valueOf(4.0).multiply(arctan(t1)))
				.subtract(arctan(t2));
		DoubleDouble pi = DoubleDouble.valueOf(4.0).multiply(pi4);
		System.out.println(pi);
		return pi;
	}

	/**
	 * Computes the arctangent based on the Taylor series expansion
	 * 
	 * arctan(x) = x - x^3 / 3 + x^5 / 5 - x^7 / 7 + ...
	 * 
	 * @param x
	 *            the argument
	 * @return an approximation to the arctangent of the input
	 */
	private DoubleDouble arctan(DoubleDouble x) {
		DoubleDouble t = x;
		DoubleDouble t2 = t.sqr();
		DoubleDouble at = new DoubleDouble(0.0);
		DoubleDouble two = new DoubleDouble(2.0);
		@SuppressWarnings("unused")
		int k = 0;
		DoubleDouble d = new DoubleDouble(1.0);
		int sign = 1;
		while (t.doubleValue() > DoubleDouble.EPS) {
			k++;
			if (sign < 0)
				at = at.subtract(t.divide(d));
			else
				at = at.add(t.divide(d));

			d = d.add(two);
			t = t.multiply(t2);
			sign = -sign;
		}
		System.out.println("Computed: " + at + "    Math.atan = "
				+ Math.atan(x.doubleValue()));
		return at;
	}

}