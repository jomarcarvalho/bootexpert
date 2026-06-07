package vendas2.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="itempedido")
public class ItemPedido {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mytable_itempedido_seq")
	@SequenceGenerator(name="mytable_itempedido_seq", sequenceName="seq_itempedido_id", allocationSize=1)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pedidoid")
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name = "produtoid")
	private Produto produto;

	private Integer quantidade;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return "ItemPedido [id=" + id + ", pedido=" + pedido + ", produto=" + produto + ", quantidade=" + quantidade
				+ "]";
	}
	
	
}
