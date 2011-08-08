package seamshop.action.checkout;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;
import seamshop.context.util.Checkout;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.model.Address;
import seamshop.model.CartItem;
import seamshop.model.Order;
import seamshop.model.OrderItem;
import seamshop.model.PaymentMethod;
import seamshop.model.Product;
import seamshop.model.ProductVariant;
import seamshop.model.ShippingMethod;
import seamshop.model.Shop;

/**
 * @author Alex Siman 2009-11-10
 */
@Component("checkoutReviewOrderAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	/*@Result(
		name = Action.INPUT,
		location = "review-order.jsp"
	),
	@Result(
		name = SUCCESS,
		type = REDIRECT_ACTION,
		params = {"actionName", "place-order", PARAM_CHECKOUT_ID, "${checkoutId}"}
	),*/
})
@SuppressWarnings("serial")
public class ReviewOrderAction extends AbstractCheckoutInProcessAction
	implements BeforeValidationAware
{
	@SkipValidation
	@Override
	public String execute()
	{
		try
		{
			log.debug("call execute()");

			// FIXME: Issue: such as count of cart items can be > 200
			//        (GenericDao.MAX_RESULTS_LIMIT), there too must be used
			//        Iterator to iterate all of cart items in shopping cart
			//        or increase MAX_RESULTS_LIMIT -> 1000.
			//        or use DB scroller.

			Checkout checkout = getCheckout();
			Long shopId = checkout.getShopId();
			Shop shop = shopDao.get(shopId);
			if (shop == null)
			{
				log.warn("Shop not fount by shopId: " + shopId);
				// TODO: i18n:
				addActionError("Shop you are trying to checkout from does not " +
					"exist or was removed.");
				return ERROR;
			}

			// Create new order and setup its fields.
			BigDecimal totalPrice = new BigDecimal("0.00");
			Order order = new Order();
			order.setCustomer(getCurrentUser());
			order.setShop(shop);
			order.setCurrency(shop.getCurrency());
			// TODO: Maybe set shopName when shop is deleted, to have up-to-date value?
			//       (n, interesting but hard to impl./trace)
			order.setShopName(shop.getName());

			Address billingAddress = addressDao.get(checkout.getBillingAddressId());
			order.setBillingAddress(billingAddress);

			if (checkout.isBillingAsShipping())
			{
				order.setShippingAddress(billingAddress);
			}
			else
			{
				Address shippingAddress = addressDao.get(checkout.getShippingAddressId());
				order.setShippingAddress(shippingAddress);
			}

			if (checkout.hasShippingMethod())
			{
				ShippingMethod shippingMethod = shippingMethodDao.get(
					checkout.getShippingMethodId());
				order.setShippingMethod(shippingMethod);
				totalPrice = totalPrice.add(shippingMethod.getPrice());
			}

			if (checkout.hasPaymentMethod())
			{
				PaymentMethod paymentMethod = paymentMethodDao.get(
					checkout.getPaymentMethodId());
				order.setPaymentMethod(paymentMethod);
			}

			List<CartItem> cartItems = cartItemDao.getAllByIdsForCheckout(
				checkout.getCartItemIds());
			if (isEmpty(cartItems))
			{
				// TODO: i18n
				addActionError("There are no products in your cart. " +
					"Add some products to your cart then come back to checkout.");
				return ERROR;
			}

			Set<OrderItem> orderItems = new HashSet<OrderItem>();
			Integer totalQuantity = 0;
			for (CartItem cartItem : cartItems)
			{
				Product product = cartItem.getProduct();
				ProductVariant productVariant = cartItem.getProductVariant();

				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(order);
				orderItem.setProductVariant(productVariant);
				orderItem.setProductName(product.getName());
				orderItem.setProductVariantName(productVariant.getName());
				orderItem.setPrice(productVariant.getPrice());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItems.add(orderItem);

				// TODO: Extract counting of totalPrice and totalQuantity to Order
				//       as business logic as part of methods? (mb) example:
				//       Order.setShippingMethod() + Order.addOrderItem();

				totalQuantity += orderItem.getQuantity();

				// FORMULA: totalPrice += (orderItem.price * orderItem.quantity);
				totalPrice = totalPrice.add(
					orderItem.getPrice().multiply(
						BigDecimal.valueOf(orderItem.getQuantity())
					)
				);
			}

			order.setOrderItems(orderItems);
			order.setItemCount(orderItems.size());
			order.setTotalQuantity(totalQuantity);
			order.setTotalPrice(totalPrice);
			checkout.setOrder(order);

			orderItemDao.loadTitleImagesOfProducts(orderItems);

			// TODO: Set flag in Checkout to indicate that order items has been loaded
			//       and do not load them on next "Review order" or page refresh? (mb)

			return SUCCESS;
		}
		catch (Exception ex)
		{
			log.error("Failed to review order.", ex);
			// TODO: i18n
			addActionError("Failed to review order. Please, try again later.");
			return ERROR;
		}
	}

	public Order getOrder()
	{
		return getCheckout().getOrder();
	}
}
