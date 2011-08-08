package seamshop.exception;

/**
 * @author Alex Siman 2009-11-02
 */
@SuppressWarnings("serial")
public class BlankCollectionArgumentException extends IllegalArgumentException
{
	public BlankCollectionArgumentException(String argumentName)
	{
		super("Collection argument is blank: " + argumentName);
	}
}
