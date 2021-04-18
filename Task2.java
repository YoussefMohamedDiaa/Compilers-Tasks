package compilers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

import compilers.Task1.Scanner;

public class Task2 {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		int max = getMax(s);

		ArrayList<Integer>[][] firstTable = new ArrayList[max + 1][2];
		ArrayList<String>[] secondTable = new ArrayList[1000];
		ArrayList<Integer>[] episClosure = new ArrayList[max + 1];
		HashSet<Integer> accep = new HashSet<>();

		
		for (int i = 0; i < max + 1; i++) {
			for (int j = 0; j < 2; j++) {
				firstTable[i][j] = new ArrayList<>();
			}
			episClosure[i] = new ArrayList<>();
			episClosure[i].add(i);
		}

		for (int i = 0; i < 1000; i++) {
			secondTable[i] = new ArrayList<>();
		}

		StringTokenizer st = new StringTokenizer(s, "#");
		String zeros = st.nextToken();
		String ones = st.nextToken();
		String epis = st.nextToken();
		String accp = st.nextToken();

		StringTokenizer stap = new StringTokenizer(accp, ",");
		while (stap.hasMoreTokens()) {
			accep.add(Integer.parseInt("" + stap.nextToken()));
		}

		/////////////// got epis closures/////////////////
		StringTokenizer step = new StringTokenizer(epis, ";");
		while (step.hasMoreTokens()) {
			StringTokenizer stepm = new StringTokenizer(step.nextToken(), ",");
			int from = Integer.parseInt("" + stepm.nextToken());
			int to = Integer.parseInt("" + stepm.nextToken());
			episClosure[from].add(to);
		}

		int saturate = max;
		while (saturate-- > 0) {
			for (int i = 0; i < episClosure.length; i++) {
				for (int j = 1; j < episClosure[i].size(); j++) {
					int child = episClosure[i].get(j);

					for (int k = 1; k < episClosure[child].size(); k++) {
						int toAdd = episClosure[child].get(k);
						if (!episClosure[i].contains(toAdd))
							episClosure[i].add(toAdd);
					}

				}
			}
		}
		/////////////// got epis closures/////////////////

		////////////// got first table////////////////////
		StringTokenizer stze = new StringTokenizer(zeros, ";");
		while (stze.hasMoreTokens()) {
			StringTokenizer stzem = new StringTokenizer(stze.nextToken(), ",");
			int from = Integer.parseInt("" + stzem.nextToken());
			int to = Integer.parseInt("" + stzem.nextToken());

			for (int j = 0; j < episClosure[to].size(); j++) {
				int toAdd = episClosure[to].get(j);
				if (!firstTable[from][0].contains(toAdd))
					firstTable[from][0].add(toAdd);
			}

		}

		StringTokenizer ston = new StringTokenizer(ones, ";");
		while (ston.hasMoreTokens()) {
			StringTokenizer stonm = new StringTokenizer(ston.nextToken(), ",");
			int from = Integer.parseInt("" + stonm.nextToken());
			int to = Integer.parseInt("" + stonm.nextToken());

			for (int j = 0; j < episClosure[to].size(); j++) {
				int toAdd = episClosure[to].get(j);
				if (!firstTable[from][1].contains(toAdd))
					firstTable[from][1].add(toAdd);
			}

		}

		////////////// got first table////////////////////

		// for(int i=0;i<firstTable.length;i++) {
		// System.out.println(firstTable[i][0].toString());
		// }
		// System.out.println("===============");
		// for(int i=0;i<firstTable.length;i++) {
		// System.out.println(firstTable[i][1].toString());
		// }

		////////////// got second table//////////////////
		HashSet<String> done = new HashSet<>();
		// real ?
		done.add("0");
		int current = 0;
		int putter = 1;

		ArrayList<Integer> toAdd = new ArrayList<>();
		for (int i = 0; i < episClosure[0].size(); i++) {
			toAdd.add(episClosure[0].get(i));
		}
		Collections.sort(toAdd);

