package seamshop.dao;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import seamshop.model.CartItem;
import seamshop.model.Image;
import seamshop.model.Product;

@Component
@SuppressWarnings("unchecked")
public class CartItemDao extends GenericDao<CartItem>
{
	@Autowired
	protected ImageDao imageDao;

	// TODO: Low: Rename to "getCurrentCartItemsByIds"? (y)
	// TODO: Low: Optimize: replace using by "scrollCartItems()"? (y) Or use pager. (n)
	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	@Override
	public <ID extends Serializable> List<CartItem> getBatch(Collection<ID> cartItemsIds)
	{
		if (!hasCurrentCart() || CollectionUtils.isEmpty(cartItemsIds))
		{
			return new ArrayList<CartItem>();
		}

		String hql = "from " + CartItem.class.getSimpleName() + " " +
			"where (cart.id = :cartId) and (id in (:cartItemsIds))";

		// TODO: Limit to max result limit? (xz, y) Or scroll.
		return createQuery(hql)
			.setLong("cartId", getCartId())
			.setParameterList("cartItemsIds", cartItemsIds)
			.list();
	}

	// TODO: Low: Rename to "deleteCurrentCartItemsByIds"? (y)
	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	@Override
	public <ID extends Serializable> void deleteBatch(Collection<ID> cartItemsIds)
	{
		if (!hasCurrentCart() || CollectionUtils.isEmpty(cartItemsIds))
		{
			return;
		}

		/*
		 * TODO: Enhancement: implement query in multiple iterations with count
		 * of elements in IN-clause <= ENTITIES_PER_SESSION_FLUSH or
		 * MAX_RESULTS_LIMIT
		 */
		String hql = "delete " + CartItem.class.getSimpleName() + " " +
			"where (cart.id = :cartId) and (id in (:cartItemsIds))";

		createQuery(hql)
			.setLong("cartId", getCartId())
			.setParameterList("cartItemsIds", cartItemsIds)
			.executeUpdate();
	}

	// TODO: Need this method? (n) See getAllCurrentCartItemsByShopId().
	// TODO: Rename to "scrollAllCurrentCartItemsByShopId"? (y)
	public ScrollableResults scrollCartItems(Long shopId)
	{
		if (hasCartItemsFromShop(shopId))
		{
			return null;
		}

		// Get cart items by cart ID and shop ID.
		String hql = "select ci from " + CartItem.class.getSimpleName() + " ci " +
			"join fetch ci.productVariant pv " +
			"join fetch pv.product p " +
			"join p.shop s " +
			"where (ci.cart.id = :cartId) and (s.id = :shopId)";

		return createQuery(hql)
			.setLong("cartId", getCartId())
			.setLong("shopId", shopId)
			.setCacheMode(CacheMode.IGNORE)
			.scroll(ScrollMode.FORWARD_ONLY);
	}

	// FIXME: Issue: such as count of cart items can be > 200
	//        (GenericDao.MAX_RESULTS_LIMIT), there too must be used
	//        Iterator to iterate all of cart items in shopping cart
	//        or increase MAX_RESULTS_LIMIT -> 1000.
	public List<CartItem> getAllByIdsForCheckout(Collection<Long> ids)
	{
		if (isEmpty(ids))
		{
			return new ArrayList<CartItem>();
		}

		String hql = new StringBuilder()
			.append("select ci from ").append(CartItem.class.getSimpleName()).append(" ci ")
			.append("join fetch ci.productVariant pv ")
			.append("join fetch pv.product p ")
			.append("where (ci.id in (:ids))")
			.toString();

		return createQuery(hql)
			.setParameterList("ids", ids, new LongType())
			.setCacheMode(CacheMode.IGNORE)
			.list();
	}

	private boolean hasCartItemsFromShop(Long shopId)
	{
		return (shopId == null) || !hasCurrentCart();
	}

