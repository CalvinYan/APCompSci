import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeSet;

public class INDO {
	
	/*
	 *  A class that helps compare single variables. Helpful for sorting.
	 *  When comparing literals, only the character part is considered.
	 *  For example, ~a would come before c because a precedes c in the
	 *  alphabet.
	 */
	private static class LiteralComparator implements Comparator<String> {
		public int compare(String s1, String s2) {
			return Character.compare(s1.charAt(s1.length() - 1), s2.charAt(s2.length() - 1));
		}
	}

	public static void main(String[] args) {
		// Test cases
		
		// Expected value: ~a|~b
		//String expression = "~(a&b)";
		
		// Expected value: a|b
		//String expression = "~(~a&~b)";
		
		// Expected value: a|b&c|~x|~y&z
		String expression = "~(~a&~b)&~~c|~(x&y|~z)";
		
		System.out.println(removeNegations(expression));
	}
	
	// Applies the O part of the INDO method to convert a CNF expression to clauses
	public static TreeSet<String[]> getClauses(String expression) {
		// Contains the clauses as String arrays
		TreeSet<String[]> clauses = new TreeSet<String[]>(new Comparator<String[]>() {
			public int compare(String[] one, String[] two) {
				StringBuilder sb1 = new StringBuilder(), sb2 = new StringBuilder();
				for (String s : one) sb1.append(s);
				for (String s : two) sb2.append(s);
				return sb1.toString().compareTo(sb2.toString());
			}
		});
		// Convert to CNF
		expression = simplify(optimize(expression));
		/*
		 * Find all expressions that form a single clause. Some examples:
		 * a, a|b, a|b|c
		 */
		Matcher m = Pattern.compile("~?[a-z](\\|(~?[a-z]))*").matcher(expression);
		while (m.find()) {
			String group = m.group(0);
			// Separate the expression into individual variables
			String[] literals = group.split("\\|");
			Arrays.sort(literals, new LiteralComparator());
			clauses.add(literals);
		}
		return clauses;
	}
	
	public static int f(int x, int y){
		
		System.out.println(x + " " + y);

	    int temp = x-1;

	    if(x == 0) return y+1;

	    if(x > 0 && y == 0) return f(x-1,1);

	    return f(x-1, f(x,y-1));

	}
	
