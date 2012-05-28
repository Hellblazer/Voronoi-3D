package com.hellblazer.delaunay;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Tests I/O for {@link DoubleDouble}s.
 * 
 * @author mbdavis
 * 
 */
public class DoubleDoubleIOTest extends TestCase {
	public static void main(String args[]) {
		TestRunner.run(DoubleDoubleIOTest.class);
	}

	public DoubleDoubleIOTest(String name) {
		super(name);
	}


	public void testStandardNotation() 
	{	
		// standard cases
		checkStandardNotation(1.0, "1.0");
		checkStandardNotation(0.0, "0.0");
		
		// cases where hi is a power of 10 and lo is negative
		checkStandardNotation(DoubleDouble.valueOf(1e12).subtract(DoubleDouble.valueOf(1)),	"999999999999.0");
		checkStandardNotation(DoubleDouble.valueOf(1e14).subtract(DoubleDouble.valueOf(1)),	"99999999999999.0");
  	checkStandardNotation(DoubleDouble.valueOf(1e16).subtract(DoubleDouble.valueOf(1)),	"9999999999999999.0");
		
		DoubleDouble num8Dec = DoubleDouble.valueOf(-379363639).divide(
				DoubleDouble.valueOf(100000000));
		checkStandardNotation(num8Dec, "-3.79363639");
		
		checkStandardNotation(new DoubleDouble(-3.79363639, 8.039137357367426E-17),
				"-3.7936363900000000000000000");
		
		checkStandardNotation(DoubleDouble.valueOf(34).divide(
				DoubleDouble.valueOf(1000)), "0.034");
		checkStandardNotation(1.05e3, "1050.0");
		checkStandardNotation(0.34, "0.34000000000000002442490654175344");
		checkStandardNotation(DoubleDouble.valueOf(34).divide(
				DoubleDouble.valueOf(100)), "0.34");
		checkStandardNotation(14, "14.0");
	}

	private void checkStandardNotation(double x, String expectedStr) {
		checkStandardNotation(DoubleDouble.valueOf(x), expectedStr);
	}

	private void checkStandardNotation(DoubleDouble x, String expectedStr) {
		String xStr = x.toStandardNotation();
		System.out.println("Standard Notation: " + xStr);
		assertEquals(expectedStr, xStr);
	}

	public void testSciNotation() {
		checkSciNotation(0.0, "0.0E0");
		checkSciNotation(1.05e10, "1.05E10");
		checkSciNotation(0.34, "3.4000000000000002442490654175344E-1");
		checkSciNotation(
				DoubleDouble.valueOf(34).divide(DoubleDouble.valueOf(100)), "3.4E-1");
		checkSciNotation(14, "1.4E1");
	}

	private void checkSciNotation(double x, String expectedStr) {
		checkSciNotation(DoubleDouble.valueOf(x), expectedStr);
	}

	private void checkSciNotation(DoubleDouble x, String expectedStr) {
		String xStr = x.toSciNotation();
		System.out.println("Sci Notation: " + xStr);
		assertEquals(xStr, expectedStr);
	}

	public void testParse() {
		checkParse("1.05e10", 1.05E10, 1e-32);
		checkParse("-1.05e10", -1.05E10, 1e-32);
		checkParse("1.05e-10", DoubleDouble.valueOf(105.).divide(
				DoubleDouble.valueOf(100.)).divide(DoubleDouble.valueOf(1.0E10)), 1e-32);
		checkParse("-1.05e-10", DoubleDouble.valueOf(105.).divide(
				DoubleDouble.valueOf(100.)).divide(DoubleDouble.valueOf(1.0E10))
				.negate(), 1e-32);

		/**
		 * The Java double-precision constant 1.4 gives rise to a value which
		 * differs from the exact binary representation down around the 17th decimal
		 * place. Thus it will not compare exactly to the DoubleDouble
		 * representation of the same number. To avoid this, compute the expected
		 * value using full DD precision.
		 */
		checkParse("1.4",
				DoubleDouble.valueOf(14).divide(DoubleDouble.valueOf(10)), 1e-30);

		// 39.5D can be converted to an exact FP representation
		checkParse("39.5", 39.5, 1e-30);
		checkParse("-39.5", -39.5, 1e-30);
	}

	private void checkParse(String str, double expectedVal, double errBound) {
		checkParse(str, new DoubleDouble(expectedVal), errBound);
	}

