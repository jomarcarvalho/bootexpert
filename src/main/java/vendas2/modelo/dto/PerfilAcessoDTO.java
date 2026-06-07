package vendas2.modelo.dto;

import java.util.List;

public class PerfilAcessoDTO {
	private String usuariologin;
	private List<String> perfil;
	
	public PerfilAcessoDTO(String usuariologin, List<String> perfil) {
		this.usuariologin = usuariologin;
		this.perfil = perfil;
	}
	
	public PerfilAcessoDTO() {
	}

	public String getUsuariologin() {
		return usuariologin;
	}

	public void setUsuariologin(String usuariologin) {
		this.usuariologin = usuariologin;
	}

	public List<String> getPerfil() {
		return perfil;
	}

	public void setPerfil(List<String> perfil) {
		this.perfil = perfil;
	}

}
