package seamshop.actionutil;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static seamshop.util.UrlUtils.encodeUrl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.text.StrBuilder;

import seamshop.exception.NotImplementedException;
import seamshop.model.Product;
import seamshop.model.Shop;

/**
 * Helper class to build action URLs relative to request context path.
 *
 * @author Alex Siman 2009-12-11
 */
public class UrlBuilder
{
	/*public static class FullUrlBuilder extends UrlBuilder
	{
		public FullUrlBuilder(HttpServletRequest request)
		{
			super(request);
		}

		@Override
		public String getBase()
		{
			return super.getFullBase();
		}
	}*/

	public static final String SLASH = "/";

	public static final String PART_ID = "/id";
	public static final String PART_SHOP = "/s";
	public static final String PART_PRODUCT = "/p";

	private HttpServletRequest request;
//	private FullUrlBuilder full;

	public UrlBuilder(HttpServletRequest request)
	{
		if (null == request)
		{
			throw new NullArgumentException("request");
		}
		this.request = request;
//		full = new FullUrlBuilder(request);
	}

	/*public FullUrlBuilder getFull()
	{
		return full;
	}*/

	/**
	 * @return Protocol + domain + webapp context path.
	 * E.g. <code>http://example.com/webapp</code>
	 */
	public String getFullBase()
	{
		// TODO: Low: Perf: Cache result.
		return request.getRequestURL().toString().replaceFirst(
			request.getRequestURI(), request.getContextPath());
	}

	/**
	 * @return Only webapp context path. E.g. <code>/webapp</code>
	 */
	public String getBase()
	{
		return request.getContextPath();
	}

	public String getViewShopFullBase()
	{
		return new StrBuilder(getFullBase()).append(PART_SHOP).toString();
	}

	/**
	 * Possible URL schemes:
	 * <ul>
	 * <li><code>context/s/{shop.urlName}</code> if shop has URL name</li>
	 * <li><code>context/s/id/{shop.id}/{shop.name}</code></li>
	 * </ul>
	 */
	public String getViewShop(Shop shop)
	{
		if (null == shop)
		{
			throw new NullArgumentException("shop");
		}

		StringBuilder builder = new StringBuilder(getBase()).append(PART_SHOP);
		if (shop.hasUrlName())
		{
			// URL: context/s/{shop.urlName}
			builder.append(SLASH).append(shop.getUrlName());
		}
		else if (shop.hasId())
		{
			// URL: context/s/id/{shop.id}/{shop.name}
			builder.append(PART_ID).append(SLASH).append(shop.getId());
			String name = shop.getName();
			if (!isEmpty(name))
			{
				name = encodeUrl(name);
				builder.append(SLASH).append(name);
			}
		}
		else
		{
			throwFailedToBuildException("ViewShop", "Shop have neither ID nor URL name.");
		}

		return builder.toString();
	}

	/**
	 * Product name is UTF-8 encoded and used in URL just for SEO.
	 */
	public String getViewProduct(Product product)
	{
		// TODO: HTML escape product name? (y)
		// TODO: URL: context/p/id/{product.id}/{product.name}

		throw new NotImplementedException();
	}

	private void throwFailedToBuildException(String urlName, String reason)
	{
		throw new IllegalStateException("Failed to build URL: [" + urlName +
			"]; reason: [" + reason + "]");
	}
}
