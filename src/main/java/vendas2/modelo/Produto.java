package vendas2.modelo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="produto")
public class Produto {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mytable_produto_seq")
	@SequenceGenerator(name="mytable_produto_seq", sequenceName="seq_produto_id", allocationSize=1)
	private Integer id;
	
	@Column
	@Size(min = 5, max = 100, message = "O campo descricao deve ter entre 5 e 100 caracteres")
	@NotBlank(message = "A descrição do produto é obrigatória")
	private String descricao;
	
	@Column(name="precounitario", precision = 20, scale = 2)
	@NotNull(message = "O preço do produto é obrigatório")
	private BigDecimal preco;

	public Produto(String desc, BigDecimal prc) {
		this.descricao = desc;
		this.preco = prc;
	}
	public Produto() {
	}
	public Integer getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", descricao=" + descricao + ", preco=" + preco + "]";
	}
	
}
