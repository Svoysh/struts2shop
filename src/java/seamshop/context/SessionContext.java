package seamshop.context;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import seamshop.context.util.Checkout;
import seamshop.context.util.CheckoutList;
import seamshop.model.Order;
import seamshop.model.User;
import seamshop.model.enums.UserRole;
import seamshop.util.UuidGenerator;

/**
 * Helper class to store session related objects (e.g. current user, etc.).
 * It is Spring bean.
 *
 * @author Alex Siman 2009-05-16
 */
// TODO: High: Concurrency: Use AtomicReference or ReentrantLock for all fields
//       because session context is concurrent and can be accessed
//       by multiple threads at the same time.

// TODO: Remove Spring related annotations? (y)
//       This Spring bean configured in "applicationContext.xml".
//@Component
//@Scope(WebApplicationContext.SCOPE_SESSION)

@SuppressWarnings("serial")
public class SessionContext implements Serializable
{
	// Current user -----------------------------------------------------------

	// TODO: Store in SessionContext only user ID instead of entire user.
	//       Fresh user entity must be loaded with every request.
	private User user = null;

	// TODO: Low: Rename: "hasCurrentUser"? (y)
	public boolean hasUser()
	{
		return getUser() != null;
	}

	// TODO: Low: Rename: "getCurrentUser"? (y)
	public User getUser()
	{
		return user;
	}

	// TODO: Low: Rename: "loginUser"/"login".
	public void setUser(User user)
	{
		if (user == null)
		{
			throw new NullPointerException("Do not set user to null explicitly." +
				" Use 'removeUser()' method.");
		}

		this.user = user;
	}

	// TODO: Low: Rename: "logoutUser"/"logout".
	public void removeUser()
	{
		// TODO: Make more sophisticated? (y)
		// TODO: Maybe set user to null in DAOs too.
		user = null;
	}

	public UserRole getUserRole()
	{
		if (user != null)
		{
			UserRole userRole = user.getRole();
			if (userRole != null)
			{
				return userRole;
			}
		}

		return UserRole.DEFAULT;
	}

	// Checkout stuff ---------------------------------------------------------

	private CheckoutList checkoutList = null;

	public CheckoutList getCheckoutList()
	{
		// Lazy initialization.
		if (checkoutList == null)
		{
			checkoutList = new CheckoutList();
		}
		return checkoutList;
	}
}
