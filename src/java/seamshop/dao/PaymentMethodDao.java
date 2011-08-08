package seamshop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import seamshop.model.PaymentMethod;

/**
 * @author Alex Siman 2009-11-09
 */
/*
 * TODO: Refactor: Duplication: Extract superclass?
 *       See [seamshop.dao.ShippingMethodDao]
 *       See [seamshop.dao.AbstractHasShopDao<E>]
 */
@Component
public class PaymentMethodDao extends AbstractHasShopDao<PaymentMethod>
{
	// TODO: Duplication: See [seamshop.dao.ShippingMethodDao]
	public PaymentMethod getMyPaymentMethodById(Long paymentMethodId)
	{
		PaymentMethod paymentMethod = null;

		if (!hasCurrentUser())
		{
			return paymentMethod;
		}

		if (paymentMethodId == null)
		{
			log.warn("Input parameter 'paymentMethodId' is null.");
			return paymentMethod;
		}

		String hql = new StringBuilder()
			.append("from ").append(PaymentMethod.class.getName()).append(" pm ")
			.append("join fetch pm.shop s ")
			.append("where (pm.id = :paymentMethodId) and (s.user.id = :userId)")
			.toString();

		paymentMethod = (PaymentMethod) createQuery(hql)
			.setLong("paymentMethodId", paymentMethodId)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();

		return paymentMethod;
	}

	// TODO: Duplication: See [seamshop.dao.ShippingMethodDao]
	public Long countMyPaymentMethods()
	{
		Long count = 0L;

		if (!hasCurrentUser())
		{
			return count;
		}

		String hql = new StringBuilder()
			.append("select count(distinct pm.id) ")
			.append("from ").append(PaymentMethod.class.getName()).append(" pm ")
			.append("where pm.shop.user.id = :userId")
			.toString();

		count = (Long) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();

		return count;
	}

	// TODO: Duplication: See [seamshop.dao.ShippingMethodDao]
	@SuppressWarnings("unchecked")
	public List<PaymentMethod> getPageOfMyPaymentMethods()
	{
		if (!hasCurrentUser())
		{
			return new ArrayList<PaymentMethod>();
		}

		String hql = new StringBuilder()
			.append("from ").append(PaymentMethod.class.getName()).append(" pm ")
			.append("join fetch pm.shop s ")
			.append("where s.user.id = :userId ")
			.append("order by pm.created desc")
			.toString();

		return createPagedQuery(hql)
			.setLong("userId", getCurrentUserId())
			.list();
	}
}
