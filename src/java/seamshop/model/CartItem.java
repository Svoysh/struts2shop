package seamshop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

/**
 * Represents product variant in customer's shopping cart.
 *
 * @author Alex Siman 2008-10-25
 */
@Entity
@SuppressWarnings("serial")
public class CartItem extends AbstractIdBasedEntity
{
	/** Quantity of product variant in shopping cart. */
	@Column(nullable = false)
	private Integer quantity;

	/** Product variant added to shopping cart. */
	@ManyToOne
	@JoinColumn(name = "product_variant_id", nullable = false)
	@ForeignKey(name = "fk_cart_item_product_variant_id")
	private ProductVariant productVariant;

	/** Customer's shopping cart. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	@ForeignKey(name = "fk_cart_item_product_cart_id")
	private Cart cart;

	// Helper fields ----------------------------------------------------------

	@Transient
	private String name = null;

	@Transient
	private BigDecimal price = null;

	@Transient
	// TODO: Rename to "total" or "totalPrice"? (y)
	private BigDecimal subtotal = null;

	@Transient
	private String currency = null;

	@Transient
	private Image image;

	@Transient
	private Product product;

	/*
	 * TODO: Store shop reference (denormalization) to simplify SQLs,
	 * related to fetching of shops by cart.
	 */
	@Transient
	private Shop shop;

	public CartItem()
	{
		quantity = 1;
	}

	public String getName()
	{
		if (name == null)
		{
			// TODO: Print with HTML dash symbol: &#150; (short dash) or &#151; (long dash).
			name = getProduct().getName() + " - " +
				productVariant.getName();
		}
		return name;
	}

	public BigDecimal getPrice()
	{
		if (price == null)
		{
			price = productVariant.getPrice();
		}
		return price;
	}

	public BigDecimal getSubtotal()
	{
		if (subtotal == null)
		{
			subtotal = productVariant.calculateSubtotal(quantity);
		}
		return subtotal;
	}

	public String getCurrency()
	{
		if (currency == null)
		{
			currency = getShop().getCurrency();
		}
		return currency;
	}

	public Product getProduct()
	{
		if (product == null)
		{
			product = productVariant.getProduct();
		}
		return product;
	}

	public Shop getShop()
	{
		if (shop == null)
		{
			shop = getProduct().getShop();
		}
		return shop;
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public ProductVariant getProductVariant()
	{
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant)
	{
		this.productVariant = productVariant;
	}

	public Cart getCart()
	{
		return cart;
	}

	public void setCart(Cart cart)
	{
		this.cart = cart;
	}
}
