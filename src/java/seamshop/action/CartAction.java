package seamshop.action;

import static com.opensymphony.xwork2.Action.INPUT;
import static seamshop.action.CartAction.RESULT_REDIRECT_ITEMS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.RequestParam;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.dto.Subcart;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.CartItem;
import seamshop.model.Shop;

import com.opensymphony.xwork2.Action;

@Component
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = INPUT,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "cart!items", RequestParam.SHOP_ID, "${shopId}"}
	),
	@Result(
		name = RESULT_REDIRECT_ITEMS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "cart!items", RequestParam.SHOP_ID, "${shopId}"}
	),
})
@SuppressWarnings("serial")
// TODO: Refactor: Move to "action.cart" subpackage & apply its own SiteMesh decorator.
public class CartAction extends AbstractGuestAction
{
	public static final String RESULT_SUBCARTS = "subcarts";
	public static final String RESULT_ITEMS = "items";
	public static final String RESULT_REDIRECT_ITEMS = "redirect-items";

	private Long shopId;
	private Shop shop;
	private List<Subcart> subcarts;
	private List<CartItem> cartItems;

	/** Used to remove cart item from cart by this ID. */
	private Long cartItemId;

	// TODO: Low: Try to get code working w/ Map<Long, Integer> instead of two lists.
	/** Cart items IDs coupled to <code>quantities</code> via list index. */
	private List<Long> cartItemIds;

	/** New quantities to update cart items. */
	private List<Integer> quantities;

	@SkipValidation
	@Override
	public String execute()
	{
		return subcarts();
	}

	/**
	 * View subcarts of current user.
	 */
	@AllowedMethod
	@SkipValidation
	public String subcarts()
	{
		log.debug("call subcarts()");

		Long subcartCount = cartDao.countSubcarts();
		if (subcartCount > 0)
		{
			subcarts = cartDao.getPageOfSubcarts();
			getPager().setAllResults(subcartCount.intValue());
			getPager().setResults(subcarts.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_SUBCARTS;
	}

	/**
	 * View current cart items filtered by shop ID.
	 */
	@AllowedMethod
	@SkipValidation
	public String items()
	{
		log.debug("call items()");
		log.debug("shopId: " + shopId);

		if (shopId != null)
		{
			// TODO: Select shop by shopId + if it's products are in user's cart? (think)
			shop = shopDao.get(shopId);
			// TODO: Add pager.
			cartItems = cartItemDao.getCartItems(shopId);
		}
		else
		{
			// TODO: addActionMessage like: "Return to subcart view and select one (to be able) to view cart items.".
		}

		return RESULT_ITEMS;
	}

	/*
	 * TODO: High: Map result INPUT (when validation failed) to RESULT_ITEMS.
	 * (+ redirect to clean up wrong inputs? (maybe)).
	 */
	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	@SkipValidation
	public String update()
	{
		log.debug("call update()");

		Map<Long, Integer> cartItemIdsAndQuantities = new HashMap<Long, Integer>();
		for (int i = 0; i < cartItemIds.size(); i++)
		{
			Long cartItemId = cartItemIds.get(i);
			Integer quntity = quantities.get(i);
			if ((cartItemId != null) && (quntity != null))
			{
				cartItemIdsAndQuantities.put(cartItemId, quntity);
			}
		}
		cartService.updateMyCartItemsQuantities(cartItemIdsAndQuantities);

		return RESULT_REDIRECT_ITEMS;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	@SkipValidation
	public String remove()
	{
		log.debug("call remove()");
		log.debug("cartItemId: " + cartItemId);

		cartService.deleteMyCartItemById(cartItemId);

		return RESULT_REDIRECT_ITEMS;
	}

	public Long getShopId()
	{
		return shopId;
	}

	public void setShopId(Long shopId)
	{
		this.shopId = shopId;
	}

	public Shop getShop()
	{
		return shop;
	}

	// TODO: Test: Does conflict with "subcarts()" action method?
	public List<Subcart> getSubcarts()
	{
		return subcarts;
	}

	public List<CartItem> getCartItems()
	{
		return cartItems;
	}

	public void setCartItemId(Long cartItemId)
	{
		this.cartItemId = cartItemId;
	}

	public void setCartItemIds(List<Long> cartItemIds)
	{
		this.cartItemIds = cartItemIds;
	}

	public void setQuantities(List<Integer> quantities)
	{
		this.quantities = quantities;
	}
}
