package compilers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Task4 {
	public static HashSet<Integer> accepted;
	public static String actions[];

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();

		// get actions array
		int max = getMax(s);
		actions = new String[max + 1];

		StringTokenizer st = new StringTokenizer(s, "#");
		String firPart = st.nextToken();
		StringTokenizer st2 = new StringTokenizer(firPart, ";");
		int actionIdx = 0;
		while (st2.hasMoreTokens()) {
			StringTokenizer st3 = new StringTokenizer(st2.nextToken(), ",");
			while (st3.hasMoreTokens()) {
				String cur = st3.nextToken();
				if (!st3.hasMoreTokens()) {
					actions[actionIdx++] = cur;
				}
			}
		}

		int[][] fdfa = fdfa(s, max);

		run(fdfa);

	}

	public static void run(int[][] fdfa) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			String path = sc.next();
			int orig = path.length();
			while (true) {

				Stack<Integer> stack = new Stack<>();
				int current = 0;
				stack.add(current);
				int first = -1;
				for (int i = 0; i < path.length(); i++) {
					current = fdfa[current][Integer.parseInt("" + path.charAt(i))];
					stack.add(current);

					first = current;
				}

				int backStep = 0;
				while (!stack.isEmpty() && !accepted.contains(stack.peek())) {
					stack.pop();
					backStep++;
				}
				// System.out.println(backStep);

				String old = path;
				if (path.length() - backStep > 0) {
					path = path.substring(path.length() - backStep);
				}

				// System.out.println(path);

				if (!stack.isEmpty())
					System.out.print(actions[stack.pop()]);

				if (path.length() == 0)
					break;

				if (path.length() == orig) {
					System.out.println(actions[first]);
					break;
				}

				if (old.equals(path)) {
					System.out.println(actions[first]);
					break;
				}

			}
			System.out.println();
		}
	}

	public static int[][] fdfa(String description, int max) {
		accepted = new HashSet<>();

		StringTokenizer st = new StringTokenizer(description, "#");
		String given = st.nextToken();
		String accep = st.nextToken();

		StringTokenizer st2 = new StringTokenizer(accep, ",");

		while (st2.hasMoreTokens()) {
			// System.out.println(Integer.parseInt(st2.nextToken()));
			accepted.add(Integer.parseInt(st2.nextToken()));
		}

		ArrayList<Integer> onlyNum = new ArrayList<>();

		StringTokenizer st3 = new StringTokenizer(given, ";");
		while (st3.hasMoreTokens()) {
			StringTokenizer st4 = new StringTokenizer(st3.nextToken(), ",");
			int n1 = Integer.parseInt(st4.nextToken());
			onlyNum.add(n1);
			int n2 = Integer.parseInt(st4.nextToken());
			onlyNum.add(n2);
			int n3 = Integer.parseInt(st4.nextToken());
			onlyNum.add(n3);
		}

		int[][] dfa = new int[max + 1][2];

		for (int i = 0; i < onlyNum.size(); i += 3) {
			dfa[Integer.parseInt("" + onlyNum.get(i))][0] = Integer.parseInt("" + onlyNum.get(i + 1));
			dfa[Integer.parseInt("" + onlyNum.get(i))][1] = Integer.parseInt("" + onlyNum.get(i + 2));
		}

		return dfa;

	}

	public static int getMax(String s) {
		StringTokenizer st = new StringTokenizer(s, ";#");
		int max = -1;
		while (st.hasMoreTokens()) {
			String unit = st.nextToken();
			StringTokenizer stu = new StringTokenizer(unit, ",");
			int count =0;
			while (stu.hasMoreTokens()) {
				if(count>=3)
					break;
				int cur = Integer.parseInt(stu.nextToken());
				if (stu.hasMoreTokens())
					max = Math.max(max, cur);
			  count++;
			}
		}
		return max;
	}

}
