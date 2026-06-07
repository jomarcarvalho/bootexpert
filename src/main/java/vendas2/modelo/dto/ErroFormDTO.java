package vendas2.modelo.dto;

public class ErroFormDTO {
	private String campo;
	private String erro;
	
	public ErroFormDTO(String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
	}
	public ErroFormDTO() {
	}
	public String getCampo() {
		return campo;
	}
	public String getErro() {
		return erro;
	}
	
}
