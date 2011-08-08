package seamshop.interceptor.security;

import static seamshop.model.enums.UserRole.GUEST_GROUP;
import static seamshop.model.enums.UserRole.MEMBER_GROUP;

/**
 * @author Alex Siman 2009-11-03
 */
public class MainActionRoleRules extends ActionRoleRules
{
	public MainActionRoleRules()
	{
		// TODO: PRODUCTION: Re-check security rules.

		// Admins only.

		// Members & Admins.
		addPackageRoles("seamshop.action.buyer", MEMBER_GROUP);
		addPackageRoles("seamshop.action.member", MEMBER_GROUP);
		addPackageRoles("seamshop.action.settings", MEMBER_GROUP);

		// Guests, Members & Admins.
		addPackageRoles("seamshop.action", GUEST_GROUP);
		addPackageRoles("seamshop.action.ajax", GUEST_GROUP);
		addPackageRoles("seamshop.action.checkout", GUEST_GROUP);
	}
}
