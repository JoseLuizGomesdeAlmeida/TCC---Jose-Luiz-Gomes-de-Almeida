
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class MeuTCC {

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

		System.out.println("Criando Declaraçoes...");
		Statement stmt = conn.createStatement();

		System.out.println("Pesquisa SIMPLES ou com SIMILARIDADE");
		System.out.println("1 - Pesquisa Simples");
		System.out.println("2 - Pesquisa Similaridade");

		int op2;

		System.out.printf("Qual tipo de pesquisa deseja fazer: ");
		op2 = scan.nextInt();

		// Pesquisa SIMPLES
		if (op2 == 1) {
			try {
				System.out.printf("Informe a Palavra (1): ");
				palavra = scan.next();

				// Iniciando contador de tempo
				long inicio = System.currentTimeMillis();

				palavra = palavra.toUpperCase();

				System.out.print("Codigo Soundex da palavra ".concat(palavra).concat(" é: "));
				System.out.println(Soundex.soundex(palavra.toLowerCase()));

				String SQL = "{CALL pesqSimp (?)}";
				CallableStatement cStmt = null;
				cStmt = conn.prepareCall(SQL);

				ResultSet rs = null;

				cStmt.setString(1, palavra);
				rs = cStmt.executeQuery();

				while (rs.next()) 
				{

					if (rs.getString(1).contains(palavra)) {
						System.out.println("Palavra encontrada na Coluna: NOME (" + rs.getString(1) + ")");
					}
					if (rs.getString(2).contains(palavra)) {
						System.out.println("Palavra encontrada na Coluna: ENDERECO (" + rs.getString(2) + ")");
					}
					if (rs.getString(3).contains(palavra)) {
						System.out.println("Palavra encontrada na Coluna: CIDADE (" + rs.getString(3) + ")");
					}

				}

				// Fim do contador de tempo
				long fim = System.currentTimeMillis();

				System.out.printf("Tempo da pesquisa foi de: ");
				System.out.println("0.0" + (fim - inicio) + " segundos");

				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				System.out.println("Finally !!!");
			}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		// Pesquisa Por SIMILARIDADE
		else if (op2 == 2)

		{
			System.out.printf("Pesquisa Palavra: ");
			palavra = scan.next();

			// Iniciando contador de tempo
			long inicio = System.currentTimeMillis();

			palavra = retiraAcentos(palavra.toUpperCase()).replaceAll("[^A-Z]", "");

			System.out.print("Codigo Soundex da palavra ".concat(palavra).concat(" é: "));
			System.out.println(Soundex.soundex(palavra.toLowerCase()));

			//String sql = "SELECT nome, livros FROM livro;";
			String sql = "SELECT nome, endereco, cidade FROM celista;";

			ResultSet rs = stmt.executeQuery(sql);

			Set<String> lista = new HashSet<String>();

			try {

				System.out.println("Carregando Tabelas... ");

				// Fazendo Leitura do Banco
				while (rs.next()) {

					if (palavra.equals(rs.getString("nome"))) {
						System.out.println("Palavra " + palavra + " encontrada na coluna NOME" );
					} else if (rs.getString("nome").length() > palavra.length()) {
						if (rs.getString("nome").substring(0, palavra.length()).equals(palavra)) {
							System.out.println("Palavra semelhante encontrada na coluna NOME: " + rs.getString("nome"));
						}
					}

					if (palavra.equals(rs.getString("endereco"))) {
						System.out.println("Palavra " + palavra + " encontrada na coluna ENDERECO" );
					} else if (rs.getString("endereco").length() > palavra.length()) {
						if (rs.getString("endereco").substring(0, palavra.length()).equals(palavra)) {
							System.out.println("Palavra semelhante encontrada na coluna ENDERECO: " + rs.getString("endereco"));
						}
					}

					if (palavra.equals(rs.getString("cidade"))) {
						System.out.println("Palavra " + palavra + " encontrada na coluna CIDADE" );
					} else if (rs.getString("cidade").length() > palavra.length()) {
						if (rs.getString("cidade").substring(0, palavra.length()).equals(palavra)) {
							System.out.println("Palavra semelhante encontrada na coluna CIDADE: " + rs.getString("cidade"));
						}
					}

					StringTokenizer tokenizer = new StringTokenizer(rs.getString("nome"));
					StringTokenizer tokenizer2 = new StringTokenizer(rs.getString("endereco"));
					StringTokenizer tokenizer3 = new StringTokenizer(rs.getString("cidade"));

					while (tokenizer.hasMoreTokens()) {
						lista.add(retiraAcentos(tokenizer.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
					}
					while (tokenizer2.hasMoreTokens()) {
						lista.add(retiraAcentos(tokenizer2.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
					}
					while (tokenizer3.hasMoreTokens()) {
						lista.add(retiraAcentos(tokenizer3.nextToken().toUpperCase()).replaceAll("[^A-Z]", ""));
					}
				}
				
				System.out.println("Pesquisando... ");

				if (!achou) {
					Statement stmt2 = conn.createStatement();
					Double resultado;
					Double divisao;
					ResultSet rs2;
					String sql2;
					int tam2;
					for (String s : lista) {
						tam2 = s.length();
						if (tam2 > 0) {
							sql2 = "SELECT levenshtein( '" + palavra + "', '" + s + "') AS leven;";
							rs2 = stmt2.executeQuery(sql2);
							while (rs2.next()) {
								resultado = rs2.getDouble(1);

								divisao = (resultado / tam2);

								if ((Double.compare(divisao, 0.3f) < 0)) {
									System.out.println("Palavra encontrada por similaridade: " + s);
									achou = true;
								}
							}
						}
					}
				}

				// Fim contador de tempo
				long fim = System.currentTimeMillis();

				System.out.printf("Tempo da pesquisa foi de: ");
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
			System.out.println("OK");

		} else {
			System.out.println("Informe Opção Correta !!!");
			return;
		}
	}
}