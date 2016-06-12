import java.util.Scanner;

public class LevenshteinDistance_2String 
{
	private static int minimo(int a, int b, int c) 
	{
		return Math.min(Math.min(a, b), c);
	}

	public static int calculoLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
		int[][] distancia = new int[lhs.length() + 1][rhs.length() + 1];

		for (int i = 0; i <= lhs.length(); i++)
			distancia[i][0] = i;
		for (int j = 1; j <= rhs.length(); j++)
			distancia[0][j] = j;

		for (int i = 1; i <= lhs.length(); i++)
			for (int j = 1; j <= rhs.length(); j++)
				distancia[i][j] = minimo(distancia[i - 1][j] + 1, distancia[i][j - 1] + 1,
						distancia[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

		return distancia[lhs.length()][rhs.length()];
	}

	public int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
		int len0 = lhs.length() + 1;
		int len1 = rhs.length() + 1;

		// Cria array das distancias
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		for (int i = 0; i < len0; i++)
			cost[i] = i;

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

	@SuppressWarnings("resource")
	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);

		String palavra1 = "";
		String palavra2 = "";

		System.out.println("Algoritmo que calcula similaridade entre duas palavras usando distancia de Levenshtein.\n");
		
		System.out.println("Informe a 1 Palavra: ");
		palavra1 = scan.next();
		
		System.out.println("Informe a 2 Palavra: ");
		palavra2 = scan.next();
		
		// inicio contador de tempo
		long inicio = System.currentTimeMillis();
					
		System.out.println("Similaridade entre palavra 1: " + palavra1 + "\nE a palavra 2: " + palavra2 + "\nÉ "
				+ LevenshteinDistance_2String.calculoLevenshteinDistance(palavra1, palavra2));

		if (LevenshteinDistance_2String.calculoLevenshteinDistance(palavra1, palavra2) < 4) 
		{
			System.out.println("As palavras sao similares...!!");
			long fim = System.currentTimeMillis();
			System.out.println("Tempo da pesquisa foi de: ");
			System.out.println("0." + (fim-inicio) + " segundos");

		} 
		else 
		{
			System.out.println("Palavras nao sao Similares... !!");
			
			long fim = System.currentTimeMillis();
			System.out.println("Tempo da pesquisa foi de: ");
			System.out.println("0." + (fim-inicio) + " segundos");

		}

		/*
		 * String[] data = { "kitten", "sitting", "saturday", "sunday","rosettacode", "raisethysword" }; 
		 * for (int i = 0; i < data.length; i* += 2){
		 * System.out.println("Informe a Palavra: ");
		 * palavra = scan.next();
		 * System.out.println("distance(" "+ palavra);
		 * System.out.println("distance(" + data[i] + ", " + data[i + 1] + ") = " + distance(data[i], data[i + 1])); }
		 */
	}

}