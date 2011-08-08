package seamshop.dao;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static seamshop.dao.enums.UserRoleAgainstOrder.BUYER;
import static seamshop.dao.enums.UserRoleAgainstOrder.SELLER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import seamshop.dao.enums.UserRoleAgainstOrder;
import seamshop.model.Image;
import seamshop.model.OrderItem;
import seamshop.model.Product;
import seamshop.model.ProductVariant;

@Component
@SuppressWarnings("unchecked")
public class OrderItemDao extends GenericDao<OrderItem>
{
	@Autowired
	protected ImageDao imageDao;

	public Long countByOrderIdPlacedByMe(Long orderId)
	{
		return countByOrderIdAndCurrentUserId(orderId, BUYER);
	}

	public Long countByOrderIdPlacedForMe(Long orderId)
	{
		return countByOrderIdAndCurrentUserId(orderId, SELLER);
	}

	private Long countByOrderIdAndCurrentUserId(Long orderId, UserRoleAgainstOrder userRole)
	{
		Long count = 0L;
		if (!hasCurrentUser())
		{
			return count;
		}
		else if (orderId == null)
		{
			log.warn("Input parameter 'orderId' is null.");
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

		/*
		 * TODO: Low: Performance: Use StringBuilder instead String
		 * concatenations or extract to HQL named functions.
		 */
		String hql =
			"select count(distinct oi.id) " +
			"from " + OrderItem.class.getName() + " oi " +
			"join oi.order o " +
			"where (o.id = :orderId) and (" + userIdPath + " = :userId)";

		count = (Long) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.setLong("orderId", orderId)
			.uniqueResult();

		return count;
	}

	public List<OrderItem> getPageByOrderIdPlacedByMe(Long orderId)
	{
		return getPageByOrderIdAndCurrentUserId(orderId, BUYER);
	}

	public List<OrderItem> getPageByOrderIdPlacedForMe(Long orderId)
	{
		return getPageByOrderIdAndCurrentUserId(orderId, SELLER);
	}

	private List<OrderItem> getPageByOrderIdAndCurrentUserId(
		Long orderId, UserRoleAgainstOrder userRole)
	{
		log.debug("call getOrderItemsByOrderIdAndCurrentUserId(Long orderId, UserRoleAgainstOrder userRole)");

		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		if (!hasCurrentUser())
		{
			log.warn("Current user is null.");
			return orderItems;
		}
		else if (orderId == null)
		{
			log.warn("Input parameter 'orderId' is null.");
			return orderItems;
		}
		else if (userRole == null)
		{
			log.warn("Input parameter 'userRole' is null.");
			return orderItems;
		}

		/*
		 * TODO: Low: Duplication: Extract this block to helper function say
		 * GenericDao.getUserIdPathByUserAttentionToOrder();
		 * See OrderDao and OrderItemDao for duplications.
		 */
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
			"select oi " +
			"from " + OrderItem.class.getSimpleName() + " oi " +
			"join fetch oi.order o " +
//			"join fetch o.shop s " +
			"join fetch oi.productVariant pv " +
			"join fetch pv.product p " +
			"where (o.id = :orderId) and (" + userIdPath + " = :userId) " +
			"order by oi.productName, oi.productVariantName";

		orderItems = createPagedQuery(hql)
			.setLong("orderId", orderId)
			.setLong("userId", getCurrentUserId())
			.list();

		// The end if there are no items in this order (does not make sense!).
		if (isEmpty(orderItems))
		{
			return orderItems;
		}

		loadTitleImagesOfProducts(orderItems);

		return orderItems;
	}

	public void loadTitleImagesOfProducts(Collection<OrderItem> orderItems)
	{
		if (isEmpty(orderItems))
		{
			return;
		}

		Set<Long> productIds = new HashSet<Long>();
		for (OrderItem orderItem : orderItems)
		{
			ProductVariant productVariant = orderItem.getProductVariant();
			// If product variant has not been deleted.
			if (productVariant != null)
			{
				Product product = productVariant.getProduct();
				productIds.add(product.getId());
			}
		}

		// Init title images of products.
		Map<Long, Image> titleImages = imageDao
			.getTitleImagesByProductIds(productIds);

		log.debug("titleImages: " + titleImages);

		for (OrderItem orderItem : orderItems)
		{
			ProductVariant productVariant = orderItem.getProductVariant();
			// If product variant has not been deleted.
			if (productVariant != null)
			{
				Product product = productVariant.getProduct();
				orderItem.setImage(titleImages.get(product.getId()));
				log.debug("orderItem.getImage: " + orderItem.getImage());
			}
		}
	}
}
