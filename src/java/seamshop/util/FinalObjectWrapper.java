package seamshop.util;

/**
 * Helper class that make possible to change value of final variable,
 * e.g. of type String, Integer, etc.
 *
 * @param <T> class of wrapped final object.
 *
 * @author Alex Siman 2009-05-14
 */
public class FinalObjectWrapper<T>
{
	private T finalObject;

	public FinalObjectWrapper()
	{
		this(true);
	}

	public FinalObjectWrapper(boolean instantiate)
	{
		if (instantiate)
		{
			finalObject = ClassUtils.newInstanceOfFirstTypeArgument(this);
		}
	}

	public FinalObjectWrapper(T initValue)
	{
		finalObject = initValue;
	}

	public T getFinalObject()
	{
		return this.finalObject;
	}

	public void setFinalObject(T finalObject)
	{
		this.finalObject = finalObject;
	}
}
