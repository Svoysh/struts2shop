package seamshop.dao;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.hibernate.criterion.Restrictions.eq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import seamshop.actionutil.Pager;
import seamshop.context.RequestContext;
import seamshop.context.SessionContext;
import seamshop.model.AbstractEntity;
import seamshop.model.Shop;
import seamshop.model.User;
import seamshop.util.ClassUtils;
import seamshop.util.CollectionUtils;
import seamshop.util.Log;

/*
 * TODO: CRUD methods must be reviewed. Use methods from HibernateEntityController.
 * TODO: Optimize: Change type of all "query" variables from String to StringBuilder
 *       or move to NamedQueries? (y)
 */
// TODO: Low: Rename to "AbstractDao"? (mb)
/**
 * @author Alex Siman 2008
 * @author Alex Siman 2009-10-15
 */
@SuppressWarnings("unchecked")
public abstract class GenericDao<E extends AbstractEntity>
{
	public static final int DEFAULT_FIRST_RESULT = 0;
	public static final int DEFAULT_MAX_RESULTS = 20;
	public static final int MAX_RESULTS_LIMIT = 200;

	/**
	 * Value must be the same as the JDBC batch size (set in Hibernate
	 * properties).
	 */
	public static final int ENTITIES_PER_SESSION_FLUSH = 20;

	/**
	 * Return a row number for Hibernate pagination.
	 * First number is <code>0</code>.
	 */
	public static int getFirstResult(Pager pager)
	{
		int defaultResult = GenericDao.DEFAULT_FIRST_RESULT;
		if (pager == null)
		{
			return defaultResult;
		}

		int pageNumber = pager.getPage();
		if (pageNumber <= 1)
		{
			return defaultResult;
		}
		else
		{
			return (pageNumber - 1) * getMaxResults(pager);
		}
	}

	/**
	 * Returns max results per page of results for Hibernate pagination.
	 */
	public static int getMaxResults(Pager pager)
	{
		int defaultResult = GenericDao.DEFAULT_MAX_RESULTS;
		if (pager == null)
		{
			return defaultResult;
		}

		int pageMaxResults = pager.getMaxResults();
		if (pageMaxResults <= 0)
		{
			return defaultResult;
		}
		else if (pageMaxResults > GenericDao.MAX_RESULTS_LIMIT)
		{
			return GenericDao.MAX_RESULTS_LIMIT;
		}
		else
		{
			return pageMaxResults;
		}
	}

	// Object fields and methods ----------------------------------------------

	protected final Log log = new Log(getClass());

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	protected DaoUtils daoUtils;

	@Autowired
	protected SessionContext sessionContext;

	@Autowired
	protected RequestContext requestContext;

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

	protected String getEntityClassName()
	{
		return getEntityClass().getSimpleName();
	}

	protected Pager getPager()
	{
		return requestContext.getPager();
	}

	/**
	 * NOTE: Do not cache instance of current user!
	 */
	public User getCurrentUser()
	{
		return sessionContext.getUser();
	}

	/**
	 * NOTE: Do not cache instance of current user ID!
	 */
	public Long getCurrentUserId()
	{
		return getCurrentUser() == null ? null : getCurrentUser().getId();
	}

	// TODO: Rename to "isLoggedIn"? (xz)
	public boolean hasCurrentUser()
	{
		return getCurrentUser() != null;
	}

	/*public boolean isLoggedIn()
	{
		return hasCurrentUser();
	}*/

	public Long getCartId()
	{
		return hasCurrentUser() ? getCurrentUser().getCart().getId() : null;
	}

	public boolean hasCurrentCart()
	{
		return getCartId() != null;
	}

	protected int getFirstResult()
	{
		return getFirstResult(getPager());
	}

	protected int getMaxResults()
	{
		return getMaxResults(getPager());
	}

	protected EntityManager getEntityManager()
	{
		return entityManager;
	}

	protected Session getSession()
	{
		return (Session) entityManager.getDelegate();
	}

	protected Query createQuery(String hql)
	{
		return getSession().createQuery(hql);
	}

	protected Criteria createCriteria()
	{
		return getSession().createCriteria(getEntityClass());
	}

