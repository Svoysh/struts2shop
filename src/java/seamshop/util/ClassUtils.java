package seamshop.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Helper class to work with generic subclasses through Java reflection.
 *
 * @author Alex Siman 2009-10-15
 */
public class ClassUtils
{
	private ClassUtils()
	{
		// Nothing.
	}

	/**
	 * @return <code>class</code> of type argument at specified position.
	 *
	 * @param genericSubclass
	 *     Class of parametrized type. Example:
	 *     <code>Superclass&lt;ONE, TWO, THREE&gt;</code>
	 *
	 * @param positionOfTypeArgument
	 *     Starts from <code>0</code>. If type of <code>genericSubclass</code>
	 *     is like <code>Superclass&lt;ONE, TWO, THREE&gt;</code> and needed to get
	 *     class of type argument <code>TWO</code>, then
	 *     <code>positionOfTypeArgument</code> is <code>1</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassOfTypeArgument(Class<?> genericSubclass,
		int positionOfTypeArgument)
	{
		Class<T> resultClass = null;
		Type supertype = genericSubclass.getGenericSuperclass();
		if (supertype instanceof ParameterizedType)
		{
			ParameterizedType paramedSupertype = (ParameterizedType) supertype;
			if (positionOfTypeArgument < 0)
			{
				positionOfTypeArgument = 0;
			}
			resultClass = (Class<T>) paramedSupertype
				.getActualTypeArguments()[positionOfTypeArgument];
		}

		if (resultClass == null)
		{
			throw new IllegalStateException(
				"Failed get class of type argument by reflection.");
		}

		return resultClass;
	}

	/**
	 * @return <code>class</code> of first type argument.
	 *
	 * @param genericSubclass
	 *     Class of parametrized type. Example:
	 *     <code>Superclass&lt;ONE, TWO, THREE&gt;</code>
	 */
	public static <T> Class<T> getClassOfFirstTypeArgument(Class<?> genericSubclass)
	{
		return getClassOfTypeArgument(genericSubclass, 0);
	}

	public static <T> Class<T> getClassOfTypeArgument(Object subclassInstance,
		int positionOfTypeArgument)
	{
		return getClassOfTypeArgument(subclassInstance.getClass(),
			positionOfTypeArgument);
	}

	public static <T> Class<T> getClassOfFirstTypeArgument(Object subclassInstance)
	{
		return getClassOfTypeArgument(subclassInstance, 0);
	}

	public static <T> T newInstanceOfClass(Class<T> clazz)
	{
		T newInstance = null;
		try
		{
			newInstance = clazz.newInstance();
		}
		catch (InstantiationException e)
		{
			// Ignore.
		}
		catch (IllegalAccessException e)
		{
			// TODO: Low: Ignore? (mb)
		}
		catch (Exception e)
		{
			// TODO: Low: Ignore? (mb)
		}

		if (newInstance == null)
		{
			throw new IllegalStateException(
				"Failed to create new instance of class by reflection.");
		}

		return newInstance;
	}

	public static <T> T newInstanceOfTypeArgument(Class<?> genericSubclass,
		int positionOfTypeArgument)
	{
		Class<T> classOfTypeArgument = getClassOfTypeArgument(
			genericSubclass, positionOfTypeArgument);

		T newInstance = newInstanceOfClass(classOfTypeArgument);

		if (newInstance == null)
		{
			throw new IllegalStateException(
				"Failed to create new instance of type argument by reflection.");
		}

		return newInstance;
	}

	public static <T> T newInstanceOfFirstTypeArgument(Class<?> genericSubclass)
	{
		return newInstanceOfTypeArgument(genericSubclass, 0);
	}

	public static <T> T newInstanceOfTypeArgument(Object subclassInstance,
		int positionOfTypeArgument)
	{
		return newInstanceOfTypeArgument(subclassInstance.getClass(),
			positionOfTypeArgument);
	}

	public static <T> T newInstanceOfFirstTypeArgument(Object subclassInstance)
	{
		return newInstanceOfTypeArgument(subclassInstance, 0);
	}

	/**
	 * @param clazz Class to retrieve class name.
	 * @return If class name is <code>SomeClass</code> then this method will
	 *         returns <code>someClass</code>
	 */
	public static String getInstanceClassName(Class<?> clazz)
	{
		StringBuilder classNameBuilder = new StringBuilder(clazz.getSimpleName());
		classNameBuilder.setCharAt(0, Character.toLowerCase(classNameBuilder.charAt(0)));
		return classNameBuilder.toString();
	}
}
