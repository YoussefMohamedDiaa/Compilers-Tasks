package compilers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FirstAndFollow {
	public static HashSet<Character> haveE = new HashSet<>();

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();

		List<String> rules = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(s, ";");
		while (st.hasMoreTokens())
			rules.add(st.nextToken());

		String firstResult = "";
		for (int i = 0; i < rules.size(); i++) {

			char curHead = rules.get(i).charAt(0);
			HashSet<Character> visited = new HashSet<>();
			//get first for each variable
			String curFirst = First(rules, i, visited);

			//filter first characters from repetions and sort them
			HashSet<Character> firsts = new HashSet<>();
			ArrayList<Character> firstArray = new ArrayList<>();
			for (int j = 0; j < curFirst.length(); j++) {
				if (!firsts.contains(curFirst.charAt(j))) {
					firstArray.add(curFirst.charAt(j));
					firsts.add(curFirst.charAt(j));
				}
			}
			Collections.sort(firstArray);

			firstResult += curHead + ",";
			for (int j = 0; j < firstArray.size(); j++) {
				firstResult += firstArray.get(j);
			}

			firstResult += ";";

		}
		firstResult = firstResult.substring(0, firstResult.length() - 1);
		System.out.println(firstResult);

		List<String> firstsRules = new ArrayList<>();
		StringTokenizer sts = new StringTokenizer(firstResult, ";");
		while (sts.hasMoreTokens())
			firstsRules.add(sts.nextToken());

		//get follow
		List<String> followRules = Follow(rules, firstsRules);
		String followResult = "";
		for (int i = 0; i < followRules.size(); i++)
			followResult += followRules.get(i) + ";";

		System.out.println(followResult.substring(0, followResult.length() - 1));
	}

	public static String First(List<String> rules, int idx, HashSet<Character> visited) {
		boolean goAgain = false;
		int time = 0;
		String result = "";

		while ((goAgain && time == 1) || time == 0) {

			time++;
			String curRule = rules.get(idx);
			StringTokenizer st2 = new StringTokenizer(curRule, ",");
			char head = st2.nextToken().charAt(0);
			visited.add(head);
			while (st2.hasMoreTokens()) {
				String rightSide = st2.nextToken();
				//if terminal added to firsts
				if (rightSide.charAt(0) >= 'a' && rightSide.charAt(0) <= 'z') {
					result += rightSide.charAt(0);
					//if it has epis put it in have epislon so we go again
					if (rightSide.charAt(0) == 'e') {
						haveE.add(head);
						goAgain = true;
					}

				} else {
					//nonterminal case
					int diveInto = 0;
					String elseResult = "";
					while (true) {
                        
						//if nonterminal gets us to terminal
						if (rightSide.charAt(diveInto) >= 'a' && rightSide.charAt(diveInto) <= 'z') {
							elseResult += rightSide.charAt(diveInto);
							break;
						}

						String curResult = "";
						String curResultCleaned = "";
						//if not visited before check where to go and get its firsts
						if (!visited.contains(rightSide.charAt(diveInto))) {
							visited.add(rightSide.charAt(diveInto));
							int whereToGo = findRule(rules, rightSide.charAt(diveInto));
							curResult = First(rules, whereToGo, visited);
						}
						boolean haveEmpty = false;
						//add the result we got
						if (curResult.contains("e")) {
							haveE.add(rightSide.charAt(diveInto));
							for (int i = 0; i < curResult.length(); i++) {
								if (curResult.charAt(i) != 'e')
									curResultCleaned += curResult.charAt(i);
							}
						} else {
							curResultCleaned = curResult;
						}

						haveEmpty = haveE.contains(rightSide.charAt(diveInto));
						elseResult += curResultCleaned;
						diveInto++;
						//if reached to terminal break
						if (!haveEmpty)
							break;
                        //if all got empty then add e the main rule and go one more check 
						if (diveInto >= rightSide.length()) {
							haveE.add(head);
							goAgain = true;
							elseResult += 'e';
							break;
						}
					}

					result += elseResult;
				}
			}
		}

		return result;
	}

	public static int findRule(List<String> rules, char lookingFor) {
		for (int i = 0; i < rules.size(); i++) {
			if (rules.get(i).charAt(0) == lookingFor)
				return i;
		}
		System.out.println(lookingFor);
		System.out.println("problem");
		return -1;
	}

	public static List<String> Follow(List<String> rules, List<String> firstsRules) {

		List<String> oldSc = new ArrayList<>();
		List<String> curResults = new ArrayList<>();
		for (int i = 0; i < rules.size(); i++) {
			oldSc.add("");
			curResults.add("");
		}
			
		while (true) {
			//for each rule
			for (int i = 0; i < rules.size(); i++) {

				curResults.set(0, curResults.get(0) + "$");

				StringTokenizer st = new StringTokenizer(rules.get(i), ",");
				char head = st.nextToken().charAt(0);
				//for each part in right
				while (st.hasMoreTokens()) {
					String curRight = st.nextToken();
					for (int j = 0; j < curRight.length(); j++) {
						if (curRight.charAt(j) >= 'a' && curRight.charAt(j) <= 'z')
							continue;
						//check what to add and where to add for each var if term skip as above
						String toAdd = whatToAdd(rules, curRight, j, firstsRules,curResults, head);
						int placeToAdd = findRule(rules, curRight.charAt(j));
						curResults.set(placeToAdd, curResults.get(placeToAdd) + toAdd);
					}
				}

			}
			List<String> newSc = screenShot(curResults);
//			System.out.println(oldSc.get(0)+"          "+newSc.get(0));
//			System.out.println(oldSc.get(1)+"          "+newSc.get(1));
//			System.out.println(oldSc.get(2)+"          "+newSc.get(2));
//			System.out.println("--------------------");
			//if equal screenshots break
			if (equalScs(newSc, oldSc)) {
				oldSc = newSc;
				break;
			}
				

			oldSc = newSc;

		}

		// prepare
		List<String> results = new ArrayList<>();
		for (int i = 0; i < rules.size(); i++) {
			results.add(rules.get(i).charAt(0) + "," + oldSc.get(i));
		}

		return results;
	}

	public static String whatToAdd(List<String> rules, String curRight, int idx, List<String> firstsRules,
			List<String> followSoFar, char head) {

		String result = "";
		boolean stopped = false;
		for (int i = idx + 1; i < curRight.length(); i++) {
			if (i >= curRight.length())
				break;

			//if terminal add as follow and break
			if (curRight.charAt(i) >= 'a' && curRight.charAt(i) <= 'z') {
				result += curRight.charAt(i);
				stopped = true;
				break;
			}
            //if nonterminal get firsts of it
			int whereFirsts = findRule(rules, curRight.charAt(i));
			StringTokenizer st = new StringTokenizer(firstsRules.get(whereFirsts), ",");
			//System.out.println("+++"+firstsRules.get(whereFirsts).toString());
			st.nextToken();
			String firsts = st.nextToken();
			//System.out.println(firsts);
			if (!firsts.contains("e")) {
				stopped = true;
				result += firsts;
				break;
			} else {
				for(int j=0;j<firsts.length();j++) {
					if(firsts.charAt(j)=='e') {
						firsts= firsts.substring(0,j)+firsts.substring(j+1);
						break;
					}		
				}
				
				result += firsts;
			}
			
		}
        //if it is not stopped till the end so we will take the upper follow
		if (!stopped) {
			int place = findRule(rules, head);
			result += followSoFar.get(place);
		}
		

		return result;
	}

	public static boolean equalScs(List<String> newSc, List<String> oldSc) {
		for (int i = 0; i < newSc.size(); i++) {
			if (!newSc.get(i).equals(oldSc.get(i)))
				return false;
		}
		return true;
	}

	public static List<String> screenShot(List<String> results) {

		List<String> scResults = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < results.get(i).length(); j++) {
				if (!sb.toString().contains("" + results.get(i).charAt(j)))
					sb.append(results.get(i).charAt(j));
			}

			char curArResult[] = sb.toString().toCharArray();
			Arrays.sort(curArResult);
			
			String res = "";
			for(int j=0;j<curArResult.length;j++)
				res+=curArResult[j];
				
			if(res.contains("$")) {
				for(int j=0;j<res.length();j++) {
					if(res.charAt(j)=='$') {
						res= res.substring(0,j)+res.substring(j+1);
						break;
					}		
				}
				res+="$";
			}
				
			
			scResults.add(res);
		}

		return scResults;
	}

}
