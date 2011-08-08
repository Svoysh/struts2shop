package seamshop.action.ajax;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.action.AbstractGuestAction;
import seamshop.consts.Interceptor;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Cart;
import seamshop.model.CartItem;
import seamshop.model.ProductVariant;

// TODO: Low: Create and use "ajaxStack" w/o silent login, etc.
@Component("ajaxCartAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class CartAction extends AbstractGuestAction
{
	public static final String RESULT_LAST_ADDED = "last-added";

	// TODO: Rename to "DEFAULT_QANTITY"? (n)
	// TODO: Move to Cart model? (y)
	public static final Integer MIN_QUANTITY = 1;

	private Long cartItemId;
	private Long productVariantId;
	private Integer quantity = MIN_QUANTITY;

	private List<CartItem> cartItems;

	@Override
	public void validate()
	{
		super.validate();
	}

	@SkipValidation
	@Override
	public String execute()
	{
		return lastAdded();
	}

	/**
	 * Lists last created cart items.
	 */
	@AllowedMethod
	@SkipValidation
	public String lastAdded()
	{
		log.debug("call lastAdded()");

		initLastAddedCartItems();

		return RESULT_LAST_ADDED;
	}

	/**
	 * Adds product variant to shopping cart by its ID with specified quantity.
	 */
	// TODO: Rename to "add", "createCartItem"? (xz, y)
	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String addProductVariant()
	{
		log.debug("call addProductVariant()");
		log.debug("productVariantId: " + productVariantId);
		log.debug("quantity: " + quantity);

		if (productVariantId == null)
		{
			log.error("Product variant ID is null");
			addActionError("Product not specified. Return to product view and try again.");

			initLastAddedCartItems();
//			return ERROR;
			return RESULT_LAST_ADDED;
		}

		// Product variant can be added to cart with quantity > 0 only.
		// Fix negative quantity value (if < 1) to default.
		if ((quantity == null) || (MIN_QUANTITY > quantity))
		{
			quantity = MIN_QUANTITY;
			log.debug("Quantity fixed to default value: " + quantity);
		}

		// TODO: High: Allow to use cart for not logged in user.

		// Add product variant in quantity to cart.
		try
		{
			// TODO: Refactor getting of current cart. Generalize or cache as field? (try)
			Cart cart = getCurrentCart();
			if (cart == null)
			{
				log.warn("There is no current cart. Nowhere to add product variant.");
				initLastAddedCartItems();
				return RESULT_LAST_ADDED;
			}

			CartItem cartItem = cartItemDao.getCartItemByProductVariantId(productVariantId);

			// TODO: High: Delete items from cart which product variant is not in stock already.
			// TODO: Or reduce quantity to value that is not greater than in stock quantity,
			//       if product variant availability = DISABLE_WHEN_SOLD_OUT.

			// If such product variant already exists in cart, then we add new
			// product variant quantity to already existent item quantity.
			if (cartItem != null)
			{
				Integer oldQuantity = cartItem.getQuantity();
				quantity += oldQuantity;

				log.debug("Before to update quantity of product variant (ID='" +
					productVariantId + "') from " + oldQuantity + " to " + quantity + "");
			}
			else
			{
				// FIXME: Throw an exception if trying to add product variant that is not in stock.

				ProductVariant productVariant = productVariantDao.get(productVariantId);
				cartItem = new CartItem();
				cartItem.setCart(cart);
				cartItem.setProductVariant(productVariant);

				log.debug("Before to add product variant " + productVariant + " to cart");
				entityManager.persist(cartItem);
			}
			cartItem.setQuantity(quantity);
			entityManager.flush();

			log.debug("Product added to cart successfully");
			addActionMessage("Product added to cart successfully.");

			initLastAddedCartItems();
			return RESULT_LAST_ADDED;
		}
		catch (Throwable ex)
		{
			log.error("Failed to add product variant with ID='" + productVariantId + "' to cart with UUID='TODO'");
			addActionError("Failed to add product to cart. Try again later.");

			initLastAddedCartItems();
//			return ERROR;
			return RESULT_LAST_ADDED;
		}
	}

	/**
	 * Removes product variant from shopping cart by its ID.
	 */
	// TODO: Rename to "remove", "delete"? (xz, y) See [action.CartAction.remove()].
	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	@SkipValidation
	public String deleteCartItem()
	{
		log.debug("call deleteCartItem()");
		log.debug("cartItemId: " + cartItemId);

		cartService.deleteMyCartItemById(cartItemId);
		initLastAddedCartItems();

		return RESULT_LAST_ADDED;
	}

	private void initLastAddedCartItems()
	{
		log.debug("Loading cart items for cart preview.");
		cartItems = cartItemDao.getLastAddedCartItems();
	}

	public Long getCartItemId()
	{
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId)
	{
		this.cartItemId = cartItemId;
	}

	public Long getProductVariantId()
	{
		return productVariantId;
	}

	public void setProductVariantId(Long productVariantId)
	{
		this.productVariantId = productVariantId;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public List<CartItem> getCartItems()
	{
		return cartItems;
	}
}
