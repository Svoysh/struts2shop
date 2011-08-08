package seamshop.interceptor.security;

import seamshop.model.enums.UserRole;

/**
 * @see RoleInterceptor
 *
 * @author Alex Siman 2009-10-22
 */
public interface UserRoleAware
{
	public UserRole getUserRole();
}
