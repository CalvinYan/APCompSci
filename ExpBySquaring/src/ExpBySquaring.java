/**
 * The ExpBySquaring class contains a single static method that implements
 * the exponentiation by squaring method.
 * @author Calvin Yan
 *
 */

public class ExpBySquaring {
	
	/**
	 * Implements exponentiation by squaring as follows:
	 * Let b be the base and e be the exponent. If e is even, then recursively
	 * calculate (b^2)^(e/2) and return that.
	 * If e is odd, then recursively calculate (b^2)^((e - 1)/2), multiply the
	 * result by b, and return it.
	 * If e < 0, then recursively calculate (1/b)^-e.
	 * The terminating condition is when e = 0, as the result would always be 1.
	 * @param base - the base to be raised to a power
	 * @param exponent - the power to raise the base number to
	 * @return the base to the power of the exponent
	 */
	public static double pow(double base, int exponent) {
		if (exponent == 0) return 1;
		if (exponent < 0) {
			return pow(1 / base, -exponent);
		}
		if (exponent % 2 == 0) {
			return pow(base * base, exponent / 2);
		} else {
			return base * pow(base * base, (exponent - 1) / 2);
		}
	}
	/*
	 * Usage: Enter <base> <exponent> in the command line. b^e is calculated
	 * using the above method, and then compared to the result from Math.pow.
	 */
	public static void main(String[] args) {
		double base = Double.parseDouble(args[0]);
		int exponent = Integer.parseInt(args[1]);
		System.out.println(pow(base, exponent));
		System.out.println(Math.pow(base, exponent));
	}
	
}
