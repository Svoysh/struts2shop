package seamshop.exception;

/**
 * @author Alex Siman 2009-11-02
 */
@SuppressWarnings("serial")
public class BlankArrayArgumentException extends IllegalArgumentException
{
	public BlankArrayArgumentException(String argumentName)
	{
		super("Array argument is blank: " + argumentName);
	}
}
