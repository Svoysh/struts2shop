package seamshop.actionutil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;

import seamshop.dao.GenericDao;
import seamshop.dto.BaseDto;
import seamshop.util.CollectionUtils;

/**
 * Represents an information about currently browsing page of the webapp.
 * It can contain such info like: current page number, max results, page title,
 * bread crumbs, etc.
 *
 * @author Alex Siman 2009-04-25
 * @author Alex Siman 2009-06-09
 */
// TODO: Low: Replace all long-s by int-s? (y)
// TODO: Low: Convert all primitives (int, long, etc.) to Objects? (y)
@SuppressWarnings("serial")
public class Pager extends BaseDto
{
	public static final int DEFAULT_PAGE_NUMBER = 1;
	public static final int DEFAULT_MAX_RESULTS = GenericDao.DEFAULT_MAX_RESULTS;

	// Preffered odd values: 7, 9, etc.
	public static final int DEFAULT_SIZE_OF_PAGE_LIST_FOR_PRINT = 9;

	public static final Integer[] DEFAULT_MAX_RESULTS_ARRAY = {5, 10, 20, 50, 100, 200};

	// DO NOT access this field directly even in this class!
	private int page = DEFAULT_PAGE_NUMBER;

	private int results = 0;

	private int maxResults = DEFAULT_MAX_RESULTS;

	private long allResults = DEFAULT_MAX_RESULTS;

	public Set<Integer> maxResultsSet = new TreeSet<Integer>(
		Arrays.asList(DEFAULT_MAX_RESULTS_ARRAY));

	/**
	 * Relative URL of page without URL parameters and request context.
	 * If pager used on such URL:
	 * <pre>http://site.com/request-context/page-url?param1=value1&amp;param2=value2</pre>
	 * where request context: <pre>/request-context</pre>
	 * and URL parameters: <pre>param1=value1&amp;param2=value2</pre>
	 * then <code>Pager.url</code>: <pre>/page-url</pre>
	 */
	private String url;

	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

	// TODO: Impl: "sort", "order".
	// TODO: Impl: Bread crumbs.

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" +
			"page=" + getPage() + "; " +
			"pages=" + getPages() + "; " +
			"results=" + results + "; " +
			"maxResults=" + maxResults + "; " +
			"allResults=" + allResults +
		"}";
	}

	/**
	 * @return number of current page.
	 */
	public int getPage()
	{
		int page = this.page;
		if (page < 1)
		{
			page = 1;
		}

		return page;
	}

	public void setPage(int page)
	{
		if (page < 1)
		{
			page = 1;
		}
		this.page = page;
	}

	public void setPage(Integer page)
	{
		if (page != null)
		{
			setPage(page.intValue());
		}
	}

	// TODO: Rename to "isFirstPage()".
	public boolean isFirst()
	{
		return getPage() == 1;
	}

	// TODO: Rename to "isLastPage()".
	public boolean isLast()
	{
		return getPage() == getPages();
	}

	// TODO: getPrevPage() {return getPage() - 1;}

	// TODO: getNextPage() {return getPage() + 1;}

	/**
	 * @return count of all pages due to value of all results.
	 */
	public long getPages()
	{
		long pages = allResults / maxResults;
		if ((allResults % maxResults) != 0)
		{
			pages++;
		}

		return pages;
	}

	// TODO: Performance: Cache this method? (y, if carefully)
	public Set<Integer> getPageListForPrint()
	{
		Set<Integer> result = new TreeSet<Integer>();

		int pageCount = Long.valueOf(getPages()).intValue();
		int page = getPage();
		int fullSize = DEFAULT_SIZE_OF_PAGE_LIST_FOR_PRINT;
		int leftSize = fullSize / 2;
		int rightSize = fullSize - leftSize - 1;

		int beginPage = 1;
		int endPage = pageCount;
		if (fullSize < pageCount)
		{
			if (page <= (1 + leftSize))
			{
				endPage = fullSize;
			}
			else if (page >= (pageCount - rightSize))
			{
				beginPage = pageCount - fullSize + 1;
			}
			else
			{
				beginPage = page - leftSize;
				endPage = page + rightSize;
			}
		}
		for (int i = beginPage; i <= endPage; i++)
		{
			result.add(Integer.valueOf(i));
		}

		return result;
	}

	public int getFirstResultNumber()
	{
		int firstResultNumber = ((getPage() - 1) * maxResults) + 1;
		if (firstResultNumber < 1)
		{
			firstResultNumber = 1;
		}
		else if (firstResultNumber > allResults)
		{
			firstResultNumber = Long.valueOf(allResults).intValue();
		}

		return firstResultNumber;
	}

	public int getLastResultNumber()
	{
		int lastResultNumber = getFirstResultNumber() + results - 1;
		if (lastResultNumber < 1)
		{
			lastResultNumber = 1;
		}
		else if (lastResultNumber > allResults)
		{
			lastResultNumber = Long.valueOf(allResults).intValue();
		}

		return lastResultNumber;
	}

	/**
	 * @return count of all results.
	 */
	public int getResults()
	{
		return results;
	}

	public void setResults(int results)
	{
		this.results = results;
	}

	public int getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(int maxResults)
	{
		this.maxResults = maxResults;
		maxResultsSet.add(Integer.valueOf(maxResults));
	}

	public void setMaxResults(Integer maxResults)
	{
		if (maxResults != null)
		{
			setMaxResults(maxResults.intValue());
		}
	}

	public int getDefaultMaxResults()
	{
		return DEFAULT_MAX_RESULTS;
	}

	public Set<Integer> getMaxResultsList()
	{
		return maxResultsSet;
	}

	public long getAllResults()
	{
		return allResults;
	}

	public void setAllResults(long allResults)
	{
		this.allResults = allResults;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * Alias for {@link #getUrlWithParameters()} to be used in OGNL expressions.
	 */
	public String getUrlWithParams()
	{
		return getUrlWithParameters();
	}

	public String getUrlWithParameters()
	{
		StringBuilder urlWithParams = new StringBuilder();
		urlWithParams.append(getUrl());
		if (!CollectionUtils.isNullOrEmpty(getParameters()))
		{
			urlWithParams.append('?');
			for (Entry<String, Object> paramAndValue : getParameters().entrySet())
			{
				urlWithParams.append(paramAndValue.getKey());
				urlWithParams.append('=');
				urlWithParams.append(paramAndValue.getValue());
				urlWithParams.append('&');
			}

			// Such as after adding parameters to URL, we will got URL like:
			// "http://site.com/url?param1=value1&paramn=valuen&"
			// then we need to remove the last '&' char.
			int lastAmpIndex = urlWithParams.length() - 1;
			urlWithParams.deleteCharAt(lastAmpIndex);
		}

		return urlWithParams.toString();
	}

	/**
	 * Alias for {@link #getParameters()} to be used in OGNL expressions.
	 */
	public Map<String, Object> getParams()
	{
		return getParameters();
	}

	public Map<String, Object> getParameters()
	{
		return parameters;
	}

	/**
	 * Alias for {@link #setParameter(String, Object)} to be used in OGNL expressions.
	 */
	public void setParam(String paramName, Object paramValue)
	{
		setParameter(paramName, paramValue);
	}

	public void setParameter(String paramName, Object paramValue)
	{
		if (paramValue instanceof String)
		{
			paramValue = StringEscapeUtils.escapeHtml((String) paramValue);
		}
		parameters.put(paramName, paramValue);
	}
}
