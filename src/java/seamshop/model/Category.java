package seamshop.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.search.annotations.Indexed;

import seamshop.util.UrlUtils;

// TODO: Low: Rename to "UserCategory" and add GlobalCategory.
//       Can be used in different searches.
@Entity
@Indexed
@SuppressWarnings("serial")
public class Category extends AbstractNamedEntity implements Comparable<Category>
{
	/**
	 * If <code>true</code> then this category and all products that belong to
	 * this category will not be displayed within shop products. By default
	 * <code>= false</code>.
	 */
	// TODO: Implement. Use as filter in HQLs when fetching products (shops).
	private boolean hidden = false;

	// TODO: Rename to "owner"/"creator"/"seller"?
	/** Category's creator. */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_category_user_id")
	private User user;

	@OneToMany(mappedBy = "category")
	private Set<ProductCategory> productCategories;

	// Helper fields ----------------------------------------------------------

	/**
	 * Encoded category name that we can inject it URL.
	 * E.g. if category name = <code>"Category Name"</code> then
	 * encoded category name = <code>"Category+Name"</code> and URL can be for example:
	 * "http://site.com/some/category/Category+Name/"
	 */
	// TODO: Persist field? (n)
	// TODO: Deprecated? Remove? (maybe)
	@Transient
	private String urlName;

	/**
	 * Count of products in this category.
	 */
	@Transient
	private Long productCount = 0L;

	public Category()
	{
		super();
	}

	public Category(String name)
	{
		this();
		setName(name);
	}

	@Override
	public int compareTo(Category category)
	{
		if (equals(category))
		{
			return 0;
		}

		if ((getName() == null) && (category.getName() != null))
		{
			return -1;
		}
		else if (getName() != null)
		{
			return getName().compareTo(category.getName());
		}

		return -1;
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("id=").append(getId()).append("; ")
				.append("name=").append(getName()).append("; ")
				.append("productCount=").append(getProductCount())
			.append("}")
			.toString();
	}

	public boolean getHidden()
	{
		return hidden;
	}

	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Set<ProductCategory> getProductCategories()
	{
		return productCategories;
	}

	public void setProductCategories(Set<ProductCategory> productCategories)
	{
		this.productCategories = productCategories;
	}

	// TODO: Remove: need this func? (y) Consider Struts 2 <s:url> tag with default encoding.
	public String getUrlName()
	{
		/*
		 * TODO: What if after encoded URL name cached the category name changed?
		 * Encode on every call of func? (y)
		 */
		if (urlName == null)
		{
			// Encode category name that we can inject it into URL e.g.:
			// if category name = "Category Name" then
			// encoded category name = "Category+Name" and URL can be:
			// "http://site.com/some/category/Category+Name/"
			urlName = UrlUtils.encodeUrl(getName());
		}

		return urlName;
	}

	public Long getProductCount()
	{
		return productCount;
	}

	public void setProductCount(Long productCount)
	{
		this.productCount = productCount;
	}
}
