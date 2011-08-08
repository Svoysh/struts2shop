package seamshop.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * Represents a payment method of shop(s).
 *
 * @author Alex Siman 2009-10-09
 */
// TODO: Low: Future: Impl: (PaymentMethod *-* Shop)? (mb)
@Entity
@SuppressWarnings("serial")
public class PaymentMethod extends AbstractHtmlDescribedEntity
{
	// TODO: Low: Impl: Use as filter in HQLs when fetching products.
	// TODO: Low: Name: "hidden = false", "enabled = true", etc.
//	private boolean hidden = false;

	@ManyToOne
	@JoinColumn(name = "shop_id", nullable = false, updatable = true)
	@ForeignKey(name = "fk_payment_method_shop_id")
	private Shop shop;

	// Helper fields ----------------------------------------------------------

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("id=").append(getId()).append("; ")
				.append("name=").append(getName())
			.append("}")
			.toString();
	}

	// Getters/Setters. -------------------------------------------------------

	public Shop getShop()
	{
		return shop;
	}

	public void setShop(Shop shop)
	{
		this.shop = shop;
	}
}