	/*
	 *  Converts the input string to CNF using the first three steps of the
	 *  INDO method.
	 *  Example:
	 *  simplify("a=>b&c|d") returns (~a|b|d)&(~a|c|d)
	 */
	private static String simplify(String expression) {
		// Simplify parenthetical expressions first
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				String parenthetical = expression.substring(i+1, getCloseParen(expression, i));
				expression = expression.replace(parenthetical, simplify(parenthetical));
				i = getCloseParen(expression, i);
			}
		}
		// I
		expression = removeImplications(expression);
		// N
		expression = removeNegations(expression);
		// D
		expression = distribute(expression);
		return expression;
	}
	
	/*
	 * Converts the expression into a format that is easier to process.
	 * Example:
	 * optimize("a & b| c& d ") returns "(a&b)|(c&d)"
	 */
	private static String optimize(String expression) {
		// Remove all whitespace
		expression = expression.replaceAll("\\s", "");
		
		/*
		 * The regex (\\||^)~?[a-z](\\&~?[a-z])+ finds all groups of literals
		 * separated by the & operator that aren't enclosed in parentheses.
		 * Example: a&~b&c|d|e&f|(g&h&i)|j&k&~l&m 
		 * Matches: a&~b&c, e&f, j&k&~l&m
		 */
		Matcher m = Pattern.compile("~?[a-z](&(~?[a-z]|\\(.*\\)))+").matcher(expression);
		while (m.find()) {
			String group = m.group(0);
			// Enclose the expression in parentheses
			expression = expression.replace(group, String.format("(%s)", group));
		}
		return expression;
	}


	// Converts all implication operators into a different form
	private static String removeImplications(String expression) {
		// Find all instances of =>, <=, or <=>
		Matcher m = Pattern.compile("<?=>?").matcher(expression);
		while (m.find()) {
			// Get an implication
			String operator = m.group(0);
			int index = expression.indexOf(operator);
			String left = expression.substring(0, index), right = expression.substring(index + operator.length());
			if (!enclosed(left) && left.length() > 2) left = "(" + left + ")";
			if (!enclosed(right) && right.length() > 2) right = "(" + right + ")";
			// Modify the expression based on the operator
			switch (operator) {
			case "=>":
				expression = "~" + left + "|" + right;
				break;
			case "<=":
				expression = left + "|" + "~" + right;
				break;
			case "<=>":
				expression = String.format("(~%s|%s)&(%s|~%s)", left, right, left, right);
				break;
			}
		}
		return expression;
	}

	/*
	 * Input: expression - The logical expression to process
	 * 
	 * Output: expression - The input statement with negations removed
	 * 
	 * Effect: Implements the N part of INDO by removing double negatives and
	 * distributing negated parentheticals
	 * 
	 * Preconditions: There can't be any parentheses in parentheses (()) or
	 * implications in the input. No whitespaces either. The input must also
	 * be a valid logical proposition, although I didn't need to tell you that
	 */
	private static String removeNegations(String expression) {
		int index = 0;
		// Find all negated parenthetical expressions
		while ((index = expression.indexOf("~(")) != -1) {
			// Remove the ~( part of the expression.
			expression = expression.substring(0, index) + expression.substring(index+2);
			// Distribute the negation among the literals in the parentheses
			char c;
			while ((c = expression.charAt(index)) != ')') {
				// Negate all literals
				if (Character.isAlphabetic(c)) {
					expression = expression.substring(0, index) + "~" + expression.substring(index);
					// Since we just added a character the index needs to get back to where it originally was
					index++;
				} else {
					// Switch all operators (AND to OR and OR to AND)
					if (c == '|') {
						expression = expression.substring(0, index) + "&" + expression.substring(index+1);
					} else if (c == '&') {
						expression = expression.substring(0, index) + "|" + expression.substring(index+1);
					}
				}
				// Next character
				index++;
			}
			// Remove the end parentheses
			expression = expression.substring(0, index) + expression.substring(index + 1);
		}
		
		// Remove double negatives
		expression = expression.replace("~~", "");
		return expression;
	}


	// Apply the rules of distribution in first-order logic
	private static String distribute(String expression) {
		while (true) {
			/*
			 * This cancerous thing finds all expressions that can potentially be distributed. Examples:
			 * a|(, )|a, a&(, )&a, )|(, )&(
			 */
			Matcher m = Pattern.compile("((~?[a-z]|\\))[\\|\\&]\\(|\\)[\\|\\&](~?[a-z]|\\())").matcher(expression);
			// Are there still expressions that can be distributed?
			boolean flag = false;
			while (m.find()) {
				String group = m.group(0), literal = "", parenthetical = "";
				// Which operator was caught by the regular expression?
				char operator = (group.contains("|")) ? '|' : '&';
				// Location of this operator in the entire expression
				int middle = expression.indexOf(group) + group.indexOf(operator);
				// Expressions left and right of the operator
				String left = getLeftExpression(expression, middle), right = getRightExpression(expression, middle);
				if (operator == '|') {
					if (left.matches("~?[a-z]")) {
						literal = left;
						parenthetical = right;
					} else {
						literal = right;
						parenthetical = left;
					}
					if (onlyOneOperator(left, '|')) {
						if (onlyOneOperator(right, '|')) {
							// Both expressions are of the form (a|b|...|c)
							expression = expression.replace(left + "|" + right, merge(literal, parenthetical, operator));
							flag = true;
							continue;
						}
					}
					// Distribute the smaller expression in the larger one
					// See distributeParenthetical() for example
					expression = expression.replace(left + "|" + right, distributeParenthetical(literal, parenthetical));
					flag = true;
				} else {
					if (onlyOneOperator(left, '&') && onlyOneOperator(right, '&')) {
						// Both expressions are of the form (a&b&...&c)
						expression = expression.replace(left + "&" + right, merge(left, right, operator));
						flag = true;
					}
				}
			}	
			// Return if no more expressions to distribute
			if (!flag) break;
		}
		return expression;
	}


	/*
	 * Applies the rule of negation to parenthetical expressions.
	 * Example:
	 * negateParenthetical("~(a&b)") returns ~a|~b
	 */
	private static String negateParenthetical(String expression) {
		StringBuilder sb = new StringBuilder(expression.substring(2, expression.length() - 1));
		int index = 0;
		while (index < sb.length()) {
			char c = sb.charAt(index);
			if (Character.isAlphabetic(c)) {
				// Negate all literals
				sb.insert(index, '~');
				/*
				 *  Since a new character was inserted, the index needs to go
				 *  to its original location
				 */
				index++;
			} else {
				// Switch all operators
				if (c == '&') sb.replace(index, index+1, "|");
				else if (c == '|') sb.replace(index, index+1, "&");
			}
			index++;
		}
		return "(" + sb.toString() + ")";
	}
	
	/*
	 * Distributes a smaller expression among a larger one using the rule of
	 * distribution. Both expressions must have a specified format:
	 * literal: either a single variable or (a|b|...|c)
	 * expression: (d&e&...&f)
	 * Example:
	 * distributeParenthetical("a", "(b&c&d&e)") returns "(a|b)&(a|c)&(a|d)&(a|e)"
	 */
	private static String distributeParenthetical(String literal, String expression) {
		StringBuilder sb = new StringBuilder();
		while (enclosed(expression)) expression = expression.substring(1, expression.length() - 1);
		// Individual literals in expression
		String[] otherLiterals = expression.split("&");
		for (int i = 0; i < otherLiterals.length; i++) {
			// Add the smaller expression to the literal and simplify
			String newExpression = distribute(literal + "|" + otherLiterals[i]);
			if (!enclosed(newExpression)) newExpression = "(" + newExpression + ")";
			// Add the result to the new string that will be returned
			sb.append(newExpression);
			if (i < otherLiterals.length - 1) sb.append("&");
		}
		String retval = sb.toString();
		if (!enclosed(retval) && retval.length() > 2) retval = "(" + retval + ")";
		return retval;
	}
	
	/*
	 * Combines two logical expressions into one.
	 * Example:
	 * merge("(a|b)", "(c|d|e)", '|') returns "((a|b)|c|d|e)"
	 */
	private static String merge(String left, String right, char operator) {
		return "(" + left + operator + right.substring(1);
	}

	/*
	 * Given the end of a parenthetical expression, where does it begin?
	 * Example:
	 * getOpenParen("a&(b|c)", 2) returns 6
	 */
	static int getCloseParen(String expression, int index) {
		int layers = 0;
		for (int i = index; i < expression.length(); i++) {
			char character = expression.charAt(i);
			if (character == '(') layers++;
			if (character == ')') layers--;
			if (layers == 0) return i;
		}
		return -1;
	}
	
	/*
	 * Given the end of a parenthetical expression, where does it begin?
	 * Example:
	 * getOpenParen("a&(b|c)", 6) returns 2
	 */
	private static int getOpenParen(String expression, int index) {
		int layers = 0;
		for (int i = index; i >= 0; i--) {
			char character = expression.charAt(i);
			if (character == ')') layers++;
			if (character == '(') layers--;
			if (layers == 0) return i;
		}
		return -1;
	}
	
	/*
	 * Get the logical expression directly to the left of the character at 
	 * the specified position.
	 * Example:
	 * getLeftExpression("(a=>~b)&(c|d)", 6) returns (a=>~b) 
	 */
	private static String getLeftExpression(String expression, int index) {
		if (expression.charAt(index - 1) == ')') {
			return expression.substring(getOpenParen(expression, index - 1), index);
		}
		String literal = expression.substring(Math.max(index - 2, 0), index);
		if (literal.charAt(0) == '~') {
			return literal;
		} else return Character.toString(literal.charAt(literal.length() - 1));
	}
	
	/*
	 * Get the logical expression directly to the right of the character at 
	 * the specified position.
	 * Example:
	 * getRightExpression("(a=>~b)&(c|d)", 6) returns (c|d) 
	 */
	private static String getRightExpression(String expression, int index) {
		if (expression.charAt(index + 1) == '(') {
			return expression.substring(index + 1, getCloseParen(expression, index + 1) + 1);
		}
		String literal = expression.substring(index + 1, Math.min(index + 3, expression.length()));
		if (literal.charAt(0) == '~') {
			return literal;
		} else return Character.toString(literal.charAt(0));
	}
	
	/* 
	 * Is the given expression of the form a|b|c|...|d or a&b&c&...&d
	 * where a, b, c, and d are arbitrary logical expressions?
	 */
	private static boolean onlyOneOperator(String expression, char operator) {
		while (enclosed(expression)) expression = expression.substring(1, expression.length() - 1);
		char otherOperator = (operator == '|') ? '&' : '|';
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == otherOperator) return false;
			if (expression.charAt(i) == '(') i += getCloseParen(expression, i) - 1;
		}
		return true;
	}
	
	/* 
	 * Is the given string enclosed in parentheses?
	 * Example:
	 * ((a&b)|c|(d&e)) - yes
	 * (a|b)=>(c|d|e) - no
	 */
	private static boolean enclosed(String expression) {
		if (expression.length() <= 2) return false;
		return getCloseParen(expression, 0) == expression.length() - 1;
	}
}