	private void checkParse(String str, DoubleDouble expectedVal,
			double relErrBound) {
		DoubleDouble xdd = DoubleDouble.parse(str);
		double err = xdd.subtract(expectedVal).doubleValue();
		double relErr = err / xdd.doubleValue();

		System.out.println("Parsed= " + xdd + " rel err= " + relErr);

		assertTrue(err <= relErrBound);
	}

	public void testParseError() {
		checkParseError("-1.05E2w");
		checkParseError("%-1.05E2w");
		checkParseError("-1.0512345678t");
	}

	private void checkParseError(String str) {
		boolean foundParseError = false;
		try {
			DoubleDouble.parse(str);
		} catch (NumberFormatException ex) {
			foundParseError = true;
		}
		assertTrue(foundParseError);
	}

	public void testRepeatedSqrt()
	{
		writeRepeatedSqrt(DoubleDouble.valueOf(1.0));
		writeRepeatedSqrt(DoubleDouble.valueOf(.999999999999));
		writeRepeatedSqrt(DoubleDouble.PI.divide(DoubleDouble.valueOf(10)));
	}
	
	/**
	 * This routine simply tests for robustness of the toString function.
	 * 
	 * @param xdd
	 */
	void writeRepeatedSqrt(DoubleDouble xdd) 
	{
		int count = 0;
		while (xdd.doubleValue() > 1e-300) {
			count++;
			if (count == 100)
				count = count + 10;
			double x = xdd.doubleValue();
			DoubleDouble xSqrt = xdd.sqrt();
			String s = xSqrt.toString();
//			System.out.println(count + ": " + s);

			DoubleDouble xSqrt2 = DoubleDouble.parse(s);
			DoubleDouble xx = xSqrt2.multiply(xSqrt2);
			@SuppressWarnings("unused")
			double err = Math.abs(xx.doubleValue() - x);
			//assertTrue(err < 1e-10);
	
			xdd = xSqrt;

			// square roots converge on 1 - stop when very close
			DoubleDouble distFrom1DD = xSqrt.subtract(DoubleDouble.valueOf(1.0));
			double distFrom1 = distFrom1DD.doubleValue();
			if (Math.abs(distFrom1) < 1.0e-40)
				break;
		}
	}
	
	public void testRepeatedSqr()
	{
		writeRepeatedSqr(DoubleDouble.valueOf(.9));
		writeRepeatedSqr(DoubleDouble.PI.divide(DoubleDouble.valueOf(10)));
	}
	
	/**
	 * This routine simply tests for robustness of the toString function.
	 * 
	 * @param xdd
	 */
	void writeRepeatedSqr(DoubleDouble xdd) 
	{
		if (xdd.ge(DoubleDouble.valueOf(1)))
			throw new IllegalArgumentException("Argument must be < 1");
		
		int count = 0;
		while (xdd.doubleValue() > 1e-300) {
			count++;
			if (count == 100)
				count = count + 1;
			@SuppressWarnings("unused")
			double x = xdd.doubleValue();
			DoubleDouble xSqr = xdd.sqr();
			String s = xSqr.toString();
			System.out.println(count + ": " + s);

			@SuppressWarnings("unused")
			DoubleDouble xSqr2 = DoubleDouble.parse(s);
	
			xdd = xSqr;
		}
	}
	
	public void testIOSquaresStress() {
		for (int i = 1; i < 10000; i++) {
			writeAndReadSqrt(i);
		}
	}

	/**
	 * Tests that printing values with many decimal places works. 
	 * This tests the correctness and robustness of both output and input.
	 * 
	 * @param x
	 */
	void writeAndReadSqrt(double x) {
		DoubleDouble xdd = DoubleDouble.valueOf(x);
		DoubleDouble xSqrt = xdd.sqrt();
		String s = xSqrt.toString();
//		System.out.println(s);

		DoubleDouble xSqrt2 = DoubleDouble.parse(s);
		DoubleDouble xx = xSqrt2.multiply(xSqrt2);
		String xxStr = xx.toString();
//		System.out.println("==>  " + xxStr);

		DoubleDouble xx2 = DoubleDouble.parse(xxStr);
		double err = Math.abs(xx2.doubleValue() - x);
		assertTrue(err < 1e-10);
	}

}