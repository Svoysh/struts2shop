package seamshop.interceptor.method;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import seamshop.interceptor.AbstractInterceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * This interceptor determine if it can allow to execute action method, named
 * other than "execute". Created to fix security hole related to Struts 2
 * feature "struts.enable.DynamicMethodInvocation = true".
 * Current versions of frameworks are: "Struts 2.1.8" and "XWork 2.1.6".
 *
 * @see AllowedMethod
 *
 * @author Alex Siman 2009-10-07
 */
// TODO: Low: Rename to: "AllowedMethodInterceptor"? (n)
// TODO: Also add @NotAllowedMethod annotation to override @AllowedMethod of superclass.
@Component
@SuppressWarnings("serial")
public class MethodInterceptor extends AbstractInterceptor
{
	private static final Set<String> ALLOWED_METHODS =
		new HashSet<String>(Arrays.asList("execute"));

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		String result = Action.ERROR;
		String methodName = getActionMethodName();
		if (ALLOWED_METHODS.contains(methodName)
			|| hasMethodAnnotation(AllowedMethod.class))
		{
			result = actionInvocation.invoke();
		}
		else
		{
			log.warn("Attempt to execute not allowed action method: [" + methodName + "].");
			// TODO: i18n: Localize.
			// TODO: Low: Maybe give a little explanation "why" the action method
			//       cannot be invoked? (because it is not allowed to execute at all)
			getAbstractAction().addActionError("Action cannot be invoked.");
		}

		return result;
	}
}