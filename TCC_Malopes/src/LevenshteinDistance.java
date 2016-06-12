import java.util.Scanner;

public class LevenshteinDistance 
{
	private static int minimum(int a, int b, int c) 
	{
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
		int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

		for (int i = 0; i <= lhs.length(); i++)
			distance[i][0] = i;
		for (int j = 1; j <= rhs.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= lhs.length(); i++)
			for (int j = 1; j <= rhs.length(); j++)
				distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1,
						distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

		return distance[lhs.length()][rhs.length()];
	}

	public int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
		int len0 = lhs.length() + 1;
		int len1 = rhs.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; i++)
			cost[i] = i;

		// dynamically computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; j++) {
			// initial cost of skipping prefix in String s1
			newcost[0] = j;

			// transformation cost for each letter in s0
			for (int i = 1; i < len0; i++) {
				// matching current letters in both strings
				int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				int cost_replace = cost[i - 1] + match;
				int cost_insert = cost[i] + 1;
				int cost_delete = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}

			// swap cost/newcost arrays
			int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}

	public static void main(String[] args) 
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		String palavra1 = "";
		String palavra2 = "";

		System.out.println("Teste"); 
		
		System.out.println("Algoritmo que calcula similaridade entre duas palavras usando distancia de Levenshtein.\n");
		
		System.out.println("Informe a 1 Palavra: ");
		palavra1 = scan.next();
		
		System.out.println("Informe a 2 Palavra: ");
		palavra2 = scan.next();
		
		System.out.println("Similaridade entre as palavras \n 1: " + palavra1 + "\nE a palavra 2: " + palavra2 + "\nÉ "
				+ LevenshteinDistance.computeLevenshteinDistance(palavra1, palavra2));

		long inicio = System.currentTimeMillis();
		if (LevenshteinDistance.computeLevenshteinDistance(palavra1, palavra2) < 4) 
		{
			System.out.println("As palavras sao similares...!!");
			long fim = System.currentTimeMillis();
			System.out.println("Tempo gasto foi: " + (fim - inicio));

		} else 
		{
			System.out.println("Palavras nao sao Similares... !!");
			long fim = System.currentTimeMillis();
			System.out.println("Tempo gasto foi: 0," + (fim - inicio));

		}
	}
}