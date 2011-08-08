package seamshop.util;

/**
 * Lazy singleton pattern. Use this as blueprint.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton</a>
 * in Wikipedia.
 *
 * @author Alex Siman 2009-12-11
 */
public class LazySingleton
{
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private LazySingleton() {}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * {@link #getInstance()} or the first access to
	 * {@code SingletonHolder.INSTANCE}, not before.
	 */
	private static class SingletonHolder
	{
		private static final LazySingleton INSTANCE = new LazySingleton();
	}

	public static LazySingleton getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
