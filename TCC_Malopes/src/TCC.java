
import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class TCC {

	// Funcao analisa tamanho Similaridade
	protected static float checkTamSimilaridade(String sString1, String sString2) throws Exception {

		if (sString1.length() != sString2.length())
			throw new Exception("Strings devem ter o mesmo tamanho!");

		int iLen = sString1.length();

		int iDiffs = 0;

		// Conta as diferenças entre as strings
		for (int i = 0; i < iLen; i++)
			if (sString1.charAt(i) != sString2.charAt(i))
				iDiffs++;

		// Calcula o percentual entre 0 e 1,
		// sendo 0 completamente diferente e
		// 1 completamente igual
		return 1f - (float) iDiffs / iLen;
	}

	// Funcao Check similaridade
	protected static float checkSimilaridade(String sString1, String sString2) throws Exception {

		// Se as strings têm tamanho distinto, obtêm a similaridade de todas as
		// combinações em que tantos caracteres quanto a diferença entre elas
		// são inseridos na string de menor tamanho.
		// Retorna a similaridade máxima entre todas as combinações,
		// descontando um percentual que representa
		// a diferença em número de caracteres.

		if (sString1.length() != sString2.length()) {
			int iDiff = Math.abs(sString1.length() - sString2.length());
			int iLen = Math.max(sString1.length(), sString2.length());
			String sBigger, sSmaller, sAux;

			if (iLen == sString1.length()) {
				sBigger = sString1.toUpperCase();
				sSmaller = sString2.toUpperCase();
			} else {
				sBigger = sString2.toUpperCase();
				sSmaller = sString1.toUpperCase();
			}

			float fSim, fMaxSimilarity = Float.MIN_VALUE;
			for (int i = 0; i <= sSmaller.length(); i++) {
				sAux = sSmaller.substring(0, i) + sBigger.substring(i, i + iDiff) + sSmaller.substring(i);
				fSim = checkTamSimilaridade(sBigger, sAux);
				if (fSim > fMaxSimilarity)
					fMaxSimilarity = fSim;
			}
			return fMaxSimilarity - (1f * iDiff) / iLen;

			// Se as strings têm o mesmo tamanho, simplesmente compara
			// caracter a caracter.
			// A similaridade sera mostrada apos realizada a regra de pesquisa
			// e as diferenças em cada posição.

		} else
			return checkTamSimilaridade(sString1, sString2);
	}

	// Funcao para retirar ascentos e alguns casos especiais
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

	// Funcao de Conexao com Banco
	public static Connection getConnection() throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/tcc", "root", "");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// Funcao Testa Conexao com Banco
	public static Connection checkBanco(String user, String password, String url, Connection con) throws Exception {

		Class.forName("com.mysql.jdbc.Driver");

		user = "root";
		password = "";

		url = "jdbc:mysql://localhost:3306/tcc";
		con = DriverManager.getConnection(url, user, password);

		while (con != null) {
			Statement statement = con.createStatement();
			@SuppressWarnings("unused")
			ResultSet resultSet = statement.executeQuery("SELECT nome, livros FROM livro");

		}

		return con;
	}

	// Driver JDBC nome e URL Banco
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/tcc";

	// Validando Banco
	static final String USER = "root";
	static final String PASS = "";

	public static void main(String args[]) throws Exception {

		boolean achou = false;

		Class.forName("com.mysql.jdbc.Driver");

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		String palavra = "";

		System.out.println("Conectando com Banco...");
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

		System.out.println(checkBanco(USER, PASS, DB_URL, conn));
		
		System.out.println("Criando Declaraçoes...");
		Statement stmt = conn.createStatement();

		System.out.println("Pesquisa Simples ou com SIMILARIDADE");
		System.out.println("1 - Pesquisa Simples");
		System.out.println("2 - Pesquisa Similaridade");
		int op2;
		
		System.out.println("Qual tipo de pesquisa deseja fazer: ");
		op2 = scan.nextInt();
		
		// Pesquisa SIMPLES
		if (op2 == 1) {
			System.out.printf("Pesquisa Palavra: ");
			palavra = scan.next();

			// inicio contador de tempo
			long inicio = System.currentTimeMillis();

			System.out.print("Codigo Soundex da palavra ".concat(palavra).concat(" é: "));
			System.out.println(Soundex.soundex(palavra.toLowerCase()));

			String sql = "ResultSet rs = stm.executeQuery(SELECT * FROM livro WHERE (nome LIKE 'palavra' OR livros LIKE 'palavra'))";

			ResultSet rs = stmt.executeQuery(sql);

			Set<String> lista = new HashSet<String>();

			try {

				System.out.print("Pesquisando... ");
				System.out.println(palavra);

				// Fazendo Leitura do Banco
				String linha = null;

				while (rs.next()) {

					linha = rs.getString("nome");

					StringTokenizer tokenizer = new StringTokenizer(linha);
					StringTokenizer tokenizer2 = new StringTokenizer(rs.getString("livros"));

					while (tokenizer.hasMoreTokens()) {
						lista.add(tokenizer.nextToken().toUpperCase());
					}
					while (tokenizer2.hasMoreTokens()) {
						lista.add(tokenizer2.nextToken().toUpperCase());
					}
				}

				// System.out.printf("%s\n", linha);
				// System.out.println(checkSimilaridade(s,palavra));
				// System.out.println(Float.compare(checkSimilaridade(s,palavra),0.8f));

				// System.out.println((scan.next(palavra)));
				// linha = lerArq.readLine();
				// lê da segunda até a última linha

				if (lista.contains(palavra)) {

					// Achou
					// String result = rs.getString(1);
					// System.out.println(result);

					System.out.print(palavra);
					System.out.println(" encontrada em: ");
					// System.out.println(linha);
					// System.out.println(sql);
					System.out.println(linha);

					achou = true;
					// break;
				} else {
					achou = false;
					// nao achou
					System.out.println("Palavra EXATA nao Encontrada");
				}

				if (!achou) {
					for (String s : lista) {
						// int tam = palavra.length();
						int tam2 = s.length();
						// int menor ;
						if (tam2 > 0) {
							// if (tam < tam2) {
							// menor = tam;
							// } else {
							// menor = tam2;
							// }
							Statement stmt2 = conn.createStatement();
							String sql2 = "SELECT psqSimples ( '" + palavra + "', '" + s + "') AS simples;";

							// System.out.println(sql2);

							ResultSet rs2 = stmt2.executeQuery(sql2);
							while (rs2.next()) {
								Double resultado = rs2.getDouble(1);

								// System.out.println(resultado);
								Double divisao = (resultado / tam2);

								// System.out.println(divisao);
								if ((Double.compare(divisao, 0.3f) < 0)) {
									System.out.println("Palavra semelhante encontrada: " + s);
									achou = true;
									break;
								}
							}
							if (achou) {
								break;
							}
						}
						// if (Float.compare(checkSimilaridade(s, palavra), 0.6f) >0) 
						//{
						// }
					}
				}

				// fim contador de tempo
				long fim = System.currentTimeMillis();

				// Collections.sort(lista);
				// System.out.println(lista);

				System.out.println("Tempo da pesquisa foi de: ");
				System.out.println("0." + (fim - inicio) + " segundos");

				// arq.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (Exception e) {
					}
			}
			System.out.println();
		} 
		
		// Pesquisa Por SIMILARIDADE
		else if (op2 == 2) {
			System.out.printf("Pesquisa Palavra: ");
			palavra = scan.next();

			// inicio contador de tempo
			long inicio = System.currentTimeMillis();

			palavra = retiraAcentos(palavra.toUpperCase()).replaceAll("[^A-Z]", "");

			System.out.print("Codigo Soundex da palavra ".concat(palavra).concat(" é: "));
			System.out.println(Soundex.soundex(palavra.toLowerCase()));

			String sql = "SELECT nome, livros FROM livro;";

			ResultSet rs = stmt.executeQuery(sql);

			Set<String> lista = new HashSet<String>();

			try {

				System.out.print("Pesquisando... ");
				System.out.println(palavra);

				// Fazendo Leitura do Banco
				String linha = null;

				while (rs.next()) {

					linha = rs.getString("nome");

					StringTokenizer tokenizer = new StringTokenizer(linha);
					StringTokenizer tokenizer2 = new StringTokenizer(rs.getString("livros"));

					while (tokenizer.hasMoreTokens()) {
						lista.add(retiraAcentos(tokenizer.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
					}
					while (tokenizer2.hasMoreTokens()) {
						lista.add(retiraAcentos(tokenizer2.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
					}
				}

				if (lista.contains(palavra)) {

					// Achou
					System.out.print(palavra);
					System.out.println(" encontrada em: ");
					System.out.println(linha);

					achou = true;
					
				} else {
					
					// nao achou
					achou = false;
					System.out.println("Palavra EXATA nao Encontrada");
					System.out.println("Pesquisando por similaridade...");
					
				}

				if (!achou) {
					for (String s : lista) {
						int tam2 = s.length();
						if (tam2 > 0) {
							Statement stmt2 = conn.createStatement();
							String sql2 = "SELECT levenshtein( '" + palavra + "', '" + s + "') AS leven;";
							ResultSet rs2 = stmt2.executeQuery(sql2);
							while (rs2.next()) {
								Double resultado = rs2.getDouble(1);

								Double divisao = (resultado / tam2);

								if ((Double.compare(divisao, 0.3f) < 0)) {
									System.out.println("Palavra semelhante encontrada: " + s);
									achou = true;
									break;
								}
							}
							if (achou) {
								break;
							}
						}
					}
				}

				// fim contador de tempo
				long fim = System.currentTimeMillis();

				System.out.println("Tempo da pesquisa foi de: ");
				System.out.println("0." + (fim - inicio) + " segundos");

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (Exception e) {
					}
			}
			System.out.println();
		} else {
			System.out.println("Informe Opcao Correta !!!");
			return;
		}
	}
}