package seamshop.consts;

/**
 * Struts 2 interceptors.
 *
 * @author Alex Siman
 */
public abstract class Interceptor
{
	// Single interceptors.
	public static final String MEMBER = "member";
	public static final String TRANSACTION = "transaction";
	public static final String READ_TRANSACTION = "readTransaction";
	public static final String WRITE_TRANSACTION = "writeTransaction";

	// Interceptor stacks.
	public static final String GUEST_STACK = "guestStack";
	public static final String MEMBER_STACK = "memberStack";

	// TODO: Need? (n)
//	public static final String READ_TRANSACTION_STACK = "readTransactionStack";
//	public static final String WRITE_TRANSACTION_STACK = "writeTransactionStack";

	// TODO: Add (if needed) others Struts 2/XWork interceptors & stacks? (y)
}
