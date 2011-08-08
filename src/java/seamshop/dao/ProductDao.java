package seamshop.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import seamshop.model.Category;
import seamshop.model.Image;
import seamshop.model.Product;
import seamshop.model.ProductCategory;
import seamshop.model.ProductVariant;
import seamshop.model.Shop;
import seamshop.util.CollectionUtils;
import seamshop.util.StringUtils;

@Component
@SuppressWarnings("unchecked")
public class ProductDao extends GenericDao<Product>
{
	@Autowired
	protected CategoryDao categoryDao;

	@Autowired
	protected ImageDao imageDao;

	@Autowired
	protected OrderDao orderDao;

	@Autowired
	protected ProductVariantDao productVariantDao;

	@Autowired
	protected ShopDao shopDao;

	@Autowired
	protected UserDao userDao;

	public Long countMyProducts()
	{
		return countMyProductsByShopId(null);
	}

	public Long countMyProductsByShopId(Long shopId)
	{
		Long count = 0L;
		if (!hasCurrentUser())
		{
			return count;
		}

		String hql =
			"select count(distinct p.id) " +
			"from " + Product.class.getName() + " p " +
			"where (p.shop.user.id = :userId) " +
			(shopId == null ? "" : "and (p.shop.id = :shopId)");

		Query query = createQuery(hql)
			.setLong("userId", getCurrentUserId());
		if (shopId != null)
		{
			query.setLong("shopId", shopId);
		}
		count = (Long) query.uniqueResult();

		return count;
	}

	public List<Product> getPageOfMyProducts()
	{
		return getPageOfMyProductsByShopId(null);
	}

	public List<Product> getPageOfMyProductsByShopId(Long shopId)
	{
		if (!hasCurrentUser())
		{
			return new ArrayList<Product>();
		}

		String hql =
			"from " + Product.class.getName() + " p " +
			"where (p.shop.user.id = :userId) " +
			(shopId == null ? "" : "and (p.shop.id = :shopId) ") +
			"order by p.created desc";

		Query query = createPagedQuery(hql)
			.setLong("userId", getCurrentUserId());
		if (shopId != null)
		{
			query.setLong("shopId", shopId);
		}

		return query.list();
	}

	public Product getModelById(Long productId)
	{
		Product product = super.get(productId);
		return this.convertEntityToModel(product);
	}

	// TODO: Refactor, rename (e.g. "loadOptionalFields") and do not return value or remove at all.
	private Product convertEntityToModel(Product product)
	{
		Long productId = product.getId();

		//---------------------------------------------------------------------
		// Get product variants.
		List<ProductVariant> productVariants = productVariantDao
			.getProductVariantsByProductId(productId);
		if (!CollectionUtils.isNullOrEmpty(productVariants))
		{
			product.setProductVariants(productVariants);
		}

		//---------------------------------------------------------------------
		// Get image file names.
		List<Image> images = imageDao.getImagesByProductId(productId);
		if (!CollectionUtils.isNullOrEmpty(images))
		{
			// TODO: Set product.setImageModel(imageModel)?
			// First (list id = 0) image in list is the title image of product.
			product.setTitleImage(images.get(0));
			product.setImages(images);
		}

		//---------------------------------------------------------------------
		// Reset categories.
		product.setCategories(new ArrayList<Category>());
		// Get category names.
		List<String> categoryNames = categoryDao
			.getCategoryNamesByProductId(productId);
		if (!CollectionUtils.isNullOrEmpty(categoryNames))
		{
			for (String categoryName : categoryNames)
			{
				// TODO: Move to Category.generateModel().
				Category category = new Category();
				category.setName(categoryName);

				product.getCategories().add(category);
			}
		}

		return product;
	}

	// TODO: Refactor, rename (e.g. "loadOptionalFields") or remove at all.
	private List<Product> convertEntityToModel(List<Product> products)
	{
		if (CollectionUtils.isNullOrEmpty(products))
		{
			return products;
		}

		Set<Long> productIds = new HashSet<Long>();
		for (Product product : products)
		{
			productIds.add(product.getId());
		}

		//---------------------------------------------------------------------
		// Get products shops.
		Map<Long, Shop> shops = shopDao.getShopsByProductIds(productIds);

		//---------------------------------------------------------------------
		// Get MIN and MAX product variants.
		// TODO: Refactor: externalize to function "getMinAndMaxProductPrices()"?
		String hql = "select product.id, min(price), max(price) from " +
			ProductVariant.class.getSimpleName() + " " +
			"where product.id in (:productIds) " +
			"group by product.id";

		List<Object[]> list = createQuery(hql)
			.setParameterList("productIds", productIds, new LongType())
			.list();

		final String minPriceKey = "minPrice";
		final String maxPriceKey = "maxPrice";
		Map<Long, Map<String, BigDecimal>> productsPrices =
			new HashMap<Long, Map<String, BigDecimal>>();
		for (Object[] objects : list)
		{
			Long productId = (Long) objects[0];

			Map<String, BigDecimal> minAndMaxPrices = new HashMap<String, BigDecimal>();
			minAndMaxPrices.put(minPriceKey, (BigDecimal) objects[1]);
			minAndMaxPrices.put(maxPriceKey, (BigDecimal) objects[2]);

			productsPrices.put(productId, minAndMaxPrices);
		}

		//---------------------------------------------------------------------
		// Get title image file names.
		Map<Long, Image> images = imageDao.getTitleImagesByProductIds(productIds);

		for (Product product : products)
		{
			Long productId = product.getId();

			// Product's shop cannot be null so there is no need to check shop for null.
			Shop shop = shops.get(productId);
			product.setShop(shop);

			Map<String, BigDecimal> minAndMaxPrices = productsPrices.get(productId);
			if (!CollectionUtils.isNullOrEmpty(minAndMaxPrices))
			{
				product.setMinPrice(minAndMaxPrices.get(minPriceKey));
				product.setMaxPrice(minAndMaxPrices.get(maxPriceKey));
			}

			Image image = images.get(productId);
			if (image != null)
			{
				product.setTitleImage(image);
			}
		}

		return products;
	}

