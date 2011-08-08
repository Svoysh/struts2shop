package seamshop.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * Used to filter shops by country for user's shopping.
 *
 * @author Alex Siman 2009-12-24
 */
@Entity
@SuppressWarnings("serial")
public class ShopShoppingCountry extends AbstractEntityWithCountry
{
	@ManyToOne
	@JoinColumn(name = "shop_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_shop_shopping_country_shop_id")
	private Shop shop;

	public ShopShoppingCountry()
	{}

	public Shop getShop()
	{
		return shop;
	}

	public void setShop(Shop shop)
	{
		this.shop = shop;
	}
}
