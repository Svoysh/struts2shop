package seamshop.service.search.filter;

import java.util.Collection;

import org.apache.lucene.index.Term;
import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.CachingWrapperFilter;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

import seamshop.util.Log;

/**
 * @author Alex Siman 2010-01-05
 */
// TODO: Low: Extract AbstractParametrizedFilerFactory.
public abstract class AbstractCountryFilterFactory
{
	public static final String PARAM_COUNTRY_CODES = "countryCodes";

	protected final Log log = new Log(getClass());

	private Collection<String> countryCodes;

	public String getIndexedFieldName()
	{
		return "countryCode";
	}

	/** Injected parameter. */
	public void setCountryCodes(Collection<String> countryCodes)
	{
		this.countryCodes = countryCodes;
	}

	@Key
	public FilterKey getKey()
	{
		StandardFilterKey key = new StandardFilterKey();
		key.addParameter(countryCodes);
		return key;
	}

	@Factory
	public Filter getFilter()
	{
		Filter[] chain = new Filter[countryCodes.size()];
		String indexedFieldName = getIndexedFieldName();
		int i = 0;
		for (String countryCode : countryCodes)
		{
			// NOTE: countryCode needs to be in lower-case such as
			// analizer modifies any word to lower-case and
			// Lucene will be searching for exact phrase.
			countryCode = countryCode.toLowerCase();
			Query query = new TermQuery(new Term(indexedFieldName, countryCode));
			Filter filter = new QueryWrapperFilter(query);
			chain[i] = filter;
			++i;
		}
		ChainedFilter chainedFilter = new ChainedFilter(chain);
		return new CachingWrapperFilter(chainedFilter);
	}
}
