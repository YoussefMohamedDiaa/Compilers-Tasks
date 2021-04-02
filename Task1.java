package compilers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

public class  Task1 {

	public static HashSet<Integer> accepted;

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		int[][] dfa = dfa(sc.next());

		// for(int i=0;i<dfa.length;i++) {
		// System.out.println(dfa[i][0]+" "+dfa[i][1]);
		// }
		// System.out.println(accepted.toString());

		while (true) {
			String test = sc.next();
			System.out.println(run(dfa, test));
		}

	}

	static int[][] dfa(String description) {
		accepted = new HashSet<>();
		char[] dis = description.toCharArray();
		int max = -1 * Integer.MAX_VALUE;
		ArrayList<Integer> onlyNum = new ArrayList<>();
		boolean takeAcceptance = false;

		for (int i = 0; i < dis.length; i++) {

			if (dis[i] != ',' && dis[i] != ';' && dis[i] != '#')
				max = Math.max(max, Integer.parseInt("" + dis[i]));

			if (dis[i] != ',' && dis[i] != ';' && dis[i] != '#' && !takeAcceptance)
				onlyNum.add(Integer.parseInt("" + dis[i]));

			if (dis[i] == '#')
				takeAcceptance = true;

			if (takeAcceptance && dis[i] != '#' && dis[i] != ',' && dis[i] != ';')
				accepted.add(Integer.parseInt("" + dis[i]));

		}

		int[][] dfa = new int[max + 1][2];

		for (int i = 0; i < onlyNum.size(); i += 3) {
			dfa[Integer.parseInt("" + onlyNum.get(i))][0] = Integer.parseInt("" + onlyNum.get(i + 1));
			dfa[Integer.parseInt("" + onlyNum.get(i))][1] = Integer.parseInt("" + onlyNum.get(i + 2));
		}

		return dfa;

	}

	static boolean run(int[][] dfa, String test) {
		boolean answer = false;
		char[] path = test.toCharArray();

		int current = 0;
		for (int i = 0; i < path.length; i++) {
			current = dfa[current][Integer.parseInt("" + path[i])];
		}

		if (accepted.contains(current))
			answer = true;

		return answer;
	}

	static class Scanner {
		StringTokenizer st;
		BufferedReader br;

		public Scanner(InputStream s) {
			br = new BufferedReader(new InputStreamReader(s));
		}

		public String next() throws IOException {
			while (st == null || !st.hasMoreTokens())
				st = new StringTokenizer(br.readLine());
			return st.nextToken();
		}

		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}

		public long nextLong() throws IOException {
			return Long.parseLong(next());
		}

		public String nextLine() throws IOException {
			return br.readLine();
		}

		public long nextlong() throws IOException {
			String x = next();
			StringBuilder sb = new StringBuilder("0");
			long res = 0, f = 1;
			boolean dec = false, neg = false;
			int start = 0;
			if (x.charAt(0) == '-') {
				neg = true;
				start++;
			}
			for (int i = start; i < x.length(); i++)
				if (x.charAt(i) == '.') {
					res = Long.parseLong(sb.toString());
					sb = new StringBuilder("0");
					dec = true;
				} else {
					sb.append(x.charAt(i));
					if (dec)
						f *= 10;
				}
			res += Long.parseLong(sb.toString()) / f;
			return res * (neg ? -1 : 1);
		}

		public boolean ready() throws IOException {
			return br.ready();
		}

	}

}

