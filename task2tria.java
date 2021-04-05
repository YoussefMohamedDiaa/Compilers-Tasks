package compilers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

public class task2tria {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		int max = getMax(s);

		ArrayList<Integer>[][] firstTable = new ArrayList[max + 1][2];
		ArrayList<Integer>[] secondTable = new ArrayList[1000];
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

		for (int i = 0; i < accp.length(); i += 2) {
			accep.add(Integer.parseInt("" + accp.charAt(i)));
		}

		/////////////// got epis closures/////////////////
		for (int i = 0; i < epis.length(); i++) {
			int from = Integer.parseInt("" + epis.charAt(i));
			int to = Integer.parseInt("" + epis.charAt(i + 2));
			episClosure[from].add(to);
			i += 3;
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
		for (int i = 0; i < zeros.length(); i++) {
			int from = Integer.parseInt("" + zeros.charAt(i));
			int to = Integer.parseInt("" + zeros.charAt(i + 2));

			for (int j = 0; j < episClosure[to].size(); j++) {
				int toAdd = episClosure[to].get(j);
				if (!firstTable[from][0].contains(toAdd))
					firstTable[from][0].add(toAdd);
			}

			i += 3;

		}

		for (int i = 0; i < ones.length(); i++) {
			int from = Integer.parseInt("" + ones.charAt(i));
			int to = Integer.parseInt("" + ones.charAt(i + 2));

			for (int j = 0; j < episClosure[to].size(); j++) {
				int toAdd = episClosure[to].get(j);
				if (!firstTable[from][1].contains(toAdd))
					firstTable[from][1].add(toAdd);
			}

			i += 3;

		}

		////////////// got first table////////////////////

//		 for(int i=0;i<firstTable.length;i++) {
//		 System.out.println(firstTable[i][0].toString());
//		 }
//		 System.out.println("===============");
//		 for(int i=0;i<firstTable.length;i++) {
//		 System.out.println(firstTable[i][1].toString());
//		 }

		////////////// got second table//////////////////
		HashSet<Integer> done = new HashSet<>();
		done.add(0);
		int current = 0;
		int putter = 1;
		
		ArrayList<Integer> toAdd = new ArrayList<>();
		for(int i=0;i<episClosure[0].size();i++) {
			toAdd.add(episClosure[0].get(i));
		}
		Collections.sort(toAdd);
		
		String tmp3 = "";
		for (int i = toAdd.size() - 1; i > -1; i--) {
			tmp3 += toAdd.get(i);
		}
		secondTable[current].add(Integer.parseInt(tmp3));
        int starting = Integer.parseInt(tmp3);
		done.add(Integer.parseInt(tmp3));
		
		while (true) {
			// System.out.println(current);
			// System.out.println(secondTable[current].size());
			// System.out.println(secondTable[current].toString());
			if (secondTable[current].size() == 0)
				break;

			String curr = "" + secondTable[current].get(0);

			// add for zero tran
			toAdd = new ArrayList<>();
			for (int i = 0; i < curr.length(); i++) {
				int curN = Integer.parseInt("" + curr.charAt(i));
				for (int j = 0; j < firstTable[curN][0].size(); j++) {
					if (!toAdd.contains(firstTable[curN][0].get(j)))
						toAdd.add(firstTable[curN][0].get(j));
				}
			}

			Collections.sort(toAdd);
			String tmp1 = "";
			for (int i = toAdd.size() - 1; i > -1; i--) {
				tmp1 += toAdd.get(i);
			}

			if (!tmp1.equals(""))
				secondTable[current].add(Integer.parseInt(tmp1));
			else
				secondTable[current].add(-1);

			// add for one tran
			toAdd = new ArrayList<>();
			for (int i = 0; i < curr.length(); i++) {
				int curN = Integer.parseInt("" + curr.charAt(i));
				for (int j = 0; j < firstTable[curN][1].size(); j++) {
					if (!toAdd.contains(firstTable[curN][1].get(j)))
						toAdd.add(firstTable[curN][1].get(j));
				}
			}

			Collections.sort(toAdd);
			String tmp2 = "";
			for (int i = toAdd.size() - 1; i > -1; i--) {
				tmp2 += toAdd.get(i);
			}

			if (!tmp2.equals(""))
				secondTable[current].add(Integer.parseInt(tmp2));
			else
				secondTable[current].add(-1);

			done.add(secondTable[current].get(0));
			if (!tmp1.equals("") && !done.contains(Integer.parseInt(tmp1))) {
				secondTable[putter].add(Integer.parseInt(tmp1));
				putter++;
				done.add(Integer.parseInt(tmp1));
			}
			if (!tmp2.equals("") && !done.contains(Integer.parseInt(tmp2))) {
				secondTable[putter].add(Integer.parseInt(tmp2));
				putter++;
				done.add(Integer.parseInt(tmp2));
			}

			current++;

		}

		////////////// got second table//////////////////

		for (int i = 0; i < 8; i++) {
			System.out.println(secondTable[i].toString());
		}

		while (true) {
			String test = sc.next();

			int at = starting;
			for (int i = 0; i < test.length(); i++) {
				int bin = Integer.parseInt("" + test.charAt(i));
				int idx = find(at, secondTable);
				if (bin == 0)
					at = secondTable[idx].get(1);
				else
					at = secondTable[idx].get(2);

				if (at == -1)
					break;

			}

			if (at == -1) {
				System.out.println(false);
				continue;
			}

			String res = "" + at;

			boolean found = false;
			for (int i = 0; i < res.length(); i++) {
				if (accep.contains(Integer.parseInt("" + res.charAt(i)))) {
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

	static int find(int num, ArrayList<Integer>[] secondTable) {
		for (int i = 0; i < secondTable.length; i++) {
			//System.out.println(num);
			if (num == secondTable[i].get(0))
				return i;
		}
		return -1;
	}

	static int getMax(String s) {
		int max = -1 * Integer.MAX_VALUE;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ';' && s.charAt(i) != ',' && s.charAt(i) != '#')
				max = Math.max(max, Integer.parseInt("" + s.charAt(i)));
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
