package seamshop.messages;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import seamshop.dto.Country;

/**
 * This is Spring's singleton.
 *
 * @author Alex Siman 2009-10-15
 */
// TODO: Spring: Make it lazy loadable.
@Component
public class CountryCollection extends ImmutableNamedCodeCollection<Country>
{
	public static final String GB = "GB";
	public static final String RU = "RU";
	public static final String UA = "UA";
	public static final String US = "US";

	public static CountryCollection getInstance()
	{
		return getInstance(CountryCollection.class);
	}

	public CountryCollection()
	{
		super();
	}

	@Override
	protected Map<String, String> getMutableCodesAndNames()
	{
		Map<String, String> mutableCodesAndNames = new HashMap<String, String>();
		mutableCodesAndNames.put(GB, "United Kingdom");
		mutableCodesAndNames.put(RU, "Russian Federation");
		mutableCodesAndNames.put(UA, "Ukraine");
		mutableCodesAndNames.put(US, "United States");

		return mutableCodesAndNames;
	}
}
