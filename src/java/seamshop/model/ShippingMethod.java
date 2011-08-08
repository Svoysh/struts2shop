package seamshop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * Represents a shipping method of shop(s).
 *
 * @author Alex Siman 2009-10-09
 */
// TODO: Low: Future: Impl: (ShippingMethod *-* Shop)? (mb)
// TODO: Future: Impl: Weight-based rate. Ex: 10kg - 50kg
// TODO: Future: Impl: Total-price-based rate. Ex: 10 EUR - 50 EUR
// TODO: Future: Impl: Product-count-based rate? (mb)
@Entity
@SuppressWarnings("serial")
public class ShippingMethod extends AbstractHtmlDescribedEntity
{
	// TODO: Low: Impl: Use as filter in HQLs when fetching products.
	// TODO: Low: Name: "hidden = false", "enabled = true", etc.
//	private boolean hidden = false;

	@Column(nullable = false)
	private BigDecimal price = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "shop_id", nullable = false, updatable = true)
	@ForeignKey(name = "fk_shipping_method_shop_id")
	private Shop shop;

	// Helper fields ----------------------------------------------------------

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("id=").append(getId()).append("; ")
				.append("name=").append(getName()).append("; ")
				.append("price=").append(getPrice()).append(" ").append(getCurrency())
			.append("}")
			.toString();
	}

	public String getCurrency()
	{
		return shop.getCurrency();
	}

	// Getters/Setters. -------------------------------------------------------

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public Shop getShop()
	{
		return shop;
	}

	public void setShop(Shop shop)
	{
		this.shop = shop;
	}
}
