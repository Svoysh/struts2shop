package seamshop.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Spring;
import seamshop.model.Category;
import seamshop.model.Product;
import seamshop.model.Shop;
import seamshop.model.User;
import seamshop.model.UserShoppingCountry;
import seamshop.service.search.ProductSearchParams;
import seamshop.service.search.SearchResult;
import seamshop.service.search.ShopSearchParams;

/**
 * @author Alex Siman 2009-09-07
 * @author Alex Siman 2009-12-30
 */
// TODO: Low: Search in users? (mb)
// TODO: Low: Highlight tokenized words.
@Component
@Scope(Spring.ACTION_SCOPE)
@SuppressWarnings("serial")
public class SearchAction extends AbstractGuestAction
{
	// TODO: Refactor next contsts to enum? (y, try)
	public static final String IN_PRODUCTS = "products";
	public static final String IN_SHOPS = "shops";
	public static final String IN_CATEGORIES = "categories";

	private String query;

	/**
	 * Where to search. Can contain such values as:
	 * <ul>
	 * <li>products</li>
	 * <li>shops</li>
	 * <li>categories</li>
	 * </ul>
	 */
	// TODO: Refactor to: "sections[]"? (y) NOTE: "s" at the end.
	private String section;

	private List<Product> products;
	private List<Shop> shops;
	private List<Category> categories;

	private List<String> countryNames;

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("query: " + query);
		log.debug("section: " + section);

		// TODO: Low: Usability: Save checked search sections in User preferences.

		/*if (CollectionUtils.isNullOrEmpty(in))
		{
			log.debug("Request array-parameter 'in' is empty.");
			return SUCCESS;
		}*/

		boolean searchForProducts = isSearchIn(IN_PRODUCTS);
		boolean searchForShops = isSearchIn(IN_SHOPS);

		int allResultSize = 0;
		int visibleResultSize = 0;

		List<String> countryCodes = new ArrayList<String>();
		if (searchForProducts || searchForShops)
		{
			List<UserShoppingCountry> shoppingCountries =
				userShoppingCountryDao.getAllByCurrentUser();
			User currentUser = getCurrentUser();
			currentUser.setShoppingCountries(shoppingCountries);
			countryCodes = currentUser.getShoppingCountryCodes();
			countryNames = countryCollection.getNamesByCodes(countryCodes);
			log.debug("Filter search by country codes: " + countryCodes);
		}

		// TODO: Search for ProductVariants not for Products
		//       it will allow to filter by price and stock availability.
		if (searchForProducts)
		{
			log.debug("Searching for products...");
			ProductSearchParams searchParams = new ProductSearchParams();
			searchParams.setCountryCodes(countryCodes);
			SearchResult<List<Product>> searchResult = productSearchService
				.searchFor(query, searchParams);
			products = searchResult.getResult();
			allResultSize = searchResult.getResultSize();
			visibleResultSize = products.size();
			log.debug("Found products: " + products);
		}

		if (searchForShops)
		{
			log.debug("Searching for shops...");
			ShopSearchParams searchParams = new ShopSearchParams();
			searchParams.setCountryCodes(countryCodes);
			SearchResult<List<Shop>> searchResult = shopSearchService
				.searchFor(query, searchParams);
			shops = searchResult.getResult();
			allResultSize = searchResult.getResultSize();
			visibleResultSize = shops.size();
			log.debug("Found shops: " + shops);
		}

		// TODO: High: Disable search for global categories 'til GlobalCategory
		//       is implemented. Because w/ simple search through categories
		//       there can be duplicates in search results.
		if (isSearchIn(IN_CATEGORIES))
		{
			// TODO: Low: Impl: Global category search based on GlobalCategory entity.
			// TODO: Impl: Search for products in certain category(es).
			// TODO: Impl: Search for member categories.
		}

		getPager().setAllResults(allResultSize);
		getPager().setResults(visibleResultSize);
		getPager().setUrl(getPagerUrl());
		getPager().setParameter("query", query);
		getPager().setParameter("section", section);

		return SUCCESS;
	}

	public boolean isSearchIn(String testIn)
	{
		boolean isCurrentSearchSection = getAction().equals("search")
			&& !isBlank(testIn) && testIn.equalsIgnoreCase(section);

//		log.debug("call isSearchIn(String)");
//		log.debug("testIn: " + testIn);
//		log.debug("section: " + section);
//		log.debug("result: " + isCurrentSearchSection);

		return isCurrentSearchSection;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	/** Shortcut for {@link #getQuery()}. */
	public String getQ()
	{
		return getQuery();
	}

	/** Shortcut for {@link #setQuery(String)}. */
	public void setQ(String query)
	{
		setQuery(query);
	}

	public String getSection()
	{
		return section;
	}

	public void setSection(String section)
	{
		this.section = section;
	}

	public List<Product> getProducts()
	{
		return products;
	}

	public List<Shop> getShops()
	{
		return shops;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public List<String> getCountryNames()
	{
		return countryNames;
	}
}
