package seamshop.service.search;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Siman 2009-12-31
 */
public abstract class AbstractSearchParamsWithCountryCodes
{
	private List<String> countryCodes = new ArrayList<String>();

	public List<String> getCountryCodes()
	{
		return countryCodes;
	}

	public void setCountryCodes(List<String> countryCodes)
	{
		this.countryCodes = countryCodes;
	}
}
