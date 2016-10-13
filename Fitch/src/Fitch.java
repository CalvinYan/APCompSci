import java.util.ArrayList;
import java.util.TreeSet;

public class Fitch {

	public static void main(String[] args) {
		// Testing andIntroduction
		ArrayList<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		System.out.println(andIntroduction(list));
		
		// Testing andElimination
		System.out.println(andElimination("a&b&c"));
		
		// Testing orIntroduction
		System.out.println(orIntroduction("x", list));
		
		// Testing orElimination
		for (int i = 0; i < 3; i++) {
			list.add(list.remove(0) + "=>d");
		}
		System.out.println(orElimination("a|b|c", list));
		
		// Testing negationIntroduction
		System.out.println(negationIntroduction("a=>b", "a=>~b"));
		
		// Testing negationElimination
		System.out.println(negationElimination("~~a"));
		
		// Testing implicationIntroduction
		System.out.println(implicationIntroduction("a", "b"));
		
		// Testing implicationElimination
		System.out.println(implicationElimination("a=>b", "a"));
		
		// Testing biconditionalIntroduction
		System.out.println(biconditionalIntroduction("a=>b", "b=>a"));
		
		// Testing biconditionalElimination
		System.out.println(biconditionalElimination("a<=>b"));
	}
	
	public static String andIntroduction(ArrayList<String> constants) {
		StringBuilder exp = new StringBuilder("");
		if (constants.isEmpty()) return exp.toString();
		exp.append(constants.get(0));
		for (int i = 1; i < constants.size(); i++) {
			exp.append("&" + constants.get(i));
		}
		return exp.toString();
	}
	
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
	
	public static String orIntroduction(String constant, ArrayList<String> constants) {
		constant = constant.replace(" ", "");
		StringBuilder exp = new StringBuilder(constant);
		for (int i = 0; i < constants.size(); i++) {
			exp.append("|" + constants.get(i));
		}
		return exp.toString();
	}
	
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
		} else return " ";
	}
	
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
	
	public static String negationElimination(String constant) {
		constant = constant.replace(" ", "");
		if (constant.charAt(0) == '~' && constant.charAt(1) == '~') return "" + constant.charAt(2);
		return "";
	}
	
	public static String implicationIntroduction(String one, String two) {
		one = one.replace(" ", "");
		two = two.replace(" ", "");
		return one + "=>" + two;
	}
	
	public static String implicationElimination(String implication, String constant) {
		implication = implication.replace(" ", "");
		constant = constant.replace(" ", "");
		if (constant.charAt(0) == implication.charAt(0)) {
			return "" + implication.charAt(implication.length() - 1);
		}
		return "";
	}
	
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
