package seamshop.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang.ArrayUtils;

/*
 * TODO: We need to check, if input parameter is of String type, if not -
 * throw exception. Possible code example:
 *
 * if (!(string instanceof String))
 *	  throw RuntimeException("Input parameter is not of String type");
 */
/**
 * String helper class.
 *
 * @author Alex Siman
 */
public abstract class StringUtils
{
	private final Log log = new Log(this.getClass());

	public static final String DEFAULT_SEPARATOR = ",";

	/**
	 * Returns <code>true</code> if <code>string</code> is either
	 * <code>null</code> or <code>""</code> or consists of white spaces only.
	 *
	 * @deprecated Use {@link org.apache.commons.lang.StringUtils#isBlank(String)}.
	 */
	// TODO: Low: Replace by: [org.apache.commons.lang.StringUtils.isBlank()]? (mb)
	@Deprecated
	public static boolean isNullOrEmpty(String string)
	{
		return (string == null) || isEmpty(string);
	}

	/**
	 * Returns <code>true</code> if at least one element of <code>strings</code>
	 * array is either <code>null</code> or <code>""</code> or consists of white
	 * spaces only.
	 */
	public static boolean isNullOrEmpty(String[] strings)
	{
		if (strings == null)
		{
			return false;
		}

		for (String string : strings)
		{
			if (isNullOrEmpty(string))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns <code>true</code> if <code>string</code> is not <code>null</code>
	 * but either equals to <code>""</code> or consists of white spaces only.
	 */
	public static boolean isEmpty(String string)
	{
		return (string != null) && (string.equals("") || (string.trim().length() == 0));
	}

	/**
	 * Returns <code>true</code> if at least one element of <code>strings</code>
	 * array is not <code>null</code> but either equals to <code>""</code> or
	 * consists of white spaces only.
	 */
	public static boolean isEmpty(String[] strings)
	{
		if (strings == null)
		{
			return false;
		}

		for (String string : strings)
		{
			if (isEmpty(string))
			{
				return true;
			}
		}

		return false;
	}

	// Legacy. Maybe delete next methods as unneeded. -------------------------

	/**
	 * Return substring bounded by markers. E.g.:
	 * <code>str = "some.sub.string"</code> if
	 * <code>beginMarker = "some."</code> and <code>endMarker = ".string"</code>
	 * then <code>substring = "sub"</code>.
	 */
	public static String getFirstSubString(String string, String beginMarker,
		String endMarker)
	{
		String[] parameters = {string, beginMarker, endMarker};
		if (isNullOrEmpty(parameters))
		{
			throw new NullPointerException(
				"All method parameters must be not null");
		}

		String substring = null;
		int markerBeginIndex = string.indexOf(beginMarker);
		// If beginMarker was not found.
		if (markerBeginIndex < 0)
		{
			return substring;
		}

		int substringBeginIndex = markerBeginIndex + beginMarker.length();
		int substringEndIndex = string.indexOf(endMarker, substringBeginIndex);
		if ((substringBeginIndex > 0) && (substringEndIndex > 0))
		{
			substring = string.substring(substringBeginIndex, substringEndIndex);
		}

		return substring;
	}

	// TODO: Low: See apache commons to string?
	public void printPropertiesThroughGetters(Object object)
	{
		String className = object.getClass().getName();
		log.debug(">>> Begin: Properties of object of class " + className + ":");
		Method[] methods = object.getClass().getMethods();
		if (!ArrayUtils.isEmpty(methods))
		{
			log.debug("There are no getters without arguments.");
		}
		else
		{
			// Sort methods by its names.
			Arrays.sort(methods, new Comparator<Method>()
			{
				@Override
				public int compare(Method m1, Method m2)
				{
					return m1.getName().compareTo(m2.getName());
				}
			});

			// Print values returned by any of get* methods that takes no arguments.
			for (Method method : methods)
			{
				String methodName = method.getName();
				if (methodName.startsWith("get")
					&& (method.getParameterTypes().length == 0))
				{
					try
					{
						log.debug(methodName + ": " + method.invoke(object));
					}
					catch (IllegalArgumentException ex)
					{
						ex.printStackTrace();
					}
					catch (IllegalAccessException ex)
					{
						ex.printStackTrace();
					}
					catch (InvocationTargetException ex)
					{
	//					ex.printStackTrace();
					}
				}
			}
		}
		log.debug("<<< End: Properties of object of class " + className + ".");
	}
}
