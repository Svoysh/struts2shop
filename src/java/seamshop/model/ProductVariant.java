package seamshop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * Represents product variant (variation).
 * <p/>
 * NOTE: Prices must be of {@link BigDecimal} type because of not fixed
 * precision of {@link Float} or {@link Double} types after math operations.
 *
 * @see Product
 *
 * @author Alex Siman 2009-01-12
 * @author Alex Siman 2009-12-22
 */
@Entity
@SuppressWarnings("serial")
public class ProductVariant extends AbstractNamedEntity
{
	@Column
	private boolean featured = false;

	/** Selling price of product variant. */
	@Column(nullable = false)
	private BigDecimal price = BigDecimal.ZERO;

	/** Old (previous) price of product variant. */
	@Column(name = "old_price", nullable = true)
	private BigDecimal oldPrice;

	// TODO: Refactor to "int" type.
	/** Current quantity of product variant in stock. Can be negative value. */
	@Column
	private Integer quantity = 0;

	@Column
	private boolean inStock = true;

	@Column
	private boolean traceStock = false;

	@Column
	private boolean soldOutable = false;

	/*
	 * TODO: Remove "number" field and implement ProductVariant as linked list
	 * (with prev and next ProductVariant).
	 */
	/**
	 * Serial number of variant in <code>Product.productVariants</code> list.
	 * Starts from 0.
	 */
	@Column
	private Integer number = 0;

	/** Product that this variant belongs to. */
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	@ForeignKey(name = "fk_product_variant_product_id")
	private Product product;

	public ProductVariant()
	{}

	public String getSummary()
	{
		return getProduct().getSummary();
	}

	public boolean hasSummary()
	{
		return getProduct().hasSummary();
	}

	public String getHtmlDescription()
	{
		return getProduct().getHtmlDescription();
	}

	/**
	 * Shortcut for {@ #getHtmlDescription()}
	 */
	public String getHtmlDesc()
	{
		return getHtmlDescription();
	}

	public String getTextDescription()
	{
		return getProduct().getTextDescription();
	}

	/**
	 * Shortcut for {@ #getTextDescription()}
	 */
	public String getTextDesc()
	{
		return getTextDescription();
	}

	/**
	 * @return <code>true</code> if product variant can be ordered/purchased.
	 */
	public boolean canBeOrdered()
	{
		return inStock && (!soldOutable || (soldOutable && (quantity > 0)));
	}

	public boolean needQuantity()
	{
		return inStock && traceStock;
	}

	public boolean isInStock()
	{
		return inStock;
	}

	public void setInStock(boolean inStock)
	{
		this.inStock = inStock;
	}

	public boolean isTraceStock()
	{
		return traceStock;
	}

	public void setTraceStock(boolean traceStock)
	{
		this.traceStock = traceStock;
	}

	public boolean isSoldOutable()
	{
		return soldOutable;
	}

	public void setSoldOutable(boolean soldOutable)
	{
		this.soldOutable = soldOutable;
	}

	public String getCurrency()
	{
		return product.getCurrency();
	}

	public Image getTitleImage()
	{
		return product.getTitleImage();
	}

	public void setTitleImage(Image titleImage)
	{
		product.setTitleImage(titleImage);
	}

	/**
	 * Used by cart item to calculate subtotal price of product variant in cart.
	 *
	 * @param quantity how many units of this product variant.
	 */
	public BigDecimal calculateSubtotal(Integer quantity)
	{
		return price.multiply(BigDecimal.valueOf(quantity));
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("id=").append(getId()).append("; ")
				.append("name=").append(getName()).append("; ")
				.append("price=").append(price).append("; ")
				.append("oldPrice=").append(oldPrice).append("; ")
				.append("featured=").append(featured).append("; ")
				.append("number=").append(number)
			.append("}")
			.toString();
	}

	public String getFullName()
	{
		return product.getName() + " - " + getName();
	}

	public boolean isFeatured()
	{
		return featured;
	}

	public void setFeatured(boolean featured)
	{
		this.featured = featured;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getOldPrice()
	{
		return oldPrice;
	}

	public void setOldPrice(BigDecimal oldPrice)
	{
		this.oldPrice = oldPrice;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}
}
