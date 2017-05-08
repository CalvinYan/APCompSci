import java.util.Arrays;

/**
 * Represents a single column in a truth table. Several logical operations are
 * supported as shown below.
 * 
 * Operations on two TruthColumns:
 * 
 * TruthColumn and(TruthColumn)
 * TruthColumn or(TruthColumn)
 * TruthColumn imply(TruthColumn)
 * TruthColumn biconditional(TruthColumn)
 * 
 * Each of these methods performs a logical operation on pairs of boolean
 * variables and stores the result in another example.
 * Below is an example result of tc1.and(tc2):
 * 
 * tc1		tc2		tc3
 * true		true	true
 * true		false	false
 * false	true	false
 * false	false	false
 * 
 * Operations on one TruthColumn:
 * 
 * TruthColumn negate()
 * 
 * Boolean functions on two TruthColumns:
 * boolean equivalent(TruthColumn)
 * boolean entails(TruthColumn)
 * boolean consistent(TruthColumn)
 * 
 * Boolean functions on one TruthColumn:
 * boolean valid()
 * boolean unsatisfiable()
 * boolean contingent()
 */

public class TruthColumn {
	
	private boolean[] column;

	public static void main(String[] args) {
		TruthColumn tc1 = new TruthColumn(new boolean[]{true, true, false, false, true, false, true, false}),
					tc2 = new TruthColumn(new boolean[]{true, true, false, true, true, true, true, false});
		// Expected value: {true, true, false, false, true, false, true, false}
		System.out.println(tc1.and(tc2));
		
		// Expected value: {true, true, false, true, true, true, true, false}
		System.out.println(tc1.or(tc2));
		
		// Expected value: {true, true, true, true, true, true, true, true}
		System.out.println(tc1.imply(tc2));
		
		// Expected value: {true, true, true, false, true, false, true, true}
		System.out.println(tc1.biconditional(tc2));
		
		// Expected value: {false, false, true, true, false, true, false, true}
		System.out.println(tc1.negate());
		
		// Expected value: false
		System.out.println(tc1.equivalent(tc2));
		
		// Expected value: true
		System.out.println(tc1.entails(tc2));
		
		// Expected value: true
		System.out.println(tc1.consistent(tc2));
		
		// Expected value: false
		System.out.println(tc1.valid());
		
		// Expected value: false
		System.out.println(tc1.unsatisfiable());
		
		// Expected value: true
		System.out.println(tc1.contingent());
	}
	
	public TruthColumn(boolean[] arr) {
		column = arr;
	}
	
	/**
	 * Given two TruthColumns, perform the and operation on pairs of
	 * corresponding values.
	 * @param tc2 - the TruthColumn to combine with this one
	 * @return a TruthColumn containing the result
	 */
	
	public TruthColumn and(TruthColumn tc2) {
		boolean[] retval = new boolean[column.length];
		for (int i = 0; i < column.length; i++) {
			retval[i] = column[i] && tc2.column[i];
		}
		return new TruthColumn(retval);
	}
	
	/**
	 * Given two TruthColumns, perform the or operation on pairs of
	 * corresponding values.
	 * @param tc2 - the TruthColumn to combine with this one
	 * @return a TruthColumn containing the result
	 */
	
	public TruthColumn or(TruthColumn tc2) {
		boolean[] retval = new boolean[column.length];
		for (int i = 0; i < column.length; i++) {
			retval[i] = column[i] || tc2.column[i];
		}
		return new TruthColumn(retval);
	}
	
	/**
	 * Creates a TruthColumn that is the negation of this one.
	 * @return a TruthColumn such that each value is the opposite of its
	 * corresponding value in the current TruthColumn
	 */
	
	public TruthColumn negate() {
		/*
		 * Boolean array with all false values - this works because boolean
		 * arrays contain all false values by default
		 */
		boolean[] allFalse = new boolean[column.length];
		return this.biconditional(new TruthColumn(allFalse));
	}
	
	/**
	 * Given two TruthColumns, perform the implies operation on pairs of
	 * corresponding values.
	 * @param tc2 - the TruthColumn to combine with this one
	 * @return a TruthColumn containing the result
	 */

	public TruthColumn imply(TruthColumn tc2) {
		// a -> b is equivalent to !a || b
		return this.negate().or(tc2);
	}
	
	/**
	 * Given two TruthColumns, perform the biconditional operation on pairs of
	 * corresponding values.
	 * @param tc2 - the TruthColumn to combine with this one
	 * @return a TruthColumn containing the result
	 */
	
	public TruthColumn biconditional(TruthColumn tc2) {
		boolean[] retval = new boolean[column.length];
		for (int i = 0; i < column.length; i++) {
			retval[i] = column[i] == tc2.column[i];
		}
		return new TruthColumn(retval);
	}
	
	/**
	 * Is this TruthColumn logically valid?
	 * @return whether or not this column contains all true values
	 */
	
	public boolean valid() {
		// TruthColumn with all true values
		TruthColumn allTrue = new TruthColumn(new boolean[column.length]).negate();
		return this.equivalent(allTrue);
	}
	
	/**
	 * Is this TruthColumn logically unsatisfiable?
	 * @return whether or not this column contains all false values
	 */
	
	public boolean unsatisfiable() {
		// TruthColumn with all false values
		TruthColumn allFalse = new TruthColumn(new boolean[column.length]);
		return this.equivalent(allFalse);
	}
	
	/**
	 * Is this TruthColumn logically contingent?
	 * @return whether or not this column contains true and false values
	 */
	
	public boolean contingent() {
		return !valid() && !unsatisfiable();
	}
	
	/**
	 * Are the two TruthColumns logically equivalent?
	 * @param tc2 - the TruthColumn to compare with this one
	 * @return whether or not both columns contain the same values in the same
	 * order
	 */
	
	public boolean equivalent(TruthColumn tc2) {
		return Arrays.equals(this.column, tc2.column);
	}
	
	/**
	 * Does this TruthColumn entail the other?
	 * @param tc2 - the TruthColumn to test with this one for entailment
	 * @return whether or not every true value in this column results in a true
	 * value in tc2
	 */
	
	public boolean entails(TruthColumn tc2) {
		return this.imply(tc2).valid();
	}
	
	/**
	 * Are these two TruthColumns logically consistent?
	 * @param tc2 - the TruthColumn to compare this one with
	 * @return whether or not both columns have true values at some point
	 */
	
	public boolean consistent(TruthColumn tc2) {
		return !(this.and(tc2).unsatisfiable());
	}
	
	/**
	 * Prints the TruthColumn in boolean array format.
	 */
	public String toString() {
		return Arrays.toString(column);
	}
	
}
