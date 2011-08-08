package seamshop.service.search.filter;

/**
 * Used to filter products in search results by shop's country code.
 *
 * @author Alex Siman 2010-01-05
 */
public class ProductCountryFilterFactory extends AbstractCountryFilterFactory
{
	public static final String FILTER_NAME = "ProductCountryFilter";

	@Override
	public String getIndexedFieldName()
	{
		return "shop.countryCode";
	}
}
