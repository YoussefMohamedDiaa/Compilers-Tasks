package compilers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import Ladder1400.array;

public class LeftRecursionElimination {
	
	public static void main(String[] args) {

		// List<Integer> foo = new ArrayList<>();
		// foo.add(1);
		// foo.add(1);
		// foo.add(1);
		// foo.add(1);
		// foo.add(2, 4);
		// System.out.println(foo);
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		String answer = LRE(s);
		System.out.println(answer);

	}

	public static String LRE(String inputCFG) {
		List<String> rules = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(inputCFG, ";");
		while (st.hasMoreTokens())
			rules.add(st.nextToken());

		for (int i = 0; i < rules.size(); i++) {
			for (int j = 0; j <= i; j++) {
				if (j == i) {
					String curRule = rules.get(j);
					StringTokenizer st2 = new StringTokenizer(curRule, ",");
					String leftSide = st2.nextToken();
					ArrayList<String> betas = new ArrayList<>();
					ArrayList<String> alphas = new ArrayList<>();
					while (st2.hasMoreTokens()) {
						String cur = st2.nextToken();
						if ((cur.charAt(0) + "").equals(leftSide)) {
							alphas.add(cur.substring(1));
						} else {
							betas.add(cur);
						}
					}

					if (alphas.size() != 0) {
						String newRule1 = leftSide;
						String newRule2 = leftSide + "'";
						for (String beta : betas) {
							newRule1 += "," + beta + leftSide + "'";
						}
						for (String alpha : alphas) {
							newRule2 += "," + alpha + leftSide + "'";
						}
						newRule2 += ",e";
						// System.out.println(newRule1);
						// System.out.println(newRule2);
						rules.remove(j);
						rules.add(j, newRule1);
						rules.add(j + 1, newRule2);
						j++;
						i++;
					}
				} else {
					String beforeRule = rules.get(j);
					StringTokenizer st2 = new StringTokenizer(beforeRule, ",");
					String leftSideBeforeRule = st2.nextToken();
					ArrayList<String> rightSideBeforeRule = new ArrayList<>();
					while (st2.hasMoreTokens())
						rightSideBeforeRule.add(st2.nextToken());

					String nowRule = rules.get(i);
					st2 = new StringTokenizer(nowRule, ",");
					String leftSideNowRule = st2.nextToken();
					ArrayList<String> rightSideNowRule = new ArrayList<>();
					while (st2.hasMoreTokens())
						rightSideNowRule.add(st2.nextToken());

					for (int k = 0; k < rightSideNowRule.size(); k++) {
//						System.out.println(k);
//						System.out.println(rightSideNowRule.get(k));
						if ((rightSideNowRule.get(k).charAt(0) + "").equals(leftSideBeforeRule)) {
							String hold = rightSideNowRule.get(k).substring(1);
							rightSideNowRule.remove(k);
							
							for (String part : rightSideBeforeRule) {
								rightSideNowRule.add(k++, part + hold);
//								System.out.println(k);
							}
							k--;
								
						}
					}
					
					String toAdd = "";
					for(String part : rightSideNowRule)
						toAdd+=part+",";
					toAdd= toAdd.substring(0,toAdd.length()-1);
					rules.set(i, leftSideNowRule+","+toAdd);
					//System.out.println(rules.get(i));
					
				}
			}
		}

//		System.out.println(rules.toString());

		String cfg = "";
		for(String rule :rules)
			cfg+=rule+";";
		
		cfg = cfg.substring(0, cfg.length()-1);
		return cfg;
	}
}
