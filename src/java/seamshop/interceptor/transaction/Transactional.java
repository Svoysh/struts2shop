package seamshop.interceptor.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * TODO: Refactor: Create separated annos like:
 *       - NoTransaction;
 *       - ReadTransaction;
 *       - WriteTransaction;
 *       - (DefaultTransaction);
 *
 *       See annos from package "interceptor.security".
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional
{
	TransactionType value() default TransactionType.READ;
}
