package seamshop.action.checkout;

import static com.opensymphony.xwork2.Action.SUCCESS;
import static seamshop.consts.ResultType.REDIRECT_ACTION;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;
import seamshop.context.util.Checkout;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Order;
import seamshop.model.OrderItem;

/**
 * @author Alex Siman 2009-12-01
 */
@Component("checkoutPlaceOrderAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	/*@Result(
		name = Action.INPUT,
		location = "place-order.jsp"
	),*/
	@Result(
		name = SUCCESS,
		type = REDIRECT_ACTION,
		params = {"actionName", "end", "orderNumber", "${orderNumber}"
			/*TODO: Remove next comment? (y)*/ /*, PARAM_CHECKOUT_ID, "${checkoutId}"*/}
	),
})
@SuppressWarnings("serial")
public class PlaceOrderAction extends AbstractCheckoutInProcessAction
	implements BeforeValidationAware
{
	/**
	 * Buyer buyerNotes, ex buyerNotes about shipping, etc.
	 */
	private String buyerNotes;

	private String orderNumber;

	@Transactional(TransactionType.WRITE)
	@SkipValidation
	@Override
	public String execute()
	{
		try
		{
			log.debug("call execute()");

			Checkout checkout = getCheckout();
			Long shopId = checkout.getShopId();
			Long orderCountByShop = orderDao.countByShopId(shopId);
			Order order = checkout.getOrder();
			order.generateNumber(shopId, orderCountByShop);
			order.setBuyerNotes(buyerNotes);

			entityManager.persist(order);
			int savedItems = 0;
			for (OrderItem orderItem : order.getOrderItems())
			{
				entityManager.persist(orderItem);
				daoUtils.flushAndClearIfNeeded(++savedItems);
			}

			// Delete cart items, that have been added to this new order.
			log.debug("Deleting unneeded cart items since they transformed into order items.");
			cartItemDao.deleteBatch(checkout.getCartItemIds());

			orderNumber = order.getNumber();
			log.debug("Order placed successfully at number: " + orderNumber);

			endCheckout();

			// TODO: High: Send 2 emails to seller and buyer about ordered products.

			return SUCCESS;
		}
		catch (Exception ex)
		{
			log.error("Failed to place order.", ex);
			// TODO: i18n
			addActionError("Failed to place order. Please, try again later.");
			return ERROR;
		}
	}

	public String getBuyerNotes()
	{
		return buyerNotes;
	}

	public void setBuyerNotes(String buyerNotes)
	{
		this.buyerNotes = buyerNotes;
	}

	public String getOrderNumber()
	{
		return orderNumber;
	}
}
