package seamshop.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.type.LongType;
import org.springframework.stereotype.Component;

import seamshop.model.Image;
import seamshop.model.ProductImage;
import seamshop.util.CollectionUtils;

@Component
@SuppressWarnings("unchecked")
public class ImageDao extends GenericDao<Image>
{
	public Image getTitleImageByProductId(Long productId)
	{
		Set<Long> productIds = new HashSet<Long>();
		productIds.add(productId);

		return getTitleImagesByProductIds(productIds).get(productId);
	}

	// TODO: Low: Refactor: Move to ProductDao?
	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	public Map<Long, Image> getTitleImagesByProductIds(Collection<Long> productIds)
	{
		if (CollectionUtils.isNullOrEmpty(productIds))
		{
			log.warn("productIds is null or empty");
			return new HashMap<Long, Image>();
		}

		String hql = "select pi.product.id, pi.image from " +
			ProductImage.class.getSimpleName() + " pi " +
			"where pi.product.id in (:productIds) and pi.number = minindex(pi.product.productImages)";

		List<Object[]> list = createQuery(hql)
			.setParameterList("productIds", productIds, new LongType())
			.list();

		Map<Long, Image> map = new HashMap<Long, Image>();
		for (Object[] name : list)
		{
			Object[] arr = name;
			map.put((Long) arr[0], (Image) arr[1]);
		}

		return map;
	}

	public List<Image> getImagesByProductId(Long productId)
	{
		Set<Long> productIds = new HashSet<Long>();
		productIds.add(productId);

		return getImagesByProductIds(productIds).get(productId);
	}

	// TODO: Normal: Optimize: Limit count of IDs to GenericDao.MAX_RESULTS_LIMIT.
	public Map<Long, List<Image>> getImagesByProductIds(Collection<Long> productIds)
	{
		if (CollectionUtils.isNullOrEmpty(productIds))
		{
			log.warn("productIds is null or empty");
			return new HashMap<Long, List<Image>>();
		}

		String hql = "select pi.product.id, pi.image from " +
			ProductImage.class.getSimpleName() + " pi " +
			"where pi.product.id in (:productIds) " +
			"order by pi.number";

		List<Object[]> list = createQuery(hql)
			.setParameterList("productIds", productIds, new LongType())
			.list();

		Map<Long, List<Image>> map = new HashMap<Long, List<Image>>();
		for (Object[] name : list)
		{
			Object[] arr = name;
			Long productId = (Long) arr[0];
			Image image = (Image) arr[1];

			List<Image> images = map.get(productId);
			if (images == null)
			{
				images = new ArrayList<Image>();
				map.put(productId, images);
			}
			images.add(image);
		}

		return map;
	}
}
