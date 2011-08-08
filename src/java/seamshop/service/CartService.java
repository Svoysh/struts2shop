package seamshop.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import seamshop.model.CartItem;
import seamshop.util.CollectionUtils;

/**
 * @author Alex Siman 2009-07-30
 */
@Component
public class CartService extends AbstractService
{
	public void deleteMyCartItemById(Long cartItemId)
	{
		Map<Long, Integer> itemsQuantities = new HashMap<Long, Integer>();
		itemsQuantities.put(cartItemId, 0);
		updateMyCartItemsQuantities(itemsQuantities);
	}

	/**
	 * Update cart items to new quantities.
	 *
	 * @param itemQuantities:
	 * key - ID of CartItem.
	 * value - new quantity of CartItem.
	 */
	public void updateMyCartItemsQuantities(Map<Long, Integer> cartItemIdsAndQuantities)
	{
		log.debug("call updateCartItems(Map<Long, Integer> cartItemIdsAndQuantities)");
		log.debug("itemsQuantities: " + cartItemIdsAndQuantities.toString());

		// If there is nothing to update.
		if (CollectionUtils.isNullOrEmpty(cartItemIdsAndQuantities))
		{
			log.debug("There is nothing to update: all new quantities of cart items are empty.");
			return;
		}

		// If there is no cart.
		if (getCurrentCart() == null)
		{
			log.debug("There is nothing to update: there is no cart.");
			return;
		}

		// Add IDs with quantity <= 0 to set of IDs of cart items to delete,
		// and IDs with quantity > 0 to set of IDs of cart items to update.
		Set<Long> cartItemsIdsToUpdate = new HashSet<Long>();
		Set<Long> cartItemsIdsToDelete = new HashSet<Long>();
		for (Entry<Long, Integer> cartItemEntry : cartItemIdsAndQuantities.entrySet())
		{
			Long cartItemId = cartItemEntry.getKey();
			Integer newQuantity = cartItemEntry.getValue();
			if (newQuantity <= 0)
			{
				cartItemsIdsToDelete.add(cartItemId);
			}
			else
			{
				cartItemsIdsToUpdate.add(cartItemId);
			}
		}

		// Delete cart items with new quantity <= 0.
		if (!CollectionUtils.isNullOrEmpty(cartItemsIdsToDelete))
		{
			log.debug("Delete cart items with IDs in (" + cartItemsIdsToDelete + ").");
			// FIXME: Authority: Delete only owned cart items.
			cartItemDao.deleteBatch(cartItemsIdsToDelete);
		}

		// Update only that cart items whose quantities are > 0.
		if (!CollectionUtils.isNullOrEmpty(cartItemsIdsToUpdate))
		{
			log.debug("Update cart items with IDs in (" + cartItemsIdsToDelete + ") to new quantities.");

			// Get batch of cart items to update.
			// FIXME: Authority: Update only owned cart items.
			List<CartItem> cartItems = cartItemDao.getBatch(cartItemsIdsToUpdate);
			for (CartItem cartItem : cartItems)
			{
				Long cartItemId = cartItem.getId();
				Integer newQuantity = cartItemIdsAndQuantities.get(cartItemId);
				if (!newQuantity.equals(cartItem.getQuantity()))
				{
					cartItem.setQuantity(newQuantity);
				}
			}
		}

		entityManager.flush();
		log.debug("Cart items updated successfully. JPA session flushed.");
	}
}
