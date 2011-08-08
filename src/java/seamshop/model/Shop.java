package seamshop.model;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static seamshop.model.validation.UrlNameValidationResult.OK;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

import seamshop.messages.CountryCollection;
import seamshop.messages.CurrencyCollection;
import seamshop.model.validation.UrlNameValidationResult;
import seamshop.model.validation.UrlNameValidator;
import seamshop.service.search.filter.AbstractCountryFilterFactory;
import seamshop.service.search.filter.ShopCountryFilterFactory;

/**
 * @author Alex Siman
 * @author Alex Siman 2009-12-30
 */
@Entity
@Indexed
@FullTextFilterDefs({
	@FullTextFilterDef(
		name = ShopCountryFilterFactory.FILTER_NAME,
		impl = ShopCountryFilterFactory.class)
})
@SuppressWarnings("serial")
public class Shop extends AbstractHtmlDescribedEntity
{
	public static final String[] INDEXED_FIELDS = new String[] {
		"name", "summary", "textDescription", "countryCode"
		/* TODO: Impl: "user.id" */};

	/**
	 * If <code>true</code> then this shop and shop categories and shop products
	 * that belong to this shop will not be displayed to clients. By default
	 * <code>= false</code>.
	 */
	// TODO: Implement. Use as filter in HQLs when fetching shop products.
	private boolean hidden = false;

	/**
	 * The name of the shop in URL. E.g. <code>http://example.com/s/<b>MyShop</b></code>
	 * where <code>MyShop</code> is <code>urlName</code>.
	 */
	// TODO: Make it case-sensitive? (n)
	@Column(unique = true, nullable = true, updatable = true)
	private String urlName;

	// TODO: Need???
	// Type of the shop: shop, store, services, real estates. Or another: products, services.
	// Something like "shop category".
//	private String type;

	/**
	 * Represents a currency for this shop products. Currencies are identified
	 * by their ISO 4217 3-letter currency codes. String length = 3 chars. Can
	 * NOT be <code>null</code>.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a>
	 */
	// TODO: Low: Rename to: currencyCode.
	// TODO: Autoset currency based on user IP and Google Geolocation.
	@Column(name = "currency_code", nullable = false, length = 3)
//	@Field(index = Index.TOKENIZED) // TODO: Need currencyCode in search index? (mb, not now)
	private String currency;

	/**
	 * 2-letter country code. String length = 2 chars.
	 * Users will be filtering shops by this country code.
	 *
	 * @see <a href="http://www.maxmind.com/app/iso3166">ISO 3166</a>
	 * @see <a href="http://www.iso.org/iso/english_country_names_and_code_elements">ISO 3166</a>
	 * @see <a href="http://en.wikipedia.org/wiki/ISO_3166">ISO 3166</a>
	 * @see <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a>
	 */
	// TODO: High: Autoset country by user IP and Google Geolocation.
	@Column(nullable = false, length = 2)
	@Field(index = Index.TOKENIZED)
	private String countryCode;

	// TODO: Impl: Maybe through ShopAddress.
//	@OneToMany(mappedBy = "shop", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	// TODO: Impl: Number, maybe through ShopAddress entity? (too complicated)
//	@IndexColumn(name = "number")
//	private List<Address> addresses = new ArrayList<Address>();

