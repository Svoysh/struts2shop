package seamshop.util.cache;

import static seamshop.util.StringBuilderUtils.removeCharsFromEnd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Alex Siman 2009-11-13
 * @author Alex Siman 2009-11-16
 */
@SuppressWarnings("serial")
public class LruCache<K, V> implements Cache<K, V>, Serializable
{
	private static class ValueHolder<V> implements Serializable
	{
		private final V value;
		private int queueIndex;

		public ValueHolder(V value, int queueIndex)
		{
			this.value = value;
			this.queueIndex = queueIndex;
		}

		public V getValue()
		{
			return value;
		}

		public int getQueueIndex()
		{
			return queueIndex;
		}

		public void setQueueIndex(int queueIndex)
		{
			this.queueIndex = queueIndex;
		}

		@Override
		public String toString()
		{
			return new StringBuilder()
//				.append(getClass().getSimpleName())
				.append("{")
				.append(value)
				.append("@")
				.append(queueIndex)
				.append("}")
				.toString();
		}
	}

	private final Map<K, ValueHolder<V>> map;
	private final K[] queue;
	private final ReentrantLock lock;

	/** Current number of elements in the queue */
	private int count;

	/** Queue index for next remove, head index. */
    private int headIndex;

    /** Queue index for next put, tail index. */
    private int tailIndex;

	@SuppressWarnings("unchecked")
	public LruCache(int capacity)
	{
		if (capacity <= 0)
		{
			throw new IllegalArgumentException("Cache capacity must be > 0");
		}

		map = new HashMap<K, ValueHolder<V>>(capacity);
		queue = (K[]) new Object[capacity];
		lock = new ReentrantLock();
	}

	// All public methods below must execute body w/in lock. ------------------

	/* (non-Javadoc)
	 * @see seamshop.util.cache.Cache#put(K, V)
	 */
	/** Can't update value at key, if it is already put in cache. */
	@Override
	public boolean put(K key, V value)
	{
		lock();
		try
		{
			if (isEmpty())
			{
				addToTail(key, value);
			}
			else
			{
				if (containsKey(key))
				{
					// TODO: Think: Throw exception or not? (not)
//					throw new FailedToPutElementToCacheException(
//						"Value at such key already put");

					return false;
				}
				else if (!isFull())
				{
					addToTail(key, value);
				}
				else
				{
					K removeKey = queue[tailIndex];
					removeAndShift(removeKey);
					addToTail(key, value);
				}
			}
			return true;
		}
		finally
		{
			unlock();
		}
	}

	/* (non-Javadoc)
	 * @see seamshop.util.cache.Cache#get(K)
	 */
	@Override
	public V get(K key)
	{
		// TODO: Think: Do we really need the lock on read operations?
		//       See more examples from "java.util.concurrent".

		lock();
		try
		{
			if (isEmpty())
			{
				return null;
			}
			else
			{
				if (!containsKey(key))
				{
					// TODO: Think: Throw exception or not? (not)
//					throw new FailedToPutElementToCacheException(
//						"There is no value at such key");

					return null;
				}
				else
				{
					ValueHolder<V> valueHolder = map.get(key);
					V value = valueHolder.getValue();
					int i = valueHolder.getQueueIndex();
					if (inc(i) != tailIndex)
					{
						// Slide queue elems.
						for (;;)
						{
							int nexti = inc(i);
							if (nexti != tailIndex)
							{
								K shiftKey = queue[i] = queue[nexti];
								updateQueueIndex(shiftKey, i);
								i = nexti;
							}
							else
							{
								queue[i] = key;
								valueHolder.setQueueIndex(i);
								break;
							}
						}
					}
					return value;
				}
			}
		}
		finally
		{
			unlock();
		}
	}

