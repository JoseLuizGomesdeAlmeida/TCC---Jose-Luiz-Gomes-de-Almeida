import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Soundex {

	public static String soundex(String s) {
		char[] x = s.toUpperCase().toCharArray();
		char firstLetter = x[0];

		// convert letters to numeric code
		for (int i = 0; i < x.length; i++) {
			switch (x[i]) {

			case 'B':
			case 'F':
			case 'P':
			case 'V':
				x[i] = '1';
				break;

			case 'C':
			case 'G':
			case 'J':
			case 'K':
			case 'Q':
			case 'S':
			case 'X':
			case 'Z':
				x[i] = '2';
				break;

			case 'D':
			case 'T':
				x[i] = '3';
				break;

			case 'L':
				x[i] = '4';
				break;

			case 'M':
			case 'N':
				x[i] = '5';
				break;

			case 'R':
				x[i] = '6';
				break;

			default:
				x[i] = '0';
				break;
			}
		}

		// remove duplicates
		String output = "" + firstLetter;
		for (int i = 1; i < x.length; i++)
			if (x[i] != x[i - 1] && x[i] != '0')
				output += x[i];

		// pad with 0's or truncate
		output = output + "0000";
		return output.substring(0, 4);
	}

	static String retiraAcentos(String sString1) throws Exception {

		sString1 = sString1.replace("Á", "A");
		sString1 = sString1.replace("Ã", "A");
		sString1 = sString1.replace("Ê", "E");
		sString1 = sString1.replace("É", "E");
		sString1 = sString1.replace("Í", "I");
		sString1 = sString1.replace("Õ", "O");
		sString1 = sString1.replace("Ó", "O");
		sString1 = sString1.replace("Ô", "O");
		sString1 = sString1.replace("Ú", "U");
		sString1 = sString1.replace("Ç", "C");
		sString1 = sString1.replace("Y", "I");
		sString1 = sString1.replace("PH", "F");
		sString1 = sString1.replace("SH", "X");
		sString1 = sString1.replace("CH", "X");
		sString1 = sString1.replace("RR", "R");
		sString1 = sString1.replace("SS", "S");

		return sString1;
	}

	public static void main (String args []) throws Exception
    {
    	Scanner scan = new Scanner (System.in);
    	
    	String palavra = "";
    	
    	Boolean achou = false;
    	
    	String nome = "teste.txt";

		System.out.printf("Pesquisa Palavra: ");
		palavra = scan.next();
		
		palavra = palavra.toUpperCase().replaceAll("[^A-Z]", "");
		
		System.out.print( "Codigo Soundex da palavra ".concat(palavra).concat(" é: ") );
		System.out.println( Soundex.soundex( palavra.toLowerCase()));

		Set<String> lista = new HashSet<String>();
		
		try {

			FileReader arq = new FileReader(nome);
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine();
			
			// inicio contador de tempo
			long inicio = System.currentTimeMillis();
			
			System.out.print("Pesquisando... ");
			System.out.println(palavra);
			
			while (linha != null) 
			{
				StringTokenizer tokenizer = new StringTokenizer(linha);
				while (tokenizer.hasMoreTokens()) 
				{
					lista.add(retiraAcentos(tokenizer.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
				}
				//System.out.printf("%s\n", linha);

				if (lista.contains(palavra)) 
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
				// lê da segunda até a última linha
			}
			
			//fim contador de tempo
			long fim = System.currentTimeMillis();

			if (! achou) 
			{
				for (String s : lista) 
				{
				    //System.out.println(checkSimilarity(s,palavra));
				    //System.out.println(Float.compare(checkSimilarity(s,palavra), 0.8f));
					if (soundex(palavra) == (soundex(s)))
					{
						System.out.println(s.toString());
				    }
				}
			}

			System.out.println("Tempo da pesquisa foi de: ");
			System.out.println("0." + (fim-inicio) + " segundos");

			arq.close();

		} 
		catch (FileNotFoundException e)
		{
			System.out.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
			e.printStackTrace();
		}

		//System.out.println();
	}}
