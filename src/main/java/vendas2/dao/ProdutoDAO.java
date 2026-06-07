package vendas2.dao;

import java.text.Normalizer;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vendas2.exception.RegraNegocioException;
import vendas2.modelo.ItemPedido;
import vendas2.modelo.Produto;

@Repository
public class ProdutoDAO {
	
	@Autowired
	private EntityManager em;
	
	/**
	 * Substitui letras acentuadas para padrao ascii
	 * @param texto
	 * @return texto
	 */
	private String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
	
	/**
	 * Persiste produto no banco de dados
	 * @param Object Produto
	 * @return identificador do registro inserido na base
	 */
	@Transactional
	public Integer inserirProduto (Produto produto) {
		try{
			produto.setDescricao(deAccent(produto.getDescricao()));
			em.persist(produto);
			return produto.getId();
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
		
	}
	
	/**
	 * Persiste um lote de produtos na base
	 * @param lista de Produto
	 * @return quantidade de registros inseridos na base
	 */
	@Transactional
	public Integer inserirLoteProduto (List<Produto> produto) {
		try{
			Integer qtd = 0;
			for(Produto c: produto) {
				c.setDescricao(deAccent(c.getDescricao()));
				em.persist(c);
				qtd += 1;
			}
			return qtd;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Atualiza registro de um produto na base
	 * @param Object produto
	 * @return Object produto
	 */
	@Transactional
	public Produto atualizar (Produto produto) {
		try{
			produto.setDescricao(deAccent(produto.getDescricao()));
			if(!em.contains(produto)) {
				System.out.println("EntityManager nao contem o produto");
				produto = em.merge(produto);
				
			}
			return produto;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Atualiza lote de produtos na base
	 * @param lista de produto
	 * @return quantidade de registros atualizados
	 */
	@Transactional
	public Integer atualizarLoteProduto (List<Produto> produto) {
		try{
			Integer qtd = 0;
			for(Produto c : produto) {
				c.setDescricao(deAccent(c.getDescricao()));
				em.merge(c);
				qtd += 1;
			}
			return qtd;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Remove um produto da base
	 * @param Object produto
	 * @return Object produto
	 */
	@Transactional
	public Produto remover (Produto produto) {
		try{
			if (!em.contains(produto)){
				produto = em.merge(produto);
			}
			em.remove(produto);
			return produto;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}

	/**
	 * Remove um produto da base pelo identificador
	 * @param identificador do produto
	 * @return Object produto
	 */
	public Produto remover (Integer id) {
		try{
			Produto produto = em.find(Produto.class, id);
			//chama remover
			remover(produto);
			return produto;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Remove lote de produtos da base
	 * @param lista de produto
	 * @return quantidade de registros removidos da base
	 */
	public Integer removerLoteProduto (List<Produto> produto) {
		try{
			Integer qtd = 0;
			for(Produto c : produto) {
				remover(c);
				qtd += 1;
			}
			return qtd;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Consulta todos os produtos onde campo "descricao" contenha o 
	 * texto case insensitive passado como parametro
	 * @param texto 
	 * @return lista de produtos
	 */
	public List<Produto> buscarPorDescricaoLike (String nome) {
		try{
			String hql = "select c from Produto c where lower(c.descricao) like lower(concat('%', :umnome, '%')) ";
			TypedQuery<Produto> query = em.createQuery(hql, Produto.class);
			query.setParameter("umnome", nome);
			List<Produto> qry = query.getResultList();
			return qry;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Busca produto pelo identificador
	 * @param id
	 * @return Object Produto ou null
	 */
	public Produto buscarPorId (Integer id) {
		try{
			Produto lp = em.find(Produto.class, id);
			if (lp != null) {
				return lp;
			} else {
				return null;
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Obter todos os registros da tabela de produtos
	 * @return lista de produtos
	 */
	public List<Produto> obterTodos() {
		try{
			String hql = "select c from Produto c";
			List<Produto> qry = em.createQuery(hql, Produto.class).getResultList();
			return qry;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Exclui todos os registros da tabela produto
	 * @return int total de registros excluidos
	 * @code util
	 */
	@Transactional
	public int excluirTodosProduto() {
		try{
			List<Produto> l=obterTodos();
			for(Produto c:l) {
				if (!em.contains(c)){
					em.merge(c);
				}
				em.remove(c);
			}
			return l.size();
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}

	
	/**
	 * Consulta produtos que nao tem pedido
	 * @return lista de produtos
	 */
	public List<Produto> buscaProdutosSemPedido() {
		try{
			String hql = "select distinct c from Produto c where c.id not in "
					+ "(select distinct d.produto from ItemPedido d) order by c.id asc";
			List<Produto> resultList = em.createQuery(hql, Produto.class).getResultList();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
		
	}
	
	
	/**
	 * Consulta produtos que tem pedido
	 * @return lista de produtos
	 */
	public List<Produto> buscaProdutosComPedido() {
		try{
			String hql = "select c from Produto c where c.id in "
					+ "(select distinct d.produto from ItemPedido d) order by c.id asc";
			List<Produto> resultList = em.createQuery(hql, Produto.class).getResultList();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Consulta produtos que nao tem pedido e que o campo descricao contenha o 
	 * texto case insensitive passado como parametro
	 * @param texto
	 * @return lista produtos
	 */
	public List<Produto> buscaProdutosSemPedidoDescricaoLike(String texto) {
		try{
			String hql = "select p from Produto p where p.id not in "
					+ "(select distinct d.produto from ItemPedido d) and "
					+ "lower(p.descricao) like lower(concat('%', :nameToFind,'%')) order by p.id asc";
			List<Produto> resultList = em.createQuery(hql, Produto.class).getResultList();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Consulta produtos que nao tem pedido e que o campo descricao contenha o 
	 * texto case insensitive passado como parametro
	 * @param texto
	 * @return lista produtos
	 */
	public Boolean existePedidoQueTenhaProduto(Produto produto) {
		try{
			String hql = "select p from ItemPedido p where p.produto = :prod";
			TypedQuery<ItemPedido> query = em.createQuery(hql, ItemPedido.class);
			query.setParameter("prod", produto);
			List<ItemPedido> resultList = query.getResultList();
			if(resultList == null) {
				System.out.println("busca pelo pedido retornou nulo");
			}
			if(resultList.size() > 0) {
				return true;
			} else {
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Consulta lote de produtos
	 * @param lista de inteiros
	 * @return lista de produtos
	 */
	public List<Produto> buscaLoteProdutos(List<Integer> intprod) {
		try{
			String lst="";
			Iterator<Integer> it = intprod.iterator();
			while (it.hasNext()) {
				lst += it.next();
				if(it.hasNext()) {
					lst += ", ";
				}
			}
			String hql = "select distinct p from Produto p where p.id in (" + lst + ") order by p.id asc";
			List<Produto> resultList = em.createQuery(hql, Produto.class).getResultList();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
}
