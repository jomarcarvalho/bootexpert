package vendas2.modelo;

public enum StatusPedidoEnum {
	
	PEDIDO,   // cliente acabou de formalizar um pedido
	PAGAMENTO,   // aguardando confirmaçao de pagamento
	INICIADO,   // estoque esta separando os itens
	PREPARADO,   // pacote pronto para enviar
	ENVIADO,   // pacote entregue a transportadora
	ENTREGUE,   // transportadora entregou pacote no destino
	CANCELADO,   // pedido foi cancelado
	TROCA,   // cliente pediu troca de itens enviados incorretamente
	DEVOLVIDO   // transportadora nao encontrou endereço de destino
}
