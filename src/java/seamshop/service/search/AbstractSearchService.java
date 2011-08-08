package seamshop.service.search;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Component;

import seamshop.actionutil.Pager;
import seamshop.dao.GenericDao;
import seamshop.model.AbstractEntity;
import seamshop.service.AbstractService;
import seamshop.util.ClassUtils;

/**
 * @author Alex Siman 2009-09-07
 */
// TODO: Refactor to: "AbstractSearchService" like "GenericDao"? (y)
@Component
@SuppressWarnings("unchecked")
public abstract class AbstractSearchService<E extends AbstractEntity>
	extends AbstractService
{
	private MultiFieldQueryParser queryParser = null;

	/**
	 * Object class of an entity in Hibernate Session.
	 */
	private Class<E> entityClass;

	/**
	 * Get the class of the entity being managed. If not explicitly specified,
	 * the generic type of implementation is used.
	 */
	protected Class<E> getEntityClass()
	{
		if (entityClass == null)
		{
			entityClass = ClassUtils.getClassOfFirstTypeArgument(this);
		}
		return entityClass;
	}

	private Pager getPager()
	{
		return requestContext.getPager();
	}

	private int getFirstResult()
	{
		return GenericDao.getFirstResult(getPager());
	}

	private int getMaxResults()
	{
		return GenericDao.getMaxResults(getPager());
	}

	private List getPageOfResults(FullTextQuery fullTextQuery)
	{
		return fullTextQuery
			.setFirstResult(getFirstResult())
			.setMaxResults(getMaxResults())
			.getResultList();
	}

	// This method must be synchronized such as this class is singleton service.
	private synchronized MultiFieldQueryParser getQueryParser()
	{
		if (null == queryParser)
		{
			// TODO: Note: Use the same analyzer as one used for indexing entities.
			Analyzer analyzer = new StandardAnalyzer();
			E entity = ClassUtils.newInstanceOfClass(getEntityClass());
			queryParser = new MultiFieldQueryParser(
				entity.getIndexedFields(), analyzer);
		}

		return queryParser;
	}

	private Query parseQuery(String searchFor) throws ParseException
	{
		return getQueryParser().parse(searchFor);
	}

	protected FullTextQuery createFullTextQuery(String searchQuery)
	{
		FullTextQuery fullTextQuery = null;
		if (isBlank(searchQuery))
		{
			// Nothing to search for.
			return fullTextQuery;
		}

		FullTextEntityManager fullTextEntityManager =
			Search.getFullTextEntityManager(entityManager);
		try
		{
			Query luceneQuery = parseQuery(searchQuery);
			fullTextQuery = fullTextEntityManager
				.createFullTextQuery(luceneQuery, getEntityClass());
		}
		catch (ParseException pe)
		{
			pe.printStackTrace();
		}

		return fullTextQuery;
	}

	protected SearchResult<List<E>> getSearchResult(FullTextQuery fullTextQuery)
	{
		// TODO: Add filter for fields: "hidden".
		// TODO: Fetch product title images.

		List<E> result = getPageOfResults(fullTextQuery);
		int resultSize = fullTextQuery.getResultSize();

		SearchResult<List<E>> searchResult = new SearchResult<List<E>>();
		searchResult.setResult(result);
		searchResult.setResultSize(resultSize);

		return searchResult;
	}

	public SearchResult<List<E>> searchFor(String searchQuery)
	{
		FullTextQuery fullTextQuery = createFullTextQuery(searchQuery);
		return getSearchResult(fullTextQuery);
	}
}
