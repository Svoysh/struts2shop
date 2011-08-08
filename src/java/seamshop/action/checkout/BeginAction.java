package seamshop.action.checkout;

import static seamshop.action.checkout.AbstractAddressAction.ACTION_BILLING_ADDRESS;
import static seamshop.action.checkout.AbstractCheckoutAction.PARAM_CHECKOUT_ID;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.context.util.Checkout;

import com.opensymphony.xwork2.Action;

@Component("checkoutBeginAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.INPUT,
		type = ResultType.REDIRECT_ACTION,
		params = {"namespace", "/", "actionName", "cart!subcarts"}
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", ACTION_BILLING_ADDRESS, PARAM_CHECKOUT_ID, "${checkoutId}"}
	),
})
@SuppressWarnings("serial")
public class BeginAction extends AbstractCheckoutAction
{
	private String checkoutId = null;
	private Long shopId;

	@Override
	public void validate()
	{
		super.validate();

		// TODO: Move to XML validation? (n)
		if (shopId == null)
		{
			log.warn("Request param 'shopId' is null.");
			// TODO: i18n: Externalize message.
			// TODO: Print link to cart page.
			addActionError("Return to the cart page and try again to begin checkout.");
		}
	}

	@Override
	public String execute()
	{
		try
		{
			log.debug("call execute()");

			// TODO: Fill Order and OrderItems here or in ReviewOrder action? (think)

			// FIXME: Delete items from cart, those product variant is not
			//        in stock already.

			// FIXME: Issue: such as count of cart items can be > 200
			//        (GenericDao.MAX_RESULTS_LIMIT), there too must be used
			//        Iterator to iterate all of cart items in shopping cart
			//        or increase MAX_RESULTS_LIMIT -> 1000.

			// key - cart item ID.
			// value - product variant quantity.
			Map<Long, Integer> cartItems = cartItemDao
				.getIdAndQuantityMapByShopId(shopId);

			// If there are no items in this shopping cart from this shop.
			if (MapUtils.isEmpty(cartItems))
			{
				log.warn("Cart items not found.");
				// TODO: i18n
				// TODO: Detalize message.
				addActionError("Seems like your shopping cart is empty.");
				return ERROR;
			}

			Checkout newCheckout = new Checkout(shopId, cartItems);
			sessionContext.getCheckoutList().add(newCheckout);

			// Checkout ID used by every page in Checkout Wizard.
			checkoutId = newCheckout.getId();

			return SUCCESS;
		}
		catch (Exception ex)
		{
			log.error("Failed to begin checkout.", ex);
			// TODO: i18n
			addActionError("Failed to begin checkout. Please, try again later.");
			return ERROR;
		}
	}

	public String getCheckoutId()
	{
		return checkoutId;
	}

	public Long getShopId()
	{
		return shopId;
	}

	public void setShopId(Long shopId)
	{
		this.shopId = shopId;
	}
}
