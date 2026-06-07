package vendas2.timer;

import org.springframework.stereotype.Component;

@Component
public class TimeWatch {

	private Long Inicio;
	private Long Fim;
	private Long Duracao;
	
	/**
     * Inicia registro de tempo com a hora atual
     */
    public void iniciaCronometro() {
    	System.out.println("Disparando cronometro ...");
        this.Inicio = System.currentTimeMillis();
    }

    /**
     * Termina registro de tempo com hora atual e calcula duracao
     */
    public void terminaCronometro() {
    	System.out.println("Parando cronometro e calculando tempo decorrido...");
        this.Fim = System.currentTimeMillis();
        this.Duracao = this.Fim - this.Inicio;
        System.out.println("Tempo decorrido: " + this.Duracao + " milisegundos");
    }

}
