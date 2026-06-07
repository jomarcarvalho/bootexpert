package vendas2.exception;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {
	
	private List<String> errors;
	
	public ApiErrors(String msgErro) {
		this.errors  = Arrays.asList(msgErro);
	}

	public ApiErrors(List<String> msgErro) {
		this.errors  = msgErro;
	}

    public ApiErrors(String cpfJáExisteNaBase, int value) {
	}

    public List<String> getErrors() {
		return errors;
	}
	
}
