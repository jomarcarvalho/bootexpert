package vendas2.modelo.dto;

public class ItemPScreenDTO {
	private Integer produtoid;
	private Integer quantidade;
	private String descricao;
	
	public Integer getProdutoid() {
		return produtoid;
	}
	public void setProdutoid(Integer produtoid) {
		this.produtoid = produtoid;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