	protected Query createPagedQuery(String hql)
	{
		log.debug("call createPagedQuery(String hql)");
		log.debug("firstResult: " + getFirstResult());
		log.debug("maxResults: " + getMaxResults());

		return createQuery(hql)
			.setFirstResult(getFirstResult())
			.setMaxResults(getMaxResults());
	}

	public void detach(E entity)
	{
		getSession().evict(entity);
	}

	public long count()
	{
		String hql = "select count(*) from " + getEntityClassName();
		Long count = (Long) createQuery(hql).uniqueResult();

		return count.longValue();
	}

	public <ID extends Serializable> void delete(ID id)
	{
		if (id == null)
		{
			log.warn("Input parameter 'id' is null.");
		}
		else
		{
			Collection<ID> idsToDelete = new HashSet<ID>();
			idsToDelete.add(id);
			deleteBatch(idsToDelete);
		}
	}

	public void delete(Collection<E> entities)
	{
		Collection<Serializable> idsToDelete = new HashSet<Serializable>();
		for (E entity : entities)
		{
			Serializable entityId = entity.getEntityId();
			if (entityId != null)
			{
				idsToDelete.add(entityId);
			}
		}
		deleteBatch(idsToDelete);
	}

	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	public <ID extends Serializable> void deleteBatch(Collection<ID> ids)
	{
		if (CollectionUtils.isNullOrEmpty(ids))
		{
			return;
		}

		CollectionUtils.removeNulls(ids);

		/*
		 * TODO: Enhancement: implement query in multiple iterations with count
		 * of elements in IN-clause <= ENTITIES_PER_SESSION_FLUSH or
		 * MAX_RESULTS_LIMIT
		 */
		String hql = "delete " + getEntityClassName() +
			" where id in (:ids)";

		createQuery(hql)
			.setParameterList("ids", ids)
			.executeUpdate();
	}

	public void deleteAll()
	{
		String hql = "delete " + getEntityClassName();
		createQuery(hql).executeUpdate();
	}

	public <ID extends Serializable> E get(ID id)
	{
		// Returns null if not found.
		return (E) getSession().get(getEntityClass(), id);

		// Throwing an exception if not found.
		// NOTE: load() method used in  Hibernate's CaveatEmptor-native application.
//		return load(getEntityClass(), id);
	}

	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	public <ID extends Serializable> List<E> getBatch(Collection<ID> ids)
	{
		if (CollectionUtils.isNullOrEmpty(ids))
		{
			return new ArrayList<E>();
		}

		String hql = "from " + getEntityClassName() +
			" where id in (:ids)";

		// TODO: Limit to max result limit? (xz)
		return createQuery(hql)
			.setParameterList("ids", ids)
			.list();
	}

	public List<E> getPage(int pageNumber, int maxResults)
	{
		Pager pager = getPager();
		pager.setPage(pageNumber);
		pager.setMaxResults(maxResults);

		return getPage();
	}

	public List<E> getPage()
	{
		log.debug("call getPage()");
		log.debug("pager.page: " + getPager().getPage());
		log.debug("pager.maxResults: " + getPager().getMaxResults());

		String hql = "from " + getEntityClassName();
		return createPagedQuery(hql).list();
	}

	public List<E> getAll()
	{
		log.debug("call getAll()");
		log.warn("Using of getAll() method is not recomended. Use getPage() instead.");

		String hql = "from " + getEntityClassName();
		return createQuery(hql).list();
	}

	// TODO: Divide into 2 methods: "save" and "update".
	public void saveOrUpdate(E entity)
	{
		getSession().saveOrUpdate(entity);

		// TODO: Refactor to JPA
		/*if (entity.getEntityId() == null)
		{
			entityManager.persist(entity);
		}
		entityManager.flush();*/
	}

	// TODO: Remove as unneeded? (y) Use entityManager.flush instead.
	public void saveOrUpdateBatch(Collection<E> entityBatch)
	{
		int savedEntities = 0;
		for (E entity : entityBatch)
		{
			saveOrUpdate(entity);
			savedEntities++;
			daoUtils.flushAndClearIfNeeded(savedEntities);
		}
	}

	// Utility methods --------------------------------------------------------

	public boolean isUniqueCriteria(Criteria criteria)
	{
		return 1 > (Integer) criteria
			.setProjection(Projections.rowCount())
			.setFlushMode(FlushMode.MANUAL)
			.uniqueResult();
	}
}
