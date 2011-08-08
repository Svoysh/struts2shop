package seamshop.messages;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import seamshop.dto.Currency;

/**
 * This is Spring's singleton.
 *
 * @author Alex Siman 2009-10-15
 */
// TODO: Spring: Make it lazy loadable.
@Component
public class CurrencyCollection extends ImmutableNamedCodeCollection<Currency>
{
	public static final String EUR = "EUR";
	public static final String GBP = "GBP";
	public static final String RUB = "RUB";
	public static final String UAH = "UAH";
	public static final String USD = "USD";

	public static CurrencyCollection getInstance()
	{
		return getInstance(CurrencyCollection.class);
	}

	public CurrencyCollection()
	{
		super();
	}

	@Override
	protected Map<String, String> getMutableCodesAndNames()
	{
		Map<String, String> mutableCodesAndNames = new HashMap<String, String>();
		mutableCodesAndNames = new HashMap<String, String>();
		mutableCodesAndNames.put(EUR, "Euro");
		mutableCodesAndNames.put(GBP, "Pound sterling");
		mutableCodesAndNames.put(RUB, "Russian ruble");
		mutableCodesAndNames.put(UAH, "Ukrainian hryvnia");
		mutableCodesAndNames.put(USD, "US dollar");

		return mutableCodesAndNames;
	}
}