	/* (non-Javadoc)
	 * @see seamshop.util.cache.Cache#remove(K)
	 */
	@Override
	public boolean remove(K key)
	{
		lock();
		try
		{
			if (!isEmpty())
			{
				if (!containsKey(key))
				{
					// TODO: Think: Throw exception or not? (not)
//					throw new FailedToPutElementToCacheException(
//						"There is no value at such key");
				}
				else
				{
					removeAndShift(key);
					return true;
				}
			}
			return false;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public String toString()
	{
		lock();
		try
		{
			StringBuilder builder = new StringBuilder()
				.append(getClass().getSimpleName())
				.append("{")
				.append("map=[");

			if (count > 0)
			{
				for (Entry<K, ValueHolder<V>> entry : map.entrySet())
				{
					builder.append(entry.getKey()).append("=").append(
						entry.getValue()).append(", ");
				}
				removeCharsFromEnd(builder, 2);
			}

			builder
				.append("], ")
				.append("queue={")
				.append("values=[");

			if (count > 0)
			{
				for (K key : queue)
				{
					builder.append(key).append(", ");
				}
				removeCharsFromEnd(builder, 2);
			}

			return builder
				.append("], headIndex=").append(headIndex)
				.append(", tailIndex=").append(tailIndex)
				.append("}}")
				.toString();
		}
		finally
		{
			unlock();
		}
	}

	// Methods to use only in unit tests. -------------------------------------

	// Indeed these protected methods are not required by API, but they are used
	// in unit tests. Also reflection can be used to be able to remove these
	// methods and have ability to test private fields.
	// See [http://stackoverflow.com/questions/34571/whats-the-best-way-of-unit-testing-private-methods]

	/** Use this method only in unit tests. */
	// TODO: Rename to: "size()" as in Java Collections.
	// TODO: Remove? (n)
	protected int getCount()
	{
		lock();
		try
		{
			return count;
		}
		finally
		{
			unlock();
		}
	}

	/** Use this method only in unit tests. */
	// TODO: Remove? (n)
	protected int getTailIndex()
	{
		lock();
		try
		{
			return tailIndex;
		}
		finally
		{
			unlock();
		}
	}

	/** Use this method only in unit tests. */
	// TODO: Remove? (n)
	protected int getHeadIndex()
	{
		lock();
		try
		{
			return headIndex;
		}
		finally
		{
			unlock();
		}
	}

	/** Use this method only in unit tests. */
	// TODO: Remove? (n)
	protected int getQueueIndex(K key)
	{
		lock();
		try
		{
			return map.get(key).getQueueIndex();
		}
		finally
		{
			unlock();
		}
	}

	// All private methods below must be called only when holding lock. -------

	private void addToTail(K key, V value)
	{
		queue[tailIndex] = key;
		ValueHolder<V> valueHolder = new ValueHolder<V>(value, tailIndex);
		map.put(key, valueHolder);
		tailIndex = inc(tailIndex);
		count++;
	}

	/**
	 * Removes element and shift queue.
	 */
	private void removeAndShift(K key)
	{
		int i = map.get(key).getQueueIndex();

		// If removing front item, just advance.
		if (i == headIndex)
		{
			map.remove(key);
			queue[headIndex] = null;
			headIndex = inc(headIndex);
		}
		else
		{
			// Slide over all others up through tailIndex.
			// TODO: Refactor: Extract shift method somehow? (if possible)
			// TODO: Improve shifting of queue accordingly to removing index:
			//       if it in 1-st part of queue - shift queue to left (back);
			//       else shift queue to right (forward).
			for (;;)
			{
				int nexti = inc(i);
				if (nexti != tailIndex)
				{
					K shiftKey = queue[i] = queue[nexti];
					updateQueueIndex(shiftKey, i);
					i = nexti;
				}
				else
				{
					map.remove(key);
					queue[i] = null;
					tailIndex = i;
					break;
				}
			}
		}
		count--;
	}

	private void updateQueueIndex(K key, int newIndex)
	{
		map.get(key).setQueueIndex(newIndex);
	}

	private boolean containsKey(K key)
	{
		return map.containsKey(key);
	}

	/**
	 * Circularly increment i.
	 */
	private int inc(int i)
	{
		return (++i == queue.length)? 0 : i;
	}

	private boolean isEmpty()
	{
		return count == 0;
	}

	private boolean isFull()
	{
		return count == queue.length;
	}

	private void lock()
	{
		lock.lock();
	}

	private void unlock()
	{
		lock.unlock();
	}
}
