package seamshop.interceptor.security;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import seamshop.model.enums.UserRole;

/**
 * Lazy singleton.
 *
 * @author Alex Siman 2009-11-02
 */
/*
 * TODO: Low: Rename to: "MethodAccessResolver", etc. such as this class can be
 *       applied not only for Action classes.
 */
public class ActionRoleResolver
{
	private final ActionRoleRules actionRoles;

	protected ActionRoleResolver(ActionRoleRules actionRoles)
	{
		this.actionRoles = actionRoles;
	}

	/**
	 * @see #getMethodPath(Class, String)
	 */
	public boolean canUserExecuteMethod(UserRole userRole, Class<?> actionClass,
		String methodName)
	{
		UserRole[] methodRoles = getMethodRoles(actionClass, methodName);
		return userRole.isRoleInGroup(methodRoles);
	}

	public UserRole[] getMethodRoles(Class<?> actionClass, String methodName)
	{
		UserRole[] methodRoles = null;
		String methodPath = getMethodPath(actionClass, methodName);
		boolean hasMethodRoles = actionRoles.hasMethodRoles(methodPath);
		if (hasMethodRoles)
		{
			methodRoles = actionRoles.getMethodRoles(methodPath);
		}
		else
		{
			// Try to find roles for mehod's class.
			String classPath = actionClass.getName();
			boolean hasClassRoles = actionRoles.hasClassRoles(classPath);
			if (hasClassRoles)
			{
				methodRoles = actionRoles.getClassRoles(classPath);
			}
			else
			{
				// Try to find roles for package of mehod's class.
				String packagePath = actionClass.getPackage().getName();
				methodRoles = actionRoles.findPackageRolesRecursively(packagePath);
			}

			if (methodRoles == null)
			{
				methodRoles = new UserRole[0];
			}

			// Cache found method roles.
			actionRoles.addMethodRoles(methodPath, methodRoles);
		}

		return methodRoles != null
			? methodRoles
			: new UserRole[0];
	}

	private String getMethodPath(Class<?> actionClass, String methodName)
	{
		if (actionClass == null)
		{
			throw new NullArgumentException("actionClass");
		}

		if (StringUtils.isBlank(methodName))
		{
			throw new NullArgumentException("methodName");
		}

		String classPath = actionClass.getName();

		return new StringBuilder()
			.append(classPath)
			.append(".")
			.append(methodName)
			.toString();
	}
}
