package seamshop.action;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.RequestParam;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.model.Category;
import seamshop.model.Product;
import seamshop.model.ProductVariant;
import seamshop.model.Shop;

@Component
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class ShopAction extends AbstractGuestAction
{
	public static final String ACTION_NAME = "shop";

	public static final String RESULT_LIST = "list";
	public static final String RESULT_PRODUCTS = "products";

	// TODO: Low: Rename to "id"? (mb, y)
	private Long shopId;
	private String urlName;

	// TODO: Rafactor: Use categoryId instead of categoryName to filter shop's products.
	// TODO: Use "categoryName" to filter products in shop.
	private String categoryName;

	private Shop shop = null;
	private List<Shop> shops;
	private List<Product> products;
	private List<ProductVariant> featuredProductVariants;
	private List<Category> categories;

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");
		if (requestedCertainShop())
		{
			return view();
		}
		else
		{
			return list();
		}
	}

	@AllowedMethod
	@SkipValidation
	public String view()
	{
		log.debug("call view()");

		shop = loadShop();

		// TODO: Duplication: Extract to Dao? (mb)
		Long productCount = productDao.getProductCountByShopId(shopId);
		shop.setProductCount(productCount);

		// TODO: What about MAX_RESULTS? Add pager? (mb)
		categories = categoryDao.getAllCategoriesByShopId(shopId);

		// TODO: Fix: Load featured product variants instead of products.
		// TODO: What about MAX_RESULTS? Add pager? (mb)
		featuredProductVariants = productVariantDao.getAllFeaturedProductsByShopId(shopId);

		return SUCCESS;
	}

	/**
	 * Print list of shops.
	 */
	// TODO: Filter shops by country (use subdomains).
	@AllowedMethod
	@SkipValidation
	public String list()
	{
		log.debug("call list()");

		// TODO: Count shops by country.
		Long shopCount = shopDao.count();
		if (shopCount > 0)
		{
			shops = shopDao.getPage();

			/*
			 * TODO: Refactor: Duplication: extract to helper method like:
			 * createPager(List entities, long allResults);
			 */
			getPager().setAllResults(shopCount.intValue());
			getPager().setResults(shops.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_LIST;
	}

	/**
	 * Lists products from shop with specified shop ID.
	 */
	@AllowedMethod
	@SkipValidation
	public String products()
	{
		log.debug("call products()");
		log.debug("shopId: " + shopId);

		if (!requestedCertainShop())
		{
			// TODO: i18n: Externalize message.
			addActionError("Such shop does not exist.");
			return ERROR;
		}

		shop = loadShop();
		if (shop == null)
		{
			log.debug("Shop not fount by shopId: " + shopId);
			// TODO: i18n: Externalize message.
			addActionError("Such shop does not exist or was removed.");
			return ERROR;
		}

		Long productCount = productDao.getProductCountByShopId(shopId);
		if (productCount > 0)
		{
			shop.setProductCount(productCount);
			// TODO: Fix: Load product variants instead of products.
			products = productDao.getPageOfProductsByShopId(shopId);
			getPager().setAllResults(productCount.intValue());
			getPager().setResults(products.size());
			getPager().setUrl(getPagerUrl());
			getPager().setParameter(RequestParam.SHOP_ID, shopId);

			// TODO: Improve next code to: getPager().setParameterIfValueIsNotEmpty(RequestParam.CATEGORY_NAME, categoryName);
			if (!isBlank(categoryName))
			{
				getPager().setParameter(RequestParam.CATEGORY_NAME, categoryName);
			}
		}

		// TODO: What about MAX_RESULTS? Add pager? (mb)
		categories = categoryDao.getAllCategoriesByShopId(shopId);

		return RESULT_PRODUCTS;
	}

	private boolean requestedCertainShop()
	{
		return hasIdParam() || hasUrlNameParam();
	}

	private boolean hasIdParam()
	{
		return (shopId != null);
	}

	private boolean hasUrlNameParam()
	{
		return !isEmpty(urlName);
	}

	private Shop loadShop()
	{
		log.debug("loadShop()");
		log.debug("shopId: " + shopId);
		log.debug("urlName: " + urlName);

		if (hasIdParam())
		{
			return shopDao.get(shopId);
		}
		else if (hasUrlNameParam())
		{
			Shop shop = shopDao.getByUrlName(urlName);
			shopId = shop.getId();
			return shop;
		}
		else
		{
			return null;
		}
	}

	/** @deprecated Replace in all JSPs: "shopId" -> "id". */
	@Deprecated
	public Long getShopId()
	{
		return shopId;
	}

	/** @deprecated Replace in all JSPs: "shopId" -> "id". */
	@Deprecated
	public void setShopId(Long shopId)
	{
		this.shopId = shopId;
	}

	// TODO: Low: Leave only this method instead of setShopId
	public void setId(Long id)
	{
		shopId = id;
	}

	// TODO: Low: Leave only this method instead of getShopId
	public Long getId()
	{
		return shopId;
	}

	public String getUrlName()
	{
		return urlName;
	}

	public void setUrlName(String urlName)
	{
		this.urlName = urlName;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public Shop getShop()
	{
		return shop;
	}

	public List<Shop> getShops()
	{
		return shops;
	}

	public List<ProductVariant> getFeaturedProductVariants()
	{
		return featuredProductVariants;
	}

	public List<Product> getProducts()
	{
		return products;
	}

	public List<Category> getCategories()
	{
		return categories;
	}
}
