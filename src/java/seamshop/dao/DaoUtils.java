package seamshop.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import seamshop.model.AbstractEntity;
import seamshop.util.CollectionUtils;

/**
 * Helper class to work with loaded entities.
 *
 * @author Alex Siman 2009-04-05
 * @author Alex Siman 2009-07-03
 */
// TODO: Move static fields to AbstractDao? (mb)
@Component
@SuppressWarnings("unchecked")
public class DaoUtils
{
	/**
	 * Creates List<ID> from collection of Entities.
	 */
	public static <ID extends Serializable, T extends AbstractEntity<ID>>
		List<ID> getIdsOfEntities(Collection<T> entities)
	{
		List<ID> ids = new ArrayList<ID>();
		if (CollectionUtils.isNullOrEmpty(entities))
		{
			return ids;
		}

		for (T entity : entities)
		{
			ids.add(entity.getEntityId());
		}

		return ids;
	}

	/**
	 * Creates Map<ID, Entity> from collection of Entities.
	 */
	public static <ID extends Serializable, T extends AbstractEntity<ID>>
		Map<ID, T> getMapOfIdsAndEntities(Collection<T> entities)
	{
		Map<ID, T> map = new HashMap<ID, T>();
		if (CollectionUtils.isNullOrEmpty(entities))
		{
			return map;
		}

		for (T entity : entities)
		{
			map.put(entity.getEntityId(), entity);
		}

		return map;
	}

	/**
	 * Returns first-found entity from collection with specified ID.
	 */
	public static <ID extends Serializable, T extends AbstractEntity<ID>>
		T findById(Collection<T> entities, ID id)
	{
		if ((id == null) || CollectionUtils.isNullOrEmpty(entities))
		{
			return null;
		}

		for (T entity : entities)
		{
			if (id.equals(entity.getEntityId()))
			{
				return entity;
			}
		}

		return null;
	}

	// Object fields and methods ----------------------------------------------

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * Simply calls <code>entityManager.flush()</code>.
	 */
	public void flush()
	{
		entityManager.flush();
	}

	/**
	 * Simply calls <code>entityManager.clear()</code>.
	 */
	public void clear()
	{
		entityManager.clear();
	}

	/**
	 * Flush a batch of saves/updates and release memory.
	 */
	public void flushAndClear()
	{
		flush();
		clear();
	}

	/**
	 * Determines whether current persistence context must be flushed and cleared
	 * if count of saved entities reached max number of entities per session
	 * flush.
	 *
	 * @param processedEntities count of entities loaded into JPA session
	 * 	during this transaction.
	 */
	public void flushAndClearIfNeeded(int processedEntities)
	{
		if (processedEntities % GenericDao.ENTITIES_PER_SESSION_FLUSH == 0)
		{
			flushAndClear();
		}
	}
}
