package seamshop.action;

import java.util.ArrayList;
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

@Component
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class CategoryAction extends AbstractGuestAction
{
	public static final String RESULT_LIST = "list";
	public static final String RESULT_PRODUCTS = "products";

	/** Category name (URL parameter). */
	private String categoryName;

	private Category category;
	private List<Category> categories;
	private List<Product> products;

	@SkipValidation
	@Override
	public String execute()
	{
		return list();
	}

	/**
	 * Print list of categories ordered by product count.
	 */
	@AllowedMethod
	@SkipValidation
	public String list()
	{
		log.debug("call list()");

		Long categoryCount = categoryDao.countDistinctCategoriesNames();
		if (categoryCount > 0)
		{
			categories = categoryDao.getPageOfCategoriesOrderedByProductCount();
			getPager().setAllResults(categoryCount.intValue());
			getPager().setResults(categories.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_LIST;
	}

	/**
	 * Print list of products filtered by category name.
	 */
	/*
	 * TODO: Duplication: Refactor: Extract main part of method to ProductService?
	 * See [action.ShopAction.products()].
	 */
	@AllowedMethod
	@SkipValidation
	public String products()
	{
		log.debug("call products()");
		log.debug("categoryName: " + categoryName);

		boolean isEmptyCategory = isBlank(categoryName);
		if (isEmptyCategory)
		{
			// TODO: i18n: Externalize message.
			addActionError("Such category does not exist.");
			return ERROR;
		}

		Long productCount = productDao.getProductCountByCategoryName(categoryName);
		if (productCount > 0)
		{
			products = productDao.getPageOfProductsByCategoryName(categoryName);
			getPager().setAllResults(productCount.intValue());
			getPager().setResults(products.size());
			getPager().setUrl(getPagerUrl());

			// TODO: Improve next code to: getPager().setParameterIfValueIsNotEmpty(RequestParam.CATEGORY_NAME, categoryName);
			if (!isEmptyCategory)
			{
				getPager().setParameter(RequestParam.CATEGORY_NAME, categoryName);
			}
		}

		// Create anonymous category with requested category name.
		category = new Category(categoryName);
		category.setProductCount(productCount);

		categories = new ArrayList<Category>();
		categories.add(category);

		return RESULT_PRODUCTS;
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public Category getCategory()
	{
		return category;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public List<Product> getProducts()
	{
		return products;
	}
}
