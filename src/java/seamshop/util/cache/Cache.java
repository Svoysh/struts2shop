package seamshop.util.cache;

/**
 * Simple concurrent cache.
 *
 * @see LruCache
 *
 * @author Alex Siman 2009-11-14
 */
public interface Cache<K, V>
{
	boolean put(K key, V value);

	V get(K key);

	boolean remove(K key);
}
