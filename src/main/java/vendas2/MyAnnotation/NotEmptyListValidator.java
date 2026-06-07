package vendas2.MyAnnotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import vendas2.modelo.dto.ItemPedidoDTO;

/**
 * Classe apontada pela interface NotEmptyList para testar se a lista de itens do pedido dto contem valores 
 * ou esta nula
 * @author jomar
 *
 */
public class NotEmptyListValidator  implements ConstraintValidator<NotEmptyList, List<ItemPedidoDTO>>{

	@Override
	public boolean isValid(List<ItemPedidoDTO> list, ConstraintValidatorContext constraintValidatorContext) {
		return (list != null) && (!list.isEmpty());
	}
	
}
