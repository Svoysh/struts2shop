package seamshop.dto;

/**
 * Represents country code and its i18n name.
 *
 * @author Alex Siman 2009-07-06
 * @author Alex Siman 2009-10-15
 */
@SuppressWarnings("serial")
public class Country extends NamedCode
{
	public Country()
	{
		super();
	}

	public Country(String code)
	{
		super(code);
	}

	public Country(String code, String name)
	{
		super(code, name);
	}
}
