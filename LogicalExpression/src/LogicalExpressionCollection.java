import java.util.ArrayList;
import java.util.HashMap;

public class LogicalExpressionCollection {

	private HashMap<Character, Boolean> constants = new HashMap<Character, Boolean>();
	
	private ArrayList<String> statements = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {
		LogicalExpressionCollection test = new LogicalExpressionCollection("p ~r q", new String[]{"p|r&q"});
		test.evaluate();
	}
	
	public LogicalExpressionCollection(String constants, String[] statements) throws Exception {
		String[] assignments = constants.split(" ");
		for (String a : assignments) {
			char letter = a.charAt(0);
			boolean value = true;
			if (letter == '~') {
				letter = a.charAt(1);
				value = false;
			}
			if (letter == 'T' || letter == 'F') {
				throw new Exception("Please avoid using T or F as constants.");
			}
			this.constants.put(letter, value);
		}
		this.constants.put('T', true);
		this.constants.put('F', false);
		for (String statement : statements) {
			this.statements.add(statement);
		}
	}
	
	public boolean[] evaluate() throws Exception {
		int length = statements.size();
		boolean[] results = new boolean[length];
		for (int i = 0; i < length; i++) {
			String statement = statements.get(i);
			removeSpaces(statement);
			results[i] = evaluate(statements.get(i));
		}
		return results;
	}
	
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
			System.out.println(statement);
		}
		// Check for implication operators
		while ((index = statement.indexOf('=')) != -1) {
			boolean biconditional = (statement.charAt(index - 1) == '<');
			String left, right;
			int rightBound = (biconditional) ? index - 1 : index;
			left = statement.substring(0, rightBound);
			right = statement.substring(index + 2);
			System.out.println("Evaluating conditional: " + left + " and " + right);
			boolean value = (biconditional) ? (evaluate(left) == evaluate(right)) : (!evaluate(left) || evaluate(right));
			System.out.println(value);
			return value;
		}
		// Check for and operators
		while ((index = statement.indexOf("&")) != -1) {
			String substring = statement.substring(index - 1, index + 2),
					left = statement.substring(index - 2, index),
					right = statement.substring(index + 1, index + 3);
			boolean value = valueOf(left) && valueOf(right);
			System.out.println("Evaluating conjunction: " + left + " and " + right);
			statement.replace(substring, (value) ? "T" : "F");
			System.out.println(value);
		}
		// Check for or operators
		while ((index = statement.indexOf("|")) != -1) {
			String substring = statement.substring(index - 1, index + 2),
					left = statement.substring(Math.max(index - 2, 0), index),
					right = statement.substring(index + 1, Math.min(statement.length(), index + 3));
			boolean value = valueOf(left) || valueOf(right);
			System.out.println("Evaluating disjunction: " + left + " and " + right);
			statement = statement.replace(substring, (value) ? "T" : "F");
			System.out.println(value);
		}
		return false;
	}
	
	private void removeSpaces(String s) {
		String[] tokens = s.split(" ");
		s = "";
		for (String t : tokens) {
			s += t;
		}
		System.out.println("Processed string: " + s);
	}
	
	private boolean valueOf(String statement) throws Exception {
		if (statement.charAt(0) != '~') {
			char variable = statement.charAt(statement.length() - 1);
			if (constants.containsKey(variable)) {
				return !constants.get(variable);
			} else throw new Exception("Invalid constant " + variable);
		} else {
			char variable = statement.charAt(1);
			if (constants.containsKey(variable)) {
				return !constants.get(variable);
			} else throw new Exception("Invalid constant " + variable);
		}
	}
	
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
