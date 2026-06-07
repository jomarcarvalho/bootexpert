package vendas2.modelo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import vendas2.modelo.StatusPedidoEnum;

/**
 * Classe usada para informar ao usuario sobre algum(ns) pedido(s) juntamente com seus respectivos itens.
 * 
 * {
 *		"idcliente" : 1,
 *		"pedidoid" : 2,
 *		"datapedido" : "2022-01-17",
 *		"total" : 100,
 *		"status": "iniciado",
 *		"itens" : [ {
 *					"idproduto": 1,
 *					"quantidade": 10,
 *					"descricao": "bla bla"
 *					} ]
 *	}
 * @author jomar
 *
 */
public class PScreenDTO {
	private Integer clienteid;
	private Integer pedidoid;
	private LocalDate datapedido; 
	private BigDecimal total;
	private StatusPedidoEnum status;
	private List<ItemPScreenDTO> itens;
	
	public Integer getClienteid() {
		return clienteid;
	}
	public void setClienteid(Integer clienteid) {
		this.clienteid = clienteid;
	}
	public Integer getPedidoid() {
		return pedidoid;
	}
	public void setPedidoid(Integer pedidoid) {
		this.pedidoid = pedidoid;
	}
	public LocalDate getDatapedido() {
		return datapedido;
	}
	public void setDatapedido(LocalDate datapedido) {
		this.datapedido = datapedido;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public StatusPedidoEnum getStatus() {
		return status;
	}
	public void setStatus(StatusPedidoEnum status) {
		this.status = status;
	}
	public List<ItemPScreenDTO> getItens() {
		return itens;
	}
	public void setItens(List<ItemPScreenDTO> itens) {
		this.itens = itens;
	}
	
}
