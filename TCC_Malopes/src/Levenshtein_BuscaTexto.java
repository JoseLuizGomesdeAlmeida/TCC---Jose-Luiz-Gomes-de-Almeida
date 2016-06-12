import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Levenshtein_BuscaTexto {

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
			for (int j = 1; j <= b.length(); j++) 
			{
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}
	
	static String retiraAcentos(String sString1) throws Exception 
	{

		sString1 = sString1.replace("¡", "A");
		sString1 = sString1.replace("√", "A");
		sString1 = sString1.replace(" ", "E");
		sString1 = sString1.replace("…", "E");
		sString1 = sString1.replace("Õ", "I");
		sString1 = sString1.replace("’", "O");
		sString1 = sString1.replace("”", "O");
		sString1 = sString1.replace("‘", "O");
		sString1 = sString1.replace("⁄", "U");
		sString1 = sString1.replace("«", "C");
		sString1 = sString1.replace("Y", "I");
		sString1 = sString1.replace("PH", "F");
		sString1 = sString1.replace("SH", "X");
		sString1 = sString1.replace("CH", "X");
		sString1 = sString1.replace("RR", "R");
		sString1 = sString1.replace("SS", "S");
		
		return sString1;

	}

	public static void main(String[] args) throws Exception 
	{
		boolean achou = false;
		
		// List<String> palavras = new LinkedList<String>();
		Scanner scan = new Scanner (System.in);
		
		String nome = "teste.txt";
		String palavra = "";

		//String palavra1 = "";
		//String palavra2 = "";
		
		System.out.println("Algoritmo que calcula similaridade entre duas palavras usando distancia de Levenshtein.\n");
		
		System.out.println("Informe a Palavra: ");
		palavra = scan.next();
		
		palavra = palavra.toUpperCase().replaceAll("[^A-Z]", "");
		// System.out.println("Informe a 1 Palavra: ");
		// palavra1 = scan.next();
		
		//System.out.println("Informe a 2 Palavra: ");
		//palavra2 = scan.next();
		
		long inicio = System.currentTimeMillis();
		
		//System.out.println("Similaridade entre palavra 1: " + palavra1 + "\nE a palavra 2: " + palavra2 + "\n… " + distance(palavra1, palavra2));
		Set<String> lista = new HashSet<String>();
		
		try
		{

				FileReader arq = new FileReader(nome);
				BufferedReader lerArq = new BufferedReader(arq);

				String linha = lerArq.readLine();
				
				System.out.print("Pesquisando... ");
				System.out.println(palavra);
				
				while (linha != null) {
					StringTokenizer tokenizer = new StringTokenizer(linha);
					while (tokenizer.hasMoreTokens()) 
					{
						lista.add(retiraAcentos(tokenizer.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
					}if (lista.contains(palavra)) 
					{
						
						// Achou
						System.out.print(palavra);
						System.out.println(" encontrada em: ");
						System.out.println(linha);
						achou = true;
						break;
					} 
					else 
					{
						achou = false;
						// nao achou
						//System.out.println("Erro !!!");;
					}

					//System.out.println((scan.next(palavra)));
					linha = lerArq.readLine(); 
					// lÍ da segunda atÈ a ˙ltima linha
				}
				
				
				if (! achou) {
					for (String s : lista) 
					{
						if (Float.compare(distance(s,palavra), 0.6f) > 0) {
					}
						System.out.println(s.toString());
						arq.close();
				    }
				}
		}
		catch (FileNotFoundException e)
		{
			System.out.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
			e.printStackTrace();
		}
		//fim contador de tempo
		long fim = System.currentTimeMillis();

		System.out.println("Tempo da pesquisa foi de: ");
		System.out.println("0." + (fim-inicio) + " segundos");
		
		/*
		if (distance(palavra1, palavra2) < 4)
		{
			System.out.println("As palavras sao similares...!!");
			long fim = System.currentTimeMillis();
			System.out.println("Tempo gasto foi: " + (fim - inicio));
				
		}
		else
		{
			System.out.println("Palavras nao sao Similares... !!");
			
			//fim contador de tempo
			long fim = System.currentTimeMillis();
			System.out.println("Tempo de pesquisa: 0."+(fim-inicio) + " segundos");

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