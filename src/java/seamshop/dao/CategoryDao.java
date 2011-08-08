package seamshop.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import seamshop.exception.NotImplementedException;
import seamshop.model.Category;
import seamshop.model.ProductCategory;
import seamshop.util.CollectionUtils;

@Component
@SuppressWarnings("unchecked")
public class CategoryDao extends GenericDao<Category>
{
	@Autowired
	protected ProductDao productDao;

	public Category getMyCategoryById(Long categoryId)
	{
		Category category = null;

		if (!hasCurrentUser())
		{
			return category;
		}

		if (categoryId == null)
		{
			log.warn("Input parameter 'categoryId' is null.");
			return category;
		}

		String hql = "from " +
			Category.class.getSimpleName() + " c " +
			"where (c.user.id = :userId) and (c.id = :categoryId)";

		category = (Category) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.setLong("categoryId", categoryId)
			.uniqueResult();

		if (category != null)
		{
			Long productCount = productDao.getProductCountByCategoryId(categoryId);
			category.setProductCount(productCount);
		};

		return category;
	}

	public Long countMyCategories()
	{
		Long count = 0L;
		if (!hasCurrentUser())
		{
			return count;
		}

		String hql =
			"select count(distinct c.id) " +
			"from " + Category.class.getName() + " c " +
			"where c.user.id = :userId";

		count = (Long) createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();

		return count;
	}

	/**
	 * Used to print list of categories for "New product" use-case.
	 */
	public List<Category> getAllMyCategories()
	{
		// TODO: Impl.
		throw new NotImplementedException();
	}

	public List<Category> getPageOfMyCategories()
	{
		if (!hasCurrentUser())
		{
			return new ArrayList<Category>();
		}

		String hql = "from " +
			Category.class.getSimpleName() + " c " +
			"where c.user.id = :userId " +
			"order by c.name";

		List<Category> categories = createPagedQuery(hql)
			.setLong("userId", getCurrentUserId())
			.list();

		if (CollectionUtils.isNullOrEmpty(categories))
		{
			return new ArrayList<Category>();
		}

		List<Long> categoryIds = DaoUtils.getIdsOfEntities(categories);
		Map<Long, Long> categoryIdsAndProductCounts =
			productDao.getProductCountsByCategoryIds(categoryIds);
		if (!CollectionUtils.isNullOrEmpty(categoryIdsAndProductCounts))
		{
			for (Category category : categories)
			{
				Long productCount = categoryIdsAndProductCounts.get(category.getId());
				if (productCount == null)
				{
					productCount = 0L;
				}
				category.setProductCount(productCount);
			}
		}

		return categories;
	}

	public List<String> getCategoryNamesByProductId(Long productId)
	{
		if (productId == null)
		{
			log.warn("Input parameter 'productId' is null.");
			return new ArrayList<String>();
		}

		String hql = "select pc.category.name from " +
			ProductCategory.class.getSimpleName() + " pc " +
			"where pc.product.id = :productId " +
			"order by pc.category.name";

		return createQuery(hql)
			.setLong("productId", productId)
			.list();
	}

	public Long countDistinctCategoriesNames()
	{
		String hql = "select count(distinct name) from " + Category.class.getName();
		Long count = (Long) createQuery(hql).uniqueResult();

		return count;
	}

	/**
	 * Return list of top categories rated by count of products.
	 * Category with most count is the first in list.
	 */
	public List<Category> getPageOfCategoriesOrderedByProductCount()
	{
		String hql =
			"select distinct c.name, count(pc.product.id) " +
			"from " + Category.class.getName() + " c " +
			"left join c.productCategories pc " +
			"group by c.name " +
			"order by count(pc.product.id) desc";

		List<Object[]> list = createPagedQuery(hql)
			.list();

		return convertToModel(list);
	}

	/*
	 * TODO: Maybe return only top ~20 categories for shop but no all categories?
	 * Or limit max results to 200 or 1000, etc.
	 */
	/**
	 * Return list of all product categories relate to this shop.
	 */
	public List<Category> getAllCategoriesByShopId(Long shopId)
	{
		if (shopId == null)
		{
			log.warn("Input parameter 'shopId' is null.");
			return new ArrayList<Category>();
		}

		String hql = "select pc.category.name, count(pc.product.id) from " +
			ProductCategory.class.getSimpleName() + " pc " +
			"where pc.product.shop.id = :shopId " +
			"group by pc.category.name " +
			"order by pc.category.name";

		// TODO: Order by product count? (no)
//			"order by count(pc.product.id) desc";

		// TODO: What about MAX_RESULTS? Add pager? (mb)
		List<Object[]> list = createQuery(hql)
			.setLong("shopId", shopId)
			.list();

		return convertToModel(list);
	}

	private List<Category> convertToModel(List<Object[]> list)
	{
		List<Category> categories = new ArrayList<Category>();
		if (CollectionUtils.isNullOrEmpty(list))
		{
			log.debug("Category list is empty");
			return categories;
		}

		log.debug("unique categries: " + list.size());
		for (Object[] nameAndProductCount : list)
		{
			log.debug(nameAndProductCount[0] + " : " + nameAndProductCount[1]);

			String categoryName = (String) nameAndProductCount[0];
			Long productCount = (Long) nameAndProductCount[1];

			Category category = new Category();
			category.setName(categoryName);
			category.setProductCount(productCount);

			categories.add(category);
		}

		return categories;
	}
}
