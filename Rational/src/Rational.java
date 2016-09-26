
public class Rational {

	private int numerator, denominator;
	
	public Rational() {
		numerator = denominator = 0;
	}
	
	public Rational(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public void printRational() {
		System.out.println(numerator + "/" + denominator);
	}
	
	public void negate() {
		numerator *= -1;
	}
	
	public void invert() {
		int temp = numerator;
		numerator = denominator;
		denominator = temp;
	}
	
	public double toDouble() {
		return (double)(numerator / denominator);
	}
	
	public Rational reduce() {
		int gcd = gcd(numerator, denominator);
		return new Rational(numerator / gcd, denominator / gcd);
	}
	
	public Rational add(Rational that) {
		Rational added = new Rational(this.numerator * that.denominator +
				that.numerator * this.denominator, this.denominator * that.denominator);
		return added.reduce();
	}
	
	public static void main(String[] args) {
		Rational fraction = new Rational(8, 49);
		fraction.printRational(); // Should print 8/49
		fraction.negate();
		fraction.invert();
		fraction.printRational(); // Should print 49/-8
		fraction = new Rational(8, 49);
		fraction.add(new Rational(-1, 49)).printRational(); // Should print 1/7
	}
	
	private static int gcd(int n, int d) {
		for (int i = n; i > 1; i++) {
			if (n % i == 0 && d % i == 0) return i;
		}
		return 1;
	}
	
}
