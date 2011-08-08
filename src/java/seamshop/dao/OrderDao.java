package seamshop.dao;

import static seamshop.dao.enums.UserRoleAgainstOrder.BUYER;
import static seamshop.dao.enums.UserRoleAgainstOrder.SELLER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import seamshop.dao.enums.UserRoleAgainstOrder;
import seamshop.model.Order;

@Component
@SuppressWarnings("unchecked")
public class OrderDao extends GenericDao<Order>
{
	/**
	 * Count orders by shop ID.
	 */
	public Long countByShopId(Long shopId)
	{
		Long count = 0L;
		if (shopId == null)
		{
			log.warn("Input parameter 'shopId' is null.");
			return count;
		}

		String hql =
			"select count(distinct o.id) " +
			"from " + Order.class.getName() + " o " +
			"where o.shop.id = :shopId";

		count = (Long) createQuery(hql)
			.setLong("shopId", shopId)
			.uniqueResult();

		return count;
	}

	public Long countPlacedByMe()
	{
		return countByCurrentUserId(BUYER);
	}

	public Long countPlacedForMe()
	{
		return countByCurrentUserId(SELLER);
	}

	private Long countByCurrentUserId(UserRoleAgainstOrder userRole)
	{
		Long count = 0L;
		if (!hasCurrentUser())
		{
			log.warn("Current user is null.");
			return count;
		}
		else if (userRole == null)
		{
			log.warn("Input parameter 'userRole' is null.");
			return count;
		}

		String userIdPath = ":userId";
		if (userRole == BUYER)
		{
			userIdPath = "o.customer.id";
		}
		else if (userRole == SELLER)
		{
			userIdPath = "o.shop.user.id";
		}

		String hql =
			"select count(distinct o.id) " +
			"from " + Order.class.getName() + " o " +
			"where " + userIdPath + " = :userId";

		count = (Long) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();

		return count;
	}

	public Order getByIdPlacedByMe(Long orderId)
	{
		return getByIdAndCurrentUserId(orderId, BUYER);
	}

	public Order getByIdPlacedForMe(Long orderId)
	{
		return getByIdAndCurrentUserId(orderId, SELLER);
	}

	private Order getByIdAndCurrentUserId(Long orderId, UserRoleAgainstOrder userRole)
	{
		log.debug("call getByIdAndCurrentUserId()");
		log.debug("orderId: " + orderId);

		Order order = null;
		if (!hasCurrentUser())
		{
			log.warn("Current user is null.");
			return order;
		}
		else if (orderId == null)
		{
			log.warn("Input parameter 'orderId' is null.");
			return order;
		}
		else if (userRole == null)
		{
			log.warn("Input parameter 'userRole' is null.");
			return order;
		}

		String userIdPath = ":userId";
		if (userRole == BUYER)
		{
			userIdPath = "o.customer.id";
		}
		else if (userRole == SELLER)
		{
			userIdPath = "o.shop.user.id";
		}

		String hql =
			"from " + Order.class.getName() + " o " +
			"join fetch o.billingAddress ba " +
			"join fetch o.shippingAddress sa " +
			"join fetch o.shop s " +
			"where (o.id = :orderId) and (" + userIdPath + " = :userId)";

		return (Order) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.setLong("orderId", orderId)
			.uniqueResult();
	}

	public List<Order> getPagePlacedByMe()
	{
		return getPageByCurrentUserId(BUYER);
	}

	public List<Order> getPagePlacedForMe()
	{
		return getPageByCurrentUserId(SELLER);
	}

	private List<Order> getPageByCurrentUserId(UserRoleAgainstOrder userRole)
	{
		log.debug("call getPageByCurrentUserId()");

		List<Order> orders = new ArrayList<Order>();
		if (!hasCurrentUser())
		{
			log.warn("Current user is null.");
			return orders;
		}
		else if (userRole == null)
		{
			log.warn("Input parameter 'userRole' is null.");
			return orders;
		}

		String userIdPath = ":userId";
		if (userRole == BUYER)
		{
			userIdPath = "o.customer.id";
		}
		else if (userRole == SELLER)
		{
			userIdPath = "o.shop.user.id";
		}

		/*
		 * TODO: Low: Performance: Use StringBuilder instead String
		 * concatenations or extract to HQL named functions.
		 */
		String hql = "from " + Order.class.getSimpleName() + " o " +
			"where " + userIdPath + " = :userId " +
			"order by o.created desc";

		return createPagedQuery(hql)
			.setLong("userId", getCurrentUserId())
			.list();
	}
}
