package seamshop.interceptor;

import org.springframework.stereotype.Component;

import seamshop.context.RequestContext;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * This interceptor initializes some {@link RequestContext} fields.
 *
 * @author Alex Siman 2009-07-30
 */
@Component
@SuppressWarnings("serial")
public class RequestContextInterceptor extends AbstractInterceptor
{
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		Object action = actionInvocation.getAction();
		if (action instanceof RequestContextAware)
		{
			((RequestContextAware) action).initRequestContext();
		}

		return actionInvocation.invoke();
	}
}
