package com.hellblazer.delaunay;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Tests basic arithmetic operations for {@link DoubleDouble}s.
 * 
 * @author Martin Davis
 * 
 */
public class DoubleDoubleBasicTest extends TestCase {
	public static void main(String args[]) {
		TestRunner.run(DoubleDoubleBasicTest.class);
	}

	public DoubleDoubleBasicTest(String name) {
		super(name);
	}

	public void testNaN() {
		assertTrue(DoubleDouble.valueOf(1).divide(DoubleDouble.valueOf(0))
				.isNaN());
		assertTrue(DoubleDouble.valueOf(1).multiply(DoubleDouble.NaN).isNaN());
	}

	public void testAddMult2() {
		checkAddMult2(new DoubleDouble(3));
		checkAddMult2(DoubleDouble.PI);
	}

	public void testMultiplyDivide() {
		checkMultiplyDivide(DoubleDouble.PI, DoubleDouble.E, 1e-30);
		checkMultiplyDivide(DoubleDouble.TWO_PI, DoubleDouble.E, 1e-30);
		checkMultiplyDivide(DoubleDouble.PI_2, DoubleDouble.E, 1e-30);
		checkMultiplyDivide(new DoubleDouble(39.4), new DoubleDouble(10), 1e-30);
	}

	public void testDivideMultiply() {
		checkDivideMultiply(DoubleDouble.PI, DoubleDouble.E, 1e-30);
		checkDivideMultiply(new DoubleDouble(39.4), new DoubleDouble(10), 1e-30);
	}

	public void testSqrt() {
		// the appropriate error bound is determined empirically
		checkSqrt(DoubleDouble.PI, 1e-30);
		checkSqrt(DoubleDouble.E, 1e-30);
		checkSqrt(new DoubleDouble(999.0), 1e-28);
	}

	private void checkSqrt(DoubleDouble x, double errBound) {
		DoubleDouble sqrt = x.sqrt();
		DoubleDouble x2 = sqrt.multiply(sqrt);
		checkErrorBound("Sqrt", x, x2, errBound);
	}

	public void testTrunc() {
		checkTrunc(
				DoubleDouble.valueOf(1e16).subtract(DoubleDouble.valueOf(1)),
				DoubleDouble.valueOf(1e16).subtract(DoubleDouble.valueOf(1)));
		// the appropriate error bound is determined empirically
		checkTrunc(DoubleDouble.PI, DoubleDouble.valueOf(3));
		checkTrunc(DoubleDouble.valueOf(999.999), DoubleDouble.valueOf(999));

		checkTrunc(DoubleDouble.E.negate(), DoubleDouble.valueOf(-2));
		checkTrunc(DoubleDouble.valueOf(-999.999), DoubleDouble.valueOf(-999));
	}

	private void checkTrunc(DoubleDouble x, DoubleDouble expected) {
		DoubleDouble trunc = x.trunc();
		boolean isEqual = trunc.equals(expected);
		assertTrue(isEqual);
	}

	public void testPow() {
		checkPow(0, 3, 16 * DoubleDouble.EPS);
		checkPow(14, 3, 16 * DoubleDouble.EPS);
		checkPow(3, -5, 16 * DoubleDouble.EPS);
		checkPow(-3, 5, 16 * DoubleDouble.EPS);
		checkPow(-3, -5, 16 * DoubleDouble.EPS);
		checkPow(0.12345, -5, 1e5 * DoubleDouble.EPS);
	}

	public void testReciprocal() {
		// error bounds are chosen to be "close enough" (i.e. heuristically)

		// for some reason many reciprocals are exact
		checkReciprocal(3.0, 0);
		checkReciprocal(99.0, 1e-29);
		checkReciprocal(999.0, 0);
		checkReciprocal(314159269.0, 0);
	}

	public void testBinom() {
		checkBinomialSquare(100.0, 1.0);
		checkBinomialSquare(1000.0, 1.0);
		checkBinomialSquare(10000.0, 1.0);
		checkBinomialSquare(100000.0, 1.0);
		checkBinomialSquare(1000000.0, 1.0);
		checkBinomialSquare(1e8, 1.0);
		checkBinomialSquare(1e10, 1.0);
		checkBinomialSquare(1e14, 1.0);
		// Following call will fail, because it requires 32 digits of precision
		// checkBinomialSquare(1e16, 1.0);

		checkBinomialSquare(1e14, 291.0);
		checkBinomialSquare(5e14, 291.0);
		checkBinomialSquare(5e14, 345291.0);
	}

	private void checkAddMult2(DoubleDouble dd) {
		DoubleDouble sum = dd.add(dd);
		DoubleDouble prod = dd.multiply(new DoubleDouble(2.0));
		checkErrorBound("AddMult2", sum, prod, 0.0);
	}

	private void checkMultiplyDivide(DoubleDouble a, DoubleDouble b,
			double errBound) {
		DoubleDouble a2 = a.multiply(b).divide(b);
		checkErrorBound("MultiplyDivide", a, a2, errBound);
	}

	private void checkDivideMultiply(DoubleDouble a, DoubleDouble b,
			double errBound) {
		DoubleDouble a2 = a.divide(b).multiply(b);
		checkErrorBound("DivideMultiply", a, a2, errBound);
	}

	@SuppressWarnings("unused")
	private DoubleDouble delta(DoubleDouble x, DoubleDouble y) {
		return x.subtract(y).abs();
	}

