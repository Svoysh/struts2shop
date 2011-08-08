package seamshop.service.search;

import org.springframework.stereotype.Component;

import seamshop.model.Product;
import seamshop.service.search.filter.ProductCountryFilterFactory;

/**
 * @author Alex Siman 2009-12-30
 */
@Component
public class ProductSearchService extends
	AbstractSearchServiceWithCountryCodes<Product, ProductSearchParams>
{
	@Override
	protected String getCountryFilterName()
	{
		return ProductCountryFilterFactory.FILTER_NAME;
	}
}
