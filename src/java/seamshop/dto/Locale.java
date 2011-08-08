package seamshop.dto;

/**
 * Represents locale code and its native name.
 *
 * @author Alex Siman 2009-12-07
 */
@SuppressWarnings("serial")
public class Locale extends NamedCode
{
	public Locale()
	{
		super();
	}

	public Locale(String code)
	{
		super(code);
	}

	public Locale(String code, String name)
	{
		super(code, name);
	}
}
