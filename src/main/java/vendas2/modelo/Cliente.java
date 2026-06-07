package vendas2.modelo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tbl_cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mytable_cliente_seq")
	@SequenceGenerator(name="mytable_cliente_seq", sequenceName="seq_cliente_id", allocationSize=1)
	private Integer id;
	
	@Column
	@Size(min = 4, max = 100, message = "O campo nome deve ter entre 2 e 100 caracteres")
	@NotBlank(message = "Nome do cliente é obrigatório")
	private String nome;
	
	@Column
	@Size(min = 7, max = 50, message = "O campo email deve ter entre 7 e 50 caracteres")
	@NotBlank(message = "O email do cliente é obrigatório")
	private String email;
	
	@Column
	@NotNull(message = "Data de nascimento do cliente é obrigatório")
	private LocalDate nascimento;
	
	@Column
	@Size(min = 11, max = 11, message = "O campo cpf deve ter exatamente 11 digitos")
	@NotBlank(message = "CPF do cliente é obrigatório e deve ser numerico")
	private String cpf;

	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos;

	public Cliente(String n, LocalDate nc, String cpfc) {
		this.nome = n;
		this.nascimento = nc;
		this.cpf = cpfc;
	}

	public Cliente() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
