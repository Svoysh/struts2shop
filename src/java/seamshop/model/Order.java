package seamshop.model;

import static org.apache.commons.lang.StringUtils.left;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import seamshop.model.enums.OrderStatus;

/**
 * Represents customer's order.
 *
 * @see OrderItem
 * @see OrderStatus
 * @see Address
 * @see ShippingMethod
 * @see PaymentMethod
 * @see Shop
 * @see User
 *
 * @author Alex Siman 2009-02-06
 * @author Alex Siman 2009-10-09
 */
@Entity
//The "order" is SQL keyword, thus table name cannot be "order".
@Table(name = "Orders")
@SuppressWarnings("serial")
public class Order extends AbstractIdBasedEntity
{
	protected static final int NOTES_LENGTH = 1023;

	// TODO: Use as unique entity ID? (yes)
	/**
	 * Unique order number. Order number can be used in email notifications
	 * (especially to guest customer), etc.
	 * <p/>
	 * Its value depends on shop ID and current order count. E.g. if shop ID =
	 * <code>123</code> and current order count = <code>0</code>, then order
	 * number = <code>123-1</code>.
	 */
	@Column(nullable = false, updatable = false)
	private String number;

	// TODO: Need? Maybe use Address.firstName & Address.lastName? (yes)
//	private String recipientName;

	/**
	 * All order related notifications will be sent to this email.
	 */
	@Column(nullable = true)
	private String email;

	@ManyToOne
	@JoinColumn(name = "billing_address_id", nullable = false)
	@ForeignKey(name = "fk_order_billing_address_id")
	private Address billingAddress;

	/**
	 * If ID of shipping address equals to ID of billing address, then billing
	 * address will be used as shipping address.
	 */
	@ManyToOne
	@JoinColumn(name = "shipping_address_id", nullable = true)
	@ForeignKey(name = "fk_order_shipping_address_id")
	private Address shippingAddress;

	@ManyToOne
	@JoinColumn(name = "shipping_method_id", nullable = true, updatable = true)
	@ForeignKey(name = "fk_order_shipping_method_id")
	private ShippingMethod shippingMethod;

	@ManyToOne
	@JoinColumn(name = "payment_method_id", nullable = true, updatable = true)
	@ForeignKey(name = "fk_order_payment_method_id")
	private PaymentMethod paymentMethod;

	/** Copy of <code>shippingMethod.name</code> at the moment of order creation. */
	@Column(name = "shipping_method_name", nullable = true, updatable = false)
	private String shippingMethodName;

	/** Copy of <code>shippingMethod.price</code> at the moment of order creation. */
	@Column(name = "shipping_price", nullable = true, updatable = false)
	// TODO: Low: Rename to "shippingMethodPrice"? (n)
	private BigDecimal shippingPrice;

	/** Copy of <code>paymentMethod.name</code> at the moment of order creation. */
	@Column(name = "payment_method_name", nullable = true, updatable = false)
	private String paymentMethodName;

	@Column(name = "buyer_notes", length = NOTES_LENGTH)
	private String buyerNotes;

	@Column(name = "seller_notes", length = NOTES_LENGTH)
	private String sellerNotes;

	/**
	 * <code>true</code> if order has been paid by customer.
	 */
	private boolean paid = false;

	/*
	 * TODO: Can order has custom status value (given by seller), not from
	 * {@link OrderStatus}?
	 */
	private OrderStatus status = OrderStatus.NEW;

	/**
	 * Copy of {@link #shop}'s currency at the time this order has been placed.
	 * Order must has its own currency, because shop or product currency can be
	 * changed any time.
	 */
	// TODO: Rename to currencyCode? (y)
	@Column(name = "currency_code", nullable = false)
	private String currency;

	/** Order total price. */
	@Column(nullable = false, updatable = false)
	private BigDecimal totalPrice;

	/** Count of items in order. */
	@Column(nullable = false, updatable = false)
	private Integer itemCount;

	/** Sum of quantities of every order item. */
	@Column(nullable = false, updatable = false)
	private Integer totalQuantity;

	@OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	/** Copy of <code>shop.name</code> at the moment of order creation. */
	@Column(name = "shop_name", nullable = false, updatable = false)
	private String shopName;

	@ManyToOne
	@JoinColumn(name = "shop_id", nullable = true, updatable = true)
	@ForeignKey(name = "fk_order_shop_id")
	private Shop shop;

	/**
	 * Can be <code>null</code> if order created by guest (not registered user).
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true, updatable = false)
	@ForeignKey(name = "fk_order_user_id")
	// TODO: Rename to "buyer"? (y)
	private User customer;

	public Order()
	{}

	/**
	 * @param shopId ID of shop which products placed to order.
	 * @param orderCountByShop Summary count of all orders by shop with ID
	 *     <code>== shopId</code>.
	 *
	 * @return Generated number of new order.
	 */
	public String generateNumber(Long shopId, Long orderCountByShop)
	{
		// TODO: Low: Throw exception if number is already set? (n)
		number = shopId + "-" + (orderCountByShop + 1);
		return number;
	}

	public boolean hasShippingMethod()
	{
		return null != shippingMethod;
	}

	public boolean hasPaymentMethod()
	{
		return null != paymentMethod;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Address getBillingAddress()
	{
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress()
	{
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

	public ShippingMethod getShippingMethod()
	{
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod)
	{
		this.shippingMethod = shippingMethod;
		shippingMethodName = shippingMethod.getName();
		shippingPrice = shippingMethod.getPrice();
	}

	public PaymentMethod getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod)
	{
		this.paymentMethod = paymentMethod;
		paymentMethodName = paymentMethod.getName();
	}

	public String getShippingMethodName()
	{
		return shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName)
	{
		this.shippingMethodName = shippingMethodName;
	}

	public BigDecimal getShippingPrice()
	{
		return shippingPrice;
	}

	public void setShippingPrice(BigDecimal shippingPrice)
	{
		this.shippingPrice = shippingPrice;
	}

	public String getPaymentMethodName()
	{
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName)
	{
		this.paymentMethodName = paymentMethodName;
	}

	public String getBuyerNotes()
	{
		return buyerNotes;
	}

	public void setBuyerNotes(String buyerNotes)
	{
		this.buyerNotes = left(buyerNotes, NOTES_LENGTH);
	}

	public String getSellerNotes()
	{
		return sellerNotes;
	}

	public void setSellerNotes(String sellerNotes)
	{
		this.sellerNotes = left(sellerNotes, NOTES_LENGTH);
	}

	public boolean getPaid()
	{
		return paid;
	}

	public void setPaid(boolean paid)
	{
		this.paid = paid;
	}

	public OrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(OrderStatus status)
	{
		this.status = status;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public BigDecimal getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public Integer getItemCount()
	{
		return itemCount;
	}

	public void setItemCount(Integer itemCount)
	{
		this.itemCount = itemCount;
	}

	public Integer getTotalQuantity()
	{
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity)
	{
		this.totalQuantity = totalQuantity;
	}

	public Set<OrderItem> getOrderItems()
	{
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems)
	{
		this.orderItems = orderItems;
	}

	public Shop getShop()
	{
		return shop;
	}

	public void setShop(Shop shop)
	{
		this.shop = shop;
	}

	public String getShopName()
	{
		return shopName;
	}

	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

	public User getCustomer()
	{
		return customer;
	}

	public void setCustomer(User customer)
	{
		this.customer = customer;
	}
}
