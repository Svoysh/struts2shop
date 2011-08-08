package seamshop.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.type.LongType;
import org.springframework.stereotype.Component;

import seamshop.dto.Subcart;
import seamshop.model.Cart;
import seamshop.model.CartItem;
import seamshop.model.Shop;
import seamshop.util.CollectionUtils;

@Component
@SuppressWarnings("unchecked")
public class CartDao extends GenericDao<Cart>
{
	// TODO: Cache in RequestContext? (xz) or cache by Ehcache (y)
	public Cart getCurrentCart()
	{
		return get(getCartId());
	}

	public Long countSubcarts()
	{
		Long count = 0L;
		if (!hasCurrentCart())
		{
			return count;
		}

		String hql =
			"select count(distinct ci.productVariant.product.shop.id) " +
			"from " + CartItem.class.getName() + " ci " +
			"where ci.cart.id = :cartId";

		count = (Long) createQuery(hql)
			.setLong("cartId", getCartId())
			.uniqueResult();

		return count;
	}
	/**
	 * Returns list of subcarts filtered by current cart ID and grouped by
	 * shops.
	 */
	// TODO: Rename to "getCurrentCartsByShops"? (y)
	public List<Subcart> getPageOfSubcarts()
	{
		List<Subcart> subcarts = new ArrayList<Subcart>();
		if (!hasCurrentCart())
		{
			return subcarts;
		}

		Long cartId = getCartId();

		//---------------------------------------------------------------------
		// Get page of shops by cart ID.
		String hql =
			"select s " +
			"from " + Shop.class.getName() + " s " +
			"where s.id in (" +
				"select distinct ci.productVariant.product.shop.id from " +
				CartItem.class.getSimpleName() + " ci " +
				"where ci.cart.id = :cartId" +
			") order by s.name";

		List<Shop> shops = createPagedQuery(hql)
			.setLong("cartId", cartId)
			.list();

		// If there are no shops by this cart UUID on this page.
		if (CollectionUtils.isNullOrEmpty(shops))
		{
			return subcarts;
		}

		// IDs of shops by cart UUID on this page.
		Map<Long, Shop> shopIdAndShop = new HashMap<Long, Shop>();
		List<Long> pageShopIds = new ArrayList<Long>();
		for (Shop shop : shops)
		{
			shopIdAndShop.put(shop.getId(), shop);
			pageShopIds.add(shop.getId());
		}

		//---------------------------------------------------------------------
		// Calculate subcart totals.
		hql =
			"select s.id, ci.quantity, pv.price " +
			"from " + CartItem.class.getName() + " ci " +
			"join ci.productVariant pv " +
			"join pv.product.shop s " +
			"where (ci.cart.id = :cartId) and (s.id in (:shopIds))";

		List<Object[]> totals = createQuery(hql)
			.setLong("cartId", cartId)
			.setParameterList("shopIds", pageShopIds, new LongType())
			.list();

		Map<Long, BigDecimal> shopIdAndTotal = new HashMap<Long, BigDecimal>();
		for (Object[] objects : totals)
		{
			Long shopId = (Long) objects[0];
			Integer quantity = (Integer) objects[1];
			BigDecimal price = (BigDecimal) objects[2];

			BigDecimal total = shopIdAndTotal.get(shopId);
			BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
			if (total != null)
			{
				subtotal = total.add(subtotal);
			}
			shopIdAndTotal.put(shopId, subtotal);
		}

		//---------------------------------------------------------------------
		// Count cart items and total quantity for every subcart by cart UUID
		// and shops IDs.
		hql =
			"select s.id, count(ci.id), sum(ci.quantity) " +
			"from " + CartItem.class.getName() + " ci " +
			"join ci.productVariant.product.shop s " +
			"where (ci.cart.id = :cartId) and (s.id in (:shopIds)) " +
			"group by s.id";

		List<Object[]> counts = createQuery(hql)
			.setLong("cartId", cartId)
			.setParameterList("shopIds", pageShopIds, new LongType())
			.list();

		for (Object[] objects : counts)
		{
			Long shopId = (Long) objects[0];
			Long itemCount = (Long) objects[1];
			Long totalQuantity = (Long) objects[2];
			Shop shop = shopIdAndShop.get(shopId);

			Subcart subcart = new Subcart();
			subcart.setShop(shop);
			subcart.setItemCount(itemCount);
			subcart.setTotalQuantity(totalQuantity);
			subcart.setTotalPrice(shopIdAndTotal.get(shopId));

			subcarts.add(subcart);
		}

		return subcarts;
	}
}
