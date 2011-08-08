package seamshop.dao;

import java.util.ArrayList;
import java.util.List;

import seamshop.model.AbstractEntity;

/**
 * @see PaymentMethodDao
 * @see ShippingMethodDao
 *
 * @author Alex Siman 2009-11-10
 */
public abstract class AbstractHasShopDao<E extends AbstractEntity<?>>
	extends GenericDao<E>
{
	@SuppressWarnings("unchecked")
	public List<E> getAllByShopId(Long shopId)
	{
		if (shopId == null)
		{
			log.warn("'shopId' is null.");
			return new ArrayList<E>();
		}

		String hql = new StringBuilder()
			.append("from ").append(getEntityClassName()).append(" e ")
			.append("where e.shop.id = :shopId ")
			// TODO: Low: Order by "price" if it's Shippping method? (mb, see usability)
			.append("order by e.name")
			.toString();

		return createQuery(hql)
			.setLong("shopId", shopId)
			.list();
	}
}
