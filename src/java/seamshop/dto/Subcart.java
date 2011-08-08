package seamshop.dto;

import java.math.BigDecimal;

import seamshop.model.Shop;

/**
 * Represents subcart, that contains information about product items added
 * to shopping cart and related to certain shop.
 *
 * @author Alex Siman 2009-02-16
 * @author Alex Siman 2009-12-16
 */
@SuppressWarnings("serial")
public class Subcart extends BaseDto
{
	private Shop shop;
	private long itemCount = 0;
	private long totalQuantity = 0;
	private BigDecimal totalPrice = null;

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("shopId=").append(shop.getId()).append("; ")
				.append("shopName=").append(shop.getName()).append("; ")
				.append("currencyCode=").append(getCurrency()).append("; ")
				.append("itemCount=").append(itemCount).append("; ")
				.append("totalQuantity=").append(totalQuantity).append("; ")
				.append("totalPrice=").append(totalPrice)
			.append("}")
			.toString();
	}

	public Shop getShop()
	{
		return shop;
	}

	public void setShop(Shop shop)
	{
		this.shop = shop;
	}

	public String getCurrency()
	{
		return shop.getCurrency();
	}

	public long getItemCount()
	{
		return itemCount;
	}

	public void setItemCount(long itemCount)
	{
		this.itemCount = itemCount;
	}

	public long getTotalQuantity()
	{
		return totalQuantity;
	}

	public void setTotalQuantity(long totalQuantity)
	{
		this.totalQuantity = totalQuantity;
	}

	public BigDecimal getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice)
	{
		this.totalPrice = totalPrice;
	}
}
