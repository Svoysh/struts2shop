package seamshop.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collections helper class.
 */
public abstract class CollectionUtils
{
	public static final Collection<?> NULL_COLLECTION = Collections.singleton(null);

	public static <T> boolean isNullOrEmpty(List<T> list)
	{
		return isNullOrEmpty((Collection<T>) list);
	}

	public static <T> boolean isNullOrEmpty(Set<T> set)
	{
		return isNullOrEmpty((Collection<T>) set);
	}

	/**
	 * Returns <code>true</code> if <code>collection</code> is <code>null</code>
	 * or its size <code>== 0</code>.
	 */
	public static <T> boolean isNullOrEmpty(Collection<T> collection)
	{
		if ((collection == null) || collection.isEmpty())
		{
			return true;
		}

		return false;
	}

	public static <K, V> boolean isNullOrEmpty(Map<K, V> map)
	{
		if ((map == null) || map.isEmpty())
		{
			return true;
		}

		return false;
	}

	public static <T> void removeNulls(List<T> list)
	{
		removeNulls((Collection<T>) list);
	}

	public static <T> void removeNulls(Set<T> set)
	{
		removeNulls((Collection<T>) set);
	}

	/**
	 * Removes all <code>null</code> elements from collection.
	 */
	public static <T> void removeNulls(Collection<T> collection)
	{
		if (!isNullOrEmpty(collection))
		{
			collection.removeAll(NULL_COLLECTION);
		}
	}

	/**
	 * Returns first not null element of collection.
	 */
	public static <T> T getFirstNotNull(Collection<T> collection)
	{
		for (T elem : collection)
		{
			if (elem != null)
			{
				return elem;
			}
		}

		// If every element is null.
		return null;
	}
}
