package vendas2.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vendas2.exception.RegraNegocioException;
import vendas2.modelo.Cliente;
import vendas2.modelo.StatusPedidoEnum;
import vendas2.modelo.ItemPedido;
import vendas2.modelo.Pedido;
import vendas2.modelo.Produto;
import vendas2.modelo.dto.ItemPScreenDTO;
import vendas2.modelo.dto.ItemPedidoDTO;
import vendas2.modelo.dto.PScreenDTO;
import vendas2.modelo.dto.PedidoDTO;

/**
 * Classe para manipulacao das tabelas: Pedido e ItemPedido
 * @author jomar
 * @since criado 08/12/2021
 */
@Repository
public class PedidoDtoDAO {

	@Autowired
	private ClienteDAO clientedao;
	@Autowired
	private ProdutoDAO produtosdao;
	@Autowired
	private PedidoDAO pedidodao;
	@Autowired
	private ItemPedidoDAO itempedidodao;
	@Autowired
	private EntityManager em;
	
	/**
	 * Insere pedido do cliente na base. O pedido sera apresentado no formato pedidodto:
	 *      {
	 *           "idcliente" : inteiro,
	 *           "status" : string,
	 *           "itens" : [ {
	 *                              "idproduto" : inteiro,
	 *                              "quantidade" : inteiro
	 *                          } ]
	 *       }
	 * @param pedidodto
	 * @return numero do pedido
	 * @code util
	 */
	@Transactional
	public Integer inserirPedidoDTO(PedidoDTO dto) {
		try{
//			define variaveis
			ArrayList<ItemPedido> arrayitempedido =  new ArrayList<ItemPedido>();
			ItemPedido itempedido =  null;
			BigDecimal valortotal = BigDecimal.ZERO;
			BigDecimal parcial = BigDecimal.ZERO;
		
//			Cria o pedido, salva e obtem seu numero
			Pedido pedido = new Pedido();
			System.out.println("Buscando o cliente para o pedido");
			Cliente cli = clientedao.obtemClienteByClienteId(dto.getClienteid());
			if(cli != null) {
				System.out.println("Cliente encontrado. Montando pedido.");
				pedido.setCliente(cli);
				pedido.setDatapedido(LocalDate.now());
				pedido.setStatus(StatusPedidoEnum.PEDIDO);
				pedido.setTotal(valortotal);
			}else{
				System.out.println("Nao e possivel gerar pedido sem um cliente valido");
				return null;
			}
			
			System.out.println("obtendo lista de itens do dto");
			List<ItemPedidoDTO> arrayitempedidodto = dto.getItens();
			if(arrayitempedidodto.size() > 0) {
				System.out.println("lista de itens dto tem " + arrayitempedidodto.size() + " produtos");
				System.out.println("Buscando cada produto para obter seu preço");
				for(ItemPedidoDTO x : arrayitempedidodto) {
//					Busca cada produto para obter seu preço
					System.out.println("Buscando produto: " + x.getProdutoid());
					Produto prod = produtosdao.buscarPorId(x.getProdutoid());
					if(prod != null) {
//						calculando total pelo preço cadastrado para o produto
						parcial = prod.getPreco();
						parcial = parcial.multiply(new BigDecimal(x.getQuantidade()));
						valortotal = valortotal.add(parcial);
//						Constroi um array com os itens do pedido e aguarda a geracao do identificador do pedido
						itempedido = new ItemPedido();
						itempedido.setProduto(prod);
						itempedido.setQuantidade(x.getQuantidade());
						arrayitempedido.add(itempedido);
					}else {
						System.out.println("Produto " + x.getProdutoid() + " nao encontrado");
						return null;
					}
				}
			}else{
				System.out.println("DTO nao contem itens para realizar um pedido.");
				return null;
			}
			
//			salva obtem o numero do pedido
			pedido = pedidodao.inserirPedido(pedido);
			
			System.out.println("Vinculando o pedido aos itens e gravando");
			for(ItemPedido x : arrayitempedido) {
//				Insere a informacao do pedido e grava o item do pedido
				x.setPedido(pedido);
			}
			itempedidodao.inserirListaItemPedido(arrayitempedido);
			System.out.println("Pedido num: " + pedido.getId() + " salvo.");
			return pedido.getId();
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}


	/**
	 * lista todos os pedidos de um cliente
	 * @param Object cliente
	 * @return lista PPScreenDTO
	 * @code util
	 */
	public List<PScreenDTO> listarPedidosByCliente(Cliente cliente) {
		try {
			System.out.println("consulta os pedidos do cliente");
			List<Pedido> listapedidocliente = obtemPedidosByCliente(cliente);
			System.out.println("encontrados " + listapedidocliente.size() + " pedidos para o cliente: " + cliente.getNome());
			if(listapedidocliente.size() > 0) {
				List<PScreenDTO> listaPScreenDTO = converteListaPedidoEmListaPScreenDTO(listapedidocliente);
				return listaPScreenDTO;
			} else {
				System.out.println("cliente nao possui pedidos");
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}

	/**
	 * lista todos os pedidos de um cliente
	 * @param numero do cliente
	 * @return lista PPScreenDTO
	 * @code util
	 */
	public List<PScreenDTO> listarPedidosByClienteId(Integer idcli) {
		try {
			System.out.println("consulta os pedidos do cliente");
			List<Pedido>lp = obtemPedidosByClienteId(idcli);
			List<PScreenDTO> lps = converteListaPedidoEmListaPScreenDTO(lp);
			return lps;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}
	
	
	/**
	 * lista todos os pedidos cadastrados para um cpf 
	 * @param cpf do cliente
	 * @return lista PPScreenDTO
	 * @code util
	 */
	public List<PScreenDTO> listarPedidosByClienteCPF(String cpf) {
		try{
			String hql = "select p from Pedido p inner join Cliente c on p.cliente = c.id where c.cpf = :cpf";
			TypedQuery<Pedido> query = em.createQuery(hql, Pedido.class);
			query.setParameter("cpf", cpf);
			List<Pedido> lppsdto = query.getResultList();

			if(lppsdto.size() > 0) {
				List<PScreenDTO> psdto = converteListaPedidoEmListaPScreenDTO(lppsdto);
				return psdto;
			}else{
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}

	/**
	 * lista pedido pelo seu identificador
	 * @param inteiro identificador
	 * @return pedidoDTO
	 * @code util
	 */
	public PScreenDTO listarPedidoByPedidoId(Integer pedid) {
		try{
			System.out.println("busca o pedido");
			Pedido pedido = obtemPedidoByPedidoId(pedid);
			if(pedido !=null) {
				PScreenDTO psdto = convertePedidoEmPScreenDTO(pedido);
				return psdto;
			}else{
				System.out.println("nenhum pedido encontrado");
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}
	
	/**
	 * Busca os 10 pedidos mais recentes contendo um produto informado para pesquisa. 
	 * @param inteiro identificador do produto
	 * @return Lista PPScreenDTO
	 * @code util
	 */
	public List<PScreenDTO> listarTop10PedidosQueContenhamUmCertoProduto(Integer proId) {
		try{
//			seleciona os 10 pedidos mais recentes
			String hql = "select pe from Pedido pe inner join ItemPedido ip on pe.id = ip.pedido "
			+ "inner join Produto pr on ip.produto = pr.id where pr.id = :id "
			+ "order by pe.datapedido desc";
			TypedQuery<Pedido> queryP = em.createQuery(hql, Pedido.class);
			queryP.setMaxResults(10);
			queryP.setParameter("id", proId);
			List<Pedido> ListPedido = queryP.getResultList();
			System.out.println("qtd pedidos encontrados: " + ListPedido.size());

			if(ListPedido.size() > 0) {
				List<PScreenDTO> lpsdto = converteListaPedidoEmListaPScreenDTO(ListPedido);
				return lpsdto;
			}else{
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}
	
	/**
	 * Atualiza os itens e suas respectivas quantidades em um pedido
	 * @param identificador do pedido
	 * @param lista de itens com as quantidades 
	 * @return pscreendto atualizado
	 * @code util
	 */
	@Transactional
	public PScreenDTO atualizarItensDoPedido(Integer pedid, PedidoDTO pedidodto) {
		try{
//			busca o pedido no banco
			Pedido p = obtemPedidoByPedidoId(pedid);
			if(p != null) {
//				obtem os itens do pedido atual no banco
				List<ItemPedido> lip = new ArrayList<ItemPedido>();
				lip = p.getItens();
				
//				cria lista de novos itens para o pedido atual
				List<ItemPedido> newlip = new ArrayList<ItemPedido>();
//				converte cada item do pedidodto em um itempedido para salvar no banco 
				List<ItemPedidoDTO> lipdto = pedidodto.getItens();
				for(ItemPedidoDTO ipdto : lipdto) {
					if(ipdto.getProdutoid() == null) {
						throw new RegraNegocioException ("[atualizarItensDoPedido] Codigo do produto não informado.");
					}
					ItemPedido ip = new ItemPedido();
					ip.setPedido(p);
					Produto pr = produtosdao.buscarPorId(ipdto.getProdutoid());
					if(pr != null) {
						ip.setProduto(pr);
						ip.setQuantidade(ipdto.getQuantidade());
						newlip.add(ip);
					}else{
						System.out.println("[atualizarItensDoPedido] produto não localizado.");
						throw new RegraNegocioException ("[atualizarItensDoPedido] produto não localizado.");
					}	
				}
//				excluir todos os itens existentes no banco referente ao pedido
				for(ItemPedido c : lip) {
					if (!em.contains(c)){
						c = em.merge(c);
					}
					em.remove(c);
				}
				
//				inserir os novos itens do pedido no banco
				for(ItemPedido ip : newlip) {
					em.persist(ip);
				}
//				atualiza o pedido com os novos itens
				p.setItens(newlip);
//				converte o novo pedido em um pscreendto para retornar ao usuario
				PScreenDTO p3 = convertePedidoEmPScreenDTO(p);
				return p3;
			} else {
				System.out.println("[atualizarItensDoPedido] pedido nao localizado");
				throw new RegraNegocioException ("[atualizarItensDoPedido] pedido nao localizado");
			}
		}catch(Exception e) {
			System.out.println("[atualizarItensDoPedido] deu zebra no produto: nao localizado");
			e.printStackTrace();
			throw new RegraNegocioException ("[atualizarItensDoPedido] deu zebra no produto: nao localizado");
		}

	}
	
	/**
	 * Atualiza o status de um pedido pelo seu identificador
	 * @param identificador do pedido
	 * @return ppscreendto atualizado
	 * @code util
	 */
	@Transactional
	public PScreenDTO atualizarStatusPedido(Integer pedid, PedidoDTO pedidodto) {
		try{
//			busca o pedido no banco
			Pedido p = obtemPedidoByPedidoId(pedid);
			if(p != null) {
//				atualiza para novo status
				p.setStatus(pedidodto.getStatus());
//				salva o pedido atualizado
				em.persist(p);
				
//				converte um pedido em um ppscreendto para retornar ao usuario
				PScreenDTO p2 = convertePedidoEmPScreenDTO(p);
				return p2;
			}else{
				System.out.println("pedido nao localizado");
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
		 
	}
	
	/**
	 * Exclui um pedido com todos os seus itens da base
	 * @param Object pedido
	 * @return pscreendto
	 */
	@Transactional
	public PScreenDTO excluirPedidoByPedido(Pedido pedido) {
		try {
			Pedido p = em.find(Pedido.class, pedido);
			if(p != null) {
				String hql = "select ip from ItemPedido ip where ip.pedido = :ped";
				TypedQuery<ItemPedido> query = em.createQuery(hql, ItemPedido.class);
				query.setParameter("ped", p);
				List<ItemPedido> qry = query.getResultList();
				if(qry.size() > 0) {
					for(ItemPedido ip : qry) {
						if (!em.contains(ip)) {
							ip = em.merge(ip);
						}
						em.remove(ip);
					}
					em.remove(p);
					PScreenDTO ret = convertePedidoEmPScreenDTO(p);
					return ret;
				}else{
					System.out.println("nenhum item encontrado para o pedido. Pedido sendo removido");
					em.remove(p);
					PScreenDTO ret = convertePedidoEmPScreenDTO(p);
					return ret;
				}
			}else{
				System.out.println("Pedido nao localizado");
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}	
	}

	/**
	 * Obtem todos os pedidos de um cliente
	 * @param Object Cliente
	 * @return lista de pedido
	 * @code util
	 */
	private List<Pedido> obtemPedidosByCliente(Cliente cliente) {
		try{
			String hql = "select p from Pedido p where p.cliente = :cli";
			TypedQuery<Pedido> query = em.createQuery(hql, Pedido.class);
			query.setParameter("cli", cliente);
			List<Pedido> qry = query.getResultList();
			return qry;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}
	
	
	/**
	 * Obtem todos os pedidos pelo numero do cliente 
	 * @param numero do cliente
	 * @return lista de pedido
	 * @code util
	 */
	private List<Pedido> obtemPedidosByClienteId(Integer idcli) {
		try{
			Cliente cli = clientedao.obtemClienteByClienteId(idcli);
			List<Pedido> lp = obtemPedidosByCliente(cli);
			return lp;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}
	
	/**
	 * Obtem pedido pelo identificador
	 * @param identificador do cliente
	 * @return lista de pedido
	 * @code util
	 */
	private Pedido obtemPedidoByPedidoId(Integer idped) {
		try{
			Pedido qry = em.find(Pedido.class, idped);
			if(qry != null) {
				return qry;
			}else{
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}

	/**
	 * Exclui um pedido com todos os seus itens pelo seu identificador
	 * @param identificador do pedido
	 * @return pscreendto
	 * @code util
	 */
	@Transactional
	public PScreenDTO excluirPedidoByPedidoId(Integer pedid) {
		try {
			Pedido p = em.find(Pedido.class, pedid);		
			if(p != null) {
				System.out.println("pedido encontrado para exclusao");
				String hql = "select ip from ItemPedido ip where ip.pedido = :ip";
				TypedQuery<ItemPedido> query = em.createQuery(hql, ItemPedido.class);
				query.setParameter("ip", p);
				List<ItemPedido> lip = query.getResultList();
				if(lip.size() > 0) {
					System.out.println("removendo os itens do pedido");
					for(ItemPedido ip : lip) {
						if (!em.contains(ip)){
							ip = em.merge(ip);
						}
						em.remove(ip);
					}
					System.out.println("removendo o pedido");
					em.remove(p);
					
					PScreenDTO ret = convertePedidoEmPScreenDTO(p);
					return ret;
				}else{
					System.out.println("O pedido nao tem itens. Removendo o pedido");
					em.remove(p);
					PScreenDTO ret = convertePedidoEmPScreenDTO(p);
					return ret;
				}
			}else{
				System.out.println("Pedido nao localizado");
				return null;
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new RegraNegocioException (e.getMessage(), e);
		}
	}
	
	
	/**
	 * Converte lista de pedidos para o formato lista de ppscreendto 
	 * @param lista de pedidos
	 * @return lista de ppscreendto
	 * @code util
	 */
	private List<PScreenDTO> converteListaPedidoEmListaPScreenDTO(List<Pedido> listapedidocliente) {
		List<PScreenDTO> listapscreendto = new ArrayList<PScreenDTO>();
		for(Pedido p : listapedidocliente) {
			System.out.println("convertendo pedido: " + p.getId());
			PScreenDTO pscreendto = new PScreenDTO();
			pscreendto.setClienteid(p.getCliente().getId());
			pscreendto.setPedidoid(p.getId());
			pscreendto.setDatapedido(p.getDatapedido());
			pscreendto.setTotal(p.getTotal());
			pscreendto.setStatus(p.getStatus());
			
//			cria uma lista de ItemPScreenDTO para cada pscreendto
			List<ItemPScreenDTO> listaitempscreendto = new ArrayList<ItemPScreenDTO>();
//			converte cada itempedido em um itempscreendto
			List<ItemPedido> listaitempedidocliente = new ArrayList<ItemPedido>();
			listaitempedidocliente = p.getItens();
			for(ItemPedido ip : listaitempedidocliente) {
				ItemPScreenDTO itemppscreendto = new ItemPScreenDTO();
				itemppscreendto.setProdutoid(ip.getProduto().getId());
				itemppscreendto.setQuantidade(ip.getQuantidade());
				itemppscreendto.setDescricao(ip.getProduto().getDescricao());
				listaitempscreendto.add(itemppscreendto);
			}
//			adiciona a lista de itens do pedidodto a pscreendto
			pscreendto.setItens(listaitempscreendto);
//			adiciona o pscreendto a uma lista de pscreendto
			listapscreendto.add(pscreendto);
		}
		return listapscreendto;
	}
	
	/**
	 * Converte pedido para o formato ppscreendto 
	 * @param Object Pedido
	 * @return ppscreendto
	 * @code util
	 */
	private PScreenDTO convertePedidoEmPScreenDTO(Pedido p) {
		System.out.println("convertendo pedido");
		
		PScreenDTO pscreendto = new PScreenDTO();
		pscreendto.setClienteid(p.getCliente().getId());
		pscreendto.setPedidoid(p.getId());
		pscreendto.setDatapedido(p.getDatapedido());
		pscreendto.setTotal(p.getTotal());
		pscreendto.setStatus(p.getStatus());
			
//		cria uma lista de ItemPScreenDTO
		List<ItemPScreenDTO> lipsdto = new ArrayList<ItemPScreenDTO>();
//		converte cada itempedido em um itempscreendto
		for(ItemPedido ip : p.getItens()) {
			ItemPScreenDTO ipsdto = new ItemPScreenDTO();
			ipsdto.setProdutoid(ip.getProduto().getId());
			ipsdto.setQuantidade(ip.getQuantidade());
			ipsdto.setDescricao(ip.getProduto().getDescricao());
//			insere o item na lista de itens
			lipsdto.add(ipsdto);
		}
//		insere a lista de itens ao pscreendto
		pscreendto.setItens(lipsdto);
		return pscreendto;
	}
	
}