	/**
	 * Shop's logo.
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "logo_id")
	@ForeignKey(name = "fk_shop_logo_id")
	private Image logo;

	/**
	 * This shop products. @ContainedIn needed by @IndexedEmbedded to keep
	 * indexed fields from {@code Shop} in {@code Product} up to date.
	 */
	@ContainedIn
	@OneToMany(mappedBy = "shop", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Product> products = new HashSet<Product>();

	// TODO: Search: Index for seller search.
	@OneToMany(mappedBy = "shop", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>();

	// TODO: Search: Index for seller search.
	@OneToMany(mappedBy = "shop", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<PaymentMethod> paymentMethods = new ArrayList<PaymentMethod>();

	/** Countries where shop targets its buyers. */
	// TODO: High: Autoset country by user IP and Google Geolocation.
	@OneToMany(mappedBy = "shop", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<ShopShoppingCountry> shoppingCountries = new ArrayList<ShopShoppingCountry>();

	// TODO: Use as filter for member search.
	// TODO: Rename to "owner"/"creator"/"seller"? (mb)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_shop_user_id")
	private User user;

	// Transient --------------------------------------------------------------

	/**
	 * Count of products in this shop.
	 */
	// TODO: Set product count when listing shops.
	@Transient
	private Long productCount = 0L;

	@Transient
	private UrlNameValidationResult resultOfUrlNameValidation = OK;

	public Shop()
	{}

	@Override
	public String[] getIndexedFields()
	{
		return INDEXED_FIELDS;
	}

	public String getCountry()
	{
		return CountryCollection.getInstance().getNameByCode(countryCode);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("id=").append(getId()).append("; ")
				.append("name=").append(getName()).append("; ")
				.append("urlName=").append(urlName).append("; ")
				.append("countryCode=").append(countryCode).append("; ")
				.append("currencyCode=").append(currency)
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

	public String getUrlName()
	{
		return urlName;
	}

	public void setUrlName(String urlName)
	{
		if (null != urlName)
		{
			urlName = urlName.trim();
		}
		this.urlName = urlName;
	}

	public boolean hasUrlName()
	{
		return !isEmpty(getUrlName());
	}

	public boolean isValidUrlName()
	{
		resultOfUrlNameValidation = UrlNameValidator.isValidExt(urlName);
		return OK == resultOfUrlNameValidation;
	}

	public UrlNameValidationResult getResultOfUrlNameValidation()
	{
		return resultOfUrlNameValidation;
	}

	/**
	 * @deprecated See {@link #getCurrencyCode()}
	 */
	@Deprecated
	public String getCurrency()
	{
		return currency;
	}

	public String getCurrencyCode()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency.toUpperCase();
	}

	public boolean hasCurrencyCode()
	{
		return !isEmpty(currency);
	}

	public boolean isValidCurrencyCode()
	{
		return CurrencyCollection.getInstance().isValidCode(currency);
	}

	public boolean isValidCurrencyCodeIfHas()
	{
		if (hasCurrencyCode())
		{
			return isValidCurrencyCode();
		}
		else
		{
			return true;
		}
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode.toUpperCase();
	}

	public boolean hasCountryCode()
	{
		return !isEmpty(countryCode);
	}

	public boolean isValidCountryCode()
	{
		return CountryCollection.getInstance().isValidCode(countryCode);
	}

	public boolean isValidCountryCodeIfHas()
	{
		if (hasCountryCode())
		{
			return isValidCountryCode();
		}
		else
		{
			return true;
		}
	}

	/*public List<Address> getAddresses()
	{
		return addresses;
	}

	public void setAddresses(List<Address> addresses)
	{
		this.addresses = addresses;
	}*/

	public Image getLogo()
	{
		return logo;
	}

	public void setLogo(Image logo)
	{
		this.logo = logo;
	}

	public Set<Product> getProducts()
	{
		return products;
	}

	public void setProducts(Set<Product> products)
	{
		this.products = products;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public List<ShippingMethod> getShippingMethods()
	{
		return shippingMethods;
	}

	public void setShippingMethods(List<ShippingMethod> shippingMethods)
	{
		this.shippingMethods = shippingMethods;
	}

	public List<PaymentMethod> getPaymentMethods()
	{
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethod> paymentMethods)
	{
		this.paymentMethods = paymentMethods;
	}

	public List<ShopShoppingCountry> getShoppingCountries()
	{
		return shoppingCountries;
	}

	public void setShoppingCountries(List<ShopShoppingCountry> shoppingCountries)
	{
		this.shoppingCountries = shoppingCountries;
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
