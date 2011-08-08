package seamshop.model.enums;

import org.apache.commons.lang.ArrayUtils;

import seamshop.exception.BlankArrayArgumentException;

public enum UserRole
{
	GUEST,
	MEMBER,
	ADMIN;

	public static final UserRole DEFAULT = GUEST;

	public static final UserRole[] ADMIN_GROUP = {ADMIN};
	public static final UserRole[] MEMBER_GROUP = {ADMIN, MEMBER};
	public static final UserRole[] GUEST_GROUP = {ADMIN, MEMBER, GUEST};

	/**
	 * @return <code>true</code> if <code>roleGroup</code> contains this role.
	 *
	 * @see UserRole#ADMIN_GROUP
	 * @see UserRole#MEMBER_GROUP
	 * @see UserRole#GUEST_GROUP
	 */
	public boolean isRoleInGroup(UserRole... roleGroup)
	{
		if (ArrayUtils.isEmpty(roleGroup))
		{
			throw new BlankArrayArgumentException("roleGroup");
		}

		return ArrayUtils.contains(roleGroup, this);
	}
}