		String tmp3 = "";
		for (int i = toAdd.size() - 1; i > -1; i--) {
			tmp3 += " " + toAdd.get(i);
		}
		secondTable[current].add(tmp3);
		String starting = tmp3;
		done.add(tmp3);

		while (true) {
			// System.out.println(current);
			// System.out.println(secondTable[current].size());
			// System.out.println(secondTable[current].toString());
			if (secondTable[current].size() == 0)
				break;

			String curr = "" + secondTable[current].get(0);

			// add for zero tran
			toAdd = new ArrayList<>();
			StringTokenizer stcur = new StringTokenizer(curr);
			while (stcur.hasMoreTokens()) {
				int curN = Integer.parseInt("" + stcur.nextToken());
				for (int j = 0; j < firstTable[curN][0].size(); j++) {
					if (!toAdd.contains(firstTable[curN][0].get(j)))
						toAdd.add(firstTable[curN][0].get(j));
				}
			}

			Collections.sort(toAdd);
			String tmp1 = "";
			for (int i = toAdd.size() - 1; i > -1; i--) {
				tmp1 += " " + toAdd.get(i);
			}

			if (!tmp1.equals(""))
				secondTable[current].add(tmp1);
			else
				secondTable[current].add("-1");

			// add for one tran
			toAdd = new ArrayList<>();
			stcur = new StringTokenizer(curr);
			while (stcur.hasMoreTokens()) {
				int curN = Integer.parseInt("" + stcur.nextToken());
				for (int j = 0; j < firstTable[curN][1].size(); j++) {
					if (!toAdd.contains(firstTable[curN][1].get(j)))
						toAdd.add(firstTable[curN][1].get(j));
				}
			}

			Collections.sort(toAdd);
			String tmp2 = "";
			for (int i = toAdd.size() - 1; i > -1; i--) {
				tmp2 += " " + toAdd.get(i);
			}

			if (!tmp2.equals(""))
				secondTable[current].add(tmp2);
			else
				secondTable[current].add("-1");

			done.add(secondTable[current].get(0));
			if (!tmp1.equals("") && !done.contains(tmp1)) {
				secondTable[putter].add(tmp1);
				putter++;
				done.add(tmp1);
			}
			if (!tmp2.equals("") && !done.contains(tmp2)) {
				secondTable[putter].add(tmp2);
				putter++;
				done.add(tmp2);
			}

			current++;

		}

		////////////// got second table//////////////////

		// for (int i = 0; i < 8; i++) {
		// System.out.println(secondTable[i].toString());
		// }

		while (true) {
			String test = sc.next();

			String at = starting;
			for (int i = 0; i < test.length(); i++) {
				int bin = Integer.parseInt("" + test.charAt(i));
				int idx = find(at, secondTable);
				if (bin == 0)
					at = secondTable[idx].get(1);
				else
					at = secondTable[idx].get(2);

				if (at.equals("-1"))
					break;

			}

			if (at.equals("-1")) {
				System.out.println(false);
				continue;
			}

			String res = "" + at;

			boolean found = false;
			StringTokenizer stres = new StringTokenizer(res);
			while (stres.hasMoreTokens()) {
				if (accep.contains(Integer.parseInt("" + stres.nextToken()))) {
					found = true;
					break;
				}
			}

			if (found)
				System.out.println(true);
			else
				System.out.println(false);

		}

	}

	static int find(String num, ArrayList<String>[] secondTable) {
		for (int i = 0; i < secondTable.length; i++) {
			// System.out.println(num);
			if (num.equals(secondTable[i].get(0)))
				return i;
		}
		return -1;
	}

	static int getMax(String s) {
		StringTokenizer st = new StringTokenizer(s, "#,;");
		int max = -1 * Integer.MAX_VALUE;
		while (st.hasMoreTokens()) {
			String k = st.nextToken();
			max = Math.max(max, Integer.parseInt("" + k));
		}

		return max;
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