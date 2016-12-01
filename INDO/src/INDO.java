import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INDO {

	public static void main(String[] args) {
		getClauses("a&b|c");
	}
	
	// The INDO method is implemented using getClauses, taking in the logical expression to convert as input. The I, N, and D steps are further encapsulated into methods.
	public static ArrayList<String[]> getClauses(String expression) {
		/*ArrayList<String[]> clauses = new ArrayList<String[]>();

		expression = simplified(expression);

		// Convert to clauses
		for (String clause : expression.split("&")) {
			clauses.add(clause.split("|"));
		}
		return clauses;*/
		System.out.println(simplify("a&b=>c|d|~e"));
		//System.out.println(simplify("a=>~(b|c&d=>e)|f<=>~~g"));
		//System.out.println(distribute("a|(b&~c&d&~e)|f&g&h"));
		return null;
	}
	
	// Converts the input string to a form that can be further converted
	// to clauses, then returns it.
	private static String simplify(String expression) {
		expression = optimize(expression);
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				int left = i+1, right = getCloseParen(expression, i);
				String parenthetical = expression.substring(left, right);
				expression = expression.replace(parenthetical, simplify(parenthetical));
				i = getCloseParen(expression, i);
			}
		}
		expression = removeImplications(expression);
		expression = removeNegations(expression);
		expression = distribute(expression);
		return expression;
	}
	
	private static String optimize(String expression) {
		// Remove all spaces
		expression = expression.replaceAll("\\s", "");
		
		/*
		 * The regex (\\||^)~?[a-z](\\&~?[a-z])+ finds all groups of literals
		 * separated by the & operator that aren't enclosed in parentheses.
		 * Example: a&~b&c|d|e&f|(g&h&i)|j&k&~l&m 
		 * Matches: a&~b&c, e&f, j&k&~l&m
		 */
		Matcher m = Pattern.compile("[\\|^<=>]~?[a-z](\\&~?[a-z])+").matcher(expression);
		while (m.find()) {
			String group = m.group(0);
			if (!expression.startsWith(group)) group = group.substring(1);
			// Enclose the expression in parentheses
			expression = expression.replace(group, String.format("(%s)", group));
		}
		return expression;
	}


	// I
	private static String removeImplications(String expression) {
		// Find all instances of <=>
		Matcher m = Pattern.compile("<?=>?").matcher(expression);
		if (m.find()) {
			String operator = m.group(0);
			int leftBound = expression.indexOf(operator), rightBound = leftBound + operator.length();
			String left = simplify(expression.substring(0, leftBound)), right = simplify(expression.substring(rightBound));
			if (left.length() > 2) left = "(" + left + ")";
			if (right.length() > 2) right = "(" + right + ")";
			System.out.println("Left: " + left + "    right: " + right);
			switch (operator) {
			case "=>":
				expression = "~" + left + "|" + right;
				break;
			case "<=":
				expression = left + "|" + "~" + right;
			case "<=>":
				expression = "~" + left + "|" + "~" + right;
			}
			System.out.println("After implication resolution: " + expression);
		}
		
		return expression;
	}


	// N
	private static String removeNegations(String expression) {
		int index = 0;
		while ((index = expression.indexOf("~(")) != -1) {
			// distribute the negation among the literals in the parentheses
			int leftBound = index, rightBound = getCloseParen(expression, leftBound + 1) + 1;
			String parenthetical = expression.substring(leftBound, rightBound);
			System.out.println("Negating " + parenthetical);
			expression = expression.replace(parenthetical, negateParenthetical(parenthetical));
			System.out.println("After negation: " + expression);
		}

		expression = expression.replace("~~", "");
		return expression;
	}


	// D
	private static String distribute(String expression) {
		Matcher m = Pattern.compile("((~?[a-z]|\\))\\|\\(|\\)\\|(~?[a-z]|\\())").matcher(expression);
		while (m.find()) {
			String operator = m.group(0), literal, parenthetical, toReplace;
			int middle = expression.indexOf(operator) + operator.indexOf('|');
			String left = getLeftExpression(expression, middle), right = getRightExpression(expression, middle);
			System.out.println("Distributing " + left + " among " + right);
			expression = expression.replace(left + "|" + right, distributeParenthetical(left, right));
			System.out.println("After distribution: " + expression);
		}
/*		for (distributable expression in expression) {
	
	 * Implementation details left out because I’m not a big enough nerd.
	 * Ideally the distributed() method would convert an expression such as
	 * a|b&c into (a|b)&(a|c).
	 
			replace distributable with distributed(distributable);
		}*/
		return expression;
	}


	// Applies the rule of negation to parenthetical expressions, such as ~(a&b) into ~a|~b.
	private static String negateParenthetical(String expression) {
		StringBuilder sb = new StringBuilder(expression.substring(2, expression.length() - 1));
		int index = 0;
		while (index < sb.length()) {
			char c = sb.charAt(index);
			if (Character.isAlphabetic(c)) {
				sb.insert(index, '~');
				index++;
			} else {
				if (c == '&') sb.replace(index, index+1, "|");
				else if (c == '|') sb.replace(index, index+1, "&");
			}
			index++;
		}
		return "(" + sb.toString() + ")";
	}
	
	private static String distributeParenthetical(String literal, String expression) {
		StringBuilder sb = new StringBuilder();
		while (enclosed(expression)) expression = expression.substring(1, expression.length() - 1);
		String[] otherLiterals = expression.split("&");
		for (int i = 0; i < otherLiterals.length; i++) {
			String newExpression = distribute(literal + "|" + otherLiterals[i]);
			if (!enclosed(newExpression)) newExpression = "(" + newExpression + ")";
			sb.append(newExpression);
			if (i < otherLiterals.length - 1) sb.append("&");
		}
		return (otherLiterals.length > 100) ? "(" + sb.toString() + ")" : sb.toString();
	}
	
	private static int getCloseParen(String expression, int index) {
		int layers = 0;
		for (int i = index; i < expression.length(); i++) {
			char character = expression.charAt(i);
			if (character == '(') layers++;
			if (character == ')') layers--;
			if (layers == 0) return i;
		}
		return -1;
	}
	
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
	
	private static String getLeftExpression(String expression, int index) {
		if (expression.charAt(index - 1) == ')') {
			return expression.substring(getOpenParen(expression, index - 1), index);
		}
		String literal = expression.substring(Math.max(index - 2, 0), index);
		if (literal.charAt(0) == '~') {
			return literal;
		} else return Character.toString(literal.charAt(literal.length() - 1));
	}
	
	private static String getRightExpression(String expression, int index) {
		if (expression.charAt(index + 1) == '(') {
			return expression.substring(index + 1, getCloseParen(expression, index + 1) + 1);
		}
		String literal = expression.substring(index + 1, Math.min(index + 3, expression.length()));
		if (literal.charAt(0) == '~') {
			return literal;
		} else return Character.toString(literal.charAt(0));
	}
	
	private static boolean enclosed(String expression) {
		return expression.matches("\\(.*\\)");
	}


}
