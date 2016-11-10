/**
 * Implementation of exercises 1.11 and 1.12. Requirements are below:
 * 
 * Exercise 1.11.  A function f is defined by the rule that f(n) = n if n < 3,
 * f(n) = f(n-1) + 2f(n-2) + 3f(n-3) if n >= 3. Write a procedure that computes
 * f by means of a recursive process. Write a procedure that computes f by
 * means of an iterative process.
 * 
 * Exercise 1.12.  The following pattern of numbers is called Pascal's triangle.
 * The numbers at the edge of the triangle are all 1, and each number inside the
 * triangle is the sum of the two numbers above it.35 Write a procedure that
 * computes elements of Pascal's triangle by means of a recursive process.
 * 
 * @author Calvin Yan
 *
 */
public class Chapter1 {

	public static void main(String[] args) {
		long before, after;
		
		// Time recursive implementation of f(n)
		before = System.nanoTime();
		System.out.println(funcRecursive(30));
		after = System.nanoTime();
		System.out.println("Recursion: " + (after - before) + " nanoseconds");
		
		// Time iterative implementation of f(n)
		before = System.nanoTime();
		System.out.println(funcIterative(30));
		after = System.nanoTime();
		System.out.println("Iteration: " + (after - before) + " nanoseconds");
		
		// Time recursive pascal's triangle method
		before = System.nanoTime();
		System.out.println(pascal(52, 5));
		after = System.nanoTime();
		System.out.println("Pascal: " + (after - before) + " nanoseconds");
	}
	
	// Recursive implementation of f(n)
	public static int funcRecursive(int n) {
		if (n < 3) return n;
		
		// Literally just f(n-1) + 2f(n-2) + 3f(n-3)
		return funcRecursive(n-1) + 2 * funcRecursive(n-2) + 3 * funcRecursive(n-3);
	}
	
	// Method is overloaded so the arguments match funcRecursive
	public static int funcIterative(int n) {
		return funcIterative(0, new int[]{0, 1, 2}, 3, n);
	}
	
	// Iterative implementation of f(n)
	private static int funcIterative(int value, int[] previous, int counter, int n) {
		if (n < 3) return n;
		if (counter > n) return value;
		
		int newValue = 0;
		// calculate f(n-1) + 2f(n-2) + 3f(n-3) using previous to store the values
		for (int i = 0; i < 3; i++) newValue += (3-i) * previous[i];
		
		// modify the stored values and move to f(counter + 1)
		return funcIterative(newValue, new int[]{previous[1], previous[2], newValue}, counter+1, n);
	}
	
	// Calculate n choose k
	public static int pascal(int n, int k) {
		// If n is on either edge of the triangle
		if (k == 0 || k == n) return 1;
		
		// A number in the triangle is the sum of the two above it
		return pascal(n-1, k-1) + pascal(n-1, k);
	}

}
