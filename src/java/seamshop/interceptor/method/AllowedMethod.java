package seamshop.interceptor.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark any action method with a name other than "execute" which can be
 * executed too.
 *
 * @author Alex Siman 2009-10-07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowedMethod
{}
