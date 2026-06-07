package vendas2.bdrestore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Service;

/**
 * O codigo abaixo serve apenas como um modelo para restaurar um database e suas tabelas.
 * Ainda é necessario adequações ao código para corrigir o owner tanto do database quanto das tabelas 
 * assim como a estrutura das tabelas e os registros que serão inseridos como base inicial.
 * De acordo com novos conhecimentos não será utilizado o entitymanager para executar as rotinas no banco.
 * 
 * Uma vez optado por desenvolver este código com sucesso ele deve ser aplicado no projeto webmarket.
 * 
 * O próximo estágio do projeto webmarket é a implantação da camada de segurança.
 * 
 * @author jomar
 *
 */
@Service
public class RestauraDatabase {

	final private String driver = "org.postgresql.Driver";
	final private String url = "jdbc:postgresql://localhost:5432/bootexpert";
	final private String usuario = "postgres";
	final private String senha = "root";
	private Connection conexao;
	public Statement s;
	public ResultSet r;

	public boolean conecta() {
		boolean result = true;
		try {
			Class.forName(driver);
			conexao = DriverManager.getConnection(url, usuario, senha);
//			JOptionPane.showMessageDialog(null, "Conectou com o banco de dados");
		} catch (ClassNotFoundException Driver) {
			JOptionPane.showMessageDialog(null, "Driver nao localizado: " + Driver);
			result = false;
		} catch (SQLException Fonte) {
//			JOptionPane.showMessageDialog(null, "Deu erro na conexão com a fonte de dados"+Fonte);
			result = false;
			String conecao = "Dejesa criar as tabelas do banco de dados?";
			int opcao_escolhida = JOptionPane.showConfirmDialog(null, conecao, " ", JOptionPane.YES_NO_OPTION);
			if (opcao_escolhida == JOptionPane.YES_OPTION) {
				String sqlcreate = "CREATE DATABASE bdexpert2";
				String sqluse = "USE DBAllure";
				String sqlagenda = "CREATE TABLE agenda ( idagenda integer NOT NULL, "
						+ "idcliente integer NOT NULL, agenome character varying(15) NOT NULL, "
						+ "agesobrenome character varying(30) NOT NULL, agedata date NOT NULL, "
						+ "agehora time without time zone NOT NULL, "
						+ "CONSTRAINT idagenda_pk PRIMARY KEY (idagenda ))";
				String sqlclientes = "CREATE TABLE clientes ( idcliente integer NOT NULL, "
						+ "clinome character varying(15) NOT NULL, clisobrenome character varying(30) NOT NULL, "
						+ "clidatanasc date NOT NULL, clirg character varying(20) NOT NULL, "
						+ "cliuf character varying(2) NOT NULL, clicep character varying(10), "
						+ "clicidade character varying(40), clibairro character varying(40), "
						+ "clirua character varying(40), clinumero integer, clicomplemento character varying(40), "
						+ "clitelres character varying(14), clitelcel character varying(14), clitelcom character varying(14), "
						+ "cliplanodesaude character varying(40) NOT NULL, cliemail character varying(40), "
						+ "cliobservacao character varying(400), CONSTRAINT clientes_pkey PRIMARY KEY (idcliente ))";
				String sqlconsultas = "CREATE TABLE consultas ( idconsulta integer NOT NULL, "
						+ "idcliente integer NOT NULL, condata date NOT NULL, "
						+ "conhora time without time zone NOT NULL, conobservacao character varying(400), "
						+ "CONSTRAINT consultas_pkey PRIMARY KEY (idconsulta ))";
				String sqllogin = "CREATE TABLE login ( idlogin integer NOT NULL, "
						+ "logaccount character varying(20) NOT NULL, logpassword character varying(20) NOT NULL, "
						+ "acesso integer, CONSTRAINT login_pkey PRIMARY KEY (idlogin ))";
				String sqlplanos = "CREATE TABLE planos ( idplano integer NOT NULL, "
						+ "nomeplano character varying(30) NOT NULL, CONSTRAINT pk_planos PRIMARY KEY (idplano ))";
				String sqlresponsavel = "CREATE TABLE responsavel ( idresponsavel integer NOT NULL, "
						+ "idcliente integer NOT NULL, respnome character varying(40) NOT NULL, "
						+ "respsobrenome character varying(40) NOT NULL, respdatanasc date NOT NULL, "
						+ "resprg character varying(20) NOT NULL, respuf character varying(2) NOT NULL, "
						+ "respcep character varying(10), respcidade character varying(40), "
						+ "respbairro character varying(40), resprua character varying(40), respnumero integer, "
						+ "respcomplemento character varying(40), resptelres character varying(14), "
						+ "resptelcel character varying(14), resptelcom character varying(14), "
						+ "respemail character varying(40), respobservacao character varying(400), "
						+ "CONSTRAINT responsavel_pkey PRIMARY KEY (idresponsavel ))";
				try {
					JOptionPane.showMessageDialog(null, "Chegou até aqui");
					executeSQL(sqlcreate);
					s.execute(sqluse);
					s.execute(sqlagenda);
					s.execute(sqlclientes);
					s.execute(sqlconsultas);
					s.execute(sqllogin);
					s.execute(sqlplanos);
					s.execute(sqlresponsavel);
					JOptionPane.showMessageDialog(null, "Chegou até aqui também");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public void desconecta() {
		try {
			conexao.close();
//			JOptionPane.showMessageDialog(null, "Banco Fechado");
		} catch (SQLException Fonte) {
			JOptionPane.showMessageDialog(null, "Não foi possivel fechar o banco de dados");
		}
	}

	public void executeSQL(String sql) {
		try {
			s = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			r = s.executeQuery(sql);
		} catch (SQLException sqlex) {
			JOptionPane.showMessageDialog(null, "Não foi possivel " + "executar o comando SQL" + sqlex);
		}
	}
}