	/**
	 * Return page of {@link Product} entities selected by shop ID.
	 *
	 * @param page number of page, from 1 to n
	 * @param resultsPerPage count of fetched result products per page
	 */
	public List<Product> getPageOfProductsByShopId(Long shopId)
	{
		String hql = "from " + Product.class.getSimpleName() + " " +
			"where shop.id = :shopId " +
			"order by name";

		List<Product> products = createPagedQuery(hql)
			.setLong("shopId", shopId)
			.list();

		return this.convertEntityToModel(products);
	}

	/**
	 * Return page of {@link Product} entities selected by category name.
	 *
	 * @param page number of page, from 1 to n
	 * @param resultsPerPage count of fetched result products per page
	 */
	public List<Product> getPageOfProductsByCategoryName(String categoryName)
	{
		// TODO: Refactor?: "select product from ProductCategory where category.name = :categoryName order by product.name"
		String hql = "select product from " + Product.class.getSimpleName() + " product " +
			"join product.productCategories productCategory " +
			"where productCategory.category.name = :categoryName " +
			"order by product.name";

		List<Product> products = createPagedQuery(hql)
			.setString("categoryName", categoryName)
			.list();

		return this.convertEntityToModel(products);
	}

	// TODO: getPageOfProductsByShopIdAndCategoryName()

	public Long getProductCountByShopId(Long shopId)
	{
		if (shopId == null)
		{
			log.warn("Input parameter 'shopId' is null.");
			return 0L;
		}

		List<Long> shopIds = Arrays.asList(shopId);
		Long productCount = getProductCountsByShopIds(shopIds).get(shopId);
		if (productCount == null)
		{
			productCount = 0L;
		}

		return productCount;
	}

	/**
	 * @return Map&lt;shopId, productCount&gt;
	 */
	public Map<Long, Long> getProductCountsByShopIds(List<Long> shopIds)
	{
		Map<Long, Long> shopIdsAndProductCounts = new HashMap<Long, Long>();
		if (CollectionUtils.isNullOrEmpty(shopIds))
		{
			log.warn("shopIds is null or empty");
			return shopIdsAndProductCounts;
		}

		String hql = "select s.id, count(p.id) from " +
			Product.class.getSimpleName() + " p " +
			"join p.shop s " +
			"where s.id in (:shopIds) " +
			"group by s.id";

		List<Object[]> list = createQuery(hql)
			.setParameterList("shopIds", shopIds, new LongType())
			.list();

		/*
		 *  TODO: Refactor: The same construction (entity id and some value)
		 *  occurs very often in DAOs... Extract to some helper function? (y)
		 */
		for (Object[] shopIdAndProductCount : list)
		{
			Long shopId = (Long) shopIdAndProductCount[0];
			Long productCount = (Long) shopIdAndProductCount[1];
			shopIdsAndProductCounts.put(shopId, productCount);
		}

		return shopIdsAndProductCounts;
	}

	public Long getProductCountByCategoryId(Long categoryId)
	{
		if (categoryId == null)
		{
			log.warn("Input parameter 'categoryId' is null.");
			return 0L;
		}

		List<Long> categoryIds = Arrays.asList(categoryId);
		Long productCount = getProductCountsByCategoryIds(categoryIds).get(categoryId);
		if (productCount == null)
		{
			productCount = 0L;
		}

		return productCount;
	}

	/**
	 * @return Map&lt;categoryId, productCount&gt;
	 */
	public Map<Long, Long> getProductCountsByCategoryIds(List<Long> categoryIds)
	{
		Map<Long, Long> categoryIdsAndProductCounts = new HashMap<Long, Long>();
		if (CollectionUtils.isNullOrEmpty(categoryIds))
		{
			log.warn("categoryIds is null or empty");
			return categoryIdsAndProductCounts;
		}

		String hql = "select c.id, count(p.id) from " +
			ProductCategory.class.getSimpleName() + " pc " +
			"join pc.product p " +
			"join pc.category c " +
			"where c.id in (:categoryIds) " +
			"group by c.id";

		List<Object[]> list = createQuery(hql)
			.setParameterList("categoryIds", categoryIds, new LongType())
			.list();

		/*
		 *  TODO: Refactor: The same construction (entity id and some value)
		 *  occurs very often in DAOs... Extract to some helper function? (y)
		 */
		for (Object[] categoryIdAndProductCount : list)
		{
			Long categoryId = (Long) categoryIdAndProductCount[0];
			Long productCount = (Long) categoryIdAndProductCount[1];
			categoryIdsAndProductCounts.put(categoryId, productCount);
		}

		return categoryIdsAndProductCounts;
	}

	public Long getProductCountByCategoryName(String categoryName)
	{
		if (StringUtils.isNullOrEmpty(categoryName))
		{
			log.warn("categoryName is null or empty");
			return 0L;
		}

		String hql = "select count(*) from " +
			ProductCategory.class.getSimpleName() + " " +
			"where category.name = :categoryName";

		Long productCount = (Long) createQuery(hql)
			.setString("categoryName", categoryName)
			.uniqueResult();

		return productCount;
	}

	// TODO: getProductCountByShopIdAndCategoryName()

}
