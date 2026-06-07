package vendas2.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vendas2.exception.RegraNegocioException;
import vendas2.modelo.ItemPedido;

@Repository
public class ItemPedidoDAO {
	
	@Autowired
	private EntityManager em;
	
	/**
	 * Insere um item de pedido no banco de dados 
	 * @param Object ItemPedido
	 * @return identificador do registro
	 * @code util
	 */
	@Transactional
	public Integer inserirItemPedido (ItemPedido itempedido) {
		try{
			em.persist(itempedido);
			return itempedido.getId();
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Insere lista de itens de pedido no banco de dados 
	 * @param lista ItemPedido
	 * @return quantidade de itens inseridos na base
	 * @code util
	 */
	@Transactional
	public Integer inserirListaItemPedido (List<ItemPedido> lip) {
		try{
			Integer qtd = 0;
			for(ItemPedido ip : lip) {
				em.persist(ip);
				qtd += 1;
			}
			return qtd;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Atualiza registro de um item de pedido na base
	 * @param Object ItemPedido
	 * @return Object ItemPedido
	 * @code util
	 */
	@Transactional
	public ItemPedido atualizarItemPedido (ItemPedido itempedido) {
		try{
			em.merge(itempedido);
			return itempedido;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Atualiza lista de itens de um pedido na base
	 * @param lista de itens do pedido
	 * @return quantidade de registros atualizados
	 * @code util
	 */
	@Transactional
	public Integer atualizarListaItemPedido (List<ItemPedido> lip) {
		try{
			Integer qtd = 0;
			for(ItemPedido ip : lip){
				em.merge(ip);
				qtd += 1;
			}
			return qtd;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Excluir um item de pedido cadastrado
	 * @param Object ItemPedido
	 * @return Object ItemPedido
	 * @code util
	 */
	@Transactional
	public ItemPedido removerItemPedido (ItemPedido itempedido) {
		try{
			if (!em.contains(itempedido)){
				em.merge(itempedido);
			}
			em.remove(itempedido);
			return itempedido;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Excluir lista itens de pedido cadastrado
	 * @param lista de ItemPedido
	 * @return quantidade de itens excluidos
	 * @code util
	 */
	@Transactional
	public Integer removerListaItemPedido (List<ItemPedido> lip) {
		try{
			Integer qtd = 0;
			for(ItemPedido ip : lip) {
				if (!em.contains(ip)){
					em.merge(ip);
				}
				em.remove(ip);
				qtd += 1;
			}
			return qtd;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Exclui todos os registros da tabela itempedido
	 * @return int total de registros excluidos
	 * @code util
	 */
	@Transactional
	public int excluirTodosItemPedido() {
		try{
			List<ItemPedido> l=obtemTodosItemPedido();
			for(ItemPedido c:l) {
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
	 * Obtem item de pedido pelo seu identificador
	 * @param identificador do item de pedido
	 * @return ItemPedido
	 * @code util
	 */
	public ItemPedido obtemItemPedidoById (Integer id) {
		try{
			ItemPedido ip = em.find(ItemPedido.class, id);
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}
	
	/**
	 * Consulta todos os registros cadastrados
	 * @return lista de itempedido
	 * @code util
	 */
	public List<ItemPedido> obtemTodosItemPedido() {
		try{
			String hql = "select c from ItemPedido c order by c.id desc";
			List<ItemPedido> resultList = em.createQuery(hql, ItemPedido.class).getResultList();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
			throw new RegraNegocioException(e.getMessage(), e);
		}
	}

}
