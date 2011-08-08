package seamshop.service.search;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.util.List;

import org.hibernate.search.jpa.FullTextQuery;

import seamshop.model.AbstractEntity;
import seamshop.service.search.filter.AbstractCountryFilterFactory;

/**
 * @author Alex Siman 2009-12-30
 */
public abstract class AbstractSearchServiceWithCountryCodes
	<E extends AbstractEntity<?>, SP extends AbstractSearchParamsWithCountryCodes>
	extends AbstractSearchService<E>
{
	protected abstract String getCountryFilterName();

	public SearchResult<List<E>> searchFor(String searchQuery, SP params)
	{
		FullTextQuery fullTextQuery = createFullTextQuery(searchQuery);

		List<String> countryCodes = params.getCountryCodes();
		if (!isEmpty(countryCodes))
		{
			fullTextQuery.enableFullTextFilter(getCountryFilterName())
				.setParameter(AbstractCountryFilterFactory.PARAM_COUNTRY_CODES, countryCodes);
		}

		return getSearchResult(fullTextQuery);
	}
}
