import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Fitch contains ten methods which take in one or more logical expressions
 * and modify them under certain circumstances. These methods each represent
 * one of the ten Fitch rules of inference, used in propositional logic proofs.
 * @author Calvin Yan
 *
 */
public class Fitch {

	public static void main(String[] args) {
		// Test each of the Fitch rules for correctness
		
		// Testing andIntroduction
		ArrayList<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		System.out.println(andIntroduction(list)); //Should be a&b&c
		
		// Testing andElimination
		System.out.println(andElimination("a&b&c")); //Should be [a, b, c]
		
		// Testing orIntroduction
		System.out.println(orIntroduction("x", list)); //Should be x|a|b|c
		
		// Testing orElimination
		for (int i = 0; i < 3; i++) {
			list.add(list.remove(0) + "=>d");
		}
		System.out.println(orElimination("a|b|c", list)); //Should be d
		
		// Testing negationIntroduction
		System.out.println(negationIntroduction("a=>b", "a=>~b")); //Should be ~a
		
		// Testing negationElimination
		System.out.println(negationElimination("~~a")); //Should be a
		
		// Testing implicationIntroduction
		System.out.println(implicationIntroduction("a", "b")); //Should be a=>b
		
		// Testing implicationElimination
		System.out.println(implicationElimination("a=>b", "a")); //Should be b
		
		// Testing biconditionalIntroduction
		System.out.println(biconditionalIntroduction("a=>b", "b=>a")); //Should be a<=>b
		
		// Testing biconditionalElimination
		System.out.println(biconditionalElimination("a<=>b")); //Should be [a=>b, b=>a]
	}
	
	// Take in multiple constants and combine them using conjunctions
	public static String andIntroduction(ArrayList<String> constants) {
		StringBuilder exp = new StringBuilder("");
		if (constants.isEmpty()) return exp.toString();
		exp.append(constants.get(0));
		for (int i = 1; i < constants.size(); i++) {
			exp.append("&" + constants.get(i));
		}
		return exp.toString();
	}
	
	// Take a conjunction and separate it into multiple constants
	public static ArrayList<String> andElimination(String exp) {
		ArrayList<String> constants = new ArrayList<String>();
		exp = exp.replace(" ", "");
		exp = exp.replace("&", " ");
		String[] arr = exp.split(" ");
		for (String s : arr) {
			constants.add(s);
		}
		return constants;
	}
	
	// Given a constant A and a list of constants B, combine A and B into a single
	// disjunction.
	public static String orIntroduction(String constant, ArrayList<String> constants) {
		constant = constant.replace(" ", "");
		StringBuilder exp = new StringBuilder(constant);
		for (int i = 0; i < constants.size(); i++) {
			exp.append("|" + constants.get(i));
		}
		return exp.toString();
	}
	
	// If you have statements such as a|b|c, a=>d, b=>d, and c=>d, then it
	// follows that d is true. This method converts the above statements to the
	// implied constant d.
	public static String orElimination(String or, ArrayList<String> implications) {
		TreeSet<String> constants = new TreeSet<String>();
		or = or.replace(" ", "");
		String[] letters = or.split("\\|");
		for (String letter : letters) {
			constants.add(letter);
		}
		String result = "";
		for (String s : implications) {
			s = s.replace(" ", "");
			String constant = s.substring(0, 1);
			String then = s.substring(3);
			if (!result.equals("") && !result.equals(then)) return "";
			result = then;
			constants.remove(constant);
		}
		if (constants.isEmpty()) {
			return result;
		} else return "";
	}
	
	// If a=>b and a=>~b, then it follows that ~a. This method implements that
	// rule.
	public static String negationIntroduction(String imp1, String imp2) {
		imp1 = imp1.replace(" ", "");
		imp2 = imp2.replace(" ", "");
		char[] first = {imp1.charAt(0), imp2.charAt(0)};
		char[] second = {imp1.charAt(imp1.length() - 1), imp2.charAt(imp2.length() - 1)};
		if (first[0] == first[1] && second[0] == second[1]) {
			if (imp2.charAt(3) == '~') return "~" + first[0];
		}
		return "";
	}
	
	// Takes a statement such as ~~a and generates the statement "a"
	public static String negationElimination(String constant) {
		constant = constant.replace(" ", "");
		if (constant.charAt(0) == '~' && constant.charAt(1) == '~') return "" + constant.charAt(2);
		return "";
	}
	
	// Unfortunately the method cannot check if one truly implies two, so it
	// returns one=>two without any conditions
	public static String implicationIntroduction(String one, String two) {
		one = one.replace(" ", "");
		two = two.replace(" ", "");
		return one + "=>" + two;
	}
	
	// Takes in statements such as a=>b and a and returns the logical conclusion
	// b is true
	public static String implicationElimination(String implication, String constant) {
		implication = implication.replace(" ", "");
		constant = constant.replace(" ", "");
		if (constant.charAt(0) == implication.charAt(0)) {
			return "" + implication.charAt(implication.length() - 1);
		}
		return "";
	}
	
	// Given two implications a=>b and b=>a, combine them into a<=>b
	public static String biconditionalIntroduction(String imp1, String imp2) {
		imp1 = imp1.replace(" ", "");
		imp2 = imp2.replace(" ", "");
		char[] first = {imp1.charAt(0), imp2.charAt(0)};
		char[] second = {imp1.charAt(imp1.length() - 1), imp2.charAt(imp2.length() - 1)};
		if (first[0] == second[1] && first[1] == second[0]) {
			return first[0] + "<=>" + first[1];
		}
		return "";
	}
	
	// Converts a<=>b into statements a=>b and b=>a
	public static ArrayList<String> biconditionalElimination(String bi) {
		bi = bi.replace(" ", "");
		char first = bi.charAt(0), second = bi.charAt(bi.length() - 1);
		if (bi.length() == 5 && bi.substring(1, 4).equals("<=>")) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(first + "=>" + second);
			list.add(second + "=>" + first);
			return list;
		}
		return null;
	}

}
