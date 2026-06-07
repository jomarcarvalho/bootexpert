package vendas2.modelo.dto;

public class TokenDTO {
	private String token;
	private String tipo;
	
	public TokenDTO(String log, String tok){
		this.token = log;
		this.tipo = tok;
	}
	public TokenDTO(){
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
