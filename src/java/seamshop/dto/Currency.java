package seamshop.dto;

/**
 * Represents currency code and its i18n name.
 *
 * @author Alex Siman 2009-05-27
 * @author Alex Siman 2009-10-15
 */
@SuppressWarnings("serial")
public class Currency extends NamedCode
{
	public Currency()
	{
		super();
	}

	public Currency(String code)
	{
		super(code);
	}

	public Currency(String code, String name)
	{
		super(code, name);
	}
}
