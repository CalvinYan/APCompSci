import java.util.HashMap;

/**
 * An object containing the truth values for several variables as well as an
 * array of Strings representing logical expressions. This object has two main
 * methods for evaluating these expressions under the given truth values as
 * well as other logical statements.
 * @author Calvin Yan
 *
 */
public class LogicalExpressionCollection {

	private HashMap<Character, Boolean> constants = new HashMap<Character, Boolean>();
	
	private String[] statements;
	
	public static void main(String[] args) throws Exception {
	LogicalExpressionCollection test = new LogicalExpressionCollection("p q r s t", new String[]{"~p&q&~r&s&~t", "p=>q=>r"});
		String input = "q&r";
		boolean[] arr = test.entails(input);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(input + " entails " + test.statements[i] + ": " + arr[i]);
		}
		boolean[][] newArr = test.evaluate();
		for (int i = 0; i < newArr.length; i++) {
			System.out.print(test.statements[i] + ":");
			for (int j = 0; j < newArr[0].length; j++) {
				System.out.print(" " + newArr[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Creates a LogicalExpressionCollection with a string as the truth values
	 * and an array of Strings as the logical expressions to store.
	 * Please note that T and F are not valid variable names, as they will be
	 * used by the class when evaluating expressions inside an expression.
	 * @param constants a String assigning each space-separated variable a truth value
	 * (i.e. a b ~c ~d)
	 * @param statements the logical expressions to store (i.e. {"a&b", "b|c", "~a<=>c"})
	 * @throws Exception if the variables T or F are used
	 */
	public LogicalExpressionCollection(String constants, String[] statements) throws Exception {
		String[] assignments = constants.split(" ");
		for (String a : assignments) {
			char letter = a.charAt(0);
			if (letter == 'T' || letter == 'F') {
				throw new Exception("Please avoid using T or F as constants.");
			}
			this.constants.put(letter, true);
		}
		this.statements = statements;
	}
	
	/**
	 * Determines whether or not each logical expression is true using the
	 * truth values passed to the constructor.
	 * @return a boolean array where each value represents the value of one of
	 * the logical expressions.
	 * @throws Exception if any of the expressions are invalid.
	 */
	public boolean[][] evaluate() throws Exception {
		int n = constants.size(), limit = (int)Math.pow(2,  n);
		boolean[][] results = new boolean[statements.length][limit];
		/*
		 * Compute all possible truth assignments
		 * A bit of a cheap way to do it; each truth assignment can be seen as
		 * an n-bit integer where n is the number of variables, so that each bit
		 * represents a single variable (0 being false, 1 being true).
		 */
		Character[] keys = constants.keySet().toArray(new Character[0]);
		for (int i = 0; i < statements.length; i++) {
			// Because we start from 1111...11 and go down to 0000...00, the
			// loop is in descending order.
			for (int j = limit - 1; j >= 0; j--) {
				/*
				 * Parse the truth assignment. The value of a certain variable
				 * can be found with the bitwise & operator. For example, to
				 * find the value of the first variable in "truth assignment" x,
				 * we do x & 00001 = y. If the first bit of x is positive, then y
				 * should equal 00001.
				 */
				for (int k = 0; k < n; k++) {
					int pow = (int)Math.pow(2, k);
					boolean value = (j & pow) == pow;
					constants.put(keys[k], value);
				}
				// Check if the statement is true using these truth values
				removeSpaces(statements[i]);
				results[i][limit - j - 1] = evaluate(statements[i]);
			}
		}
		return results;
	}

	/**
	 * Given an expression as input, determines if that expression
	 * logically entails each of the expressions and returns a boolean array of the
	 * results.
	 * 
	 * @param statement the input expression
	 * @return a boolean array where each value represents whether or not the
	 * input entails each logic expression.
	 * @throws Exception if the input is invalid.
	 */
	public boolean[] entails(String statement) throws Exception {
		int n = constants.size(), limit = (int)Math.pow(2,  n);
		boolean[] results = new boolean[statements.length];
		//Compute all possible truth assignments
		Character[] keys = constants.keySet().toArray(new Character[0]);
		for (int i = 0; i < statements.length; i++) {
			for (int j = 0; j < limit; j++) {
				//Parse the truth assignment
				for (int k = 0; k < n; k++) {
					int pow = (int)Math.pow(2, k);
					boolean value = (j & pow) == pow;
					constants.put(keys[k], value);
				}
				// Check if the input entails this statement
				if (evaluate(statement)) {
					if (!evaluate(statements[i])) {
						results[i] = false;
						break;
					}
				}
				results[i] = true;
			}
		}
		return results;
	}
	
	/*
	 * Evaluates a single logical expression in the form of a string. The method
	 * does this as follows: First, it recursively evaluates all expressions in
	 * parentheses. It then recursively evaluates other operators in their
	 * reverse order: first conditionals, then conjunctions, then disjunctions,
	 * then negations.
	 */
	private boolean evaluate(String statement) throws Exception {
		if (statement.isEmpty()) {
			throw new Exception("Empty string where expression should be");
		}
		if (statement.length() < 3) {
			//statement is a single variable
			return valueOf(statement);
		}
		
		// Remove parentheses
		int index = 0;
		while ((index = statement.indexOf('(')) != -1) {
			int close = findCloseParen(statement, index);
			String substring = statement.substring(index, close + 1);
			boolean result = evaluate(statement.substring(index + 1, close));
			statement = statement.replace(substring, (result) ? "T" : "F");
		}
		// Check for implication operators
		while ((index = statement.indexOf('=')) != -1) {
			boolean biconditional = (statement.charAt(index - 1) == '<');
			String left, right;
			int rightBound = (biconditional) ? index - 1 : index;
			left = statement.substring(0, rightBound);
			right = statement.substring(index + 2);
			boolean value = (biconditional) ? (evaluate(left) == evaluate(right)) : (!evaluate(left) || evaluate(right));
			return value;
		}
		
		// Check for and operators
		while ((index = statement.indexOf("&")) != -1) {
			int leftBound = (index > 1 && statement.charAt(index - 2) == '~') ? index - 2 : index - 1,
					rightBound = (statement.charAt(index + 1) == '~') ? index + 3 : index + 2;	
			String left = statement.substring(leftBound, index),
					right = statement.substring(index + 1, rightBound),
					substring = left + "&" + right;
			boolean value = valueOf(left) && valueOf(right);
			statement = statement.replace(substring, (value) ? "T" : "F");
		}
		// Check for or operators
		while ((index = statement.indexOf("|")) != -1) {
			int leftBound = (index > 1 && statement.charAt(index - 2) == '~') ? index - 2 : index - 1,
					rightBound = (statement.charAt(index + 1) == '~') ? index + 3 : index + 2;	
			String left = statement.substring(leftBound, index),
					right = statement.substring(index + 1, rightBound),
					substring = left + "|" + right;
			boolean value = valueOf(left) || valueOf(right);
			statement = statement.replace(substring, (value) ? "T" : "F");
		}
		return statement.equals("T");
	}
	
	// Removes all whitespace in a given string: makes logical expressions easier to read
	private void removeSpaces(String s) {
		String[] tokens = s.split(" ");
		s = "";
		for (String t : tokens) {
			s += t;
		}
	}
	
	// Value of a logical expression containing a single variable
	private boolean valueOf(String statement) throws Exception {
		char variable = statement.charAt(statement.length() - 1);
		boolean value;
		if (variable == 'T') {
			value = true;
		} else if (variable == 'F') {
			value = false;
		} else if (constants.containsKey(variable)) {
			value = constants.get(variable);
		} else throw new Exception("Invalid constant " + variable);
		return (statement.length() == 1) ? value : !value;
	}
	
	// Given a ( symbol in a string, where is its corresponding )?
	private int findCloseParen(String statement, int index) {
		int count = 1;
		do {
			index++;
			if (statement.charAt(index) == '(') {
				count++;
			} else if (statement.charAt(index) == ')') {
				count--;
			}
		} while (count != 0);
		return index;
	}
}
