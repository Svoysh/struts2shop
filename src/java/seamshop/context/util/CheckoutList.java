package seamshop.context.util;

import java.io.Serializable;

import seamshop.context.SessionContext;
import seamshop.util.cache.Cache;
import seamshop.util.cache.LruCache;

/**
 * @see SessionContext
 * @see LruCache
 *
 * @author Alex Siman 2009-11-09
 * @author Alex Siman 2009-11-16
 */
@SuppressWarnings("serial")
public class CheckoutList implements Serializable
{
	private static final int MAX_CONCURRENT_CHECKOUTS = 5;

	/**
	 * <ul>
	 * <li>key - checkout UUID</li>
	 * <li>value - checkout process</li>
	 * </ul>
	 */
	/*
	 * TODO: Low: Memory usage: Make this cache expirable say after 10 min? (n,
	 *       server session will expire it)
	 */
	private Cache<String, Checkout> checkoutCache =
		new LruCache<String, Checkout>(MAX_CONCURRENT_CHECKOUTS);

	public CheckoutList()
	{}

	public boolean add(Checkout checkout)
	{
		if (checkout == null)
		{
			throw new IllegalArgumentException("Checkout cannot be null");
		}

		return checkoutCache.put(checkout.getId(), checkout);
	}

	public Checkout get(String checkoutId)
	{
		return checkoutCache.get(checkoutId);
	}

	public boolean remove(String checkoutId)
	{
		return checkoutCache.remove(checkoutId);
	}
}
