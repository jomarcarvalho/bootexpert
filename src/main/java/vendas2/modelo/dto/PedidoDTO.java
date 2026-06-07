package vendas2.modelo.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import vendas2.MyAnnotation.NotEmptyList;
import vendas2.modelo.StatusPedidoEnum;

/**
 * Classe usada pelo usuario para registrar pedidos no banco de dados 
 * 
 * {
 *		"clienteid" : 1,
 *		"itens" : [ {
 *						"produtoid": 1,
 *						"quantidade": 10
 *					} ],
 *		"status": "iniciado"
 *	}
 * @author jomar
 *
 */
public class PedidoDTO {
	@NotNull(message = "Código do cliente é obrigatório.")
	private Integer clienteid;
	@NotEmptyList(message = "Não existe pedido sem itens, cacete.")
	private List<ItemPedidoDTO> itens;
	@NotNull(message = "É obrigatório informar status para o pedido.")
	private StatusPedidoEnum status;
	
	public Integer getClienteid() {
		return clienteid;
	}
	public void setClienteid(Integer clienteid) {
		this.clienteid = clienteid;
	}
	public List<ItemPedidoDTO> getItens() {
		return itens;
	}
	public void setItens(List<ItemPedidoDTO> itens) {
		this.itens = itens;
	}
	public StatusPedidoEnum getStatus() {
		return status;
	}
	public void setStatus(StatusPedidoEnum status) {
		this.status = status;
	}
	
}
