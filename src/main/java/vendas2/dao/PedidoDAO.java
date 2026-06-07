package vendas2.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vendas2.exception.RegraNegocioException;
import vendas2.modelo.Pedido;

@Repository
public class PedidoDAO {
	
	@Autowired
	private EntityManager em;
	
	/**
	 * Insere Pedido no banco de dados 
	 * @param Object Pedido
	 * @return Object Pedido
	 * @code util
	 */
	@Transactional
	public Pedido inserirPedido (Pedido pedido) {
		try{
			em.persist(pedido);
			return pedido;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Atualiza registro de um Pedido na base
	 * @param Object Pedido
	 * @return Object Pedido
	 * @code util
	 */
	@Transactional
	public Pedido atualizarPedidoByPedido (Pedido pedido) {
		try{
			em.merge(pedido);
			return pedido;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Exclui pedido
	 * @param Object Pedido
	 * @return Object Pedido
	 * @code util
	 */
	@Transactional
	public Pedido removerByPedido (Pedido pedido) {
		try{
			if (!em.contains(pedido)){
				em.merge(pedido);
			}
			em.remove(pedido);
			return pedido;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Exclui pedido
	 * @param numero do pedido
	 * @return Object Pedido
	 * @code util
	 */
	@Transactional
	public Pedido removerByPedidoId (Integer id) {
		try{
			Pedido p = em.find(Pedido.class, id);
			if (!em.contains(p)) {
				em.merge(p);
			}
			em.remove(p);
			return p;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}

	/**
	 * Obtem um pedido 
	 * @param numero do pedido
	 * @return Object Pedido
	 * @code
	 */
	public Pedido obtemPedidoByPedidoId (Integer id) {
		try{
			Pedido p = em.find(Pedido.class, id);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Exclui todos os registros da tabela pedido
	 * @return int total de registros excluidos
	 * @code util
	 */
	@Transactional
	public int excluirTodosPedidos() {
		try{
			List<Pedido> l=obtemTodosPedidos();
			for(Pedido c:l) {
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
	 * Consulta todos os registros cadastrados
	 * @return lista de pedido
	 * @code util
	 */
	public List<Pedido> obtemTodosPedidos() {
		try{
			String hql = "select c from Pedido c order by c.id desc";
			List<Pedido> resultList = em.createQuery(hql, Pedido.class).getResultList();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}

}
