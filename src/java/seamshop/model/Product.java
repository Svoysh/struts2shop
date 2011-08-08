package seamshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import seamshop.service.search.filter.AbstractCountryFilterFactory;
import seamshop.service.search.filter.ProductCountryFilterFactory;

/**
 * Name this domain object either "Item" (as in eBay.com) or "Product" (as in
 * Amazon.com).
 *
 * @author Alex Siman
 * @author Alex Siman 2009-07-21
 */
@Entity
@Indexed
@FullTextFilterDefs({
	@FullTextFilterDef(
		name = ProductCountryFilterFactory.FILTER_NAME,
		impl = ProductCountryFilterFactory.class)
})
@SuppressWarnings("serial")
public class Product extends AbstractHtmlDescribedEntity
{
	public static final String[] INDEXED_FIELDS = new String[] {
		"name", "summary", "textDescription", "shop.countryCode"
		/* TODO: Impl: "shop.user.id" */
		/* TODO: "productVariants.name", "shop.name", "categories.name"? (mb) */};

	/**
	 * If <code>true</code> then product will not be displayed within shop
	 * products. By default <code>= false</code>.
	 */
	// TODO: Implement. Use as filter in HQLs when fetching products.
	private boolean hidden = false;

	// TODO: Rename to "variants"?
	// TODO: Index product variants.
	@OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@IndexColumn(name = "number")
	private List<ProductVariant> productVariants = new ArrayList<ProductVariant>();

	@OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@IndexColumn(name = "number")
	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	// TODO: Must product contain Category.name in search index?
	@OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();

	// TODO: Low: Note: For now (2010-01-05) product needs only "shop.countryCode"
	//       to be indexed. But using "@IndexedEmbedded" makes indexed all
	//       fields from Shop that are indexed in Shop itself.
	@IndexedEmbedded(depth = 1)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shop_id", nullable = false)
	@ForeignKey(name = "fk_product_shop_id")
	private Shop shop = new Shop();

	// Helper fields ----------------------------------------------------------

	// TODO: Low: Is there real need in such transients like prices, currency?
	//       Maybe just call appropriate methods on associations every time?
	//       (y, if it not cost)

	@Transient
	private BigDecimal minPrice;

	@Transient
	private BigDecimal maxPrice;

	// TODO: Future: Impl:
//	private Double weight = null; // or 0.0? (n)

	// TODO: Impl: Make it persistent.
	@Transient
	private Image titleImage;

	// TODO: Need? Maybe not: images can be accessed successfully through "productImages".
	@Transient
	private List<Image> images = new ArrayList<Image>();

	// TODO: Make categories EAGER loaded? (y, if it will work)
	@Transient
	private List<Category> categories = new ArrayList<Category>();

	/*// TODO: To print ordered list of categories name.
	@Transient
	private List<String> categoriesNames = new ArrayList<String>();*/

	@Transient
	private String currency = null;

	public Product()
	{}

	@Override
	public String[] getIndexedFields()
	{
		return INDEXED_FIELDS;
	}

	public String getCurrency()
	{
		if (currency == null)
		{
			currency = shop.getCurrency();
		}

		return currency;
	}

	// Getters/Setters. -------------------------------------------------------

	public boolean getHidden()
	{
		return hidden;
	}

	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}

	public List<ProductVariant> getProductVariants()
	{
		return productVariants;
	}

	public void setProductVariants(List<ProductVariant> productVariants)
	{
		this.productVariants = productVariants;
	}

	public List<ProductImage> getProductImages()
	{
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages)
	{
		this.productImages = productImages;
	}

	public Set<ProductCategory> getProductCategories()
	{
		return productCategories;
	}

	public void setProductCategories(Set<ProductCategory> productCategories)
	{
		this.productCategories = productCategories;
	}

	public Shop getShop()
	{
		return shop;
	}

	public void setShop(Shop shop)
	{
		this.shop = shop;
	}

	public BigDecimal getMinPrice()
	{
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice)
	{
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice()
	{
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice)
	{
		this.maxPrice = maxPrice;
	}

	public Image getTitleImage()
	{
		return titleImage;
	}

	public void setTitleImage(Image titleImage)
	{
		this.titleImage = titleImage;
	}

	public List<Image> getImages()
	{
		return images;
	}

	public void setImages(List<Image> images)
	{
		this.images = images;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}
}
