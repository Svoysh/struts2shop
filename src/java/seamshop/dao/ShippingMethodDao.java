package seamshop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import seamshop.model.ShippingMethod;

/**
 * @author Alex Siman 2009-11-05
 */
/*
 * TODO: Refactor: Duplication: Extract superclass?
 *       See [seamshop.dao.PaymentMethodDao]
 *       See [seamshop.dao.AbstractHasShopDao<E>]
 */
@Component
public class ShippingMethodDao extends AbstractHasShopDao<ShippingMethod>
{
	// TODO: Duplication: See [seamshop.dao.PaymentMethodDao]
	public ShippingMethod getMyShippingMethodById(Long shippingMethodId)
	{
		ShippingMethod shippingMethod = null;

		if (!hasCurrentUser())
		{
			return shippingMethod;
		}

		if (shippingMethodId == null)
		{
			log.warn("Input parameter 'shippingMethodId' is null.");
			return shippingMethod;
		}

		String hql = new StringBuilder()
			.append("from ").append(ShippingMethod.class.getName()).append(" sm ")
			.append("join fetch sm.shop s ")
			.append("where (sm.id = :shippingMethodId) and (s.user.id = :userId)")
			.toString();

		shippingMethod = (ShippingMethod) createQuery(hql)
			.setLong("shippingMethodId", shippingMethodId)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();

		return shippingMethod;
	}

	// TODO: Duplication: See [seamshop.dao.PaymentMethodDao]
	public Long countMyShippingMethods()
	{
		Long count = 0L;

		if (!hasCurrentUser())
		{
			return count;
		}

		String hql = new StringBuilder()
			.append("select count(distinct sm.id) ")
			.append("from ").append(ShippingMethod.class.getName()).append(" sm ")
			.append("where sm.shop.user.id = :userId")
			.toString();

		count = (Long) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();

		return count;
	}

	// TODO: Duplication: See [seamshop.dao.PaymentMethodDao]
	@SuppressWarnings("unchecked")
	public List<ShippingMethod> getPageOfMyShippingMethods()
	{
		if (!hasCurrentUser())
		{
			return new ArrayList<ShippingMethod>();
		}

		String hql = new StringBuilder()
			.append("from ").append(ShippingMethod.class.getName()).append(" sm ")
			.append("join fetch sm.shop s ")
			.append("where s.user.id = :userId ")
			.append("order by sm.created desc")
			.toString();

		return createPagedQuery(hql)
			.setLong("userId", getCurrentUserId())
			.list();
	}
}
