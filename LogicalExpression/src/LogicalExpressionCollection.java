import java.util.ArrayList;
import java.util.HashMap;

public class LogicalExpressionCollection {

	private HashMap<Character, Boolean> constants = new HashMap<Character, Boolean>();
	
	private ArrayList<String> statements = new ArrayList<String>();
	
	public LogicalExpressionCollection(String constants, String[] statements) {
		String[] assignments = constants.split(" ");
		for (String a : assignments) {
			char letter = a.charAt(0);
			boolean value = true;
			if (letter == '~') {
				letter = a.charAt(1);
				value = false;
			}
			this.constants.put(letter, value);
		}
		for (String statement : statements) {
			this.statements.add(statement);
		}
	}
	
	public boolean[] evaluate() {
		int length = constants.size();
		boolean[] results = new boolean[length];
		for (int i = 0; i < length; i++) {
			
		}
	}
	
	private boolean
	
}
