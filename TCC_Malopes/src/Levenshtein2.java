import java.util.Scanner;

public class Levenshtein2 {

	public static int distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		// i == 0
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	public static void main(String[] args) 
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner (System.in);
		
		String palavra1 = "";
		String palavra2 = "";
		
		System.out.println("Algoritmo que calcula similaridade entre duas palavras usando distancia de Levenshtein.\n");
		System.out.println("Informe a 1 Palavra: ");
		palavra1 = scan.next();
		System.out.println("Informe a 2 Palavra: ");
		palavra2 = scan.next();
		System.out.println("Similaridade entre palavra 1: " + palavra1 + "\nE a palavra 2: " + palavra2 + "\nÉ " + distance(palavra1, palavra2));
		
		long inicio = System.currentTimeMillis();
		if (distance(palavra1, palavra2) < 4)
		{
			System.out.println("As palavras sao similares...!!");
			long fim = System.currentTimeMillis();
			System.out.println("Tempo gasto foi: " + (fim - inicio));
				
		}else{
			System.out.println("Palavras nao sao Similares... !!");
			long fim = System.currentTimeMillis();
			System.out.println("Tempo gasto foi: " + (fim - inicio));
			
		}
		
		/*String[] data = { "kitten", "sitting", "saturday", "sunday", "rosettacode", "raisethysword" };
		for (int i = 0; i < data.length; i += 2)
			
			//System.out.println("Informe a Palavra: ");
			//palavra = scan.next();
			//System.out.println("distance(" + palavra);
			System.out.println("distance(" + data[i] + ", " + data[i + 1] + ") = " + distance(data[i], data[i + 1]));
	}*/
		
	}
}