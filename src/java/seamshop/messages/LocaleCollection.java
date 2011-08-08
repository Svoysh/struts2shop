package seamshop.messages;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import seamshop.dto.Locale;

/**
 * This is Spring's singleton.
 *
 * @author Alex Siman 2009-12-07
 */
// TODO: Spring: Make it lazy loadable.
@Component
public class LocaleCollection extends ImmutableNamedCodeCollection<Locale>
{
	public static LocaleCollection getInstance()
	{
		return getInstance(LocaleCollection.class);
	}

	public LocaleCollection()
	{
		super();
	}

	@Override
	protected Map<String, String> getMutableCodesAndNames()
	{
		Map<String, String> mutableCodesAndNames = new HashMap<String, String>();
		mutableCodesAndNames.put("en", "English");
		mutableCodesAndNames.put("ru", "Русский");

		// TODO: Low: Impl in future.
//		mutableCodesAndNames.put("en_US", "English (US)");
//		mutableCodesAndNames.put("en_GB", "English (UK)");
//		mutableCodesAndNames.put("ua", Ukrainska);

		return mutableCodesAndNames;
	}
}
