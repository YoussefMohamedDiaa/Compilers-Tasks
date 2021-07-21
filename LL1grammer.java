package compilers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LL1grammer {
	static ArrayList<Character> lookUp = new ArrayList<>();
	static int terminalN;
	static int varN;
	static ArrayList<String> grammerAr;
	static ArrayList<String> firstAr;
	static ArrayList<String> followAr;
	static String[][] ll1Table;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		StringTokenizer st = new StringTokenizer(s, "#");
		String gram = st.nextToken();
		String firs = st.nextToken();
		String foll = st.nextToken();

		grammerAr = new ArrayList<>();
		StringTokenizer stg = new StringTokenizer(gram, ";");
		while (stg.hasMoreTokens())
			grammerAr.add(stg.nextToken());

		firstAr = new ArrayList<>();
		StringTokenizer stfi = new StringTokenizer(firs, ";");
		while (stfi.hasMoreTokens())
			firstAr.add(stfi.nextToken());

		followAr = new ArrayList<>();
		StringTokenizer stfo = new StringTokenizer(foll, ";");
		while (stfo.hasMoreTokens())
			followAr.add(stfo.nextToken());

		varN = grammerAr.size();

		HashSet<Character> hs = new HashSet<>();
		for (int i = 0; i < grammerAr.size(); i++) {
			for (int j = 0; j < grammerAr.get(i).length(); j++) {
				if (grammerAr.get(i).charAt(j) >= 'a' && grammerAr.get(i).charAt(j) <= 'z'
						&& !(grammerAr.get(i).charAt(j) == 'e')) {
					if (!hs.contains(grammerAr.get(i).charAt(j)))
						lookUp.add(grammerAr.get(i).charAt(j));
					hs.add(grammerAr.get(i).charAt(j));
				}
			}
		}

		Collections.sort(lookUp);
		lookUp.add('$');

		terminalN = hs.size() + 1;
		// System.out.println(varN);
		// System.out.println(terminalN);
		//System.out.println(lookUp.toString());

		ll1();
		// System.out.println(grammerAr.toString());
		// System.out.println(firstAr.toString());
		// System.out.println(followAr.toString());
		while(true) {
			String word = sc.next();
			System.out.println(parse(word));	
		}
		

	}

	public static String parse(String word) {

		String curDir = "S$";
		String Dir = curDir.substring(0, curDir.length()-1);;
		word += "$";
		int i=0;
		while(true) {

			
			int firstVarIdx = findRuleIndex(getFirstVar(curDir)+"");
			int firtVarPlaceInDir = getFirstVarPlace(curDir);
			//add on
			i = firtVarPlaceInDir;
			if(i>=word.length()) {
				curDir = "ERROR";
				Dir += "," + curDir;
				return Dir;
			}
			char neededChar = word.charAt(i);
			if (firtVarPlaceInDir == -1) {
				//System.out.println("error1");
				curDir = "ERROR";
				Dir += "," + curDir;
				return Dir;
			}

			int terPlace = terminalPlaceInLl1(neededChar);

			String subBy = ll1Table[firstVarIdx][terPlace];
			if (subBy == null) {
//				System.out.println("neeed "+neededChar);
//				System.out.println("cur Dir "+getFirstVar(curDir));
//				System.out.println("error2");
				curDir = "ERROR";
				Dir += "," + curDir;
				return Dir;
			}
			
//			System.out.println("curDir "+curDir);
//			System.out.println("subBy "+subBy);
//			System.out.println("needed char "+neededChar);
//			System.out.println("ter place "+terPlace);
//			System.out.println("----------------------");
			if (!subBy.equals("e")) {
				curDir = curDir.substring(0, firtVarPlaceInDir) + subBy + curDir.substring(firtVarPlaceInDir + 1, curDir.length());
//			    if(subBy.charAt(0)==neededChar)
//			    	i++;
			}else {
				curDir = curDir.substring(0, firtVarPlaceInDir) + curDir.substring(firtVarPlaceInDir + 1, curDir.length());
//				if(curDir.charAt(firtVarPlaceInDir )==neededChar)
//			    	i++;
			}
			
			Dir += "," + curDir.substring(0, curDir.length()-1);

			if(curDir.equals(word))
				break;
			
		}
		
		

		return Dir;

	}

	public static char getFirstVar(String curDir) {
		for (int i = 0; i < curDir.length(); i++) {
			if (!(curDir.charAt(i) >= 'a' && curDir.charAt(i) <= 'z'))
				return curDir.charAt(i);
		}
		System.out.println("problem");
		return 'm';
	}
	
	public static int getFirstVarPlace(String curDir) {
		for (int i = 0; i < curDir.length(); i++) {
			if (!(curDir.charAt(i) >= 'a' && curDir.charAt(i) <= 'z'))
				return i;
		}
		System.out.println("problem");
		return 'm';
	}

	public static void ll1() {
		ll1Table = new String[varN][terminalN];
		for (int i = 0; i < grammerAr.size(); i++) {
			StringTokenizer st = new StringTokenizer(grammerAr.get(i), ",");
			String head = st.nextToken();
			int ruleIdx = 0;
			while (st.hasMoreTokens()) {
				String curRight = st.nextToken();
				String valuesToPutTheRuleFor = getValuesReachable(i, ruleIdx);
				// System.out.println(valuesToPutTheRuleFor);
				fillTable(i, curRight, valuesToPutTheRuleFor);
				ruleIdx++;
			}

		}

//		 for(int i=0;i<ll1Table.length;i++)
//		 System.out.println(Arrays.toString(ll1Table[i]));

	}

	public static void fillTable(int headIdx, String rule, String valuesToPutFor) {

		for (int i = 0; i < valuesToPutFor.length(); i++) {
			char curChar = valuesToPutFor.charAt(i);
			int terPlace = terminalPlaceInLl1(curChar);
			ll1Table[headIdx][terPlace] = rule;
		}

	}

	public static int terminalPlaceInLl1(char curChar) {
		for (int i = 0; i < lookUp.size(); i++) {
			if (curChar == lookUp.get(i))
				return i;
		}
		System.out.println("problem");
		return -1;
	}

	public static String getValuesReachable(int headIdx, int ruleIdx) {

		String firstHead = firstAr.get(headIdx);
		StringTokenizer st = new StringTokenizer(firstHead, ",");
		st.nextToken();
		String curRight = "";
		int counter = 0;
		while (st.hasMoreTokens()) {
			curRight = st.nextToken();
			if (counter == ruleIdx)
				break;
			counter++;
		}

		char firChar = curRight.charAt(0);
		if (firChar >= 'a' && firChar <= 'z' && firChar != 'e') {
			if (!curRight.contains("e"))
				return curRight;
			else {
				for (int i = 0; i < curRight.length(); i++) {
					if (curRight.charAt(i) == 'e') {
						curRight = curRight.substring(0, i) + curRight.substring(i + 1, curRight.length());
						break;
					}
				}

				String followHead = followAr.get(headIdx);
				st = new StringTokenizer(followHead, ",");
				st.nextToken();
				return curRight + st.nextToken();

			}

		} else if (firChar == 'e') {
			String followHead = followAr.get(headIdx);
			st = new StringTokenizer(followHead, ",");
			st.nextToken();
			return st.nextToken();
		}

		return "";
	}

	public static int findRuleIndex(String head) {
		for (int i = 0; i < grammerAr.size(); i++) {
			if (head.equals(grammerAr.get(i).charAt(0) + ""))
				return i;
		}
		return -1;
	}

}
