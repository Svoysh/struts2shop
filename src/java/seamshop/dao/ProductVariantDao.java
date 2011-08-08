package seamshop.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import seamshop.model.Image;
import seamshop.model.ProductVariant;
import seamshop.util.CollectionUtils;

/**
 * @author Alex Siman 2009-01-21
 */
@Component
@SuppressWarnings("unchecked")
public class ProductVariantDao extends GenericDao<ProductVariant>
{
	@Autowired
	protected ImageDao imageDao;

	public List<ProductVariant> getProductVariantsByProductId(Long productId)
	{
		Set<Long> productIds = new HashSet<Long>();
		productIds.add(productId);

		return getProductVariantsByProductIds(productIds).get(productId);
	}

	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	public Map<Long, List<ProductVariant>> getProductVariantsByProductIds(
		Collection<Long> productIds)
	{
		Map<Long, List<ProductVariant>> productIdAndProductVariants =
			new HashMap<Long, List<ProductVariant>>();
		if (CollectionUtils.isNullOrEmpty(productIds))
		{
			// Return empty collection.
			return productIdAndProductVariants;
		}

		String hql = "from ProductVariant " +
			"where product.id in (:productIds)" +
			"order by number";

		List<ProductVariant> allProductVariants = createQuery(hql)
			.setParameterList("productIds", productIds, new LongType())
			.list();

		if (!CollectionUtils.isNullOrEmpty(allProductVariants))
		{
			for (ProductVariant productVariant : allProductVariants)
			{
				Long productId = productVariant.getProduct().getId();
				List<ProductVariant> productVariantsByProductId =
					productIdAndProductVariants.get(productId);

				if (productVariantsByProductId == null)
				{
					productVariantsByProductId = new ArrayList<ProductVariant>();
					productIdAndProductVariants.put(productId, productVariantsByProductId);
				}
				productVariantsByProductId.add(productVariant);
			}
		}

		return productIdAndProductVariants;
	}

	public List<ProductVariant> getAllFeaturedProductsByShopId(Long shopId)
	{
		String hql = new StringBuilder()
			.append("from ").append(ProductVariant.class.getName()).append(" pv ")
			.append("join fetch pv.product p ")
			.append("join fetch p.shop s ")
			.append("where (s.id = :shopId) and (pv.featured = true) ")
			.append("order by pv.name")
			.toString();

		// TODO: What about MAX_RESULTS? Add pager? (mb)
		List<ProductVariant> productVariants = createQuery(hql)
			.setLong("shopId", shopId)
			.list();

//		log.debug("class of productVariants.get(0): " + productVariants.get(0).getClass());
		loadTiteImages(productVariants);

		return productVariants;
	}

	private void loadTiteImages(List<ProductVariant> productVariants)
	{
//		log.debug("class of productVariants.get(0): " + productVariants.get(0).getClass());

		Set<Long> productIds = new HashSet<Long>();
		for (ProductVariant productVariant : productVariants)
		{
			productIds.add(productVariant.getProduct().getId());
		}

		Map<Long, Image> productIdAndTitleImageMap = imageDao.getTitleImagesByProductIds(productIds);
		for (ProductVariant productVariant : productVariants)
		{
			Image titleImage = productIdAndTitleImageMap.get(productVariant.getProduct().getId());
			if (titleImage != null)
			{
				productVariant.setTitleImage(titleImage);
			}
		}
	}
}
