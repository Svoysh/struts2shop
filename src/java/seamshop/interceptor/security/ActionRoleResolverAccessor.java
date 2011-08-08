package seamshop.interceptor.security;

/**
 * Lazy singleton factory.
 *
 * @author Alex Siman 2009-11-03
 */
// TODO: Rename to: "ActionRoleResolverFactory"? (mb)
public class ActionRoleResolverAccessor
{
	/**
	 * {@link SingletonHolder} is loaded on the first execution of
	 * {@link ActionRoleResolverAccessor#getActionRoleResolver()} or the first
	 * access to {@link SingletonHolder#INSTANCE}, not before.
	 */
	private static class SingletonHolder
	{
		private static final ActionRoleResolver INSTANCE =
			new ActionRoleResolver(
				new MainActionRoleRules());
	}

	public static ActionRoleResolver getActionRoleResolver()
	{
		return SingletonHolder.INSTANCE;
	}

	// Private constructor prevents instantiation from other classes.
	private ActionRoleResolverAccessor()
	{}
}
