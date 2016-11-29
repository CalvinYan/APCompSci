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
		System.out.println(distribute("a|(b&c)"));
		return null;
	}
	
	// Converts the input string to a form that can be further converted
	// to clauses, then returns it.
	private static String simplified(String expression) {
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				int left = i+1, right = getCloseParen(expression, i);
				String parenthetical = expression.substring(left, right);
				expression = expression.replace(parenthetical, simplified(parenthetical));
				i = getCloseParen(expression, i);
			}
		}
		removeImplications(expression);
		removeNegations(expression);
		distribute(expression);
		return expression;
	}


	// I
	private static String removeImplications(String expression) {
		int index = 0;
		while ((index = expression.indexOf("=>")) != -1) {
			String left = expression.substring(0, index), right = expression.substring(index + 2);
			expression = String.format("~(%s)|(%s)", left, right);
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
		}

		expression = expression.replace("~~", "");
		return expression;
	}


	// D
	private static String distribute(String expression) {
		Matcher m = Pattern.compile("\\|\\(?~?.").matcher(expression);
		while (m.find()) {
			System.out.println(m.group(0));
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
				index += 2;
			} else {
				if (c == '&') {
					sb.replace(index, index+1, "|");
				} else if (c == '|') {
					sb.replace(index, index+1, "&");
				}
				index++;
			}
		}
		return sb.toString();
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
	
	private static String replace(String expression, String substring, int start, int end) {
		return expression.substring(0, start) + substring + expression.substring(end);
	}


}
