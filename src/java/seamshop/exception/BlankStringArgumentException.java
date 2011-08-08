package seamshop.exception;

/**
 * @author Alex Siman 2009-11-02
 */
@SuppressWarnings("serial")
public class BlankStringArgumentException extends IllegalArgumentException
{
	public BlankStringArgumentException(String argumentName)
	{
		super("String argument is blank: " + argumentName);
	}
}
