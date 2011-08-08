package seamshop.util;

/**
 * Array helper class.
 *
 * @author Alex Siman 2009-01-31
 */
public abstract class ArrayUtils
{
	/**
	 * Returns <code>true</code> if <code>array</code> is <code>null</code> or
	 * its length <code>== 0</code>.
	 *
	 * @deprecated Use {@link org.apache.commons.lang.ArrayUtils#isEmpty(Object[])}.
	 */
	@Deprecated
	public static boolean isNullOrEmpty(Object[] array)
	{
		if ((array == null) || (array.length == 0))
		{
			return true;
		}

		return false;
	}
}
