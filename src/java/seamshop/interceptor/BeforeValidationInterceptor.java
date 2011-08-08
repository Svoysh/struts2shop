package seamshop.interceptor;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * This interceptor calls {@link BeforeValidationAware#doBeforeValidation()}
 * on actions which implement {@link BeforeValidationAware}. This interceptor
 * is very useful for any situation, where you need to clean up or modify some
 * parameters just after all parameters are set but before validation and
 * action method invoked.
 *
 * @see BeforeValidationAware
 *
 * @author Alex Siman 2009-06-08
 */
//TODO: Do we need this method? (n) See PrepareInterceptor.
@Component
@SuppressWarnings("serial")
public class BeforeValidationInterceptor extends AbstractInterceptor
{
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		Object action = actionInvocation.getAction();
		if (action instanceof BeforeValidationAware)
		{
			((BeforeValidationAware) action).doBeforeValidation();
		}

		return actionInvocation.invoke();
	}
}
