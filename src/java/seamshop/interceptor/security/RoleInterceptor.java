package seamshop.interceptor.security;

import static seamshop.model.enums.UserRole.DEFAULT;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import seamshop.action.AbstractAction;
import seamshop.interceptor.AbstractInterceptor;
import seamshop.model.enums.UserRole;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * @author Alex Siman 2009-10-28
 * @author Alex Siman 2009-11-03
 */
@Component
@SuppressWarnings("serial")
public class RoleInterceptor extends AbstractInterceptor
{
	/**
	 * Result if role interceptor disallow to invoke current action.
	 */
	// TODO: Low: Move to [seamshop.consts.ActionResult]? (mb)
	public static final String ACCESS_DENIED_RESULT = Action.LOGIN;

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		boolean allowActionInvokation = false;
		UserRole currentRole = DEFAULT;
		UserRole[] requiredRoles = {};

		AbstractAction action = getAbstractAction();
		if (action instanceof UserRoleAware)
		{
			UserRoleAware roleAwareAction = action;
			currentRole = roleAwareAction.getUserRole();
			try
			{
				requiredRoles = getRolesRequiredForCurrentAction();
				allowActionInvokation = currentRole.isRoleInGroup(requiredRoles);
			}
			catch (Exception ex)
			{
				log.error("Failed to determine whether this action can be " +
					"invoked by current user. User role is [" + currentRole +
					"].", ex);
			}
		}

		// Action can be invoked.
		if (allowActionInvokation)
		{
			return actionInvocation.invoke();
		}
		// Login required.
		else
		{
			String actionUrl = action.getPagerUrl();
			log.warn("Action [" + actionUrl + "] cannot be invoked. " +
				"Current user role is [" + currentRole +
				"]. Required roles are " + Arrays.toString(requiredRoles));

			// TODO: i18n: Localize.
			getAbstractAction().addActionError(
				"You have no permission to execute this action.");

			return ACCESS_DENIED_RESULT;
		}
	}

	private UserRole[] getRolesRequiredForCurrentAction()
	{
		String methodName = getActionMethodName();
		return ActionRoleResolverAccessor.getActionRoleResolver()
			.getMethodRoles(getActionClass(), methodName);
	}
}