	public Map<Long, Integer> getIdAndQuantityMapByShopId(Long shopId)
	{
		Map<Long, Integer> result = new HashMap<Long, Integer>();
		if (hasCartItemsFromShop(shopId))
		{
			return new HashMap<Long, Integer>();
		}

		String hql = new StringBuilder()
			.append("select ci.id, ci.quantity ")
			.append("from ").append(CartItem.class.getName()).append(" ci ")
			.append("join ci.productVariant.product.shop s ")
			.append("where (ci.cart.id = :cartId) and (s.id = :shopId)")
			.toString();

		List<Object[]> list = createQuery(hql)
			.setLong("cartId", getCartId())
			.setLong("shopId", shopId)
			.list();

		for (Object[] varIdAndQuantity : list)
		{
			Long id = (Long) varIdAndQuantity[0];
			Integer quantity = (Integer) varIdAndQuantity[1];
			result.put(id, quantity);
		}

		return result;
	}

	// TODO: Rename to "getCurrentCartItemByProductVariantId"? (y)
	public CartItem getCartItemByProductVariantId(Long productVariantId)
	{
		if (!hasCurrentCart() || (productVariantId == null))
		{
			return null;
		}

		String hql = "from " + CartItem.class.getSimpleName() + " " +
			"where (cart.id = :cartId) and (productVariant.id = :productVariantId)";

		return (CartItem) createQuery(hql)
			.setLong("cartId", getCartId())
			.setLong("productVariantId", productVariantId)
			.uniqueResult();
	}

	// TODO: Rename to "getLastAddedCurrentCartItems"? (y)
	public List<CartItem> getLastAddedCartItems()
	{
		return getCartItems();
	}

	// TODO: Rename to "getAllCurrentCartItems"? (y)
	public List<CartItem> getCartItems()
	{
		return this.getCartItems(null);
	}

	/**
	 * Return cart items filtered by cart UUID and shop ID.
	 * <p/>
	 * If <code>shopId == null</code> then returns all cart items from cart with
	 * UUID <code>== cartId</code>.
	 */
	// TODO: Rename to "getCurrentCartItemsByShopId"? (y)
	public List<CartItem> getCartItems(Long shopId)
	{
		List<CartItem> cartItems = new ArrayList<CartItem>();
		if (!hasCurrentCart())
		{
			return cartItems;
		}

		// FIXME: Delete items from cart which product variant is not in stock already.

		String hql = "select ci from " + CartItem.class.getSimpleName() + " ci " +
			"join fetch ci.productVariant pv " +
			"join fetch pv.product p " +
			"join fetch p.shop s " +
			"where (ci.cart.id = :cartId) " +
			(shopId == null ? "" : "and (s.id = :shopId) ") +
			"order by ci.created desc";

		Query query = createQuery(hql)
			.setLong("cartId", getCartId())
			.setFirstResult(0)	// TODO: Refactor: externalize. Use pager?
			.setMaxResults(10);	// TODO: Refactor: externalize. Use pager?

		cartItems =
			(shopId == null ? query : query.setLong("shopId", shopId))
			.list();

		// If there are no items in this shopping cart.
		if (isEmpty(cartItems))
		{
			return cartItems;
		}

		Set<Long> productIds = new HashSet<Long>();
		for (CartItem cartItem : cartItems)
		{
			Product product = cartItem.getProductVariant().getProduct();
			productIds.add(product.getId());
		}

		// Product title images.
		Map<Long, Image> titleImages = imageDao
			.getTitleImagesByProductIds(productIds);

		log.debug("titleImages: " + titleImages);

		for (CartItem cartItem : cartItems)
		{
			Product product = cartItem.getProductVariant().getProduct();
			cartItem.setImage(titleImages.get(product.getId()));
			log.debug("cartItem.getImage: " + cartItem.getImage());
		}

		return cartItems;
	}

	// TODO: Rename to "deleteAllCurrentCartItems"? (y)
	public void deleteCartItems()
	{
		if (!hasCurrentCart())
		{
			return;
		}

		String hql = "delete " + CartItem.class.getSimpleName() + " " +
			"where cart.id = :cartId";

		createQuery(hql)
			.setLong("cartId", getCartId())
			.executeUpdate();
	}
}
