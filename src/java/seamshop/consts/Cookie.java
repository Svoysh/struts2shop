package seamshop.consts;

/**
 * Cookie names. NOTE: Compose any cookie name starting w/ <code>PREFIX</code>.
 *
 * @author Alex Siman 2009-08-26
 */
public abstract class Cookie
{
	private static final String PREFIX = "struts2shop.";

	// TODO: Compose cookie name due to current subdomain.
	// TODO: Low: Rename: SESSION_ID = "sid"? (y)
	public static final String REMEMBER_ME = PREFIX + "rmid";
}
