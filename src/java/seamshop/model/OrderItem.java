package seamshop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.ForeignKey;

/**
 * Represents product variant as item in customer's order. Order item must has
 * own product info, copied from product variant at the moment of order
 * creation, such as product and productVariant info can be changed at any time.
 *
 * @author Alex Siman 2009-02-06
 * @author Alex Siman 2009-07-09
 */
@Entity
@SuppressWarnings("serial")
public class OrderItem extends AbstractIdBasedEntity
{
	/** Copy of <code>productVariant.product.name</code> at the moment of order creation. */
	@Column(name = "product_name", nullable = false, updatable = false)
	private String productName;

	/** Copy of <code>productVariant.name</code> at the moment of order creation. */
	@Column(name = "product_variant_name", nullable = true, updatable = false)
	private String productVariantName;

	/** Copy of <code>productVariant.price</code> at the moment of order creation. */
	@Column(nullable = false, updatable = false)
	// TODO: Low: Rename to "productVariantPrice"? (n)
	private BigDecimal price;

	/**
	 * Ordered product variant.
	 * <p/>
	 * NOTE: Do not relay on this {@link #productVariant} for reading of
	 * product price or product name, because {@link #productVariant} or
	 * its <code>product</code> can be deleted any time by seller. Use
	 * {@link #productName}, {@link #productVariantName}, {@link #price}
	 * instead. These fields contain info copied from {@link #productVariant}
	 * at the time of {@link #order} creation.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = true, updatable = true)
	@ForeignKey(name = "fk_order_item_product_variant_id")
	private ProductVariant productVariant;

	/** Quantity of ordered product variant. */
	@Column(nullable = false, updatable = false)
	private Integer quantity = 1;

	/** Customer's order. */
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_order_item_order_id")
	private Order order;

	// Helper fields ----------------------------------------------------------

	@Transient
	private Image image;

	// Helper methods ---------------------------------------------------------

	public String getName()
	{
		String name = productName;
		if (!StringUtils.isBlank(productVariantName))
		{
			// TODO: Print with HTML dash symbol: &#150; (short dash) or &#151; (long dash).
			name += " - " + productVariantName;
		}

		return name;
	}

	public BigDecimal getSubtotal()
	{
		return price.multiply(BigDecimal.valueOf(quantity));
	}

	public String getCurrency()
	{
		return getOrder().getCurrency();
	}

	public Product getProduct()
	{
		if (productVariant != null)
		{
			return productVariant.getProduct();
		}
		else
		{
			return null;
		}
	}

	public Shop getShop()
	{
		return getOrder().getShop();
	}

	public String getShopName()
	{
		Shop shop = getOrder().getShop();
		if (shop != null)
		{
			return shop.getName();
		}
		else
		{
			return getOrder().getShopName();
		}
	}

	// Getters/Setters --------------------------------------------------------

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getProductVariantName()
	{
		return productVariantName;
	}

	public void setProductVariantName(String productVariantName)
	{
		this.productVariantName = productVariantName;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public ProductVariant getProductVariant()
	{
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant)
	{
		this.productVariant = productVariant;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}
}
