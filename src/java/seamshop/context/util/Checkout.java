package seamshop.context.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.NullArgumentException;

import seamshop.context.SessionContext;
import seamshop.model.Order;
import seamshop.util.UuidGenerator;

import com.google.common.collect.ImmutableMap;

/**
 * @see SessionContext
 *
 * @author Alex Siman 2009-11-09
 */
@SuppressWarnings("serial")
public class Checkout implements Serializable
{
	/** ID generated in constructor. */
	private final String id;

	private final Long shopId;

	/**
	 * key - cart item ID.
	 * value - product variant quantity.
	 */
	private final ImmutableMap<Long, Integer> cartItems;

	private Order order;
//	private List<OrderItem> orderItems;

	private Long billingAddressId;
	private Long shippingAddressId;

	private Long shippingMethodId;
	private Long paymentMethodId;

	public Checkout(Long shopId, Map<Long, Integer> cartItems)
	{
		if (shopId == null)
		{
			throw new NullArgumentException("shopId");
		}
		else if (cartItems == null)
		{
			throw new NullArgumentException("cartItems");
		}
		else if (MapUtils.isEmpty(cartItems))
		{
			throw new IllegalArgumentException("cartItems is empty.");
		}

		id = UuidGenerator.generateRandomUuid();
		this.shopId = shopId;
//		order = new Order();
		this.cartItems = ImmutableMap.copyOf(cartItems);
	}

	public Collection<Long> getCartItemIds()
	{
		return cartItems.keySet();
	}

	public String getId()
	{
		return id;
	}

	public Long getShopId()
	{
		return shopId;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public Order getOrder()
	{
		return order;
	}

	public ImmutableMap<Long, Integer> getCartItems()
	{
		return cartItems;
	}

	public Long getBillingAddressId()
	{
		return billingAddressId;
	}

	public void setBillingAddressId(Long billingAddressId)
	{
		this.billingAddressId = billingAddressId;
	}

	public Long getShippingAddressId()
	{
		return shippingAddressId;
	}

	public void setShippingAddressId(Long shippingAddressId)
	{
		this.shippingAddressId = shippingAddressId;
	}

	public boolean isBillingAsShipping()
	{
		return billingAddressId.equals(shippingAddressId);
	}

	public boolean hasShippingMethod()
	{
		return null != shippingMethodId;
	}

	public Long getShippingMethodId()
	{
		return shippingMethodId;
	}

	public void setShippingMethodId(Long shippingMethodId)
	{
		this.shippingMethodId = shippingMethodId;
	}

	public boolean hasPaymentMethod()
	{
		return null != paymentMethodId;
	}

	public Long getPaymentMethodId()
	{
		return paymentMethodId;
	}

	public void setPaymentMethodId(Long paymentMethodId)
	{
		this.paymentMethodId = paymentMethodId;
	}
}
