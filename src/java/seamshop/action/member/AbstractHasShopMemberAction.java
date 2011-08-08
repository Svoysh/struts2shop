package seamshop.action.member;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seamshop.dao.DaoUtils;
import seamshop.model.AbstractEntity;
import seamshop.model.Shop;
import seamshop.util.CollectionUtils;
import seamshop.util.Json;

/**
 * @author Alex Siman 2009-11-05
 */
@SuppressWarnings("serial")
public abstract class AbstractHasShopMemberAction
	<E extends AbstractEntity<ID>, ID extends Serializable>
	extends AbstractCrudMemberAction<E, ID>
{
	protected Long shopId;
	private List<Shop> shops;
	private Map<Long, String> shopIdToCurrencyMap = null;

	public Long getShopId()
	{
		if ((shopId == null) && (getShops() != null) && (getShops().size() == 1))
		{
			// If seller has only one shop, then try to get ID of this shop.
			Shop shop = CollectionUtils.getFirstNotNull(getShops());
			if (shop != null)
			{
				shopId = shop.getId();
				log.debug("Pre-selecting sellers' single shop with ID: " + shopId);
			}
		}

		return shopId;
	}

	public void setShopId(Long shopId)
	{
		this.shopId = shopId;
	}

	/**
	 * Returns selected shop by shop ID from add/edit model form.
	 */
	public Shop getShop()
	{
		// TODO: Cache result (in "shop" variable)? (maybe, test)
		return DaoUtils.findById(getShops(), shopId);
	}

	/**
	 * Returns all shops created by this user (seller).
	 */
	public List<Shop> getShops()
	{
		if (shops == null)
		{
			log.debug("Loading seller shops...");
			shops = shopDao.getAllMyShops();
		}

		return shops;
	}

	public String getShopIdToCurrencyJsonMap()
	{
		String result = "{}";
		if (null == shopIdToCurrencyMap)
		{
			shopIdToCurrencyMap = new HashMap<Long, String>();
			for (Shop shop : shops)
			{
				shopIdToCurrencyMap.put(shop.getId(), shop.getCurrencyCode());
			}
			result = Json.to(shopIdToCurrencyMap);
		}
		return result;
	}

	protected boolean hasShopChanged()
	{
		return (shopId != null) && !(shopId.equals(getCurrentShop().getId()));
	}

	@Override
	protected void saveOrUpdate()
	{
		boolean changeShop = false;
		if (isNewEntity())
		{
			changeShop = true;
		}
		else if (hasShopChanged())
		{
			log.debug("Updating shop to another selected...");
			changeShop = true;
		}

		if (changeShop)
		{
			setChangedShop(getShop());
		}

		super.saveOrUpdate();
	}

	/**
	 * @return Shop of persistent model (product, shipping method, etc.).
	 */
	protected abstract Shop getCurrentShop();

	/**
	 * Set newly selected shop on entity.
	 *
	 * @param changedShop Newly selected shop.
	 */
	protected abstract void setChangedShop(Shop changedShop);
}
