package vendas2.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="pedido")
public class Pedido {

	public Pedido() {
		this.itens = new ArrayList<ItemPedido>();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mytable_pedido_seq")
	@SequenceGenerator(name="mytable_pedido_seq", sequenceName="seq_pedido_id", allocationSize=1)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "clienteid")
	private Cliente cliente;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens;
	
	@Column
	private LocalDate datapedido;
	
//	@JsonIgnore   porque coloquei esta anotacao neste campo ?
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private StatusPedidoEnum status;
	
	@Column(name="total", precision = 20, scale = 2)
	private BigDecimal total;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<ItemPedido> getItens() {
		return this.itens;
	}
	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}
	public LocalDate getDatapedido() {
		return datapedido;
	}
	public void setDatapedido(LocalDate datapedido) {
		this.datapedido = datapedido;
	}
	public StatusPedidoEnum getStatus() {
		return status;
	}
	public void setStatus(StatusPedidoEnum status) {
		this.status = status;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", cliente=" + cliente + ", itens=" + itens + ", datapedido=" + datapedido
				+ ", status=" + status + ", total=" + total + "]";
	}	
}
