package vendas2.MyAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/*
@Retention(RetentionPolicy.RUNTIME) means that the annotation can be accessed via reflection at runtime. 
If you do not set this directive, the annotation will not be preserved at runtime, and thus not available via reflection.

@Target(ElementType.TYPE) means that the annotation can only be used ontop of types (classes and 
interfaces typically). You can also specify METHOD or FIELD, or you can leave the target out alltogether so 
the annotation can be used for both classes, methods and fields.

Ver em PedidoDTO.java
*/


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyListValidator.class)
public @interface NotEmptyList {
	String message() default "A lista não pode ser vazia.";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