	private void checkErrorBound(String tag, DoubleDouble x, DoubleDouble y,
			double errBound) {
		DoubleDouble err = x.subtract(y).abs();
		System.out.println(tag + " err=" + err);
		boolean isWithinEps = err.doubleValue() <= errBound;
		assertTrue(isWithinEps);
	}

	/**
	 * Computes (a+b)^2 in two different ways and compares the result. For
	 * correct results, a and b should be integers.
	 * 
	 * @param a
	 * @param b
	 */
	void checkBinomialSquare(double a, double b) {
		// binomial square
		DoubleDouble add = new DoubleDouble(a);
		DoubleDouble bdd = new DoubleDouble(b);
		DoubleDouble aPlusb = add.add(bdd);
		DoubleDouble abSq = aPlusb.multiply(aPlusb);
		// System.out.println("(a+b)^2 = " + abSq);

		// expansion
		DoubleDouble a2dd = add.multiply(add);
		DoubleDouble b2dd = bdd.multiply(bdd);
		DoubleDouble ab = add.multiply(bdd);
		DoubleDouble sum = b2dd.add(ab).add(ab);

		// System.out.println("2ab+b^2 = " + sum);

		DoubleDouble diff = abSq.subtract(a2dd);
		// System.out.println("(a+b)^2 - a^2 = " + diff);

		DoubleDouble delta = diff.subtract(sum);

		System.out.println();
		System.out.println("A = " + a + ", B = " + b);
		System.out.println("[DoubleDouble] 2ab+b^2 = " + sum
				+ "   (a+b)^2 - a^2 = " + diff + "   delta = " + delta);
		printBinomialSquareDouble(a, b);

		boolean isSame = diff.equals(sum);
		assertTrue(isSame);
		boolean isDeltaZero = delta.isZero();
		assertTrue(isDeltaZero);
	}

	void printBinomialSquareDouble(double a, double b) {
		double sum = 2 * a * b + b * b;
		double diff = (a + b) * (a + b) - a * a;
		System.out.println("[double] 2ab+b^2= " + sum + "   (a+b)^2-a^2= "
				+ diff + "   delta= " + (sum - diff));
	}

	public void testBinomial2() {
		checkBinomial2(100.0, 1.0);
		checkBinomial2(1000.0, 1.0);
		checkBinomial2(10000.0, 1.0);
		checkBinomial2(100000.0, 1.0);
		checkBinomial2(1000000.0, 1.0);
		checkBinomial2(1e8, 1.0);
		checkBinomial2(1e10, 1.0);
		checkBinomial2(1e14, 1.0);

		checkBinomial2(1e14, 291.0);

		checkBinomial2(5e14, 291.0);
		checkBinomial2(5e14, 345291.0);
	}

	void checkBinomial2(double a, double b) {
		// binomial product
		DoubleDouble add = new DoubleDouble(a);
		DoubleDouble bdd = new DoubleDouble(b);
		DoubleDouble aPlusb = add.add(bdd);
		DoubleDouble aSubb = add.subtract(bdd);
		DoubleDouble abProd = aPlusb.multiply(aSubb);
		// System.out.println("(a+b)^2 = " + abSq);

		// expansion
		DoubleDouble a2dd = add.multiply(add);
		DoubleDouble b2dd = bdd.multiply(bdd);

		// System.out.println("2ab+b^2 = " + sum);

		// this should equal b^2
		DoubleDouble diff = abProd.subtract(a2dd).negate();
		// System.out.println("(a+b)^2 - a^2 = " + diff);

		DoubleDouble delta = diff.subtract(b2dd);

		System.out.println();
		System.out.println("A = " + a + ", B = " + b);
		System.out.println("[DoubleDouble] (a+b)(a-b) = " + abProd
				+ "   -((a^2 - b^2) - a^2) = " + diff + "   delta = " + delta);
		// printBinomialSquareDouble(a,b);

		boolean isSame = diff.equals(b2dd);
		assertTrue(isSame);
		boolean isDeltaZero = delta.isZero();
		assertTrue(isDeltaZero);
	}

	private void checkReciprocal(double x, double errBound) {
		DoubleDouble xdd = new DoubleDouble(x);
		DoubleDouble rr = xdd.reciprocal().reciprocal();

		double err = xdd.subtract(rr).doubleValue();

		System.out.println("DD Recip = " + xdd + " DD delta= " + err
				+ " double recip delta= " + (x - 1.0 / (1.0 / x)));

		assertTrue(err <= errBound);
	}

	private void checkPow(double x, int exp, double errBound) {
		DoubleDouble xdd = new DoubleDouble(x);
		DoubleDouble pow = xdd.pow(exp);
		System.out.println("Pow(" + x + ", " + exp + ") = " + pow);
		DoubleDouble pow2 = slowPow(xdd, exp);

		double err = pow.subtract(pow2).doubleValue();

		boolean isOK = err < errBound;
		if (!isOK)
			System.out.println("Test slowpow value " + pow2);

		assertTrue(err <= errBound);
	}

	private DoubleDouble slowPow(DoubleDouble x, int exp) {
		if (exp == 0)
			return DoubleDouble.valueOf(1.0);

		int n = Math.abs(exp);
		// MD - could use binary exponentiation for better precision & speed
		DoubleDouble pow = new DoubleDouble(x);
		for (int i = 1; i < n; i++) {
			pow = pow.multiply(x);
		}
		if (exp < 0) {
			return pow.reciprocal();
		}
		return pow;
	}

}